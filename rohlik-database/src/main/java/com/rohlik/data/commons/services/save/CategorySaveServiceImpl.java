package com.rohlik.data.commons.services.save;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.entities.Category;

@Service("CategorySaveService")
@Transactional
public class CategorySaveServiceImpl implements CategorySaveService{
	private CategoryBuildService buildService;
	private CategoryDao categoryDao;
@Autowired
	public CategorySaveServiceImpl(CategoryBuildService buildService, CategoryDao categoryDao) {
		super();
		this.buildService = buildService;
		this.categoryDao = categoryDao;
	}

	@Override
	public Optional<Category> saveMainCategory(Integer categoryId) {
		Optional<Category> toSave =buildService.buildMainCategory(categoryId);
		return saveCategory(toSave);
	}

	@Override
	public Optional<Category> saveMainCategoryWithChildren(Integer categoryId) {
		Optional<Category> toSave =buildService.buildMainCategoryWithChildren(categoryId);
		return saveCategory(toSave);
	}
	
	private Optional<Category> saveCategory(Optional<Category> toSave) {
		Optional<Category> saved = Optional.empty();
		if(toSave.isPresent()) {
			saved = Optional.ofNullable(categoryDao.save(toSave.get()));	
		}		
		return saved;	
	}

	@Override
	public List<Category> saveAllMainCategories() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Category> saveAllMainCategoriesWithChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Category> saveCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Category> saveCategoryWithChildren(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Map<Integer, Set<Category>> saveCompleteTreeOfMainCategory(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Set<Category>> saveCompleteTreeOfMainCategoryDownToLevel(Integer categoryId, int toLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Set<Category>> saveCompleteTreeOfMainCategoryFromLevelToLevel(Integer categoryId, int fromLevel,
			int toLevel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Category> saveLevelFromCompleteTreeOfMainCategory(Integer categoryId, int level) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, Set<Category>> saveLowestLevelOfEachBranchOfMainCategoryTree(Integer categoryId) {
		// TODO Auto-generated method stub
		return null;
	}

}