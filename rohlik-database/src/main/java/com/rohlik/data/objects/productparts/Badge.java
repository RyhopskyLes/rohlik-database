
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "slug",
    "label",
    "dataTip"
})
@Data
public class Badge {

    @JsonProperty("slug")
    public String slug;
    @JsonProperty("label")
    public String label;
    @JsonProperty("dataTip")
    public String dataTip;

}
