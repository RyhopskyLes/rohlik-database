package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
@Component("filters")
public class Filters {
@Autowired
private RootObject rootObject;	
private String name;
private String slug;
private List<SlugAndName> items;
private static Logger log = LoggerFactory.getLogger(Filters.class);
public Filters() {	
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
		return StreamSupport.stream(filters.orElse(new JsonArray()).spliterator(), false)
				.filter(filter -> slug.equals(getFilterName(filter))).map(filter -> filter.getAsJsonObject())
				.findFirst().map(filterData -> getFilterItems(filterData))
				.map(filterItems -> getSlugsAndNames(filterItems)).get();}
	return new ArrayList<>();
}

private Optional<JsonObject> getJsonDataObject(Integer categoryId) {
	return Objects.equals(rootObject.getCategoryId(), categoryId) && Objects.equals(rootObject.getFrom(), RootObject.CalledFrom.PRODUCER) ? rootObject.getJsonObject().map(object -> object.getAsJsonObject("data"))
			:rootObject.metadataForCategory(categoryId).map(object -> object.getAsJsonObject("data"));
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
	Function<JsonObject, Integer> toAmount = object-> object.get("amount").getAsInt();
	return StreamSupport.stream(filterItems.spliterator(), false)
	.map(item->item.getAsJsonObject())
	.map(description->new SlugAndName(toSlug.apply(description), toName.apply(description), toAmount.apply(description)))
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
