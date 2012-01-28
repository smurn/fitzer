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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a key-value pair of a FITS HDU header. <p>The key is always
 * represented as a single string. This class does not enforce a certain format
 * on the key to support conventions such as {@code HIERARCH} that extend the
 * set of legal keys.</p> <p>FITS supports several value types such as integers
 * or complex numbers again there are conventions that describe additional
 * types. This class stores the value as an {@code Object} to support any
 * possible data type.<br/> For convenience this class provides special methods
 * for the most commonly used types.</p> <p>The FITS standard discriminates
 * between properties with a value and commentary properties that, by
 * definition, have no value (the value is always set to {@code null}. Both
 * types can have a comment attached.</p> <p>Conventions are often using
 * comments to encode information in a way that allows backward compatibility.
 * For this reason both comments on regular, and especially comments of
 * commentary properties might store information beyond mere comments. When
 * loading or storing FITS files those comments may be interpreted if the
 * convention is supported. Thous the properties that the applications sees
 * might not map directly to the properties in the file.</p> <p>Only immutable
 * objects must be used as values.</p> <p>Instances of this class are
 * immutable.</p>
 */
public final class Property {

    private final String keyword;
    private final Object value;
    private final String comment;
    private final boolean commentary;


    public Property(String keyword){
        if (keyword == null){
            throw new NullPointerException();
        }
        this.keyword = keyword;
        this.value = null;
        this.comment = "";
        this.commentary = false;
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
