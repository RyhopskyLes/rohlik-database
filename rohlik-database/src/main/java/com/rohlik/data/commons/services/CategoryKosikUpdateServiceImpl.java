package com.rohlik.data.commons.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.entities.Category;
import com.rohlik.data.kosik.components.CategoryBuilder;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.Cancelled;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.kosik.objects.LinkAndName;
import com.rohlik.data.kosik.objects.NavigationItem;
import com.rohlik.data.kosik.objects.NavigationSubItem;

@Component
public class CategoryKosikUpdateServiceImpl implements CategoryKosikUpdateService {
	private CategoryKosikDao catKosikDao;
	private CategoryKosikOverview overView;
	private CategoryDao catDao;
	private CategoryService catService;
	private CategoryBuilder categoryBuilder;
	private NavigationBuilder navigationBuilder;

	private static final String BASIC_URL = "https://www.kosik.cz";

	@Override
	public Set<CategoryKosik> findCategoriesWithUnactiveEquivalent() {
		List<CategoryKosik> all = catKosikDao.findAllWithChildrenAndCategories();
		// to do
		return null;
	}

	@Override
	public Cancelled findCancelledCategories() {
		BiConsumer<Set<String>, Entry<String, Set<String>>> flattenToSet = (set, entry) -> {
			set.add(entry.getKey());
			set.addAll(entry.getValue());
		};

		List<CategoryKosik> categories = catKosikDao.findAllWithChildren();
		Set<ChildKosik> children = categories.stream().map(CategoryKosik::getChildren).flatMap(Set::stream)
				.collect(Collectors.toSet());

		Set<String> links = overView.mainCategoriesLinks().stream()
				.map(link -> overView.allLinksOfCategory(BASIC_URL + link)).flatMap(map -> map.entrySet().stream())
				.collect(HashSet::new, flattenToSet::accept, HashSet::addAll);

		List<ChildKosik> cancelledChildren = children.stream().filter(child -> !links.contains(child.getUri()))
				.collect(Collectors.toList());
		List<CategoryKosik> cancelledCategories = categories.stream()
				.filter(category -> !links.contains(category.getUri())).collect(Collectors.toList());

		return new Cancelled(cancelledCategories, cancelledChildren);
	}

	@Override
	public void fixWrongEquiNamesByChildren() {
		List<CategoryKosik> all = catKosikDao.findAllWithChildren();
		all.stream().map(category -> category.getChildren().stream()).map(children -> children.filter(equiIdNotNull))
				.map(children -> children.filter(namesNotEqual)).flatMap(Function.identity())
				.forEach(setEquiCategoryNameForChild::apply);

	}

	@Override
	public void addMissingEquiNamesAndIdsToChildren() {
		Function<ChildKosik, List<Category>> categoriesForComparison = child -> {
			CategoryKosik parent = catKosikDao.findByUri(child.getParentUri()).orElseGet(CategoryKosik::new);
			return new ArrayList<>(catService.findFirstLevelChildren(parent.getEquiId()));
		};
		UnaryOperator<ChildKosik> setEquiIdAndEquiName = child -> {
			List<Category> toCompare = categoriesForComparison.apply(child);
			return !toCompare.isEmpty() ? categoryBuilder.setChildPropertiesFromMatch(toCompare, 0.45).apply(child)
					: child;
		};

		List<CategoryKosik> all = catKosikDao.findAllWithChildren();
		all.stream().map(category -> category.getChildren().stream()).map(children -> children.filter(equiIdNull))
				.flatMap(Function.identity()).map(setEquiIdAndEquiName::apply).filter(equiIdNotNull)
				.collect(Collectors.toList());

	}

	@Override
	public Optional<CategoryKosik> updateParentUriByMainCategory(String categoryURI) {
		Optional<CategoryKosik> mainCategory = catKosikDao.findByUriWithChildren(categoryURI);
		mainCategory.ifPresent(category -> {
			category.setParentUri("");
			category.getChildren().forEach(child -> child.setParentUri(categoryURI));
		});
		return mainCategory;
	}

	@Override
	public void fixWrongEquiNames() {
		List<CategoryKosik> all = catKosikDao.findAll();
		all.stream().filter(isEquiIdNotNull).filter(areNamesNotEqual).map(CategoryKosik::getId)
				.map(id -> catKosikDao.findByIdWithChildren(id)).forEach(category -> category
						.ifPresent(setEquiCategoryNameForCategory.andThen(catKosikDao::save)::apply));

	}

	@Override
	public void addMissingEquiCategoryNamesToAllCategories() {
		List<CategoryKosik> all = catKosikDao.findAll();
		all.stream().filter(isEquiIdNotNull).filter(isEquiCategoryNameNull).map(CategoryKosik::getId)
				.map(id -> catKosikDao.findByIdWithChildren(id)).forEach(category -> category
						.ifPresent(setEquiCategoryNameForCategory.andThen(catKosikDao::save)::apply));

	}

	@Override
	public List<CategoryKosik> findCategoriesWithWrongEquiNames() {
		List<CategoryKosik> all = catKosikDao.findAll();
		return all.stream().filter(isEquiIdNotNull).filter(areNamesNotEqual).collect(Collectors.toList());
	}

