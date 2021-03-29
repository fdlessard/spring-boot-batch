package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.batch.core.configuration.annotation.BatchConfigurer;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "batchEntityManagerFactory",
    transactionManagerRef = "batchTransactionManager",
    basePackages = {"io.fdlessard.codebites.batch.jobs"}
)
public class BatchDatasourceConfiguration {

  @Bean(name = "batchDataSource")
  @Primary
  @ConfigurationProperties(prefix = "batch.datasource")
  public DataSource batchDataSource() {

    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "batchEntityManagerFactory")
  @Primary
  public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
      @Qualifier("batchDataSource") DataSource batchDataSource
  ) {

    return new LocalContainerEntityManagerFactoryBean() {{
      setDataSource(batchDataSource);
      setPersistenceProviderClass(HibernatePersistenceProvider.class);
      setPersistenceUnitName("batch");
      setPackagesToScan("io.fdlessard.codebites.batch.jobs");
      setJpaProperties(Utils.getH2HibernateProperties());

    }};
  }

  @Bean(name = "batchTransactionManager")
  public PlatformTransactionManager batchTransactionManager(
      @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory
  ) {
    return new JpaTransactionManager(batchEntityManagerFactory);
  }

}