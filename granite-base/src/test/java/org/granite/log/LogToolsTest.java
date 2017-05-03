package org.granite.log;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * User: cbrophy
 * Date: 5/3/17
 * Time: 9:30 AM
 */
public class LogToolsTest {

  @Test
  public void info() throws Exception {
    LogTools.info("This is an info message {0},{1}", 1,2);
  }

  @Test
  public void warn() throws Exception {
    LogTools.warn("This is a warn message: {0},{1},{2}", 1,2,"hello");
  }

  @Test
  public void error() throws Exception {
    LogTools.error("This is an error: {0},{1},{1}",0.1,4);
  }

}