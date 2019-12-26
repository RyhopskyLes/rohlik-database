package com.rohlik.data.objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.jsondeserializers.NavSectionsCategoryDataDeserializer;
import com.rohlik.data.objects.jsondeserializers.NavigationCategoryInfoDeserializer;
import com.rohlik.data.patterns.CategoryPattern;

import lombok.Data;

//@Data
public class NavigationCategoryInfo  extends CategoryPattern {
//	private Integer categoryId;
	private Integer occurrence;
	private Integer position;
//	private String categoryName;
	private Boolean aboveAverage;
	private String link;
	private Integer companyId;
	
public NavigationCategoryInfo() {
		super();
			}

public NavigationCategoryInfo(Integer categoryId, String categoryName, Integer parentId, Integer occurrence, Integer position,
			Boolean aboveAverage, String link, Integer companyId, List<Integer> children) {
		super(categoryId, categoryName, parentId);
		this.occurrence = occurrence;
		this.position = position;
		this.aboveAverage = aboveAverage;
		this.link = link;
		this.companyId = companyId;
		this.children = children;
	}

	//	private Integer parentId;
	private List<Integer> children = new ArrayList<>();
	
	public Category toCategory() {
	Category category = new Category();
	category.setParentId(this.parentId);
	category.setCategoryId(this.categoryId);
	category.setCategoryName(this.categoryName);
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
	public NavigationCategoryInfo deserializeFromJson(JsonObject object) {
		GsonBuilder gsonBldr = new GsonBuilder();
	    gsonBldr.registerTypeAdapter(NavigationCategoryInfo.class, new NavigationCategoryInfoDeserializer());
	    return gsonBldr.create().fromJson(object, NavigationCategoryInfo.class);
	}

	public Integer getOccurrence() {
		return occurrence;
	}

	public void setOccurrence(Integer occurrence) {
		this.occurrence = occurrence;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Boolean getAboveAverage() {
		return aboveAverage;
	}

	public void setAboveAverage(Boolean aboveAverage) {
		this.aboveAverage = aboveAverage;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public List<Integer> getChildren() {
		return children;
	}

	public void setChildren(List<Integer> children) {
		this.children = children;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.categoryId, this.categoryName, this.parentId, aboveAverage, children, companyId, link, occurrence, position);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NavigationCategoryInfo other = (NavigationCategoryInfo) obj;
		return Objects.equals(aboveAverage, other.aboveAverage) && Objects.equals(children, other.children)
			&&	Objects.equals(this.categoryId, other.categoryId) && Objects.equals(this.categoryName, other.categoryName)
			&& Objects.equals(this.parentId, other.parentId)	
			&& Objects.equals(companyId, other.companyId) && Objects.equals(link, other.link)
				&& Objects.equals(occurrence, other.occurrence) && Objects.equals(position, other.position);
	}

	@Override
	public String toString() {
		return "NavigationCategoryInfo [categoryId="+categoryId+", categoryName="+categoryName+", parentId="+parentId+", occurrence=" + occurrence + ", position=" + position + ", aboveAverage="
				+ aboveAverage + ", link=" + link + ", companyId=" + companyId + ", children=" + children + "]";
	}
}
