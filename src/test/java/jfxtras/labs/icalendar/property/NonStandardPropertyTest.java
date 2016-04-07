package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.misc.NonStandardProperty;

public class NonStandardPropertyTest
{
    @Test
    public void canParseLocation()
    {
        String content = "X-MICROSOFT-CDO-ALLDAYEVENT:FALSE";
        NonStandardProperty madeProperty = new NonStandardProperty(content);
        assertEquals(content, madeProperty.toContentLine());
        NonStandardProperty expectedProperty = new NonStandardProperty("FALSE");
        assertEquals(expectedProperty, madeProperty);
    }
}
