/*
 * Copyright 2012 Stefan C. Mueller.
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
 * Header value converter for {@code String} values.
 */
public class StringHeaderValueConverter implements HeaderValueConverter {

    @Override
    public boolean compatibleTypeCheck(Object value) {
        return value instanceof String;
    }

    @Override
    public boolean compatibleEncodingCheck(byte[] bytes) {
        if (bytes == null) {
            throw new NullPointerException("bytes must not be null.");
        }
        if (bytes.length != 70) {
            throw new IllegalArgumentException("bytes must be of length 70.");
        }

        for (int i = 0; i < bytes.length && bytes[i] != '/'; i++) {
            if (bytes[i] == ' ' || bytes[i] == '\t') {
                continue; // ignore leading spaces
            }
            if (bytes[i] == '\'' || bytes[i] == '\"') {
                return true;
            }
        }
        return false;
    }

    @Override
    public ParsingResult parse(byte[] bytes, long offset,
            ErrorHandler errorHandler) throws IOException {

        if (bytes == null) {
            throw new NullPointerException("bytes must not be null.");
        }
        if (errorHandler == null) {
            throw new NullPointerException("errorHandler must not be null.");
        }
        if (bytes.length != 70) {
            throw new IllegalArgumentException("bytes must be of length 70.");
        }

        int foundNonSpaces = -1;
        int start = 0;
        while (start < bytes.length
                && (bytes[start] == ' ' || bytes[start] == '\t')) {
            if (foundNonSpaces < 0 && bytes[start] == '\t') {
                foundNonSpaces = start;
            }
            start++;
        }

        if (foundNonSpaces >= 0) {
            FitsFormatException ex = new FitsFormatException(
                    offset + foundNonSpaces,
                    "HeaderValueNonSpaceChars", (int) '\t');
            errorHandler.warning(ex);
        }

        if (start >= bytes.length
                || (bytes[start] != '\'' && bytes[start] != '\"')) {
            throw new IllegalArgumentException(
                    "Encoded type is not compatible with this converter.");
        }

        byte quote = bytes[start];  // either ' or "

        if (quote == '"') {
            FitsFormatException ex = new FitsFormatException(
                    offset + start,
                    "StringHeaderValueDoubleQuotes");
            errorHandler.error(ex);
        }

        int pos = start + 1;
        boolean inside = true;
        StringBuilder str = new StringBuilder();
        while (pos < bytes.length) {
            byte c = bytes[pos];
            if (inside) {
                if (c == quote) {
                    inside = false;
                } else {
                    checkCharacter(c, offset + pos, errorHandler);
                    str.append((char) c);
                }
            } else {
                if (c == quote) {
                    inside = true;
                    checkCharacter(c, offset + pos, errorHandler);
                    str.append((char) c);
                } else {
                    break;
                }
            }
            pos++;
        }

        if (inside) {
            FitsFormatException ex = new FitsFormatException(
                    offset + bytes.length - 1,
                    "StringHeaderValueNotClosed");
            errorHandler.fatal(ex);
            throw ex;
        }

        return new ParsingResult(start == 0, pos, str.toString());
    }

    /**
     * Checks if a character is in the valid range and handles
     * the error if not.
     * @param character Character to check.
     * @param offset    Address of the character.
     * @param errorHandler  Error handler to report errors to.
     * @throws IOException Thrown if there is a problem.
     */
    private void checkCharacter(byte character, long offset,
            ErrorHandler errorHandler) throws IOException {

        if (character < 0) {
            int code = ((int) character) & 0x0000FF;
            FitsFormatException ex = new FitsFormatException(
                    offset, "StringHeaderValueHighBit", code);
            errorHandler.fatal(ex);
            throw ex;
        }

        if (character < 32 || character > 126) {
            FitsFormatException ex = new FitsFormatException(
                    offset, "StringHeaderValueInvalidChar", (int) character);
            errorHandler.error(ex);
        }
    }

    @Override
    public byte[] encode(Object value, boolean fixedFormat) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Value is not a string.");
        }
        String string = (String) value;
        try {
            return string.getBytes("US-ASCII");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }
}
