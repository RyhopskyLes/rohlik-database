package com.rohlik.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Category.class)
public abstract class Category_ {

	public static volatile SetAttribute<Category, Child> children;
	public static volatile SingularAttribute<Category, Integer> id;
	public static volatile SingularAttribute<Category, String> categoryName;
	public static volatile SingularAttribute<Category, Integer> categoryId;
	public static volatile SingularAttribute<Category, Integer> parentId;
	public static volatile SetAttribute<Category, Product> products;

	public static final String CHILDREN = "children";
	public static final String ID = "id";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String CATEGORY_ID = "categoryId";
	public static final String PARENT_ID = "parentId";
	public static final String PRODUCTS = "products";

}

