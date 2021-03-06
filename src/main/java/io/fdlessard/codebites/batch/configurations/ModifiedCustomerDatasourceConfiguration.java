package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariConfig;
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
    entityManagerFactoryRef = "modifiedCustomerEntityManagerFactory",
    transactionManagerRef = "modifiedCustomerTransactionManager",
    basePackages = {"io.fdlessard.codebites.batch.modified"}
)
public class ModifiedCustomerDatasourceConfiguration {

  @Bean(name = "modifiedCustomerDataSource")
  @ConfigurationProperties(prefix = "cust2.datasource")
  public DataSource modifiedCustomerDataSource() {

    return DataSourceBuilder.create()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean(name = "modifiedCustomerEntityManagerFactory")
  public LocalContainerEntityManagerFactoryBean modifiedCustomerEntityManagerFactory(
      @Qualifier("modifiedCustomerDataSource") DataSource modifiedCustomerDataSource
  ) {

    return new LocalContainerEntityManagerFactoryBean() {{
      setDataSource(modifiedCustomerDataSource);
      setPersistenceProviderClass(HibernatePersistenceProvider.class);
      setPersistenceUnitName("cust2");
      setPackagesToScan("io.fdlessard.codebites.batch.modified");
      setJpaProperties(Utils.getMariaDbHibernateProperties());
    }};

  }

  @Bean(name = "modifiedCustomerTransactionManager")
  public PlatformTransactionManager modifiedCustomerTransactionManager(
      @Qualifier("modifiedCustomerEntityManagerFactory") EntityManagerFactory modifiedCustomerEntityManagerFactory
  ) {
    return new JpaTransactionManager(modifiedCustomerEntityManagerFactory);
  }
}
