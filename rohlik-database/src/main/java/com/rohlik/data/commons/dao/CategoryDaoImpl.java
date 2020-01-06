package com.rohlik.data.commons.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.exceptions.NullIdException;
import com.rohlik.data.commons.exceptions.WrongOrMissingClassException;
import com.rohlik.data.commons.objects.Record;
import com.rohlik.data.commons.objects.Registry;
import com.rohlik.data.commons.repos.CategoryRepository;
import com.rohlik.data.dtos.ChildDTO;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;

@Repository("categoryDao")
@Transactional
public class CategoryDaoImpl implements CategoryDao {
	CategoryRepository catRepository;
	@PersistenceContext
	private EntityManager em;
	private Registry registry;

	@Autowired
	public CategoryDaoImpl(CategoryRepository catRepository, Registry registry) {
		super();
		this.catRepository = catRepository;
		this.registry = registry;
	}

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
		Category saved = catRepository.save(category);
		try {
			boolean added = registry.addCategoryRecord(new Record(saved.getId(), saved.getCategoryId(), Category.class));
			log.info("{} removed from registry: {}", category, added);
		} catch (NullIdException | WrongOrMissingClassException e) {
			log.info("not added to registry: {}", category);
			log.info("{}", e);
		}
		return saved;
	}

	@Override
	public void removeById(Integer id) {
		registry.getCategoryRecords().stream().filter(record -> record.getPersistedId().equals(id)).findFirst()
				.ifPresent(record ->{ boolean removed = registry.removeCategoryRecord(record);
				log.info("{} removed from registry: {}", record, removed);});
		catRepository.deleteById(id);

	}

	@Override
	public void remove(Category category) {
		catRepository.delete(category);
		try {
			boolean removed = registry.removeCategoryRecord(new Record(category.getId(), category.getCategoryId(), Category.class));
			log.info("{} removed from registry: {}", category, removed);
		} catch (NullIdException | WrongOrMissingClassException e) {
			log.info("not removed from registry: {}", category);
			log.info("{}", e);
		}
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
		log.info("categoryId: {}", id);
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

	@Override
	public List<Category> findSubcategoriesOfCategoryOnAllLevels(Integer categoryId) {
		return catRepository.findSubcategoriesOfCategoryOnAllLevels(categoryId).stream().filter(Category::getActive)
				.collect(Collectors.toCollection(ArrayList::new));
	}

	@Override
	public List<Category> findCompleteParentChainOfCategory(Integer categoryId) {
		return catRepository.findCompleteParentChainOfCategory(categoryId);
	}

	@Override
	public Map<Integer, Set<Category>> findSubcategoriesOfMainCategoryOnAllLevelsGroupedByLevels(Integer categoryId) {
		List<Category> completeTree = findSubcategoriesOfCategoryOnAllLevels(categoryId);
		Map<Integer, List<Category>> completeTreeGroupedByCategoryId = completeTree.stream()
				.collect(Collectors.groupingBy(Category::getCategoryId));
		Category root = this.findByCategoryIdWithChildren(categoryId);
		Set<Category> level = new HashSet<>();
		Map<Integer, Set<Category>> groupedCompleteTree = new HashMap<>();
		int counter = 0;
		if (root != null) {
			level.add(root);
			groupedCompleteTree.put(counter, level);
			Set<Child> childrenOnLevel = root.getChildren();
			while (!childrenOnLevel.isEmpty()) {
				level = childrenOnLevel.stream()
						.map(child -> completeTreeGroupedByCategoryId.get(child.getCategoryId()))
						.filter(Objects::nonNull).filter(list -> !list.isEmpty()).map(list -> list.iterator().next())
						.filter(Objects::nonNull).collect(Collectors.toCollection(HashSet::new));
				counter++;
				groupedCompleteTree.put(counter, level);
				childrenOnLevel = level.stream().map(Category::getChildren).filter(children -> !children.isEmpty())
						.flatMap(Set::stream).collect(Collectors.toCollection(HashSet::new));
			}
		}
		return groupedCompleteTree;
	}

}
