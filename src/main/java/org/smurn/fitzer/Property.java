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
import java.math.BigInteger;

/**
 * Represents a key-value pair of a FITS HDU header.
 * <p>The key is always represented as a single string. This class does
 * not enforce a certain format on the key to support conventions such
 * as {@code HIERARCH} that extend the set of legal keys.</p>
 * <p>FITS supports several value types such as integers or complex numbers
 * again there are conventions that describe additional types. This class
 * stores the value as an {@code Object} to support any possible data type.<br/>
 * For convenience this class provides special methods for the most commonly
 * used types.</p>
 * <p>The FITS standard discriminates between properties with a value
 * and commentary properties that, by definition, have no value (the value is
 * always set to {@code null}. Both types
 * can have a comment attached.</p>
 * <p>Conventions are often using comments to encode information in a way that
 * allows backward compatibility. For this reason both comments on regular,
 * and especially comments of commentary properties might store information
 * beyond mere comments. When loading or storing FITS files those comments
 * may be interpreted if the convention is supported. Thous the properties
 * that the applications sees might not map directly to the properties in the
 * file.</p>
 * <p>Only immutable objects must be used as values.</p>
 * <p>Instances of this class are immutable.</p>
 */
public final class Property {

    private final String keyword;

    private final Object value;

    private final String comment;

    private final boolean commentary;

    /**
     * Creates an instance.
     * <p>The value of the created property is {@code null}, the comment is an
     * empty string and {@link #isCommentary()} is false.</p>
     * @param keyword Keyword of the new property.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property(String keyword) {
        if (keyword == null) {
            throw new NullPointerException("Keyword must not be null.");
        }
        this.keyword = keyword;
        this.value = null;
        this.comment = "";
        this.commentary = false;
    }

    /**
     * Creates an instance.
     * <p>The comment of the created property is an empty string and
     * {@link #isCommentary()} is false.</p>
     * @param keyword Keyword of the new property.
     * @param value Value of the new property. {@code null} is allowed.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property(String keyword, Object value) {
        if (keyword == null) {
            throw new NullPointerException("Keyword must not be null.");
        }
        this.keyword = keyword;
        this.value = value;
        this.comment = "";
        this.commentary = false;
    }

    /**
     * Creates an instance.
     * <p> {@link #isCommentary()} of the created property is false.</p>
     * @param keyword Keyword of the new property.
     * @param value Value of the new property. {@code null} is allowed.
     * @param comment Comment of the new property.
     * @throws NullPointerException if {@code keyword} or {@code comment} is
     * {@code null}.
     */
    public Property(String keyword, Object value, String comment) {
        if (keyword == null) {
            throw new NullPointerException("Keyword must not be null.");
        }
        if (comment == null) {
            throw new NullPointerException("Comment must not be null.");
        }
        this.keyword = keyword;
        this.value = value;
        this.comment = comment;
        this.commentary = false;
    }

    /**
     * Creates an instance.
     * <p>The value of the created property is {@code null}</p>
     * @param keyword Keyword of the new property.
     * @param value Value of the new property. {@code null} is allowed.
     * @param comment Comment of the new property.
     * @param commentary {@code true} if the new property shall be of 
     * commentary type, {@code false} otherwise.
     * @throws NullPointerException if {@code keyword} or {@code comment} is
     * {@code null}.
     * @throws IllegalArgumentException if {@code commentary} is {@code true}
     * and {code value} is not {@code null}.
     */
    public Property(String keyword, Object value, String comment,
            boolean commentary) {
        if (keyword == null) {
            throw new NullPointerException("Keyword must not be null.");
        }
        if (comment == null) {
            throw new NullPointerException("Comment must not be null.");
        }
        if (commentary && value != null) {
            throw new IllegalArgumentException(
                    "Commentary properties must have null as value.");
        }
        this.keyword = keyword;
        this.value = value;
        this.comment = comment;
        this.commentary = commentary;
    }

