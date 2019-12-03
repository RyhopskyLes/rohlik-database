package com.rohlik.data.kosik.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.objects.Result;
import com.rohlik.data.commons.services.CategoryService;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.CategoryMatcher;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;
import com.rohlik.data.kosik.objects.NavigationItem;

@Component("categoryBuilder")
public class CategoryBuilder {
	@Autowired
	private CategoryDao catDao;
	@Autowired
	CategoryService catService;
	@Autowired
	CategoryKosikOverview review;
	@Autowired
	NavigationBuilder navigationBuilder;
	@Autowired
	CategoryMatcher matcher;
	private List<Category> all;
	private List<Category> main;
	private static Logger log = LoggerFactory.getLogger(CategoryBuilder.class);
	private static final String BASIC_URL = "https://www.kosik.cz";

	public CategoryBuilder() {
		// No-args constructor required by *Spring*
	}

	@PostConstruct
	public void init() {
		all = catDao.findAllWithChildrenAndCategoriesKosikAndProducts().stream()
				.filter(cat -> cat.getActive().equals(true)).collect(Collectors.toCollection(ArrayList::new));
		main = all.stream().filter(cat -> cat.getParentId() == 0 && cat.getActive().equals(true))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public CategoryKosik buildMainCategory(String categoryURI) {
		CategoryKosik mainCategory = new CategoryKosik();
		mainCategory.set(mainCategory::setUri, categoryURI)
		.set(mainCategory::setActive, true)
		.set(mainCategory::setCategoryName,
				review.getCurrentMainCategoryName(BASIC_URL + categoryURI).orElse(null));
		setCategoryPropertiesFromMatch(main, 0.45).apply(mainCategory);
		return mainCategory.set(mainCategory::setParentName, "root").set(mainCategory::setParentUri, "");
	}

	public CategoryKosik buildMainCategoryWithChildren(String categoryURI) {
		CategoryKosik mainCategory = buildMainCategory(categoryURI);
		navigationBuilder.buildNavigationLevel().andThen(buildChildrenFromNavigation(mainCategory))
				.andThen(addChildrenToCategory(mainCategory)).apply(categoryURI);
		return mainCategory;
	}

	public Optional<CategoryKosik> buildCategoryWithChildrenFromChild(CategoryKosik parent,
			NavigationItem navigationItem) {
		Function<CategoryKosik, Map<String, ChildKosik>> transformSetOfChildrenToMap = category -> category
				.getChildren().stream().collect(Collectors.toMap(ChildKosik::getCategoryName, Function.identity()));
		Map<String, ChildKosik> children = transformSetOfChildrenToMap.apply(parent);
		Optional<ChildKosik> child = navigationItem != null
				? Optional.ofNullable(children.get(navigationItem.getCategoryName()))
				: Optional.empty();
		if (child.isPresent()) {
			CategoryKosik toBuild = buildCategoryFromChild(child).apply(CategoryKosik::new);
			buildChildrenFromSubItems(navigationItem, 0.45).andThen(addChildrenToCategory(toBuild))
					.andThen(addEquivalentCategory(child)).apply(toBuild);
			return Optional.ofNullable(toBuild);
		}
		return Optional.empty();
	}

	private UnaryOperator<CategoryKosik> addEquivalentCategory(Optional<ChildKosik> child) {
		return category -> {
			Category rohlik = all.stream().filter(cat -> cat.getCategoryId().equals(child.get().getEquiId()))
					.findFirst().orElse(null);
			addEquivalentsBasedOnProductMatch(category);
		/*	List<Result<Category>> byProducts = matcher.findMatchBasedOnProducts(category, preFilterCategoriesForMatching(category));
			Result<Category> min = byProducts.isEmpty() ? new Result<Category>() : byProducts.get(0);
			System.out.println(min);
			Integer equiId = min.getEntity().orElse(new Category()).getCategoryId();
			String equiCategoryName = min.getEntity().orElse(new Category()).getCategoryName();
			byProducts.stream().map(Result::getEntity).filter(Optional::isPresent).map(Optional::get)
					.forEach(roh -> category.addCategory(roh));			
			if (category.getEquiId() == null) {
				category.setEquiId(equiId);
				category.setEquiCategoryName(equiCategoryName);
			}*/
			
			category.addCategory(rohlik);
			return category;
		};
	}

	List<Category> preFilterCategoriesForMatching(CategoryKosik category) {
		List<Result<Category>> contained = matcher.preMatchBasedOnProducts(category, main);
		List<Category> selected =contained.stream().map(res -> res.getEntity()).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(ArrayList::new));
		List<Category> result =selected.stream().map(cat->findChildrenToLowestLevel(cat.getCategoryId())).flatMap(Set::stream)
		.collect(Collectors.toCollection(ArrayList::new));
		result.addAll(selected);
		return result;
	}