	@Override
	public void updateUriBySecondLevelCategories(String firstLevelCategoryURI) {
		List<NavigationItem> navigationItems = navigationBuilder.buildLevel(firstLevelCategoryURI);
		getCategoryFromUri.andThen(category -> category.orElseGet(CategoryKosik::new).getCategoryName())
				.andThen(catKosikDao::findByParentName).andThen(updateUri(navigationItems))
				.apply(firstLevelCategoryURI);

	}

	@Override
	public void updateParentUriBySecondLevelCategories(String firstLevelCategoryURI) {
		getCategoryFromUri.andThen(category -> category.orElseGet(CategoryKosik::new).getCategoryName())
		.andThen(catKosikDao::findByParentName).andThen(updateParentUri(firstLevelCategoryURI))
		.apply(firstLevelCategoryURI);

	}

	@Override
	public void updateUriByChildrenOfCategory(String categoryUri) {
		Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(categoryUri);
		category.ifPresent(theCategory -> {
			NavigationItem item = navigationBuilder.buildItem(categoryUri);
			List<NavigationSubItem> subitems = item.getSubcategories();
			theCategory.getChildren().forEach(child -> {
				Optional<String> uri = getUri(child).apply(subitems);
				uri.ifPresent(theUri -> child.set(child::setUri, theUri));
			});
		});

	}

	@Override
	public void updateParentUriByChildrenOfCategory(String categoryUri) {
		Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(categoryUri);
		category.ifPresent(theCategory -> {
			theCategory.getChildren().forEach(child -> child.setParentUri(theCategory.getUri()));
			catKosikDao.save(theCategory);
		});

	}

	private Predicate<ChildKosik> equiIdNull = child -> child.getEquiId() == null;
	private Predicate<CategoryKosik> isEquiIdNotNull = category -> category.getEquiId() != null;
	private Function<CategoryKosik, Optional<String>> getEquivalentName = category -> {
		Optional<Category> rohlik = catDao.findByCategoryId(category.getEquiId());
		return rohlik.map(Category::getCategoryName);
	};

	private Predicate<CategoryKosik> areNamesNotEqual = category -> !Objects.equals(category.getEquiCategoryName(),
			getEquivalentName.apply(category).orElseGet(null));

	private Function<ChildKosik, Optional<String>> equivalentName = child -> {
		Optional<Category> rohlik = catDao.findByCategoryId(child.getEquiId());
		return rohlik.map(Category::getCategoryName);
	};
	private Predicate<ChildKosik> namesNotEqual = child -> !Objects.equals(child.getEquiCategoryName(),
			equivalentName.apply(child).orElse(null));

	private Predicate<ChildKosik> equiIdNotNull = child -> child.getEquiId() != null;

	private UnaryOperator<ChildKosik> setEquiCategoryNameForChild = child -> {
		Optional<String> name = equivalentName.apply(child);
		name.ifPresent(child::setEquiCategoryName);
		return child;
	};

	private UnaryOperator<CategoryKosik> setEquiCategoryNameForCategory = category -> {
		Optional<String> name = getEquivalentName.apply(category);
		name.ifPresent(category::setEquiCategoryName);
		return category;
	};
	private Predicate<CategoryKosik> isEquiCategoryNameNull = category -> category.getEquiId() != null;

	private Function<String, Optional<CategoryKosik>> getCategoryFromUri = uri -> uri.equals("undefined")
			? Optional.empty()
			: catKosikDao.findByUriWithChildren(uri);

	private UnaryOperator<List<CategoryKosik>> updateUri(List<NavigationItem> navigationItems) {
		return categories -> {
			categories
					.forEach(category -> uriForCategory().apply(category, navigationItems).ifPresent(category::setUri));
			return categories;
		};
	}
	
	private BiFunction<CategoryKosik, List<NavigationItem>, Optional<String>> uriForCategory() {
		BiPredicate<CategoryKosik, NavigationItem> hasSameName = (category, item) -> Objects
				.equals(item.getCategoryName(), category.getCategoryName());
		return (category, items) -> items.stream().filter(item -> hasSameName.test(category, item)).findFirst()
				.map(NavigationItem::getUri);
	}

	private UnaryOperator<List<CategoryKosik>> updateParentUri(String parentUri) {
		return categories -> {
			categories.forEach(category ->	category.setParentUri(parentUri));
			return categories;
		};
	}
	
	private Function<List<NavigationSubItem>, Optional<String>> getUri(ChildKosik child) {
		return subitems -> subitems.stream().filter(areEquivalent(child)).map(NavigationSubItem::getUri).findFirst();
	}
	
	private Predicate<NavigationSubItem> areEquivalent(ChildKosik child) {
		return subitem -> Objects.equals(child.getCategoryName(), subitem.getCategoryName())
				&& Objects.equals(child.getParentUri(), subitem.getParentUri());
	}

	@Override
	public List<ChildKosik> addMissingChildrenOfSubCategoriesToParentsInCategory(String categoryURI) {
		Map<LinkAndName, Set<LinkAndName>> allLinks = overView.allLinksAndNamesOnFirstLevel(BASIC_URL + categoryURI);
		// to do
		return null;
	}
}
