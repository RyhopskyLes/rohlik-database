package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

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
private final Integer CHIPSY_A_BRAMBURKY=300106154;
private final Integer VOLNE_PRODEJNE_LEKY=300113171;
private final Integer LEKARNA=300112985;
private final Integer LUSTENINOVE_A_RYZOVE=300114931;
private final Integer SLANE_SNACKY_A_ORECHY =300106153;
private final Integer FRANCOUZSKA=300116325;
private final Integer CERVENA=300108066;
private final Integer BORDEAUX=300116331;


private  List<NavigationCategoryInfo> allCategoriesInfo;
private Optional<NavigationCategoryInfo> pekarnaInfo;
private  List<NavigationCategoryInfo> allMainCategoriesInfo;
private Optional<NavigationCategoryInfo> chipsyInfo;
@BeforeAll
public void setup() {
	allCategoriesInfo = navigation.getAllCategoriesData();
	pekarnaInfo =allCategoriesInfo.stream()
			.filter(category -> Objects.equals(category.getId(), PEKARNA)).findFirst();
	allMainCategoriesInfo=allCategoriesInfo.stream().filter(cat -> cat.getParentId() != null)
			.filter(cat -> cat.getParentId().equals(0)).collect(Collectors.toCollection(ArrayList::new));
	chipsyInfo = allCategoriesInfo.stream()
			.filter(category -> Objects.equals(category.getId(), CHIPSY_A_BRAMBURKY)).findFirst();
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

@Test
@Order(4) 
@DisplayName("should build all main categories")
public void buildAllMainCategoriesWithChildren() {
	List<Category> all = buildService.buildAllMainCategoriesWithChildren();
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
	all.stream().map(Category::getChildren).forEach(set->assertFalse(set.isEmpty()));
	}

@Test
@Order(5) 
@DisplayName("should build category 300106154")
public void buildCategory() {
	Optional<Category> chipsy = buildService.buildCategory(CHIPSY_A_BRAMBURKY);
	assertEquals(true, chipsy.isPresent());
	assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(300106154), chipsy.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(300106153), chipsy.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));	
}

@Test
@Order(6) 
@DisplayName("should build category 300106154")
public void buildCategoryWithChildren() {
	Optional<Integer> sizeExpected = chipsyInfo.map(NavigationCategoryInfo::getChildren).map(List::size);
	Optional<Category> chipsy = buildService.buildCategoryWithChildren(CHIPSY_A_BRAMBURKY);
	Optional<Integer> sizeActual = chipsy.map(Category::getChildren).map(Set::size);
	
	assertEquals(true, chipsy.isPresent());
	assertEquals(Optional.ofNullable("Chipsy a brambůrky"), chipsy.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(CHIPSY_A_BRAMBURKY), chipsy.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(SLANE_SNACKY_A_ORECHY), chipsy.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), chipsy.map(Category::getActive));
	assertEquals(sizeExpected, sizeActual);		
}

@Test
@Order(6) 
@DisplayName("should build category 300113171")
public void buildCategoryLeky() {
	Optional<Category> leky = buildService.buildCategory(VOLNE_PRODEJNE_LEKY);
	assertTrue(leky.isPresent());
	assertEquals(Optional.ofNullable("Volně prodejné léky"), leky.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(VOLNE_PRODEJNE_LEKY), leky.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(LEKARNA), leky.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), leky.map(Category::getActive));	
}

@Test
@Order(7) 
@DisplayName("should build category 300114931")
public void buildCategoryLusteninove() {
	Optional<Category> lusteninove = buildService.buildCategory(LUSTENINOVE_A_RYZOVE);
	assertEquals(true, lusteninove.isPresent());
	assertEquals(Optional.ofNullable("Luštěninové a rýžové"), lusteninove.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(LUSTENINOVE_A_RYZOVE), lusteninove.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(CHIPSY_A_BRAMBURKY), lusteninove.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), lusteninove.map(Category::getActive));	
}

@Test
@Order(8) 
@DisplayName("should build category 300116325")
public void buildCategoryFrancouzskaCervenaVina() {
	Optional<Category> francouzska = buildService.buildCategory(FRANCOUZSKA);
	assertEquals(true, francouzska.isPresent());
	assertEquals(Optional.ofNullable("Francouzská"), francouzska.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(FRANCOUZSKA), francouzska.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(CERVENA), francouzska.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), francouzska.map(Category::getActive));		
}

@Test
@Order(9) 
@DisplayName("should build category 300116331")
public void buildCategoryBordeaux() {
	Optional<Category> bordeaux = buildService.buildCategoryNotContainedInNavigationJson(BORDEAUX);
	assertEquals(true, bordeaux.isPresent());
	assertEquals(Optional.ofNullable("Bordeaux"), bordeaux.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(BORDEAUX), bordeaux.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(FRANCOUZSKA), bordeaux.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), bordeaux.map(Category::getActive));		
}

@Test
@Order(10) 
@DisplayName("should build category 300116331")
public void buildCategoryLekarna() {
	Optional<Category> lekarna = buildService.buildCategoryNotContainedInNavigationJsonWithChildren(LEKARNA);
	assertEquals(true, lekarna.isPresent());
	assertEquals(Optional.ofNullable("Lékárna"), lekarna.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(LEKARNA), lekarna.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(0), lekarna.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), lekarna.map(Category::getActive));	
	lekarna.ifPresent(lek->assertFalse(lek.getChildren().isEmpty()));
}

@Test
@Order(11) 
@DisplayName("should build category 300116325")
public void buildCategoryFrancouzskaCervenaVinaWithChildren() {
	Optional<Category> francouzska = buildService.buildCategoryWithChildren(FRANCOUZSKA);
	assertEquals(true, francouzska.isPresent());
	assertEquals(Optional.ofNullable("Francouzská"), francouzska.map(Category::getCategoryName));
	assertEquals(Optional.ofNullable(FRANCOUZSKA), francouzska.map(Category::getCategoryId));
	assertEquals(Optional.ofNullable(CERVENA), francouzska.map(Category::getParentId));
	assertEquals(Optional.ofNullable(true), francouzska.map(Category::getActive));
	francouzska.ifPresent(franc->assertFalse(franc.getChildren().isEmpty()));
}
}
