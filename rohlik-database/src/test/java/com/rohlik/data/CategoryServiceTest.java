package com.rohlik.data;

import java.util.TreeMap;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.services.CategoryService;
import com.rohlik.data.config.AppConfigTest;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.entities.Category;

@SpringJUnitConfig(classes = {AppConfigTest.class, DataTestConfig.class})
@DisplayName("Integration CategoryService Test")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryServiceTest {
private static Logger logger = LoggerFactory.getLogger(CategoryServiceTest.class);	
@Autowired
CategoryDao catDao;
@Autowired
CategoryService catService;

@Test
@Order(1) 
@DisplayName("should find 3 categories")
public void findParentsUpToHighestParent() {
	TreeMap<Integer, Category> parents =catService.findParentsUpToHighestParent(300114179);
	parents.entrySet().forEach(System.out::println);
	assertEquals(parents.size(), 3);
	assertTrue(parents.containsKey(1) && parents.get(1).getCategoryName().equals("Plátkové"));
	assertTrue(parents.containsKey(1) && parents.get(2).getCategoryName().equals("Sýry"));
	assertTrue(parents.containsKey(1) && parents.get(3).getCategoryName().equals("Mléčné a chlazené"));
}

}
