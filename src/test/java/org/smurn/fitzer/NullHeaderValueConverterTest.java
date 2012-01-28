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
import org.junit.Test;
import static org.junit.Assert.*;
import static org.smurn.fitzer.TestUtils.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link NullHeaderValueConverter}.
 */
public class NullHeaderValueConverterTest {

    @Test
    public void compatibleCheck_Null() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertTrue(target.compatibleTypeCheck(null));
    }

    @Test
    public void compatibleCheck_Object() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertFalse(target.compatibleTypeCheck(new Object()));
    }

    @Test(expected = NullPointerException.class)
    public void compatibleEncodingCheck_Null() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        target.compatibleEncodingCheck(null);
    }

    @Test
    public void compatibleEncodingCheck_Spaces() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(toByte(repeat(" ", 70))));
    }

    @Test
    public void compatibleEncodingCheck_SpacesComment() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte(repeat(" ", 49) + "/" + repeat(" ", 20))));
    }

    @Test
    public void compatibleEncodingCheck_OnlyComment() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertTrue(target.compatibleEncodingCheck(
                toByte("/" + repeat(" ", 69))));
    }

    @Test
    public void compatibleEncodingCheck_DoubleQuotationMark() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 49) + "\"" + repeat(" ", 20))));
    }

    @Test
    public void compatibleEncodingCheck_SingeQuotationMark() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 49) + "'" + repeat(" ", 20))));
    }

    @Test
    public void compatibleEncodingCheck_Decimal() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 49) + "5" + repeat(" ", 20))));
    }

    @Test
    public void compatibleEncodingCheck_Bracket() {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        assertFalse(target.compatibleEncodingCheck(
                toByte(repeat(" ", 49) + "(" + repeat(" ", 20))));
    }

    @Test(expected = NullPointerException.class)
    public void parse_NullNull() throws IOException {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        target.decode(null, 1000, null);
    }

    @Test(expected = NullPointerException.class)
    public void parse_Null() throws IOException {
        NullHeaderValueConverter target = new NullHeaderValueConverter();
        target.decode(null, 1000, THROW_ALWAYS);
    }

    @Test
    public void parse_Spaces() throws IOException {
        NullHeaderValueConverter target = new NullHeaderValueConverter();

        HeaderValueConverter.ParsingResult actual =
                target.decode(toByte(repeat(" ", 70)), 0, THROW_ALWAYS);

        HeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 70, null);

        assertEquals(expected, actual);
    }

    @Test
    public void parse_SpacesComment() throws IOException {
        NullHeaderValueConverter target = new NullHeaderValueConverter();

        HeaderValueConverter.ParsingResult actual =
                target.decode(toByte(repeat(" ", 49) + "/" + repeat(" ", 20)),
                0, THROW_ALWAYS);

        HeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 49, null);

        assertEquals(expected, actual);
    }

    @Test
    public void parse_OnlyComment() throws IOException {
        NullHeaderValueConverter target = new NullHeaderValueConverter();

        HeaderValueConverter.ParsingResult actual =
                target.decode(toByte("/" + repeat(" ", 69)),
                0, THROW_ALWAYS);

        HeaderValueConverter.ParsingResult expected =
                new HeaderValueConverter.ParsingResult(true, 0, null);

        assertEquals(expected, actual);
    }
}
