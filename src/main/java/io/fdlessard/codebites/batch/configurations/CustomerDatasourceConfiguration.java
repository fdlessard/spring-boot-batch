package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ConfigurationProperties("cust1.datasource")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "customerEntityManagerFactory",
        transactionManagerRef = "customerTransactionManager",
        basePackages = {"io.fdlessard.codebites.batch.customer"}
)
public class CustomerDatasourceConfiguration extends HikariConfig {

    protected final static Properties JPA_PROPERTIES = new Properties() {{
        put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        put("hibernate.hbm2dll.create_namespaces",  "true");
        put("hibernate.hbm2ddl.auto", "update");
        put("hibernate.ddl-auto", "create");
        put("show-sql", "true");
       // put("javax.persistence.create-database-schemas", "true");
    }};

    public final static String PERSISTENCE_UNIT_NAME = "cust1";

    @Bean
    public HikariDataSource customerDataSource() {
        return new HikariDataSource(this);
    }

    @Bean(name = "customerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean customerEntityManagerFactory(final HikariDataSource customerDataSource) {

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(customerDataSource);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan("io.fdlessard.codebites.batch.customer");
            setJpaProperties(JPA_PROPERTIES);

        }};
    }

    @Bean(name = "customerTransactionManager")
    public PlatformTransactionManager customerTransactionManager(
        @Qualifier("customerEntityManagerFactory") EntityManagerFactory customerEntityManagerFactory
    ) {
        return new JpaTransactionManager(customerEntityManagerFactory);
    }
}
