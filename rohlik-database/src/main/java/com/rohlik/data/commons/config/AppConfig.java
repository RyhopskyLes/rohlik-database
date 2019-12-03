package com.rohlik.data.commons.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by iuliana.cosmina on 4/29/17.
 */
@Configuration
@Profile("production")
@ComponentScan(basePackages = {"com.rohlik.data"})
public class AppConfig {
	
	
	
}

