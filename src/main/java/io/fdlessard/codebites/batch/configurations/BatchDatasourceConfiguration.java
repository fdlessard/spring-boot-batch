package io.fdlessard.codebites.batch.configurations;


import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.jpa.HibernatePersistenceProvider;
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
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef = "batchTransactionManager",
        basePackages = {"io.fdlessard.codebites.batch.jobs"}
)
public class BatchDatasourceConfiguration {

    protected final static Properties JPA_PROPERTIES = new Properties() {{
        put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
        put("hibernate.hbm2dll.create_namespaces", "true");
        put("hibernate.hbm2ddl.auto", "update");
        put("hibernate.ddl-auto", "create");
        put("show-sql", "true");
        // put("javax.persistence.create-database-schemas", "true");
    }};

    public final static String PERSISTENCE_UNIT_NAME = "cust1";

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "batch.datasource")
    public DataSource batchDataSource() {

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "batchEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(DataSource batchDataSource) {

        return new LocalContainerEntityManagerFactoryBean() {{
            setDataSource(batchDataSource);
            setPersistenceProviderClass(HibernatePersistenceProvider.class);
            setPersistenceUnitName(PERSISTENCE_UNIT_NAME);
            setPackagesToScan("io.fdlessard.codebites.batch.jobs");
            setJpaProperties(JPA_PROPERTIES);

        }};
    }

    @Primary
    @Bean(name = "batchTransactionManager")
    public PlatformTransactionManager batchTransactionManager(
            @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory
    ) {
        return new JpaTransactionManager(batchEntityManagerFactory);
    }
}
