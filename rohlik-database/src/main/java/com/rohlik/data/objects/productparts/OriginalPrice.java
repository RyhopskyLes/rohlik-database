
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
public class OriginalPrice {

    @JsonProperty("full")
    public Integer full;
    @JsonProperty("whole")
    public Integer whole;
    @JsonProperty("fraction")
    public Integer fraction;
    @JsonProperty("currency")
    public String currency;

}
