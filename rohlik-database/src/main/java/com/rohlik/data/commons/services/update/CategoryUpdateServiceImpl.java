package com.rohlik.data.commons.services.update;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryDaoImpl;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;

@Service("CategoryUpdateService")
@Transactional
public class CategoryUpdateServiceImpl implements CategoryUpdateService {
	private CategoryBuildService buildService;
	private CategoryDao categoryDao;
	private static Logger log = LoggerFactory.getLogger(CategoryUpdateServiceImpl.class);

	@Autowired
	public CategoryUpdateServiceImpl(CategoryBuildService buildService, CategoryDao categoryDao) {
		super();
		this.buildService = buildService;
		this.categoryDao = categoryDao;
	}

	@Override
	public void updateCompleteTreeOfMainCategory(Integer categoryId) {
		Map<Integer, Set<Category>> freshState = buildService.buildCompleteTreeOfMainCategory(categoryId);
		Map<Integer, Set<Category>> persistedState = categoryDao
				.findSubcategoriesOfMainCategoryOnAllLevelsGroupedByLevels(categoryId);

		freshState.entrySet().forEach(entry -> {
			log.info("entry {}", entry);
			Map<Integer, List<Category>> levelMap = entry.getValue().stream()
					.collect(Collectors.groupingBy(Category::getCategoryId));
			Set<Category> persistedSet = persistedState.get(entry.getKey());
			Map<Integer, List<Category>> persistedLevelMap = persistedSet != null
					? persistedSet.stream().collect(Collectors.groupingBy(Category::getCategoryId))
					: new HashMap<>();
			log.info("Web level: {}", entry.getKey());
			checkIfNewCategoriesWereAddedToLevel(levelMap, persistedLevelMap).stream().map(levelMap::get)
			.flatMap(List::stream).forEach(this::addNewCategoryToParentAndSaveIt);
			log.info("Persited level: {}", entry.getKey());
			checkIfOldCategoriesWereRemovedFromLevel(levelMap, persistedLevelMap).stream().map(persistedLevelMap::get)
			.flatMap(List::stream).forEach(this::deactivateOldCategory);
			levelMap.entrySet().forEach(checkAndUpdateFields(persistedLevelMap)::accept);
		});

	}

	private boolean categoryEquals(Category web, Category persisted) {
		return Objects.equals(web.getCategoryId(), persisted.getCategoryId())
				&& Objects.equals(web.getCategoryName(), persisted.getCategoryName())
				&& Objects.equals(web.getParentId(), persisted.getParentId())
				&& Objects.equals(web.getActive(), persisted.getActive());
	}

	private void setFieldsFromWeb(Category web, Category persisted) {
		persisted.setCategoryId(web.getCategoryId());
		persisted.setCategoryName(web.getCategoryName());
		persisted.setParentId(web.getParentId());
		persisted.setActive(web.getActive());
	}

	private Consumer<Entry<Integer, List<Category>>> checkAndUpdateFields(
			Map<Integer, List<Category>> persistedLevelGroupedByCategoryId) {
		return entry -> {
			if (!entry.getValue().isEmpty()) {
				Category web = entry.getValue().iterator().next();
				List<Category> persisted = persistedLevelGroupedByCategoryId.get(entry.getKey());
				if (persisted != null && !persisted.isEmpty()) {
					Category db = persisted.iterator().next();
					if (!categoryEquals(web, db)) {
						setFieldsFromWeb(web, db);
					}
				}
			}
		};
	}

	private Set<Integer> checkIfNewCategoriesWereAddedToLevel(Map<Integer, List<Category>> levelMap,
			Map<Integer, List<Category>> persistedLevelMap) {
		Set<Integer> levelIds = levelMap.keySet().stream().collect(Collectors.toCollection(HashSet::new));
		Set<Integer> persistedLevelIds = persistedLevelMap.keySet().stream()
				.collect(Collectors.toCollection(HashSet::new));
		levelIds.removeAll(persistedLevelIds);
		levelIds.forEach(id->log.info("not persisted {}", id));
		return levelIds;
	}

	private Set<Integer> checkIfOldCategoriesWereRemovedFromLevel(Map<Integer, List<Category>> levelMap,
			Map<Integer, List<Category>> persistedLevelMap) {
		Set<Integer> levelIds = levelMap.keySet().stream().collect(Collectors.toCollection(HashSet::new));
		Set<Integer> persistedLevelIds = persistedLevelMap.keySet().stream()
				.collect(Collectors.toCollection(HashSet::new));
		persistedLevelIds.removeAll(levelIds);
		persistedLevelIds.forEach(id->log.info("not deactivated {}", id));
		return persistedLevelIds;
	}

	private void addNewCategoryToParentAndSaveIt(Category category) {
		if (category != null) {
			Category parent = categoryDao.findByCategoryIdWithChildren(category.getParentId());
			if (parent != null) {
				Child child = category.toChild();
				Set<Integer> childIds = parent.getChildren().stream().map(Child::getCategoryId).collect(Collectors.toCollection(HashSet::new));
				if (!childIds.contains(child.getCategoryId()))
				{child.setActive(true);
					parent.addChild(child);
				categoryDao.save(parent);}
				Optional<Child> added = parent.getChildren().stream().filter(item->item.getCategoryId().equals(child.getCategoryId())).findFirst();
				log.info("{} added to {}", added, parent);
			
			}
			Optional<Category> persisted = categoryDao.findByCategoryId(category.getCategoryId());
			if(persisted.isPresent())
			{persisted.get().setActive(true);
			Category saved =categoryDao.save(persisted.get());
			log.info("{} updated", saved);
			}	else {
			Category saved =categoryDao.save(category);
			log.info("{} added", saved);}
		}
	}
	
	private void deactivateOldCategory(Category category) {
		if (category != null) {
			Category parent = categoryDao.findByCategoryIdWithChildren(category.getParentId());
			if (parent != null) {
				Child child = category.toChild();
				Set<Integer> childIds = parent.getChildren().stream().map(Child::getCategoryId).collect(Collectors.toCollection(HashSet::new));
				Set<Child> children = parent.getChildren();
				if (childIds.contains(child.getCategoryId()))
					children.stream().filter(item->item.getCategoryId().equals(child.getCategoryId())).forEach(item->item.setActive(false));
				log.info("{} deactivated in {}", child, parent);
			
			}
			category.setActive(false);
			Category saved =categoryDao.save(category);
			log.info("{} deactivated", saved);
		}
	}
}
