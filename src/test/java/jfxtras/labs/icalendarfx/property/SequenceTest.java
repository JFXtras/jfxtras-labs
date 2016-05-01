package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.change.Sequence;

public class SequenceTest
{
    @Test
    public void canParseSequenceCount()
    {
        String expectedContent = "SEQUENCE:0";
        Sequence madeProperty = Sequence.parse(expectedContent);
        assertEquals(expectedContent, madeProperty.toContentLines());
        assertEquals((Integer) 0, madeProperty.getValue());
    }
    
    @Test
    public void canParseSequenceCount2()
    {
        Sequence madeProperty = new Sequence(2);
        String expectedContent = "SEQUENCE:2";
        assertEquals(expectedContent, madeProperty.toContentLines());
        assertEquals((Integer) 2, madeProperty.getValue());
    }
}
