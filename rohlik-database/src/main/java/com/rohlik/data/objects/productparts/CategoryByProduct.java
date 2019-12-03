package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"name",
"nameId",
"parentId",
"level",
"topParentName",
"nameLong",
"showDurability",
"primary"
})
@Data
public class CategoryByProduct {

@JsonProperty("id")
private Integer id;
@JsonProperty("name")
private String name;
@JsonProperty("nameId")
private String nameId;
@JsonProperty("parentId")
private Integer parentId;
@JsonProperty("level")
private Integer level;
@JsonProperty("topParentName")
private Object topParentName;
@JsonProperty("nameLong")
private Object nameLong;
@JsonProperty("showDurability")
private Object showDurability;
@JsonProperty("primary")
private Boolean primary;

}
