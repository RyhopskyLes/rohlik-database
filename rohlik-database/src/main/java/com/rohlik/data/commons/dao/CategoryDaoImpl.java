package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.CategoryRepository;
import com.rohlik.data.entities.Category;
@Repository("categoryDao")
@Transactional
public class CategoryDaoImpl implements CategoryDao {
	@Autowired 
	CategoryRepository catRepository;
	private static Logger log = LoggerFactory.getLogger(CategoryDaoImpl.class);
	@Override
	public Category findById(Integer id) {
		return catRepository.findById(id).orElse(null);
	}

	@Override
	public Optional<Category> findByCategoryId(Integer id) {
		return catRepository.findByCategoryId(id);
	}

	@Override
	public List<Category> findByParentId(Integer id) {
		return catRepository.findByParentId(id);
	}

	@Override
	public List<Category> findAll() {
		return catRepository.findAll();
	}

	@Override
	public List<Category> findAllWithChildren() {
		return catRepository.findAllWithChildren();
	}

	@Override
	public Optional<Category> findByIdWithChildren(Integer id) {
		return catRepository.findByIdWithChildren(id);
	}

	@Override
	public Category findByCategoryIdWithChildren(Integer id) {
		return catRepository.findByCategoryIdWithChildren(id);
	}

	@Override
	public List<Category> findAllByParentIdWithChildren(Integer id) {
		return catRepository.findAllByParentIdWithChildren(id);
	}

	@Override
	public Category save(Category category) {
		return catRepository.save(category);
	}

	@Override
	public void removeById(Integer id) {
		catRepository.deleteById(id);
		
	}

	@Override
	public void remove(Category category) {
		catRepository.delete(category);		
	}

	@Override
	public List<Category> findMainCategoriesFromNavigation() {
		return catRepository.findMainCategoriesFromNavigation();
	}

	@Override
	public List<Category> findMainCategoriesFromNavigationWithChildren() {
			return catRepository.findMainCategoriesFromNavigationWithChildren();
	}

	@Override
	public Category findByCategoryIdWithChildrenAndCategoriesKosik(Integer id) {
		log.info("categoryId: "+id);
		return catRepository.findByCategoryIdWithChildrenAndCategoriesKosik(id);
	}

	@Override
	public List<Category> findAllWithChildrenAndCategoriesKosikAndProducts() {
		return catRepository.findAllWithChildrenAndCategoriesKosikAndProducts();
	}

	@Override
	public List<Category> findAllWithCategoriesKosikAndProducts() {
		return catRepository.findAllCategoriesKosikAndProducts();
	}

	@Override
	public Category findByCategoryIdWithCategoriesKosik(Integer id) {
		return catRepository.findByCategoryIdWithCategoriesKosik(id);
	}

}
