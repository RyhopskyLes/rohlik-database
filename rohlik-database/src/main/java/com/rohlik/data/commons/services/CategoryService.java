package com.rohlik.data.commons.services;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.NavSectionsCategoryData;

public interface CategoryService {
//public void saveAllCategoriesWithChildren();
public Set<Category> saveUnsavedCategories(Product product);
public void addMissingChildToParent(Integer childCategoryId);
public TreeMap<Integer, Category> findParentsUpToHighestParent(Integer categoryId);
//public void updateCategories();
public Set<Category> findChildrenToLowestLevel(Integer categoryId);
public Set<Category> findFirstLevelChildren(Integer categoryId);
public Set<Category> findAllActiveSubcategories(Integer categoryId);
public Set<NavSectionsCategoryData> findAllMissingSubcategories(Integer categoryId);
public Set<Category> buildAllMissingSubcategories(Integer categoryId);
//public Set<Child> saveAllMissingSubcategoriesAsChildren(Integer categoryId);
public Set<Category> saveAllMissingSubcategories(Integer categoryId);
public Set<Category> deactivateDeadSubcategories(Integer categoryId);
public void addMissingProductsToCategory(Integer categoryId);
public void updateActiveStateByAllChildren();
}
