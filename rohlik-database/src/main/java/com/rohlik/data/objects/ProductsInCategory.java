package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.Spliterator;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Product;

@Component("ProductsInCategory")
public class ProductsInCategory {
	private RootObject rootObject;
	private Filters filters;
	private Navigation navigation;
	private static final String PRODUCT_LIST = "productList";
	private static final String DATA = "data";
	private static final String PRODUCT_ID = "productId";
	private static final String TOTAL_HITS = "totalHits";

	@Autowired
	public ProductsInCategory(RootObject rootObject, Filters filters, Navigation navigation) {
		this.rootObject = rootObject;
		this.filters = filters;
		this.navigation=navigation;
	}

	public List<RawProduct> getRawProductListForCategory(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<RawProduct> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			products.add(converted);
		});

		return products;
	}

	public List<Product> getProductListForCategory(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			products.add(converted.toProduct());
		});

		return products;
	}

	public List<Product> getProductListForCategoryWithProducers(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Map<String, Set<Integer>> producers = producersWithProductsForCategory(categoryId);
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			Product productConverted = converted.toProduct();
			setProducerName(productConverted, producers);
			products.add(productConverted);
		});

		return products;
	}

	public List<Product> getProductListForCategoryWithSales(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			products.add(converted.toProductWithSales());
		});
		return products;
	}

	public Optional<JsonArray> getProductListJsonArrayForCategory(Integer categoryId, Integer limitResults) {
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		return data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
	}

	public List<Integer> getProductIdsForCategory(Integer categoryId, Integer limitResults) {
		List<Integer> products = new ArrayList<>();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));

		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).map(Optional::ofNullable).forEach(productElement -> {
			Integer productId = productElement.map(JsonElement::getAsJsonObject).map(object -> object.get(PRODUCT_ID))
					.map(JsonElement::getAsInt).orElse(null);
			if (productId != null)
				products.add(productId);
		});
		return products;
	}

	private Optional<JsonObject> getJsonDataObject(Integer categoryId, Integer limitResults) {
		return Objects.equals(rootObject.getCategoryId(), categoryId)
				&& Objects.equals(rootObject.getLimitResults(), limitResults)
				&& Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.CATEGORYDATA)
						? rootObject.getJsonObject().map(object -> object.getAsJsonObject(DATA))
						: rootObject.dataForCategory(categoryId, limitResults)
								.map(object -> object.getAsJsonObject(DATA));
	}

	public Optional<String> getTotalHitsForCategory(Integer categoryId) {
		return rootObject.dataForCategory(categoryId, 25)
				.map(object -> object.getAsJsonObject(DATA).get(TOTAL_HITS).getAsString());
	}

	private Map<String, Set<Integer>> producersWithProductsForCategory(Integer categoryId) {
		List<SlugAndName> slugMap = filters.forCategoryAndSlug(categoryId, "znacka");
		return producersWithProducts(slugMap, categoryId);
	}

	private Map<String, Set<Integer>> producersWithProducts(List<SlugAndName> slugsAndNames, Integer categoryId) {
		Map<String, Set<Integer>> productMap = new HashMap<>();
		String producer = "";
		for (SlugAndName slugAndName : slugsAndNames) {
			Optional<JsonObject> data = rootObject.dataForCategoryAndProducer(categoryId, slugAndName.getSlug())
					.map(object -> object.getAsJsonObject(DATA));
			Optional<JsonArray> productList = data.map(object -> object.get(PRODUCT_LIST).getAsJsonArray());
			producer = slugAndName.getName();
			if (productList.isPresent()) {
				Set<Integer> products = new HashSet<>();
				for (int i = 0; i < productList.get().size(); i++) {
					JsonObject descriptionData = productList.get().get(i).getAsJsonObject();
					Integer product = descriptionData.get(PRODUCT_ID).getAsInt();
					products.add(product);
				}
				productMap.put(producer, products);
			}
		}
		return productMap;
	}

	private void setProducerName(Product product, Map<String, Set<Integer>> producers) {
		Integer productId = product.getProductId();
		product.setProducer("");
		producers.entrySet().stream().filter(entry -> entry.getValue().contains(productId))
				.forEach(entry -> product.setProducer(entry.getKey()));

	}

}
