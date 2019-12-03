package com.rohlik.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Child.class)
public abstract class Child_ {

	public static volatile SingularAttribute<Child, Category> parent;
	public static volatile SingularAttribute<Child, Integer> id;
	public static volatile SingularAttribute<Child, String> categoryName;
	public static volatile SingularAttribute<Child, Integer> categoryId;

	public static final String PARENT = "parent";
	public static final String ID = "id";
	public static final String CATEGORY_NAME = "categoryName";
	public static final String CATEGORY_ID = "categoryId";

}

