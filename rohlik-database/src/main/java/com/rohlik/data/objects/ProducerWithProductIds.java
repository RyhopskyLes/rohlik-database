package com.rohlik.data.objects;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProducerWithProductIds {
	private String name;
	private Integer categoryId;
	private Set<Integer> productIds = new HashSet<>();
	private int productCount;		
	
	public ProducerWithProductIds() {
		super();		
	}

	public ProducerWithProductIds(String name, Integer categoryId) {
		this(name, categoryId, new HashSet<>(), 0);
		this.name = name;
		this.categoryId = categoryId;
	}

	public ProducerWithProductIds(String name, Integer categoryId, Set<Integer> productIds) {
		this(name, categoryId, productIds, productIds.size());		
	}

	public ProducerWithProductIds(String name, Integer categoryId, Set<Integer> productIds, int productCount) {
		super();
		this.name = name;
		this.categoryId = categoryId;
		this.productIds = productIds;
		this.productCount = productCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Set<Integer> getProductIds() {
		return productIds;
	}

	public void setProductIds(Set<Integer> productIds) {
		this.productIds = productIds;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryId, name, productCount, productIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProducerWithProductIds other = (ProducerWithProductIds) obj;
		return Objects.equals(categoryId, other.categoryId) && Objects.equals(name, other.name)
				&& productCount == other.productCount && Objects.equals(productIds, other.productIds);
	}

	@Override
	public String toString() {
		return "ProducerWithProductIds [name=" + name + ", categoryId=" + categoryId + ", productIds=" + productIds
				+ ", productCount=" + productCount + "]";
	}	

}
