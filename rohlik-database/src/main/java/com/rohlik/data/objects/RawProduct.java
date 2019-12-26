
package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

@Data
public class RawProduct {

	private Integer productId;
	private String productName;
	private Integer mainCategoryId;
	private String imgPath;
	private String baseLink;
	private String link;
	private Integer expectedAvailability;
	private Boolean expectedAvailabilityHasTime;
	private Object unavailabilityReason;
	private Boolean preorderEnabled;
	private String unit;
	private String textualAmount;
	private Boolean semicaliber;
	private String currency;
	private Price price;
	private PricePerUnit pricePerUnit;
	private RecommendedPricePerUnit recommendedPricePerUnit;
	private OriginalPrice originalPrice;
	private Boolean goodPrice;
	private Integer goodPriceSalePercentage;
	private List<Sale> sales = new ArrayList<>();
	private Integer maxBasketAmount;
	private String maxBasketAmountReason;
	private List<String> tags = new ArrayList<>();
	private List<Badge> badge = new ArrayList<>();
	private Integer favouriteCount;
	private Object stars;
	private Country country;
	private List<Country> countries = new ArrayList<>();
	private Integer orderCount;
	private Integer imageScaleRatio;
	private Integer imagesCount;
	private Boolean immediateConsumption;
	private Boolean babyClubUser;
	private Boolean watchDog;
	private Composition composition;
	private Integer companyId;
	private String producerHtml;
	private Object productStory;
	private Object vivino;
	private Boolean inStock;
	private FirstActiveSale firstActiveSale;
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
		if(this.getImgPath()!=null) {product.setImgPath(this.getImgPath());} else {product.setImgPath("");}
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
