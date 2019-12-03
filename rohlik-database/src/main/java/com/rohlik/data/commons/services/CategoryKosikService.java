package com.rohlik.data.commons.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.objects.Cancelled;
import com.rohlik.data.kosik.objects.NavigationSubItem;

public interface CategoryKosikService {
public CategoryKosik buildMainCategoryFromURI(String categoryURI);	
public CategoryKosik buildMainCategoryWithChildrenFromURI(String categoryURI);
public Optional<CategoryKosik> updateParentUriByMainCategory(String categoryURI);
public void saveMainCategoryWithChildren(String categoryURL);
public void saveSecondLevelCategoriesWithChildrenBuiltFromURI(String categoryURI);
public void saveThirdLevelCategoriesWithChildrenBuiltFromURI(String categoryURI);
public void saveUnsavedSecondLevelCategoriesWithChildrenBuiltFromURI(String topLevelCategoryURI);
public List<CategoryKosik> findCategoriesWithWrongEquiNames();
public void fixWrongEquiNames();
public void addMissingEquiCategoryNamesToAllCategories();
public List<CategoryKosik> buildSecondLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI);
public void updateUriBySecondLevelCategories(String firstLevelCategoryURI);
public void updateParentUriBySecondLevelCategories(String firstLevelCategoryURI);
public void updateUriByChildrenOfCategory(String categoryUri);
public void updateParentUriByChildrenOfCategory(String categoryUri);
public List<NavigationSubItem> findMissingChildrenOfCategory(String categoryURI);
public List<ChildKosik> buildMissingChildrenOfCategory(String categoryURI);
public List<ChildKosik> addMissingChildrenOfCategoryToParent(String categoryURI);
public List<ChildKosik> addMissingChildrenOfSubCategoriesToParentsInCategory(String categoryURI);
public Cancelled findCancelledCategories();
public void fixWrongEquiNamesByChildren();
public void addMissingEquiNamesAndIdsToChildren();
public List<CategoryKosik> buildThirdLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI);
public List<CategoryKosik> buildFourthLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI);
public List<CategoryKosik> buildFifthLevelCategoriesWithChildrenFromURI(String firstLevelCategoryURI);
public Set<CategoryKosik> findCategoriesWithUnactiveEquivalent();
public void saveFourthLevelCategoriesWithChildrenBuiltFromURI(String categoryURI);
public void saveFifthLevelCategoriesWithChildrenBuiltFromURI(String categoryURI);
Set<CategoryKosik> getLowestLevelCategoriesInTreeOf(String categoryURI);
TreeMap<Integer, CategoryKosik> findAllParentCategoriesUpToTheHighestParent(String categoryURI);
public void addCategorytoCategoryKosik(Integer idCategoryKosik, Integer categoryIdCategoryRohlik, boolean setParameters);
public void removeCategoryFromCategoryKosik(Integer idCategoryKosik, Integer categoryIdCategoryRohlik);
}