package com.d4h.application;

import org.apache.openejb.Container;

import javax.ejb.embeddable.EJBContainer;
import java.util.Properties;

public class TestUtils {

  public static String DATA_SOURCE_NAME = "jdbc/db";

  public static EJBContainer bootstrapEJBContainerWithInMemoryDb() {
    final Properties p = new Properties();
    p.put(DATA_SOURCE_NAME, "new://Resource?type=DataSource");
    p.put(DATA_SOURCE_NAME + ".JdbcDriver", "org.hsqldb.jdbcDriver");
    p.put(DATA_SOURCE_NAME + ".JdbcUrl", "jdbc:hsqldb:mem:moviedb");

    return EJBContainer.createEJBContainer(p);
  }

}
