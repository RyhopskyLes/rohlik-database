package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.rohlik.data.commons.exceptions.WrongCategoryIdException;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.entities.Category;

public final class CategorySnapshot {
	private final Category category;
	private final Map<Integer, Category> parentChain;
	private final List<Integer> productIds;
	private ProductsInCategory productsInCategory;
	private CategoryBuildService buildService;
	private static Logger log = LoggerFactory.getLogger(CategorySnapshot.class);
	
	

	public CategorySnapshot() {
		super();
		category=null;
		Map<Integer, Category>  holder = new TreeMap<>();
		this.parentChain = Collections.unmodifiableSortedMap((TreeMap<Integer, Category>) holder);
		List<Integer> productIdsHolder = new ArrayList<>();
		this.productIds = Collections.unmodifiableList(productIdsHolder);	
		
	}

	public CategorySnapshot(ProductsInCategory productsInCategory, CategoryBuildService buildService, Category category)
			throws WrongCategoryIdException {
		super();
		this.buildService = buildService;
		this.productsInCategory = productsInCategory;
		if (category != null) {
			this.category = category;
			Map<Integer, Category>  holder = new TreeMap<>();
			holder=buildService.buildParentChainOfCategory(category.getCategoryId());
			this.parentChain = Collections.unmodifiableSortedMap((TreeMap<Integer, Category>) holder);
			List<Integer> productIdsHolder = new ArrayList<>();
			productIdsHolder.addAll(productsInCategory.getProductIdsForCategory(category.getCategoryId(), 10000));
			this.productIds = Collections.unmodifiableList(productIdsHolder);
		} else {
			throw new WrongCategoryIdException("Category not exists");
		}
	}

	public Category getCategory() {
		return category;
	}

	public Map<Integer, Category> getParentChain() {
		return parentChain;
	}

	public List<Integer> getProductIds() {
		return productIds;
	}

	@Override
	public String toString() {
		return "CategorySnapshot [category=" + category + "\r\n, parentChain=" + parentChain + "\r\n, productIds="
				+ productIds + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(category, parentChain, productIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategorySnapshot other = (CategorySnapshot) obj;
		return Objects.equals(category, other.category) && Objects.equals(parentChain, other.parentChain)
				&& Objects.equals(productIds, other.productIds);
	}

}
