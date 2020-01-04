package com.rohlik.data.commons.services.build;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.NavSectionsCategoryData;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.NavigationCategoryInfo;

@Service("CategoryBuildService")
public class CategoryBuildServiceImpl implements CategoryBuildService {
	private static Logger logger = LoggerFactory.getLogger(CategoryBuildServiceImpl.class);
	private Navigation navigation;
	private NavSections navsections;

	private List<NavigationCategoryInfo> allCategoriesInfo;
	private List<Integer> allCategoriesId;
	private List<NavigationCategoryInfo> mainCategoriesInfo;
	private final Integer LEKARNA = 300112985;

	@Autowired
	public CategoryBuildServiceImpl(Navigation navigation, NavSections navsections) {
		super();
		this.navigation = navigation;
		this.navsections = navsections;
	}

	@PostConstruct
	public void initDB() {
		logger.info("Starting navigation loading...");
		allCategoriesInfo = navigation.getAllCategoriesData();
		allCategoriesId = allCategoriesInfo.stream().map(NavigationCategoryInfo::getCategoryId)
				.collect(Collectors.toCollection(ArrayList::new));
		mainCategoriesInfo = allCategoriesInfo.stream().filter(cat -> cat.getParentId() != null)
				.filter(cat -> cat.getParentId().equals(0)).collect(Collectors.toCollection(ArrayList::new));
		logger.info("Navigation loading finished.");
	}

	@Override
	public Optional<Category> buildMainCategory(Integer categoryId) {
		return getMainCategoryInfo(mainCategoriesInfo).apply(categoryId).map(NavigationCategoryInfo::toCategory);

	}

	private Function<Integer, Optional<NavigationCategoryInfo>> getMainCategoryInfo(
			List<NavigationCategoryInfo> categoriesInfo) {
		return categoryId -> categoriesInfo.stream()
				.filter(category -> Objects.equals(category.getCategoryId(), categoryId)).findFirst();
	}

	private Function<Integer, Optional<NavigationCategoryInfo>> getCategoryInfo(
			List<NavigationCategoryInfo> categoriesInfo) {
		return categoryId -> categoriesInfo.stream()
				.filter(category -> Objects.equals(category.getCategoryId(), categoryId)).findFirst();
	}

	@Override
	public Optional<Category> buildMainCategoryWithChildren(Integer categoryId) {
		Optional<NavigationCategoryInfo> mainCategoryInfo = getMainCategoryInfo(mainCategoriesInfo).apply(categoryId);
		List<Integer> children = mainCategoryInfo.map(NavigationCategoryInfo::getChildren).orElseGet(ArrayList::new);
		boolean isEmpty = children.stream().filter(id -> allCategoriesId.contains(id))
				.collect(Collectors.toCollection(ArrayList::new)).isEmpty();
		return isEmpty ? buildCategoryNotContainedInNavigationJsonWithChildren(categoryId)
				: convertToCategoryAndAddChildren(mainCategoryInfo);
	}

	private Optional<Category> convertToCategoryAndAddChildren(Optional<NavigationCategoryInfo> mainCategoryInfo) {
		Optional<Category> mainCategory = mainCategoryInfo.map(NavigationCategoryInfo::toCategory);
		List<Integer> childrenId = mainCategoryInfo.map(NavigationCategoryInfo::getChildren).orElseGet(ArrayList::new);
		allCategoriesInfo.stream().filter(info -> childrenId.contains(info.getCategoryId()))
				.map(NavigationCategoryInfo::toChild)
				.forEach(child -> mainCategory.ifPresent(category -> category.addChild(child)));
		return mainCategory;
	}

	@Override
	public List<Category> buildAllMainCategories() {
		return mainCategoriesInfo.stream().map(NavigationCategoryInfo::toCategory)
				.collect(Collectors.toCollection(ArrayList::new));

	}

