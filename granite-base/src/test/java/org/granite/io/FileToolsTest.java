package org.granite.io;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;

public class FileToolsTest {

  @Test
  @Ignore
  public void recursiveListDirectories() {
    File temp = new File("/tmp");

    if(!temp.exists()) return;

    List<File> result = FileTools.recursiveListDirectories("/tmp");

    assertNotNull(result);
    assertTrue(result.size() > 0);
    for (File file : result) {
      assertTrue(file.isDirectory());
    }

  }

  @Test
  @Ignore
  public void recursiveListFiles() {
    File temp = new File("/tmp");

    if(!temp.exists()) return;

    List<File> result = FileTools.recursiveListFiles("/tmp");

    assertNotNull(result);
    assertTrue(result.size() > 0);
    for (File file : result) {
      assertTrue(file.isFile());
    }
  }
}