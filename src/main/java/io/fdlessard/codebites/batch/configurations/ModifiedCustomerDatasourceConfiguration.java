package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Properties;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "modifiedCustomerEntityManagerFactory",
        transactionManagerRef = "modifiedCustomerTransactionManager",
        basePackages = {"io.fdlessard.codebites.batch.modified"}
)
public class ModifiedCustomerDatasourceConfiguration extends HikariConfig {

    protected final static Properties JPA_PROPERTIES = new Properties() {{
        put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        put("hibernate.hbm2dll.create_namespaces",  "true");
        put("hibernate.hbm2ddl.auto", "update");
        put("hibernate.ddl-auto", "create");
        put("show-sql", "true");
    }};

    public final static String PERSISTENCE_UNIT_NAME = "cust2";

    @Bean
    @ConfigurationProperties(prefix = "cust2.datasource")
    public DataSource modifiedCustomerDataSource() {

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "modifiedCustomerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean modifiedCustomerEntityManagerFactory(DataSource modifiedCustomerDataSource) {

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(modifiedCustomerDataSource);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan("io.fdlessard.codebites.batch.modified");
            setJpaProperties(JPA_PROPERTIES);
        }};
    }

    @Bean(name = "modifiedCustomerTransactionManager")
    public PlatformTransactionManager modifiedCustomerTransactionManager(
        @Qualifier("modifiedCustomerEntityManagerFactory") EntityManagerFactory modifiedCustomerEntityManagerFactory
    ) {
        return new JpaTransactionManager(modifiedCustomerEntityManagerFactory);
    }
}
