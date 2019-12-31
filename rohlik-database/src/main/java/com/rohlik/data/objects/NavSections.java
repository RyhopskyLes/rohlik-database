package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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

import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;


@Component("navSections")
public class NavSections {
	public static final String CATEGORIES_METADATA_START = "https://www.rohlik.cz/services/frontend-service/categories/";
	public static final String CATEGORIES_METADATA_END = "/metadata";
	private static Logger log = LoggerFactory.getLogger(NavSections.class);

	private RootObject rootObject;

	@Autowired
	public NavSections(RootObject rootObject) {
		this.rootObject = rootObject;
	}

	public List<NavSectionsCategoryData> ofCategory(Integer categoryId) {
		Optional<JsonObject> data = getJsonDataObject(categoryId);
		Optional<JsonArray> navSections = data.map(content -> content.getAsJsonArray("navSections"));
		if (navSections.isPresent()) {
			return StreamSupport.stream(navSections.get().spliterator(), false).map(JsonElement::getAsJsonObject)
					.map(toCategoryData(categoryId)::apply).collect(Collectors.toList());
		}
		return new ArrayList<>();
	}

	public List<Breadcrumb> breadcrumbsOfCategory(Integer categoryId) {
		Optional<JsonObject> data = getJsonDataObject(categoryId);
		Gson gson = new Gson();
		Optional<JsonArray> breadcrumbs = data.map(content -> content.getAsJsonArray("breadcrumbs"));
		if (breadcrumbs.isPresent()) {
			return StreamSupport.stream(breadcrumbs.get().spliterator(), false).map(JsonElement::getAsJsonObject)
					.map(breadcrumb->gson.fromJson(breadcrumb, Breadcrumb.class)).collect(Collectors.toCollection(LinkedList::new));
		}
		return new LinkedList<>();
	}
	
	public Optional<Category> getAsCategoryFromBreadCrumbs(Integer categoryId) {
		LinkedList<Breadcrumb> breadcrumbs = (LinkedList<Breadcrumb>) breadcrumbsOfCategory(categoryId);
		Category category = null;
		if(!breadcrumbs.isEmpty()) {
		Breadcrumb last = breadcrumbs.getLast();
		breadcrumbs.removeLast();
		Breadcrumb nexttolast = breadcrumbs.pollLast();
		Integer parentId= nexttolast==null ? 0 : nexttolast.getCategoryId();
		category = new Category();
		category.setCategoryId(last.getCategoryId());
		category.setCategoryName(last.getTitle());
		category.setParentId(parentId);
		category.setActive(true);
		}	
		return Optional.ofNullable(category);
	}
	
	public Optional<Child> getAsChildFromBreadcrumbs(Integer categoryId) {
		LinkedList<Breadcrumb> breadcrumbs = (LinkedList<Breadcrumb>) breadcrumbsOfCategory(categoryId);
		Child child = null;
		if(!breadcrumbs.isEmpty()) {
			Breadcrumb last = breadcrumbs.getLast();
			child = new Child();
			child.setCategoryId(last.getCategoryId());
			child.setCategoryName(last.getTitle());
			child.setActive(true);
			}	
		return Optional.ofNullable(child);		
	}
	
	public Set<NavSectionsCategoryData> completeTreeOfCategory(Integer categoryId) {
		List<NavSectionsCategoryData> topLevel = ofCategory(categoryId);
		Set<NavSectionsCategoryData> tree = new HashSet<>(topLevel);
		Set<NavSectionsCategoryData> toIterate = new HashSet<>(topLevel);

		while (!toIterate.isEmpty()) {
			toIterate = toIterate.stream().map(categorydata -> ofCategory(categorydata.getCategoryId()))
					.filter(list -> !list.isEmpty()).flatMap(List::stream).map(categoryData -> {
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
			toIterate = toIterate.stream().map(categorydata -> {
				List<NavSectionsCategoryData> ofCategory = ofCategory(categorydata.getCategoryId());
				if (ofCategory.isEmpty()) {
					tree.add(categorydata);
				}				
				return ofCategory;
			}).filter(list -> !list.isEmpty()).flatMap(List::stream)
					.collect(Collectors.toCollection(HashSet::new));
		}
		return tree;
	}

	private Function<JsonObject, NavSectionsCategoryData> toCategoryData(Integer parentId) {
		return object -> {
		    NavSectionsCategoryData navSectionsCategoryData = new NavSectionsCategoryData().deserializeFromJson(object);
		    navSectionsCategoryData.setParentId(parentId);
			navSectionsCategoryData.setMetadataLink(
					CATEGORIES_METADATA_START + navSectionsCategoryData.getCategoryId() + CATEGORIES_METADATA_END);
			return navSectionsCategoryData;
		};
	};

	private Optional<JsonObject> getJsonDataObject(Integer categoryId) {
		return Objects.equals(rootObject.getCategoryId(), categoryId)
				&& Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.METADATA)
						? rootObject.getJsonObject().map(object -> object.getAsJsonObject("data"))
						: rootObject.metadataForCategory(categoryId).map(object -> object.getAsJsonObject("data"));
	}

}
