package com.gitlab.zachdeibert.jnet;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * The JUnit test suite
 * 
 * @author Zach Deibert
 * @since 1.0
 * @version 1.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TransmissionTest.class, BindingTest.class,
                MultiClientTest.class, EventTest.class })
public class TestSuite
{
}
