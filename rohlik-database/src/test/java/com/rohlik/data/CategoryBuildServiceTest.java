package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
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

import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.config.AppConfigTest;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.NavigationCategoryInfo;

@SpringJUnitConfig(classes = {AppConfigTest.class, DataTestConfig.class})
@DisplayName("Unit CategoryBuildService Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryBuildServiceTest {
private static Logger logger = LoggerFactory.getLogger(CategoryBuildServiceTest.class);		
@Autowired	
private CategoryBuildService buildService;
@Autowired	
private Navigation navigation;
private final Integer PEKARNA=300101000;
private  List<NavigationCategoryInfo> allCategoriesInfo;
private Optional<NavigationCategoryInfo> pekarnaInfo;
private  List<NavigationCategoryInfo> allMainCategoriesInfo;
@BeforeAll
public void setup() {
	allCategoriesInfo = navigation.getAllCategoriesData();
	pekarnaInfo =allCategoriesInfo.stream()
			.filter(category -> Objects.equals(category.getId(), PEKARNA)).findFirst();
	allMainCategoriesInfo=allCategoriesInfo.stream().filter(cat -> cat.getParentId() != null)
			.filter(cat -> cat.getParentId().equals(0)).collect(Collectors.toCollection(ArrayList::new));
}
@Test
@Order(1) 
@DisplayName("should build category 300101000")
public void buildMainCategory() {
	Optional<Category> pekarna = buildService.buildMainCategory(PEKARNA);
	assertEquals(true, pekarna.isPresent());
	assertEquals(Optional.ofNullable("Pekárna a cukrárna"), pekarna.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(300101000), pekarna.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(0), pekarna.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), pekarna.map(Category::getActive));	
}

@Test
@Order(2) 
@DisplayName("should build category 300101000 with children")
public void buildMainCategoryWithChildren() {
	Optional<Integer> sizeExpected = pekarnaInfo.map(NavigationCategoryInfo::getChildren).map(List::size);
	Optional<Category> pekarna = buildService.buildMainCategoryWithChildren(PEKARNA);
	Optional<Integer> sizeActual = pekarna.map(Category::getChildren).map(Set::size);
	assertEquals(true, pekarna.isPresent());
	assertEquals(Optional.ofNullable("Pekárna a cukrárna"), pekarna.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(300101000), pekarna.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(0), pekarna.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), pekarna.map(Category::getActive));
	assertEquals(sizeExpected, sizeActual);	
}

@Test
@Order(3) 
@DisplayName("should build all main categories")
public void buildAllMainCategories() {
	List<Category> all = buildService.buildAllMainCategories();
	assertEquals(allMainCategoriesInfo.size(), all.size());
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
			Matchers.<Category>hasProperty("categoryId", equalTo(300112985))			
			));	
}
}
