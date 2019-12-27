package com.rohlik.data.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.productparts.Badge;
import com.rohlik.data.objects.productparts.Composition;
import com.rohlik.data.objects.productparts.Country;
import com.rohlik.data.objects.productparts.OriginalPrice;
import com.rohlik.data.objects.productparts.Price;
import com.rohlik.data.objects.productparts.PricePerUnit;
import com.rohlik.data.objects.productparts.RecommendedPricePerUnit;
import com.rohlik.data.objects.productparts.Sale;

public abstract class ProductPattern {
	
	protected Integer productId;
	protected String productName;
	protected Integer mainCategoryId;
	protected String imgPath;
	protected String baseLink;
	protected String link;
	protected Integer expectedAvailability;
	protected Object unavailabilityReason;
	protected Boolean preorderEnabled;
	protected String unit;
	protected String textualAmount;
	protected Boolean semicaliber;
	protected String currency;
	protected Price price;
	protected PricePerUnit pricePerUnit;
	protected RecommendedPricePerUnit recommendedPricePerUnit;
	protected OriginalPrice originalPrice;
	protected Boolean goodPrice;
	protected Integer goodPriceSalePercentage;
	protected List<Sale> sales = new ArrayList<>();
	protected Integer maxBasketAmount;
	protected String maxBasketAmountReason;
	protected List<String> tags = new ArrayList<>();
	protected List<Badge> badge = new ArrayList<>();
	protected Object stars;
	protected Country country;
	protected List<Country> countries = new ArrayList<>();
	protected Integer imageScaleRatio;
	protected Integer imagesCount;
	protected Boolean immediateConsumption;
	protected Boolean babyClubUser;
	protected Boolean watchDog;
	protected Composition composition;
	protected Integer companyId;
	protected String producerHtml;
	protected Object productStory;
	protected Object vivino;
	protected Boolean inStock;
	protected Boolean favourite;
	
	public ProductPattern() {
		super();		
	}

	public ProductPattern(Integer productId, String productName, Integer mainCategoryId) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.mainCategoryId = mainCategoryId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getMainCategoryId() {
		return mainCategoryId;
	}

	public void setMainCategoryId(Integer mainCategoryId) {
		this.mainCategoryId = mainCategoryId;
	}

