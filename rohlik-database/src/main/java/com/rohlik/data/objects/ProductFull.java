package com.rohlik.data.objects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.productparts.CategoryByProduct;
import com.rohlik.data.objects.productparts.Nutrients;
import com.rohlik.data.objects.productparts.Sale;
import com.rohlik.data.patterns.ProductPattern;


public class ProductFull extends ProductPattern {	
	
	private String description;
	private String ingredients;
	private Nutrients nutrients;
	private Boolean allowedAllergens;
	private List<String> images = new ArrayList<>();
	private Set<CategoryByProduct> categories = new LinkedHashSet<>();
	private Integer shelfLifeAvg;
	private Integer shelfLifeMin;
	private Object leaflet;
	private List<Object> informationBlocks = new ArrayList<>();
	
	public ProductFull() {
		super();		
	}

	public ProductFull(Integer productId, String productName, Integer mainCategoryId) {
		super(productId, productName, mainCategoryId);		
	}
	
	public Set<Category> getCategoriesConverted() {
		return categories.stream().map(this::toCategory).collect(Collectors.toCollection(LinkedHashSet::new));
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
	
	

	@Override
	public Product toProduct() {
		Product product = new Product();
		product.setProductId(this.productId);
		product.setProductName(this.productName);
		if(this.imgPath!=null) {product.setImgPath(this.imgPath);} else {product.setImgPath("");}
		product.setBaseLink(this.baseLink);
		product.setInStock(this.inStock);
		product.setIsFromRohlik(true);
		product.setHasSales(!this.getSales().isEmpty());
		product.setLink(this.link);
		product.setOriginalPrice(this.getFullOriginalPrice());
		product.setPrice(this.getFullPrice());
		product.setPricePerUnit(this.getFullPricePerUnit());
		product.setTextualAmount(this.textualAmount);
		product.setMainCategoryId(this.mainCategoryId);
		product.setMainCategoryName(this.getMainCategoryName());
		product.setUnit(this.unit);
		product.setActive(true);		
		return product;
		}
	
	public String getMainCategoryName() {
	return this.getCategoriesConverted().stream().limit(1).findFirst().map(Category::getCategoryName).orElseGet(()->"");	
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public Nutrients getNutrients() {
		return nutrients;
	}

	public void setNutrients(Nutrients nutrients) {
		this.nutrients = nutrients;
	}

	public Boolean getAllowedAllergens() {
		return allowedAllergens;
	}

	public void setAllowedAllergens(Boolean allowedAllergens) {
		this.allowedAllergens = allowedAllergens;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Set<CategoryByProduct> getCategories() {
		return categories;
	}

	public void setCategories(Set<CategoryByProduct> categories) {
		this.categories = categories;
	}

	public Integer getShelfLifeAvg() {
		return shelfLifeAvg;
	}

	public void setShelfLifeAvg(Integer shelfLifeAvg) {
		this.shelfLifeAvg = shelfLifeAvg;
	}

	public Integer getShelfLifeMin() {
		return shelfLifeMin;
	}

	public void setShelfLifeMin(Integer shelfLifeMin) {
		this.shelfLifeMin = shelfLifeMin;
	}

	public Object getLeaflet() {
		return leaflet;
	}

	public void setLeaflet(Object leaflet) {
		this.leaflet = leaflet;
	}

	public List<Object> getInformationBlocks() {
		return informationBlocks;
	}

	public void setInformationBlocks(List<Object> informationBlocks) {
		this.informationBlocks = informationBlocks;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowedAllergens, categories, description, images, informationBlocks, ingredients, leaflet,
				nutrients, shelfLifeAvg, shelfLifeMin, super.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductFull other = (ProductFull) obj;
		return Objects.equals(allowedAllergens, other.allowedAllergens) && Objects.equals(categories, other.categories)
				&& Objects.equals(description, other.description) && Objects.equals(images, other.images)
				&& Objects.equals(informationBlocks, other.informationBlocks)
				&& Objects.equals(ingredients, other.ingredients) && Objects.equals(leaflet, other.leaflet)
				&& Objects.equals(nutrients, other.nutrients) && Objects.equals(shelfLifeAvg, other.shelfLifeAvg)
				&& Objects.equals(shelfLifeMin, other.shelfLifeMin);
	}

	@Override
	public Product toProductWithSales() {
		Product product = this.toProduct();
		Set<com.rohlik.data.entities.Sale> salesConverted = this.sales.stream().map(Sale::toSale).collect(Collectors.toCollection(HashSet::new));
    	salesConverted.forEach(product::addSales);
		return product;
	}

	@Override
	public String toString() {
		return "ProductFull [productId=" + productId + ", productName="
				+ productName + ", mainCategoryId=" + mainCategoryId + ", imgPath=" + imgPath + ", baseLink=" + baseLink
				+ ", link=" + link + ", expectedAvailability=" + expectedAvailability + ", unavailabilityReason="
				+ unavailabilityReason + ", preorderEnabled=" + preorderEnabled + ", unit=" + unit + ", textualAmount="
				+ textualAmount + ", semicaliber=" + semicaliber + ", currency=" + currency + ", price=" + price
				+ ", pricePerUnit=" + pricePerUnit + ", recommendedPricePerUnit=" + recommendedPricePerUnit
				+ ", originalPrice=" + originalPrice + ", goodPrice=" + goodPrice + ", goodPriceSalePercentage="
				+ goodPriceSalePercentage + ", sales=" + sales + ", maxBasketAmount=" + maxBasketAmount
				+ ", maxBasketAmountReason=" + maxBasketAmountReason + ", tags=" + tags + ", badge=" + badge
				+ ", stars=" + stars + ", country=" + country + ", countries=" + countries + ", imageScaleRatio="
				+ imageScaleRatio + ", imagesCount=" + imagesCount + ", immediateConsumption=" + immediateConsumption
				+ ", babyClubUser=" + babyClubUser + ", watchDog=" + watchDog + ", composition=" + composition
				+ ", companyId=" + companyId + ", producerHtml=" + producerHtml + ", productStory=" + productStory
				+ ", vivino=" + vivino + ", inStock=" + inStock + ", favourite=" + favourite +", description=" + description + ", ingredients=" + ingredients + ", nutrients=" + nutrients
				+ ", allowedAllergens=" + allowedAllergens + ", images=" + images + ", categories=" + categories
				+ ", shelfLifeAvg=" + shelfLifeAvg + ", shelfLifeMin=" + shelfLifeMin + ", leaflet=" + leaflet
				+ ", informationBlocks=" + informationBlocks + "]";
	}

	
	
	
}
