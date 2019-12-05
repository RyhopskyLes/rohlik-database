package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rohlik.data.kosik.entities.CategoryKosik;


public interface CategoryKosikRepository extends JpaRepository<CategoryKosik, Integer>{
	Set<CategoryKosik> findByEquiId(Integer id);
	List<CategoryKosik> findByEquiParentId(Integer id);
	List<CategoryKosik> findByParentName(String name);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children WHERE c.parentName = (:name)")
	List<CategoryKosik> findByParentNameWithChildren(@Param("name")String name);
	List<CategoryKosik> findByCategoryName(String name);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children")
	List<CategoryKosik> findAllWithChildren();
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.categories")
	List<CategoryKosik> findAllWithChildrenAndCategories();
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.categories")
	List<CategoryKosik> findAllWithCategories();
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children WHERE c.categoryName = (:name)")
	List<CategoryKosik> findByCategoryNameWithChildren(@Param("name") String name);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c  LEFT JOIN FETCH c.children WHERE c.id = (:id)")
	Optional<CategoryKosik> findByIdWithChildren(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.categories WHERE c.id = (:id)")
	Optional<CategoryKosik> findByIdWithCategories(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children LEFT JOIN FETCH c.categories cat WHERE c.id = (:id)")
	Optional<CategoryKosik> findByIdWithCategoriesAndChildren(@Param("id") Integer id);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.products WHERE c.uri = (:uri)")
	Optional<CategoryKosik> findByUri(@Param("uri") String uri);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.products LEFT JOIN FETCH c.children WHERE c.uri = (:uri)")
	Optional<CategoryKosik> findByUriWithChildren(@Param("uri") String uri);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.categories WHERE c.uri = (:uri)")
	Optional<CategoryKosik> findByUriWithCategories(@Param("uri") String uri);
	List<CategoryKosik> findByParentUri(String uri);
	@Query("SELECT DISTINCT c FROM  CategoryKosik c LEFT JOIN FETCH c.children WHERE c.parentUri = (:uri)")
	List<CategoryKosik> findByParentUriWithChildren(@Param("uri") String uri);
}
