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
import java.math.BigDecimal;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Interface for converters between the fits binary format and the java
 * representation of header values.
 */
interface HeaderValueConverter {

    /**
     * Represents the results from the parsing operation.
     * <p>See 
     * {@link HeaderValueConverter#parse(byte[], org.smurn.fitzer.ErrorHandler)}
     * .</p>
     */
    public static final class ParsingResult {

        final boolean fixedFormat;
        final int bytesConsumed;
        final Object value;

        /**
         * Creates an instance.
         * @param fixedFormat if the value is in fixed format.
         * @param bytesConsumed Number of bytes from the beginning of the
         * array given to the parsing operation that contain the value.
         * @param value Value that was parsed.
         * @throws IllegalArgumentException if {@code value} is not an
         * instance of {@code String}, {@code BigDecimal}, {@link Complex} or
         * the value {@code null}. Also thrown if {@code bytesConsumed} is
         * negative.
         */
        public ParsingResult(boolean fixedFormat, int bytesConsumed,
                Object value) {

            if (bytesConsumed < 0) {
                throw new IllegalArgumentException(
                        "bytesConsumed must not be negative.");
            }
            if (value != null && !(value instanceof String
                    || value instanceof BigDecimal
                    || value instanceof Complex)) {
                throw new IllegalArgumentException("Value as an invalid type.");
            }
            this.fixedFormat = fixedFormat;
            this.bytesConsumed = bytesConsumed;
            this.value = value;
        }

        /**
         * Gets the number of bytes that contained the encoded value.
         * <p>The bytes are counted from the beginning of the 70-byte array
         * passed to the parser.</p>
         * @return Number of bytes from the beginning of the array.
         */
        public int getBytesConsumed() {
            return bytesConsumed;
        }

        /**
         * Gets if the value was stored in fixed format.
         * <p>The FITS specification defines a a fixed and a free format
         * for some of the value types.</p>
         * @return {@code true} if the value was stored in fixed format,
         * {@code false} otherwise.
         */
        public boolean isFixedFormat() {
            return fixedFormat;
        }

        /**
         * Parsed value. 
         * @return Instance of {@code String}, {@code BigDecimal} 
         * ,{@link Complex}, or the value {@code null}.
         */
        public Object getValue() {
            return value;
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
            ParsingResult rhs = (ParsingResult) obj;
            return new EqualsBuilder().append(fixedFormat, rhs.fixedFormat).
                    append(bytesConsumed, rhs.bytesConsumed).
                    append(value, rhs.value).
                    isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(fixedFormat).
                    append(bytesConsumed).
                    append(value).
                    toHashCode();
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append(value).
                    append(bytesConsumed).
                    append(fixedFormat).build();
        }
    }

    /**
     * Checks if a given java object can be encoded by this converter.
     * @param value Value to check, {@code null} is allowed.
     * @return {@code true} if this value can be encoded by this converter,
     * {@code false} otherwise.
     */
    boolean compatibleTypeCheck(Object value);

    /**
     * Checks if a given FITS representation of a value is of the type
     * supported by this converter.
     * <p>This check is 'lazy' in the way that it does not ensure
     * that the value is properly formatted. It returns {@code true}
     * if the bytes can be recognized as an encoding of this converter's type
     * even if the encoding is invalid. Precise checking and error reporting
     * is done by {@link #parse(byte[], org.smurn.fitzer.ErrorHandler)}.
     * @param bytes Part of the header record containing the value. Must be of
     * length 70.
     * @return {@code true} if the bytes contain a value of the type
     * this converter works with, {@code false} otherwise.
     * @throws NullPointerException if {@code bytes} is {@code null}.
     * @throws IllegalArgumentException if {@code bytes} is not of length 70.
     */
    boolean compatibleEncodingCheck(byte[] bytes);

    /**
     * Parses the value in the FITS binary format into a java type.
     * <p>The given FITS representation might contain additional data
     * such as comments. The implementation recognizes this and reports
     * back which bytes contained the value.</p>
     * @param bytes Part of the header record containing the value. Must be of
     * length 70.
     * @param offset Offset to the first byte of the array. Used for
     * error reporting.
     * @param errorHandler Parsing errors are reported to this handler.
     * @return Results from the parsing process.
     * @throws IOException if the value is not correctly formatted.
     * @throws NullPointerException if either {@code bytes} or
     * {@code errorHandler} is {@code null}.
     * @throws IllegalArgumentException if {@code bytes} is not of length 70.
     */
    ParsingResult parse(byte[] bytes, long offset, ErrorHandler errorHandler)
            throws IOException;

    /**
     * Encodes the value given as a java object into the FITS format.
     * @param value Value to encode.
     * @param fixedFormat if {@code true} the encoding uses the fixed
     * format, otherwise the encoding should be such that the resulting
     * array is as small (no unnecessary padding).
     * @return Array containing the encoded value. No more than 70 bytes.
     * @throws IllegalArgumentException if {@code value} does not compatible
     * (see {@link #compatibleTypeCheck()}) or if {@code fixedFormat} is
     * {@code true} but the converter does not support the fixed format.
     */
    byte[] encode(Object value, boolean fixedFormat);
}
