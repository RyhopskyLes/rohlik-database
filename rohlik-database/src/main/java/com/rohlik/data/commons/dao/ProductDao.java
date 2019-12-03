package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import com.rohlik.data.entities.Product;

public interface ProductDao {
	public Product findById(Integer id);
	public List<Product> findAll();
	Product save(Product product);
	public void delete(Product product);
	public Optional<Product> findByProductId(Integer id);
	Optional<Product> findByProductIdEagerlyWithCategoriesAndChildren(Integer id);
	public List<Product> findAllPremiumProducts();
	public List<Product> findAllProductsWithNearingExpiryDate();
	public List<Product> findAllProductsWithoutProducer();
	public Product findByIdEagerly(Integer id);
	public Optional<Product> findByIdEagerlyWithCategories(Integer id);
	public Optional<Product> findByIdEagerlyWithCategoriesAndChildren(Integer id);
	public List<Product> findAllEagerly();
	public List<Product> findAllEagerlyWithCategories();
	public List<Product> findAllEagerlyWithCategoriesAndChildren();
	public List<Product> findByMainCategoryIdEagerly(Integer id);
	public List<Product> findByMainCategoryIdEagerlyWithCategories(Integer id);
	public List<Product> findByMainCategoryIdEagerlyWithCategoriesAndChildren(Integer id);
	public List<Product> findAllProductsByCategoryId(Integer id);
	public List<Product> findAllWithoutImgPath();
	public List<Product> findAllWithoutMainCategoryName();
	

}
