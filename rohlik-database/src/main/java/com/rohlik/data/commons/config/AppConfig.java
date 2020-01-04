package com.rohlik.data.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.rohlik.data.commons.exceptions.WrongCategoryIdException;
import com.rohlik.data.commons.services.build.CategoryBuildService;
import com.rohlik.data.entities.Category;
import com.rohlik.data.objects.CategorySnapshot;
import com.rohlik.data.objects.ProductsInCategory;

/**
 * Created by iuliana.cosmina on 4/29/17.
 */
@Configuration
@Profile("production")
@ComponentScan(basePackages = {"com.rohlik.data"})
public class AppConfig {
	@Autowired
	private ProductsInCategory productsInCategory;
	@Autowired
	private CategoryBuildService buildService;
	@Bean
	   @Scope(value = "prototype")
	   public CategorySnapshot snapshot(Category category) {
		CategorySnapshot snapshot;
	       try {
			snapshot = new CategorySnapshot(productsInCategory, buildService, category);
		} catch (WrongCategoryIdException e) {
			snapshot = new CategorySnapshot();
			e.printStackTrace();
		}
	       return snapshot;}
	
}

