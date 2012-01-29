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

import java.math.BigDecimal;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link HeaderValueConverter}.
 */
public class HeaderValueConverterResultTest {

    @Test(expected = IllegalArgumentException.class)
    public void ParsingResult_ctr_Negative() {
        new HeaderValueConverter.ParsingResult(true, -1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ParsingResult_ctr_Object() {
        new HeaderValueConverter.ParsingResult(true, -1, new Object());
    }

    @Test
    public void ParsingResult_ctr_String() {
        HeaderValueConverter.ParsingResult target =
                new HeaderValueConverter.ParsingResult(true, 10, "abc");

        assertEquals("abc", target.getValue());
    }

    @Test
    public void ParsingResult_ctr_BigDecimal() {
        HeaderValueConverter.ParsingResult target =
                new HeaderValueConverter.ParsingResult(true, 10,
                new BigDecimal(5));

        assertEquals(new BigDecimal(5), target.getValue());
    }

    @Test
    public void ParsingResult_ctr_Complex() {
        HeaderValueConverter.ParsingResult target =
                new HeaderValueConverter.ParsingResult(true, 10,
                new Complex(BigDecimal.ZERO, BigDecimal.ZERO));

        assertEquals(new Complex(BigDecimal.ZERO, BigDecimal.ZERO),
                target.getValue());
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(
                HeaderValueConverter.ParsingResult.class).verify();
    }
}
