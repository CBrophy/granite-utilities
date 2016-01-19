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

package org.granite.log;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class LogTools {

    private LogTools(){}

    // log output will look like:
    // [Tue Dec 16 10:29:07 PST 2014] INFO: <message>
    public static final String EASY_LOG_FORMAT = "[%1$tc] %4$s: %5$s %n";

    private final static Logger LOG = Logger.getGlobal();

    public static void updateLogFormat(){
        updateLogFormat(EASY_LOG_FORMAT);
    }

    public static void updateLogFormat(final String logFormat){
        // see doc for java.util.logging.SimpleFormatter
        System.setProperty("java.util.logging.SimpleFormatter.format", logFormat);
    }

    private static void log(final Level level, final String message, final String... args) {
        LOG.log(level, message, args);
    }

    public static void info(final String message, final String... args) {
        log(Level.INFO, message, args);
    }

    public static void info(final String message) {
        info(message, "");
    }

    public static void warn(final String message, final String... args) {
        log(Level.WARNING, message, args);
    }

    public static void warn(final String message) {warn(message, "");}

    public static void error(final String message, final String... args) {
        log(Level.SEVERE, message, args);
    }

    public static void error(final String message) {
        error(message, "");
    }
}
