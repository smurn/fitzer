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
import org.junit.Test;
import static org.smurn.fitzer.TestUtils.*;

/**
 * Unit tests for {@link HeaderValueConverter} implementations.
 */
public abstract class HeaderValueConverterTest {

    abstract HeaderValueConverter createTarget();

    @Test(expected = NullPointerException.class)
    public void compatibleEncodingCheck_Null() {
        HeaderValueConverter target = createTarget();
        target.compatibleEncodingCheck(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compatibleEncodingCheck_Length69() {
        HeaderValueConverter target = createTarget();
        target.compatibleEncodingCheck(toByte(repeat(" ", 69)));
    }

    @Test(expected = IllegalArgumentException.class)
    public void compatibleEncodingCheck_Length71() {
        HeaderValueConverter target = createTarget();
        target.compatibleEncodingCheck(toByte(repeat(" ", 71)));
    }

    @Test(expected = NullPointerException.class)
    public void decode_NullNull() throws IOException {
        HeaderValueConverter target = createTarget();;
        target.decode(null, 1000, null);
    }

    @Test(expected = NullPointerException.class)
    public void decode_Null() throws IOException {
        HeaderValueConverter target = createTarget();
        target.decode(null, 1000, THROW_ALWAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_Length69() throws IOException {
        HeaderValueConverter target = createTarget();
        target.decode(toByte(repeat(" ", 69)), 1000, THROW_ALWAYS);
    }

    @Test(expected = IllegalArgumentException.class)
    public void decode_Length71() throws IOException {
        HeaderValueConverter target = createTarget();
        target.decode(toByte(repeat(" ", 71)), 1000, THROW_ALWAYS);
    }

    @Test(expected = NullPointerException.class)
    public void encode_Null() throws IOException {
        HeaderValueConverter target = createTarget();
        target.decode(null, 1000, null);
    }
}
