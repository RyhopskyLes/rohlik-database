package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rohlik.data.commons.exceptions.WrongCategoryIdException;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.commons.services.build.ProductBuildService;
import com.rohlik.data.entities.Category;

public class CategorySnapshot {
	private Category category;
	private Set<Category> parentChain = new HashSet<>();
	private List<Integer> productIds = new ArrayList<>();
	private ProductsInCategory productsInCategory;
	private CategoryBuildService buildService;
	private static Logger log = LoggerFactory.getLogger(CategorySnapshot.class);
	
	

	public CategorySnapshot() {
		super();		
	}

	public CategorySnapshot(ProductsInCategory productsInCategory, CategoryBuildService buildService, Category category)
			throws WrongCategoryIdException {
		super();
		log.info("category in constructor: {}", category);
		this.buildService = buildService;
		this.productsInCategory = productsInCategory;
		if (category != null) {
			this.category = category;
			this.parentChain = buildService.buildParentChainOfCategory(category.getCategoryId()).values().stream()
					.collect(Collectors.toCollection(HashSet::new));
			this.productIds = productsInCategory.getProductIdsForCategory(category.getCategoryId(), 10000);
		} else {
			log.info("argument in else: {}", category);
			throw new WrongCategoryIdException("Category not exists");
		}
	}

	public Category getCategory() {
		return category;
	}

	public Set<Category> getParentChain() {
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
