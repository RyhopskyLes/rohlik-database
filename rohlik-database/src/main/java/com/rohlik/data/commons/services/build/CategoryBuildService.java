package com.rohlik.data.commons.services.build;

import java.util.List;
import java.util.Optional;

import com.rohlik.data.entities.Category;

public interface CategoryBuildService {
Optional<Category> buildMainCategory(Integer categoryId);
Optional<Category> buildMainCategoryWithChildren(Integer categoryId);
List<Category> buildAllMainCategories();
List<Category> buildAllMainCategoriesWithChildren();
}
