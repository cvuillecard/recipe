package com.recipe.config.spring;

import com.recipe.config.spring.repository.ExtensibleRepository;
import com.recipe.config.spring.jpa.ExtensibleRepositoryFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.*;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

@Configuration
@EnableTransactionManagement
@ComponentScans(value = { @ComponentScan({ "com.recipe.app" }) })
@EnableJpaRepositories(value = "com.recipe.app.repository",
        repositoryFactoryBeanClass = ExtensibleRepositoryFactoryBean.class,
        repositoryBaseClass = ExtensibleRepository.class)
public class JpaConfig {
    @Bean
    public LocalEntityManagerFactoryBean entityManagerFactory() {
        final LocalEntityManagerFactoryBean factoryBean = new LocalEntityManagerFactoryBean();

        factoryBean.setPersistenceUnitName("LOCAL_PERSISTENCE");

        return factoryBean;
    }

    @Bean
    JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}