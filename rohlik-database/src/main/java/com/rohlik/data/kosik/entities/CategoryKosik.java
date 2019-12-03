package com.rohlik.data.kosik.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.UnaryOperator;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rohlik.data.commons.interfaces.IdMediator;
import com.rohlik.data.entities.Category;
import com.rohlik.data.kosik.interfaces.Setter;

@Entity(name = "CategoryKosik")
@Table(name = "categorykosik")
public class CategoryKosik implements Serializable, IdMediator {
	private static final long serialVersionUID = 4050514273308125436L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private String categoryName;
	private String uri;
	private Integer equiId;
	private String equiCategoryName;
	private String parentName;
	private String parentUri;
	private Integer equiParentId;
	private Boolean active;
	@ManyToMany(mappedBy = "categories")
	private Set<ProductKosik> products = new HashSet<>();
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "parent")
	private Set<ChildKosik> children = new HashSet<>();
	@ManyToMany(cascade = { CascadeType.MERGE })
	@JoinTable(name = "kosik_rohlik_category", joinColumns = @JoinColumn(name = "kosik_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "rohlik_id", referencedColumnName = "id"))
	private Set<Category> categories = new HashSet<>();

	public CategoryKosik() {
	}

	public CategoryKosik(String categoryName) {
		this.categoryName = categoryName;
	}

	public CategoryKosik(String categoryName, Integer equiId) {
		this.categoryName = categoryName;
		this.equiId = equiId;
	}

	public CategoryKosik doIf(Boolean condition, UnaryOperator<CategoryKosik> operate) {
		if (Boolean.TRUE.equals(condition)) {
			return operate.apply(this);
		}
		return this;
	}

	public <T> CategoryKosik set(Setter<T> setter, T param) {
		setter.accept(param);
		return this;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getEquiId() {
		return equiId;
	}

	public void setEquiId(Integer equiId) {
		this.equiId = equiId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public Integer getEquiParentId() {
		return equiParentId;
	}

	public void setEquiParentId(Integer equiParentId) {
		this.equiParentId = equiParentId;
	}

	public void addChildKosik(ChildKosik child) {
		children.add(child);
		child.setParent(this);
	}

	public void removeChildKosik(ChildKosik child) {
		children.remove(child);
		child.setParent(null);
	}

	@Override
	public String toString() {
		return "CategoryKosik [id=" + id + ", categoryName=" + categoryName + ", uri=" + uri + ", equiId=" + equiId
				+ ", equiCategoryName=" + equiCategoryName + ", parentName=" + parentName + ", parentUri=" + parentUri
				+ ", equiParentId=" + equiParentId + ", active=" + active + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryName, equiCategoryName, equiId, equiParentId, parentName, uri, parentUri, active);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoryKosik other = (CategoryKosik) obj;
		return Objects.equals(categoryName, other.categoryName)
				&& Objects.equals(equiCategoryName, other.equiCategoryName) && Objects.equals(equiId, other.equiId)
				&& Objects.equals(equiParentId, other.equiParentId) && Objects.equals(parentName, other.parentName)
				&& Objects.equals(parentUri, other.parentUri) && Objects.equals(active, other.active)
				&& Objects.equals(uri, other.uri);
	}

	public Set<ChildKosik> getChildren() {
		return children;
	}

	public void setChildren(Set<ChildKosik> children) {
		this.children = children;
	}

	public String getEquiCategoryName() {
		return equiCategoryName;
	}

	public void setEquiCategoryName(String equiCategoryName) {
		this.equiCategoryName = equiCategoryName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getParentUri() {
		return parentUri;
	}

	public void setParentUri(String parentUri) {
		this.parentUri = parentUri;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public void addCategory(Category category) {
		if (category != null) {
			categories.add(category);
			category.getCategoriesKosik().add(this);
		}
	}

	public void removeCategory(Category category) {
		if (category != null) {
			categories.remove(category);
			category.getCategoriesKosik().remove(this);
		}
	}

	public Set<ProductKosik> getProducts() {
		return products;
	}

	public void setProducts(Set<ProductKosik> products) {
		this.products = products;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public Integer provideId() {
		return this.id;
	}

}
