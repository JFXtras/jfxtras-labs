package jfxtras.labs.icalendarfx.parameter.rrule;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;

public class ByYearDayTest
{
    @Test
    public void canParseByYearDay()
    {
        ByYearDay element = new ByYearDay(100,200,300);
        assertEquals(Arrays.asList(100,200,300), element.getValue());
        assertEquals("BYYEARDAY=100,200,300", element.toContent());
    }
}
