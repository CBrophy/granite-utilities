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
package org.granite.process;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.base.Throwables;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import org.granite.base.ExceptionTools;

public final class ProcessTools implements Serializable {

  private ProcessTools() {
  }

  public static String getCommandOutput(final String... commandParts) {

    checkNotNull(commandParts, "commandParts");
    checkArgument(commandParts.length > 0, "No command provided");

    try {
      final Process process = new ProcessBuilder(commandParts)
          .start();

      final StreamReaderThread inputRunnable = new StreamReaderThread(process.getInputStream());
      final StreamReaderThread errorRunnable = new StreamReaderThread(process.getErrorStream());

      final Thread inputThread = new Thread(inputRunnable);
      final Thread errorThread = new Thread(errorRunnable);

      inputThread.start();
      errorThread.start();

      final int exitCode = process.waitFor();

      inputThread.join();
      errorThread.join();

      checkState(exitCode == 0, "Process returned an error state");
      checkState(inputRunnable.isSuccessful(), "Failed to read input stream");
      checkState(errorRunnable.isSuccessful(), "Failed to read error stream");

      return inputRunnable.getStreamContents() + errorRunnable.getStreamContents();
    } catch (IOException | InterruptedException e) {
      throw ExceptionTools.checkedToRuntime(e);
    }
  }

  private static class StreamReaderThread implements Runnable {

    private final BufferedReader bufferedReader;
    private String streamContents;
    private boolean successful = false;

    StreamReaderThread(final InputStream inputStream) {
      this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void run() {
      StringBuilder output = new StringBuilder();

      String line = null;

      try {
        while ((line = bufferedReader.readLine()) != null) {
          output = output.append(line).append('\n');
        }

        bufferedReader.close();

        streamContents = output.toString();

        successful = true;
      } catch (IOException e) {
        throw ExceptionTools.checkedToRuntime(e);
      }
    }

    public String getStreamContents() {
      return streamContents;
    }

    public boolean isSuccessful() {
      return successful;
    }
  }
}
