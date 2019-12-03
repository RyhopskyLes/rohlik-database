package com.rohlik.data.commons.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.kosik.components.CategoryBuilder;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.Cancelled;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.kosik.objects.LinkAndName;
import com.rohlik.data.kosik.objects.NavigationItem;
import com.rohlik.data.kosik.objects.NavigationSubItem;
import com.rohlik.data.entities.Category;

@Service("CategoryKosikService")
@Transactional
@SuppressWarnings("unchecked")
public class CategoryKosikServiceImpl implements CategoryKosikService {
	@Autowired
	CategoryDao catDao;
	@Autowired
	CategoryService catService;
	@Autowired
	CategoryKosikDao catKosikDao;
	@Autowired
	NavigationBuilder navigationBuilder;
	@Autowired
	CategoryKosikOverview review;
	@Autowired
	CategoryBuilder categoryBuilder;
	private static Logger log = LoggerFactory.getLogger(CategoryKosikServiceImpl.class);
	private static final String BASIC_URL = "https://www.kosik.cz";

	public CategoryKosik buildMainCategoryFromURI(String categoryURI) {
		return categoryBuilder.buildMainCategory(categoryURI);
	}

	public Optional<CategoryKosik> updateParentUriByMainCategory(String categoryURI) {
		Optional<CategoryKosik> mainCategory = catKosikDao.findByUriWithChildren(categoryURI);
		mainCategory.ifPresent(category -> {
			category.setParentUri("");
			category.getChildren().forEach(child -> child.setParentUri(categoryURI));
		});
		return mainCategory;
	}

	public CategoryKosik buildMainCategoryWithChildrenFromURI(String categoryURI) {
		return categoryBuilder.buildMainCategoryWithChildren(categoryURI);
	}

	public void saveMainCategoryWithChildren(String uri) {
		CategoryKosik mainParent = buildMainCategoryWithChildrenFromURI(uri);
		catKosikDao.save(mainParent);
	}

	public List<NavigationSubItem> findMissingChildrenOfCategory(String categoryURI) {
		Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(categoryURI);
		Set<ChildKosik> children = category.isPresent() ? category.get().getChildren() : new HashSet<>();
		NavigationItem navigationItem = navigationBuilder.buildItem(categoryURI);
		return navigationItem.getSubcategories().stream().filter(hasNoEquivalent(children))
				.collect(Collectors.toList());
	}

	public List<ChildKosik> buildMissingChildrenOfCategory(String categoryURI) {
		List<NavigationSubItem> items = findMissingChildrenOfCategory(categoryURI);
		Function<NavigationSubItem, List<Category>> categoriesForComparison = item -> {
			CategoryKosik parent = catKosikDao.findByUri(item.getParentUri()).orElse(new CategoryKosik());
			return new ArrayList<>(catService.findFirstLevelChildren(parent.getEquiId()));
		};
		return items.stream()
				.map(item -> buildChildFromNavigationSubItem(categoriesForComparison.apply(item), 0.45).apply(item))
				.collect(Collectors.toList());
	}

	public Cancelled findCancelledCategories() {
		BiConsumer<Set<String>, Entry<String, Set<String>>> flattenToSet = (set, entry) -> {
			set.add(entry.getKey());
			set.addAll(entry.getValue());
		};

		List<CategoryKosik> categories = catKosikDao.findAllWithChildren();
		Set<ChildKosik> children = categories.stream().map(CategoryKosik::getChildren).flatMap(set -> set.stream())
				.collect(Collectors.toSet());

		Set<String> links = review.mainCategoriesLinks().stream()
				.map(link -> review.allLinksOfCategory(BASIC_URL + link)).flatMap(map -> map.entrySet().stream())
				.collect(HashSet::new, flattenToSet::accept, HashSet::addAll);

		List<ChildKosik> cancelledChildren = children.stream().filter(child -> !links.contains(child.getUri()))
				.collect(Collectors.toList());
		List<CategoryKosik> cancelledCategories = categories.stream()
				.filter(category -> !links.contains(category.getUri())).collect(Collectors.toList());

		return new Cancelled(cancelledCategories, cancelledChildren);
	}

	private Predicate<NavigationSubItem> hasNoEquivalent(Set<ChildKosik> children) {
		return subitem -> children.stream().noneMatch(equalsWith(subitem));
	}

