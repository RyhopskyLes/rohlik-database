package com.rohlik.data.objects;

import java.util.Objects;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.kosik.interfaces.Setter;
import com.rohlik.data.objects.jsondeserializers.NavSectionsCategoryDataDeserializer;

public class NavSectionsCategoryData extends CategoryPattern {
//private Integer categoryId;
//private Integer parentId;
//private String categoryName;
private String metadataLink;
private Integer productDisplayCount;
private Integer productsCount;
public NavSectionsCategoryData() {	
	super();
}
public NavSectionsCategoryData(Integer categoryId, String categoryName) {
	super(categoryId, categoryName);
}

public NavSectionsCategoryData(Integer categoryId, String categoryName, Integer productDisplayCount,
		Integer productsCount) {
	super(categoryId, categoryName);
	this.productDisplayCount = productDisplayCount;
	this.productsCount = productsCount;
}
public <T> NavSectionsCategoryData set(Setter<T> setter, T param) {
	setter.accept(param);
	return this;
}

public String getMetadataLink() {
	return metadataLink;
}
public void setMetadataLink(String metadataLink) {
	this.metadataLink = metadataLink;
}
public Integer getProductDisplayCount() {
	return productDisplayCount;
}
public void setProductDisplayCount(Integer productDisplayCount) {
	this.productDisplayCount = productDisplayCount;
}
public Integer getProductsCount() {
	return productsCount;
}
public void setProductsCount(Integer productsCount) {
	this.productsCount = productsCount;
}
@Override
public String toString() {
	return "NavSectionsCategoryData [categoryId=" + categoryId + ", parentId=" + parentId + ", categoryName=" + categoryName
			+ ", metadataLink=" + metadataLink + ", productDisplayCount=" + productDisplayCount + ", productsCount="
			+ productsCount + "]";
}
@Override
public int hashCode() {
	return Objects.hash(categoryId, metadataLink, categoryName, parentId, productDisplayCount, productsCount);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	NavSectionsCategoryData other = (NavSectionsCategoryData) obj;
	return Objects.equals(categoryId, other.categoryId) && Objects.equals(metadataLink, other.metadataLink)
			&& Objects.equals(categoryName, other.categoryName) && Objects.equals(parentId, other.parentId)
			&& Objects.equals(productDisplayCount, other.productDisplayCount)
			&& Objects.equals(productsCount, other.productsCount);
}


public Category toCategory() {
	Category category = new Category();
	category.setCategoryId(this.categoryId);
	category.setCategoryName(this.categoryName);
	category.setParentId(this.parentId);
	category.setActive(true);
	return category;
}

public Child toChild() {
	Child child = new Child();
	child.setCategoryId(this.categoryId);
	child.setCategoryName(this.categoryName);
	child.setActive(true);
	return child;
}

@Override
//fields parentId and metadataLink not included in json
public NavSectionsCategoryData deserializeFromJson(JsonObject object) {
	GsonBuilder gsonBldr = new GsonBuilder();
    gsonBldr.registerTypeAdapter(NavSectionsCategoryData.class, new NavSectionsCategoryDataDeserializer());
    return gsonBldr.create().fromJson(object, NavSectionsCategoryData.class);
}


}
