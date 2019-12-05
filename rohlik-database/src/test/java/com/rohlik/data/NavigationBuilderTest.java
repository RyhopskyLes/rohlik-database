package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.config.AppConfigNoDB;
import com.rohlik.data.kosik.interfaces.NavigationBuilder;
import com.rohlik.data.kosik.objects.NavigationItem;
import com.rohlik.data.kosik.objects.NavigationSubItem;


@SpringJUnitConfig(classes = {AppConfigNoDB.class})
@DisplayName("NavigationBuilder Unit Test")
@ActiveProfiles("noDB")
public class NavigationBuilderTest {
	@Autowired
	NavigationBuilder navigationBuilder;
	
	private static Logger logger = LoggerFactory.getLogger(NavigationBuilderTest.class);
	public static String categoryURI = "/napoje/vina/sumiva-a-sampanske";
	public static String napojeURI = "/napoje";
	@Test
	@DisplayName("should build navigationItem")
	public void buildItem() {
		NavigationItem navigationItem =navigationBuilder.buildItem(categoryURI);		
		assertNotNull(navigationItem);
		List<NavigationSubItem> subItems = navigationItem.getSubcategories();
		NavigationSubItem prosecco =subItems.stream().filter(sub->sub.getCategoryName().equals("Prosecco")).findFirst().get();
		subItems.forEach(subItem->logger.info(" "+subItem));
		 assertThat(navigationItem.getCategoryName(), is(equalTo("Šumivá a šampaňské")));
		 assertThat(subItems.size(), is(equalTo(4)));
		 assertThat(prosecco.getCategoryName(), is(equalTo("Prosecco")));
	}
	
	@Test
	@DisplayName("should build navigationLevel")
	public void buildLevel() {
		List<NavigationItem> navigationItems =navigationBuilder.buildLevel(napojeURI);
		NavigationItem vina = navigationItems.stream().filter(item->item.getCategoryName().equals("Vína")).findFirst().get();
		vina.getSubcategories().forEach(sub->logger.info(""+sub));
		assertNotNull(navigationItems);
		navigationItems.forEach(subItem->logger.info(" "+subItem));
		 assertThat(navigationItems.size(), is(equalTo(14)));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("categoryName", equalTo("Vína"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("uri", equalTo("/napoje/vina"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("categoryName", equalTo("Piva"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("uri", equalTo("/napoje/piva"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("categoryName", equalTo("Lihoviny"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("uri", equalTo("/napoje/lihoviny"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("categoryName", equalTo("Džusy a ovocné nápoje"))));
		 assertThat(navigationItems, hasItem(Matchers.<NavigationItem>hasProperty("uri", equalTo("/napoje/dzusy-a-ovocne-napoje"))));
		 assertThat(vina.getSubcategories(), hasItems(				 
	                hasProperty("categoryName", is("Šumivá a šampaňské")),
	                hasProperty("uri", is("/napoje/vina/sumiva-a-sampanske")),
	                hasProperty("parentUri", is("/napoje/vina")),
	                hasProperty("categoryName", is("Červená")),
	                hasProperty("uri", is("/napoje/vina/cervena")),
	                hasProperty("parentUri", is("/napoje/vina")),
	                hasProperty("categoryName", is("Medová")),
	                hasProperty("uri", is("/napoje/vina/medova")),
	                hasProperty("parentUri", is("/napoje/vina")),
	                hasProperty("categoryName", is("Bílá")),
	                hasProperty("uri", is("/napoje/vina/bila")),
	                hasProperty("parentUri", is("/napoje/vina")),
	                hasProperty("categoryName", is("Velká balení (BiB)")),
	                hasProperty("uri", is("/napoje/vina/velka-baleni-bib")),
	                hasProperty("parentUri", is("/napoje/vina")),
	                hasProperty("categoryName", is("Růžová")),
	                hasProperty("uri", is("/napoje/vina/ruzova")),
	                hasProperty("parentUri", is("/napoje/vina"))
	        ));
	}
}
