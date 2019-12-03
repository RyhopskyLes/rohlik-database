package com.rohlik.data.kosik.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(CategoryKosik.class)
public abstract class CategoryKosik_ {

	public static volatile SingularAttribute<CategoryKosik, String> parentName;
	public static volatile SingularAttribute<CategoryKosik, Integer> equiId;
	public static volatile SingularAttribute<CategoryKosik, Integer> equiParentId;
	public static volatile SingularAttribute<CategoryKosik, Integer> id;
	public static volatile SingularAttribute<CategoryKosik, String> categoryName;

	public static final String PARENT_NAME = "parentName";
	public static final String EQUI_ID = "equiId";
	public static final String EQUI_PARENT_ID = "equiParentId";
	public static final String ID = "id";
	public static final String CATEGORY_NAME = "categoryName";

}

