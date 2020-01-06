package com.rohlik.data;

import java.util.List;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.emptyOrNullString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.testcontainers.shaded.org.apache.commons.lang.math.NumberUtils;

import com.rohlik.data.commons.services.build.ProductBuildService;
import com.rohlik.data.config.AppEmptyDBConfig;
import com.rohlik.data.config.EmptyDBConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Product;
import com.rohlik.data.entities.Sale;
import com.rohlik.data.objects.ProductsInCategory;

@SpringJUnitConfig(classes = {AppEmptyDBConfig.class, EmptyDBConfig.class})
@DisplayName("Unit ProductBuildService Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("emptyDB")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductBuildServiceTest {
	private static Logger logger = LoggerFactory.getLogger(ProductBuildServiceTest.class);		
	@Autowired	
	private ProductBuildService buildService;
	@Autowired	
	private ProductsInCategory inCategory;
	private final Integer LUSTENINOVE_A_RYZOVE=300114931;
	private final Integer ZVIRE=300112000;
	private final Integer PEKARNA=300101000;
	private final Integer OVOCE=300102000;
	private final Integer MASO=300103000;
	private final Integer UZENINY=300104000;
	private final Integer MLECNE=300105000;
	private final Integer TRVANLIVE=300106000;
	private final Integer MRAZENE=300107000;
	private final Integer NAPOJE=300108000;
	private final Integer SPECIALNI=300112393;
	private final Integer DITE=300110000;
	private final Integer DROGERIE=300109000;
	private final Integer DOMACNOST=300111000;
	private final Integer LEKARNA=300112985;
	@Test
	@Order(1) 
	@DisplayName("should build products in category 300114931")
	public void buildProductsInCategoryLUSTENINOVE_A_RYZOVE() {
		List<Product> lusteninove = buildService.buildAllProductsInCategoryWithoutProducers(LUSTENINOVE_A_RYZOVE);
		Optional<String> totalHits = inCategory.getTotalHitsForCategory(LUSTENINOVE_A_RYZOVE);
		assertEquals(lusteninove.size(), NumberUtils.toInt(totalHits.orElseGet(()->"0")));		
		lusteninove.forEach(item->assertThat(item, allOf(
				Matchers.<Product>hasProperty("id", nullValue()),
				Matchers.<Product>hasProperty("productId", notNullValue()),
				Matchers.<Product>hasProperty("productName", notNullValue()),
				Matchers.<Product>hasProperty("producer", nullValue()),
				Matchers.<Product>hasProperty("originalPrice", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("price", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("textualAmount", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("unit", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("baseLink", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("imgPath", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("inStock", notNullValue()),
				Matchers.<Product>hasProperty("active", notNullValue()),
				Matchers.<Product>hasProperty("hasSales", notNullValue()),
				Matchers.<Product>hasProperty("link", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("pricePerUnit", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("mainCategoryId", notNullValue()),
				Matchers.<Product>hasProperty("isFromRohlik", is(true)),
				Matchers.<Product>hasProperty("sales", Matchers.<Sale> iterableWithSize(greaterThanOrEqualTo(0))),
				Matchers.<Product>hasProperty("categories", Matchers.<Category> iterableWithSize(0)),
				Matchers.<Product>hasProperty("productKosik", nullValue())
				)));
			}
	@Test
	@Order(2) 
	@DisplayName("should build all products in category 300112000")
	public void buildAllProductsWithProducersAndCategoriesInCategoryZVIRE() {
		Integer test =ZVIRE;
		List<Product> zvire = buildService.buildAllProductsInMainCategoryWithProducers(test);
		Optional<String> totalHits = inCategory.getTotalHitsForCategory(test);
		logger.info("size {}, totalHits {}", zvire.size(), totalHits);
		assertEquals(zvire.size(), NumberUtils.toInt(totalHits.orElseGet(()->"0")));
		zvire.forEach(item->assertThat(item, allOf(
				Matchers.<Product>hasProperty("id", nullValue()),
				Matchers.<Product>hasProperty("productId", notNullValue()),
				Matchers.<Product>hasProperty("productName", notNullValue()),
				Matchers.<Product>hasProperty("producer", notNullValue()),
				Matchers.<Product>hasProperty("originalPrice", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("price", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("textualAmount", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("unit", notNullValue()),
				Matchers.<Product>hasProperty("baseLink", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("imgPath", notNullValue()),
				Matchers.<Product>hasProperty("inStock", notNullValue()),
				Matchers.<Product>hasProperty("active", notNullValue()),
				Matchers.<Product>hasProperty("hasSales", notNullValue()),
				Matchers.<Product>hasProperty("link", is(not(emptyOrNullString()))),
				Matchers.<Product>hasProperty("pricePerUnit", is(greaterThanOrEqualTo(0.0))),
				Matchers.<Product>hasProperty("mainCategoryId", notNullValue()),
				Matchers.<Product>hasProperty("isFromRohlik", is(true)),
				Matchers.<Product>hasProperty("sales", Matchers.<Sale> iterableWithSize(greaterThanOrEqualTo(0))),
				Matchers.<Product>hasProperty("categories", Matchers.<Category> iterableWithSize(greaterThanOrEqualTo(1))),
				Matchers.<Product>hasProperty("productKosik", nullValue())
				)));
		
			}
}
