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
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for {@link Property}.
 */
public class PropertyTest {

    @Test(expected = NullPointerException.class)
    public void ctrKeyword_null() {
        new Property(null);
    }

    @Test
    public void ctrKeyword_empty() {
        new Property("");
    }

    @Test(expected = NullPointerException.class)
    public void ctrKeywordValue_nullNull() {
        new Property(null, null);
    }

    @Test
    public void ctrKeywordValue_emptyNull() {
        new Property("", null);
    }

    @Test(expected = NullPointerException.class)
    public void ctrKeywordValueComment_nullNullEmpty() {
        new Property(null, null, "");
    }

    @Test(expected = NullPointerException.class)
    public void ctrKeywordValueComment_emptyNullNull() {
        new Property("", null, null);
    }

    @Test
    public void ctrKeywordValueComment_emptyNullEmpty() {
        new Property("", null, "");
    }

    @Test
    public void ctrKeywordValueCommentCommentary_emptyEmptyEmptyFalse() {
        new Property("", "", "", false);
    }

    @Test
    public void ctrKeywordValueCommentCommentary_emptyNullEmptyTrue() {
        new Property("", null, "", true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ctrKeywordValueCommentCommentary_emptyEmptyEmptyTrue() {
        new Property("", "", "", true);
    }

    @Test
    public void getKeyword() {
        Property target = new Property("test");
        assertEquals("test", target.getKeyword());
    }

    @Test
    public void getComment() {
        Property target = new Property("", null, "test");
        assertEquals("test", target.getComment());
    }

    @Test
    public void isCommentary_True() {
        Property target = new Property("", null, "", true);
        assertTrue(target.isCommentary());
    }

    @Test
    public void isCommentary_False() {
        Property target = new Property("", null, "", false);
        assertFalse(target.isCommentary());
    }

    @Test
    public void getValue_Object() {
        Object obj = new Object();
        Property target = new Property("", obj);
        assertEquals(obj, target.getValue());
    }

    @Test
    public void getValue_Null() {
        Property target = new Property("", null);
        assertNull(target.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Null() {
        Property target = new Property("", null);
        target.getIntValue();
    }

    @Test
    public void getIntValue_Byte() {
        Property target = new Property("", (byte) -42);
        assertEquals(-42, target.getIntValue());
    }

    @Test
    public void getIntValue_Short() {
        Property target = new Property("", (short) -42);
        assertEquals(-42, target.getIntValue());
    }

    @Test
    public void getIntValue_Int() {
        Property target = new Property("", -42);
        assertEquals(-42, target.getIntValue());
    }

    @Test
    public void getIntValue_Long_Min() {
        Property target = new Property("", (long) Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, target.getIntValue());
    }

    @Test
    public void getIntValue_Long_Max() {
        Property target = new Property("", (long) Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, target.getIntValue());
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Long_Underflow() {
        Property target = new Property("", (long) Integer.MIN_VALUE - 1L);
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Long_Overflow() {
        Property target = new Property("", (long) Integer.MAX_VALUE + 1L);
        target.getIntValue();
    }

    @Test
    public void getIntValue_BigInteger_Min() {
        Property target = new Property("",
                BigInteger.valueOf(Integer.MIN_VALUE));
        assertEquals(Integer.MIN_VALUE, target.getIntValue());
    }

    @Test
    public void getIntValue_BigInteger_Max() {
        Property target = new Property("",
                BigInteger.valueOf(Integer.MAX_VALUE));
        assertEquals(Integer.MAX_VALUE, target.getIntValue());
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_BigInteger_Underflow() {
        Property target = new Property("",
                BigInteger.valueOf(Integer.MIN_VALUE - 1L));
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_BigInteger_Overflow() {
        Property target = new Property("",
                BigInteger.valueOf(Integer.MAX_VALUE + 1L));
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Float() {
        Property target = new Property("", 4.2f);
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Double() {
        Property target = new Property("", 4.2);
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_BigDecimal() {
        Property target = new Property("", BigDecimal.ONE);
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getIntValue_Object() {
        Property target = new Property("", new Object());
        target.getIntValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_Null() {
        Property target = new Property("", null);
        target.getLongValue();
    }

    @Test
    public void getLongValue_Byte() {
        Property target = new Property("", (byte) -42);
        assertEquals(-42L, target.getLongValue());
    }

    @Test
    public void getLongValue_Short() {
        Property target = new Property("", (short) -42);
        assertEquals(-42L, target.getLongValue());
    }

    @Test
    public void getLongValue_Int() {
        Property target = new Property("", -42);
        assertEquals(-42L, target.getLongValue());
    }

    @Test
    public void getLongValue_Long() {
        Property target = new Property("", -42L);
        assertEquals(-42L, target.getLongValue());
    }

    @Test
    public void getLongValue_BigInteger_Min() {
        Property target = new Property("",
                BigInteger.valueOf(Long.MIN_VALUE));
        assertEquals(Long.MIN_VALUE, target.getLongValue());
    }

    @Test
    public void getLongValue_BigInteger_Max() {
        Property target = new Property("",
                BigInteger.valueOf(Long.MAX_VALUE));
        assertEquals(Long.MAX_VALUE, target.getLongValue());
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_BigInteger_Underflow() {
        Property target = new Property("",
                BigInteger.valueOf(Long.MIN_VALUE).subtract(BigInteger.ONE));
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_BigInteger_Overflow() {
        Property target = new Property("",
                BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE));
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_Float() {
        Property target = new Property("", 4.2f);
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_Double() {
        Property target = new Property("", 4.2);
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_BigDecimal() {
        Property target = new Property("", BigDecimal.ONE);
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getLongValue_Object() {
        Property target = new Property("", new Object());
        target.getLongValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_Null() {
        Property target = new Property("", null);
        target.getFloatValue();
    }

    @Test
    public void getFloatValue_Byte() {
        Property target = new Property("", (byte) -42);
        assertEquals(-42.0f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Short() {
        Property target = new Property("", (short) -42);
        assertEquals(-42.0f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Int() {
        Property target = new Property("", (int) -42);
        assertEquals(-42.0f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Long() {
        Property target = new Property("", (long) -42);
        assertEquals(-42.0f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_BigInteger() {
        Property target = new Property("", BigInteger.valueOf(-42));
        assertEquals(-42.0f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_BigInteger_Max() {
        // The largest 32bit-float is
        // max = 2^127 * (1 + (2^23-1)/2^23)
        // But since java uses IEEE-754 'roundTiesToEven' the largest
        // allowed integer is the one that is rounded down to that number.
        // We basically add a 'half' leat-significant-bit' to find the
        // true limiting number: limit = max + 2^(127-24)
        // Since the LSB of max is odd, this will be rounded up. Therefore
        // only numbers smaller than that are convertable.

        BigInteger biggestConvertible = new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "111" // 103 ones
                , 2);

        Property target = new Property("", biggestConvertible);
        assertEquals(Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_BigInteger_Overflow() {
        BigInteger limit = new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 103 zeros)
                , 2);
        Property target = new Property("", limit);
        target.getFloatValue();
    }

    @Test
    public void getFloatValue_BigInteger_Min() {
        // The largest 32bit-float is
        // max = 2^127 * (1 + (2^23-1)/2^23)
        // But since java uses IEEE-754 'roundTiesToEven' the largest
        // allowed integer is the one that is rounded down to that number.
        // We basically add a 'half' leat-significant-bit' to find the
        // true limiting number: limit = max + 2^(127-24)
        // Since the LSB of max is odd, this will be rounded up. Therefore
        // only numbers smaller than that are convertable.

        BigInteger smallest = new BigInteger(
                "-"
                + "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "111" // 103 ones
                , 2);

        Property target = new Property("", smallest);
        assertEquals(-Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_BigInteger_Underflow() {
        BigInteger limit = new BigInteger(
                "-"
                + "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 103 zeros)
                , 2);
        Property target = new Property("", limit);
        target.getFloatValue();
    }

    @Test
    public void getFloatValue_Float() {
        Property target = new Property("", -4.2f);
        assertEquals(-4.2f, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Float_PosInf() {
        Property target = new Property("", Float.POSITIVE_INFINITY);
        assertEquals(Float.POSITIVE_INFINITY, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Float_NegInf() {
        Property target = new Property("", Float.NEGATIVE_INFINITY);
        assertEquals(Float.NEGATIVE_INFINITY, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Float_NaN() {
        Property target = new Property("", Float.NaN);
        assertEquals(Float.NaN, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Double() {
        Property target = new Property("", -4.5);
        assertEquals(-4.5f, target.getFloatValue(), 0.0f);
    }
    
    @Test
    public void getFloatValue_Double_Max(){
        BigInteger largest = new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111100" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 28 ones, 75 zeros
                , 2);

        Property target = new Property("", largest.doubleValue());
        assertEquals(Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }
    
    @Test(expected=IllegalStateException.class)
    public void getFloatValue_Double_Overflow(){
        BigInteger largest = new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 28 ones, 75 zeros
                , 2);
        Property target = new Property("", largest.doubleValue());
        target.getFloatValue();
    }
    
    @Test
    public void getFloatValue_Double_Min(){
        BigInteger largest = new BigInteger(
                "-1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111100" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 28 ones, 75 zeros
                , 2);
        Property target = new Property("", largest.doubleValue());
        assertEquals(-Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }
    
    @Test(expected=IllegalStateException.class)
    public void getFloatValue_Double_Underflow(){
        BigInteger largest = new BigInteger(
                "-1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 28 ones, 75 zeros
                , 2);
        Property target = new Property("", largest.doubleValue());
        target.getFloatValue();
    }

    @Test
    public void getFloatValue_Double_PosInf() {
        Property target = new Property("", Double.POSITIVE_INFINITY);
        assertEquals(Float.POSITIVE_INFINITY, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Double_NegInf() {
        Property target = new Property("", Double.NEGATIVE_INFINITY);
        assertEquals(Float.NEGATIVE_INFINITY, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_Double_NaN() {
        Property target = new Property("", Double.NaN);
        assertEquals(Float.NaN, target.getFloatValue(), 0.0f);
    }

    @Test
    public void getFloatValue_BigDecimal_Max() {

        BigDecimal largest = new BigDecimal(new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "111" // 103 ones
                , 2));

        // actually even smaller values are possible, as we can
        // add as many '1' behind the decimal as we like.

        Property target = new Property("", largest);
        assertEquals(Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_BigDecimal_Overflow() {
        BigDecimal limit = new BigDecimal(new BigInteger(
                "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 103 zeros)
                , 2));

        Property target = new Property("", limit);
        target.getFloatValue();
    }

    @Test
    public void getFloatValue_BigDecimal_Min() {

        BigDecimal smallest = new BigDecimal(new BigInteger(
                "-"
                + "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "0" // the 'half' bit
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "1111111111" + "1111111111"
                + "1111111111" + "1111111111" + "111" // 103 ones
                , 2));

        // actually even smaller values are possible, as we can
        // add as many '1' behind the decimal as we like.

        Property target = new Property("", smallest);
        assertEquals(-Float.MAX_VALUE, target.getFloatValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_BigDecimal_Underflow() {
        BigDecimal limit = new BigDecimal(new BigInteger(
                "-"
                + "1" // one in front of the fraction
                + "1111111111" + "1111111111" + "111" // 23 fractional bits
                + "1" // the 'half' bit
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "0000000000" + "0000000000"
                + "0000000000" + "0000000000" + "000" // 103 zeros)
                , 2));

        Property target = new Property("", limit);
        target.getFloatValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getFloatValue_Object() {
        Property target = new Property("", new Object());
        target.getFloatValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_Null() {
        Property target = new Property("", null);
        target.getDoubleValue();
    }

    @Test
    public void getDoubleValue_Byte() {
        Property target = new Property("", (byte) -42);
        assertEquals(-42.0, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_Short() {
        Property target = new Property("", (short) -42);
        assertEquals(-42.0, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_Int() {
        Property target = new Property("", (int) -42);
        assertEquals(-42.0, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_Long() {
        Property target = new Property("", (long) -42);
        assertEquals(-42.0, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_BigInteger() {
        Property target = new Property("", BigInteger.valueOf(-42));
        assertEquals(-42.0, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_BigInteger_Max() {
        // The largest 32bit-float is
        // max = 2^127 * (1 + (2^23-1)/2^23)
        // But since java uses IEEE-754 'roundTiesToEven' the largest
        // allowed integer is the one that is rounded down to that number.
        // We basically have to add a 'half' leat-significant-bit to find the
        // true limiting number: limit = max + 2^(127-24)
        // Since the LSB of max is odd, this will be rounded up. Therefore
        // only numbers smaller than that are convertable.

        BigInteger biggestConvertible = new BigInteger(
                "1"
                + repeat("1", 52)
                + "0" // the 'half' bit
                + repeat("1", 970), 2);

        Property target = new Property("", biggestConvertible);
        assertEquals(Double.MAX_VALUE, target.getDoubleValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_BigInteger_Overflow() {
        BigInteger limit = new BigInteger(
                "1"
                + repeat("1", 52)
                + "1" // the 'half' bit
                + repeat("0", 970), 2);
        Property target = new Property("", limit);
        target.getDoubleValue();
    }

    @Test
    public void getDoubleValue_BigInteger_Min() {
        // The largest 32bit-float is
        // max = 2^127 * (1 + (2^23-1)/2^23)
        // But since java uses IEEE-754 'roundTiesToEven' the largest
        // allowed integer is the one that is rounded down to that number.
        // We basically add a 'half' leat-significant-bit' to find the
        // true limiting number: limit = max + 2^(127-24)
        // Since the LSB of max is odd, this will be rounded up. Therefore
        // only numbers smaller than that are convertable.

        BigInteger smallest = new BigInteger(
                "-"
                + "1"
                + repeat("1", 52)
                + "0" // the 'half' bit
                + repeat("1", 970), 2);

        Property target = new Property("", smallest);
        assertEquals(-Double.MAX_VALUE, target.getDoubleValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_BigInteger_Underflow() {
        BigInteger limit = new BigInteger(
                "-"
                + "1"
                + repeat("1", 52)
                + "1" // the 'half' bit
                + repeat("0", 970), 2);
        Property target = new Property("", limit);
        target.getDoubleValue();
    }

    @Test
    public void getDoubleValue_Float() {
        Property target = new Property("", -4.5f);
        assertEquals(-4.5, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_Float_PosInf() {
        Property target = new Property("", Float.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_Float_NegInf() {
        Property target = new Property("", Float.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_Float_NaN() {
        Property target = new Property("", Float.NaN);
        assertEquals(Double.NaN, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_Double() {
        Property target = new Property("", -4.2);
        assertEquals(-4.2, target.getDoubleValue(), 0.0);
    }

    @Test
    public void getDoubleValue_Double_PosInf() {
        Property target = new Property("", Double.POSITIVE_INFINITY);
        assertEquals(Double.POSITIVE_INFINITY, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_Double_NegInf() {
        Property target = new Property("", Double.NEGATIVE_INFINITY);
        assertEquals(Double.NEGATIVE_INFINITY, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_Double_NaN() {
        Property target = new Property("", Double.NaN);
        assertEquals(Double.NaN, target.getDoubleValue(), 0.0f);
    }

    @Test
    public void getDoubleValue_BigDecimal_Max() {

        BigDecimal largest = new BigDecimal(new BigInteger(
                "1"
                + repeat("1", 52)
                + "0" // the 'half' bit
                + repeat("1", 970), 2));

        // actually even smaller values are possible, as we can
        // add as many '1' behind the decimal as we like.

        Property target = new Property("", largest);
        assertEquals(Double.MAX_VALUE, target.getDoubleValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_BigDecimal_Overflow() {
        BigDecimal limit = new BigDecimal(new BigInteger(
                "1"
                + repeat("1", 52)
                + "1" // the 'half' bit
                + repeat("0", 970), 2));

        Property target = new Property("", limit);
        target.getDoubleValue();
    }

    @Test
    public void getDoubleValue_BigDecimal_Min() {

        BigDecimal smallest = new BigDecimal(new BigInteger(
                "-"
                + "1"
                + repeat("1", 52)
                + "0" // the 'half' bit
                + repeat("1", 970), 2));

        // actually even smaller values are possible, as we can
        // add as many '1' behind the decimal as we like.

        Property target = new Property("", smallest);
        assertEquals(-Double.MAX_VALUE, target.getDoubleValue(), 0.0f);
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_BigDecimal_Underflow() {
        BigDecimal limit = new BigDecimal(new BigInteger(
                "-"
                + "1"
                + repeat("1", 52)
                + "1" // the 'half' bit
                + repeat("0", 970), 2));

        Property target = new Property("", limit);
        target.getDoubleValue();
    }

    @Test(expected = IllegalStateException.class)
    public void getDoubleValue_Object() {
        Property target = new Property("", new Object());
        target.getDoubleValue();
    }

    @Test
    public void getStringValue_Null() {
        Property target = new Property("", null);
        assertNull(target.getStringValue());
    }

    @Test
    public void getStringValue_String() {
        Property target = new Property("", "test");
        assertEquals("test", target.getStringValue());
    }

    @Test(expected = IllegalStateException.class)
    public void getStringValue_Object() {
        Property target = new Property("", new Object());
        target.getStringValue();
    }

    @Test
    public void withValue_Null() {
        Property target = new Property("", new Object());
        target = target.withValue(null);
        assertNull(target.getValue());
    }

    @Test
    public void withValue_Object() {
        Object obj = new Object();
        Property target = new Property("", null);
        target = target.withValue(obj);
        assertSame(obj, target.getValue());
    }

    @Test
    public void withValue_Int() {
        Property target = new Property("", null);
        target = target.withValue(10);
        assertEquals(Integer.valueOf(10), target.getValue());
    }

    @Test
    public void withValue_Long() {
        Property target = new Property("", null);
        target = target.withValue(10L);
        assertEquals(Long.valueOf(10L), target.getValue());
    }

    @Test
    public void withValue_Float() {
        Property target = new Property("", null);
        target = target.withValue(10.10f);
        assertEquals(Float.valueOf(10.10f), target.getValue());
    }

    @Test
    public void withValue_Double() {
        Property target = new Property("", null);
        target = target.withValue(10.10);
        assertEquals(Double.valueOf(10.10), target.getValue());
    }

    @Test
    public void withValue_String() {
        Property target = new Property("", null);
        target = target.withValue("test");
        assertEquals("test", target.getValue());
    }

    @Test
    public void withValue_String_Null() {
        Property target = new Property("", null);
        target = target.withValue((String) null);
        assertNull(target.getValue());
    }

    @Test
    public void withComment_String() {
        Property target = new Property("", null);
        target = target.withComment("test");
        assertEquals("test", target.getComment());
    }

    @Test(expected = NullPointerException.class)
    public void withComment_Null() {
        Property target = new Property("", null);
        target.withComment(null);
    }

    @Test
    public void withCommentary_True() {
        Property target = new Property("", null, "", false);
        target = target.withCommentary(true);
        assertTrue(target.isCommentary());
    }

    @Test
    public void withCommentary_False() {
        Property target = new Property("", null, "", true);
        target = target.withCommentary(false);
        assertFalse(target.isCommentary());
    }

    @Test(expected = IllegalStateException.class)
    public void withCommentary_Fails() {
        Property target = new Property("", "", "", false);
        target.withCommentary(true);
    }

    /**
     * Helper method that repeats a string a given times.
     * @param pattern String to repeat.
     * @param times How often the string should be self-concatenated.
     * @return The strings concatenated to it-self the given number of times.
     */
    private static String repeat(String pattern, int times) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < times; i++) {
            str.append(pattern);
        }
        return str.toString();
    }
}
