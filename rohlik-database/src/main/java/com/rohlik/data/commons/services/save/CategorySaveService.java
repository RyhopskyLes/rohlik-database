package com.rohlik.data.commons.services.save;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.rohlik.data.entities.Category;

public interface CategorySaveService {
	Optional<Category> saveMainCategory(Integer categoryId);
	Optional<Category> saveMainCategoryWithChildren(Integer categoryId);
	List<Category> saveAllMainCategories();
	List<Category> saveAllMainCategoriesWithChildren();
	Optional<Category> saveCategory(Integer categoryId);
	Optional<Category> saveCategoryWithChildren(Integer categoryId);
	Map<Integer, Set<Category>> saveCompleteTreeOfMainCategory(Integer categoryId);
	Map<Integer, Set<Category>> saveCompleteTreeOfMainCategoryDownToLevel(Integer categoryId, int toLevel);
	Map<Integer, Set<Category>> saveCompleteTreeOfMainCategoryFromLevelToLevel(Integer categoryId, int fromLevel, int toLevel);
	Set<Category> saveLevelFromCompleteTreeOfMainCategory(Integer categoryId, int level);
	Map<Integer, Set<Category>> saveLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId);

}
