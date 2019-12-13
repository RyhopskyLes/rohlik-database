
package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.productparts.Badge;
import com.rohlik.data.objects.productparts.Composition;
import com.rohlik.data.objects.productparts.Country;
import com.rohlik.data.objects.productparts.FirstActiveSale;
import com.rohlik.data.objects.productparts.OriginalPrice;
import com.rohlik.data.objects.productparts.Price;
import com.rohlik.data.objects.productparts.PricePerUnit;
import com.rohlik.data.objects.productparts.RecommendedPricePerUnit;
import com.rohlik.data.objects.productparts.Sale;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "productId", "productName", "mainCategoryId", "imgPath", "baseLink", "link",
		"expectedAvailability", "expectedAvailabilityHasTime", "unavailabilityReason", "preorderEnabled", "unit",
		"textualAmount", "semicaliber", "currency", "price", "pricePerUnit", "recommendedPricePerUnit", "originalPrice",
		"goodPrice", "goodPriceSalePercentage", "sales", "maxBasketAmount", "maxBasketAmountReason", "tags", "badge",
		"favouriteCount", "stars", "country", "countries", "orderCount", "imageScaleRatio", "imagesCount",
		"immediateConsumption", "babyClubUser", "watchDog", "composition", "companyId", "producerHtml", "productStory",
		"vivino", "inStock", "firstActiveSale", "favourite" })
@Data
public class RawProduct {

	@JsonProperty("productId")
	private Integer productId;
	@JsonProperty("productName")
	private String productName;
	@JsonProperty("mainCategoryId")
	private Integer mainCategoryId;
	@JsonProperty("imgPath")
	private String imgPath;
	@JsonProperty("baseLink")
	private String baseLink;
	@JsonProperty("link")
	private String link;
	@JsonProperty("expectedAvailability")
	private Integer expectedAvailability;
	@JsonProperty("expectedAvailabilityHasTime")
	private Boolean expectedAvailabilityHasTime;
	@JsonProperty("unavailabilityReason")
	private Object unavailabilityReason;
	@JsonProperty("preorderEnabled")
	private Boolean preorderEnabled;
	@JsonProperty("unit")
	private String unit;
	@JsonProperty("textualAmount")
	private String textualAmount;
	@JsonProperty("semicaliber")
	private Boolean semicaliber;
	@JsonProperty("currency")
	private String currency;
	@JsonProperty("price")
	private Price price;
	@JsonProperty("pricePerUnit")
	private PricePerUnit pricePerUnit;
	@JsonProperty("recommendedPricePerUnit")
	private RecommendedPricePerUnit recommendedPricePerUnit;
	@JsonProperty("originalPrice")
	private OriginalPrice originalPrice;
	@JsonProperty("goodPrice")
	private Boolean goodPrice;
	@JsonProperty("goodPriceSalePercentage")
	private Integer goodPriceSalePercentage;
	@JsonProperty("sales")
	private List<Sale> sales = new ArrayList<>();
	@JsonProperty("maxBasketAmount")
	private Integer maxBasketAmount;
	@JsonProperty("maxBasketAmountReason")
	private String maxBasketAmountReason;
	@JsonProperty("tags")
	private List<String> tags = new ArrayList<>();
	@JsonProperty("badge")
	private List<Badge> badge = new ArrayList<>();
	@JsonProperty("favouriteCount")
	private Integer favouriteCount;
	@JsonProperty("stars")
	private Object stars;
	@JsonProperty("country")
	private Country country;
	@JsonProperty("countries")
	private List<Country> countries = new ArrayList<>();
	@JsonProperty("orderCount")
	private Integer orderCount;
	@JsonProperty("imageScaleRatio")
	private Integer imageScaleRatio;
	@JsonProperty("imagesCount")
	private Integer imagesCount;
	@JsonProperty("immediateConsumption")
	private Boolean immediateConsumption;
	@JsonProperty("babyClubUser")
	private Boolean babyClubUser;
	@JsonProperty("watchDog")
	private Boolean watchDog;
	@JsonProperty("composition")
	private Composition composition;
	@JsonProperty("companyId")
	private Integer companyId;
	@JsonProperty("producerHtml")
	private String producerHtml;
	@JsonProperty("productStory")
	private Object productStory;
	@JsonProperty("vivino")
	private Object vivino;
	@JsonProperty("inStock")
	private Boolean inStock;
	@JsonProperty("firstActiveSale")
	private FirstActiveSale firstActiveSale;
	@JsonProperty("favourite")
	private Boolean favourite;

	Product toProduct() {
    	Product product = new Product();
		product.setProductId(this.getProductId());
		product.setProductName(this.getProductName());
		product.setOriginalPrice(this.getFullOriginalPrice());
		product.setPrice(this.getFullPrice());
		product.setTextualAmount(this.getTextualAmount());
		product.setUnit(this.getUnit());
		product.setBaseLink(this.getBaseLink());
		product.setImgPath(this.getImgPath());
		product.setInStock(this.getInStock());
		product.setLink(this.getLink());
		product.setPricePerUnit(this.getFullPricePerUnit());
		product.setMainCategoryId(this.getMainCategoryId());
		product.setActive(true);
		if(!this.getSales().isEmpty()) product.setHasSales(true);
		product.setFromRohlik(true);    	
		return product;    	
    }

	Product toProductWithSales() {
		Product product = this.toProduct();
		Set<com.rohlik.data.entities.Sale> salesConverted = this.getSales().stream().map(Sale::toSale).collect(Collectors.toCollection(HashSet::new));
    	salesConverted.forEach(product::addSales);
		return product;
		   	
    }

	public Double getFullOriginalPrice() {
		if (this.getOriginalPrice() != null && this.getOriginalPrice().getFull() != null ) {
			return this.getOriginalPrice().getFull().doubleValue();
		}
		return 0.0;
	}
	
	public Double getFullPrice() {
		if (this.getPrice() != null && this.getPrice().getFull() != null) {
				return this.getPrice().getFull().doubleValue();
		}
		return 0.0;
	}
	
	public Double getFullPricePerUnit() {
		if (this.getPricePerUnit() != null && this.getPricePerUnit().getFull() != null) {
				return this.getPricePerUnit().getFull().doubleValue();
		}
		return 0.0;
	}
}
