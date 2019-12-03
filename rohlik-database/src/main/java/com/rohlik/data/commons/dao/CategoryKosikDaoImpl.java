package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.CategoryKosikRepository;
import com.rohlik.data.kosik.entities.CategoryKosik;

@Repository("categoryKosikDao")
@Transactional
public class CategoryKosikDaoImpl implements CategoryKosikDao {
	@Autowired 
	CategoryKosikRepository catRepository;

	@Override
	public Optional<CategoryKosik> findById(Integer id) {		
		return catRepository.findById(id);
	}

	@Override
	public List<CategoryKosik> findAll() {
		return catRepository.findAll();
	}

	@Override
	public Set<CategoryKosik> findByEquiId(Integer id) {
		return catRepository.findByEquiId(id);
	}

	@Override
	public List<CategoryKosik> findByEquiParentId(Integer id) {
		return catRepository.findByEquiParentId(id);
	}

	@Override
	public List<CategoryKosik> findByParentName(String name) {
		return catRepository.findByParentName(name);
	}

	@Override
	public List<CategoryKosik> findByCategoryName(String name) {
		return catRepository.findByCategoryName(name);
	}

	@Override
	public CategoryKosik save(CategoryKosik category) {
		return catRepository.save(category);
	}

	@Override
	public void delete(CategoryKosik category) {
		catRepository.delete(category);		
	}

	@Override
	public List<CategoryKosik> findByCategoryNameWithChildren(String name) {		
		return catRepository.findByCategoryNameWithChildren(name);
	}

	@Override
	public Optional<CategoryKosik> findByIdWithChildren(Integer id) {
		return catRepository.findByIdWithChildren(id);
	}

	@Override
	public Optional<CategoryKosik> findByUri(String uri) {		
		return catRepository.findByUri(uri);
	}

	@Override
	public Optional<CategoryKosik> findByUriWithChildren(String uri) {		
		return catRepository.findByUriWithChildren(uri);
	}

	@Override
	public List<CategoryKosik> findAllWithChildren() {
		return catRepository.findAllWithChildren();
	}

	@Override
	public List<CategoryKosik> findByParentNameWithChildren(String name) {
		return catRepository.findByParentNameWithChildren(name);
	}

	@Override
	public List<CategoryKosik> findByParentUri(String uri) {
		return catRepository.findByParentUri(uri);
	}

	@Override
	public List<CategoryKosik> findByParentUriWithChildren(String uri) {
		return catRepository.findByParentUriWithChildren(uri);
	}

	@Override
	public List<CategoryKosik> findAllWithChildrenAndCategories() {
		return catRepository.findAllWithChildrenAndCategories();
	}

	@Override
	public List<CategoryKosik> findAllWithCategories() {
		return catRepository.findAllWithCategories();
	}

	@Override
	public Optional<CategoryKosik> findByIdWithCategories(Integer id) {
		return catRepository.findByIdWithCategories(id);
	}

	@Override
	public Optional<CategoryKosik> findByUriWithCategories(String uri) {
		return catRepository.findByUriWithCategories(uri);
	}

	@Override
	public Optional<CategoryKosik> findByIdWithCategoriesAndChildren(Integer id) {
		return catRepository.findByIdWithCategoriesAndChildren(id);
	}
}
