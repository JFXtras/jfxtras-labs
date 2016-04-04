package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Summary;

public class SummaryTest
{    
    @Test
    public void canParseSummary1()
    {
        String content = "SUMMARY:TEST SUMMARY";
        Summary madeProperty = new Summary(content);
        assertEquals(content, madeProperty.toContentLine());
        Summary expectedProperty = new Summary("TEST SUMMARY");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canParseSummary2()
    {
        String content = "SUMMARY;ALTREP=\"cid:part1.0001@example.org\";LANGUAGE=en:Department Party";
        Summary madeProperty = new Summary(content);
        assertEquals(content, madeProperty.toContentLine());
        Summary expectedProperty = new Summary("Department Party")
                .withAlternateTextRepresentation("cid:part1.0001@example.org")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
}
