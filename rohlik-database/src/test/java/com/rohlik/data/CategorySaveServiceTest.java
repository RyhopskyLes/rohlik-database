package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.hamcrest.Matchers;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.services.save.CategorySaveService;
import com.rohlik.data.config.AppConfigTestContainer;
import com.rohlik.data.config.AppEmptyDBConfig;
import com.rohlik.data.config.EmptyDBConfig;
import com.rohlik.data.config.TestContainerConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.NavSectionsCategoryData;

@SpringJUnitConfig(classes = {/*AppEmptyDBConfig.class, EmptyDBConfig.class*/AppConfigTestContainer.class, TestContainerConfig.class})
@DisplayName("Integration CategorySaveService Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("testContainer")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:application-test_container.properties")
public class CategorySaveServiceTest {
	private static Logger logger = LoggerFactory.getLogger(CategorySaveServiceTest.class);
	@Autowired
	CategorySaveService saveService;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	DataSource dataSource;
	@Autowired
	@Container
    public MySQLContainer mysqlContainer;
	
	private final Integer PEKARNA=300101000;
	private final Integer CHIPSY_A_BRAMBURKY=300106154;
	private final Integer ZVIRE=300112000;
	
	
	
	@Test
	@Order(1) 
	@Transactional
	@DisplayName("should save category 300101000")
	public void saveMainCategory() {
		Optional<Category> pekarna = saveService.saveMainCategory(PEKARNA);
		assertEquals(true, pekarna.isPresent());
		assertEquals(Optional.ofNullable("Pekárna a cukrárna"), pekarna.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300101000), pekarna.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(0), pekarna.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), pekarna.map(Category::getActive));	
		assertTrue(pekarna.map(Category::getId).isPresent());
		assertThat(pekarna.orElseGet(Category::new), hasProperty("id", notNullValue()));
		logger.info("Test n. 1 finished");
		}

	@Test
	@Order(2) 
	@Transactional
	@DisplayName("should save category 300101000 with Children")
	public void saveMainCategoryWithChildren() {
		assertTrue(categoryDao.findAll().isEmpty());
		Optional<Category> pekarna = saveService.saveMainCategoryWithChildren(PEKARNA);
		Set<Child> children =pekarna.map(Category::getChildren).orElseGet(HashSet::new);
		assertEquals(true, pekarna.isPresent());
		assertEquals(Optional.ofNullable("Pekárna a cukrárna"), pekarna.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300101000), pekarna.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(0), pekarna.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), pekarna.map(Category::getActive));	
		assertTrue(children.size()==7);
		assertThat(children, hasItems(
				Matchers.<Child>hasProperty("categoryId", equalTo(300101024)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101012)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101007)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101049)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101043)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101033)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101019)),
				Matchers.<Child>hasProperty("id", notNullValue())				
				));	
		logger.info("Test n. 2 finished");
	}
	
	@Test
	@Order(3) 
	@Transactional
	@DisplayName("should save 14 categories")
	public void saveAllMainCategories() {
		assertTrue(categoryDao.findAll().isEmpty());
		List<Category> all = saveService.saveAllMainCategories();
		assertThat(all, hasSize(14));
		assertThat(all, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300101000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300102000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300103000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300104000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300105000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300106000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300107000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300108000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300109000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300110000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300111000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112393)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112985)),
				Matchers.<Category>hasProperty("id", notNullValue())				
				));	
		all.forEach(category->assertThat(category.getChildren(), hasSize(0)));
		logger.info("Test n. 3 finished");
		}
	@Test
	@Order(4) 
	@Transactional
	@DisplayName("should save 14 categories with children")
	public void saveAllMainCategoriesWithChildren() {
		assertTrue(categoryDao.findAll().isEmpty());
		List<Category> all = saveService.saveAllMainCategoriesWithChildren();
		Category pekarna = all.stream().filter(category->category.getCategoryId().equals(PEKARNA)).findFirst().orElseGet(Category::new);
		assertThat(all, hasSize(14));
		assertThat(all, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300101000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300102000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300103000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300104000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300105000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300106000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300107000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300108000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300109000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300110000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300111000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112393)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112985)),
				Matchers.<Category>hasProperty("id", notNullValue())
				)				
				);	
		all.forEach(category->System.out.println(category.getChildren()));
	//	all.forEach(category->assertThat(category.getChildren(), not(IsEmptyCollection.empty())));
		assertTrue(pekarna.getChildren().size()==7);
		assertThat(pekarna.getChildren(), hasItems(
				Matchers.<Child>hasProperty("categoryId", equalTo(300101024)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101012)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101007)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101049)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101043)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101033)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300101019))											
				));	
		logger.info("Test n. 4 finished");
		}
	
	@Test
	@Order(5) 
	@Transactional
	@DisplayName("should save category 300106154")
	public void saveCategory() {
		assertTrue(categoryDao.findAll().isEmpty());
		Optional<Category> chipsy = saveService.saveCategory(CHIPSY_A_BRAMBURKY);
		assertEquals(true, chipsy.isPresent());
		assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300106154), chipsy.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(300106153), chipsy.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));
		assertTrue(chipsy.map(Category::getId).isPresent());
		assertThat(chipsy.orElseGet(Category::new), Matchers.<Category>hasProperty("id", notNullValue()));	
		logger.info("Test n. 5 finished");
		}
	
	@Test
	@Order(6)
	@Transactional
	@DisplayName("should save category 300106154 with children")
	public void saveCategoryWithChildren() {
		assertTrue(categoryDao.findAll().isEmpty());
		Optional<Category> chipsy = saveService.saveCategoryWithChildren(CHIPSY_A_BRAMBURKY);
		Set<Child> children = chipsy.orElseGet(Category::new).getChildren();
		assertEquals(true, chipsy.isPresent());
		assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300106154), chipsy.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(300106153), chipsy.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));
		assertTrue(chipsy.map(Category::getId).isPresent());
		assertThat(chipsy.orElseGet(Category::new), Matchers.<Category>hasProperty("id", notNullValue()));
		assertThat(children, hasSize(2));
		assertThat(children, hasItems(
				Matchers.<Child>hasProperty("categoryId", equalTo(300114929)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300114931))															
				));	
		
		logger.info("Test n. 6 finished");
		}
	
	@Test
	@Order(7) 
	@Transactional
	@DisplayName("should save category 300112000")
	public void saveCompleteTreeOfMainCategoryZvire() {
		Map<Integer, Set<Category>> zvire = saveService.saveCompleteTreeOfMainCategory(ZVIRE);
		zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
		Set<Category> levelZero = zvire.get(0);
		Set<Category> levelOne = zvire.get(1);
		Set<Category> levelTwo = zvire.get(2);
		Set<Category> levelThree = zvire.get(3);
		Category zero = levelZero.iterator().next();
		assertThat(levelZero, hasSize(1));
		assertThat(levelZero, hasItems(
				 Matchers.<Category>hasProperty("id", notNullValue()),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Zvíře")),
				Matchers.<Category>hasProperty("parentId", equalTo(0))
				));
		assertThat(zero.getChildren(), hasSize(4));
		assertThat(zero.getChildren(), hasItems(
				Matchers.<Child>hasProperty("id", notNullValue()),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Pes")),				
				Matchers.<Child>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Ptactvo"))				
				));	
		assertThat(levelOne, hasSize(4));
		assertThat(levelOne, hasItems(
				Matchers.<Category>hasProperty("id", notNullValue()),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pes")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Ptactvo")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000))
				));	
		assertThat(levelTwo, hasSize(17));
		assertThat(levelTwo, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300112003)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Konzervy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112004)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112002)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115115)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Štěňata")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112008)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Psí hygiena a zdraví")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112014)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Podestýlky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114373)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112009)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112012)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky, konzervy a vaničky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112011)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112013)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112875)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky a paštiky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112019)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114103)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112022)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112020)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Steliva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112017)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní"))						
				));	
		assertThat(levelThree, hasSize(8));
		assertThat(levelThree, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300114067)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Vzorky - vyzkoušejte")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115111)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Konzervy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115113)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Vaničky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112881)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Velká plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112879)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Střední plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114369)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Mražené")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112877)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115109)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky"))									
				));	
		logger.info("Test n. 7 finished");
	}
	
	@Test
	@Order(8) 
	@Transactional
	@DisplayName("should save category 300112000 to level 2")
	public void saveCompleteTreeOfMainCategoryZvireToLevel2() {
		Map<Integer, Set<Category>> zvire = saveService.saveCompleteTreeOfMainCategoryDownToLevel(ZVIRE, 2);
		zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
		Set<Category> levelZero = zvire.get(0);
		Set<Category> levelOne = zvire.get(1);
		Set<Category> levelTwo = zvire.get(2);
		Category zero = levelZero.iterator().next();
		assertThat(levelZero, hasSize(1));
		assertThat(levelZero, hasItems(
				Matchers.<Category>hasProperty("id", notNullValue()),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Zvíře")),
				Matchers.<Category>hasProperty("parentId", equalTo(0))
				));
		assertThat(zero.getChildren(), hasSize(4));
		assertThat(zero.getChildren(), hasItems(
				Matchers.<Child>hasProperty("id", notNullValue()),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Pes")),				
				Matchers.<Child>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Ptactvo"))				
				));	
		assertThat(levelOne, hasSize(4));
		assertThat(levelOne, hasItems(
				Matchers.<Category>hasProperty("id", notNullValue()),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pes")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Ptactvo")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000))
				));	
		assertThat(levelTwo, hasSize(17));
		assertThat(levelTwo, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300112003)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Konzervy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112004)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112002)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115115)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Štěňata")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112008)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Psí hygiena a zdraví")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112014)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Podestýlky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114373)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112009)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112012)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky, konzervy a vaničky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112011)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112013)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112875)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky a paštiky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112019)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114103)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112022)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112020)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Steliva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112017)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní"))						
				));		
		assertThat(zvire.keySet(), hasSize(3));
		assertThat(zvire.keySet(), hasItems(0, 1, 2));	
		logger.info("Test n. 8 finished");
	}
	
	@Test
	@Order(9) 
	@Transactional
	@DisplayName("should build category 300112000 from level 2 to level 3")
	public void saveCompleteTreeOfMainCategoryZvireFromLevel2ToLevel3() {
		Map<Integer, Set<Category>> zvire = saveService.saveCompleteTreeOfMainCategoryFromLevelToLevel(ZVIRE, 2, 3);
		zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
		Set<Category> levelThree = zvire.get(3);
		assertThat(zvire.keySet(), hasSize(2));
		assertThat(zvire.keySet(), hasItems(2, 3));	
		assertThat(levelThree, hasSize(8));
		assertThat(levelThree, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300114067)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Vzorky - vyzkoušejte")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115111)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Konzervy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115113)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Vaničky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112881)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Velká plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112879)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Střední plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114369)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Mražené")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112877)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá plemena")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115109)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky"))									
				));	
		logger.info("Test n. 9 finished");
	}
	
	@Test
	@Order(10) 
	@Transactional
	@DisplayName("should build level 2 of category 300112000")
	public void saveLevel2ofCategoryZvire() {
		 Set<Category> zvire = saveService.saveLevelFromCompleteTreeOfMainCategory(ZVIRE, 2);
		assertThat(zvire, hasSize(17));
		assertThat(zvire, hasItems(
				Matchers.<Category>hasProperty("categoryId", equalTo(300112003)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Konzervy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112004)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112002)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300115115)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Štěňata")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112008)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Psí hygiena a zdraví")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112014)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Podestýlky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114373)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112009)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112012)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky, konzervy a vaničky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112011)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Granulovaná krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112013)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pamlsky a doplňky stravy")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112875)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kapsičky a paštiky")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112019)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300114103)),
				Matchers.<Category>hasProperty("categoryName", equalTo("BARF")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112022)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Krmiva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112020)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Steliva")),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112017)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Hračky, misky a ostatní"))						
				));	
		logger.info("Test n. 10 finished");
	}
	
	@Test
	@Order(11) 
	@Transactional
	@DisplayName("should build lowest levels of category 300112000")
	public void saveLowestLevelOfEachBranchOfCategoryZvire() {
		Map<Integer, Set<Category>> zvire = saveService.saveLowestLevelOfEachBranchOfMainCategoryTree(ZVIRE);
		assertThat(zvire.get(2), hasSize(14));	
		assertThat(zvire.get(3), hasSize(8));
		zvire.get(2).stream().map(Category::getChildren).forEach(children->is(IsEmptyCollection.empty()));
		zvire.get(3).stream().map(Category::getChildren).forEach(children->is(IsEmptyCollection.empty()));
		logger.info("Test n. 11 finished");
	}
	

}
