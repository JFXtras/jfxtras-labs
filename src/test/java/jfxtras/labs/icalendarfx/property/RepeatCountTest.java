package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.alarm.RepeatCount;

public class RepeatCountTest
{
    @Test
    public void canParseRepeatCount()
    {
        String expectedContent = "REPEAT:0";
        RepeatCount madeProperty = new RepeatCount(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals((Integer) 0, madeProperty.getValue());
    }
    
    @Test
    public void canParseRepeatCount2()
    {
        RepeatCount madeProperty = new RepeatCount(2);
        String expectedContent = "REPEAT:2";
        assertEquals(expectedContent, madeProperty.toContentLine());
        assertEquals((Integer) 2, madeProperty.getValue());
    }
}