	private Predicate<ChildKosik> equalsWith(NavigationSubItem subitem) {
		return child -> Objects.equals(child.getCategoryName(), subitem.getCategoryName())
				&& Objects.equals(child.getParentUri(), subitem.getParentUri())
				&& Objects.equals(child.getUri(), subitem.getUri());
	}

	@Override
	public Set<CategoryKosik> findCategoriesWithUnactiveEquivalent() {
		List<CategoryKosik> all = catKosikDao.findAllWithChildrenAndCategories();
		// to do
		return null;
	}

	public void saveSecondLevelCategoriesWithChildrenBuiltFromURI(String firstLevelCategoryURI) {
		List<CategoryKosik> builtCategories = buildSecondLevelCategoriesWithChildrenFromURI(firstLevelCategoryURI);
		builtCategories.forEach(catKosikDao::save);
	}

	public void updateUriBySecondLevelCategories(String firstLevelCategoryURI) {
		List<NavigationItem> navigationItems = navigationBuilder.buildLevel(firstLevelCategoryURI);
		getCategoryFromUri.andThen(category -> category.orElse(new CategoryKosik()).getCategoryName())
				.andThen(catKosikDao::findByParentName).andThen(updateUri(navigationItems))
				.apply(firstLevelCategoryURI);
	}

	public void updateParentUriBySecondLevelCategories(String firstLevelCategoryURI) {
		getCategoryFromUri.andThen(category -> category.orElse(new CategoryKosik()).getCategoryName())
				.andThen(catKosikDao::findByParentName).andThen(updateParentUri(firstLevelCategoryURI))
				.apply(firstLevelCategoryURI);
	}

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

	private Function<List<NavigationSubItem>, Optional<String>> getUri(ChildKosik child) {
		return subitems -> subitems.stream().filter(areEquivalent(child)).map(NavigationSubItem::getUri).findFirst();
	}

	private Predicate<NavigationSubItem> areEquivalent(ChildKosik child) {
		return subitem -> Objects.equals(child.getCategoryName(), subitem.getCategoryName())
				&& Objects.equals(child.getParentUri(), subitem.getParentUri());
	}

	@Override
	public void updateParentUriByChildrenOfCategory(String categoryUri) {
		Optional<CategoryKosik> category = catKosikDao.findByUriWithChildren(categoryUri);
		category.ifPresent(theCategory -> {
			theCategory.getChildren().forEach(child -> child.setParentUri(theCategory.getUri()));
			catKosikDao.save(theCategory);
		});
	}

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

	private UnaryOperator<List<CategoryKosik>> updateParentUri(String parentUri) {
		return categories -> {
			categories.forEach(category ->	category.setParentUri(parentUri));
			return categories;
		};
	}

	private BiFunction<CategoryKosik, List<NavigationItem>, Optional<String>> uriForCategory() {
		BiPredicate<CategoryKosik, NavigationItem> hasSameName = (category, item) -> Objects
				.equals(item.getCategoryName(), category.getCategoryName());
		return (category, items) -> items.stream().filter(item -> hasSameName.test(category, item)).findFirst()
				.map(item -> item.getUri());
	}

