package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.services.save.CategorySaveService;
import com.rohlik.data.config.AppEmptyDBConfig;
import com.rohlik.data.config.EmptyDBConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.NavSectionsCategoryData;

@SpringJUnitConfig(classes = {AppEmptyDBConfig.class, EmptyDBConfig.class})
@DisplayName("Unit CategorySaveService Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("emptyDB")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategorySaveServiceTest {
	private static Logger logger = LoggerFactory.getLogger(CategorySaveServiceTest.class);
	@Autowired
	CategorySaveService saveService;
	@Autowired
	CategoryDao categoryDao;
	@Autowired
	DataSource dataSource;
	@Autowired	
	private NavSections navSections;
	
	private final Integer PEKARNA=300101000;
	private final Integer CHIPSY_A_BRAMBURKY=300106154;
	private final Integer VOLNE_PRODEJNE_LEKY=300113171;
	private final Integer LEKARNA=300112985;
	private final Integer LUSTENINOVE_A_RYZOVE=300114931;
	private final Integer SLANE_SNACKY_A_ORECHY =300106153;
	private final Integer FRANCOUZSKA=300116325;
	private final Integer CERVENA=300108066;
	private final Integer BORDEAUX=300116331;
	private final Integer ZVIRE=300112000;
	
	@AfterEach
	public void tearDown() {
	    try {
	        clearDatabase();
	        logger.info("Inmemory database cleared!");
	    } catch (Exception e) {
	       logger.info(e.getMessage());
	    }
	}
	
	@Test
	@Order(1) 
	@DisplayName("should save category 300101000")
	public void saveMainCategory() {
		Optional<Category> pekarna = saveService.saveMainCategory(PEKARNA);
		assertEquals(true, pekarna.isPresent());
		assertEquals(Optional.ofNullable("Pekárna a cukrárna"), pekarna.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300101000), pekarna.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(0), pekarna.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), pekarna.map(Category::getActive));	
		assertTrue(pekarna.map(Category::getId).isPresent());
		assertTrue(pekarna.map(Category::getId).get().equals(1));	
		logger.info("Test n. 1 finished");
		}

	@Test
	@Order(2) 
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
				Matchers.<Child>hasProperty("id", equalTo(1)),
				Matchers.<Child>hasProperty("id", equalTo(2)),
				Matchers.<Child>hasProperty("id", equalTo(3)),
				Matchers.<Child>hasProperty("id", equalTo(4)),
				Matchers.<Child>hasProperty("id", equalTo(5)),
				Matchers.<Child>hasProperty("id", equalTo(6)),
				Matchers.<Child>hasProperty("id", equalTo(7))							
				));	
		logger.info("Test n. 2 finished");
	}
	
	@Test
	@Order(3) 
	@DisplayName("should save 14 categories")
	public void saveAllMainCategories() {
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
				Matchers.<Category>hasProperty("id", equalTo(1)),
				Matchers.<Category>hasProperty("id", equalTo(2)),
				Matchers.<Category>hasProperty("id", equalTo(3)),
				Matchers.<Category>hasProperty("id", equalTo(4)),
				Matchers.<Category>hasProperty("id", equalTo(5)),
				Matchers.<Category>hasProperty("id", equalTo(6)),
				Matchers.<Category>hasProperty("id", equalTo(7)),
				Matchers.<Category>hasProperty("id", equalTo(8)),
				Matchers.<Category>hasProperty("id", equalTo(9)),
				Matchers.<Category>hasProperty("id", equalTo(10)),
				Matchers.<Category>hasProperty("id", equalTo(11)),
				Matchers.<Category>hasProperty("id", equalTo(12)),
				Matchers.<Category>hasProperty("id", equalTo(13)),
				Matchers.<Category>hasProperty("id", equalTo(14))				
				));	
		all.forEach(category->assertThat(category.getChildren(), hasSize(0)));
		logger.info("Test n. 3 finished");
		}
	@Test
	@Order(4) 
	@DisplayName("should save 14 categories with children")
	public void saveAllMainCategoriesWithChildren() {
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
				Matchers.<Category>hasProperty("id", equalTo(1)),
				Matchers.<Category>hasProperty("id", equalTo(2)),
				Matchers.<Category>hasProperty("id", equalTo(3)),
				Matchers.<Category>hasProperty("id", equalTo(4)),
				Matchers.<Category>hasProperty("id", equalTo(5)),
				Matchers.<Category>hasProperty("id", equalTo(6)),
				Matchers.<Category>hasProperty("id", equalTo(7)),
				Matchers.<Category>hasProperty("id", equalTo(8)),
				Matchers.<Category>hasProperty("id", equalTo(9)),
				Matchers.<Category>hasProperty("id", equalTo(10)),
				Matchers.<Category>hasProperty("id", equalTo(11)),
				Matchers.<Category>hasProperty("id", equalTo(12)),
				Matchers.<Category>hasProperty("id", equalTo(13)),
				Matchers.<Category>hasProperty("id", equalTo(14))				
				));	
		all.forEach(category->assertThat(category.getChildren(), not(IsEmptyCollection.empty())));
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
	@DisplayName("should save category 300106154")
	public void saveCategory() {
		Optional<Category> chipsy = saveService.saveCategory(CHIPSY_A_BRAMBURKY);
		assertEquals(true, chipsy.isPresent());
		assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300106154), chipsy.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(300106153), chipsy.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));
		assertTrue(chipsy.map(Category::getId).isPresent());
		assertTrue(chipsy.map(Category::getId).get().equals(1));	
		logger.info("Test n. 5 finished");
		}
	
	@Test
	@Order(6)
	@DisplayName("should save category 300106154 with children")
	public void saveCategoryWithChildren() {
		Optional<Category> chipsy = saveService.saveCategoryWithChildren(CHIPSY_A_BRAMBURKY);
		Set<Child> children = chipsy.orElseGet(Category::new).getChildren();
		assertEquals(true, chipsy.isPresent());
		assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
		assertEquals(Optional.ofNullable(300106154), chipsy.map(Category::getCategoryId));
		assertEquals(Optional.ofNullable(300106153), chipsy.map(Category::getParentId));
		assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));
		assertTrue(chipsy.map(Category::getId).isPresent());
		assertTrue(chipsy.map(Category::getId).get().equals(1));
		assertThat(children, hasSize(2));
		assertThat(children, hasItems(
				Matchers.<Child>hasProperty("categoryId", equalTo(300114929)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300114931))															
				));	
		
		logger.info("Test n. 6 finished");
		}
	
	@Test
	@Order(7) 
	@DisplayName("should save category 300112000")
	public void saveCompleteTreeOfMainCategoryZvire() {
		Map<Integer, Set<Category>> zvire = saveService.saveCompleteTreeOfMainCategory(ZVIRE);
		zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
		Set<Category> levelZero = zvire.get(0);
		Set<Category> levelOne = zvire.get(1);
		Set<Category> levelTwo = zvire.get(2);
		Category zero = levelZero.iterator().next();
		assertThat(levelZero, hasSize(1));
		assertThat(levelZero, hasItems(
				Matchers.<Category>hasProperty("id", equalTo(1)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Zvíře")),
				Matchers.<Category>hasProperty("parentId", equalTo(0))
				));
		assertThat(zero.getChildren(), hasSize(4));
		assertThat(zero.getChildren(), hasItems(
				Matchers.<Child>hasProperty("id", equalTo(1)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Pes")),				
				Matchers.<Child>hasProperty("id", equalTo(2)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Child>hasProperty("id", equalTo(3)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Child>hasProperty("id", equalTo(4)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Ptactvo"))				
				));	
		assertThat(levelOne, hasSize(4));
		assertThat(levelOne, hasItems(
				Matchers.<Category>hasProperty("id", equalTo(2)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pes")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(3)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(4)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(5)),
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
		zvire.remove(0);
		Set<NavSectionsCategoryData> categories = navSections.completeTreeOfCategory(ZVIRE);
		Set<Integer> names =categories.stream().map(NavSectionsCategoryData::getCategoryId).collect(Collectors.toCollection(HashSet::new));
		Set<Integer> namesTree = zvire.values().stream().flatMap(Set::stream).map(Category::getCategoryId).collect(Collectors.toCollection(HashSet::new));
	namesTree.removeAll(names);

	int count =zvire.values().stream().map(Set::size).reduce(0, Integer::sum);
		assertEquals(categories.size()+namesTree.size(), count);
	}
	
	@Test
	@Order(8) 
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
				Matchers.<Category>hasProperty("id", equalTo(1)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112000)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Zvíře")),
				Matchers.<Category>hasProperty("parentId", equalTo(0))
				));
		assertThat(zero.getChildren(), hasSize(4));
		assertThat(zero.getChildren(), hasItems(
				Matchers.<Child>hasProperty("id", equalTo(1)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Pes")),				
				Matchers.<Child>hasProperty("id", equalTo(2)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Child>hasProperty("id", equalTo(3)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Child>hasProperty("id", equalTo(4)),
				Matchers.<Child>hasProperty("categoryId", equalTo(300112021)),
				Matchers.<Child>hasProperty("categoryName", equalTo("Ptactvo"))				
				));	
		assertThat(levelOne, hasSize(4));
		assertThat(levelOne, hasItems(
				Matchers.<Category>hasProperty("id", equalTo(2)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112001)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Pes")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(3)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112018)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Malá zvířata")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(4)),
				Matchers.<Category>hasProperty("categoryId", equalTo(300112010)),
				Matchers.<Category>hasProperty("categoryName", equalTo("Kočka")),
				Matchers.<Category>hasProperty("parentId", equalTo(300112000)),
				Matchers.<Category>hasProperty("id", equalTo(5)),
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
	}
	public void clearDatabase() throws SQLException {
	    Connection c = dataSource.getConnection();
	    Statement s = c.createStatement();

	    // Disable FK
	    s.execute("SET REFERENTIAL_INTEGRITY FALSE");

	    // Find all tables and truncate them
	    Set<String> tables = new HashSet<String>();
	    ResultSet rs = s.executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES  where TABLE_SCHEMA='PUBLIC'");
	    while (rs.next()) {
	        tables.add(rs.getString(1));
	    }
	    rs.close();
	    for (String table : tables) {
	        s.executeUpdate("TRUNCATE TABLE " + table);
	    }

	    // Idem for sequences
	    Set<String> sequences = new HashSet<String>();
	    rs = s.executeQuery("SELECT SEQUENCE_NAME FROM INFORMATION_SCHEMA.SEQUENCES WHERE SEQUENCE_SCHEMA='PUBLIC'");
	    while (rs.next()) {
	        sequences.add(rs.getString(1));
	    }
	    rs.close();
	    for (String seq : sequences) {
	        s.executeUpdate("ALTER SEQUENCE " + seq + " RESTART WITH 1");
	    }

	    // Enable FK
	    s.execute("SET REFERENTIAL_INTEGRITY TRUE");
	    s.close();
	    c.close();
	}
}
