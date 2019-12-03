
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "type",
    "promoted",
    "remaining",
    "price",
    "priceForUnit",
    "originalPrice",
    "discountPercentage",
    "discountPrice",
    "multipackNeedAmount",
    "multipackAsPriceDiff",
    "asPriceDiff",
    "startedAt",
    "endsAt",
    "originalSemiCaliberPricePerKg",
    "codeActivationId",
    "active",
    "soonExpirationType",
    "soonExpirationTypeId"
})
@Data
public class FirstActiveSale {

    @JsonProperty("id")
    public Integer id;
    @JsonProperty("type")
    public String type;
    @JsonProperty("promoted")
    public Boolean promoted;
    @JsonProperty("remaining")
    public Integer remaining;
    @JsonProperty("price")
    public Price price;
    @JsonProperty("priceForUnit")
    public PriceForUnit priceForUnit;
    @JsonProperty("originalPrice")
    public OriginalPrice originalPrice;
    @JsonProperty("discountPercentage")
    public Integer discountPercentage;
    @JsonProperty("discountPrice")
    public DiscountPrice discountPrice;
    @JsonProperty("multipackNeedAmount")
    public Object multipackNeedAmount;
    @JsonProperty("multipackAsPriceDiff")
    public Boolean multipackAsPriceDiff;
    @JsonProperty("asPriceDiff")
    public Boolean asPriceDiff;
    @JsonProperty("startedAt")
    public Integer startedAt;
    @JsonProperty("endsAt")
    public Integer endsAt;
    @JsonProperty("originalSemiCaliberPricePerKg")
    public String originalSemiCaliberPricePerKg;
    @JsonProperty("codeActivationId")
    public Object codeActivationId;
    @JsonProperty("active")
    public Boolean active;
    @JsonProperty("soonExpirationType")
    public String soonExpirationType;
    @JsonProperty("soonExpirationTypeId")
    public Integer soonExpirationTypeId;

}
