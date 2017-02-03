package org.granite.base;

import static org.junit.Assert.*;

import java.io.IOException;
import org.junit.Test;

public class ExceptionToolsTest {

    private static void methodThrowsChecked() throws IOException {
        throw new IOException("Checked IO problem");
    }

    private static void methodThrowsUnchecked() {
        final int i = 1;
        final int j = 0;
        final int k = i / j;
    }


    @Test
    public void checkedToRuntime() {
        Throwable convertedChecked = null;
        Throwable unchecked = null;
        Throwable error = null;

        try {
            methodThrowsChecked();
        } catch (Exception e) {
            convertedChecked = ExceptionTools.checkedToRuntime(e);
        }

        try {
            methodThrowsUnchecked();
        } catch (Exception e) {
            unchecked = ExceptionTools.checkedToRuntime(e);
        }

        assertTrue(unchecked instanceof RuntimeException);
        assertTrue(convertedChecked instanceof RuntimeException);
    }

}