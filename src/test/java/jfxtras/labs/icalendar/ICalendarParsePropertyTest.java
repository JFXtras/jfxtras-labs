package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.descriptive.Summary;
import jfxtras.labs.icalendar.properties.recurrence.rrule.RRule;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByWeekNo;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Daily;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Monthly;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Yearly;

public class ICalendarParsePropertyTest extends ICalendarTestAbstract
{
    @Test
    public void canParseSummary()
    {
        String propertyString = "TEST SUMMARY";
        Summary madeSummary = new Summary(propertyString);
        String expectedSummary = "SUMMARY:TEST SUMMARY";
        assertEquals(expectedSummary, madeSummary.toString());
    }
    
    /** tests parsing RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Yearly()
                .withInterval(2);
        frequency.addByRule(new ByMonth(Month.JANUARY));
        frequency.addByRule(new ByDay(DayOfWeek.SUNDAY));
        expectedRRule.setFrequency(frequency);
        System.out.println("rule:" + rRule);
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Monthly();
        frequency.addByRule(new ByDay(DayOfWeek.SATURDAY));
        frequency.addByRule(new ByMonthDay(7,8,9,10,11,12,13));
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Yearly();
        frequency.addByRule(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)));
        frequency.addByRule(new ByWeekNo(20));
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule();
        expectedRRule.setUntil(LocalDateTime.of(2015, 12, 1, 10, 0));
        Frequency frequency = new Daily();
        frequency.setInterval(2);
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }
}
