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

import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.MissingResourceException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link FitsFormatException}.
 */
public class FitsFormatExceptionTest {

    @Test(expected = IllegalArgumentException.class)
    public void ctr_NegativeOffset() {
        new FitsFormatException(-1, "FitsFormatExceptionTest", 0, 0);
    }

    @Test(expected = NullPointerException.class)
    public void ctr_NullKey() {
        new FitsFormatException(0, null, 0, 0);
    }

    @Test(expected = MissingResourceException.class)
    public void ctr_NonExistentKey() {
        new FitsFormatException(0, "does_not_exist", 0, 0);
    }

    @Test(expected = IllegalFormatException.class)
    public void ctr_MissingParameter() {
        new FitsFormatException(0, "FitsFormatExceptionTest", 0);
    }

    @Test
    public void getOffset() {
        FitsFormatException target = new FitsFormatException(100,
                "FitsFormatExceptionTest", 0, 0);
        assertEquals(100, target.getOffset());
    }

    @Test
    public void getMessage() {
        FitsFormatException target = new FitsFormatException(100,
                "FitsFormatExceptionTest", 1, 2);
        String actual = target.getMessage();
        String expected = "Near byte 100: Message with two parameters 1 2.";
        assertEquals(expected, actual);
    }

    @Test
    public void getLocalizedMessage() {
        FitsFormatException target = new FitsFormatException(100,
                "FitsFormatExceptionTest", 1, 2);
        Locale.setDefault(Locale.GERMAN);
        String actual = target.getLocalizedMessage();
        String expected = "Nahe Byte 100: Nachricht mit zwei Parametern 1 2.";
        assertEquals(expected, actual);
    }

    @Test
    public void getLocalizedMessage_GERMAN() {
        FitsFormatException target = new FitsFormatException(100,
                "FitsFormatExceptionTest", 1, 2);
        String actual = target.getLocalizedMessage(Locale.GERMAN);
        String expected = "Nahe Byte 100: Nachricht mit zwei Parametern 1 2.";
        assertEquals(expected, actual);
    }
}
