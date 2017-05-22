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
package org.granite.base;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UUIDTools {

  public static byte[] uuidToByteArray(final UUID uuid) {
    checkNotNull(uuid, "uuid");
    final ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
    buffer.putLong(uuid.getMostSignificantBits());
    buffer.putLong(uuid.getLeastSignificantBits());
    return buffer.array();
  }

  public static UUID byteArrayToUUID(final byte[] bytes) {
    checkNotNull(bytes, "bytes");
    checkArgument(bytes.length == 16,
        "UUID has 16 bytes, argument has %s",
        String.valueOf(bytes.length));

    final ByteBuffer buffer = ByteBuffer.wrap(bytes);

    return new UUID(buffer.getLong(0), buffer.getLong(8));
  }

  public static UUID tryParse(final String uuidString) {
    if (!StringTools.isNullOrEmpty(uuidString)) {
      try {
        return UUID.fromString(uuidString);
      } catch (Exception ignored) {

      }
    }

    return null;
  }

  public static String uuidToBase64(final UUID uuid) {
    return Base64
        .getEncoder()
        .encodeToString(uuidToByteArray(uuid));
  }

  public static UUID base64ToUUID(final String base64) {
    checkNotNull(base64, "base64");

    return byteArrayToUUID(
        Base64
            .getDecoder()
            .decode(base64));
  }
}
