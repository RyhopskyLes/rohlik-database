package com.rohlik.data.entities;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.ColumnResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import org.hibernate.annotations.GenericGenerator;

import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
@Entity(name = "Child")
@Table(name = "child")
@SqlResultSetMapping(
        name = "BasicChildMapping",
        entities = @EntityResult(
                entityClass = Child.class,
                fields = {
                    @FieldResult(name = "id", column = "id"),
                    @FieldResult(name = "categoryId", column = "categoryId"),
                    @FieldResult(name = "categoryName", column = "categoryName"),
                    @FieldResult(name = "active", column = "active")}))
public class Child implements Serializable {
	private static final long serialVersionUID = 1097414407215795301L;
	@Id
	@GenericGenerator(name = "UseIdOrGenerate", strategy = "com.rohlik.data.commons.objects.UseIdOrGenerate")
	@GeneratedValue(generator = "UseIdOrGenerate")
 //   @GeneratedValue(strategy = IDENTITY)
    private Integer id;
	private Integer categoryId;
	private String categoryName;
	private Boolean active;
	@ManyToOne(fetch = FetchType.EAGER)
		@JoinTable(
	            name="category_child",
	            joinColumns = @JoinColumn( name="id_child", referencedColumnName="id"),
	            inverseJoinColumns = @JoinColumn( name="id_category", referencedColumnName="id")
	        )
		public Category parent;
	
	public Child() {
		super();
		}
	public Child(Integer categoryId, String categoryName) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}	
	
	public Child(Integer categoryId, String categoryName, Boolean active) {
		super();
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.active = active;
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
	public Category getParent() {
		return parent;
	}
	public void setParent(Category parent) {
		this.parent = parent;
	}
	
	public Integer getParentId() {
		return parent.getCategoryId();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(categoryId, categoryName, active);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Child other = (Child) obj;
		return Objects.equals(categoryId, other.categoryId) && Objects.equals(categoryName, other.categoryName)
				 && Objects.equals(active, other.active);
	}
	@Override
	public String toString() {
		return "Child [id=" + id + ", categoryId=" + categoryId + ", categoryName=" + categoryName + ", active=" + active+ "]";
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

}
