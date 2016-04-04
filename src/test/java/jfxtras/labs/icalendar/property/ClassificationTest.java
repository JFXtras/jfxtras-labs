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
        System.out.println(madeProperty.toContentLine());
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
}
