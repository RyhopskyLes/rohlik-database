package com.rohlik.data;

import java.util.Optional;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
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
import com.rohlik.data.commons.objects.Registry;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.config.AppConfigTestContainer;
import com.rohlik.data.config.TestContainerConfig;
import com.rohlik.data.entities.Category;

@SpringJUnitConfig(classes = { AppConfigTestContainer.class, TestContainerConfig.class })
@DisplayName("Unit Registry Class Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("testContainer")
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@PropertySource("classpath:application-test_container.properties")
public class RegistryTest {
	private static Logger logger = LoggerFactory.getLogger(RegistryTest.class);
	@Autowired
	private CategoryDao categoryDao;
	@Autowired
	private CategoryBuildService buildService;
	@Autowired
	private Registry registry;
	@Autowired
	@Container
	public MySQLContainer mysqlContainer;
	private final Integer ZVIRE = 300112000;
	private final Integer PEKARNA = 300101000;

	@Test
	@Order(1)
	@DisplayName("should test concurrent adding")
	@Transactional
	public void concurrentCategoryAdding() {

		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(10);
			assertSize(0);
			for (int i = 0; i < 10; i++) {
				Future<Category> result = service.submit(() -> {
					Optional<Category> zvire = buildService.buildMainCategory(ZVIRE);
					return categoryDao.save(zvire.get());
				});
				if (result.isDone()) {
					assertSize(1);
				}
			}
		} finally {
			shutDownService(service);
		}
		finalizeTest(service, 1, 1);
	}

	@Test
	@Order(2)
	@DisplayName("should test concurrent removing")
	@Transactional
	public void concurrentCategoryRemovingById() {
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 10; i++) {
				CompletableFuture.runAsync(() -> categoryDao.removeById(2713), service)
						.thenRunAsync(() -> assertSize(0), service);
			}

		} finally {
			shutDownService(service);
		}
		finalizeTest(service, 2, 0);
	}

	@Test
	@Order(3)
	@DisplayName("should test concurrent removing")
	@Transactional
	public void concurrentCategoryRemoving() {
		Optional<Category> zvire = buildService.buildMainCategory(ZVIRE);
		categoryDao.save(zvire.get());
		Category category = categoryDao.findByCategoryId(ZVIRE).orElseGet(() -> null);
		ExecutorService service = null;
		try {
			service = Executors.newFixedThreadPool(10);
			for (int i = 0; i < 10; i++) {
				CompletableFuture.runAsync(() -> categoryDao.remove(category), service)
						.thenRunAsync(() -> assertSize(0), service);
			}
		} finally {
			shutDownService(service);
		}
		finalizeTest(service, 3, 0);
	}

	public void finalizeTest(ExecutorService service, int number, int size) {
		if (service != null) {
			try {
				service.awaitTermination(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (service.isTerminated()) {
				logger.info("tests n. " + number + " finished");
				assertSize(size);
			}
		}
	}

	public void shutDownService(ExecutorService service) {
		if (service != null)
			service.shutdown();
	}

	public void assertSize(int size) {
		assertThat(registry.getCategoryRecords(), hasSize(size));
	}
}
