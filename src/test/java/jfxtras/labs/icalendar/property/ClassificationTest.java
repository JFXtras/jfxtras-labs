package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Classification;

public class ClassificationTest
{
    @Test
    public void canParseClassification()
    {
        Classification madeProperty = new Classification("CLASS:PUBLIC");
        String expectedContent = "CLASS:PUBLIC";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
    
    @Test
    public void canParseClassification2()
    {
        Classification madeProperty = new Classification("CLASS:CUSTOM");
        String expectedContent = "CLASS:CUSTOM";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
}
