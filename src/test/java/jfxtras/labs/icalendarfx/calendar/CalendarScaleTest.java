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
        System.out.println(property.toContent());
        assertEquals(expectedContent, property.toContent());
        CalendarScale property2 = CalendarScale.parse(expectedContent);
        System.out.println(property2.toContent());
        assertEquals(property, property2);
    }
//    
//    @Test
//    public void canCopyCalendarScale()
//    {
//        CalendarScale property = new CalendarScale(CalendarScaleType.GREGORIAN);
//    }
}
