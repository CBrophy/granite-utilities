/*
 * Copyright (C) 2016 Charles Brophy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.granite.io;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.granite.base.ExceptionTools;

public class FileTools {

  public static boolean fileExistsAndCanRead(final String filePath) {
    return fileExistsAndCanRead(new File(filePath));
  }

  public static boolean fileExistsAndCanRead(final File file) {
    return file != null && file.exists() && file.isFile() && file.canRead();
  }

  public static BufferedWriter createWriter(final String filePath, final boolean replace) {
    checkNotNull(filePath, "filePath");

    final File destination = new File(filePath);

    try {
      if (!destination.getParentFile().exists()) {
        Files.createParentDirs(destination);
      }
    } catch (IOException e) {
      throw ExceptionTools.checkedToRuntime(e);
    }

    if (destination.exists() && replace) {
      checkState(destination.delete(), "Failed to delete existing file: %s", filePath);
    }

    try {
      OutputStream outputStream = new FileOutputStream(destination);

      if (filePath.endsWith(".gz")) {
        outputStream = new GZIPOutputStream(outputStream);
      }

      return new BufferedWriter(new OutputStreamWriter(outputStream));
    } catch (IOException e) {
      throw ExceptionTools.checkedToRuntime(e);
    }
  }

  public static BufferedReader createReader(final String filePath) {
    checkNotNull(filePath, "filePath");

    final File source = new File(filePath);

    if (fileExistsAndCanRead(source)) {

      try {
        InputStream inputStream = new FileInputStream(source);

        if (filePath.endsWith(".gz")) {
          inputStream = new GZIPInputStream(inputStream);
        }

        return new BufferedReader(new InputStreamReader(inputStream));
      } catch (IOException e) {
        throw ExceptionTools.checkedToRuntime(e);
      }

    } else {
      return null;
    }
  }
}
