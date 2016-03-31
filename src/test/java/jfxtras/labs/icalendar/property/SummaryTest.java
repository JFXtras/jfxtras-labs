package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.parameters.Language;
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
        Summary madeSummary = new Summary("SUMMARY:TEST SUMMARY");
        String expectedSummary = "SUMMARY:TEST SUMMARY";
        assertEquals(expectedSummary, madeSummary.toContentLine());
//        madeSummary.setValueType(ValueType.TEXT);
//        madeSummary.setValueType(ValueType.BOOLEAN);
//        madeSummary.setValueType(null);
        madeSummary.setLanguage((Language)null);
        madeSummary.setLanguage("en");
    }
    
    @Test
    public void canParseSummary2()
    {
        Summary madeSummary = new Summary("TEST SUMMARY2");
        String expectedSummary = "SUMMARY:TEST SUMMARY2";
        assertEquals(expectedSummary, madeSummary.toContentLine());
    }

}
