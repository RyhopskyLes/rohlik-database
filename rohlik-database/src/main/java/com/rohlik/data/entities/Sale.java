package com.rohlik.data.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rohlik.data.entities.Product;
import com.rohlik.data.entities.Sale;

@Entity
@Table(name = "sales")
public class Sale implements Serializable {
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "idSales")
	Integer idSales;
	@Column(name = "id")
	Integer id;	
	@Column(name = "discountPercentage")
	Double discountPercentage;
	@Column(name = "discountPrice")
	Double discountPrice;
	@Column(name = "originalPrice")
	Double originalPrice;
	@Column(name = "price")
	Double price;
	@Column(name = "pricePerUnit")
	Double pricePerUnit;
	@Column(name = "type")
	String type;
	@Column(name = "promoted")
	boolean promoted;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinTable(
            name="product_sales",
            joinColumns = @JoinColumn( name="id_sales", referencedColumnName="idSales"),
            inverseJoinColumns = @JoinColumn( name="id_product", referencedColumnName="id")
        )
	public Product product;
	public Integer getIdSales() {
		return idSales;
	}
	public void setIdSales(Integer idSales) {
		this.idSales = idSales;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Double getDiscountPercentage() {
		return discountPercentage;
	}
	public void setDiscountPercentage(Double discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
	public Double getDiscountPrice() {
		return discountPrice;
	}
	public void setDiscountPrice(Double discountPrice) {
		this.discountPrice = discountPrice;
	}
	public Double getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(Double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPricePerUnit() {
		return pricePerUnit;
	}
	public void setPricePerUnit(Double pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isPromoted() {
		return promoted;
	}
	public void setPromoted(boolean promoted) {
		this.promoted = promoted;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	@Override
	public String toString() {
		return "Sales [idSales=" + idSales + ", id=" + id + ", price=" + price + ", pricePerUnit=" + pricePerUnit
				+ ", type=" + type + "]";
	}
	@Override
	public int hashCode() {
		return Objects.hash(discountPercentage, discountPrice, id, originalPrice, price, pricePerUnit, promoted, type);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sale other = (Sale) obj;
		return Objects.equals(discountPercentage, other.discountPercentage)
				&& Objects.equals(discountPrice, other.discountPrice) && Objects.equals(id, other.id)
				&& Objects.equals(originalPrice, other.originalPrice) && Objects.equals(price, other.price)
				&& Objects.equals(pricePerUnit, other.pricePerUnit) && promoted == other.promoted
				&& Objects.equals(type, other.type);
	}
	
}
