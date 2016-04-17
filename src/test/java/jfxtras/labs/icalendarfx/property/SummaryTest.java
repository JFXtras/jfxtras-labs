package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;

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
        assertEquals("TEST SUMMARY", madeProperty.getValue());
    }
    
    @Test
    public void canParseSummary2()
    {
        String content = "SUMMARY;ALTREP=\"cid:part1.0001@example.org\";LANGUAGE=en:Department Party";
        Summary madeProperty = new Summary(content);
        assertEquals(content, madeProperty.toContentLine());
        Summary expectedProperty = new Summary("Department Party")
                .withAlternateText("cid:part1.0001@example.org")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test // can handle lowercase in property and parameter name
    public void canParseSummary3()
    {
        String content = "sUmmARY;lanGUAGE=en:TEST SUMMARY";
        Summary madeProperty = new Summary(content);
        assertEquals("SUMMARY;LANGUAGE=en:TEST SUMMARY", madeProperty.toContentLine());
        Summary expectedProperty = new Summary("TEST SUMMARY")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
}
