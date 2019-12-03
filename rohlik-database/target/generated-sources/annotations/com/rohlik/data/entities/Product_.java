package com.rohlik.data.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Product.class)
public abstract class Product_ {

	public static volatile SingularAttribute<Product, Boolean> hasSales;
	public static volatile SingularAttribute<Product, String> textualAmount;
	public static volatile SingularAttribute<Product, Integer> productId;
	public static volatile SingularAttribute<Product, Double> originalPrice;
	public static volatile SingularAttribute<Product, String> link;
	public static volatile SingularAttribute<Product, String> baseLink;
	public static volatile SingularAttribute<Product, String> productName;
	public static volatile SetAttribute<Product, Sale> sales;
	public static volatile SingularAttribute<Product, Double> pricePerUnit;
	public static volatile SingularAttribute<Product, String> mainCategoryName;
	public static volatile SingularAttribute<Product, String> unit;
	public static volatile SingularAttribute<Product, Double> price;
	public static volatile SingularAttribute<Product, String> imgPath;
	public static volatile SingularAttribute<Product, String> producer;
	public static volatile SingularAttribute<Product, Boolean> inStock;
	public static volatile SingularAttribute<Product, Integer> id;
	public static volatile SetAttribute<Product, Category> categories;
	public static volatile SingularAttribute<Product, Integer> mainCategoryId;

	public static final String HAS_SALES = "hasSales";
	public static final String TEXTUAL_AMOUNT = "textualAmount";
	public static final String PRODUCT_ID = "productId";
	public static final String ORIGINAL_PRICE = "originalPrice";
	public static final String LINK = "link";
	public static final String BASE_LINK = "baseLink";
	public static final String PRODUCT_NAME = "productName";
	public static final String SALES = "sales";
	public static final String PRICE_PER_UNIT = "pricePerUnit";
	public static final String MAIN_CATEGORY_NAME = "mainCategoryName";
	public static final String UNIT = "unit";
	public static final String PRICE = "price";
	public static final String IMG_PATH = "imgPath";
	public static final String PRODUCER = "producer";
	public static final String IN_STOCK = "inStock";
	public static final String ID = "id";
	public static final String CATEGORIES = "categories";
	public static final String MAIN_CATEGORY_ID = "mainCategoryId";

}

