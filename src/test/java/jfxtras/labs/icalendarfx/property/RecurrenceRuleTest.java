package jfxtras.labs.icalendarfx.property;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleElement;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class RecurrenceRuleTest
{
    @Test
    public void canParseRecurrenceRule()
    {
        String content = "RRULE:FREQ=YEARLY;BYMONTH=1,2";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        assertEquals(content, madeProperty.toContent());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRuleElement()
                .withFrequency(new Yearly()
                        .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
        assertEquals(expectedProperty, madeProperty);
    }
    
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
    public void canParseRecurrenceRule2()
    {
        String content = "RRULE:FREQ=YEARLY;UNTIL=19730429T070000Z;BYMONTH=4;BYDAY=-1SU";
        RecurrenceRule madeProperty = RecurrenceRule.parse(content);
        assertEquals(content, madeProperty.toContent());
        RecurrenceRule expectedProperty = new RecurrenceRule(
                new RecurrenceRuleElement()
                    .withUntil("19730429T070000Z")
                    .withFrequency(new Yearly()
                            .withByRules(new ByMonth(Month.APRIL),
                                    new ByDay(new ByDay.ByDayPair(DayOfWeek.SUNDAY, -1)))));
        assertEquals(expectedProperty, madeProperty);
    }
}
