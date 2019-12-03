package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Category;

import lombok.NoArgsConstructor;

@Component("navigation")
@NoArgsConstructor
public class Navigation {

	private static Logger log = LoggerFactory.getLogger(Navigation.class);
	@Autowired
	private RootObject rootObject;
	private Gson gson = new Gson();

	public List<NavigationCategoryInfo> getAllCategoriesData() {
		Optional<JsonObject> navigation = rootObject.getNavigationData()
				.map(object -> object.getAsJsonObject("navigation"));
		Set<Entry<String, JsonElement>> entrySet = navigation.map(navig -> navig.entrySet()).orElse(new HashSet<>());
		return entrySet.stream().map(entry -> entry.getValue())
				.map(elem -> gson.fromJson(elem, NavigationCategoryInfo.class))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	public List<NavigationCategoryInfo> getAllMainCategoriesInfo() {
		return getAllCategoriesData().stream().filter(cat -> cat.getParentId() != null)
				.filter(cat -> cat.getParentId().equals(0)).collect(Collectors.toCollection(ArrayList::new));

	}

	public Map<Integer, String> getAllMainCategoriesIdandName() {
		return getAllMainCategoriesInfo().stream()
				.collect(Collectors.toMap(NavigationCategoryInfo::getId, NavigationCategoryInfo::getName));

	}

	public Map<Integer, String> getAllCategoriesIdandName() {
		return getAllCategoriesData().stream()
				.collect(Collectors.toMap(NavigationCategoryInfo::getId, NavigationCategoryInfo::getName));

	}

	public Set<Category> getAllCategories() {
		return getAllCategoriesData().stream().map(NavigationCategoryInfo::toCategory)
				.collect(Collectors.toCollection(HashSet::new));
	}
	
	public String getCategoryName(Integer catNum, Set<Category> categories) {
		Predicate<Category> idEquals = category -> category.getCategoryId().equals(catNum);
		return categories.stream().filter(idEquals).map(Category::getCategoryName).findFirst().orElse("");
	}	
}