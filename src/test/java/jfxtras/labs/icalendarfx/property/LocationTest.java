package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;

public class LocationTest
{
    @Test
    public void canParseLocation()
    {
        String content = "LOCATION:Conference Room - F123\\, Bldg. 002";
        Location madeProperty = Location.parse(content);
        assertEquals(content, madeProperty.toContentLine());
        Location expectedProperty = Location.parse("Conference Room - F123\\, Bldg. 002");
        assertEquals(expectedProperty, madeProperty);
        assertEquals("Conference Room - F123, Bldg. 002", madeProperty.getValue());
    }
    
    @Test
    public void canParseLocation2() throws URISyntaxException
    {
        String content = "LOCATION;ALTREP=\"http://xyzcorp.com/conf-rooms/f123.vcf\";LANGUAGE=en-US:Conference Room - F123\\, Bldg. 00";
        Location madeProperty = Location.parse(content);
        assertEquals(content, madeProperty.toContentLine());
        Location expectedProperty = Location.parse("Conference Room - F123\\, Bldg. 00")
                .withAlternateText(new URI("http://xyzcorp.com/conf-rooms/f123.vcf"))
                .withLanguage("en-US");
        assertEquals(expectedProperty, madeProperty);
    }
}
