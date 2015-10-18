package com.castleby.invoice;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@Test
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/applicationContext.xml" })
public class TestBaseClass extends AbstractTestNGSpringContextTests {

}
