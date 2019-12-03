package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rohlik.data.commons.utilities.DataRohlik;
import com.rohlik.data.commons.utilities.Source;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;

@Component("navSections")
public class NavSections {
	public static final String CATEGORIES_METADATA_START = "https://www.rohlik.cz/services/frontend-service/categories/";
	public static final String CATEGORIES_METADATA_END = "/metadata";
	private static Logger log = LoggerFactory.getLogger(NavSections.class);
	@Autowired
	private RootObject rootObject;

	public NavSections() {

	}

	public List<NavSectionsCategoryData> ofCategory(Integer categoryId) {
		Optional<JsonObject> data = getJsonDataObject(categoryId);				
		Optional<JsonArray> navSections = data.map(content->content.getAsJsonArray("navSections"));
		if(navSections.isPresent()) {return StreamSupport.stream(navSections.get().spliterator(), false)
			.map( elem-> elem.getAsJsonObject())
			.map(toCategoryData(categoryId)::apply)
			.collect(Collectors.toList());					
		}		
		return new ArrayList<>();
	}

	public Set<NavSectionsCategoryData> completeTreeOfCategory(Integer categoryId) {
		List<NavSectionsCategoryData> topLevel = ofCategory(categoryId);
		Set<NavSectionsCategoryData> tree = new HashSet<>(topLevel);
		Set<NavSectionsCategoryData> toIterate = new HashSet<>(topLevel);

		while (!toIterate.isEmpty()) {
			toIterate = toIterate.stream().map(categorydata -> ofCategory(categorydata.getCategoryId()))
					.filter(list -> !list.isEmpty()).flatMap(list -> list.stream()).map(categoryData -> {
						tree.add(categoryData);
						return categoryData;
					}).collect(Collectors.toCollection(HashSet::new));
		}
		return tree;
	}

	public Set<NavSectionsCategoryData> lowestLevelChildrenOfCategory(Integer categoryId) {
		List<NavSectionsCategoryData> topLevel = ofCategory(categoryId);
		Set<NavSectionsCategoryData> tree = new HashSet<>();
		Set<NavSectionsCategoryData> toIterate = new HashSet<>(topLevel);

		while (!toIterate.isEmpty()) {
			toIterate = toIterate.stream().map(categorydata ->{
				List<NavSectionsCategoryData> ofCategory = ofCategory(categorydata.getCategoryId());
				if(ofCategory.isEmpty()) {tree.add(categorydata);};
			return ofCategory;
			})	.filter(list -> !list.isEmpty()).flatMap(list -> list.stream())					
				.collect(Collectors.toCollection(HashSet::new));
		}
		return tree;
	}
	
	private Function<JsonObject, NavSectionsCategoryData> toCategoryData(Integer parentId) {return  object -> {
		NavSectionsCategoryData navSectionsCategoryData = new NavSectionsCategoryData();
		Optional<JsonElement> productDisplayCount = Optional.ofNullable(object.get("productDisplayCount"));
		Optional<JsonElement> productsCount = Optional.ofNullable(object.get("productsCount"));
		navSectionsCategoryData.setParentCategoryId(parentId);
		navSectionsCategoryData.setCategoryId(object.get("categoryId").getAsInt());
		navSectionsCategoryData.setName(object.get("name").getAsString());
		navSectionsCategoryData
				.setMetadataLink(CATEGORIES_METADATA_START + navSectionsCategoryData.getCategoryId() + CATEGORIES_METADATA_END);
		productDisplayCount.ifPresent(element -> navSectionsCategoryData.setProductDisplayCount(element.getAsInt()));
		productsCount.ifPresent(element -> navSectionsCategoryData.setProductsCount(element.getAsInt()));
		return navSectionsCategoryData;};
	};
	
	private Optional<JsonObject> getJsonDataObject(Integer categoryId) {
		return Objects.equals(rootObject.getCategoryId(), categoryId) && Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.METADATA) ? rootObject.getJsonObject().map(object -> object.getAsJsonObject("data"))
				:rootObject.metadataForCategory(categoryId).map(object -> object.getAsJsonObject("data"));
	}

}
