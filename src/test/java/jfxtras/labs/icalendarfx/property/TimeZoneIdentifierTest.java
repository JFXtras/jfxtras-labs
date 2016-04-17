package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.time.ZoneId;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.timezone.TimeZoneIdentifier;

public class TimeZoneIdentifierTest
{
    @Test
    public void canParseTimeZoneIdentifier1()
    {
        String content = "TZID:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = new TimeZoneIdentifier(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"));
//        Class<? extends ZoneId> c = ZoneId.of("America/Los_Angeles").getClass();
//        System.out.println(ZoneId.class.isAssignableFrom(c));
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseTimeZoneIdentifier2()
    {
        String content = "TZID;VALUE=TEXT:America/Los_Angeles";
        TimeZoneIdentifier madeProperty = new TimeZoneIdentifier(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier(ZoneId.of("America/Los_Angeles"))
                .withValueParameter(ValueType.TEXT);
        assertEquals(expectedProperty, madeProperty);
    }

    @Test
    public void canParseTimeZoneIdentifier3()
    {
        String content = "TZID:/US-New_York-New_York";
        TimeZoneIdentifier madeProperty = new TimeZoneIdentifier(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeZoneIdentifier expectedProperty = new TimeZoneIdentifier("/US-New_York-New_York");
        assertEquals(expectedProperty, madeProperty);
        assertNull(expectedProperty.getValue());
    }
}
