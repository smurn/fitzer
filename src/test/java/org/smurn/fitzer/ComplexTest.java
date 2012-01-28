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

import java.math.BigDecimal;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link Complex}.
 */
public class ComplexTest {

    @Test(expected = NullPointerException.class)
    public void ctr_OneNull() {
        new Complex(BigDecimal.ONE, null);
    }

    @Test(expected = NullPointerException.class)
    public void ctr_NullOne() {
        new Complex(null, BigDecimal.ONE);
    }

    @Test(expected = NullPointerException.class)
    public void ctr_NullNull() {
        new Complex(null, null);
    }

    @Test
    public void getReal() {
        Complex target = new Complex(BigDecimal.ONE, BigDecimal.TEN);
        assertEquals(BigDecimal.ONE, target.getReal());
    }

    @Test
    public void getImag() {
        Complex target = new Complex(BigDecimal.ONE, BigDecimal.TEN);
        assertEquals(BigDecimal.TEN, target.getImag());
    }
    
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Complex.class).verify();
    }
}
