package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

public class GeneralPropertyTest
{
    @Test
    public void canMakeParameterMap()
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(contentLine));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management");
        expectedMap.put("ALTREP", "\"CID:part3.msg.970415T083000@example.com\"");
        expectedMap.put("LANGUAGE", "en");
        expectedMap.entrySet().stream().forEach(System.out::println);
        valueMap.entrySet().stream().forEach(System.out::println);
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canParseSummaryProperty()
    {
        String expectedContent = "SUMMARY:Department Party";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(expectedContent));
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
}
