package jfxtras.labs.icalendarfx.parameter;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Daily;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Monthly;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;

public class RecurrenceRuleParameterTest
{
    /** tests parsing FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RecurrenceRule2 rRule = new RecurrenceRule2(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(new Yearly()
                        .withInterval(2)
                        .withByRules(new ByMonth(Month.JANUARY), new ByDay(DayOfWeek.SUNDAY)));
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RecurrenceRule2 rRule = new RecurrenceRule2(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(new Monthly()
                        .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13)));

        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RecurrenceRule2 rRule = new RecurrenceRule2(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withFrequency(new Yearly()
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)), new ByWeekNumber(20)));
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z";
        RecurrenceRule2 rRule = new RecurrenceRule2(s);
        RecurrenceRule2 expectedRRule = new RecurrenceRule2()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0),ZoneId.of("Z")))
                .withFrequency(new Daily()
                        .withInterval(2));
        assertEquals(expectedRRule, rRule);
    }
}
