package com.rohlik.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("noDB")
@ComponentScan(basePackages = {"com.rohlik.data.entities", "com.rohlik.data.commons.utilities", "com.rohlik.data.commons.objects",
		"com.rohlik.data.kosik.objects", "com.rohlik.data.kosik.entities", "com.rohlik.data.objects"}
)
		
public class AppConfigNoDB {

}
