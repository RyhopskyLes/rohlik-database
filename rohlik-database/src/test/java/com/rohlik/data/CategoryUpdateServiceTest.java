package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

import java.util.Objects;
import java.util.Optional;
import javax.sql.DataSource;

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

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.ChildDao;
import com.rohlik.data.commons.objects.Registry;
import com.rohlik.data.commons.services.save.CategorySaveService;
import com.rohlik.data.commons.services.update.CategoryUpdateService;
import com.rohlik.data.config.AppConfigTestContainer;
import com.rohlik.data.config.TestContainerConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.entities.Child;

@SpringJUnitConfig(classes = { AppConfigTestContainer.class,
		TestContainerConfig.class/* , RegistryRepositoryMySqlImpl.class */ })
@DisplayName("Integration CategoryUpdateServiceTest Test")
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
	private ChildDao childDao;
	@Autowired
	DataSource dataSource;
	@Autowired
	Registry registry;
	@Autowired
	@Container
	public MySQLContainer mysqlContainer;
	private final Integer ZVIRE = 300112000;
	private final Integer PTACTVO = 300112021;
	private final Category TO_DEACTIVATE = new Category(20, "DeactivationTest", 300112000, true);
	private final Child CHILD_TO_DEACTIVATE = new Child(20, "DeactivationTest", true);

	@Test
	@Order(1)
	@DisplayName("should test deactivation")
	@Transactional
	public void testDeactivation() {
		saveCompleteTreeOfCategoryAndTestIfDone(ZVIRE);
		createCategoryAndChildForDeactivationAndSaveThem(TO_DEACTIVATE, CHILD_TO_DEACTIVATE);
		testIfCategoryAndChildForDeactivationWereSaved();
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		updateService.updateCompleteTreeOfMainCategory(ZVIRE);
		testIfCategoryAndChildForDeactivationWereDeactivated(zvire);
	}

	@Test
	@Order(2)
	@DisplayName("should test adding new category")
	@Transactional
	public void addingNewCategory() {
		logger.info("adding new category test started");
		registry.refreshCategoryRegistry();
		// saveService.saveCompleteTreeOfMainCategory(ZVIRE);
		saveCompleteTreeOfCategoryAndTestIfDone(ZVIRE);
		Category ptactvo = categoryDao.findByCategoryIdWithChildren(PTACTVO);
		categoryDao.remove(ptactvo);
		assertTrue(categoryDao.findByCategoryIdWithChildren(PTACTVO) == null);
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		Child toRemove = zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(300112021))
				.findFirst().get();
		if (zvire != null)
			zvire.removeChild(toRemove);
		categoryDao.save(zvire);
		Category krmiva = categoryDao.findByCategoryIdWithChildren(300112022);
		logger.info("krmiva {}", krmiva);
		zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		assertEquals(Optional.empty(),
				zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(300112021)).findFirst());
		updateService.updateCompleteTreeOfMainCategory(ZVIRE);
		ptactvo = categoryDao.findByCategoryIdWithChildren(300112021);
		assertTrue(ptactvo != null);
		assertThat(ptactvo, hasProperty("categoryName", equalTo("Ptactvo")));
		assertThat(ptactvo, hasProperty("categoryId", equalTo(300112021)));
		assertThat(ptactvo, hasProperty("parentId", equalTo(ZVIRE)));
		assertThat(ptactvo, hasProperty("active", equalTo(true)));
		assertThat(ptactvo.getChildren(), hasSize(1));
		assertTrue(childDao.findByCategoryId(300112022).isPresent());
		zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		krmiva = categoryDao.findByCategoryIdWithChildren(300112022);
		logger.info("krmiva {}", krmiva);
		Optional<Child> restored = zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(300112021))
				.findFirst();
		assertTrue(restored.isPresent());
		assertThat(restored.get(), hasProperty("categoryName", equalTo("Ptactvo")));
		assertThat(restored.get(), hasProperty("categoryId", equalTo(300112021)));
		assertThat(restored.get(), hasProperty("active", equalTo(true)));
		logger.info("adding new category test finished");
	}

	public void saveCompleteTreeOfCategoryAndTestIfDone(Integer categoryId) {
		assertEquals(0, registry.getCategoryRecords().size());
		saveService.saveCompleteTreeOfMainCategory(ZVIRE);
		assertEquals(categoryDao.findAll().size(), registry.getCategoryRecords().size());
	}

	public void createCategoryAndChildForDeactivationAndSaveThem(Category category, Child child) {
		categoryDao.save(category);
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		if (zvire != null) {
			zvire.addChild(child);
			categoryDao.save(zvire);
		}
	}

	public void testIfCategoryAndChildForDeactivationWereSaved() {
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE);
		Optional<Child> persisted = zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(20))
				.findFirst();
		Category toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(20);
		assertEquals(true, persisted.isPresent());
		assertEquals(Optional.of(true), persisted.map(Child::getActive));
		assertEquals(true, Objects.nonNull(toDeactivatePersisted));
	}

	public void testIfCategoryAndChildForDeactivationWereDeactivated(Category zvire) {
		Optional<Child> persisted = zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(20))
				.findFirst();
		Category toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(20);
		assertEquals(true, persisted.isPresent());
		assertEquals(Optional.of(false), persisted.map(Child::getActive));
		assertEquals(false, toDeactivatePersisted.getActive());
	}
}
