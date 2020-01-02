package com.rohlik.data.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("testContainer")

@ComponentScan(basePackages = {"com.rohlik.data"})
public class AppConfigTestContainer {

}
