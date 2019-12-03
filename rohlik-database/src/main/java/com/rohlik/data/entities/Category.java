package com.rohlik.data.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.rohlik.data.commons.interfaces.IdMediator;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.entities.Product;
import com.rohlik.data.kosik.entities.CategoryKosik;


@Entity(name = "Category")
@Table(name = "category")
public class Category implements Serializable, IdMediator {
	/**
	 * 
	 */
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
	public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }
public static class CategoryBuilder {
	private Integer categoryId;
	private String categoryName;
	private Integer parentId;
	
public	CategoryBuilder categoryId(final Integer categoryId) {
		this.categoryId=categoryId;
		return this;
	}
public	CategoryBuilder categoryName(final String categoryName) {
		this.categoryName=categoryName;
		return this;
	}
	
public	CategoryBuilder parentId(final Integer parentId) {
		this.parentId=parentId;
		return this;
	}
	
	 public Category build() {
         return new Category(categoryId, categoryName, parentId);
     }
	 
	 
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
@Override
public Integer provideId() {
	return this.id;
}



}
