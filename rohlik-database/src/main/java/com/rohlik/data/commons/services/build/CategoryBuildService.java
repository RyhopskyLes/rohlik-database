package com.rohlik.data.commons.services.build;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.rohlik.data.entities.Category;

public interface CategoryBuildService {
Optional<Category> buildMainCategory(Integer categoryId);
Optional<Category> buildMainCategoryWithChildren(Integer categoryId);
List<Category> buildAllMainCategories();
List<Category> buildAllMainCategoriesWithChildren();
Optional<Category> buildCategory(Integer categoryId);
Optional<Category> buildCategoryWithChildren(Integer categoryId);
Optional<Category> buildCategoryNotContainedInNavigationJson(Integer categoryId);
Optional<Category> buildCategoryNotContainedInNavigationJsonWithChildren(Integer categoryId);
Map<Integer, Set<Category>> buildCompleteTreeOfMainCategory(Integer categoryId);
Map<Integer, Set<Category>> buildCompleteTreeOfMainCategoryDownToLevel(Integer categoryId, int toLevel);
Map<Integer, Set<Category>> buildCompleteTreeOfMainCategoryFromLevelToLevel(Integer categoryId, int fromLevel, int toLevel);
Set<Category> buildLevelFromCompleteTreeOfMainCategory(Integer categoryId, int level);
Map<Integer, Set<Category>> buildLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId);
Set<Category> buildFlattenedLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId);
Map<Integer, Category> buildParentChainOfCategory(Integer categoryId);
}
