package org.granite.configuration;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ConfigToolsTest {

  @Test
  public void testReadConfiguration() throws Exception {
    final ApplicationConfiguration applicationConfig = ConfigTools
        .readConfiguration(null, "test-config.properties", "test-config-2.properties");

    assertEquals(7, applicationConfig.getConfigMap().size());
    assertEquals("bar", applicationConfig.getString("test.val"));
    assertEquals("goodbye", applicationConfig.getString("test.val2"));
    assertEquals("old", applicationConfig.getString("test.old"));
    assertEquals("", applicationConfig.getString("test.not-in-2"));
    assertEquals("foo", applicationConfig.getString("test.not-in-1"));
  }
}