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
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Property of a FITS header. <p>Instances of this type are immutable.</p>
 */
public class Property {

    private final String keyword;
    private final Object value;
    private final String comment;
    private final boolean commentary;

    /**
     * Creates an instance. <p>The created property is not commentary. The
     * comment is an empty string.</p>
     * @param keyword Keyword of the property.
     * @param value Value of the property, must of one of the following types:
     * {@code String}, {@code BigDecimal} or {@link Complex}.
     * @throws NullPointerException if {@code keyword} or {@code comment} are
     * {@code null}.
     * @throws IllegalArgumentException if {@code value} is of a wrong type.
     */
    public Property(String keyword, Object value) {
        this(keyword, value, "", false);
    }

    /**
     * Creates an instance. <p>The created property is not commentary.</p>
     * @param keyword Keyword of the property.
     * @param value Value of the property, must of one of the following types:
     * {@code String}, {@code BigDecimal} or {@link Complex}.
     * @param comment Comment of this property.
     * @throws NullPointerException if {@code keyword} or {@code comment} are
     * {@code null}.
     * @throws IllegalArgumentException if {@code value} is of a wrong type.
     */
    public Property(String keyword, Object value, String comment) {
        this(keyword, value, comment, false);
    }

    /**
     * Creates an instance.
     * @param keyword Keyword of the property.
     * @param value Value of the property, must of one of the following types:
     * {@code String}, {@code BigDecimal} or {@link Complex}.
     * @param comment Comment of this property.
     * @param commentary If this property is commentary or not.
     * @throws NullPointerException if {@code keyword} or {@code comment} are
     * {@code null}.
     * @throws IllegalArgumentException if {@code value} is of a wrong type or
     * if {@code commentary} is {@code true} but {@code value} is not
     * {@code null}.
     */
    public Property(String keyword, Object value, String comment,
            boolean commentary) {

        if (keyword == null) {
            throw new NullPointerException("Keyword must not be null.");
        }
        if (value != null
                && !(value instanceof String)
                && !(value instanceof BigDecimal)
                && !(value instanceof Complex)) {
            throw new IllegalArgumentException(
                    "Unsupported value type: "
                    + value.getClass().getSimpleName());
        }
        if (comment == null) {
            throw new NullPointerException("Comment must not be null.");
        }
        if (commentary && value != null) {
            throw new IllegalArgumentException(
                    "Commentary properties must not have a non-null value.");
        }
        this.keyword = keyword;
        this.value = value;
        this.comment = comment;
        this.commentary = commentary;
    }

    /**
     * Gets the keyword of this property.
     * @return Keyword, never {@code null}.
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the value of this property.
     * @return Value of this property. Will be {@code null} or of type
     * {@code String}, {@code BigDecimal} or {@link Complex}.
     */
    public Object get() {
        return value;
    }

    /**
     * Gets the value of this property.
     * @return Value of this property, may be {@code null}.
     * @throws IllegalStateException if the value is neigher {@code null} nor a
     * {@code String}.
     */
    public String getString() {
        if (value == null) {
            return null;
        } else if (value instanceof String) {
            return (String) value;
        } else {
            throw new IllegalStateException(
                    "Property does not contain a string value.");
        }
    }

    /**
     * Gets the value of this property.
     * @return Value of this property, may be {@code null}.
     * @throws IllegalStateException if the value is neigher {@code null} nor a
     * {@code BigDecimal}.
     */
    public BigDecimal getNumber() {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else {
            throw new IllegalStateException(
                    "Property does not contain a numeric value.");
        }
    }

    /**
     * Gets the value of this property.
     * @return Value of this property, may be {@code null}.
     * @throws IllegalStateException if the value is neigher {@code null} nor a
     * {@code Complex}.
     */
    public Complex getComplex() {
        if (value == null) {
            return null;
        } else if (value instanceof BigDecimal) {
            return (Complex) value;
        } else {
            throw new IllegalStateException(
                    "Property does not contain a complex value.");
        }
    }

    /**
     * Gets the comment of this property.
     * @return Comment, never {@code null}.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Gets if this property is of commentary type.
     * @return {@code true} if this property is commentary, {@code false}
     * otherwise.
     */
    public boolean isCommentary() {
        return commentary;
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
        Property rhs = (Property) obj;
        return new EqualsBuilder().append(keyword, rhs.keyword).
                append(value, rhs.value).
                append(comment, rhs.comment).
                append(commentary, rhs.commentary).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(keyword).
                append(value).
                append(comment).
                append(commentary).
                toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(keyword).
                append(value).
                append(comment).
                append(commentary).
                build();
    }
}
