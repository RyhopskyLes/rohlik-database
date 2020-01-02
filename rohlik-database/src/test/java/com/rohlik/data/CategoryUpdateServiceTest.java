package com.rohlik.data;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.services.save.CategorySaveService;
import com.rohlik.data.commons.services.update.CategoryUpdateService;
import com.rohlik.data.config.AppConfigTestContainer;
import com.rohlik.data.config.AppEmptyDBConfig;
import com.rohlik.data.config.EmptyDBConfig;
import com.rohlik.data.config.TestContainerConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;
import com.rohlik.data.objects.NavSections;

@SpringJUnitConfig(classes = {AppConfigTestContainer.class, TestContainerConfig.class})
@DisplayName("Unit CategoryUpdateServiceTest Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("testContainer")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:application-test_container.properties")
public class CategoryUpdateServiceTest {
	private static Logger logger = LoggerFactory.getLogger(CategoryUpdateServiceTest.class);
	@Autowired
	private CategorySaveService saveService;
	@Autowired
	private CategoryUpdateService updateService;
	@Autowired
	private CategoryDao categoryDao;
	@Autowired	
	private NavSections navSections;
	@Autowired
	DataSource dataSource;
	@Autowired
	@Container
    public MySQLContainer mysqlContainer;
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
	 
	
		
	@Test
	@Order(1) 
	@DisplayName("should test deactivation")
	@Transactional
	public void testDeactivation() {
		logger.info("MySQL exposed ports: {}", mysqlContainer.getExposedPorts());
		saveService.saveCompleteTreeOfMainCategory(ZVIRE);
		categoryDao.findAll().forEach(System.out::println);
		Category toDeactivate = new Category(20, "DeactivationTest", 300112000, true);
		Child childToDeactivate = new Child(20, "DeactivationTest", true);
		categoryDao.save(toDeactivate);
		categoryDao.findAll().forEach(System.out::println);
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		if(zvire!=null){zvire.addChild(childToDeactivate);
	categoryDao.save(zvire);
	zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
	Optional<Child> persisted = zvire.getChildren().stream().filter(child->child.getCategoryId().equals(20)).findFirst();	
	Category toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(20);
	assertEquals(true, persisted.isPresent());
	assertEquals(Optional.of(true), persisted.map(Child::getActive));
	assertEquals(true, Objects.nonNull(toDeactivatePersisted));
	updateService.updateCompleteTreeOfMainCategory(ZVIRE);
	persisted = zvire.getChildren().stream().filter(child->child.getCategoryId().equals(20)).findFirst();	
	toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(20);
	assertEquals(true, persisted.isPresent());
	assertEquals(Optional.of(false), persisted.map(Child::getActive));
	assertEquals(false, toDeactivatePersisted.getActive());}
	
	}
	@Test
	@Order(2) 
	@DisplayName("should test deactivation")
	@Transactional
	public void addingNewCategory() {
		logger.info("Starting loading data...");
		categoryDao.findAll().forEach(System.out::println);
		//to do
		logger.info("Finished loading data...");
	}
	
}
