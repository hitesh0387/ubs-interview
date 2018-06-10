package com.ubs.opsit.interviews;

import org.junit.Assert;
import org.junit.Test;

public class TimeConverterImplTest {

    private TimeConverter timeConverter = new TimeConverterImpl();
    private static final String EMPTY_STRING = "";

    @Test
    public void testConvertTimeForNullValue() {
        String convertedTime = this.timeConverter.convertTime(null);
        Assert.assertEquals(EMPTY_STRING, convertedTime);
    }

    @Test
    public void testConvertTimeForEmptyValue() {
        String convertedTime = this.timeConverter.convertTime(EMPTY_STRING);
        Assert.assertEquals(EMPTY_STRING, convertedTime);
    }

    @Test
    public void testConvertTimeForInvalidTimeValue() {
        String convertedTime = this.timeConverter.convertTime("25:60:60");
        Assert.assertEquals(EMPTY_STRING, convertedTime);
    }

    @Test
    public void testConvertTimeWhenSecondsBitisOn() {
        String convertedTime = this.timeConverter.convertTime("13:00:00");
        String expectedOutput = "Y\r\nRROO\r\nRRRO\r\nOOOOOOOOOOO\r\nOOOO";
        Assert.assertEquals(expectedOutput, convertedTime);
    }
}
