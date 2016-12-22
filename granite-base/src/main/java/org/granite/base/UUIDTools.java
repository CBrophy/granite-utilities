package org.granite.base;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
