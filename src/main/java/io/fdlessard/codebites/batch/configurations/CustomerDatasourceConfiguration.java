package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariDataSource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
    entityManagerFactoryRef = "customerEntityManagerFactory",
    transactionManagerRef = "customerTransactionManager",
    basePackages = {"io.fdlessard.codebites.batch.customer"}
)
public class CustomerDatasourceConfiguration {

  @Bean(name = "customerDataSource")
  @ConfigurationProperties(prefix = "cust1.datasource")
  public DataSource customerDataSource() {

    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "customerEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(
      @Qualifier("customerDataSource") DataSource customerDataSource
  ) {

    return new LocalContainerEntityManagerFactoryBean() {{
      setDataSource(customerDataSource);
      setPersistenceProviderClass(HibernatePersistenceProvider.class);
      setPersistenceUnitName("cust1");
      setPackagesToScan("io.fdlessard.codebites.batch.customer");
      setJpaProperties(Utils.getPostgresHibernateProperties());

    }};
  }

  @Bean
  public PlatformTransactionManager customerTransactionManager(
      @Qualifier("customerEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory
  ) {
    return new JpaTransactionManager(customerEntityManagerFactory);
  }
}
