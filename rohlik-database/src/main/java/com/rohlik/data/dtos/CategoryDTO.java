package com.rohlik.data.dtos;

import java.util.Objects;

import com.rohlik.data.entities.Category;

public class CategoryDTO {
	private Integer id;
	private Integer categoryId;
	private String categoryName;
	private Integer parentId;
	private Boolean active;
	public CategoryDTO(Integer id, Integer categoryId, String categoryName, Integer parentId, Boolean active) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.parentId = parentId;
		this.active = active;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	@Override
	public int hashCode() {
		return Objects.hash(active, categoryId, categoryName, id, parentId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryDTO other = (CategoryDTO) obj;
		return Objects.equals(active, other.active) && Objects.equals(categoryId, other.categoryId)
				&& Objects.equals(categoryName, other.categoryName) && Objects.equals(id, other.id)
				&& Objects.equals(parentId, other.parentId);
	}
	@Override
	public String toString() {
		return "CategoryDTO [id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", parentId="
				+ parentId + ", active=" + active + "]";
	}
	
	public Category toCategory() {
		Category category = new Category();
		category.setId(id);
		category.setCategoryId(categoryId);
		category.setCategoryName(categoryName);
		category.setParentId(parentId);
		category.setActive(active);
		return category;
	}
}
