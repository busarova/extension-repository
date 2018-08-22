package com.telerik.extensionrepository.configuration;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;

@Configuration
@PropertySource("classpath:application.properties")
public class AppConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public DataSource securityDataSource() {
        ComboPooledDataSource securityDataSource = new ComboPooledDataSource();

        try {
            securityDataSource.setDriverClass(env.getProperty("database.driver"));
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }

        securityDataSource.setJdbcUrl(env.getProperty("database.url"));
        securityDataSource.setUser(env.getProperty("database.username"));
        securityDataSource.setPassword(env.getProperty("database.password"));

        securityDataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("connection.initialPoolSize")));
        securityDataSource.setMinPoolSize(Integer.parseInt(env.getProperty("connection.minPoolSize")));
        securityDataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("connection.maxPoolSize")));
        securityDataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("connection.maxIdleTime")));

        return securityDataSource;
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver getCommonsMultipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(20971520);   // 20MB
        multipartResolver.setMaxInMemorySize(1048576);  // 1MB
        return multipartResolver;
    }
}

