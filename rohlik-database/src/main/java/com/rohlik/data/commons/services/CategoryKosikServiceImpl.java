package com.rohlik.data.commons.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;
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
import com.rohlik.data.kosik.objects.NavigationItem;
import com.rohlik.data.kosik.objects.NavigationSubItem;
import com.rohlik.data.entities.Category;

@Service("CategoryKosikService")
@Transactional
public class CategoryKosikServiceImpl implements CategoryKosikService {
	private CategoryDao catDao;
	private CategoryService catService;
	private CategoryKosikDao catKosikDao;
	private NavigationBuilder navigationBuilder;
	private CategoryBuilder categoryBuilder;
	
	@Autowired
	public CategoryKosikServiceImpl(CategoryDao catDao, CategoryService catService, CategoryKosikDao catKosikDao,
			NavigationBuilder navigationBuilder, CategoryBuilder categoryBuilder) {
		this.catDao = catDao;
		this.catService = catService;
		this.catKosikDao = catKosikDao;
		this.navigationBuilder = navigationBuilder;
		this.categoryBuilder = categoryBuilder;
	}

	private static Logger log = LoggerFactory.getLogger(CategoryKosikServiceImpl.class);
	private static final String BASIC_URL = "https://www.kosik.cz";

	public CategoryKosik buildMainCategoryFromURI(String categoryURI) {
		return categoryBuilder.buildMainCategory(categoryURI);
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
		return navigationItem.getSubcategories().stream().filter(hasNoEquivalent(children)::test)
				.collect(Collectors.toList());
	}

	public List<ChildKosik> buildMissingChildrenOfCategory(String categoryURI) {
		List<NavigationSubItem> items = findMissingChildrenOfCategory(categoryURI);
		Function<NavigationSubItem, List<Category>> categoriesForComparison = item -> {
			CategoryKosik parent = catKosikDao.findByUri(item.getParentUri()).orElseGet(CategoryKosik::new);
			return new ArrayList<>(catService.findFirstLevelChildren(parent.getEquiId()));
		};
		return items.stream()
				.map(item -> buildChildFromNavigationSubItem(categoriesForComparison.apply(item), 0.45).apply(item))
				.collect(Collectors.toList());
	}

	
	private Predicate<NavigationSubItem> hasNoEquivalent(Set<ChildKosik> children) {
		return subitem -> children.stream().noneMatch(equalsWith(subitem));
	}

	private Predicate<ChildKosik> equalsWith(NavigationSubItem subitem) {
		return child -> Objects.equals(child.getCategoryName(), subitem.getCategoryName())
				&& Objects.equals(child.getParentUri(), subitem.getParentUri())
				&& Objects.equals(child.getUri(), subitem.getUri());
	}

	public void saveSecondLevelCategoriesWithChildrenBuiltFromURI(String firstLevelCategoryURI) {
		List<CategoryKosik> builtCategories = buildSecondLevelCategoriesWithChildrenFromURI(firstLevelCategoryURI);
		builtCategories.forEach(catKosikDao::save);
	}

	
	public List<CategoryKosik> buildSecondLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElseGet(CategoryKosik::new);
		return navigationBuilder.buildNavigationLevel().andThen(buildCategories(mainParent))
				.apply(firstLevelCategoryURI);
	}

	public List<CategoryKosik> buildThirdLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElseGet(CategoryKosik::new);
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
		return parent.orElseGet(CategoryKosik::new).getChildren().stream().filter(child -> uris.contains(child.getUri()))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	
	private Function<List<NavigationItem>, List<CategoryKosik>> buildCategories(CategoryKosik parent) {
		return navigationItems -> navigationItems.stream()
				.map(item -> categoryBuilder.buildCategoryWithChildrenFromChild(parent, item))
				.filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
	}

	public void saveUnsavedSecondLevelCategoriesWithChildrenBuiltFromURI(String topLevelCategoryURI) {
		List<CategoryKosik> builtCategories = buildUnsavedSecondLevelCategoriesWithChildrenFromURI(
				topLevelCategoryURI);
		builtCategories.stream().forEach(catKosikDao::save);
	}

	private List<CategoryKosik> buildUnsavedSecondLevelCategoriesWithChildrenFromURI(
			String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElseGet(CategoryKosik::new);
		return navigationBuilder.buildNavigationLevel().andThen(buildUnSavedCategories(mainParent))
				.apply(firstLevelCategoryURI);
	}

	private Function<List<NavigationItem>, List<CategoryKosik>> buildUnSavedCategories(CategoryKosik parent) {
		return navigationItems -> navigationItems.stream()
				.map(item -> categoryBuilder.buildCategoryWithChildrenFromChild(parent, item))
				.filter(Optional::isPresent).filter(isNotSavedWithSameName()::apply)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.collect(Collectors.toList());
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

	
	private BiPredicate<Optional<CategoryKosik>, Optional<CategoryKosik>> parentNamesEquals = (category,
			mainCategory) -> Objects.equals(category.map(CategoryKosik::getParentName),
					mainCategory.map(CategoryKosik::getParentName));		
		

	@Override
	public void saveThirdLevelCategoriesWithChildrenBuiltFromURI(String categoryURI) {
		List<CategoryKosik> builtCategories = buildThirdLevelCategoriesWithChildrenFromURI(categoryURI);
		builtCategories.forEach(catKosikDao::save);
	}

	@Override
	public List<CategoryKosik> buildFourthLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI) {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElseGet(CategoryKosik::new);
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
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(firstLevelCategoryURI).orElseGet(CategoryKosik::new);
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
		CategoryKosik category = catKosikDao.findByUriWithChildren(categoryURI).orElseGet(CategoryKosik::new);
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
