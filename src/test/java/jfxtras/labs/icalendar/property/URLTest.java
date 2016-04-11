package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.relationship.UniformResourceLocator;

public class URLTest
{
    @Test
    public void canParseUniformResourceLocator() throws URISyntaxException
    {
        String expectedContentLine = "URL:http://example.com/pub/calendars/jsmith/mytime.ics";
        UniformResourceLocator property = new UniformResourceLocator(expectedContentLine);
        String madeContentLine = property.toContentLine();
        assertEquals(expectedContentLine, madeContentLine);
        System.out.println(property.getValue().getClass());

        assertEquals(new URI("http://example.com/pub/calendars/jsmith/mytime.ics"), property.getValue());
    }
}
