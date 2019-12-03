package com.rohlik.data.kosik.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rohlik.data.commons.interfaces.IdMediator;
import com.rohlik.data.entities.Product;

@Entity
@Table(name = "product_kosik")
public class ProductKosik implements Serializable, IdMediator {
	private static final long serialVersionUID = 1787352153097188085L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id_kosik")
	Integer id;
	@Column(name = "name")
	String name;
	@Column(name = "producer_kosik")
	String producer;
	@Column(name = "idProduct_kosik")
	Integer idProduct;	
	@Column(name = "orig_price")
	Double origPrice;
	@Column(name = "actual_price")
	Double actualPrice;
	@Column(name = "category")
	String category;
	@Column(name = "product_Path")
	String productPath;
	@Column(name = "image_Src")
	String imageSrc;
	@Column(name = "amount_Product")
	String amountProduct;
	@Column(name = "unit_Price")
	String unitPrice;
	@Column(name = "in_Stock")
	Boolean inStock;
	@Column(name = "active")
	Boolean active;
	@Transient
	private Integer equiId;
	@Transient
	private Double dissimilarity;
	
	@ManyToMany(cascade = {
    	    CascadeType.PERSIST,
    	    CascadeType.MERGE
    	})
    	@JoinTable(name="product_kosik_category_kosik",
    	    joinColumns = @JoinColumn(name = "product_kosik_id", referencedColumnName="id_kosik"),
    	    inverseJoinColumns = @JoinColumn(name="categorykosik_id", referencedColumnName="id")
    	)
	@JsonIgnore
    private Set<CategoryKosik> categories = new HashSet<>();
	@OneToOne(cascade = {
    	    CascadeType.PERSIST,
    	    CascadeType.MERGE
    	}, fetch = FetchType.EAGER)
    	@JoinTable(name="kosik_rohlik_product",
    	    joinColumns = @JoinColumn(name = "kosik", referencedColumnName="id_kosik"),
    	    inverseJoinColumns = @JoinColumn(name="rohlik", referencedColumnName="id")
    	)
	Product product;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProducer() {
		return producer;
	}
	public void setProducer(String producer) {
		this.producer = producer;
	}
	public Integer getIdProduct() {
		return idProduct;
	}
	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}
	public Double getOrigPrice() {
		return origPrice;
	}
	public void setOrigPrice(Double origPrice) {
		this.origPrice = origPrice;
	}
	public Double getActualPrice() {
		return actualPrice;
	}
	public void setActualPrice(Double actualPrice) {
		this.actualPrice = actualPrice;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getProductPath() {
		return productPath;
	}
	public void setProductPath(String productPath) {
		this.productPath = productPath;
	}
	public String getImageSrc() {
		return imageSrc;
	}
	public void setImageSrc(String imageSrc) {
		this.imageSrc = imageSrc;
	}
	public String getAmountProduct() {
		return amountProduct;
	}
	public void setAmountProduct(String amountProduct) {
		this.amountProduct = amountProduct;
	}
	public String getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}
	@Override
	public String toString() {
		return "ProductKosik [id=" + id + ", name=" + name + ", producer=" + producer + ", idProduct=" + idProduct
				+ ", origPrice=" + origPrice + ", actualPrice=" + actualPrice + ", category=" + category
				+ ", productPath=" + productPath + ", imageSrc=" + imageSrc + ", amountProduct=" + amountProduct
				+ ", unitPrice=" + unitPrice + ", inStock=" + inStock + ", active=" + active + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(active, actualPrice, amountProduct, idProduct, imageSrc, inStock, name, origPrice, producer,
				productPath, unitPrice, inStock, active);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductKosik other = (ProductKosik) obj;
		return active == other.active && Objects.equals(actualPrice, other.actualPrice)
				&& Objects.equals(amountProduct, other.amountProduct) && Objects.equals(idProduct, other.idProduct)
				&& Objects.equals(imageSrc, other.imageSrc) && inStock == other.inStock
				&& Objects.equals(name, other.name) && Objects.equals(origPrice, other.origPrice)
				&& Objects.equals(producer, other.producer) && Objects.equals(productPath, other.productPath)
				&& Objects.equals(unitPrice, other.unitPrice)
				&& Objects.equals(inStock, other.inStock) && Objects.equals(active, other.active)
				&&Objects.equals(product, other.product);
	}
	
	public void addCategory(CategoryKosik category) {
		if(category!=null)
		{categories.add(category);
        category.getProducts().add(this);}
    }
 
    public void removeCategory(CategoryKosik category) {
    	if(category!=null)
    	{ categories.remove(category);
        category.getProducts().remove(this);}
    }
	public Set<CategoryKosik> getCategories() {
		return categories;
	}
	public void setCategories(Set<CategoryKosik> categories) {
		this.categories = categories;
	}
	public Boolean isInStock() {
		return inStock;
	}
	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}
	public Boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		if (product == null) {
            if (this.product != null) {
                this.product.setProductKosik(null);
            }
        }
        else {
            product.setProductKosik(this);
        }
        this.product = product;
	}
	
	public Boolean getInStock() {
		return inStock;
	}
	public Integer getEquiId() {
		return equiId;
	}
	public void setEquiId(Integer equiId) {
		this.equiId = equiId;
	}
	public Double getDissimilarity() {
		return dissimilarity;
	}
	public void setDissimilarity(Double dissimilarity) {
		this.dissimilarity = dissimilarity;
	}
	@Override
	public Integer provideId() {
		return this.id;
	}
	
	
	
	
	
}
