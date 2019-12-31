package com.rohlik.data.objects;

import java.util.Objects;

public class SlugAndName {
private String slug;
private String name;
private Integer amount;


public SlugAndName() {	
}

public SlugAndName(String slug, String name) {
	this.slug = slug;
	this.name = name;
}
public SlugAndName(String slug, String name, Integer amount) {
	this.slug = slug;
	this.name = name;
	this.amount = amount;
}

public String getSlug() {
	return slug;
}

public void setSlug(String slug) {
	this.slug = slug;
}

public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

public Integer getAmount() {
	return amount;
}

public void setAmount(Integer amount) {
	this.amount = amount;
}


@Override
public int hashCode() {
	return Objects.hash(amount, name, slug);
}

@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	SlugAndName other = (SlugAndName) obj;
	return Objects.equals(amount, other.amount) && Objects.equals(name, other.name) && Objects.equals(slug, other.slug);
}

@Override
public String toString() {
	return "SlugAndName [slug=" + slug + ", name=" + name + ", amount=" + amount + "]";
}

public ProducerWithProductIds toProducerWithoutProductIdsAndCategoryId() {
	ProducerWithProductIds producer = new ProducerWithProductIds();
	producer.setName(this.name);
	producer.setProductCount(this.amount);
	return producer;
	}


public ProducerWithProducts toProducerWithoutProductsAndCategoryId() {
	ProducerWithProducts producer = new ProducerWithProducts();
	producer.setName(this.name);
	producer.setProductCount(this.amount);
	return producer;
	}
}