	@Override
	public List<Category> buildAllMainCategoriesWithChildren() {
		return mainCategoriesInfo.stream().map(NavigationCategoryInfo::getCategoryId)
				.map(this::buildMainCategoryWithChildren).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public Optional<Category> buildCategory(Integer categoryId) {
		boolean isContained = allCategoriesId.contains(categoryId);
		return isContained
				? getCategoryInfo(allCategoriesInfo).apply(categoryId).map(NavigationCategoryInfo::toCategory)
				: buildCategoryNotContainedInNavigationJson(categoryId);
	}

	@Override
	public Optional<Category> buildCategoryWithChildren(Integer categoryId) {
		boolean isContained = allCategoriesId.contains(categoryId);
		Optional<NavigationCategoryInfo> categoryInfo = getCategoryInfo(allCategoriesInfo).apply(categoryId);
		boolean isNotEmpty = !categoryInfo.map(NavigationCategoryInfo::getChildren).orElseGet(ArrayList::new).isEmpty();
		return isContained && isNotEmpty ? convertToCategoryAndAddChildren(categoryInfo)
				: buildCategoryNotContainedInNavigationJsonWithChildren(categoryId);
	}

	@Override
	public Optional<Category> buildCategoryNotContainedInNavigationJson(Integer categoryId) {
		return navsections.getAsCategoryFromBreadCrumbs(categoryId);
	}

	@Override
	public Optional<Category> buildCategoryNotContainedInNavigationJsonWithChildren(Integer categoryId) {
		Optional<Category> category = navsections.getAsCategoryFromBreadCrumbs(categoryId);
		navsections.ofCategory(categoryId).stream().map(NavSectionsCategoryData::toChild)
				.forEach(child -> category.ifPresent(theCategory -> theCategory.addChild(child)));
		return category;
	}

	@Override
	public Map<Integer, Set<Category>> buildCompleteTreeOfMainCategory(Integer categoryId) {
		return buildCompleteTreeOfMainCategoryToLevel(categoryId, -1);
	}

	private Set<Child> addStartingCategoryToTreeAndReturnItsChildren(Map<Integer, Set<Category>> tree,
			Category category, int counter) {
		Set<Category> categoriesOnLevel = new HashSet<>();
		categoriesOnLevel.add(category);
		tree.put(counter, categoriesOnLevel);
		return category.getChildren();
	}

	private Set<Child> addAnotherLevelToTreeAndReturnChildrenCollection(Map<Integer, Set<Category>> tree,
			Set<Child> childrenOnLevel, int counter) {
		Set<Category> categoriesOnLevel = childrenOnLevel.stream().map(Child::getCategoryId)
				.map(this::buildCategoryWithChildren).filter(Optional::isPresent).map(Optional::get)
				.collect(Collectors.toCollection(HashSet::new));
		tree.put(counter, categoriesOnLevel);
		return categoriesOnLevel.stream().map(Category::getChildren).flatMap(Set::stream)
				.collect(Collectors.toCollection(HashSet::new));

	}

	private Map<Integer, Set<Category>> buildCompleteTreeOfMainCategoryToLevel(Integer categoryId, Integer level) {
		Map<Integer, Set<Category>> tree = new HashMap<>();
		Optional<Category> mainCategory = buildMainCategoryWithChildren(categoryId);
		int counter = 0;
		if (mainCategory.isPresent()) {
			Set<Child> childrenOnLevel = addStartingCategoryToTreeAndReturnItsChildren(tree, mainCategory.get(),
					counter);
			counter++;
			while (Objects.equals(level, -1) ? !childrenOnLevel.isEmpty()
					: !childrenOnLevel.isEmpty() && counter <= level) {
				childrenOnLevel = addAnotherLevelToTreeAndReturnChildrenCollection(tree, childrenOnLevel, counter);
				counter++;
			}
		}
		return tree;
	}

	@Override
	public Map<Integer, Set<Category>> buildCompleteTreeOfMainCategoryDownToLevel(Integer categoryId, int toLevel) {
		return toLevel >= 0 ? buildCompleteTreeOfMainCategoryToLevel(categoryId, toLevel) : new HashMap<>();

	}

	@Override
	public Map<Integer, Set<Category>> buildCompleteTreeOfMainCategoryFromLevelToLevel(Integer categoryId,
			int fromLevel, int toLevel) {
		Map<Integer, Set<Category>> result = new HashMap<>();
		if (fromLevel <= toLevel && fromLevel >= 0) {
			buildCompleteTreeOfMainCategoryToLevel(categoryId, toLevel).entrySet().stream()
					.filter(entry -> entry.getKey() >= fromLevel && entry.getKey() <= toLevel)
					.forEach(entry -> result.put(entry.getKey(), entry.getValue()));
		}
		return result;
	}

	@Override
	public Set<Category> buildLevelFromCompleteTreeOfMainCategory(Integer categoryId, int level) {
		Map<Integer, Set<Category>> result = new HashMap<>();
		if (level >= 0) {
			result = buildCompleteTreeOfMainCategoryToLevel(categoryId, level);
		}
		return result.containsKey(level) ? result.get(level) : new HashSet<>();
	}

	@Override
	public Map<Integer, Set<Category>> buildLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId) {
		Map<Integer, Set<Category>> result = new HashMap<>();
		Map<Integer, Set<Category>> completeTree = buildCompleteTreeOfMainCategoryToLevel(categoryId, -1);
		completeTree.entrySet().stream().filter(containsCategoryWithoutChildren::test).forEach(entry -> {
			Set<Category> lowest = collectCategoriesWithoutChildren.apply(entry.getValue());
			if (!lowest.isEmpty())
				result.put(entry.getKey(), lowest);
		});
		return result;
	}

	private Predicate<Entry<Integer, Set<Category>>> containsCategoryWithoutChildren = entry -> entry.getValue()
			.stream().map(Category::getChildren).anyMatch(Set::isEmpty);
	private UnaryOperator<Set<Category>> collectCategoriesWithoutChildren = set -> set.stream()
			.filter(category -> category.getChildren().isEmpty()).collect(Collectors.toCollection(HashSet::new));

	@Override
	public Map<Integer, Category> buildParentChainOfCategory(Integer categoryId) {
		Map<Integer, Category> chain = new HashMap<>();
		Optional<Category> category = buildCategory(categoryId);
		int counter = 0;
		if (category.isPresent()) {
			Integer parentId = category.get().getParentId();
			while (parentId != null && !parentId.equals(0)) {
				category = buildCategory(parentId);
				if (category.isPresent()) {
					counter++;
					chain.put(counter, category.get());
					parentId = category.get().getParentId();
				} else {
					parentId = null;
				}
			}
		}
		return chain;
	}

	@Override
	public Set<Category> buildFlattenedLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId) {
		return buildLowestLevelOfEachBranchOfMainCategoryTree(categoryId).values().stream().flatMap(Set::stream).collect(Collectors.toCollection(HashSet::new));
	}
}
