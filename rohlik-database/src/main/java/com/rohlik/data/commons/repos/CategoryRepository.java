package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rohlik.data.entities.Category;




public interface CategoryRepository extends JpaRepository<Category, Integer> {
	@Query("SELECT DISTINCT c FROM  Category c LEFT JOIN FETCH c.children WHERE c.id = (:id)")
	public Optional<Category> findByIdWithChildren(@Param("id") Integer id);
	public Optional<Category> findByCategoryId(Integer id);
	public List<Category> findByParentId(Integer id);
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.categoriesKosik LEFT JOIN FETCH c.products WHERE c.active =true")
	public List<Category> findAllWithChildrenAndCategoriesKosikAndProducts();
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.categoriesKosik LEFT JOIN FETCH c.products WHERE c.active =true")
	public List<Category> findAllCategoriesKosikAndProducts();
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children WHERE c.categoryId = (:id)")
	public Category findByCategoryIdWithChildren(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.categoriesKosik WHERE c.categoryId = (:id)")
	public Category findByCategoryIdWithChildrenAndCategoriesKosik(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.categoriesKosik WHERE c.categoryId = (:id)")
	public Category findByCategoryIdWithCategoriesKosik(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parentId = (:id)")
	public List<Category> findAllByParentIdWithChildren(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children")
	public List<Category> findAllWithChildren();
	@Query("SELECT c FROM Category c WHERE c.parentId = 0 and c.id < 1341")
	public List<Category> findMainCategoriesFromNavigation();
	@Query("SELECT DISTINCT c FROM Category c LEFT JOIN FETCH c.children WHERE c.parentId = 0 and c.id < 1341")
	public List<Category> findMainCategoriesFromNavigationWithChildren();
}
