package com.rohlik.data.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.rohlik.data.commons.interfaces.IdMediator;
import com.rohlik.data.dtos.ChildDTO;
import com.rohlik.data.kosik.entities.CategoryKosik;


@Entity(name = "Category")
@Table(name = "category")
public class Category implements Serializable, IdMediator {
	
	private static final long serialVersionUID = 1262870598664693503L;
	@Id
	@GenericGenerator(name = "UseIdOrGenerate", strategy = "com.rohlik.data.commons.objects.UseIdOrGenerate")
	@GeneratedValue(generator = "UseIdOrGenerate")
  //  @GeneratedValue(strategy = IDENTITY)
    private Integer id;
	private Integer categoryId;
	private String categoryName;
	private Integer parentId;
	private Boolean active;
	@ManyToMany(mappedBy = "categories")
	private Set<Product> products =new HashSet<>();
	@ManyToMany(mappedBy = "categories")
	private Set<CategoryKosik> categoriesKosik =new HashSet<>();
	@OneToMany(//fetch = FetchType.LAZY,
	        cascade = CascadeType.ALL,
	        orphanRemoval = true, mappedBy="parent"
	    )
	private Set<Child> children = new HashSet<>();
	public Category() {
		super();		
	}
	public Category(Integer categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}
	public Category(Integer categoryId, String categoryName, Integer parentId) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.parentId = parentId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Set<CategoryKosik> getCategoriesKosik() {
		return categoriesKosik;
	}
	public void setCategoriesKosik(Set<CategoryKosik> categoriesKosik) {
		this.categoriesKosik = categoriesKosik;
	}
	public Set<Product> getProducts() {
		return products;
	}
	public void setProducts(Set<Product> products) {
		this.products = products;
	}
	public Set<Child> getChildren() {
		return children;
	}
	public void setChildren(Set<Child> children) {
		this.children = children;
	}
	public void addChild(Child child) {
		children.add(child);
		child.setParent(this);		
	}
	public void removeChild(Child child) {
		children.remove(child);
		child.setParent(null);		
	}
	
	public Child toChild() {
		Child child = new Child();
		child.setCategoryId(categoryId);
		child.setCategoryName(categoryName);
		child.setActive(active);
		return child;
	}
	
	@Override
	public Integer provideId() {
		return this.id;
	}
	@Override
	public int hashCode() {
		return Objects.hash(categoryId, categoryName, parentId);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		return Objects.equals(categoryId, other.categoryId) && Objects.equals(categoryName, other.categoryName)
				&& Objects.equals(children, other.children) && Objects.equals(parentId, other.parentId)
				&& Objects.equals(active, other.active)
				&& Objects.equals(products, other.products);
	}
	@Override
	public String toString() {
		return "Category [id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", parentId="
				+ parentId + ", active=" + active + "]";
	}






}
