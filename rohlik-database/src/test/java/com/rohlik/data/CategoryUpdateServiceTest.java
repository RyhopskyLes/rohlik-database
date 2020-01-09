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
	private final static Integer ZVIRE_CATEGORY_ID = 300112000;
	private final static Integer PTACTVO_CATEGORY_ID = 300112021;
	private final static String PTACTVO_CATEGORY_NAME = "Ptactvo";
	private final static Integer KRMIVA_CATEGORY_ID = 300112022;
	private final static Category TO_DEACTIVATE = new Category(20, "DeactivationTest", ZVIRE_CATEGORY_ID, true);
	private final static Integer DEACTIVATION_CATEGORY_ID = TO_DEACTIVATE.getCategoryId();
	private final static String DEACTIVATION_CATEGORY_NAME = TO_DEACTIVATE.getCategoryName();
	private final static Child CHILD_TO_DEACTIVATE = new Child(DEACTIVATION_CATEGORY_ID, DEACTIVATION_CATEGORY_NAME,
			true);

	@Test
	@Order(1)
	@DisplayName("should test deactivation")
	@Transactional
	public void testDeactivation() {
		saveCompleteTreeOfCategoryAndTestIfDone(ZVIRE_CATEGORY_ID);
		createCategoryAndChildForDeactivationAndSaveThem(TO_DEACTIVATE, CHILD_TO_DEACTIVATE);
		testIfCategoryAndChildForDeactivationWereSaved();
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE_CATEGORY_ID);
		updateService.updateCompleteTreeOfMainCategory(ZVIRE_CATEGORY_ID);
		testIfCategoryAndChildForDeactivationWereDeactivated(zvire);
		logger.info("deactivating category test finished");
	}

	@Test
	@Order(2)
	@DisplayName("should test adding new category")
	@Transactional
	public void addingNewCategory() {
		logger.info("adding new category test started");
		registry.refreshCategoryRegistry();
		saveCompleteTreeOfCategoryAndTestIfDone(ZVIRE_CATEGORY_ID);
		removeCategoryAndTestIfDone(PTACTVO_CATEGORY_ID);
		removeRemovedCategoryAsChildFromParentAndTestifDone(ZVIRE_CATEGORY_ID, PTACTVO_CATEGORY_ID);
		updateService.updateCompleteTreeOfMainCategory(ZVIRE_CATEGORY_ID);
		testIfRemovedCategoryWasAddedAfterUpdate(PTACTVO_CATEGORY_ID, PTACTVO_CATEGORY_NAME, ZVIRE_CATEGORY_ID,
				KRMIVA_CATEGORY_ID);
		testIfRemovedCategoryWasAddedAsChildAfterUpdate(ZVIRE_CATEGORY_ID, PTACTVO_CATEGORY_ID, PTACTVO_CATEGORY_NAME);
		logger.info("adding new category test finished");
	}

	public void saveCompleteTreeOfCategoryAndTestIfDone(Integer categoryId) {
		assertEquals(0, registry.getCategoryRecords().size());
		saveService.saveCompleteTreeOfMainCategory(ZVIRE_CATEGORY_ID);
		assertEquals(categoryDao.findAll().size(), registry.getCategoryRecords().size());
	}

	public void createCategoryAndChildForDeactivationAndSaveThem(Category category, Child child) {
		categoryDao.save(category);
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE_CATEGORY_ID);
		if (zvire != null) {
			zvire.addChild(child);
			categoryDao.save(zvire);
		}
	}

	public void testIfCategoryAndChildForDeactivationWereSaved() {
		Category zvire = categoryDao.findByCategoryIdWithChildren(ZVIRE_CATEGORY_ID);
		Optional<Child> persisted = zvire.getChildren().stream()
				.filter(child -> child.getCategoryId().equals(DEACTIVATION_CATEGORY_ID)).findFirst();
		Category toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(DEACTIVATION_CATEGORY_ID);
		assertEquals(true, persisted.isPresent());
		assertEquals(Optional.of(true), persisted.map(Child::getActive));
		assertEquals(true, Objects.nonNull(toDeactivatePersisted));
	}

	public void testIfCategoryAndChildForDeactivationWereDeactivated(Category zvire) {
		Optional<Child> persisted = zvire.getChildren().stream()
				.filter(child -> child.getCategoryId().equals(DEACTIVATION_CATEGORY_ID)).findFirst();
		Category toDeactivatePersisted = categoryDao.findByCategoryIdWithChildren(DEACTIVATION_CATEGORY_ID);
		assertEquals(true, persisted.isPresent());
		assertEquals(Optional.of(false), persisted.map(Child::getActive));
		assertEquals(false, toDeactivatePersisted.getActive());
	}

	public void removeCategoryAndTestIfDone(Integer categoryId) {
		Category ptactvo = categoryDao.findByCategoryIdWithChildren(categoryId);
		categoryDao.remove(ptactvo);
		assertTrue(categoryDao.findByCategoryIdWithChildren(categoryId) == null);

	}

	public void removeRemovedCategoryAsChildFromParentAndTestifDone(Integer parentId, Integer removedCategoryId) {
		Category zvire = categoryDao.findByCategoryIdWithChildren(parentId);
		Child toRemove = zvire.getChildren().stream().filter(child -> child.getCategoryId().equals(removedCategoryId))
				.findFirst().get();
		if (zvire != null)
			zvire.removeChild(toRemove);
		categoryDao.save(zvire);
		zvire = categoryDao.findByCategoryIdWithChildren(parentId);
		assertEquals(Optional.empty(), zvire.getChildren().stream()
				.filter(child -> child.getCategoryId().equals(removedCategoryId)).findFirst());
	}

	public void testIfRemovedCategoryWasAddedAfterUpdate(Integer addedCategoryId, String addedCategoryname,
			Integer addedCategoryParentId, Integer addedCategoryChildCategoryId) {
		Category ptactvo = categoryDao.findByCategoryIdWithChildren(addedCategoryId);
		assertTrue(ptactvo != null);
		assertThat(ptactvo, hasProperty("categoryName", equalTo(addedCategoryname)));
		assertThat(ptactvo, hasProperty("categoryId", equalTo(addedCategoryId)));
		assertThat(ptactvo, hasProperty("parentId", equalTo(addedCategoryParentId)));
		assertThat(ptactvo, hasProperty("active", equalTo(true)));
		assertThat(ptactvo.getChildren(), hasSize(1));
		assertTrue(childDao.findByCategoryId(addedCategoryChildCategoryId).isPresent());

	}

	public void testIfRemovedCategoryWasAddedAsChildAfterUpdate(Integer parentCategoryId, Integer childCategoryId,
			String childCategoryName) {
		Category zvire = categoryDao.findByCategoryIdWithChildren(parentCategoryId);
		Optional<Child> restored = zvire.getChildren().stream()
				.filter(child -> child.getCategoryId().equals(childCategoryId)).findFirst();
		assertTrue(restored.isPresent());
		assertThat(restored.get(), hasProperty("categoryName", equalTo(childCategoryName)));
		assertThat(restored.get(), hasProperty("categoryId", equalTo(childCategoryId)));
		assertThat(restored.get(), hasProperty("active", equalTo(true)));

	}
}
