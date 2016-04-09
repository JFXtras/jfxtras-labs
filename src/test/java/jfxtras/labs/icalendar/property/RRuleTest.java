package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.RecurrenceImpl;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Daily;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Monthly;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Yearly;
import jfxtras.labs.icalendar.utilities.ICalendarUtilities;

@Deprecated // many of these tests can probably be recycled
public class RRuleTest
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
    
    /** tests parsing RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RecurrenceImpl rRule = new RecurrenceImpl(s);
        RecurrenceImpl expectedRRule = new RecurrenceImpl()
                .withFrequency(new Yearly()
                        .withInterval(2)
                        .withByRules(new ByMonth(Month.JANUARY), new ByDay(DayOfWeek.SUNDAY)));
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "RRULE:FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RecurrenceImpl rRule = new RecurrenceImpl(s);
        RecurrenceImpl expectedRRule = new RecurrenceImpl()
                .withFrequency(new Monthly()
                        .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13)));

        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "RRULE:FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RecurrenceImpl rRule = new RecurrenceImpl(s);
        RecurrenceImpl expectedRRule = new RecurrenceImpl()
                .withFrequency(new Yearly()
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)), new ByWeekNumber(20)));
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z";
        RecurrenceImpl rRule = new RecurrenceImpl(s);
        RecurrenceImpl expectedRRule = new RecurrenceImpl()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0),ZoneId.of("Z")))
                .withFrequency(new Daily()
                        .withInterval(2));
        System.out.println(rRule);
        assertEquals(expectedRRule, rRule);
    }
}
