package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"heading",
"values"
})
@Data
public class Nutrients {

@JsonProperty("heading")
public Object heading;
@JsonProperty("values")
public Object values;

}
