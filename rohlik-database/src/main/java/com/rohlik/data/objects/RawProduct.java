
package com.rohlik.data.objects;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.productparts.FirstActiveSale;
import com.rohlik.data.objects.productparts.Sale;
import com.rohlik.data.patterns.ProductPattern;

public class RawProduct extends ProductPattern {

	private Boolean expectedAvailabilityHasTime;	
	private Integer favouriteCount;
	private Integer orderCount;
	private FirstActiveSale firstActiveSale;
	

	public Boolean getExpectedAvailabilityHasTime() {
		return expectedAvailabilityHasTime;
	}

	public void setExpectedAvailabilityHasTime(Boolean expectedAvailabilityHasTime) {
		this.expectedAvailabilityHasTime = expectedAvailabilityHasTime;
	}

	public Integer getFavouriteCount() {
		return favouriteCount;
	}

	public void setFavouriteCount(Integer favouriteCount) {
		this.favouriteCount = favouriteCount;
	}

	public Integer getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(Integer orderCount) {
		this.orderCount = orderCount;
	}

	public FirstActiveSale getFirstActiveSale() {
		return firstActiveSale;
	}

	public void setFirstActiveSale(FirstActiveSale firstActiveSale) {
		this.firstActiveSale = firstActiveSale;
	}

	public Product toProduct() {
    	Product product = new Product();
		product.setProductId(this.productId);
		product.setProductName(this.productName);
		product.setOriginalPrice(this.getFullOriginalPrice());
		product.setPrice(this.getFullPrice());
		product.setTextualAmount(this.textualAmount);
		product.setUnit(this.unit);
		product.setBaseLink(this.baseLink);
		if(this.imgPath!=null) {product.setImgPath(this.imgPath);} else {product.setImgPath("");}
		product.setInStock(this.inStock);
		product.setLink(this.link);
		product.setPricePerUnit(this.getFullPricePerUnit());
		product.setMainCategoryId(this.mainCategoryId);
		product.setActive(true);
		product.setHasSales(!this.sales.isEmpty());
		product.setIsFromRohlik(true);    	
		return product;    	
    }

	public Product toProductWithSales() {
		Product product = this.toProduct();
		Set<com.rohlik.data.entities.Sale> salesConverted = this.sales.stream().map(Sale::toSale).collect(Collectors.toCollection(HashSet::new));
    	salesConverted.forEach(product::addSales);
		return product;
		   	
    }

	@Override
	public int hashCode() {
		return Objects.hash(expectedAvailabilityHasTime, favouriteCount, firstActiveSale, orderCount, super.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RawProduct other = (RawProduct) obj;
		return Objects.equals(expectedAvailabilityHasTime, other.expectedAvailabilityHasTime)
				&& Objects.equals(favouriteCount, other.favouriteCount)
				&& Objects.equals(firstActiveSale, other.firstActiveSale)
				&& Objects.equals(orderCount, other.orderCount);
	}

	@Override
	public String toString() {
		return "RawProduct [productId=" + productId +", productName=" + productName + ", mainCategoryId=" + mainCategoryId
				+ ", imgPath=" + imgPath + ", baseLink=" + baseLink + ", link=" + link + ", expectedAvailability="
				+ expectedAvailability + ", unavailabilityReason=" + unavailabilityReason + ", preorderEnabled="
				+ preorderEnabled + ", unit=" + unit + ", textualAmount=" + textualAmount + ", semicaliber="
				+ semicaliber + ", currency=" + currency + ", price=" + price + ", pricePerUnit=" + pricePerUnit
				+ ", recommendedPricePerUnit=" + recommendedPricePerUnit + ", originalPrice=" + originalPrice
				+ ", goodPrice=" + goodPrice + ", goodPriceSalePercentage=" + goodPriceSalePercentage + ", sales="
				+ sales + ", maxBasketAmount=" + maxBasketAmount + ", maxBasketAmountReason=" + maxBasketAmountReason
				+ ", tags=" + tags + ", badge=" + badge + ", stars=" + stars + ", country=" + country + ", countries="
				+ countries + ", imageScaleRatio=" + imageScaleRatio + ", imagesCount=" + imagesCount
				+ ", immediateConsumption=" + immediateConsumption + ", babyClubUser=" + babyClubUser + ", watchDog="
				+ watchDog + ", composition=" + composition + ", companyId=" + companyId + ", producerHtml="
				+ producerHtml + ", productStory=" + productStory + ", vivino=" + vivino + ", inStock=" + inStock
				+ ", favourite=" + favourite+",expectedAvailabilityHasTime=" + expectedAvailabilityHasTime + ", favouriteCount="
				+ favouriteCount + ", orderCount=" + orderCount + ", firstActiveSale=" + firstActiveSale
				+  "]";
	}

	
	
	
	
}
