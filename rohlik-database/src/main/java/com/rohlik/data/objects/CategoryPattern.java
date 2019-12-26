package com.rohlik.data.objects;


import com.google.gson.JsonObject;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;

public abstract class CategoryPattern {
	protected Integer categoryId;
	protected String categoryName;	
	protected Integer parentId;
	
	public CategoryPattern() {		
	}
	public CategoryPattern(Integer categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	
	public CategoryPattern(Integer categoryId, String categoryName, Integer parentId) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.parentId = parentId;
	}
	
	
	
	public abstract Category toCategory();
	public abstract Child toChild();
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public abstract CategoryPattern deserializeFromJson(JsonObject object);
	

}
