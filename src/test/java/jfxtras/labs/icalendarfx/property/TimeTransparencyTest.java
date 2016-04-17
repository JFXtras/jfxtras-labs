package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency;
import jfxtras.labs.icalendarfx.properties.component.time.TimeTransparency.TransparencyType;

public class TimeTransparencyTest
{
    @Test
    public void canParseStatus()
    {
        String content = "TRANSP:TRANSPARENT";
        TimeTransparency madeProperty = new TimeTransparency(content);
        assertEquals(content, madeProperty.toContentLine());
        TimeTransparency expectedProperty = new TimeTransparency(TransparencyType.TRANSPARENT);
        assertEquals(expectedProperty, madeProperty);
        assertEquals(TransparencyType.TRANSPARENT, madeProperty.getValue());
    }
    
    @Test
    public void canParseStatus2()
    {
        String content = "TRANSP:TRANSPARENT2";
        TimeTransparency madeProperty = new TimeTransparency(content);
        assertEquals(content, madeProperty.toContentLine());
//        TimeTransparency expectedProperty = new TimeTransparency(TransparencyType.TRANSPARENT);
//        assertEquals(expectedProperty, madeProperty);
        assertEquals(TransparencyType.UNKNOWN, madeProperty.getValue());
    }
}
