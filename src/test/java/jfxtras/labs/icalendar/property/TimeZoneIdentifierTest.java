package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.ZoneId;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.timezone.TimeZoneIdentifier;

public class TimeZoneIdentifierTest
{
    @Test
    public void canParseTimeZoneIdentifier()
    {
        String content = "TZID:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = new TimeZoneIdentifier(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"));
        assertEquals(expectedProperty, madeProperty);
    }

    @Test
    public void canParseTimeZoneIdentifier2()
    {
        String content = "TZID:/example.org/America/New_York";
        TimeZoneIdentifier madeProperty = new TimeZoneIdentifier(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("/example.org/America/New_York"));
        assertEquals(expectedProperty, madeProperty);
    }
}
