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

    /**
     * Creates an instance.
     * <p>The value of the created property is {@code null}, the comment is an
     * empty string and {@link #isCommentary()} is false.</p>
     * @param keyword Keyword of the new property.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property(String keyword){
        throw new UnsupportedOperationException();
    }
    
    /**
     * Creates an instance.
     * <p>The comment of the created property is an empty string and
     * {@link #isCommentary()} is false.</p>
     * @param keyword Keyword of the new property.
     * @param value Value of the new property. {@code null} is allowed.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property(String keyword, Object value){
        throw new UnsupportedOperationException();
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
    public Property(String keyword, Object value, String comment){
        throw new UnsupportedOperationException();
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
    public Property(String keyword, Object value, String comment, boolean commentary){
        throw new UnsupportedOperationException();
    }
    
    
    /**
     * Gets the keyword of this property.
     * @return Keyword, might empty but never {@code null}.
     */
    public String getKeyword() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a copy of this property with a different keyword.
     * @param keyword   Keyword to set in the copy.
     * @return New instance equal to this one except for the keyword.
     * @throws NullPointerException if {@code keyword} is {@code null}.
     */
    public Property withKeyword(String keyword) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value of this property.
     * @return Value, might be {@code null}.
     */
    public Object getValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value of this property as an {@code int}.
     * @return value as an {@code int}.
     * @throws ClassCastException if the value is not of type {@code Byte},
     * {@code Short}, {@code Integer}, {@code Long} or {@code BigInteger}. Or
     * if the value is of one of the above types
     * but is either to large or to small for an integer. This differs from
     * the "Narrowing Primitive Conversions" in the java specification.
     * which silently discards the most-significant bits.
     */
    public int getIntValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the value of this property as an {@code long}.
     * @return value as a {@code long}.
     * @throws ClassCastException if the value is not of type {@code Byte},
     * {@code Short}, {@code Integer}, {@code Long} or {@code BigInteger}. Or
     * if the value is of one of the above types
     * but is either to large or to small for a long integer. This differs from
     * the "Narrowing Primitive Conversions" in the java specification.
     * which silently discards the most-significant bits.
     */
    public long getLongValue() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a copy of this property with a different value.
     * @param value   Value to set in the copy. {@code null} is allowed.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(Object value) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /** Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Double}.</p>
     * @param value {@code double} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(double value) {
        throw new UnsupportedOperationException();
    }

    /** Creates a copy of this property with a different value.
     * <p>The value in the new property will be of type {@code Float}.</p>
     * @param value {@code float} value to set in the copy.
     * @return New instance equal to this one except for the value.
     * @throws IllegalStateException if this property is commentary (see
     * {@link #isCommentary()} and {@code value} is not {@code null}.
     */
    public Property withValue(float value) {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    /**
     * Gets the commentary of this property.
     * @return Comment, might empty but never {@code null}.
     */
    public String getComment() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a copy of this property with a different comment.
     * @param comment Comment to set in the copy.
     * @return New instance equal to this one except for the comment.
     * @throws NullPointerException if {@code comment} is {@code null}.
     */
    public Property withComment(String comment) {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets if this property is of the commentary type.
     * <p>Commentary properties always have {@code null} as a value.</p>
     * @return {@code true} if this property is a commentary one, {@code false}
     * otherwise.
     */
    public boolean isCommentary() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }
}
