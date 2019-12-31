package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
@Component("filters")
public class Filters {
private RootObject rootObject;	
private String name;
private String slug;
private List<SlugAndName> items;
private static Logger log = LoggerFactory.getLogger(Filters.class);
@Autowired
public Filters(RootObject rootObject) {	
	this.rootObject=rootObject;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSlug() {
	return slug;
}
public void setSlug(String slug) {
	this.slug = slug;
}
public List<SlugAndName> getItems() {
	return items;
}
public void setItems(List<SlugAndName> items) {
	this.items = items;
}

public List<SlugAndName> forCategoryAndSlug(Integer categoryId, String slug) {
	Optional<JsonObject> data = getJsonDataObject(categoryId);
	Optional<JsonArray> filters = data.map(content->content.getAsJsonArray("filters"));
	if(filters.isPresent()) {
		return StreamSupport.stream(filters.orElseGet(JsonArray::new).spliterator(), false)
				.filter(filter -> slug.equals(getFilterName(filter))).map(JsonElement::getAsJsonObject)
				.findFirst().map(this::getFilterItems)
				.map(this::getSlugsAndNames).orElseGet(ArrayList::new);}
	return new ArrayList<>();
}

private Optional<JsonObject> getJsonDataObject(Integer categoryId) {
	return rootObject.metadataForCategory(categoryId).map(object -> object.getAsJsonObject("data"));
}

private String getFilterName(JsonElement filter) {
	return filter.getAsJsonObject().get("slug").getAsString();
}

private JsonArray getFilterItems(JsonObject filterData) {
	return filterData.get("items").getAsJsonArray();
}

private List<SlugAndName> getSlugsAndNames(JsonArray filterItems) {
	Function<JsonObject, String> toSlug = object-> object.get("slug").getAsString();
	Function<JsonObject, String> toName = object-> object.get("name").getAsString();
	ToIntFunction<JsonObject> toAmount = object-> object.get("amount").getAsInt();
	return StreamSupport.stream(filterItems.spliterator(), false)
	.map(JsonElement::getAsJsonObject)
	.map(description->new SlugAndName(toSlug.apply(description), toName.apply(description), toAmount.applyAsInt(description)))
	.collect(Collectors.toCollection(ArrayList::new));	
}

@Override
public String toString() {
	return "Filters [name=" + name + ", slug=" + slug + ", items=" + items + "]";
}
@Override
public int hashCode() {
	return Objects.hash(items, name, slug);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	Filters other = (Filters) obj;
	return Objects.equals(items, other.items) && Objects.equals(name, other.name) && Objects.equals(slug, other.slug);
}

}
