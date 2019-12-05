package com.rohlik.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Created by iuliana.cosmina on 4/29/17.
 */
@Configuration
@Profile("test")

@ComponentScan(basePackages = {"com.rohlik.data"})
public class AppConfigTest {
	/*@Autowired
	DataSource dataSource;
	@Autowired
	Properties hibernateProperties;*/
	
	
	
}

