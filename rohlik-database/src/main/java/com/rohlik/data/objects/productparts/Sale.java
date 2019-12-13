
package com.rohlik.data.objects.productparts;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "type", "promoted", "remaining", "price", "priceForUnit", "originalPrice",
		"discountPercentage", "discountPrice", "multipackNeedAmount", "multipackAsPriceDiff", "asPriceDiff",
		"startedAt", "endsAt", "originalSemiCaliberPricePerKg", "codeActivationId", "active", "soonExpirationType",
		"soonExpirationTypeId" })
@Data
public class Sale {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("type")
	private String type;
	@JsonProperty("promoted")
	private Boolean promoted;
	@JsonProperty("remaining")
	private Integer remaining;
	@JsonProperty("price")
	private Price price;
	@JsonProperty("priceForUnit")
	private PriceForUnit priceForUnit;
	@JsonProperty("originalPrice")
	private OriginalPrice originalPrice;
	@JsonProperty("discountPercentage")
	private Integer discountPercentage;
	@JsonProperty("discountPrice")
	private DiscountPrice discountPrice;
	@JsonProperty("multipackNeedAmount")
	private Object multipackNeedAmount;
	@JsonProperty("multipackAsPriceDiff")
	private Boolean multipackAsPriceDiff;
	@JsonProperty("asPriceDiff")
	private Boolean asPriceDiff;
	@JsonProperty("startedAt")
	private Integer startedAt;
	@JsonProperty("endsAt")
	private Integer endsAt;
	@JsonProperty("originalSemiCaliberPricePerKg")
	private String originalSemiCaliberPricePerKg;
	@JsonProperty("codeActivationId")
	private Object codeActivationId;
	@JsonProperty("active")
	private Boolean active;
	@JsonProperty("soonExpirationType")
	private String soonExpirationType;
	@JsonProperty("soonExpirationTypeId")
	private Integer soonExpirationTypeId;

	public com.rohlik.data.entities.Sale toSale() {
		com.rohlik.data.entities.Sale sale = new com.rohlik.data.entities.Sale();
		sale.setType(this.getType());
		sale.setDiscountPercentage(this.getDiscountPercentageAsDouble());
		sale.setDiscountPrice(this.getFullDiscountPrice());
		sale.setOriginalPrice(this.getFullOriginalPrice());
		sale.setPrice(getFullPrice());
		sale.setPricePerUnit(this.getFullPriceForUnit());
		sale.setPromoted(this.getPromoted());
		return sale;
	}

	public Double getDiscountPercentageAsDouble() {
		if (this.getDiscountPercentage() != null)
			return this.getDiscountPercentage().doubleValue();
		return null;
	}

	public Double getFullDiscountPrice() {
		if (this.getDiscountPrice() != null && this.getDiscountPrice().getFull() != null) {
			return this.getDiscountPrice().getFull().doubleValue();
		}
		return null;
	}

	public Double getFullOriginalPrice() {
		if (this.getOriginalPrice() != null && this.getOriginalPrice().getFull() != null) {
			return this.getOriginalPrice().getFull().doubleValue();
		}
		return null;
	}

	public Double getFullPrice() {
		if (this.getPrice() != null && this.getPrice().getFull() != null) {
			return this.getPrice().getFull().doubleValue();
		}
		return null;
	}

	public Double getFullPriceForUnit() {
		if (this.getPriceForUnit() != null && this.getPriceForUnit().getFull() != null) {
			return this.getPriceForUnit().getFull().doubleValue();
		}
		return null;
	}
}
