package io.fdlessard.codebites.batch.configurations;

import java.util.Properties;

public class Utils {

  private Utils() {
  }

  public static final Properties getPostgresHibernateProperties() {

    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");
    hibernateProperties.put("hibernate.hbm2dll.create_namespaces", "true");
    hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
    hibernateProperties.put("hibernate.ddl-auto", "update");
    hibernateProperties.put("show-sql", "true");

    return hibernateProperties;
  }


  public static final Properties getH2HibernateProperties() {

    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    hibernateProperties.put("hibernate.hbm2dll.create_namespaces", "true");
    hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
    hibernateProperties.put("hibernate.ddl-auto", "update");
    hibernateProperties.put("show-sql", "true");

    return hibernateProperties;
  }

  public static final Properties getMariaDbHibernateProperties() {

    Properties hibernateProperties = new Properties();
    hibernateProperties.put("hibernate.dialect", "org.hibernate.dialect.MariaDB103Dialect");
    hibernateProperties.put("hibernate.hbm2dll.create_namespaces", "true");
    hibernateProperties.put("hibernate.hbm2ddl.auto", "update");
    hibernateProperties.put("hibernate.ddl-auto", "update");
    hibernateProperties.put("show-sql", "true");

    return hibernateProperties;
  }




}
