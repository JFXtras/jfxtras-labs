package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendar.ICalendarUtilities;
import jfxtras.labs.icalendar.properties.recurrence.RDate;

public class RDateTest
{    
    @Test
    public void canParseRDate1()
    {
        String s = "RDATE;VALUE=DATE:19970304,19970504,19970704,19970904";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970304,19970504,19970704,19970904");
        expectedMap.put("VALUE", "DATE");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canParseRDate2()
    {
        String s = "RDATE;TZID=America/New_York:19970714T083000";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19970714T083000");
        expectedMap.put("TZID", "America/New_York");
        assertEquals(expectedMap, valueMap);
    }
    
    // Can parsing period, but period not currently supported in streaming dates
    @Test
    public void canParseRDate3()
    {
        String s = "RDATE;VALUE=PERIOD:19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "19960403T020000Z/19960403T040000Z,19960404T010000Z/PT3H");
        expectedMap.put("VALUE", "PERIOD");
        assertEquals(expectedMap, valueMap);
    }
    
    @Test
    public void canConvertRDateToString()
    {
        RDate rDate = new RDate()
                .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0));
        assertEquals("20151112T100000,20151114T120000", rDate.toString()); // property name is added in enum
    }

    @Test
    public void canChageTemporalClassAfterClear()
    {
        RDate rDate = new RDate()
                .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDateTime.of(2015, 11, 14, 12, 0));
        rDate.getTemporals().clear();
        rDate.getTemporals().add(LocalDate.of(2015, 11, 14));
        rDate.getTemporals().add(LocalDate.of(2015, 11, 17));
        assertEquals(2, rDate.getTemporals().size());
    }
    
    /*
     * Error checking
     */
    @Test(expected=DateTimeException.class)
    @Ignore // Doesn't catch exception - wrong thread?
    public void canDetectTemporalClassChange()
    {
        RDate rDate = new RDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                     , LocalDate.of(2015, 11, 14));
        assertEquals(1, rDate.getTemporals().size());
    }    
}