	public List<CategoryKosik> buildSecondLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElse(new CategoryKosik());
		return navigationBuilder.buildNavigationLevel().andThen(buildCategories(mainParent))
				.apply(firstLevelCategoryURI);
	}

	public List<CategoryKosik> buildThirdLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElse(new CategoryKosik());
		Set<CategoryKosik> secondLevel = mainParent.getChildren().stream().map(ChildKosik::getUri)
				.map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		List<CategoryKosik> result = new ArrayList<>();
		secondLevel.stream().forEach(category -> {
			List<CategoryKosik> temp = navigationBuilder.buildNavigationLevel().andThen(buildCategories(category))
					.apply(category.getUri()).stream().filter(Objects::nonNull)
					.collect(Collectors.toCollection(ArrayList::new));
			result.addAll(temp);
		});
		return result;
	}

	@Override
	public List<ChildKosik> addMissingChildrenOfCategoryToParent(String categoryURI) {
		List<ChildKosik> missing = buildMissingChildrenOfCategory(categoryURI);
		missing.forEach(missed -> log.info("Missing: {}", missed));
		List<String> uris = missing.stream().map(ChildKosik::getUri)
				.collect(Collectors.toCollection(ArrayList::new));
		Optional<CategoryKosik> parent = catKosikDao.findByUriWithChildren(categoryURI);
		parent.ifPresent(theParent -> missing.forEach(theParent::addChildKosik));
		return parent.orElse(new CategoryKosik()).getChildren().stream().filter(child -> uris.contains(child.getUri()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public List<ChildKosik> addMissingChildrenOfSubCategoriesToParentsInCategory(String categoryURI) {
		Map<LinkAndName, Set<LinkAndName>> allLinks = review.allLinksAndNamesOnFirstLevel(BASIC_URL + categoryURI);
		// to do
		return null;
	}

	private Function<List<NavigationItem>, List<CategoryKosik>> buildCategories(CategoryKosik parent) {
		return navigationItems -> navigationItems.stream()
				.map(item -> categoryBuilder.buildCategoryWithChildrenFromChild(parent, item))
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	public void saveUnsavedSecondLevelCategoriesWithChildrenBuiltFromURI(String topLevelCategoryURI) {
		List<Optional<CategoryKosik>> builtCategories = buildUnsavedSecondLevelCategoriesWithChildrenFromURI(
				topLevelCategoryURI);
		builtCategories.stream().forEach(category -> category.ifPresent(catKosikDao::save));
	}

	private List<Optional<CategoryKosik>> buildUnsavedSecondLevelCategoriesWithChildrenFromURI(
			String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElse(new CategoryKosik());
		return navigationBuilder.buildNavigationLevel().andThen(buildUnSavedCategories(mainParent))
				.apply(firstLevelCategoryURI);
	}

	private Function<List<NavigationItem>, List<Optional<CategoryKosik>>> buildUnSavedCategories(CategoryKosik parent) {
		return navigationItems -> navigationItems.stream()
				.map(item -> categoryBuilder.buildCategoryWithChildrenFromChild(parent, item))
				.filter(Optional::isPresent).filter(isNotSavedWithSameName()::apply).collect(Collectors.toList());
	}

	Function<Optional<CategoryKosik>, Boolean> isNotSavedWithSameName() {
		BiPredicate<List<CategoryKosik>, Optional<CategoryKosik>> zeroWithSameParent = (categories,
				mainCategory) -> categories.stream()
						.noneMatch(category -> parentNamesEquals.test(Optional.ofNullable(category), mainCategory));
		BiPredicate<List<CategoryKosik>, Optional<CategoryKosik>> someWithDifferentParent = (categories,
				mainCategory) -> categories.stream()
						.anyMatch(category -> !parentNamesEquals.test(Optional.ofNullable(category), mainCategory));

		return category -> {
			List<CategoryKosik> categories = catKosikDao
					.findByCategoryName(category.map(CategoryKosik::getCategoryName).orElse(""));
			return categories.isEmpty() || someWithDifferentParent.and(zeroWithSameParent).test(categories, category);
		};
	}

	private Function<NavigationSubItem, ChildKosik> buildChildFromNavigationSubItem(List<Category> categories,
			Double dissimilarityLimit) {
		return item -> {
			ChildKosik child = new ChildKosik();
			child.set(child::setCategoryName, item.getCategoryName()).set(child::setUri, item.getUri())
					.set(child::setParentUri, item.getParentUri()).doIf(!categories.isEmpty(),
							categoryBuilder.setChildPropertiesFromMatch(categories, dissimilarityLimit));
			return child;
		};
	}

	@Override
	public List<CategoryKosik> findCategoriesWithWrongEquiNames() {
		List<CategoryKosik> all = catKosikDao.findAll();
		return all.stream().filter(isEquiIdNotNull).filter(areNamesNotEqual)
				.collect(Collectors.toList());		
	}

	@Override
	public void addMissingEquiCategoryNamesToAllCategories() {
		List<CategoryKosik> all = catKosikDao.findAll();
		all.stream().filter(isEquiIdNotNull).filter(isEquiCategoryNameNull).map(CategoryKosik::getId)
				.map(id -> catKosikDao.findByIdWithChildren(id)).forEach(category -> category
						.ifPresent(setEquiCategoryNameForCategory.andThen(catKosikDao::save)::apply));
	}

	@Override
	public void fixWrongEquiNames() {
		List<CategoryKosik> all = catKosikDao.findAll();
		all.stream().filter(isEquiIdNotNull).filter(areNamesNotEqual).map(CategoryKosik::getId)
				.map(id -> catKosikDao.findByIdWithChildren(id)).forEach(category -> category
						.ifPresent(setEquiCategoryNameForCategory.andThen(catKosikDao::save)::apply));
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
			CategoryKosik parent = catKosikDao.findByUri(child.getParentUri()).orElse(new CategoryKosik());
			return new ArrayList<>(catService.findFirstLevelChildren(parent.getEquiId()));
		};
		UnaryOperator<ChildKosik> setEquiIdAndEquiName = child -> {
			List<Category> toCompare = categoriesForComparison.apply(child);
			return !toCompare.isEmpty() ? categoryBuilder.setChildPropertiesFromMatch(toCompare, 0.45).apply(child)
					: child;
		};

		List<CategoryKosik> all = catKosikDao.findAllWithChildren();
		List<ChildKosik> withEquiIdSet = all.stream().map(category -> category.getChildren().stream())
				.map(children -> children.filter(equiIdNull)).flatMap(Function.identity())
				.map(setEquiIdAndEquiName::apply).filter(equiIdNotNull).collect(Collectors.toList());
		withEquiIdSet.forEach(child->log.info("EquiId was set by: {}", child));
		log.info("Size: {}", withEquiIdSet.size());
	}

	private BiPredicate<Optional<CategoryKosik>, Optional<CategoryKosik>> parentNamesEquals = (category,
			mainCategory) -> Objects.equals(category.map(CategoryKosik::getParentName),
					mainCategory.map(CategoryKosik::getParentName));

	private Predicate<CategoryKosik> isEquiCategoryNameNull = category -> category.getEquiId() != null;

	private Predicate<CategoryKosik> isEquiIdNotNull = category -> category.getEquiId() != null;
	private Predicate<ChildKosik> equiIdNotNull = child -> child.getEquiId() != null;
	private Predicate<ChildKosik> equiIdNull = child -> child.getEquiId() == null;
	private Function<CategoryKosik, Optional<String>> getEquivalentName = category -> {
		Optional<Category> rohlik = catDao.findByCategoryId(category.getEquiId());
		return rohlik.map(Category::getCategoryName);
	};
	private Function<ChildKosik, Optional<String>> equivalentName = child -> {
		Optional<Category> rohlik =catDao.findByCategoryId(child.getEquiId());
		return rohlik.map(Category::getCategoryName);
	};

	private Predicate<CategoryKosik> areNamesNotEqual = category -> !Objects.equals(category.getEquiCategoryName(),
			getEquivalentName.apply(category).orElse(null));
	private Predicate<ChildKosik> namesNotEqual = child -> !Objects.equals(child.getEquiCategoryName(),
			equivalentName.apply(child).orElse(null));

	private UnaryOperator<CategoryKosik> setEquiCategoryNameForCategory = category -> {
		Optional<String> name = getEquivalentName.apply(category);
		name.ifPresent(category::setEquiCategoryName);
		return category;
	};

	private UnaryOperator<ChildKosik> setEquiCategoryNameForChild = child -> {
		Optional<String> name = equivalentName.apply(child);
		name.ifPresent(child::setEquiCategoryName);
		return child;
	};

	@Override
	public void saveThirdLevelCategoriesWithChildrenBuiltFromURI(String categoryURI) {
		List<CategoryKosik> builtCategories = buildThirdLevelCategoriesWithChildrenFromURI(categoryURI);
		builtCategories.forEach(catKosikDao::save);
	}

	@Override
	public List<CategoryKosik> buildFourthLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElse(new CategoryKosik());
		Set<CategoryKosik> secondLevel = mainParent.getChildren().stream().map(ChildKosik::getUri)
				.map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		Set<CategoryKosik> thirdLevel = secondLevel.stream().map(CategoryKosik::getChildren).flatMap(Set::stream)
				.map(ChildKosik::getUri).map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		List<CategoryKosik> result = new ArrayList<>();
		thirdLevel.stream().forEach(category -> {
			List<CategoryKosik> temp = navigationBuilder.buildNavigationLevel().andThen(buildCategories(category))
					.apply(category.getUri()).stream().filter(Objects::nonNull)
					.collect(Collectors.toCollection(ArrayList::new));
			result.addAll(temp);
		});
		return result;
	}

	@Override
	public void saveFourthLevelCategoriesWithChildrenBuiltFromURI(String categoryURI) {
		List<CategoryKosik> builtCategories = buildFourthLevelCategoriesWithChildrenFromURI(categoryURI);
		builtCategories.forEach(catKosikDao::save);
	}

	@Override
	public List<CategoryKosik> buildFifthLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElse(new CategoryKosik());
		Set<CategoryKosik> secondLevel = mainParent.getChildren().stream().map(ChildKosik::getUri)
				.map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		Set<CategoryKosik> thirdLevel = secondLevel.stream().map(CategoryKosik::getChildren).flatMap(Set::stream)
				.map(ChildKosik::getUri).map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		Set<CategoryKosik> fourthLevel = thirdLevel.stream().map(CategoryKosik::getChildren).flatMap(Set::stream)
				.map(ChildKosik::getUri).map(catKosikDao::findByUri).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));

		List<CategoryKosik> result = new ArrayList<>();
		fourthLevel.stream().forEach(category -> {
			List<CategoryKosik> temp = navigationBuilder.buildNavigationLevel().andThen(buildCategories(category))
					.apply(category.getUri()).stream().filter(Objects::nonNull)
					.collect(Collectors.toCollection(ArrayList::new));
			result.addAll(temp);
		});
		return result;
	}

	@Override
	public void saveFifthLevelCategoriesWithChildrenBuiltFromURI(String categoryURI) {
		List<CategoryKosik> builtCategories = buildFifthLevelCategoriesWithChildrenFromURI(categoryURI);
		builtCategories.forEach(catKosikDao::save);

	}

	@Override
	public Set<CategoryKosik> getLowestLevelCategoriesInTreeOf(String categoryURI) {
		CategoryKosik category = catKosikDao.findByUriWithChildren(categoryURI).orElse(new CategoryKosik());
		Set<CategoryKosik> result = new HashSet<>();
		Set<ChildKosik> children = category.getChildren();
		while (!children.isEmpty()) {
			Set<CategoryKosik> newLevel = children.stream().map(ChildKosik::getUri).map(catKosikDao::findByUri)
					.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toCollection(HashSet::new));
			newLevel.stream().filter(cat -> cat.getChildren().isEmpty()).forEach(result::add);
			children = newLevel.stream().map(CategoryKosik::getChildren).flatMap(Set::stream)
					.collect(Collectors.toCollection(HashSet::new));
		}
		return result;
	}

	@Override
	public TreeMap<Integer, CategoryKosik> findAllParentCategoriesUpToTheHighestParent(String categoryURI) {
		TreeMap<Integer, CategoryKosik> parentsMap = new TreeMap<>();
		Optional<CategoryKosik> child = catKosikDao.findByUri(categoryURI);
		if (child.isPresent()) {
			
			String parentUri = child.get().getParentUri();
			Integer key = 1;
			while (!parentUri.equals("")) {
				Optional<CategoryKosik> parent = catKosikDao.findByUri(parentUri);
				if(parent.isPresent()) {
				parentsMap.put(key, parent.get());
				parentUri = parent.get().getParentUri();
				} else {return parentsMap;}
				key++;				
			}
		}
		return parentsMap;		
	}

		@Override
	public void addCategorytoCategoryKosik(Integer idCategoryKosik, Integer categoryIdCategoryRohlik, boolean setParameters) {
			CategoryKosik kosik = catKosikDao.findByIdWithCategories(idCategoryKosik).orElseGet(()->null);
			
			if(kosik!=null)
			{Category rohlik = catDao.findByCategoryIdWithCategoriesKosik(categoryIdCategoryRohlik);
			if(setParameters) {
			kosik.setEquiCategoryName(rohlik.getCategoryName());
			kosik.setEquiId(rohlik.getCategoryId());
			kosik.setEquiParentId(rohlik.getParentId());}
			kosik.addCategory(rohlik);
			log.info("Added: {}", rohlik);
			catKosikDao.save(kosik);}
		
	}

		@Override
		public void removeCategoryFromCategoryKosik(Integer idCategoryKosik, Integer categoryIdCategoryRohlik) {
			CategoryKosik kosik = catKosikDao.findByIdWithCategories(idCategoryKosik).orElseGet(()->null);
			if(kosik!=null)
			{Category rohlik = catDao.findByCategoryIdWithCategoriesKosik(categoryIdCategoryRohlik);			
			kosik.removeCategory(rohlik);
			log.info("Removed: {}", rohlik);
			catKosikDao.save(kosik);}
		}

}
