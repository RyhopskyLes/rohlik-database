package com.rohlik.data.dtos;

import java.io.Serializable;
import java.util.Objects;

import com.rohlik.data.entities.Child;


public class ChildDTO implements Serializable {	
	private static final long serialVersionUID = 6852278826326939241L;
	private Integer id;
	private Integer categoryId;
	private String categoryName;
	private Boolean active;
	
	public ChildDTO(Integer id, Integer categoryId, String categoryName, Boolean active) {
		super();
		this.id = id;
		this.categoryId = categoryId;
		this.categoryName = categoryName;
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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		return Objects.hash(active, categoryId, categoryName, id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildDTO other = (ChildDTO) obj;
		return Objects.equals(active, other.active) && Objects.equals(categoryId, other.categoryId)
				&& Objects.equals(categoryName, other.categoryName) && Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ChildDTO [id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", active="
				+ active + "]";
	}
	
	public Child toChild() {
		Child child = new Child();
		child.setId(id);
		child.setCategoryId(categoryId);
		child.setCategoryName(categoryName);
		child.setActive(active);
		return child;
	}
	

}
