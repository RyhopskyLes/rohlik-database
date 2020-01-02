package com.rohlik.data.config;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import static java.lang.String.format;

import java.beans.PropertyVetoException;
import java.time.Duration;


@Configuration
@ComponentScan(basePackages={"com.rohlik.data"})
@Profile("testContainer")
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = {"com.rohlik.data"}
        )
@EnableAspectJAutoProxy(proxyTargetClass = true)
@PropertySource("classpath:application-test_container.properties")
public class TestContainerConfig {
	@Value("${datasource.driver.class}")
	private String driverClass;
	@Value("${datasource.minPoolSize}")
	private Integer minPoolSize;
	@Value("${datasource.maxPoolSize}")
	private Integer maxPoolSize;
	@Value("${datasource.maxIdleTime}")
	private Integer maxIdleTime;
	@Value("${datasource.maxStatements}")
	private Integer maxStatements;	
	@Value("${datasource.maxStatementsPerConnection}")
	private Integer maxStatementsPerConnection;	
	@Value("${datasource.maxIdleTimeExcessConnections}")
	private Integer maxIdleTimeExcessConnections;				
	@Value("${testcontainer.user}")
	private String userName;
	@Value("${testcontainer.password}")
	private String password;
	@Value("${testcontainer.mysql.version}")
	private String mysql_version;
	@Value("${testcontainer.database.name}")
	private String databaseName;
	@Value("${testcontainer.exposed.port}")
	private Integer portExposed;
	@Value("${testcontainer.createdatabase}")
	private String createDatabaseIfNotExist;
	@Value("${testcontainer.init.script}")
	private String initScript;
	private Logger logger = LoggerFactory.getLogger(TestContainerConfig.class);
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ComboPooledDataSource dataSource() {
		ComboPooledDataSource ds = new ComboPooledDataSource();
		try {
			ds.setDriverClass(driverClass);
			ds.setJdbcUrl(format("jdbc:tc:%s:///%s:%s/%s?%s=%s&%s=%s", mysql_version, "localhost",
					portExposed, databaseName, "createDatabaseIfNotExist", createDatabaseIfNotExist, "TC_INITSCRIPT", initScript));
			ds.setUser(userName);
			ds.setPassword(password);
			ds.setMinPoolSize(minPoolSize);
            ds.setMaxPoolSize(maxPoolSize);
            ds.setMaxIdleTime(maxIdleTime);
            ds.setMaxStatements(maxStatements);
            ds.setMaxStatementsPerConnection(maxStatementsPerConnection);
            ds.setMaxIdleTimeExcessConnections(maxIdleTimeExcessConnections);
			} catch (PropertyVetoException e) {
			e.printStackTrace();
		}		
		if(ds!= null)logger.info("C3PO Data source created");
		return ds;
	}
	@Bean
	public Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.MySQL57Dialect");
		hibernateProp.put("hibernate.hbm2ddl.auto", "none");		
		hibernateProp.put("hibernate.format_sql", true);
		hibernateProp.put("hibernate.use_sql_comments", true);
		hibernateProp.put("hibernate.show_sql", false);
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
	@Bean
	public MySQLContainer mysqlContainer() {
		MySQLContainer mysqlContainer = (MySQLContainer) new MySQLContainer(mysql_version).withDatabaseName(databaseName)
		.withUsername(userName)
		.withPassword(password).withExposedPorts(portExposed).withStartupTimeout(Duration.ofSeconds(30));		
		return mysqlContainer;
	}
	


}
