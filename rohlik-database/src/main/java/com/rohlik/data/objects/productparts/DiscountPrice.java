
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "full",
    "whole",
    "fraction",
    "currency"
})
@Data
public class DiscountPrice {

    @JsonProperty("full")
    public Double full;
    @JsonProperty("whole")
    public Integer whole;
    @JsonProperty("fraction")
    public Double fraction;
    @JsonProperty("currency")
    public String currency;

}
