package com.rohlik.data.objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;

import lombok.Data;

@Data
public class NavigationCategoryInfo {
	private Integer id;
	private Integer occurrence;
	private Integer position;
	private String name;
	private Boolean aboveAverage;
	private String link;
	private Integer companyId;
	private Integer parentId;
	private List<Integer> children = new ArrayList<>();
	
	public Category toCategory() {
	Category category = new Category();
	category.setParentId(parentId);
	category.setCategoryId(id);
	category.setCategoryName(name);
	category.setActive(true);
	return category;	
		
	}
	
	public Child toChild() {
		Child child = new Child();
		child.setCategoryId(id);
		child.setCategoryName(name);
		child.setActive(true);
		return child;			
		}
}
