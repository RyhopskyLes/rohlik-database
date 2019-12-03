package com.rohlik.data.commons.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rohlik.data.commons.repos.ProductRepository;
import com.rohlik.data.entities.Product;
@Repository("productDao")
@Transactional
public class ProductDaoImpl implements ProductDao {
	@Autowired
	 private ProductRepository productRepository;
	@PersistenceContext
	private EntityManager em;	
	@Override
	public Product findById(Integer id) {
		return productRepository.findById(id).orElse(null);
	}

	@Override
	public List<Product> findAll() {
		return productRepository.findAll();
	}

	@Override
	public Product save(Product product) {
		
		return productRepository.save(product);
	}

	
	@Override
	public void delete(Product product) {
		productRepository.delete(product);		
	}

	@Override
	public Optional<Product> findByProductId(Integer id) {
		return productRepository.findByProductId(id);
	}

	@Override
	public List<Product> findAllPremiumProducts() {
		Query query = em.createNativeQuery("SELECT product.id, product.productId, product.productName, product.producer, product.originalPrice, product.price,"
				+ "product.textualAmount, product.unit, product.baseLink, product.imgPath, product.inStock, product.hasSales, product.link, product.pricePerUnit,"
				+ "product.mainCategoryId, product.mainCategoryName"
				+ " FROM product\r\n" + 
				" inner join product_sales on product_sales.id_product=product.id\r\n" + 
				" inner join sales on product_sales.id_sales=sales.idSales where sales.type=\"premium\";", Product.class);
		
		return query.getResultList();
	}

	@Override
	public List<Product> findAllProductsWithNearingExpiryDate() {
		Query query = em.createNativeQuery("SELECT product.id, product.productId, product.productName, product.producer, product.originalPrice, product.price,"
				+ "product.textualAmount, product.unit, product.baseLink, product.imgPath, product.inStock, product.hasSales, product.link, product.pricePerUnit,"
				+ "product.mainCategoryId, product.mainCategoryName"
				+ " FROM product\r\n" + 
				" inner join product_sales on product_sales.id_product=product.id\r\n" + 
				" inner join sales on product_sales.id_sales=sales.idSales where sales.type=\"expiration\";", Product.class);
		
		return query.getResultList();
	}

	@Override
	public List<Product> findAllProductsWithoutProducer() {
		Query query = em.createNativeQuery("SELECT * FROM rohlik_data.product where producer=\"\";", Product.class);
		
		return query.getResultList();
	}

	@Override
	public Product findByIdEagerly(Integer id) {
		return productRepository.findByIdEagerly(id);
	}

	@Override
	public List<Product> findAllEagerly() {
		return productRepository.findAllEagerly();
	}

	@Override
	public List<Product> findByMainCategoryIdEagerly(Integer id) {
		return productRepository.findByMainCategoryIdEagerly(id);
	}

	@Override
	public Optional<Product> findByIdEagerlyWithCategories(Integer id) {
		return productRepository.findByIdEagerlyWithCategories(id);
	}

	@Override
	public Optional<Product> findByIdEagerlyWithCategoriesAndChildren(Integer id) {
		return productRepository.findByIdEagerlyWithCategoriesAndChildren(id);
	}

	@Override
	public List<Product> findAllEagerlyWithCategories() {
		return productRepository.findAllEagerlyWithCategories();
	}

	@Override
	public List<Product> findAllEagerlyWithCategoriesAndChildren() {
		return productRepository.findAllEagerlyWithCategoriesAndChildren();
	}

	@Override
	public List<Product> findByMainCategoryIdEagerlyWithCategories(Integer id) {
		return productRepository.findByMainCategoryIdEagerlyWithCategories(id);
	}

	@Override
	public List<Product> findByMainCategoryIdEagerlyWithCategoriesAndChildren(Integer id) {
		return productRepository.findByMainCategoryIdEagerlyWithCategoriesAndChildren(id);
	}

	@Override
	public Optional<Product> findByProductIdEagerlyWithCategoriesAndChildren(Integer id) {
		return productRepository.findByProductIdEagerlyWithCategoriesAndChildren(id);
	}

	@Override
	public List<Product> findAllProductsByCategoryId(Integer id) {
		return productRepository.findAllProductsByCategoryId(id);
	}

	@Override
	public List<Product> findAllWithoutImgPath() {
		return productRepository.findAllWithoutImgPath();
	}

	@Override
	public List<Product> findAllWithoutMainCategoryName() {
		return productRepository.findAllWithoutMainCategoryName();
	}
}
