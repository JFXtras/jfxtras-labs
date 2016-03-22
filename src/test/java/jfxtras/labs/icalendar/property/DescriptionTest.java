package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.descriptive.Description;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

public class DescriptionTest
{    
    @Test
    public void canParseDescriptionString()
    {
        String s = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\":Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management");
        expectedMap.put("ALTREP", "CID:part3.msg.970415T083000@example.com");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canParseDescription()
    {
        String contentLine = "DESCRIPTION;ALTREP=\"CID:part3.msg.970415T083000@example.com\";LANGUAGE=en:Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management";
        Description madeDescription = new Description(contentLine);
        Description expectedDescription = new Description()
                .withAlternateTextRepresentation("CID:part3.msg.970415T083000@example.com")
                .withLanguage("en")
                .withValue("Project XYZ Review Meeting will include the following agenda items: (a) Market Overview\\, (b) Finances\\, (c) Project Management");
        assertEquals(expectedDescription, madeDescription);
        assertEquals(contentLine, expectedDescription.toContentLine());
    }
}
