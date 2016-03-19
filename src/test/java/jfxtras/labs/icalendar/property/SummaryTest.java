package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities;
import jfxtras.labs.icalendar.properties.descriptive.Summary;

public class SummaryTest
{
    @Test
    public void canParseSummaryProperty()
    {
        String s = "SUMMARY:Department Party";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party");
        assertEquals(expectedMap, valueMap);
    }
 
    @Test
    public void canParseSummary()
    {
        Summary madeSummary = new Summary("SUMMARY:TEST SUMMARY");
        String expectedSummary = "SUMMARY:TEST SUMMARY";
        assertEquals(expectedSummary, madeSummary.toContentLine());
    }
    

}
