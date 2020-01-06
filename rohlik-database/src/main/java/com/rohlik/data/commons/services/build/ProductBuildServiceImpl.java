package com.rohlik.data.commons.services.build;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.UnaryOperator;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.CategorySnapshot;
import com.rohlik.data.objects.ProductsInCategory;
import com.rohlik.data.kosik.objects.MapEntry;

@Service("ProductBuildService")
public class ProductBuildServiceImpl implements ProductBuildService {
	private ProductsInCategory productsInCategory;
	private CategoryBuildService buildService;
	private BeanFactory beanFactory;
	private static Logger log = LoggerFactory.getLogger(ProductBuildServiceImpl.class);

	@Autowired
	public ProductBuildServiceImpl(ProductsInCategory productsInCategory, CategoryBuildService buildService,
			BeanFactory beanFactory) {
		super();
		this.productsInCategory = productsInCategory;
		this.buildService = buildService;
		this.beanFactory = beanFactory;
	}

	@Override
	public List<Product> buildAllProductsInCategoryWithoutProducers(Integer categoryId) {
		return productsInCategory.getProductListForCategoryWithSales(categoryId, 8000);
	}

	@Override
	public List<Product> buildAllProductsInMainCategoryWithProducers(Integer categoryId) {
		Map<Integer, Product> productsWithProducersMappedToProductId = getProductsMappedToProductIdsInCategory(
				categoryId);

		Set<CategorySnapshot> lowestLevelSnapshots = getLowestLevelCategorySnapshotsForCategory(categoryId);
		
		addCategoriesToProducts(lowestLevelSnapshots, productsWithProducersMappedToProductId);
		
		Map<Integer, Product> withEmptyCategories = getProductsWithoutCategoriesMappedToProductIds(
				productsWithProducersMappedToProductId);

		if (!withEmptyCategories.isEmpty()) {
			Set<CategorySnapshot> oneLevelUp = getCategorySnapshotsForOnelevelUp(lowestLevelSnapshots);
			while (!withEmptyCategories.isEmpty() && !oneLevelUp.isEmpty()) {
				Map<Integer, Product> toRemove = new HashMap<>();
				oneLevelUp.forEach(addCategoriesToProductsWithoutCategories(withEmptyCategories,
						productsWithProducersMappedToProductId, toRemove)::accept);
				oneLevelUp = getCategorySnapshotsForOnelevelUp(oneLevelUp);
			}

		}
		return productsWithProducersMappedToProductId.values().stream()
				.collect(Collectors.toCollection(ArrayList::new));
	}

	
	
	private void addCategoriesToProducts(Set<CategorySnapshot> lowestLevelSnapshots,
			Map<Integer, Product> productsWithProducersMappedToProductId) {
		lowestLevelSnapshots.forEach(addCategoriesToProductsListedInSnapshot(productsWithProducersMappedToProductId,
				productsWithProducersMappedToProductId)::accept);
	}

	private Map<Integer, Product> getProductsMappedToProductIdsInCategory(Integer categoryId) {
		return productsInCategory.getProductListForCategoryWithSalesAndProducers(categoryId, 8000).stream()
				.map(product -> new MapEntry<Integer, Product>(product.getProductId(), product))
				.collect(Collectors.toMap(MapEntry::getKey, MapEntry::getValue));
	}

	private Map<Integer, Product> getProductsWithoutCategoriesMappedToProductIds(
			Map<Integer, Product> productsWithProducersMappedToProductId) {
		return productsWithProducersMappedToProductId.entrySet().stream()
				.filter(entry -> entry.getValue().getCategories().isEmpty())
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	
	private Set<CategorySnapshot> getLowestLevelCategorySnapshotsForCategory(Integer categoryId) {return buildService
			.buildFlattenedLowestLevelOfEachBranchOfMainCategoryTree(categoryId).stream().map(toCategorySnapshot::apply)
			.collect(Collectors.toCollection(HashSet::new));}

	private Set<CategorySnapshot> getCategorySnapshotsForOnelevelUp(Set<CategorySnapshot> snapshots) { return snapshots.stream()
			.map(getDirectParent::apply).filter(Objects::nonNull).map(toCategorySnapshot::apply)
			.sorted((s1, s2)->s2.getCategory().getParentId()-s1.getCategory().getParentId())
			.collect(Collectors.toCollection(HashSet::new));}

	
	private Function<CategorySnapshot, Category> getDirectParent = snapshot -> snapshot.getParentChain().entrySet().stream()
			.filter(entry -> entry.getValue().getCategoryId().equals(snapshot.getCategory().getParentId())).map(Entry::getValue).findFirst()
			.orElseGet(() -> null);

	private Function<Category, CategorySnapshot> toCategorySnapshot = category -> {
		CategorySnapshot snapshot = null;
		try {
			snapshot = beanFactory.getBean(CategorySnapshot.class, category);
		} catch (Exception e) {
			log.info("{}", e);
		}
		return snapshot;
	};

	private BinaryOperator<Product> mergeCategoriesByProduct = (oldOne, newOne) -> {
		oldOne.getCategories().forEach(newOne::addCategory);
		return newOne;
	};
	private Consumer<CategorySnapshot> addCategoriesToProductsListedInSnapshot(Map<Integer, Product> getProductFrom,
			Map<Integer, Product> updateProductIn) {
		
		return snapshot -> {
			log.info("processing {} {}", snapshot.getCategory().getCategoryId(), snapshot.getCategory().getCategoryName());
		snapshot.getProductIds().stream().forEach(id -> {
			Product toHandle = getProductFrom.get(id);
			if (toHandle != null) {
				toHandle.addCategory(snapshot.getCategory());
				snapshot.getParentChain().entrySet().forEach(entry->
						toHandle.addCategory(entry.getValue()));
				updateProductIn.merge(id, toHandle, mergeCategoriesByProduct::apply);
			}
		});};
	}

	private Consumer<CategorySnapshot> addCategoriesToProductsWithoutCategories(
			Map<Integer, Product> productsWithoutCategories, Map<Integer, Product> updateProductIn,
			Map<Integer, Product> toRemove) {
		return snapshot -> {
			log.info("processing {} {}", snapshot.getCategory().getCategoryId(), snapshot.getCategory().getCategoryName());
		snapshot.getProductIds().stream().forEach(id -> {
			Product toHandle = productsWithoutCategories.get(id);
			if (toHandle != null) {
				log.info("snapshot for {}", snapshot.getCategory().getCategoryName());
				log.info("adding Categories to {}", toHandle);
				toRemove.put(id, toHandle);
				toHandle.addCategory(snapshot.getCategory());
				snapshot.getParentChain().entrySet().forEach(entry->{
					toHandle.addCategory(entry.getValue());});
				log.info("added Categories {}", toHandle.getCategories());
				updateProductIn.merge(id, toHandle, mergeCategoriesByProduct::apply);
			}
			productsWithoutCategories.keySet().removeAll(toRemove.keySet());
		});};
	}
}
