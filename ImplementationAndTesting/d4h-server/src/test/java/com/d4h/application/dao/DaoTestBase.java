package com.d4h.application.dao;

import com.d4h.application.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.ejb.embeddable.EJBContainer;

public class DaoTestBase {
    static EJBContainer container;

     @BeforeAll
     public static void setUp() {
         container = TestUtils.bootstrapEJBContainerWithInMemoryDb();
     }

     @AfterAll
     public static void tearDown() {
         UsersDaoTest.container.close();
     }
}
