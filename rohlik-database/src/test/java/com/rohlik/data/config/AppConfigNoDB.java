package com.rohlik.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import com.rohlik.data.commons.dao.CategoryDao;
import com.rohlik.data.commons.dao.CategoryKosikDao;
import com.rohlik.data.commons.dao.ChildKosikDao;
import com.rohlik.data.commons.dao.ProductDao;
import com.rohlik.data.commons.dao.ProductKosikDao;

import org.springframework.context.annotation.FilterType;

@Configuration
@Profile("noDB")
@ComponentScan(basePackages = {"com.rohlik.data.entities", "com.rohlik.data.commons.utilities", "com.rohlik.data.commons.objects",
		"com.rohlik.data.kosik.objects", "com.rohlik.data.kosik.entities", "com.rohlik.data.objects"}
)
		
public class AppConfigNoDB {

}
