package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendarfx.parameters.ValueParameter;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

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
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap2()
    {
        String contentLine = "DESCRIPTION:";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(contentLine));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "");
        expectedMap.entrySet().stream().forEach(System.out::println);
        valueMap.entrySet().stream().forEach(System.out::println);
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap3()
    {
        String contentLine = ":";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(contentLine));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "");
        expectedMap.entrySet().stream().forEach(System.out::println);
        valueMap.entrySet().stream().forEach(System.out::println);
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap4()
    {
        String expectedContent = "SUMMARY:Department Party";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(expectedContent));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap5()
    {
        String s = "Department Party";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "Department Party");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap6()
    {
        String s = ";LANGUAGE=en:TEST SUMMARY";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY");
        expectedMap.put("LANGUAGE", "en");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canMakeParameterMap7()
    {
        String s = "LANGUAGE=en:TEST SUMMARY";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "TEST SUMMARY");
        expectedMap.put("LANGUAGE", "en");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canChangeValueType()
    {
        String content = "X-MYPROP:1";
        NonStandardProperty madeProperty = NonStandardProperty.parse(content);
        madeProperty.valueTypeProperty().set(new ValueParameter(ValueType.INTEGER));
        assertEquals(1, madeProperty.getValue());
    }
    
    @Test
    public void canEscapeSpecialCharacters()
    {
        String content = "the dog, frog; ran \\ far\naway\\;";
        String contentEscaped = "the dog\\, frog\\; ran \\\\ far\\naway\\\\\\;";
        String content2 = (String) ValueType.TEXT.getConverter().fromString(contentEscaped);
        String contentEscaped2 = ValueType.TEXT.getConverter().toString(content);
        assertEquals(content2, content);
        assertEquals(contentEscaped2, contentEscaped);
    }
    
    @Test // can handle lowercase in property and parameter name
    public void canHandleLowercase()
    {
        String content = "sUmmARY;lanGUAGE=en:TEST SUMMARY";
        Summary madeProperty = Summary.parse(content);
        assertEquals("SUMMARY;LANGUAGE=en:TEST SUMMARY", madeProperty.toContent());
        Summary expectedProperty = Summary.parse("TEST SUMMARY")
                .withLanguage("en");
        assertEquals(expectedProperty, madeProperty);
    }
}
