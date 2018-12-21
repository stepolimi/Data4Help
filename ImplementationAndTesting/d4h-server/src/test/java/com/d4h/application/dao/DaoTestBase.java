package com.d4h.application.dao;

import com.d4h.application.TestUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class DaoTestBase {
  @BeforeAll
  public static void setUp() {
      UsersDaoTest.container = TestUtils.bootstrapEJBContainerWithInMemoryDb();
  }

  @AfterAll
  public static void tearDown() {
      UsersDaoTest.container.close();
  }
}
