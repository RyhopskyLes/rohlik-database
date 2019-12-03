package com.rohlik.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Sale.class)
public abstract class Sale_ {

	public static volatile SingularAttribute<Sale, Double> discountPercentage;
	public static volatile SingularAttribute<Sale, Product> product;
	public static volatile SingularAttribute<Sale, Integer> idSales;
	public static volatile SingularAttribute<Sale, Double> originalPrice;
	public static volatile SingularAttribute<Sale, Double> price;
	public static volatile SingularAttribute<Sale, Double> discountPrice;
	public static volatile SingularAttribute<Sale, Integer> id;
	public static volatile SingularAttribute<Sale, String> type;
	public static volatile SingularAttribute<Sale, Boolean> promoted;
	public static volatile SingularAttribute<Sale, Double> pricePerUnit;

	public static final String DISCOUNT_PERCENTAGE = "discountPercentage";
	public static final String PRODUCT = "product";
	public static final String ID_SALES = "idSales";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String PRICE = "price";
	public static final String DISCOUNT_PRICE = "discountPrice";
	public static final String ID = "id";
	public static final String TYPE = "type";
	public static final String PROMOTED = "promoted";
	public static final String PRICE_PER_UNIT = "pricePerUnit";

}

