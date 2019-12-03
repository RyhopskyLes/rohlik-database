package com.rohlik.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

import org.hamcrest.Matchers;
import org.hamcrest.beans.HasPropertyWithValue;
import org.hamcrest.core.Every;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsNot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.commons.services.CategoryKosikService;
import com.rohlik.data.commons.services.CategoryService;
import com.rohlik.data.config.AppConfigTest;
import com.rohlik.data.config.DataTestConfig;
import com.rohlik.data.kosik.entities.CategoryKosik;
import com.rohlik.data.kosik.entities.ChildKosik;
import com.rohlik.data.kosik.objects.NavigationSubItem;


@SpringJUnitConfig(classes = {AppConfigTest.class, DataTestConfig.class})
@DisplayName("Integration CategoryKosikService Test")
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryKosikServiceTest {
	@Autowired
	CategoryKosikService catKosikService;
	@Autowired
	CategoryKosikDao catKosikDao;
	@Autowired
	CategoryService catService;
	private static Logger logger = LoggerFactory.getLogger(CategoryKosikServiceTest.class);
	private static final String BASIC_URL = "https://www.kosik.cz";
	public static String categoryURI = "/napoje";
			
	@Test
	@Order(1) 
	@DisplayName("should save 14 categories")
	
	public void saveMainCategoryWithChildren() {
		catKosikService.saveMainCategoryWithChildren(categoryURI);
		List<CategoryKosik> result = catKosikDao.findAllWithChildren();
		result.forEach(kosik->logger.info("Saved categories: "+kosik));
		CategoryKosik category = result.get(0);
		Set<ChildKosik> children = category.getChildren();
		assertNotNull(result);
		 assertThat(category.getCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getUri(), is(equalTo("/napoje")));
		 assertThat(category.getEquiCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getEquiParentId(), is(equalTo(0)));
		 assertThat(category.getParentUri(), is(equalTo("")));
		 assertThat(category.getEquiId(), is(equalTo(300108000)));
		 assertThat(children, hasItem(Matchers.<ChildKosik>hasProperty("parentUri", equalTo("/napoje"))));
		 assertThat(children, hasItem(Matchers.<ChildKosik>hasProperty("uri", equalTo("/napoje/piva"))));
		assertEquals(1, result.size());
		assertEquals(14, children.size());
		category.set(category::setParentUri, null);
		category.getChildren().forEach(child-> child.setParentUri(null));
		category = catKosikDao.save(category);
		 assertThat(category.getParentUri(), is(equalTo(null)));
	}
	@Test
	@Order(2) 
	@DisplayName("should update parentUri")
	
	public void updateMainCategoryParentUri() {
		List<CategoryKosik> result = catKosikDao.findAllWithChildren();
		CategoryKosik category = result.get(0);
		Set<ChildKosik> children = category.getChildren();
		catKosikService.updateParentUriByMainCategory(categoryURI);		
		result = catKosikDao.findAllWithChildren();
		category = result.get(0);
		children = category.getChildren();
		assertNotNull(result);
		 assertThat(category.getCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getUri(), is(equalTo("/napoje")));
		 assertThat(category.getEquiCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getEquiParentId(), is(equalTo(0)));
		 assertThat(category.getParentUri(), is(equalTo("")));
		 assertThat(category.getEquiId(), is(equalTo(300108000)));
		 assertThat(children, hasItem(Matchers.<ChildKosik>hasProperty("parentUri", equalTo("/napoje"))));
		 assertThat(children, hasItem(Matchers.<ChildKosik>hasProperty("uri", equalTo("/napoje/piva"))));
		assertEquals(1, result.size());
		assertEquals(14, children.size());
	}
	
	@Test
	@Order(3) 
	@DisplayName("should build category Napoje")
	public void buildMainCategoryFromURL( ) {
		CategoryKosik category = catKosikService.buildMainCategoryFromURI(categoryURI);
		assertNotNull(category);
		assertThat(category.getCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getUri(), is(equalTo("/napoje")));
		 assertThat(category.getEquiCategoryName(), is(equalTo("Nápoje")));
		 assertThat(category.getEquiParentId(), is(equalTo(0)));
		 assertThat(category.getParentUri(), is(equalTo("")));
		 assertThat(category.getEquiId(), is(equalTo(300108000)));
	}
	
	@Test
	@Order(4) 
	@DisplayName("should build 15 categories")
	public void  buildSecondLevelCategoriesWithChildrenFromURI() {
		List<CategoryKosik> categories =catKosikService.buildSecondLevelCategoriesWithChildrenFromURI(categoryURI);
		List<String> names = categories.stream().map(category->category.getCategoryName()).collect(Collectors.toList());
		assertNotNull(categories);
		CategoryKosik category = categories.stream().filter(cat->cat.getCategoryName().equals("Vody")).findFirst().get();
		category.getChildren().forEach(child->assertThat(child.getParentUri(), is(equalTo("/napoje/vody"))));
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Vína"))));
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Piva"))));
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("parentUri", equalTo("/napoje"))));
		assertEquals(14, categories.size());
		
	}
	@Test
	@Order(5) 
	@DisplayName("should find 15 categories")
	public void  saveUnsavedSecondLevelCategoriesWithChildrenFromURI() {		
		List<CategoryKosik> results = catKosikDao.findAllWithChildren();
		results.forEach(result->logger.info("Result "+result));
		assertNotNull(results);
		catKosikService.saveUnsavedSecondLevelCategoriesWithChildrenBuiltFromURI(categoryURI);
		List<CategoryKosik> categories = catKosikDao.findAllWithChildren();
	//	List<String> names = categories.stream().map(category->category.getCategoryName()).collect(Collectors.toList());
		assertNotNull(categories);
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Vína"))));
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Piva"))));
		assertEquals(15, categories.size());
		
	}
	@Test
	@Order(6) 
	@DisplayName("should update parentUri by 14 categories")
	public void updateParentUriBySecondLevelCategories() {
		List<CategoryKosik> result = catKosikDao.findByParentNameWithChildren("Nápoje");
		result.forEach(category->{category.setParentUri(null); catKosikDao.save(category);});
		result.forEach(category->{
			category.getChildren().forEach(child->child.setParentUri(null)); 
		catKosikDao.save(category);
		});
		result = catKosikDao.findByParentNameWithChildren("Nápoje");
		result.forEach(category->assertEquals(null, category.getParentUri()));
		result.forEach(category->category.getChildren().forEach(child->assertEquals(null, child.getParentUri())));		
		catKosikService.updateParentUriBySecondLevelCategories(categoryURI);
		result = catKosikDao.findByParentNameWithChildren("Nápoje");
		result.forEach(category->assertEquals("/napoje", category.getParentUri()));		
	}
	
	@Test
	@Order(7) 
	@DisplayName("should update parentUri by children")
	public void updateParentUriByChildren() {
		List<CategoryKosik> result = catKosikDao.findByParentNameWithChildren("Nápoje");
		result.forEach(category->{
			category.getChildren().forEach(child->assertEquals(null, child.getParentUri())); 
		});
	//	result.forEach(category->catKosikService.updateUriByChildrenOfCategory(category.getUri()));
		result.forEach(category->catKosikService.updateParentUriByChildrenOfCategory(category.getUri()));
		//catKosikService.updateUriByChildrenOfCategory(categoryURI);
		result = catKosikDao.findByParentNameWithChildren("Nápoje");
		//result.forEach(category->category.getChildren().forEach(child->logger.info("Child: "+child)));
		result.forEach(category->catKosikService.updateUriByChildrenOfCategory(category.getUri()));
		result.forEach(category->category.getChildren().forEach(child->assertEquals(category.getUri(), child.getParentUri())));
	}
	@Test
	@Order(8) 
	@DisplayName("should find 15 categories")
	public void  saveNoUnsavedSecondLevelCategoriesWithChildrenFromURI() {
		catKosikService.saveUnsavedSecondLevelCategoriesWithChildrenBuiltFromURI(categoryURI);
		List<CategoryKosik> categories = catKosikDao.findAllWithChildren();
		//List<String> names = categories.stream().map(category->category.getCategoryName()).collect(Collectors.toList());
		assertNotNull(categories);
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Vína"))));
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("categoryName", equalTo("Piva"))));
		assertEquals(15, categories.size());
		
	}
	
	@Test
	@Order(9) 
	@DisplayName("should save category Mazlicci")
	public void  saveMainCategoryWithChildrenFromURI() {
		String uri = "/mazlicci";
		catKosikService.saveMainCategoryWithChildren(uri);
		CategoryKosik category = catKosikDao.findByUriWithChildren(uri).get();
		String name = category.getCategoryName();
	//	logger.info("Category mazlicci: "+category);
	//	logger.info("Category mazlicci children: "+category.getChildren());
		assertNotNull(category);
		assertThat(name, is(equalTo("Mazlíčci")));
		assertEquals(null, category.getEquiId());
		
	}
	
	@Test
	@Order(10) 
	@DisplayName("should update uri by 16 categories")
	public void  updateUriBySecondLevelCategories() {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		String parentName = mainParent.getCategoryName();
		List<CategoryKosik> categories = catKosikDao.findByParentName(parentName);
		categories.forEach(category->{category.setUri(""); catKosikDao.save(category);});
		categories = catKosikDao.findByParentName(parentName);
		assertFalse(categories.isEmpty());
		assertThat(categories, (Every.everyItem(HasPropertyWithValue.hasProperty("uri", Is.is("")))));	
		catKosikService.updateUriBySecondLevelCategories(categoryURI);
		categories = catKosikDao.findByParentName(parentName);
		assertFalse(categories.isEmpty());
		assertThat(categories, hasItem(Matchers.<CategoryKosik>hasProperty("uri", equalTo("/napoje/vina"))));
		assertThat(
				  categories,
				  hasItem(allOf(
				    Matchers.<CategoryKosik>hasProperty("uri", is("/napoje/caje")),
				    Matchers.<CategoryKosik>hasProperty("equiId", is(300108007))
				  ))
				);
		assertThat(categories, (Every.everyItem(HasPropertyWithValue.hasProperty("uri", IsNot.not("")))));
	}
	
	@Test
	@Order(11) 
	@DisplayName("should build 3 children")
	public void  buildMissingChildren() {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		Set<ChildKosik> children = mainParent.getChildren();
		ChildKosik poctivePiti = children.stream().filter(child->child.getCategoryName().equals("Poctivé pití"))
				.findFirst().get();
		ChildKosik vody = children.stream().filter(child->child.getCategoryName().equals("Vody"))
				.findFirst().get();
		ChildKosik cidery = children.stream().filter(child->child.getCategoryName().equals("Cidery"))
				.findFirst().get();
		assertEquals(14, children.size());
		mainParent.removeChildKosik(cidery);
		mainParent.removeChildKosik(vody);
		mainParent.removeChildKosik(poctivePiti);
		catKosikDao.save(mainParent);
		mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		children = mainParent.getChildren();
		assertEquals(11, children.size());
		List<ChildKosik> missing = catKosikService.buildMissingChildrenOfCategory(mainParent.getUri());
		assertEquals(3, missing.size());
		assertThat(missing, hasItems(
				hasProperty("categoryName", is("Cidery")),
                hasProperty("uri", is("/napoje/cidery")),
                hasProperty("equiCategoryName", is("Cidery")),
                hasProperty("categoryName", is("Vody")),
                hasProperty("uri", is("/napoje/vody")),
                hasProperty("equiCategoryName", is(nullValue())),
                hasProperty("categoryName", is("Poctivé pití")),
                hasProperty("uri", is("/napoje/poctive-piti"))               
				));
	}
	
	@Test
	@Order(12) 
	@DisplayName("should find 3 subitems")
	public void  findMissingChildrenToPatrent() {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		Set<ChildKosik> children = mainParent.getChildren();
		children.forEach(System.out::println);
		assertEquals(11, children.size());
		List<NavigationSubItem> missing =catKosikService.findMissingChildrenOfCategory(mainParent.getUri());
		missing.forEach(System.out::println);
		assertEquals(3, missing.size());
		assertThat(missing, hasItems(
				hasProperty("categoryName", is("Cidery")),
                hasProperty("uri", is("/napoje/cidery")),
                hasProperty("parentUri", is("/napoje")),
                hasProperty("categoryName", is("Vody")),
                hasProperty("uri", is("/napoje/vody")),
                hasProperty("parentUri", is("/napoje")),
                hasProperty("categoryName", is("Poctivé pití")),
                hasProperty("uri", is("/napoje/poctive-piti"))               
				));
	}

	
	@Test
	@Order(13) 
	@DisplayName("should add 3 children")
	public void  adddMissingChildrenToPatrent() {
		CategoryKosik mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		Set<ChildKosik> children = mainParent.getChildren();
		assertEquals(11, children.size());
		catKosikService.addMissingChildrenOfCategoryToParent(mainParent.getUri());
		mainParent = catKosikDao.findByUriWithChildren(categoryURI).get();
		children = mainParent.getChildren();
		assertEquals(14, children.size());
		assertThat(children, hasItems(
				hasProperty("categoryName", is("Cidery")),
                hasProperty("uri", is("/napoje/cidery")),
                hasProperty("equiCategoryName", is("Cidery")),
                hasProperty("categoryName", is("Vody")),
                hasProperty("uri", is("/napoje/vody")),
                hasProperty("equiCategoryName", is(nullValue())),
                hasProperty("categoryName", is("Poctivé pití")),
                hasProperty("uri", is("/napoje/poctive-piti"))               
				));
	}
}
