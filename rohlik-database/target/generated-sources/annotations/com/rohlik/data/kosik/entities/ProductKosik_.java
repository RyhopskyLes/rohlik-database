package com.rohlik.data.kosik.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProductKosik.class)
public abstract class ProductKosik_ {

	public static volatile SingularAttribute<ProductKosik, String> unitPrice;
	public static volatile SingularAttribute<ProductKosik, String> productPath;
	public static volatile SingularAttribute<ProductKosik, Double> actualPrice;
	public static volatile SingularAttribute<ProductKosik, String> name;
	public static volatile SingularAttribute<ProductKosik, Double> origPrice;
	public static volatile SingularAttribute<ProductKosik, String> producer;
	public static volatile SingularAttribute<ProductKosik, Integer> idProduct;
	public static volatile SingularAttribute<ProductKosik, String> imageSrc;
	public static volatile SingularAttribute<ProductKosik, Integer> id;
	public static volatile SingularAttribute<ProductKosik, String> category;
	public static volatile SingularAttribute<ProductKosik, String> amountProduct;

	public static final String UNIT_PRICE = "unitPrice";
	public static final String PRODUCT_PATH = "productPath";
	public static final String ACTUAL_PRICE = "actualPrice";
	public static final String NAME = "name";
	public static final String ORIG_PRICE = "origPrice";
	public static final String PRODUCER = "producer";
	public static final String ID_PRODUCT = "idProduct";
	public static final String IMAGE_SRC = "imageSrc";
	public static final String ID = "id";
	public static final String CATEGORY = "category";
	public static final String AMOUNT_PRODUCT = "amountProduct";

}

