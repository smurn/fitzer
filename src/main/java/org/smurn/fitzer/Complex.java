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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Represents a complex number.
 */
public final class Complex {

    private final BigDecimal real;
    private final BigDecimal imag;

    /**
     * Creates an instance.
     * @param real Real part of the complex number.
     * @param imag Imaginary part of the complex number.
     * @throws NullPointerException if {@code real} or {@code imag} is
     * {@code null}.
     */
    public Complex(BigDecimal real, BigDecimal imag) {
        if (real == null) {
            throw new NullPointerException("Real part must not be null.");
        }
        if (imag == null) {
            throw new NullPointerException("Imaginary part must not be null.");
        }
        this.real = real;
        this.imag = imag;
    }

    /**
     * Gets the imaginary part of this complex number.
     * @return Imaginary part, never {@code null}.
     */
    public BigDecimal getImag() {
        return imag;
    }

    /**
     * Gets the real part of this complex number.
     * @return Real part, never {@code null}.
     */
    public BigDecimal getReal() {
        return real;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        Complex rhs = (Complex) obj;
        return new EqualsBuilder().append(real, rhs.real).
                append(imag, rhs.imag).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(real).
                append(imag).
                toHashCode();
    }

    @Override
    public String toString() {
        return "(" + real + ", " + imag + ")";
    }
}
