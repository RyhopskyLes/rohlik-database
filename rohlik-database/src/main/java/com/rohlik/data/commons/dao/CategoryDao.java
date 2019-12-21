package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import com.rohlik.data.entities.Category;

public interface CategoryDao {
	public Category findById(Integer id);
	public Optional<Category> findByCategoryId(Integer id);
	public List<Category> findByParentId(Integer id);
	public List<Category> findAll();
	public List<Category> findAllWithChildren();
	public List<Category> findAllWithCategoriesKosikAndProducts();
	public List<Category> findAllWithChildrenAndCategoriesKosikAndProducts();
	public List<Category> findMainCategoriesFromNavigation();
	public Optional<Category> findByIdWithChildren(Integer id);
	public Category findByCategoryIdWithChildren(Integer id);
	public List<Category> findAllByParentIdWithChildren(Integer id);
	public Category save(Category category);
	public void removeById(Integer id);
	public void remove(Category category);
	public List<Category> findMainCategoriesFromNavigationWithChildren();
	public Category findByCategoryIdWithChildrenAndCategoriesKosik(Integer id);
	public Category findByCategoryIdWithCategoriesKosik(Integer id);
	public List<Category> findSubcategoriesOfCategoryOnAllLevels(Integer categoryId);
	public List<Category> findCompleteParentChainOfCategory(Integer categoryId);
}
