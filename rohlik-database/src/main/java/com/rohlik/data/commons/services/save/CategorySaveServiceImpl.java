package com.rohlik.data.commons.services.save;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

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

	private Category saveCategory(Category toSave) {
		Category saved=null;
		if(toSave!= null) {
			saved = categoryDao.save(toSave);	
		}		
		return saved;	
	}
	
	@Override
	public List<Category> saveAllMainCategories() {
		List<Category> categories = new ArrayList<>();
		buildService.buildAllMainCategories().forEach(saveAndAddToCollection(categories)::accept);
		return categories;
	}

private Consumer<Category> saveAndAddToCollection(Collection<Category> categories) {
	return category-> {Category saved =saveCategory(category);
	if(saved!=null) categories.add(saved);};
}
	@Override
	public List<Category> saveAllMainCategoriesWithChildren() {
		List<Category> categories = new ArrayList<>();
		buildService.buildAllMainCategoriesWithChildren().forEach(saveAndAddToCollection(categories)::accept);
		return categories;
	}

	@Override
	public Optional<Category> saveCategory(Integer categoryId) {
		Optional<Category> toSave =buildService.buildCategory(categoryId);
		return saveCategory(toSave);
	}

	@Override
	public Optional<Category> saveCategoryWithChildren(Integer categoryId) {
		Optional<Category> toSave =buildService.buildCategoryWithChildren(categoryId);
		return saveCategory(toSave);
	}
	
	@Override
	public Map<Integer, Set<Category>> saveCompleteTreeOfMainCategory(Integer categoryId) {
		Map<Integer, Set<Category>> result = new HashMap<>();
		Map<Integer, Set<Category>> completeTree = buildService.buildCompleteTreeOfMainCategory(categoryId);
		completeTree.entrySet().stream().forEach(saveLevelAndAddItToResult(result)::accept);		
		return result;
	}
	
	private Consumer<Entry<Integer, Set<Category>>> saveLevelAndAddItToResult(Map<Integer, Set<Category>> result) {
		return entry->{
			Set<Category>	temp = new HashSet<>();
			entry.getValue().stream().forEach(saveAndAddToCollection(temp)::accept);
			result.put(entry.getKey(), temp);			
		};		
		
	}

	@Override
	public Map<Integer, Set<Category>> saveCompleteTreeOfMainCategoryDownToLevel(Integer categoryId, int toLevel) {
		Map<Integer, Set<Category>> result = new HashMap<>();
		Map<Integer, Set<Category>> treeDownToLevel = buildService.buildCompleteTreeOfMainCategoryDownToLevel(categoryId, toLevel);
		treeDownToLevel.entrySet().stream().forEach(saveLevelAndAddItToResult(result)::accept);
		return result;
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
