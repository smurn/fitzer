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

/**
 * Signals that the data to be written into a FITS file is not compatible with
 * the FITS specification.
 */
public class FitsDataException extends FitsException {

    /**
     * Creates an instance.
     * @param messageKey Key of the error message within the {@code messages}
     * resource bundle. The stored string will be formatted with
     * {@link Formatter} to produce the final message.
     * @param messageParameters Parameters to of the error message. Passed to
     * {@link Formatter}.
     * @throws NullPointerException if {@code messageKey} is {@code null}.
     * @throws IllegalArgumentException if {@code offset} is negative.
     * @throws MissingResourceException if the message key does not exist in the
     * root locale.
     * @throws java.util.IllegalFormatException if formatting the message in the
     * root locale fails.
     */
    public FitsDataException(String messageKey, Object... messageParameters) {
        super(messageKey, messageParameters);
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
        FitsDataException rhs = (FitsDataException) obj;
        return new EqualsBuilder().appendSuper(super.equals(obj)).
                isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().appendSuper(super.hashCode()).
                toHashCode();
    }
}
