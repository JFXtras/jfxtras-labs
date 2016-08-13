package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale.CalendarScaleType;

public class CalendarScaleTest
{
    @Test
    public void canParseCalendarScale()
    {
        CalendarScale property = new CalendarScale(CalendarScaleType.GREGORIAN);
        String expectedContent = "CALSCALE:GREGORIAN";
        assertEquals(expectedContent, property.toContent());
        CalendarScale property2 = CalendarScale.parse(expectedContent);
        assertEquals(property, property2);
    }
}
