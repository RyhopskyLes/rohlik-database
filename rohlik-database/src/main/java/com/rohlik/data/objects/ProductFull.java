package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.rohlik.data.entities.Category;
import com.rohlik.data.objects.productparts.CategoryByProduct;
import com.rohlik.data.objects.productparts.Composition;
import com.rohlik.data.objects.productparts.Country;
import com.rohlik.data.objects.productparts.Nutrients;
import com.rohlik.data.objects.productparts.OriginalPrice;
import com.rohlik.data.objects.productparts.Price;
import com.rohlik.data.objects.productparts.PricePerUnit;
import com.rohlik.data.objects.productparts.RecommendedPricePerUnit;

import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "productId", "productName", "mainCategoryId", "imgPath", "baseLink", "link",
		"expectedAvailability", "unavailabilityReason", "preorderEnabled", "unit", "textualAmount", "semicaliber",
		"currency", "price", "pricePerUnit", "recommendedPricePerUnit", "originalPrice", "goodPrice",
		"goodPriceSalePercentage", "sales", "maxBasketAmount", "maxBasketAmountReason", "tags", "badge", "stars",
		"country", "countries", "imageScaleRatio", "imagesCount", "immediateConsumption", "watchDog", "composition",
		"companyId", "productStory", "vivino", "description", "ingredients", "nutrients", "allowedAllergens", "images",
		"producerHtml", "categories", "shelfLifeAvg", "shelfLifeMin", "leaflet", "informationBlocks", "favourite",
		"inStock" })
@Data
public class ProductFull {

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
	private Object expectedAvailability;
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
	private List<Object> sales = new ArrayList<Object>();
	@JsonProperty("maxBasketAmount")
	private Integer maxBasketAmount;
	@JsonProperty("maxBasketAmountReason")
	private String maxBasketAmountReason;
	@JsonProperty("tags")
	private List<String> tags = new ArrayList<String>();
	@JsonProperty("badge")
	private List<Object> badge = new ArrayList<Object>();
	@JsonProperty("stars")
	private Object stars;
	@JsonProperty("country")
	private Country country;
	@JsonProperty("countries")
	private List<Country> countries = new ArrayList<Country>();
	@JsonProperty("imageScaleRatio")
	private Object imageScaleRatio;
	@JsonProperty("imagesCount")
	private Integer imagesCount;
	@JsonProperty("immediateConsumption")
	private Boolean immediateConsumption;
	@JsonProperty("watchDog")
	private Boolean watchDog;
	@JsonProperty("composition")
	private Composition composition;
	@JsonProperty("companyId")
	private Integer companyId;
	@JsonProperty("productStory")
	private Object productStory;
	@JsonProperty("vivino")
	private Object vivino;
	@JsonProperty("description")
	private String description;
	@JsonProperty("ingredients")
	private String ingredients;
	@JsonProperty("nutrients")
	private Nutrients nutrients;
	@JsonProperty("allowedAllergens")
	private Boolean allowedAllergens;
	@JsonProperty("images")
	private List<String> images = new ArrayList<String>();
	@JsonProperty("producerHtml")
	private Object producerHtml;
	@JsonProperty("categories")
	private Set<CategoryByProduct> categories = new LinkedHashSet<>();
	@JsonProperty("shelfLifeAvg")
	private Integer shelfLifeAvg;
	@JsonProperty("shelfLifeMin")
	private Integer shelfLifeMin;
	@JsonProperty("leaflet")
	private Object leaflet;
	@JsonProperty("informationBlocks")
	private List<Object> informationBlocks = new ArrayList<Object>();
	@JsonProperty("favourite")
	private Boolean favourite;
	@JsonProperty("inStock")
	private Boolean inStock;

	public Set<Category> getCategoriesConverted() {
		return categories.stream().map(cat -> toCategory(cat)).collect(Collectors.toCollection(LinkedHashSet::new));
	}

	private Category toCategory(CategoryByProduct toConvert) {
		Category category = new Category();
		if (toConvert != null) {
			category.setCategoryId(toConvert.getId());
			category.setCategoryName(toConvert.getName());
			category.setParentId(toConvert.getParentId() == null ? 0 : toConvert.getParentId());
			category.setActive(true);
		}
		return category;
	}
}
