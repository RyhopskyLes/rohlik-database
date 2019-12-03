package com.rohlik.data;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.dao.ProductKosikDao;
import com.rohlik.data.commons.objects.Converter;
import com.rohlik.data.commons.services.ProductKosikService;
import com.rohlik.data.commons.services.ProductService;
import com.rohlik.data.commons.utilities.DataRohlik;
import com.rohlik.data.config.AppConfigTest;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.kosik.components.ProductKosikOverview;
import com.rohlik.data.kosik.entities.ProductKosik;

@SpringJUnitConfig(classes = {AppConfigTest.class, DataTestConfig.class})
@DisplayName("Integration ProductKosikService Test")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ProductKosikServiceTest {
	static final String vinaURL = "https://www.kosik.cz/napoje/vina/cervena";
	private static Logger log = LoggerFactory.getLogger(ProductKosikServiceTest.class);

	@Autowired
	ProductKosikService productService;
	@Autowired
	ProductKosikDao productDao;
	@Autowired
	ProductService prodService;
	@Autowired
	ProductKosikOverview productKosikOverview;
	@Autowired
	Converter mapper;
	@BeforeAll
	public void setup() {
	    prodService.saveAllProductsInCategoryToDatabase(300108066, new HashSet<>());
	}
	@Test
	@Order(1) 
	@DisplayName("should save products Napoje")	
	public void saveAllProductsInCategoryToDatabase() {
		List<ProductKosik> products = productDao.findAll();
		assertTrue(products.isEmpty());
		List<ProductKosik> toSave = productKosikOverview.getProductKosikListForcategoryGroupedByProducers(vinaURL);
		Optional<Elements> raw =productKosikOverview.getCompleteProductElementListUsingPagination(vinaURL);
		Set<ProductKosik>  rawProducts = raw.get().stream().map(element->mapper.toProductKosik(null, element)).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
		productService.saveAllKosikProductsInCategoryToDatabase(vinaURL);
		products = productDao.findAll();
		Set<Integer> uniqueIds = products.stream().map(ProductKosik::getIdProduct).collect(Collectors.toCollection(HashSet::new));
			
		log.info("saved: "+products.size()+" uniqueIdsSaved: "+uniqueIds.size()+" raw: "+raw.get().size()+" "+rawProducts.size()+" expected: "+toSave.size());
		products.stream().filter(product->product.getProduct()!=null)
		.forEach(product->System.out.println(product.getName()+"\t"+product.getProduct().getProductName()));
		assertEquals(products.size(), toSave.size());
	}
	
	
}
