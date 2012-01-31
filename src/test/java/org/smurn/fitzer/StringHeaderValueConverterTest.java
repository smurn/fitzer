/*
 <<<<<<< Updated upstream
 * Copyright 2012 stefan.
 =======
 * Copyright 2012 Stefan C. Mueller.
 >>>>>>> Stashed changes
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
import org.junit.Test;
import static org.junit.Assert.*;
import static org.smurn.fitzer.TestUtils.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StringHeaderValueConverter}.
 */
public class StringHeaderValueConverterTest extends HeaderValueConverterTest {

    @Override
    HeaderValueConverter createTarget() {
        return new StringHeaderValueConverter();
    }

    @Test
    public void compatibleCheck_Null() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertFalse(target.compatibleTypeCheck(null));
    }

    @Test
    public void compatibleCheck_Object() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertFalse(target.compatibleTypeCheck(new Object()));
    }

    @Test
    public void compatibleTypeCheck_String() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleTypeCheck("abc"));
    }

    @Test
    public void compatibleEncodingCheck_Spaces() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(toByte(repeat(" ", 70))));
    }

    @Test
    public void compatibleEncodingCheck_Digit() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "7" + repeat(" ", 29))));
    }

    @Test
    public void compatibleEncodingCheck_Braket() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "(" + repeat(" ", 29))));
    }

    @Test
    public void compatibleEncodingCheck_lonelySingleQuote() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "'" + repeat(" ", 29))));
    }

    @Test
    public void compatibleEncodingCheck_SingleQuoteString() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "'ab'" + repeat(" ", 26))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_Spaces() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        target.decode(toByte(repeat(" ", 70)), 1000, THROW_ALWAYS);
    }

    @Test
    public void decode_fixed() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'ab'", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 4, "ab");

        assertEquals(expected, actual);
    }

    public void decode_free() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad(" 'ab'", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(false, 5, "ab");

        assertEquals(expected, actual);
    }

    @Test
    public void encode_EmptyString() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.decode(
                toByte("''" + repeat(" ", 68)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 2, "");
        assertEquals(expected, actual);
    }

    public void decode_escapeMiddle() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'a''b'", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 6, "a'b");

        assertEquals(expected, actual);
    }

    @Test
    public void decode_escapeDouble() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'a''''b'", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 8, "a''b");

        assertEquals(expected, actual);
    }

    public void decode_escapeStart() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'''ab'", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 6, "'ab");
        assertEquals(expected, actual);
    }

    public void decode_escapeEnd() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'ab'''", 70), 1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 6, "ab'");
        assertEquals(expected, actual);
    }

    public void decode_highbit() throws IOException {
        ErrorHandler handler = mock(ErrorHandler.class);

        FitsFormatException expected = new FitsFormatException(1002,
                "StringHeaderValueConverter_HighBit", 128);

        StringHeaderValueConverter target = new StringHeaderValueConverter();
        try {
            StringHeaderValueConverter.ParsingResult actual =
                    target.decode(toBytePad("'a" + (char) 128 + "b'", 70), 1000,
                    handler);
        } catch (IOException e) {
            assertEquals(expected, e);
        }

        verify(handler).fatal(expected);
    }

    @Test
    public void decode_lowbit() throws IOException {
        ErrorHandler handler = mock(ErrorHandler.class);

        StringHeaderValueConverter target = new StringHeaderValueConverter();
        StringHeaderValueConverter.ParsingResult actual =
                target.decode(toBytePad("'a" + (char) 127 + "b'", 70), 1000,
                handler);

        StringHeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 5,
                "a" + (char) 127 + "b");

        assertEquals(expected, actual);

        verify(handler).error(new FitsFormatException(1002,
                "StringHeaderValueConverter_DecodeLowBit", 127));
    }

    @Test
    public void decode_open() throws IOException {
        ErrorHandler handler = mock(ErrorHandler.class);
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        FitsFormatException expected = new FitsFormatException(1069,
                "StringHeaderValueConverter_DecodeOpen");

        try {
            target.decode(toBytePad("'ab", 70), 1000, handler);
            fail("Fatal exception not thrown.");
        } catch (IOException e) {
            assertEquals(expected, e);
        }

        verify(handler).fatal(expected);
    }

    @Test
    public void encode_fixed() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        byte[] actual = target.encode("abc", true, THROW_ALWAYS);
        byte[] expected = toBytePad("'abc'", 70);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void encode_free() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        byte[] actual = target.encode("abc", false, THROW_ALWAYS);
        byte[] expected = toBytePad("'abc'", 70);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void encode_length() throws IOException {
        ErrorHandler handler = mock(ErrorHandler.class);
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        FitsDataException expected = new FitsDataException(
                "StringHeaderValueConverter_EncodeLength",
                repeat("x", 69), 69);
        try {
            target.encode(repeat("x", 69), false, handler);
        } catch (FitsDataException ex) {
            assertEquals(expected, ex);
        }

        verify(handler).fatal(expected);
    }

    @Test
    public void encode_lowbit() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        ErrorHandler handler = mock(ErrorHandler.class);
        String string = "a" + (char) 127 + "c";
        byte[] actual = target.encode(string, false, handler);
        byte[] expected = toBytePad("'a" + (char) 127 + "c'", 70);
        assertArrayEquals(expected, actual);
        verify(handler).error(new FitsDataException(
                "StringHeaderValueConverter_EncodeLowBit", string, 127));
    }

    @Test
    public void encode_highbit() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        ErrorHandler handler = mock(ErrorHandler.class);
        String string = "a" + (char) 128 + "c";

        FitsDataException expected = new FitsDataException(
                "StringHeaderValueConverter_EncodeHighBit", string);
        try {
            target.encode(string, false, handler);
        } catch (FitsDataException ex) {
            assertEquals(expected, ex);
        }
        verify(handler).fatal(expected);
    }
}
