package com.rohlik.data.commons.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Profile("production")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.rohlik.data"}
        )
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:jdbc.properties")
public class DataConfig {
	private static Logger logger = LoggerFactory.getLogger(DataConfig.class);
	@Value("${driverClassName}")
	private String driverClassName;
	@Value("${url}")
	private String url;
	@Value("${user}")
	private String username;
	@Value("${password}")
	private String password;
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public DataSource dataSource() {
		try {	
			BasicDataSource dataSource = new BasicDataSource();
			dataSource.setDriverClassName(driverClassName);
			dataSource.setUrl(url);
			dataSource.setUsername(username);
			dataSource.setPassword(password);
			logger.info("MySql DataSource created!");
			return dataSource;
		} catch (Exception e) {
			logger.error("DBCP DataSource bean cannot be created!", e);
			return null;
		}
	}
	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		hibernateProp.put("hibernate.format_sql", true);
		hibernateProp.put("hibernate.use_sql_comments", true);
		hibernateProp.put("hibernate.show_sql", true);
		hibernateProp.put("hibernate.max_fetch_depth", 4);
		hibernateProp.put("hibernate.jdbc.batch_size", 10);
		hibernateProp.put("hibernate.jdbc.fetch_size", 50);
		return hibernateProp;
	}
	@Bean
	public PlatformTransactionManager transactionManager() {
		return new JpaTransactionManager(entityManagerFactory());
	}

	
	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		return new HibernateJpaVendorAdapter();
	}
		
	

	@Bean
	public EntityManagerFactory entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
		factoryBean.setPackagesToScan("com.rohlik.data.entities", "com.rohlik.data.kosik.entities");
		factoryBean.setDataSource(dataSource());
		factoryBean.setJpaProperties(hibernateProperties());
		factoryBean.setJpaVendorAdapter(jpaVendorAdapter());
		factoryBean.afterPropertiesSet();
		return factoryBean.getNativeEntityManagerFactory();
	}
}
