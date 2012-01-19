/*
 * Copyright 2012 stefan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.smurn.fitzer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Utility methods for unit tests.
 */
public final class TestUtils {
    
    private TestUtils() {
        // no instances
    }
    /**
     * Error handler that throws the exception for all levels.
     */
    public static final ErrorHandler THROW_ALWAYS = new ErrorHandler() {
        
        @Override
        public void fatal(FitsFormatException exception) throws IOException {
            throw exception;
        }
        
        @Override
        public void error(FitsFormatException exception) throws IOException {
            throw exception;
        }
        
        @Override
        public void warning(FitsFormatException exception) throws IOException {
            throw exception;
        }
    };

    /**
     * Concatenates a string repetitively.
     * @param pattern String to concatenate.
     * @param count Number of types to concatenate the pattern.
     * @return Concatenated string.
     */
    public static String repeat(String pattern, int count) {
        if (pattern == null) {
            throw new NullPointerException();
        }
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < count; i++) {
            str.append(pattern);
        }
        return str.toString();
    }

    /**
     * Converts a string to a US-ASCII byte array.
     * @param string String to convert.
     * @return String as US-ASCII byte array.
     */
    public static byte[] toByte(String string) {
        if (string == null) {
            throw new NullPointerException();
        }
        try {
            return string.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
