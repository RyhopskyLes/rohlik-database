package com.rohlik.data.commons.services.build;

import java.util.List;
import java.util.Optional;

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
}
