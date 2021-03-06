package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import com.rohlik.data.config.AppEmptyDBConfig;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.config.EmptyDBConfig;
import com.rohlik.data.entities.Category;
import com.rohlik.data.objects.NavSections;
import com.rohlik.data.objects.NavSectionsCategoryData;
import com.rohlik.data.objects.Navigation;
import com.rohlik.data.objects.NavigationCategoryInfo;

@SpringJUnitConfig(classes = {AppEmptyDBConfig.class, EmptyDBConfig.class})
@DisplayName("Unit CategoryBuildService Test")
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("emptyDB")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryBuildServiceTest {
private static Logger logger = LoggerFactory.getLogger(CategoryBuildServiceTest.class);		
@Autowired	
private CategoryBuildService buildService;
@Autowired	
private Navigation navigation;
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


private  List<NavigationCategoryInfo> allCategoriesInfo;
private Optional<NavigationCategoryInfo> pekarnaInfo;
private  List<NavigationCategoryInfo> allMainCategoriesInfo;
private Optional<NavigationCategoryInfo> chipsyInfo;
@BeforeAll
public void setup() {
	allCategoriesInfo = navigation.getAllCategoriesData();
	pekarnaInfo =allCategoriesInfo.stream()
			.filter(category -> Objects.equals(category.getCategoryId(), PEKARNA)).findFirst();
	allMainCategoriesInfo=allCategoriesInfo.stream().filter(cat -> cat.getParentId() != null)
			.filter(cat -> cat.getParentId().equals(0)).collect(Collectors.toCollection(ArrayList::new));
	chipsyInfo = allCategoriesInfo.stream()
			.filter(category -> Objects.equals(category.getCategoryId(), CHIPSY_A_BRAMBURKY)).findFirst();
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

@Test
@Order(12) 
@DisplayName("should build category 300112000")
public void buildCompleteTreeOfMainCategoryZvire() {
	Map<Integer, Set<Category>> zvire = buildService.buildCompleteTreeOfMainCategory(ZVIRE);
	zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
	zvire.remove(0);
	Set<NavSectionsCategoryData> categories = navSections.completeTreeOfCategory(ZVIRE);
	Set<Integer> names =categories.stream().map(NavSectionsCategoryData::getCategoryId).collect(Collectors.toCollection(HashSet::new));
	Set<Integer> namesTree = zvire.values().stream().flatMap(Set::stream).map(Category::getCategoryId).collect(Collectors.toCollection(HashSet::new));
namesTree.removeAll(names);

int count =zvire.values().stream().map(Set::size).reduce(0, Integer::sum);
	assertEquals(categories.size()+namesTree.size(), count);
}

@Test
@Order(13) 
@DisplayName("should build category 300112000 to level 2")
public void buildCompleteTreeOfMainCategoryZvireToLevel2() {
	Map<Integer, Set<Category>> zvire = buildService.buildCompleteTreeOfMainCategoryDownToLevel(ZVIRE, 2);
	zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
	assertThat(zvire.keySet(), hasSize(3));
	assertThat(zvire.keySet(), hasItems(0, 1, 2));	
}

@Test
@Order(14) 
@DisplayName("should build category 300112000 from level 2 to level 3")
public void buildCompleteTreeOfMainCategoryZvireFromLevel2ToLevel3() {
	Map<Integer, Set<Category>> zvire = buildService.buildCompleteTreeOfMainCategoryFromLevelToLevel(ZVIRE, 2, 3);
	zvire.forEach((k, v)->System.out.println(k+"\t"+ v));
	assertThat(zvire.keySet(), hasSize(2));
	assertThat(zvire.keySet(), hasItems(2, 3));	
}

@Test
@Order(15) 
@DisplayName("should build level 1 of category 300112000")
public void buildLevel1ofCategoryZvire() {
	 Set<Category> zvire = buildService.buildLevelFromCompleteTreeOfMainCategory(ZVIRE, 1);
	zvire.forEach(System.out::println);
	assertThat(zvire, hasSize(4));
	
}

@Test
@Order(16) 
@DisplayName("should build lowest levels of category 300112000")
public void buildLowestLevelOfEachBranchOfCategoryZvire() {
	Map<Integer, Set<Category>> zvire = buildService.buildLowestLevelOfEachBranchOfMainCategoryTree(ZVIRE);
	zvire.forEach((k, v)->System.out.println(k+"\t"+v));
	assertThat(zvire.get(2), hasItems(
			Matchers.<Category>hasProperty("categoryId", equalTo(300112011)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112014)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112013)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112017)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112004)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112003)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112875)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112009)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112008)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300115115)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112019)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112020)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112022)),
			Matchers.<Category>hasProperty("categoryId", equalTo(300112017))
			));		
}
@Test
@Order(17) 
@DisplayName("should build parent chain of category 300114931")
public void buildParentChainOfCategoryLUSTENINOVE_A_RYZOVE() { 
	Category zero = buildService.buildCategory(LUSTENINOVE_A_RYZOVE).orElseGet(Category::new);
	Map<Integer, Category> parentChain =buildService.buildParentChainOfCategory(LUSTENINOVE_A_RYZOVE);
assertThat(parentChain.entrySet(), hasSize(3));
Category one =parentChain.get(1);
Category two =parentChain.get(2);
Category three=parentChain.get(3);
assertThat(one, allOf(Matchers.<Category>hasProperty("categoryId", equalTo(zero.getParentId())),
		Matchers.<Category>hasProperty("parentId", equalTo(two.getCategoryId()))
		));
assertThat(two, allOf(Matchers.<Category>hasProperty("categoryId", equalTo(one.getParentId())),
		Matchers.<Category>hasProperty("parentId", equalTo(three.getCategoryId()))
		));
assertThat(three, allOf(Matchers.<Category>hasProperty("categoryId", equalTo(two.getParentId())),
		Matchers.<Category>hasProperty("parentId", equalTo(0))
		));
}

}
