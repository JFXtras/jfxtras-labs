package jfxtras.labs.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;

public class RecurrenceRuleParseTest
{
    /** tests parsing FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        RecurrenceRule3 expectedRRule = new RecurrenceRule3()
                .withFrequency(FrequencyType.YEARLY)
                .withInterval(2)
                .withByRules(new ByMonth(Month.JANUARY), new ByDay(DayOfWeek.SUNDAY));
        rRule.elements().stream().forEach(System.out::println);
        System.out.println(rRule.toContent());
        assertEquals(s, expectedRRule.toContent());
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        RecurrenceRule3 expectedRRule = new RecurrenceRule3()
                .withFrequency(FrequencyType.MONTHLY)
                .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13));
//        System.out.println(rRule.elements().get(0).getElement(rRule));
//        System.out.println(expectedRRule.elements().get(0).getElement(expectedRRule));
//        System.out.println(expectedRRule.getFrequency().toContent());
//        rRule.elements().stream().forEach(System.out::println);
        assertEquals(s, expectedRRule.toContent());
        assertEquals(s, rRule.toContent());
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        RecurrenceRule3 expectedRRule = new RecurrenceRule3()
                .withFrequency(FrequencyType.YEARLY)
                .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)), new ByWeekNumber(20));
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z";
        RecurrenceRule3 rRule = RecurrenceRule3.parse(s);
        RecurrenceRule3 expectedRRule = new RecurrenceRule3()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0),ZoneId.of("Z")))
                .withFrequency(FrequencyType.DAILY)
                .withInterval(2);
        assertEquals(expectedRRule, rRule);
    }
    
//    @Test
//    public void canParseRRule5()
//    {
//        RecurrenceRule3 rRule = new RecurrenceRule3()
//        .withFrequency(FrequencyType.YEARLY)
//        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20)));
//    }
}
