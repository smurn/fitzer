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
import java.util.Formatter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Signals that the data read is not in the valid FITS format.
 */
public class FitsFormatException extends IOException {

    private final long offset;
    private final String messageKey;
    private final Object[] messageParameters;

    /**
     * Creates an instance.
     * @param offset    Position in the data stream giving an indication
     * where in the file the error was detected. The first byte in the file
     * has offset 0.
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
    public FitsFormatException(long offset, String messageKey,
            Object... messageParameters) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset must not be negative.");
        }
        if (messageKey == null) {
            throw new NullPointerException("messageKey must not be null.");
        }
        this.offset = offset;
        this.messageKey = messageKey;
        this.messageParameters = messageParameters;
        
        ResourceBundle messages = ResourceBundle.getBundle(
                "org.smurn.fitzer.messages", Locale.ROOT);
        Formatter formatter = new Formatter(Locale.ROOT);
        formatter.format(messages.getString(messageKey), messageParameters);
    }

    /**
     * Gets the position in the data stream giving an indication where in
     * the file the error wad detected. 
     * @return Position in the data stream. The first byte has position 0.
     */
    public long getOffset() {
        return offset;
    }

    /**
     * Gets the message describing the error in the given locale.
     * @param locale Locale in which to return the message.
     * @return Message in the given locale.
     */
    public String getLocalizedMessage(Locale locale) {
        ResourceBundle messages = ResourceBundle.getBundle(
                "org.smurn.fitzer.messages", locale);
        Formatter formatter = new Formatter(locale);
        formatter.format(messages.getString("FitsFormatException"), offset);
        formatter.format(messages.getString(messageKey), messageParameters);
        return formatter.toString();
    }

    /**
     * Gets the message describing the error in {@code Locale.ENGLISH}.
     * @return Error message in English.
     */
    @Override
    public String getMessage() {
        return getLocalizedMessage(Locale.ENGLISH);
    }

    /**
     * Gets the message describing the error in the current locale.
     * @return Error message in the current locale.
     */
    @Override
    public String getLocalizedMessage() {
        return getLocalizedMessage(Locale.getDefault());
    }
}
