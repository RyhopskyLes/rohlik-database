package com.rohlik.data.commons.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.objects.Cancelled;

public interface CategoryKosikUpdateService {
	public Set<CategoryKosik> findCategoriesWithUnactiveEquivalent();
	public Cancelled findCancelledCategories();
	public void fixWrongEquiNamesByChildren();
	public void addMissingEquiNamesAndIdsToChildren();
	public Optional<CategoryKosik> updateParentUriByMainCategory(String categoryURI);
	public void fixWrongEquiNames();
	public void addMissingEquiCategoryNamesToAllCategories();
	public List<CategoryKosik> findCategoriesWithWrongEquiNames();
	public void updateUriBySecondLevelCategories(String firstLevelCategoryURI);
	public void updateParentUriBySecondLevelCategories(String firstLevelCategoryURI);
	public void updateUriByChildrenOfCategory(String categoryUri);
	public void updateParentUriByChildrenOfCategory(String categoryUri);

}
