package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.rohlik.data.kosik.entities.CategoryKosik;

public interface CategoryKosikDao {
	public Optional<CategoryKosik> findById(Integer id);
	public List<CategoryKosik> findAll();
	public List<CategoryKosik> findAllWithChildren();
	public Set<CategoryKosik> findByEquiId(Integer id);
	public List<CategoryKosik> findByEquiParentId(Integer id);
	public List<CategoryKosik> findByParentName(String name);
	public List<CategoryKosik> findByParentNameWithChildren(String name);
	public List<CategoryKosik> findByCategoryName(String name);
	public CategoryKosik save(CategoryKosik category);
	public void delete(CategoryKosik category);
	public List<CategoryKosik> findByCategoryNameWithChildren(String name);
	public Optional<CategoryKosik> findByIdWithChildren(Integer id);
	public Optional<CategoryKosik> findByUri(String uri);
	public Optional<CategoryKosik> findByUriWithChildren(String uri);
	public List<CategoryKosik> findByParentUri(String uri);
	public List<CategoryKosik> findByParentUriWithChildren(String uri);
	public List<CategoryKosik> findAllWithChildrenAndCategories();
	public List<CategoryKosik> findAllWithCategories();
	public Optional<CategoryKosik> findByIdWithCategories(Integer id);
	public Optional<CategoryKosik> findByUriWithCategories(String uri);
	Optional<CategoryKosik> findByIdWithCategoriesAndChildren(Integer id);
}
