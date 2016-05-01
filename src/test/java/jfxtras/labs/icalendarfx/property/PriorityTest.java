package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Priority;

public class PriorityTest
{
    @Test
    public void canParseSequenceCount()
    {
        String expectedContent = "PRIORITY:5";
        Priority madeProperty = Priority.parse(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLines());
        assertEquals((Integer) 5, madeProperty.getValue());
    }
    
    @Test
    public void canParseSequenceCount2()
    {
        Priority madeProperty = new Priority(2);
        String expectedContent = "PRIORITY:2";
        assertEquals(expectedContent, madeProperty.toContentLines());
        assertEquals((Integer) 2, madeProperty.getValue());
    }
}
