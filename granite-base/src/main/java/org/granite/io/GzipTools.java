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

import com.google.common.base.Throwables;
import com.google.common.io.CharStreams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static com.google.common.base.Preconditions.checkNotNull;

public class GzipTools {
    public static byte[] compressText(final String plainText) {
        return compressText(plainText, Charset.defaultCharset());
    }

    public static byte[] compressText(final String plainText, final Charset charset) {
        checkNotNull(plainText, "plainText");
        checkNotNull(charset, "charset");

        try {

            final ByteArrayOutputStream memoryStream = new ByteArrayOutputStream();

            final GZIPOutputStream gzipOutputStream = new GZIPOutputStream(memoryStream);

            gzipOutputStream.write(plainText.getBytes(charset));

            memoryStream.close();

            gzipOutputStream.close();

            return memoryStream.toByteArray();

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

    public static String uncompressText(final byte[] compressedText) {
        checkNotNull(compressedText, "compressedText");

        try (final InputStreamReader reader = new InputStreamReader(
                new GZIPInputStream(new ByteArrayInputStream(compressedText)))) {

            return CharStreams.toString(reader);

        } catch (IOException e) {
            throw Throwables.propagate(e);
        }
    }

}
