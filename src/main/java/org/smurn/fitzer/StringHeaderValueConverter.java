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
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

/**
 * Header value converter for {@code String} values.
 */
final class StringHeaderValueConverter implements HeaderValueConverter {

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
            if (bytes[i] == ' ') {
                continue; // ignore leading spaces
            }
            if (bytes[i] == '\'') {
                return true;
            }
        }
        return false;
    }

    @Override
    public ParsingResult decode(byte[] bytes, long offset,
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

        int start = 0;
        while (start < bytes.length && bytes[start] == ' ') {
            start++;
        }

        if (start >= bytes.length || bytes[start] != '\'') {
            throw new IllegalArgumentException(
                    "Encoded type is not compatible with this converter.");
        }

        int pos = start + 1;
        boolean inside = true;
        StringBuilder str = new StringBuilder();
        while (pos < bytes.length) {
            byte c = bytes[pos];
            if (inside) {
                if (c == '\'') {
                    inside = false;
                } else {
                    checkCharacter(c, offset + pos, errorHandler);
                    str.append((char) c);
                }
            } else {
                if (c == '\'') {
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
                    "StringHeaderValueConverter_DecodeOpen");
            errorHandler.fatal(ex);
            throw ex;
        }

        return new ParsingResult(start == 0, pos, str.toString());
    }

    /**
     * Checks if a character is in the valid range and handles the error if not.
     * @param character Character to check.
     * @param offset Address of the character.
     * @param errorHandler Error handler to report errors to.
     * @throws IOException Thrown if there is a problem.
     */
    private void checkCharacter(byte character, long offset,
            ErrorHandler errorHandler) throws IOException {

        if (character < 0) {
            int code = ((int) character) & 0x0000FF;
            FitsFormatException ex = new FitsFormatException(
                    offset, "StringHeaderValueConverter_DecodeHighBit", code);
            errorHandler.fatal(ex);
            throw ex;
        }

        if (character < 32 || character > 126) {
            FitsFormatException ex = new FitsFormatException(
                    offset, "StringHeaderValueConverter_DecodeLowBit",
                    (int) character);
            errorHandler.error(ex);
        }
    }

    @Override
    public byte[] encode(Object value, boolean fixedFormat,
            ErrorHandler errorHandler) throws IOException {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Value is not a string.");
        }
        String string = (String) value;

        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
        encoder.onMalformedInput(CodingErrorAction.REPORT);
        encoder.onUnmappableCharacter(CodingErrorAction.REPORT);
        CharBuffer buffer = CharBuffer.allocate(string.length());
        buffer.clear();
        buffer.put(string);
        buffer.flip();
        ByteBuffer encodedBody;
        try {
            encodedBody = encoder.encode(buffer);
        } catch (CharacterCodingException ex) {
            FitsDataException e = new FitsDataException(
                    "StringHeaderValueConverter_EncodeHighBit", string);
            errorHandler.fatal(e);
            throw e;
        }

        if (encodedBody.remaining() > 68) {
            FitsDataException ex = new FitsDataException(
                    "StringHeaderValueConverter_EncodeLength",
                    string, encodedBody.remaining());
            errorHandler.fatal(ex);
            throw ex;
        }
        byte[] v = new byte[70];
        int pos = 0;
        v[pos++] = '\'';
        while (encodedBody.hasRemaining()) {
            byte b = encodedBody.get();
            if (b < 32 || b > 126) {
                FitsDataException ex = new FitsDataException(
                        "StringHeaderValueConverter_EncodeLowBit",
                        string, (int) b);
                errorHandler.error(ex);
            }
            v[pos++] = b;
        }
        v[pos++] = '\'';
        while (pos < v.length) {
            v[pos++] = ' ';
        }
        return v;
    }
}
