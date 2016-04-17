package jfxtras.labs.icalendarfx;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.mocks.InstanceMock;
import jfxtras.labs.icalendarfx.mocks.VEventMock;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Daily;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Monthly;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Weekly;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;

public abstract class ICalendarTestAbstract
{
//    private final static Class<InstanceMock> clazz = InstanceMock.class;
//    public Class<InstanceMock> getClazz() { return clazz; }
    
//    /**
//     * Tests equality between two VEventImplMock objects.  Treats v1 as expected.  Produces a JUnit-like
//     * exception is objects are not equal.
//     * 
//     * @param v1 - expected VEventImplMock
//     * @param v2 - actual VEventImplMock
//     * @return - equality result
//     */
//    protected static <T> boolean AppointmentIsEqualTo(Appointment a1, Appointment a2)
//    {
//        boolean startEquals = a1.getStartLocalDateTime().equals(a2.getStartLocalDateTime());
//        boolean endEquals = a1.getEndLocalDateTime().equals(a2.getEndLocalDateTime());
//        boolean descriptionEquals = (a1.getDescription() == null) ? (a2.getDescription() == null) : a1.getDescription().equals(a2.getDescription());
//        boolean locationEquals = (a1.getLocation() == null) ? (a2.getLocation() == null) : a1.getLocation().equals(a2.getLocation());
//        boolean summaryEquals = (a1.getSummary() == null) ? (a2.getSummary() == null) : a1.getSummary().equals(a2.getSummary());
//        boolean appointmentGroupEquals = (a1.getAppointmentGroup() == null) ? (a2.getAppointmentGroup() == null) : a1.getAppointmentGroup().equals(a2.getAppointmentGroup());
//        return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals && startEquals && endEquals;
//    }
    
