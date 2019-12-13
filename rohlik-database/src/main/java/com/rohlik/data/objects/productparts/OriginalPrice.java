
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
    private Integer full;
    @JsonProperty("whole")
    private Integer whole;
    @JsonProperty("fraction")
    private Integer fraction;
    @JsonProperty("currency")
    private String currency;

}
