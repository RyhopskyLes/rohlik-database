package com.rohlik.data.objects;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("ProducersInCategory")
public class ProducersInCategory {
	private Integer categoryId;
	private Filters filter;
	private ProductsInCategory productsInCategory;

	@Autowired
	public ProducersInCategory(Filters filter, ProductsInCategory productsInCategory) {
		super();
		this.filter = filter;
		this.productsInCategory = productsInCategory;
	}

	public Set<ProducerWithProducts> withoutProducts(Integer categoryId) {
		this.categoryId = categoryId;
		return filter.forCategoryAndSlug(categoryId, "znacka").stream()
				.map(SlugAndName::toProducerWithoutProductsAndCategoryId).map(setCategoryId::apply)
				.collect(Collectors.toCollection(HashSet::new));

	}

	public Set<ProducerWithProducts> withProducts(Integer categoryId) {
		Map<String, Set<Integer>> productsGroupedByProducers = this.productsInCategory
				.producersWithProductsForCategory(categoryId);
		return withoutProducts(categoryId).stream()
				.map(setProductIds(productsGroupedByProducers)::apply)
				.collect(Collectors.toCollection(HashSet::new));

	}

	private UnaryOperator<ProducerWithProducts> setCategoryId = producer -> {
		if (producer != null) {
			producer.setCategoryId(categoryId);
		}
		return producer;
	};

	private UnaryOperator<ProducerWithProducts> setProductIds(
			Map<String, Set<Integer>> productsGroupedByProducers) {
		return producer -> {
			if (producer != null && producer.getName() != null) {
				Set<Integer> products = productsGroupedByProducers.get(producer.getName());
				producer.setProductIds(products);				
			}
			return producer;
		};
	}
}
