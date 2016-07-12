package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.WeekStart;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class RecurrenceRuleTest
{    
    @Test
    public void canParseRRuleProperty()
    {
        String s = "RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2";
        SortedMap<String, String> valueMap = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s));
        SortedMap<String, String> expectedMap = new TreeMap<>();
        expectedMap.put(ICalendarUtilities.PROPERTY_VALUE_KEY, "FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2");
        assertEquals(expectedMap, valueMap);

        String s2 = "FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=2";
        SortedMap<String, String> valueMap2 = new TreeMap<>(ICalendarUtilities.propertyLineToParameterMap(s2));
        SortedMap<String, String> expectedMap2 = new TreeMap<>();
        expectedMap2.put("FREQ", "DAILY");
        expectedMap2.put("UNTIL", "20160417T235959Z");
        expectedMap2.put("INTERVAL", "2");
        assertEquals(expectedMap2, valueMap2);
    }
    
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;UNTIL=19730429T070000Z;BYMONTH=4;BYDAY=-1SU";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        assertEquals(content, madeProperty.toContent());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRule2()
                    .withFrequency(FrequencyType.YEARLY)
                    .withUntil("19730429T070000Z")
                    .withByRules(new ByMonth(Month.APRIL),
                                new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1))));
        assertEquals(content, madeProperty.toContent());
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test // different ordering of rule parts
    public void canParseRecurrenceRule2()
    {
        String content = "RRULE:BYDAY=-1SU;UNTIL=19730429T070000Z;BYMONTH=4;FREQ=YEARLY";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRule2()
                    .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1)))
                    .withUntil("19730429T070000Z")
                    .withByRules(new ByMonth(Month.APRIL))
                    .withFrequency(FrequencyType.YEARLY));
        assertEquals(content, madeProperty.toContent());
        assertEquals(expectedProperty, madeProperty);
    }
    
    @Test
    public void canCopyRecurrenceRule()
    {
        String content = "RRULE:UNTIL=19730429T070000Z;FREQ=YEARLY;BYMONTH=4;BYDAY=-1SU";
        RecurrenceRule r1 = RecurrenceRule.parse(content);
        RecurrenceRule r2 = new RecurrenceRule(r1);
        assertEquals(r1, r2);
        assertTrue(r1 != r2);
        assertTrue(r1.equals(r2));
        assertEquals(content, r2.toContent());
        assertTrue(r1.getValue() != r2.getValue());
    }
    
    /*
     * TEST RECURRENCE RULE ELEMENTS
     */
    
    @Test
    public void canParseUntil()
    {
        String content = "19730429T070000Z";
        Until element = Until.parse(content);
        ZonedDateTime t = ZonedDateTime.of(LocalDateTime.of(1973, 4, 29, 7, 0), ZoneId.of("Z"));
        assertEquals(t, element.getValue());
        assertEquals("UNTIL=19730429T070000Z", element.toContent());
    }
    
    @Test (expected = DateTimeException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchWrongUntil()
    {
        String content = "19730429T070000";
        Until.parse(content);
    }
    
    @Test
    public void canParseCount()
    {
        Count element = Count.parse("2");
        assertEquals((Integer) 2, element.getValue());
        assertEquals("COUNT=2", element.toContent());
    }
    
    @Test (expected = IllegalArgumentException.class)
    @Ignore // JUnit won't recognize exception - exception is thrown in listener is cause
    public void canCatchNegativeCount()
    {
        Count element = new Count(5);
        assertEquals((Integer) 5, element.getValue());
        element.setValue(0);
        assertEquals("COUNT=5", element.toContent());
    }
    
    @Test
    public void canParseInterval()
    {
        Interval element = Interval.parse("2");
        assertEquals((Integer) 2, element.getValue());
        assertEquals("INTERVAL=2", element.toContent());
    }

    @Test
    public void canParseWeekStart()
    {
        WeekStart element = new WeekStart().withValue("SU");
        assertEquals(DayOfWeek.SUNDAY, element.getValue());
        assertEquals("WKST=SU", element.toContent());
    }
    
    @Test
    public void canDetectErrors1()
    {
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRule2()
                    .withUntil("19730429T070000Z")
                    .withFrequency(FrequencyType.YEARLY)
                    .withByRules(new ByDay()));
        assertEquals(1, expectedProperty.errors().size());
    }
    
    @Test
    public void canDetectErrors2()
    {
        RecurrenceRule madeProperty = new RecurrenceRule(
                new RecurrenceRule2()
                .withUntil("19730429T070000Z")
                .withByRules(new ByMonth(Month.APRIL),
                            new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1))));
        assertEquals(1, madeProperty.errors().size());
    }
}
