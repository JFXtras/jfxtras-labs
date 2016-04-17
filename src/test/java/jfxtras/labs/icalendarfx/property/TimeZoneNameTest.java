package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneName;

public class TimeZoneNameTest
{
    @Test
    public void canParseTimeZoneName()
    {
        String content = "TZNAME;LANGUAGE=fr-CA:HNE";
        TimeZoneName madeProperty = new TimeZoneName(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneName expectedProperty = new TimeZoneName("HNE")
                .withLanguage("fr-CA");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("HNE", madeProperty.getValue());
        assertEquals("fr-CA", madeProperty.getLanguage().getValue());
    }
}
