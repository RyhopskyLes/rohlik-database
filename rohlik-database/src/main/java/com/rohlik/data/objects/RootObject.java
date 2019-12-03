package com.rohlik.data.objects;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.rohlik.data.commons.utilities.Source;
@Component("rootObject")
public class RootObject {
	@Autowired
	private Source source;
	public static final String CATEGORIES_METADATA_START = "https://www.rohlik.cz/services/frontend-service/categories/";
	public static final String CATEGORIES_METADATA_END = "/metadata";
	public static final String START_SLUG_URL = "https://www.rohlik.cz/services/frontend-service/products/";
	public static final String QUERY_SLUG_URL = "?offset=0&limit=150&data=%7B\"filters\"%3A%5B%7B\"filterSlug\"%3A\"znacka\"%2C\"valueSlug\"%3A\"";
	public static final String SLUG_END_URL = "\"%7D%5D%7D";
	public static final String CATEGORY_URL = "https://www.rohlik.cz/services/frontend-service/products/";
	public static final String OFFSET_LIMIT = "?offset=0&limit=";
	public static final String BASIC_LIMIT = "25";
	public static final String PRODUCT_URL_START = "https://www.rohlik.cz/services/frontend-service/product/";
	public static final String PRODUCT_URL_END = "/full";
	public static final String NAVIGATION_URL = "https://www.rohlik.cz/services/frontend-service/renderer/navigation/flat.json";
	private static Logger log = LoggerFactory.getLogger(RootObject.class);
	private Integer categoryId;
	private Integer limitResults;
	private String slug;
	private Integer productId;
	private Optional<JsonObject> jsonObject;
	public static enum CalledFrom {METADATA, PRODUCER, CATEGORYDATA, PRODUCT, NAVIGATION};
	private CalledFrom from;
	public RootObject() {	
	}
	
	public Optional<JsonObject> metadataForCategory(Integer categoryId) {
		this.categoryId=categoryId;
		this.from=CalledFrom.METADATA;
		String categoryURL = CATEGORIES_METADATA_START+this.categoryId+CATEGORIES_METADATA_END;
		this.jsonObject=source.rootObject(categoryURL);
	return 	this.jsonObject;	
	}

	public Optional<JsonObject> getNavigationData() {
		this.from=CalledFrom.NAVIGATION;
		this.jsonObject=source.rootObject(NAVIGATION_URL);
	return 	this.jsonObject;	
	}
	
	public Optional<JsonObject> dataForCategoryAndProducer(Integer categoryId, String producerSlug) {
		this.categoryId=categoryId;
		this.slug=producerSlug;
		this.from=CalledFrom.PRODUCER;
		String producerFilter = START_SLUG_URL + categoryId + QUERY_SLUG_URL + producerSlug + SLUG_END_URL;
		this.jsonObject=source.rootObject(producerFilter);
	return 	this.jsonObject;	
	}
	
	public Optional<JsonObject> dataForCategory(Integer categoryId, Integer limitResults) {
		this.categoryId=categoryId;
		this.limitResults=limitResults;
		this.from=CalledFrom.CATEGORYDATA;
		String categoryURL = CATEGORY_URL + categoryId + OFFSET_LIMIT + limitResults;
		this.jsonObject=source.rootObject(categoryURL);
	return 	this.jsonObject;	
	}
	
	public Optional<JsonObject> dataForProduct(Integer productId) {
		this.productId=productId;
		this.from=CalledFrom.PRODUCT;
		String productURL = PRODUCT_URL_START + productId + PRODUCT_URL_END;
		this.jsonObject=source.rootObject(productURL);
	return 	this.jsonObject;	
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}

	public Optional<JsonObject> getJsonObject() {
		return jsonObject;
	}	

	public String getSlug() {
		return slug;
	}

	public CalledFrom getFrom() {
		return from;
	}

	public Integer getProductId() {
		return productId;
	}

	public Integer getLimitResults() {
		return limitResults;
	}

	public void setLimitResults(Integer limitResults) {
		this.limitResults = limitResults;
	}
	

}
