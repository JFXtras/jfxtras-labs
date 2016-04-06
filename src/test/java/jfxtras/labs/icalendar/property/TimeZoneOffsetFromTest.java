package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.ZoneOffset;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneOffsetFrom;

public class TimeZoneOffsetFromTest
{
    @Test
    public void canParseTimeZoneIdentifier()
    {
        String content = "TZOFFSETFROM:-0500";
        TimeZoneOffsetFrom madeProperty = new TimeZoneOffsetFrom(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneOffsetFrom expectedProperty = new TimeZoneOffsetFrom(ZoneOffset.of("-05:00"));
        assertEquals(expectedProperty, madeProperty);
    }
}
