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

/**
 * An error handler provides call-backs used by this library to inform
 * the application of irregular events.
 */
public interface ErrorHandler {
    
    /**
     * Called if an unrecoverable error occurred.
     * <p>If the application throws an exception in this method
     * that exception will be thrown down the call-stack back to the
     * application. If no exception is thrown in this method then
     * the one provided to this method is thrown automatically afterwards.
     * Since the error is unrecoverable there is no way to avoid that
     * an exception will be thrown.</p>
     * @param exception Exception describing the error.
     * @throws IOException exception of the applications choice.
     * @throws NullPointerException if {@code exception} is {@code null}.
     */
    void fatal(FitsFormatException exception) throws IOException;
    
    /**
     * Called if a recoverable error occurred.
     * <p>If the application throws an exception in this method
     * that exception will be thrown down the call-stack back to the
     * application. If no exception is thrown in this method then
     * the library will finish the operation.</p>
     * @param exception Exception describing the error.
     * @throws IOException exception of the applications choice.
     * @throws NullPointerException if {@code exception} is {@code null}.
     */
    void error(FitsFormatException exception);
    
    /**
     * Called if a non-critical irregularity occurred.
     * <p>If the application throws an exception in this method
     * that exception will be thrown down the call-stack back to the
     * application. If no exception is thrown in this method then
     * the library will finish the operation.</p>
     * @param exception Exception describing the warning.
     * @throws IOException exception of the applications choice.
     * @throws NullPointerException if {@code exception} is {@code null}.
     */
    void warning(FitsFormatException exception);
    
}
