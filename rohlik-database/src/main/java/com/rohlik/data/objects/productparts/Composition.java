
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "additiveScoreMax",
    "withoutAdditives",
    "nutritionalValues",
    "withoutE250E251"
})
@Data
public class Composition {

    @JsonProperty("additiveScoreMax")
    public Integer additiveScoreMax;
    @JsonProperty("withoutAdditives")
    public Boolean withoutAdditives;
    @JsonProperty("nutritionalValues")
    public NutritionalValues nutritionalValues;
    @JsonProperty("withoutE250E251")
    public Boolean withoutE250E251;

}
