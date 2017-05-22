package org.granite.configuration;

import static org.junit.Assert.assertEquals;

import com.google.common.base.CharMatcher;
import com.google.common.collect.ImmutableMap;
import java.util.Map;
import org.junit.Test;

public class ApplicationConfigurationTest {

  private final static ApplicationConfiguration TEST_CONFIG = new ApplicationConfiguration(
      ImmutableMap.of(
          "testInt", "1"
          , "testDouble", "0.001"
          , "testLong", "123456789101112"
          , "testBoolean", "true"
          , "testMap", "k1=v1,k2=v2"
      )
  );

  @Test
  public void testGetInt() throws Exception {
    assertEquals(1, TEST_CONFIG.getInt("testInt"));
  }

  @Test
  public void testGetInt1() throws Exception {
    assertEquals(5, TEST_CONFIG.getInt("testInt1", 5));
  }

  @Test
  public void testGetLong() throws Exception {
    assertEquals(123456789101112L, TEST_CONFIG.getLong("testLong"));
  }

  @Test
  public void testGetLong1() throws Exception {
    assertEquals(123456789101115L, TEST_CONFIG.getLong("testLong1", 123456789101115L));
  }

  @Test
  public void testGetDouble() throws Exception {
    assertEquals(0.001, TEST_CONFIG.getDouble("testDouble"), 0.0001);
  }

  @Test
  public void testGetDouble1() throws Exception {
    assertEquals(0.75, TEST_CONFIG.getDouble("testDouble1", 0.75), 0.0001);

  }

  @Test
  public void testGetBoolean() throws Exception {
    assertEquals(true, TEST_CONFIG.getBoolean("testBoolean"));
  }

  @Test
  public void testGetBoolean1() throws Exception {
    assertEquals(false, TEST_CONFIG.getBoolean("testBoolean1", false));
  }

  @Test
  public void testGetString() throws Exception {
    assertEquals("true", TEST_CONFIG.getString("testBoolean"));
  }

  @Test(expected = NullPointerException.class)
  public void testGetStringUnknownKey() {
    TEST_CONFIG.getString("no such key");
  }

  @Test
  public void testGetString1() throws Exception {
    assertEquals("foo", TEST_CONFIG.getString("blarg", "foo"));
  }

  @Test
  public void testGetMap() {
    Map<String, String> map = TEST_CONFIG.getMap("testMap",
        CharMatcher.is(','),
        CharMatcher.is('='),
        value -> value);

    assertEquals(2, map.size());
    assertEquals("v1", map.get("k1"));
    assertEquals("v2", map.get("k2"));
  }

}