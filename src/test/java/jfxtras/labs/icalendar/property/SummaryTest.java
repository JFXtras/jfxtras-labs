package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Summary;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

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
    public void canParseSummaryProperty2()
    {
        String s = "Department Party";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party");
        assertEquals(expectedMap, valueMap);
    }
 
    @Test
    public void canParseSummary()
    {
        Summary madeProperty = new Summary("SUMMARY:TEST SUMMARY");
        String expectedContent = "SUMMARY:TEST SUMMARY";
        assertEquals(expectedContent, madeProperty.toContentLine());
//        madeProperty.setLanguage((Language)null); - for testing change listeners - TODO 
//        madeProperty.setLanguage("en");
    }
    
    @Test
    public void canParseSummary2()
    {
        Summary madeProperty = new Summary("TEST SUMMARY2");
        String expectedContent = "SUMMARY:TEST SUMMARY2";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }
    
    @Test // Tests unknown value type
    public void canParseSummary3()
    {
        Summary madeProperty = new Summary("SUMMARY;VALUE=HOT:TEST SUMMARY");
        String expectedContent = "SUMMARY;VALUE=HOT:TEST SUMMARY";
        assertEquals(expectedContent, madeProperty.toContentLine());
    }

}
