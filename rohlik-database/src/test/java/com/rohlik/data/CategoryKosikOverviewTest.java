package com.rohlik.data;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.rohlik.data.config.AppConfigNoDB;
import com.rohlik.data.kosik.objects.CategoryKosikOverview;

@SpringJUnitConfig(classes = {AppConfigNoDB.class})
@DisplayName("NavigationBuilder Unit Test")
@ActiveProfiles("noDB")
public class CategoryKosikOverviewTest {
	@Autowired
	CategoryKosikOverview overview;
	private static Logger logger = LoggerFactory.getLogger(CategoryKosikOverviewTest.class);
	public static String categoryURI = "/napoje/vina/cervena";
	private static final String BASIC_URL = "https://www.kosik.cz";
	
	@Test
	@DisplayName("should collect all links of /napoje/vina/cervena")
	public void allLinksOfCategory() {
		
		Map<String, Set<String>> allLinks = overview.allLinksOfCategory(BASIC_URL+categoryURI);
		Set<String> values = allLinks.values().stream().flatMap(Set::stream).collect(Collectors.toCollection(HashSet::new));
		System.out.println(allLinks.keySet().size());
		values.forEach(System.out::println);
		 assertThat(allLinks.size(), is(equalTo(16)));
		 assertThat(values.size(), is(equalTo(15)));
		 assertThat(values, hasItems("/napoje/vina/cervena/argentina", "/napoje/vina/cervena/italie/veneto",
				 "/napoje/vina/cervena/italie/abruzzo", "/napoje/vina/cervena/francie/bordeaux",
				 "/napoje/vina/cervena/australie-a-novy-zeland", "/napoje/vina/cervena/francie/languedoc-provence",
				 "/napoje/vina/cervena/chile", "/napoje/vina/cervena/francie",
				 "/napoje/vina/cervena/italie/toskansko", "/napoje/vina/cervena/francie/languedoc-roussilon",
				 "/napoje/vina/cervena/italie", "/napoje/vina/cervena/jar",
				 "/napoje/vina/cervena/italie/sicilia", "/napoje/vina/cervena/francie/cotes-du-rhone",
				 "/napoje/vina/cervena/ostatni")                
	        );
	}
}
