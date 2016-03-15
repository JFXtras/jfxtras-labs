package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.descriptive.Summary;
import jfxtras.labs.icalendar.properties.recurrence.rrule.RRule;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Daily;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Monthly;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Yearly;

public class ICalendarParsePropertyTest extends ICalendarTestAbstract
{
    @Test
    public void canParseSummary()
    {
        Summary madeSummary = new Summary("TEST SUMMARY");
        String expectedSummary = "SUMMARY:TEST SUMMARY";
        assertEquals(expectedSummary, madeSummary.toString());
    }
    
    /** tests parsing RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule()
                .withFrequency(new Yearly()
                        .withInterval(2)
                        .withByRules(new ByMonth(Month.JANUARY), new ByDay(DayOfWeek.SUNDAY)));
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYMONTHDAY=7,8,9,10,11,12,13;BYDAY=SA";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule()
                .withFrequency(new Monthly()
                        .withByRules(new ByDay(DayOfWeek.SATURDAY), new ByMonthDay(7,8,9,10,11,12,13)));

        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule()
                .withFrequency(new Yearly()
                        .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)), new ByWeekNumber(20)));
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z";
        RRule rRule = new RRule(s);
        RRule expectedRRule = new RRule()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0),ZoneId.of("Z")))
                .withFrequency(new Daily()
                        .withInterval(2));
        System.out.println(rRule);
        assertEquals(expectedRRule, rRule);
    }
    
    /*
     * Catch errors
     */
//    @Rule
//    public final ExpectedException exception = ExpectedException.none();
//    
//    @Test // (expected=IllegalArgumentException.class)
//    public void canStopTwoSameByRules()
//    {
//        try {
//            Yearly y = new Yearly();
//            y.byRules().add(new ByMonth(Month.JANUARY));
//            y.byRules().add(new ByDay(DayOfWeek.SUNDAY));
//            y.byRules().add(new ByMonth(Month.MARCH));
//        } catch (Exception ex) {
//            System.out.println("ok");
////            assertEquals("Can't add ByMonth (BYMONTH) more than once.", ex.getMessage());
//        }
////        fail("expected IllegalArgumentException Can't add ByMonth (BYMONTH) more than once.");
////        exception.expect(RuntimeException.class);
////        exception.expectMessage("Can't add ByMonth (BYMONTH) more than once.");
//    }
//    
//    @Test(expected=IndexOutOfBoundsException.class)
//    public void testIndexOutOfBoundsException() {
//        ArrayList emptyList = new ArrayList();
//        Object o = emptyList.get(0);
//    }
//    
////    @Rule
////    public final ExpectedException exception = ExpectedException.none();
//    
//    @Test
//    public void doStuffThrowsIndexOutOfBoundsException() {
//        ArrayList emptyList = new ArrayList();
//
//      exception.expect(IndexOutOfBoundsException.class);
//      Object o = emptyList.get(0);
//    }

}
