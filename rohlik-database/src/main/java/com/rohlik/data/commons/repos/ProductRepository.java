package com.rohlik.data.commons.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.rohlik.data.entities.Product;





public interface ProductRepository extends JpaRepository<Product, Integer> {
	Optional<Product> findByProductId(Integer id);
	@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales"
			+ " LEFT JOIN FETCH p.categories categories"
			+ " LEFT JOIN FETCH categories.children WHERE p.productId = (:id)")
	Optional<Product> findByProductIdEagerlyWithCategoriesAndChildren(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales WHERE p.id = (:id)")
public Product findByIdEagerly(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories WHERE p.id = (:id)")
public Optional<Product> findByIdEagerlyWithCategories(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories categories"
		+ " LEFT JOIN FETCH categories.children WHERE p.id = (:id)")
public Optional<Product> findByIdEagerlyWithCategoriesAndChildren(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales WHERE p.mainCategoryId = (:id)")
public List<Product> findByMainCategoryIdEagerly(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories WHERE p.mainCategoryId = (:id)")
public List<Product> findByMainCategoryIdEagerlyWithCategories(@Param("id") Integer id);
@Query("SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories categories "
		+ " LEFT JOIN FETCH categories.children WHERE p.mainCategoryId = (:id)")
public List<Product> findByMainCategoryIdEagerlyWithCategoriesAndChildren(@Param("id") Integer id);
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales")
public List<Product> findAllEagerly();
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories")
public List<Product> findAllEagerlyWithCategories();
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories categories"
		+ " LEFT JOIN FETCH categories.children")
public List<Product> findAllEagerlyWithCategoriesAndChildren();
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories category"
		+ " WHERE category.categoryId=(:id)")
public List<Product> findAllProductsByCategoryId(@Param("id") Integer id);
@RestResource(path = "productnamecontains", rel = "productnamecontains")
public Page<Product> findByProductNameIgnoreCaseContaining(@Param("name") String name, Pageable p);

@RestResource(path = "premiumproducts", rel = "premiumproducts")
@Query(
		  value = "SELECT product.id, product.productId, product.productName, product.producer, product.originalPrice, product.price,"
					+ "product.textualAmount, product.unit, product.baseLink, product.imgPath, product.inStock, product.hasSales, product.link, product.pricePerUnit,"
					+ "product.mainCategoryId, product.mainCategoryName"
					+ " FROM product\r\n" + 
					" inner join product_sales on product_sales.id_product=product.id\r\n" + 
					" inner join sales on product_sales.id_sales=sales.idSales where sales.type=\"premium\"", 
		  countQuery = "SELECT count(*) \r\n" + 
		  		"FROM rohlik_data.product\r\n" + 
		  		" inner join rohlik_data.product_sales on rohlik_data.product_sales.id_product=rohlik_data.product.id\r\n" + 
		  		" inner join rohlik_data.sales on rohlik_data.product_sales.id_sales=rohlik_data.sales.idSales where rohlik_data.sales.type=\"premium\";", 
		  nativeQuery = true)
public Page<Product> findAllPremiumProductsWithPagination(Pageable pageable);
@RestResource(path = "incategory", rel = "incategory")
@Query(
		  value = "SELECT product.id, product.productId, product.productName, product.producer, product.originalPrice, product.price,"
					+ "product.textualAmount, product.unit, product.baseLink, product.imgPath, product.inStock, product.hasSales, product.link, product.pricePerUnit,"
					+ "product.mainCategoryId, product.mainCategoryName"
					+ " FROM product\r\n" + 
					" inner join rohlik_data.product_category on rohlik_data.product_category.product_id=product.id\r\n" + 
					" inner join category on rohlik_data.product_category.category_id = category.id where category.categoryId= :id", 
		  countQuery = "SELECT count(*) \r\n" + 
				   " FROM product\r\n" + 
					" inner join rohlik_data.product_category on rohlik_data.product_category.product_id=product.id\r\n" + 
					" inner join rohlik_data.category on rohlik_data.product_category.category_id = category.id where category.categoryId= :id", 
		  nativeQuery = true)
public Page<Product> findAllProductsByCategoryId(Pageable pageable, @Param("id") Integer id);
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories where p.imgPath=''")
public List<Product> findAllWithoutImgPath();
@Query("SELECT distinct p FROM Product p LEFT JOIN FETCH p.sales"
		+ " LEFT JOIN FETCH p.categories where p.mainCategoryName='' OR  p.mainCategoryName is Null")
public List<Product> findAllWithoutMainCategoryName();
}

