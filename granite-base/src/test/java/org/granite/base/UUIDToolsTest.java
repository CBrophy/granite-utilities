package org.granite.base;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UUIDToolsTest {

    private static final UUID uuid = UUID.fromString("09e328ab-087d-443f-a55a-804bedf3c334");

    @Test
    public void tryParse() throws Exception {
        UUID test = UUIDTools.tryParse("09e328ab-087d-443f-a55a-804bedf3c334");

        assertEquals(uuid, test);

        assertNull(UUIDTools.tryParse(null));

        assertNull(UUIDTools.tryParse(""));

    }

    @Test
    public void uuidToBase64() throws Exception {
        final String base64 = UUIDTools.uuidToBase64(uuid);

        assertEquals("CeMoqwh9RD+lWoBL7fPDNA==",base64);
    }

    @Test
    public void base64ToUUID() throws Exception {
       final UUID test = UUIDTools.base64ToUUID("CeMoqwh9RD+lWoBL7fPDNA==");

        assertEquals(uuid, test);
    }

}