	public String getImgPath() {
		return imgPath;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public String getBaseLink() {
		return baseLink;
	}

	public void setBaseLink(String baseLink) {
		this.baseLink = baseLink;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Integer getExpectedAvailability() {
		return expectedAvailability;
	}

	public void setExpectedAvailability(Integer expectedAvailability) {
		this.expectedAvailability = expectedAvailability;
	}

	public Object getUnavailabilityReason() {
		return unavailabilityReason;
	}

	public void setUnavailabilityReason(Object unavailabilityReason) {
		this.unavailabilityReason = unavailabilityReason;
	}

	public Boolean getPreorderEnabled() {
		return preorderEnabled;
	}

	public void setPreorderEnabled(Boolean preorderEnabled) {
		this.preorderEnabled = preorderEnabled;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getTextualAmount() {
		return textualAmount;
	}

	public void setTextualAmount(String textualAmount) {
		this.textualAmount = textualAmount;
	}

	public Boolean getSemicaliber() {
		return semicaliber;
	}

	public void setSemicaliber(Boolean semicaliber) {
		this.semicaliber = semicaliber;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public PricePerUnit getPricePerUnit() {
		return pricePerUnit;
	}

	public void setPricePerUnit(PricePerUnit pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	public RecommendedPricePerUnit getRecommendedPricePerUnit() {
		return recommendedPricePerUnit;
	}

	public void setRecommendedPricePerUnit(RecommendedPricePerUnit recommendedPricePerUnit) {
		this.recommendedPricePerUnit = recommendedPricePerUnit;
	}

	public OriginalPrice getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(OriginalPrice originalPrice) {
		this.originalPrice = originalPrice;
	}

	public Boolean getGoodPrice() {
		return goodPrice;
	}

	public void setGoodPrice(Boolean goodPrice) {
		this.goodPrice = goodPrice;
	}

	public Integer getGoodPriceSalePercentage() {
		return goodPriceSalePercentage;
	}

	public void setGoodPriceSalePercentage(Integer goodPriceSalePercentage) {
		this.goodPriceSalePercentage = goodPriceSalePercentage;
	}

	public List<Sale> getSales() {
		return sales;
	}

	public void setSales(List<Sale> sales) {
		this.sales = sales;
	}

	public Integer getMaxBasketAmount() {
		return maxBasketAmount;
	}

	public void setMaxBasketAmount(Integer maxBasketAmount) {
		this.maxBasketAmount = maxBasketAmount;
	}

	public String getMaxBasketAmountReason() {
		return maxBasketAmountReason;
	}

	public void setMaxBasketAmountReason(String maxBasketAmountReason) {
		this.maxBasketAmountReason = maxBasketAmountReason;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public List<Badge> getBadge() {
		return badge;
	}

	public void setBadge(List<Badge> badge) {
		this.badge = badge;
	}

	public Object getStars() {
		return stars;
	}

	public void setStars(Object stars) {
		this.stars = stars;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void setCountries(List<Country> countries) {
		this.countries = countries;
	}

	public Integer getImageScaleRatio() {
		return imageScaleRatio;
	}

	public void setImageScaleRatio(Integer imageScaleRatio) {
		this.imageScaleRatio = imageScaleRatio;
	}

	public Integer getImagesCount() {
		return imagesCount;
	}

	public void setImagesCount(Integer imagesCount) {
		this.imagesCount = imagesCount;
	}

	public Boolean getImmediateConsumption() {
		return immediateConsumption;
	}

	public void setImmediateConsumption(Boolean immediateConsumption) {
		this.immediateConsumption = immediateConsumption;
	}

	public Boolean getBabyClubUser() {
		return babyClubUser;
	}

	public void setBabyClubUser(Boolean babyClubUser) {
		this.babyClubUser = babyClubUser;
	}

	public Boolean getWatchDog() {
		return watchDog;
	}

	public void setWatchDog(Boolean watchDog) {
		this.watchDog = watchDog;
	}

	public Composition getComposition() {
		return composition;
	}

	public void setComposition(Composition composition) {
		this.composition = composition;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getProducerHtml() {
		return producerHtml;
	}

	public void setProducerHtml(String producerHtml) {
		this.producerHtml = producerHtml;
	}

	public Object getProductStory() {
		return productStory;
	}

	public void setProductStory(Object productStory) {
		this.productStory = productStory;
	}

	public Object getVivino() {
		return vivino;
	}

	public void setVivino(Object vivino) {
		this.vivino = vivino;
	}

	public Boolean getInStock() {
		return inStock;
	}

	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}

	public Boolean getFavourite() {
		return favourite;
	}

	public void setFavourite(Boolean favourite) {
		this.favourite = favourite;
	}
	
	public Double getFullOriginalPrice() {
		if (this.originalPrice != null && this.originalPrice.getFull() != null ) {
			return this.originalPrice.getFull().doubleValue();
		}
		return 0.0;
	}
	
	public Double getFullPrice() {
		if (this.price != null && this.price.getFull() != null) {
				return this.price.getFull().doubleValue();
		}
		return 0.0;
	}
	
	public Double getFullPricePerUnit() {
		if (this.pricePerUnit != null && this.pricePerUnit.getFull() != null) {
				return this.pricePerUnit.getFull().doubleValue();
		}
		return 0.0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(babyClubUser, badge, baseLink, companyId, composition, countries, country, currency,
				expectedAvailability, favourite, goodPrice, goodPriceSalePercentage, imageScaleRatio, imagesCount,
				imgPath, immediateConsumption, inStock, link, mainCategoryId, maxBasketAmount, maxBasketAmountReason,
				originalPrice, preorderEnabled, price, pricePerUnit, producerHtml, productId, productName, productStory,
				recommendedPricePerUnit, sales, semicaliber, stars, tags, textualAmount, unavailabilityReason, unit,
				vivino, watchDog);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductPattern other = (ProductPattern) obj;
		return Objects.equals(babyClubUser, other.babyClubUser) && Objects.equals(badge, other.badge)
				&& Objects.equals(baseLink, other.baseLink) && Objects.equals(companyId, other.companyId)
				&& Objects.equals(composition, other.composition) && Objects.equals(countries, other.countries)
				&& Objects.equals(country, other.country) && Objects.equals(currency, other.currency)
				&& Objects.equals(expectedAvailability, other.expectedAvailability)
				&& Objects.equals(favourite, other.favourite) && Objects.equals(goodPrice, other.goodPrice)
				&& Objects.equals(goodPriceSalePercentage, other.goodPriceSalePercentage)
				&& Objects.equals(imageScaleRatio, other.imageScaleRatio)
				&& Objects.equals(imagesCount, other.imagesCount) && Objects.equals(imgPath, other.imgPath)
				&& Objects.equals(immediateConsumption, other.immediateConsumption)
				&& Objects.equals(inStock, other.inStock) && Objects.equals(link, other.link)
				&& Objects.equals(mainCategoryId, other.mainCategoryId)
				&& Objects.equals(maxBasketAmount, other.maxBasketAmount)
				&& Objects.equals(maxBasketAmountReason, other.maxBasketAmountReason)
				&& Objects.equals(originalPrice, other.originalPrice)
				&& Objects.equals(preorderEnabled, other.preorderEnabled) && Objects.equals(price, other.price)
				&& Objects.equals(pricePerUnit, other.pricePerUnit) && Objects.equals(producerHtml, other.producerHtml)
				&& Objects.equals(productId, other.productId) && Objects.equals(productName, other.productName)
				&& Objects.equals(productStory, other.productStory)
				&& Objects.equals(recommendedPricePerUnit, other.recommendedPricePerUnit)
				&& Objects.equals(sales, other.sales) && Objects.equals(semicaliber, other.semicaliber)
				&& Objects.equals(stars, other.stars) && Objects.equals(tags, other.tags)
				&& Objects.equals(textualAmount, other.textualAmount)
				&& Objects.equals(unavailabilityReason, other.unavailabilityReason) && Objects.equals(unit, other.unit)
				&& Objects.equals(vivino, other.vivino) && Objects.equals(watchDog, other.watchDog);
	}

	@Override
	public String toString() {
		return "ProductPattern [productId=" + productId + ", productName=" + productName + ", mainCategoryId="
				+ mainCategoryId + ", imgPath=" + imgPath + ", baseLink=" + baseLink + ", link=" + link
				+ ", expectedAvailability=" + expectedAvailability + ", unavailabilityReason=" + unavailabilityReason
				+ ", preorderEnabled=" + preorderEnabled + ", unit=" + unit + ", textualAmount=" + textualAmount
				+ ", semicaliber=" + semicaliber + ", currency=" + currency + ", price=" + price + ", pricePerUnit="
				+ pricePerUnit + ", recommendedPricePerUnit=" + recommendedPricePerUnit + ", originalPrice="
				+ originalPrice + ", goodPrice=" + goodPrice + ", goodPriceSalePercentage=" + goodPriceSalePercentage
				+ ", sales=" + sales + ", maxBasketAmount=" + maxBasketAmount + ", maxBasketAmountReason="
				+ maxBasketAmountReason + ", tags=" + tags + ", badge=" + badge + ", stars=" + stars + ", country="
				+ country + ", countries=" + countries + ", imageScaleRatio=" + imageScaleRatio + ", imagesCount="
				+ imagesCount + ", immediateConsumption=" + immediateConsumption + ", babyClubUser=" + babyClubUser
				+ ", watchDog=" + watchDog + ", composition=" + composition + ", companyId=" + companyId
				+ ", producerHtml=" + producerHtml + ", productStory=" + productStory + ", vivino=" + vivino
				+ ", inStock=" + inStock + ", favourite=" + favourite + "]";
	}
	
	public abstract Product toProduct();
	public abstract Product toProductWithSales();
	
	
	
	

}