    /** FREQ=YEARLY; */
    protected VEventMock getYearly1()
    {
        return new VEventMock()
                .withCategories("group13")
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withUniqueIdentifier("20151109T082900-0@jfxtras.org")
                .withDuration(Duration.ofHours(1))
                .withDescription("Yearly1 Description")
                .withSummary("Yearly1 Summary")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()));
    }

    /** FREQ=YEARLY;BYDAY=FR; */
    protected VEventMock getYearly2()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByDay(DayOfWeek.FRIDAY))));
    }
    
    /**Every Thursday, but only during June, July, and August, forever:
     * DTSTART;TZID=America/New_York:19970605T090000
     * RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8
     * example in RFC 5545 iCalendar, page 129 */
    protected VEventMock getYearly3()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByDay(DayOfWeek.THURSDAY)
                                           , new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST))));
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    protected VEventMock getYearly4()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByMonth(Month.JANUARY, Month.FEBRUARY))));
//        RRule rule = new RRule();
//                .withRRule(rule);
//        Frequency yearly = new Yearly();
//        rule.setFrequency(yearly);
//        Rule byRule = new ByMonth(Month.JANUARY, Month.FEBRUARY);
//        yearly.addByRule(byRule);
    }

    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    protected VEventMock getYearly5()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByMonth(Month.NOVEMBER)
                                           , new ByMonthDay(10))));
    }

    /** RFC 5545 iCalendar, page 130 
     * Every 4 years, the first Tuesday after a Monday in November,
      forever (U.S. Presidential Election day):

       DTSTART;TZID=America/New_York:19961105T090000
       RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;
        BYMONTHDAY=2,3,4,5,6,7,8 */
    protected VEventMock getYearly6()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withInterval(4)
                                .withByRules(new ByMonth(Month.NOVEMBER)
                                           , new ByDay(DayOfWeek.TUESDAY)
                                           , new ByMonthDay(2,3,4,5,6,7,8))));
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    protected VEventMock getYearly7()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20)))));
    }
    
    /** FREQ=YEARLY;WKST=MO;BYWEEKNO=20;BYDAY=MO */
    protected VEventMock getYearly8()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Yearly()
                                .withByRules(new ByWeekNumber(20).withWeekStart(DayOfWeek.MONDAY)
                                           , new ByDay(DayOfWeek.MONDAY))));
    }
        
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected VEventMock getMonthly1()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()));
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected VEventMock getMonthly2()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByMonthDay()
                                        .withDaysOfMonth(-2)))); // repeats 2nd to last day of month
    }

    /** FREQ=MONTHLY;BYDAY=TU,WE,FR */
    protected VEventMock getMonthly3()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))));
    }

    /** FREQ=MONTHLY;BYDAY=-1SA */
    protected VEventMock getMonthly4()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1))))); // last Saturday in month
    }

    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    protected VEventMock getMonthly5()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(1997, 6, 13, 10, 0))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(1997, 9, 1, 8, 30), ZoneOffset.UTC))
                .withDuration(Duration.ofHours(1))
                .withUniqueIdentifier("19970901T083000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(DayOfWeek.FRIDAY), new ByMonthDay(13))));
    }

    /** FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR  */
    protected VEventMock getMonthly6()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDuration(Duration.ofMinutes(90))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByMonth(Month.NOVEMBER, Month.DECEMBER)
                                           , new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))));
    }
    
    /** FREQ=MONTHLY;BYDAY=3MO */
    protected VEventMock getMonthly7()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.MONDAY, 3)))));
    }
    
    
    /** FREQ=WEEKLY, Basic weekly stream */
    protected VEventMock getWeekly1()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()));
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    protected static VEventMock getWeekly2()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly1 Description")
                .withSummary("Weekly1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()
                                .withInterval(2)
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))));
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    protected VEventMock getWeekly3()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly3 Description")
                .withSummary("Weekly3 Summary")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))));
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    protected VEventMock getWeekly4()
    {
        VEventMock vEvent = getWeekly2();
        vEvent.getRRule().setCount(11);
        return vEvent;
    }
    
    protected VEventMock getWeekly5()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2016, 1, 3, 5, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 1, 3, 7, 0))
                .withDescription("Weekly5 Description")
                .withSummary("Weekly5 Summary")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY))));      
    }
    
    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    public static VEventMock getWeeklyZoned()
    {
        return new VEventMock()
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 45), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withDescription("WeeklyZoned Description")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))))
                .withSummary("WeeklyZoned Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }

    
    /** FREQ=DAILY, Basic daily stream */
    public static VEventMock getDaily1()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily1 Description")
                .withSummary("Daily1 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily()));
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static VEventMock getDaily2()
    {
        return new VEventMock()
                .withCategories("group03")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(90))
                .withDescription("Daily2 Description")
                .withSummary("Daily2 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withCount(6)
                        .withFrequency(new Daily()
                                .withInterval(3)));
    }

    /** FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 */
    protected VEventMock getDaily3()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withCount(10)
                        .withFrequency(new Daily()
                                .withInterval(3)
                                .withByRules(new ByMonthDay()
                                        .withDaysOfMonth(9,10,11,12,13,14))));
    }

    /** FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    protected VEventMock getDaily4()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily()
                                .withInterval(2)
                                .withByRules(new ByMonthDay(9))));
    }
    
    /** FREQ=DAILY;INVERVAL=2;BYDAY=FR */
    protected VEventMock getDaily5()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily()
                                .withInterval(2)
                                .withByRules(new ByDay(DayOfWeek.FRIDAY))));
    }

    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151201T095959 */
    protected static VEventMock getDaily6()
    {
        return new VEventMock()
                .withCategories("group03")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(new Daily()
                                .withInterval(2)));
    }
    
    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151129T100000 
     * Tests inclusive UNTIL */
    protected static VEventMock getDaily7()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(new Daily()
                                .withInterval(2)));
    }
    
    public static VEventMock getDailyUTC()
    {
        return new VEventMock()
                .withCategories("group03")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 11, 0), ZoneOffset.UTC))
                .withDescription("DailyUTC Description")
                .withSummary("DailyUTC Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 10, 0), ZoneOffset.UTC))
                        .withFrequency(new Daily()
                                .withInterval(2)));
    }
    
    public static VEventMock getDailyJapanZone()
    {
        return new VEventMock()
                .withCategories("group03")
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 9, 0), ZoneId.of("Japan")))
                .withDescription("Japan Description")
                .withSummary("Japan Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 1, 0), ZoneOffset.UTC))
                        .withFrequency(new Daily()));
    }
    
    /** Individual - non repeatable VEvent */
    public static VEventMock getIndividual1()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30))
                .withDuration(Duration.ofMinutes(60))
                .withDescription("Individual Description")
                .withSummary("Individual Summary")
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    protected static VEventMock getIndividual2()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDate.of(2015, 11, 11))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    public static VEventMock getIndividualZoned()
    {
        return new VEventMock()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    public static VEventMock getDailyWithException1()
    {
        return getDaily2()
                .withExDate(new ExDate()
                        .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 15, 10, 0)));
    }

    protected static VEventMock getRDate()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(60))
                .withRDate(new RDate()
                        .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 14, 12, 0)));
    }
    
    /** all-day appointments */
    protected VEventMock getWholeDayDaily1()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /* FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected VEventMock getWholeDayDaily2()
    {
        return new VEventMock()
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withCount(6)
                        .withFrequency(new Daily()
                                .withInterval(3)));
    }

    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    protected static VEventMock getWholeDayDaily3()
    {
        return new VEventMock()
                .withCategories("group06")
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 11))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRule()
                        .withUntil(LocalDate.of(2015, 11, 24))
                        .withFrequency(new Daily()
                                .withInterval(3)));
    }

    /*
     *  Tests for multi-part recurrence sets
     *  Children have RECURRENCE-ID
     *  Branches have RELATED-TO
     */
    
    public static List<VComponent<InstanceMock>> getRecurrenceSetDaily1()
    {
        List<VComponent<InstanceMock>> recurrenceSet = new ArrayList<>();
        VEventMock parent = getDaily1();

        VEventMock child1 = getDaily1()
                .withRRule(null)
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 10, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 15, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 17, 0));

        VEventMock child2 = getDaily1()
                .withRRule(null)
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 12, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 13, 6, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 13, 7, 0));
                
        recurrenceSet.add(parent);
        recurrenceSet.add(child1);
        recurrenceSet.add(child2);
        parent.getRRule().recurrences().add(child1);
        parent.getRRule().recurrences().add(child2);

        return recurrenceSet;

    }

    // child of getDaily6
    protected static VEventMock getChild1()
    {
        VEventMock v = getDaily6();
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 22, 16, 0));
        v.setDateTimeEnd(LocalDateTime.of(2015, 11, 22, 18, 0));
        v.setDateTimeRecurrence(LocalDateTime.of(2015, 11, 21, 10, 0));
        v.setRRule(null);
        return v;
    }
    
    protected static List<VEventMock> getDailyWithRecurrence()
    {
        List<VEventMock> recurrenceSet = new ArrayList<>();
        VEventMock parent = getDaily6();
        VEventMock child = getChild1();
        
        recurrenceSet.add(parent);
        recurrenceSet.add(child);
        parent.getRRule().recurrences().add(child);

        return recurrenceSet;
    }
    
    /* Example Google individual appointment */
    protected static VEventMock getGoogleIndividual()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 15, 0), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneOffset.UTC))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withSummary("test1")
                .withUniqueIdentifier("vpqej26mlpg3adcncqqs7t7a34@google.com");
    }
    
    /* Example Google repeatable appointment */
    public static VEventMock getGoogleRepeatable()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 8, 0), ZoneId.of("America/Los_Angeles")))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY))))
                .withSummary("test2")
                .withUniqueIdentifier("im8hmpakeigu3d85j3vq9q8bcc@google.com");
    }
    
    /* Example Google repeatable appointment with EXDATE*/
    protected static VEventMock getGoogleWithExDates()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 15, 30), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 7, 22, 31), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExDate(new ExDate(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily())
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneOffset.UTC)))
                .withSummary("test3")
                .withUniqueIdentifier("86801l7316n97h75cefk1ruc00@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Parent*/
    protected static VEventMock getGoogleRepeatablePart1()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 13, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily())
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 18, 59, 59), ZoneOffset.UTC)))
                .withSummary("test4")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts
     * 
     * This-and-future edit of Parent
     * For this part, Google doesn't use RELATED-TO to establish the parent.
     * Instead, Google adds a UTC date, like a RECURRENCE-ID, to the UID
     * The special UID is converted to the RELATED-TO field internally */
    protected static VEventMock getGoogleRepeatablePart2()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 14, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRelatedTo("mrrfvnj5acdcvn13273on9nrhs@google.com")
                .withRRule(new RecurrenceRule()
                        .withFrequency(new Daily())
                        .withCount(6))
                .withSummary("test5")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs_R20160218T190000@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Recurrence */
    protected static VEventMock getGoogleRepeatablePart3()
    {
        return new VEventMock()
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 9, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 32, 26), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 7, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeRecurrence(ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withSequence(1)
                .withSummary("test6")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs@google.com");
    }
    
    protected static List<VEventMock> getGoogleRepeatableParts()
    {
        List<VEventMock> vComponents = new ArrayList<>();
        VEventMock p1 = getGoogleRepeatablePart1();
        VEventMock p2 = getGoogleRepeatablePart2();
        VEventMock p3 = getGoogleRepeatablePart3();
        vComponents.add(p1);
        vComponents.add(p2);
        vComponents.add(p3);
        p1.getRRule().withRecurrences(p3);
        return vComponents;
    }

    protected static VEventMock getSplitWeek()
    {
        return new VEventMock()
                .withDateTimeEnd(LocalDateTime.of(2016, 3, 13, 5, 45))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2016, 3, 12, 4, 0))
                .withDescription("Split Description")
                .withSummary("Split Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
}
