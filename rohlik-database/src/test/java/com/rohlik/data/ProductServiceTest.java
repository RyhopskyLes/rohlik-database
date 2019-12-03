package com.rohlik.data;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.services.ProductService;
import com.rohlik.data.config.AppConfigTest;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.NavSectionsCategoryData;
import com.rohlik.data.objects.NavSections;

@SpringJUnitConfig(classes = {AppConfigTest.class, DataTestConfig.class})
@DisplayName("Integration ProductService Test")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceTest {
	@Autowired
	CategoryDao catDao;	
	@Autowired
	ProductDao productDao;
	@Autowired
	ProductService productService;
	@Autowired
	NavSections navSections;
	@Test
	@Order(1) 
	@DisplayName("should save products from 300116463")	
	public void saveAllProductsInCategoryToDatabase() {
		List<Product> products = productDao.findAllEagerlyWithCategories();
		List<NavSectionsCategoryData> categories = navSections.ofCategory(300116463);
		NavSectionsCategoryData tmavyChleb = categories.stream().filter(category->category.getCategoryId().equals(300116467)).findFirst().orElseGet(NavSectionsCategoryData::new);
		productService.saveAllProductsInCategoryToDatabase(300116467, new HashSet<>());
		products = productDao.findAllEagerlyWithCategories();
		assertTrue(products.size() == tmavyChleb.getProductsCount());		
	}

	@Test
	@Order(2) 
	@DisplayName("should set MainCategoryName by 1 product")	
	public void setMissingMainCategoryName() {
		productService.saveAllProductsInCategoryToDatabase(300101013, new HashSet<>());
		Optional<Product> rohlik = productDao.findByProductId(1286399);
		rohlik.ifPresent(roh->{roh.setMainCategoryName("");
		productDao.save(roh);
		});
		rohlik=productDao.findByProductId(1286399);
		assertTrue(rohlik.isPresent());
		assertTrue(rohlik.get().getMainCategoryName().equals(""));	
		List<Product> without =productDao.findAllWithoutMainCategoryName();
		assertTrue(without.size()==1);		
		Integer added = productService.addMissingMainCategoryNameToProducts();		
		assertTrue(added.equals(0));
		rohlik = productDao.findByProductId(1286399);
		assertTrue(rohlik.get().getMainCategoryName().equals("Rohlíky"));	
	}
	
	@Test
	@Order(3) 
	@DisplayName("should set categories by 1 product")	
	public void setCategoriesForProducts() {
		productDao.findAllEagerlyWithCategories().stream().limit(2).forEach(product->product.getCategories().forEach(System.out::println));
		
	}

}
