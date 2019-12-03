package com.rohlik.data.kosik.entities;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.rohlik.data.kosik.interfaces.Setter;


@Entity(name = "ChildKosik")
@Table(name = "childkosik")
public class ChildKosik implements Serializable {
	private static Logger log = LoggerFactory.getLogger(ChildKosik.class);
	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;
	private Integer equiId;
	private String categoryName;
	private String equiCategoryName;
	private String uri;
	private String parentUri;
	private Boolean active;
	@Transient
	private String parentName;
	@Transient
	private Integer equiParentId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "categorykosik_childkosik", joinColumns = @JoinColumn(name = "id_childkosik", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_categorykosik", referencedColumnName = "id"))
	public CategoryKosik parent;

	public ChildKosik() {
	}

	public ChildKosik(Integer equiId, String categoryName) {
		this.equiId = equiId;
		this.categoryName = categoryName;
	}
public ChildKosik doIf(Boolean condition, UnaryOperator<ChildKosik> operate) {
	if(condition) {ChildKosik temp = operate.apply(this);
	return temp;}
	return this;	
}
	public <T> ChildKosik set(Setter<T> setter, T param) {
		setter.accept(param);
		return this;
	}
		
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getEquiId() {
		return equiId;
	}

	public void setEquiId(Integer equiId) {
		this.equiId = equiId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public CategoryKosik getParent() {
		return parent;
	}

	public void setParent(CategoryKosik parent) {
		this.parent = parent;
	}

	public String getParentName() {
		return this.getParent().getCategoryName();
	}

	public Integer getEquiParentId() {
		return this.getParent().getEquiId();
	}

	@Override
	public int hashCode() {
		return Objects.hash(categoryName, uri, parentUri, active);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChildKosik other = (ChildKosik) obj;
		return Objects.equals(categoryName, other.categoryName) && Objects.equals(equiId, other.equiId)
				&& Objects.equals(equiCategoryName, other.equiCategoryName)
				&& Objects.equals(uri, other.uri)
				&& Objects.equals(parentUri, other.parentUri)
				&& Objects.equals(active, other.active)
				&& Objects.equals(parent, other.parent);
	}

	@Override
	public String toString() {
		return "[equiId " + equiId + ", categoryName=" + categoryName + ", equiCategoryName=" + equiCategoryName + ", uri=" + uri +", parentUri=" + parentUri +", active=" + active+"]";
		// return "ChildKosik [id=" + id + ", equiId=" + equiId + ", categoryName=" +
		// categoryName + ", equiCategoryName=" + equiCategoryName + "]";
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

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public void setEquiParentId(Integer equiParentId) {
		this.equiParentId = equiParentId;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}
}
