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
import org.junit.Test;
import static org.junit.Assert.*;
import static org.smurn.fitzer.TestUtils.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StringHeaderValueConverter}.
 */
public class StringHeaderValueConverterTest {

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
    public void compatibleCheck_String() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleTypeCheck("abc"));
    }

    @Test(expected = NullPointerException.class)
    public void compatibleEncodingCheck_Null() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        target.compatibleEncodingCheck(null);
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
    public void compatibleEncodingCheck_lonelyDoubleQuote() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "\"" + repeat(" ", 29))));
    }

    @Test
    public void compatibleEncodingCheck_SingleQuoteString() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "'ab'" + repeat(" ", 26))));
    }

    @Test
    public void compatibleEncodingCheck_DoubleQuoteString() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 40) + "\"ab\"" + repeat(" ", 26))));
    }

    @Test
    public void compatibleEncodingCheck_Tabs() {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat("\t", 40) + "'ab'" + repeat(" ", 26))));
    }

    @Test(expected = NullPointerException.class)
    public void parse_NullNull() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        target.parse(null, 1000, null);
    }

    @Test(expected = NullPointerException.class)
    public void parse_Null() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        target.parse(null, 1000, THROW_ALWAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void parse_Spaces() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        target.parse(toByte(repeat(" ", 70)), 1000, THROW_ALWAYS);
    }

    @Test
    public void parse_String() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'abcD'" + repeat(" ", 64)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 6, "abcD");

        assertEquals(expected, actual);
    }
    

    @Test
    public void parse_Free() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("    'abcD'" + repeat(" ", 60)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                false, 10, "abcD");

        assertEquals(expected, actual);
    }
    
    @Test
    public void parse_Tabs() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();
        
        ErrorHandler handler = mock(ErrorHandler.class);

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte(" \t  'abcD'" + repeat(" ", 60)),
                1000, handler);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                false, 10, "abcD");

        assertEquals(expected, actual);
        
        verify(handler).warning(
                new FitsFormatException(
                1001, "HeaderValueNonSpaceChars", (int) '\t'));
    }

    @Test
    public void parse_EmptyString() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("''" + repeat(" ", 68)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 2, "");

        assertEquals(expected, actual);
    }

    @Test
    public void parse_StringEscaped() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'a''b'" + repeat(" ", 64)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 6, "a'b");

        assertEquals(expected, actual);
    }

    @Test
    public void parse_StringEscapedTwice() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'a''''b'" + repeat(" ", 62)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 8, "a''b");

        assertEquals(expected, actual);
    }

    @Test
    public void parse_StringEscapedFirst() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'''b'" + repeat(" ", 65)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 5, "'b");

        assertEquals(expected, actual);
    }

    @Test
    public void parse_StringEscapedLast() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'b'''" + repeat(" ", 65)),
                1000, THROW_ALWAYS);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 5, "b'");

        assertEquals(expected, actual);
    }

    @Test
    public void parse_InvalidChar() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        ErrorHandler handler = mock(ErrorHandler.class);

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("'\037'" + repeat(" ", 67)),
                1000, handler);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 3, "\037");

        assertEquals(expected, actual);

        verify(handler).error(new FitsFormatException(1001,
                "StringHeaderValueInvalidChar", 037));
    }

    @Test
    public void parse_highBit() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        ErrorHandler handler = mock(ErrorHandler.class);

        byte[] bytes = toByte("'x'" + repeat(" ", 67));
        bytes[1] = (byte) 128;

        FitsFormatException expected = new FitsFormatException(1001,
                "StringHeaderValueHighBit", 128);

        try {
            target.parse(bytes, 1000, handler);
        } catch (FitsFormatException ex) {
            assertEquals(expected, ex);
        } finally {
            verify(handler).fatal(expected);
        }
    }

    @Test
    public void parse_NotClosed() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        ErrorHandler handler = mock(ErrorHandler.class);

        FitsFormatException expected = new FitsFormatException(1069,
                "StringHeaderValueNotClosed");

        try {
            target.parse(toByte("'abc" + repeat(" ", 66)), 1000, handler);
        } catch (FitsFormatException ex) {
            assertEquals(expected, ex);
        } finally {
            verify(handler).fatal(expected);
        }
    }
    
    @Test
    public void parse_doubleQuote() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        ErrorHandler handler = mock(ErrorHandler.class);

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("\"ab\"" + repeat(" ", 66)),
                1000, handler);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 4, "ab");

        assertEquals(expected, actual);

        verify(handler).error(new FitsFormatException(1000,
                "StringHeaderValueDoubleQuotes"));
    }
    
    @Test
    public void parse_doubleQuoteEscape() throws IOException {
        StringHeaderValueConverter target = new StringHeaderValueConverter();

        ErrorHandler handler = mock(ErrorHandler.class);

        StringHeaderValueConverter.ParsingResult actual = target.parse(
                toByte("\"a\"\"b\"" + repeat(" ", 64)),
                1000, handler);

        StringHeaderValueConverter.ParsingResult expected = new HeaderValueConverter.ParsingResult(
                true, 6, "a\"b");

        assertEquals(expected, actual);

        verify(handler).error(new FitsFormatException(1000,
                "StringHeaderValueDoubleQuotes"));
    }
}
