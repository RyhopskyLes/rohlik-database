package com.rohlik.data;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
import com.rohlik.data.entities.Product;
import com.rohlik.data.objects.NavSectionsCategoryData;
import com.rohlik.data.objects.ProductsInCategory;
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
	@Autowired
	ProductsInCategory inCategory;
	private static final Integer CHLEB_VOLNY =300116463;
	private static final Integer TMAVY_CHLEB =300116467;
	@Test
	@Order(1) 
	@DisplayName("should save products from 300116467")	
	public void saveAllProductsInCategoryToDatabase() {		
		List<NavSectionsCategoryData> categories = navSections.ofCategory(CHLEB_VOLNY);
		NavSectionsCategoryData tmavyChleb = categories.stream().filter(category->category.getCategoryId().equals(TMAVY_CHLEB)).findFirst().orElseGet(NavSectionsCategoryData::new);
		productService.saveAllProductsInCategoryToDatabase(TMAVY_CHLEB, new HashSet<>());
		List<Product> products = productDao.findAllEagerlyWithCategories();
		List<Product> incategory = 	inCategory.getProductListForCategoryWithSalesAndProducers(TMAVY_CHLEB, 100);
		assertTrue(products.size() == tmavyChleb.getProductsCount());
		Long sales1= products.stream().map(Product::getSales).filter(sales->!sales.isEmpty()).flatMap(Set::stream).count();
		Long sales2= incategory.stream().map(Product::getSales).filter(sales->!sales.isEmpty()).flatMap(Set::stream).count();
		Set<String> producers1 = products.stream().map(Product::getProducer).collect(Collectors.toCollection(HashSet::new));
		Set<String> producers2 = incategory.stream().map(Product::getProducer).collect(Collectors.toCollection(HashSet::new));
		Set<String> names1 = products.stream().map(Product::getProductName).collect(Collectors.toCollection(HashSet::new));
		Set<String> names2 = incategory.stream().map(Product::getProductName).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> ids1 = products.stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> ids2 = incategory.stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));
		assertTrue(products.size() == incategory.size());	
		assertTrue(sales1 == sales2);
		assertThat(producers1, containsInAnyOrder(producers2.toArray(new String[producers2.size()])));
		assertThat(names1, containsInAnyOrder(names2.toArray(new String[names2.size()])));
		assertThat(ids1, containsInAnyOrder(ids2.toArray(new Integer[ids2.size()])));
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

	@Test
	@Order(4) 
	@DisplayName("should build category TMAVY_CHLEB")	
	public void buildAllProductsInCategory() {
	List<Product> products = productService.buildAllProductsInCategory(TMAVY_CHLEB);
	List<Product> incategory = inCategory.getProductListForCategoryWithSalesAndProducers(TMAVY_CHLEB, 100);
	Long sales1= products.stream().map(Product::getSales).filter(sales->!sales.isEmpty()).flatMap(Set::stream).count();
	Long sales2= incategory.stream().map(Product::getSales).filter(sales->!sales.isEmpty()).flatMap(Set::stream).count();
	Set<String> producers1 = products.stream().map(Product::getProducer).collect(Collectors.toCollection(HashSet::new));
	Set<String> producers2 = incategory.stream().map(Product::getProducer).collect(Collectors.toCollection(HashSet::new));
	Set<String> names1 = products.stream().map(Product::getProductName).collect(Collectors.toCollection(HashSet::new));
	Set<String> names2 = incategory.stream().map(Product::getProductName).collect(Collectors.toCollection(HashSet::new));
	Set<Integer> ids1 = products.stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));
	Set<Integer> ids2 = incategory.stream().map(Product::getProductId).collect(Collectors.toCollection(HashSet::new));

	assertTrue(products.size() == incategory.size());	
	assertTrue(sales1 == sales2);
	assertThat(producers1, containsInAnyOrder(producers2.toArray(new String[producers2.size()])));
	assertThat(names1, containsInAnyOrder(names2.toArray(new String[names2.size()])));
	assertThat(ids1, containsInAnyOrder(ids2.toArray(new Integer[ids2.size()])));
	}
	
	
}
