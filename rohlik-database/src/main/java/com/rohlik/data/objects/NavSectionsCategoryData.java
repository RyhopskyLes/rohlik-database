package com.rohlik.data.objects;

import java.util.Objects;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.kosik.interfaces.Setter;

public class NavSectionsCategoryData {
private Integer categoryId;
private Integer parentCategoryId;
private String name;
private String metadataLink;
private Integer productDisplayCount;
private Integer productsCount;
public NavSectionsCategoryData() {	
}
public NavSectionsCategoryData(Integer categoryId, String name) {
	this.categoryId = categoryId;
	this.name = name;
}

public <T> NavSectionsCategoryData set(Setter<T> setter, T param) {
	setter.accept(param);
	return this;
}
public Integer getCategoryId() {
	return categoryId;
}
public void setCategoryId(Integer categoryId) {
	this.categoryId = categoryId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
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
	return "NavSectionsCategoryData [categoryId=" + categoryId + ", parentCategoryId=" + parentCategoryId + ", name=" + name
			+ ", metadataLink=" + metadataLink + ", productDisplayCount=" + productDisplayCount + ", productsCount="
			+ productsCount + "]";
}
@Override
public int hashCode() {
	return Objects.hash(categoryId, metadataLink, name, parentCategoryId, productDisplayCount, productsCount);
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
			&& Objects.equals(name, other.name) && Objects.equals(parentCategoryId, other.parentCategoryId)
			&& Objects.equals(productDisplayCount, other.productDisplayCount)
			&& Objects.equals(productsCount, other.productsCount);
}
public Integer getParentCategoryId() {
	return parentCategoryId;
}
public void setParentCategoryId(Integer parentCategoryId) {
	this.parentCategoryId = parentCategoryId;
}

public Category toCategory() {
	Category category = new Category();
	category.setCategoryId(this.categoryId);
	category.setCategoryName(this.name);
	category.setParentId(this.parentCategoryId);
	category.setActive(true);
	return category;
}

public Child toChild() {
	Child child = new Child();
	child.setCategoryId(this.categoryId);
	child.setCategoryName(this.name);
	child.setActive(true);
	return child;
}

}
