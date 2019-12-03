
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "nameId"
})
@Data
public class Country {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("nameId")
    public String nameId;

}
