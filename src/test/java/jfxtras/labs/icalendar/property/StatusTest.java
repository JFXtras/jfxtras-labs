package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Status;
import jfxtras.labs.icalendar.properties.component.descriptive.Status.StatusType;

public class StatusTest
{
    @Test
    public void canParseStatus()
    {
        String content = "STATUS:NEEDS-ACTION";
        Status madeProperty = new Status(content);
        assertEquals(content, madeProperty.toContentLine());
        Status expectedProperty = new Status("NEEDS-ACTION");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(madeProperty.getValue(), StatusType.NEEDS_ACTION);
    }
    
    @Test
    public void canParseStatus2()
    {
        String content = "STATUS:NON-STANDARD STATUS";
        Status madeProperty = new Status(content);
        assertEquals(content, madeProperty.toContentLine());
        Status expectedProperty = new Status("NON-STANDARD STATUS");
        assertEquals(expectedProperty, madeProperty);
        assertEquals(madeProperty.getValue(), StatusType.UNKNOWN);
    }
}
