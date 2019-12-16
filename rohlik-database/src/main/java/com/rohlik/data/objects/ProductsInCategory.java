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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
	private static Logger log = LoggerFactory.getLogger(ProductsInCategory.class);

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
		Map<Integer, String> categories = navigation.getAllCategoriesIdandName();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			Product productConverted = converted.toProduct();
			String mainCategoryName = getCategoryName(productConverted.getMainCategoryId(), categories);
			productConverted.setMainCategoryName(mainCategoryName);
			products.add(productConverted);
		});

		return products;
	}

	public List<Product> getProductListForCategoryWithProducers(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Map<String, Set<Integer>> producers = producersWithProductsForCategory(categoryId);
		Map<Integer, String> categories = navigation.getAllCategoriesIdandName();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			Product productConverted = converted.toProduct();
			String mainCategoryName = getCategoryName(productConverted.getMainCategoryId(), categories);
			productConverted.setMainCategoryName(mainCategoryName);
			setProducerName(productConverted, producers);
			products.add(productConverted);
		});

		return products;
	}

	public List<Product> getProductListForCategoryWithSales(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Map<Integer, String> categories = navigation.getAllCategoriesIdandName();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			Product productConverted = converted.toProductWithSales();
			String mainCategoryName = getCategoryName(productConverted.getMainCategoryId(), categories);
			productConverted.setMainCategoryName(mainCategoryName);
			products.add(productConverted);
		});
		return products;
	}

	public List<Product> getProductListForCategoryWithSalesAndProducers(Integer categoryId, Integer limitResults) {
		Gson gson = new Gson();
		List<Product> products = new ArrayList<>();
		Map<String, Set<Integer>> producers = producersWithProductsForCategory(categoryId);
		Map<Integer, String> categories = navigation.getAllCategoriesIdandName();
		Optional<JsonObject> data = getJsonDataObject(categoryId, limitResults);
		Optional<JsonArray> productList = data.map(theData -> theData.getAsJsonArray(PRODUCT_LIST));
		Spliterator<JsonElement> elements = productList.orElseGet(JsonArray::new).spliterator();
		StreamSupport.stream(elements, false).forEach(product -> {
			RawProduct converted = gson.fromJson(product, RawProduct.class);
			Product productConverted = converted.toProductWithSales();
			String mainCategoryName = getCategoryName(productConverted.getMainCategoryId(), categories);
			productConverted.setMainCategoryName(mainCategoryName);
			setProducerName(productConverted, producers);
			products.add(productConverted);			
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
					.map(JsonElement::getAsInt).orElseGet(()->null);
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

	private Optional<JsonObject> getJsonDataObjectForCategoryAndProducer(Integer categoryId, String slug) {
		return Objects.equals(rootObject.getCategoryId(), categoryId)
				&& Objects.equals(rootObject.getSlug(), slug)
				&& Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.PRODUCER)
						? rootObject.getJsonObject().map(object -> object.getAsJsonObject(DATA))
						: rootObject.dataForCategoryAndProducer(categoryId, slug)
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
			Optional<JsonObject> data =getJsonDataObjectForCategoryAndProducer(categoryId, slugAndName.getSlug());
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
	
	private String getCategoryName(Integer catNum, Map<Integer, String> categoriesMap) {
		String catName = categoriesMap.get(catNum);
		return catName == null ? "" : catName;
	}

}
