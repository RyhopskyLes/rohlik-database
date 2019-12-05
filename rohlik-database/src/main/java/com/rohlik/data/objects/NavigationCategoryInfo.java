package com.rohlik.data.objects;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rohlik.data.entities.Category;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"occurrence",
"position",
"name",
"aboveAverage",
"link",
"companyId",
"parentId",
"children"
})
@Data
public class NavigationCategoryInfo {
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("occurrence")
	private Integer occurrence;
	@JsonProperty("position")
	private Integer position;
	@JsonProperty("name")
	private String name;
	@JsonProperty("aboveAverage")
	private Boolean aboveAverage;
	@JsonProperty("link")
	private String link;
	@JsonProperty("companyId")
	private Integer companyId;
	@JsonProperty("parentId")
	private Integer parentId;
	@JsonProperty("children")
	private List<Integer> children = new ArrayList<>();
	
	Category toCategory() {
	Category category = new Category();
	category.setParentId(parentId);
	category.setCategoryId(id);
	category.setCategoryName(name);
	return category;	
		
	}
}