	private Function<CategoryKosik, List<ChildKosik>> buildChildrenFromSubItems(NavigationItem item,
			Double dissimilarityLimit) {
		return category -> StreamSupport.stream(item.getSubcategories().spliterator(), false).map(subItem -> {
			List<Category> categories = childrenCategoriesFromRohlik(category.getEquiId());
			ChildKosik child = new ChildKosik();
			child.setActive(true);
			BeanUtils.copyProperties(subItem, child);
			child.doIf(!categories.isEmpty(), setChildPropertiesFromMatch(categories, dissimilarityLimit));
			return child;
		}).collect(Collectors.toCollection(ArrayList::new));

	}

	private List<Category> childrenCategoriesFromRohlik(Integer categoryEquiId) {
		return categoryEquiId != null ? new ArrayList<>(catService.findChildrenToLowestLevel(categoryEquiId))
				: new ArrayList<>();
	}

	public UnaryOperator<ChildKosik> setChildPropertiesFromMatch(List<Category> categories, Double dissimilarityLimit) {
		return child -> {
			Result<Category> result = matcher.findMatch(new CategoryKosik(child.getCategoryName()), categories);
			Optional<Category> category = result.getEntityForLimit(dissimilarityLimit);
			category.ifPresent(theCategory -> {
				child.set(child::setEquiId, theCategory.getCategoryId()).set(child::setEquiCategoryName,
						theCategory.getCategoryName());
			});
			return child;
		};
	}

	private UnaryOperator<CategoryKosik> setCategoryPropertiesFromMatch(List<Category> categories,
			Double dissimilarityLimit) {
		return mainCategory -> {			
			Result<Category> result = matcher.findMatch(mainCategory, categories);
			Optional<Category> category = result.getEntityForLimit(dissimilarityLimit);
			category.ifPresent(theCategory -> {
				mainCategory.set(mainCategory::setEquiId, theCategory.getCategoryId())
						.set(mainCategory::setEquiCategoryName, theCategory.getCategoryName())
						.set(mainCategory::setEquiParentId, theCategory.getParentId());
				mainCategory.addCategory(theCategory);
			});
			return mainCategory;
		};
	}

	private Function<Supplier<CategoryKosik>, CategoryKosik> buildCategoryFromChild(Optional<ChildKosik> child) {
		return supplier -> {
			CategoryKosik toBuild = supplier.get();
			String[] ignoreProperties = { "id" };
			if (child.isPresent()) {
				BeanUtils.copyProperties(child.get(), toBuild, ignoreProperties);
			}
			return toBuild;
		};
	}

	private Function<List<ChildKosik>, CategoryKosik> addChildrenToCategory(CategoryKosik category) {
		return children -> {
			children.forEach(category::addChildKosik);
			return category;
		};
	}

	private Function<List<NavigationItem>, List<ChildKosik>> buildChildrenFromNavigation(CategoryKosik parent) {
		List<Category> categories = childrenCategoriesFromRohlik(parent.getEquiId());
		return navigationItems -> navigationItems.stream().map(item -> {
			ChildKosik child = new ChildKosik();
			BeanUtils.copyProperties(item, child);
			child.setActive(true);
			child.doIf(!categories.isEmpty(), setChildPropertiesFromMatch(categories, 0.45)).set(child::setParentUri,
					parent.getUri());
			return child;
		}).collect(Collectors.toList());
	}
	
	public Set<Category> findChildrenToLowestLevel(Integer categoryId) {
		Category category = all.stream().filter(cat->cat.getCategoryId().equals(categoryId)).findFirst().orElse(new Category());
		Set<Category> children = new HashSet<>();
		if (category != null) {
			Set<Child> directChildren = category.getChildren();
			while (!directChildren.isEmpty()) {
				Set<Child> tempChildren = new HashSet<>();
				for (Child directChild : directChildren) {
					Integer childId = directChild.getCategoryId();
					Category child = all.stream().filter(cat->cat.getCategoryId().equals(childId)).findFirst().orElse(new Category());					
					if(child!=null) {tempChildren.addAll(child.getChildren());
					children.add(child);};
				}
				directChildren = tempChildren;
			}
		}		
		return children;
	}
	
	private void addEquivalentsBasedOnProductMatch(CategoryKosik category) {
		List<Result<Category>> byProducts = matcher.findMatchBasedOnProducts(category, preFilterCategoriesForMatching(category));
		Result<Category> min = byProducts.isEmpty() ? new Result<>() : byProducts.get(0);
		Integer equiId = min.getEntity().orElse(new Category()).getCategoryId();
		String equiCategoryName = min.getEntity().orElse(new Category()).getCategoryName();
		byProducts.stream().map(Result::getEntity).filter(Optional::isPresent).map(Optional::get)
				.forEach(category::addCategory);
		if (category.getEquiId() == null) {
			category.setEquiId(equiId);
			category.setEquiCategoryName(equiCategoryName);
		}
	}
}
