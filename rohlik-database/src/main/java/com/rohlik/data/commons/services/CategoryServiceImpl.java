package com.rohlik.data.commons.services;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.utilities.DataRohlik;
import com.rohlik.data.commons.dao.ChildDao;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.NavSectionsCategoryData;
import com.rohlik.data.objects.Full;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.ProductsInCategory;

@Service("CategoryService")
@Transactional

public class CategoryServiceImpl implements CategoryService {
	CategoryDao catDao;
	Full full;
	ProductDao productDao;
	DataRohlik dataRohlik;
	NavSections nav;
	ProductsInCategory productsInCategory;	
	ChildDao childDao;
	
	private static Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);
	
	@Autowired
	public CategoryServiceImpl(CategoryDao catDao, Full full, ProductDao productDao, DataRohlik dataRohlik,
			NavSections nav, ProductsInCategory productsInCategory, ChildDao childDao) {
		super();
		this.catDao = catDao;
		this.full = full;
		this.productDao = productDao;
		this.dataRohlik = dataRohlik;
		this.nav = nav;
		this.productsInCategory = productsInCategory;
		this.childDao = childDao;
	}
	
	@Override
	public Set<Category> saveUnsavedCategories(Product product) {
		Set<Category> categoriesByProduct = product == null ? new HashSet<>()
				: full.getProductFull(product.getProductId()).getCategoriesConverted();
		for (Category category : categoriesByProduct) {
			Optional<Category> saved = catDao.findByCategoryId(category.getCategoryId());
			if(!saved.isPresent()) {catDao.save(category);}
		}
		return categoriesByProduct;
	}

	
	@Override
	public void addMissingChildToParent(Integer childCategoryId) {
		Optional<Category> child = catDao.findByCategoryId(childCategoryId);
		Optional<Child> checked = child.map(ch -> new Child(childCategoryId, ch.getCategoryName()));
		Optional<Integer> parentId = child.map(Category::getParentId);
		parentId.filter(id -> id != 0).ifPresent(id -> {
			Category parent = catDao.findByCategoryIdWithChildren(id);
			if (parent != null) {
				Set<Child> children = parent.getChildren();
				if (checked.isPresent()&&!children.contains(checked.get())) {
					parent.addChild(checked.get());
					catDao.save(parent);
				}
			}
		});
	}

	@Override
	public TreeMap<Integer, Category> findParentsUpToHighestParent(Integer categoryId) {
		TreeMap<Integer, Category> parentsMap = new TreeMap<>();
		Optional<Category> child = catDao.findByCategoryId(categoryId);
		child.map(Category::getParentId).ifPresent(id -> collectParents.accept(id, parentsMap));
		return parentsMap;
	}

	private Function<Category, Integer> putParentIntoMapAndIcrementKey(Integer[] key, TreeMap<Integer, Category> map) {
		return category -> {
			map.put(key[0], category);
			key[0]++;			
			return category.getParentId();
		};
	}

	private BiConsumer<Integer, TreeMap<Integer, Category>> collectParents = (id, map) -> {
		Integer[] key = { 1 };
		while (id != 0) {
			Optional<Category> parent = catDao.findByCategoryId(id);
			id = parent.map(putParentIntoMapAndIcrementKey(key, map)::apply)
					.orElseGet(() -> Integer.valueOf(0));			
		}
	};

	

	@Override
	public Set<Category> findChildrenToLowestLevel(Integer categoryId) {
		Category category = catDao.findByCategoryIdWithChildren(categoryId);
		Set<Category> children = new HashSet<>();
		if (category != null) {
			Set<Child> directChildren = category.getChildren();
			while (!directChildren.isEmpty()) {
				Set<Child> tempChildren = new HashSet<>();
				for (Child directChild : directChildren) {
					Integer childId = directChild.getCategoryId();
					Category child = catDao.findByCategoryIdWithChildren(childId);
					if (child != null) {
						tempChildren.addAll(child.getChildren());
						children.add(child);
					}					
				}
				directChildren = tempChildren;
			}
		}
		return children;
	}

	@Override
	public Set<Category> findFirstLevelChildren(Integer categoryId) {
		Category category = catDao.findByCategoryIdWithChildren(categoryId);
		Set<Category> children = new HashSet<>();
		if (category != null) {
			Set<Child> directChildren = category.getChildren();
			if (!directChildren.isEmpty()) {
				for (Child directChild : directChildren) {
					Integer childId = directChild.getCategoryId();
					Category child = catDao.findByCategoryIdWithChildren(childId);
					children.add(child);
				}

			}
		}
		return children;
	}

	@Override
	public Set<Category> findAllActiveSubcategories(Integer categoryId) {
		Set<NavSectionsCategoryData> active = nav.completeTreeOfCategory(categoryId);
		return active.stream().map(data -> catDao.findByCategoryIdWithChildren(data.getCategoryId())).filter(category->category!=null).map(category -> {
			category.setActive(true);
			return category;
		}).collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public Set<NavSectionsCategoryData> findAllMissingSubcategories(Integer categoryId) {
		Set<NavSectionsCategoryData> active = nav.completeTreeOfCategory(categoryId);
		return active.stream().filter(data -> catDao.findByCategoryIdWithChildren(data.getCategoryId()) == null)
				.collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public Set<Category> buildAllMissingSubcategories(Integer categoryId) {
		Function<NavSectionsCategoryData, Category> toCategory = data -> {
			Category category = new Category();
			category.setCategoryId(data.getCategoryId());
			category.setCategoryName(data.getName());
			category.setParentId(data.getParentCategoryId());
			category.setActive(true);
			return category;
		};
		Set<NavSectionsCategoryData> missing = findAllMissingSubcategories(categoryId);
		return missing.stream().map(toCategory::apply).collect(Collectors.toCollection(HashSet::new));

	}

	@Override
	public Set<Category> saveAllMissingSubcategories(Integer categoryId) {
		Set<Category> toSave = buildAllMissingSubcategories(categoryId);
		Set<Category> wasSaved = new HashSet<>();
		toSave.stream().forEach(category -> {
			Category saved = catDao.save(category);
			log.info("Saved: {}", saved);
			wasSaved.add(saved);
		});
		toSave.stream().forEach(category -> addMissingChildToParent(category.getCategoryId()));
		return wasSaved;
	}

	@Override
	public Set<Category> deactivateDeadSubcategories(Integer categoryId) {
		Set<Integer> activeIds = findAllActiveSubcategories(categoryId).stream().map(Category::getCategoryId)
				.collect(Collectors.toCollection(HashSet::new));
		Set<Integer> treeIds = findChildrenToLowestLevel(categoryId).stream().map(Category::getCategoryId)
				.collect(Collectors.toCollection(HashSet::new));
		treeIds.removeAll(activeIds);
		return treeIds.stream().map(id -> catDao.findByCategoryIdWithChildren(id)).map(category -> {
			category.setActive(false);
			return category;
		}).peek(category -> log.info("Deactivated: {}", category)).collect(Collectors.toCollection(HashSet::new));
	}

	@Override
	public void addMissingProductsToCategory(Integer categoryId) {
		Optional<Category> category = catDao.findByCategoryId(categoryId);
		List<Integer> productIds = productsInCategory.getProductIdsForCategory(categoryId, 3000);
		Set<Category> categories = new HashSet<>(findParentsUpToHighestParent(categoryId).values());
		category.ifPresent(categories::add);
		productIds.stream().map(id -> productDao.findByProductIdEagerlyWithCategoriesAndChildren(id))
				.filter(Optional::isPresent).map(Optional::get)
				.filter(product -> !product.getCategories().equals(categories)).forEach(product -> {
					Set<Category> current = new HashSet<>(product.getCategories());
					Set<Category> toAdd = new HashSet<>(categories);
					toAdd.removeAll(current);
					log.info("To add: {}", toAdd);
					toAdd.stream().forEach(product::addCategory);
					if (!toAdd.isEmpty())
						log.info("Categories added {} to {} ", toAdd.size(), product);
					productDao.save(product);
				});

	}

	@Override
	public void updateActiveStateByAllChildren() {
		Map<Integer, Category> categories = catDao.findAll().stream().collect(Collectors.toMap(Category::getCategoryId, Function.identity()));
		List<Child> children = childDao.findAll();
		Consumer<Child> setActive = child -> {
		Integer id = child.getCategoryId();
		Category category = categories.get(id);
		child.setActive(category.getActive());
		};		
		children.stream().forEach(setActive::accept);
		
	}

}
