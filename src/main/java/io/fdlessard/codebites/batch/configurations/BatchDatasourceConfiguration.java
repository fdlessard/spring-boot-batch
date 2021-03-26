package io.fdlessard.codebites.batch.configurations;


import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class BatchDatasourceConfiguration {

  @Primary
  @Bean
  @ConfigurationProperties(prefix = "batch.datasource")
  public DataSource batchDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Primary
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      EntityManagerFactoryBuilder builder,
      DataSource batchDataSource
  ) {
    return builder
        .dataSource(batchDataSource)
        .packages("io.fdlessard.codebites.batch")
        .persistenceUnit("batch")
        .build();
  }
/*
  @Primary
  @Bean(name = "batchTransactionManager")
  public PlatformTransactionManager batchTransactionManager(
      @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory
  ) {
    return new JpaTransactionManager(batchEntityManagerFactory);
  }

  @Bean
  public BatchConfigurer configurer(DataSource batchDataSource) {
    return new DefaultBatchConfigurer(batchDataSource);
  }
  */

}