    /**
     * Gets the keyword of this property.
     * @return Keyword, might empty but never {@code null}.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Creates a copy of this property with a different keyword.
     * @param keyword   Keyword to set in the copy.
     * @return New instance equal to this one except for the keyword.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property withKeyword(String keyword) {
        return new Property(keyword, value, comment, commentary);
    }

    /**
     * Gets the value of this property.
     * @return Value, might be {@code null}.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Gets the value of this property as an {@code int}.
     * @return value as an {@code int}.
     * @throws IllegalStateException if the value is null or not of type 
     * {@code Byte}, {@code Short}, {@code Integer}, {@code Long} or
     * {@code BigInteger}. Or if the value is of one of the above types
     * but is either to large or to small for an integer. This differs from
     * the "Narrowing Primitive Conversions" in the java specification.
     * which silently discards the most-significant bits.
     */
    public int getIntValue() {
        if (value == null) {
            throw new IllegalStateException(
                    "Cannot convert null value to an int value.");
        }
        if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            long number = (Long) value;
            if (number < Integer.MIN_VALUE || number > Integer.MAX_VALUE) {
                throw new IllegalStateException("Value is outside of the"
                        + " range for an int value.");
            }
            return (int) number;
        } else if (value instanceof BigInteger) {
            BigInteger number = (BigInteger) value;
            BigInteger min = BigInteger.valueOf(Integer.MIN_VALUE);
            BigInteger max = BigInteger.valueOf(Integer.MAX_VALUE);
            if (number.compareTo(min) < 0 || number.compareTo(max) > 0) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for an int value.");
            }
            return number.intValue();
        } else {
            throw new IllegalStateException(
                    "Cannot convert type " + value.getClass().getSimpleName()
                    + " to int.");
        }
    }

    /**
     * Gets the value of this property as an {@code long}.
     * @return value as a {@code long}.
     * @throws IllegalStateException if the value is null or not of type 
     * {@code Byte}, {@code Short}, {@code Integer}, {@code Long} or
     * {@code BigInteger}. Or if the value is of one of the above types
     * but is either to large or to small for a long integer. This differs from
     * the "Narrowing Primitive Conversions" in the java specification.
     * which silently discards the most-significant bits.
     */
    public long getLongValue() {
        if (value == null) {
            throw new IllegalStateException(
                    "Cannot convert null value to a long value.");
        }
        if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof BigInteger) {
            BigInteger number = (BigInteger) value;
            BigInteger min = BigInteger.valueOf(Long.MIN_VALUE);
            BigInteger max = BigInteger.valueOf(Long.MAX_VALUE);
            if (number.compareTo(min) < 0 || number.compareTo(max) > 0) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for an long value.");
            }
            return number.longValue();
        } else {
            throw new IllegalStateException(
                    "Cannot convert type " + value.getClass().getSimpleName()
                    + " to long.");
        }
    }

    /**
     * Gets the value of this property as a {@code float}.
     * @return value as a {@code float}.
     * @throws IllegalStateException if the value is not of type {@code Byte},
     * {@code Short}, {@code Integer}, {@code Long}, {@code BigInteger},
     * {@code Float}, {@code BigInteger}{@code Double} or {@code BigDecimal}.
     * Or if the value is finite and larger than
     * {@code Float.MAX_VALUE} or smaller than {@code -Float.MAX_VALUE}.
     * Note that even if the value is in between a loss of precision is
     * possible. This will NOT cause an exception.
     */
    public float getFloatValue() {
        if (value == null) {
            throw new IllegalStateException(
                    "Cannot convert null value to a float value.");
        }
        if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof BigInteger) {
            BigInteger number = (BigInteger) value;
            float asFloat = number.floatValue();
            if (Float.isInfinite(asFloat)) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for a float value.");
            }
            return asFloat;
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Double) {
            double asDouble = (Double) value;
            if (Double.isInfinite(asDouble) || Double.isNaN(asDouble)) {
                return (float) asDouble;
            }
            float asFloat = (float) asDouble;
            if (Float.isInfinite(asFloat)) {
                throw new IllegalStateException("Value " + value
                        + " is outside of the"
                        + " range for a float value.");
            }
            return asFloat;
        } else if (value instanceof BigDecimal) {
            BigDecimal number = (BigDecimal) value;
            float asFloat = number.floatValue();
            if (Float.isInfinite(asFloat)) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for a float value.");
            }
            return asFloat;
        } else {
            throw new IllegalStateException(
                    "Cannot convert type " + value.getClass().getSimpleName()
                    + " to float.");
        }
    }

    /**
     * Gets the value of this property as a {@code double}.
     * @return value as a {@code double}.
     * @throws IllegalStateException if the value is not of type {@code Byte},
     * {@code Short}, {@code Integer}, {@code Long}, {@code BigInteger},
     * {@code Float}, {@code BigInteger}{@code Double} or {@code BigDecimal}.
     * Or if the value is finite and larger than
     * {@code Double.MAX_VALUE} or smaller than {@code -Double.MAX_VALUE}.
     * Note that even if the value is in between a loss of precision is
     * possible. This will NOT cause an exception.
     */
    public double getDoubleValue() {
        if (value == null) {
            throw new IllegalStateException(
                    "Cannot convert null value to a double value.");
        }
        if (value instanceof Byte) {
            return (Byte) value;
        } else if (value instanceof Short) {
            return (Short) value;
        } else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof BigInteger) {
            BigInteger number = (BigInteger) value;
            double asDouble = number.doubleValue();
            if (Double.isInfinite(asDouble)) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for a double value.");
            }
            return asDouble;
        } else if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof Double) {
            return (Double) value;
        } else if (value instanceof BigDecimal) {
            BigDecimal number = (BigDecimal) value;
            double asDouble = number.doubleValue();
            if (Double.isInfinite(asDouble)) {
                throw new IllegalStateException("Value " + number
                        + " is outside of the"
                        + " range for a double value.");
            }
            return asDouble;
        } else {
            throw new IllegalStateException(
                    "Cannot convert type " + value.getClass().getSimpleName()
                    + " to double.");
        }
    }

    /**
     * Gets the value of this property as a {@code String}.
     * <p>Note that this method is NOT equivalent to
     * {@code getValue().toString()}. It's a short hand for
     * {@code (String)getValue()} (but with a different exception).</p>
     * @return value as a {@code String}, might be {@code null}.
     * @throws IllegalStateException if the value is not of type {@code String}.
     */
    public String getStringValue() {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            throw new IllegalStateException(
                    "Cannot convert type " + value.getClass().getSimpleName()
                    + " to String.");
        }
    }

    /**
     * Creates a copy of this property with a different value.
     * @param value   Value to set in the copy. {@code null} is allowed.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(Object value) {
        return new Property(keyword, value, comment, commentary);
    }

    /**
     * Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Integer}.</p>
     * @param value {@code int} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(int value) {
        return new Property(keyword, value, comment, commentary);
    }

    /**
     * Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Long}.</p>
     * @param value {@code long} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(long value) {
        return new Property(keyword, value, comment, commentary);
    }

    /** Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Double}.</p>
     * @param value {@code double} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(double value) {
        return new Property(keyword, value, comment, commentary);
    }

    /** Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Float}.</p>
     * @param value {@code float} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(float value) {
        return new Property(keyword, value, comment, commentary);
    }

    /** Creates a copy of this property with a different value.
     * <p>The behaviour is identical to {@link #withValue(java.lang.Object)}.
     * This method only exists for completeness of the API.</p>
     * @param value {@code String} value to set in the copy. {@code null} is
     * allowed.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(String value) {
        return new Property(keyword, value, comment, commentary);
    }

    /**
     * Gets the commentary of this property.
     * @return Comment, might empty but never {@code null}.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Creates a copy of this property with a different comment.
     * @param comment Comment to set in the copy.
     * @return New instance equal to this one except for the comment.
     * @throws NullPointerException if {@code comment} is {@code null}.
     */
    public Property withComment(String comment) {
        return new Property(keyword, value, comment, commentary);
    }

    /**
     * Gets if this property is of the commentary type.
     * <p>Commentary properties always have {@code null} as a value.</p>
     * @return {@code true} if this property is a commentary one, {@code false}
     * otherwise.
     */
    public boolean isCommentary() {
        return commentary;
    }

    /**
     * Creates a copy of this property with a possibly commentary type.
     * @param commentary {@code true} if the copy shall be a commentary one, 
     * {@code false} otherwise.
     * @return New instance equal to this one except for the comment.
     * @throws IllegalStateException if {@code commentary} is {@code true} but
     * the value of this property is not {@code null}.
     */
    public Property withCommentary(boolean commentary) {
        if (commentary && value != null) {
            throw new IllegalStateException(
                    "Commentary properties must have null as value.");
        }
        return new Property(keyword, value, comment, commentary);
    }
}
