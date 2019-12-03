
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "dose",
    "energyValueKJ",
    "energyValueKcal",
    "fats",
    "saturatedFattyAcids",
    "carbohydrates",
    "sugars",
    "proteins",
    "salt",
    "fiber"
})
@Data
public class NutritionalValues {

    @JsonProperty("dose")
    public String dose;
    @JsonProperty("energyValueKJ")
    public Double energyValueKJ;
    @JsonProperty("energyValueKcal")
    public Double energyValueKcal;
    @JsonProperty("fats")
    public Double fats;
    @JsonProperty("saturatedFattyAcids")
    public Double saturatedFattyAcids;
    @JsonProperty("carbohydrates")
    public Double carbohydrates;
    @JsonProperty("sugars")
    public Double sugars;
    @JsonProperty("proteins")
    public Double proteins;
    @JsonProperty("salt")
    public Double salt;
    @JsonProperty("fiber")
    public Object fiber;

}
