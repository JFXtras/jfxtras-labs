package jfxtras.labs.icalendaragenda;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.VEventImpl;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleElement;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Daily;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Monthly;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Weekly;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Yearly;

/**
 * Static VEvents representing iCalendar components
 */
public final class ICalendarStaticVEvents
{   
    private ICalendarStaticVEvents() { }

    /** FREQ=YEARLY; */
    static VEventImpl getYearly1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC))
                .withUniqueIdentifier("20151109T082900-0@jfxtras.org")
                .withDuration(Duration.ofHours(1))
                .withDescription("Yearly1 Description")
                .withSummary(Summary.parse("Yearly1 Summary"))
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Yearly()));
    }
        
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    static VEventImpl getMonthly1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Monthly()));
    }
    
    /** FREQ=MONTHLY;BYDAY=3MO */
    static VEventImpl getMonthly7()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Monthly()
                                .withByRules(new ByDay(new ByDay.ByDayPair(DayOfWeek.MONDAY, 3)))));
    }
    
    
    /** FREQ=WEEKLY, Basic weekly stream */
    static VEventImpl getWeekly1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Weekly()));
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    static VEventImpl getWeekly2()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDuration(Duration.ofMinutes(45))
                .withDescription("Weekly1 Description")
                .withSummary(Summary.parse("Weekly1 Summary"))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Weekly()
                                .withInterval(2)
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))));
    }

    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    static VEventImpl getWeekly4()
    {
        VEventImpl vEvent = getWeekly2();
        vEvent.getRRule().setCount(11);
        return vEvent;
    }
    
    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    public static VEventImpl getWeeklyZoned()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 45), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withDescription("WeeklyZoned Description")
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))))
                .withSummary(Summary.parse("WeeklyZoned Summary"))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /** FREQ=DAILY, Basic daily stream */
    public static VEventImpl getDaily1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily1 Description")
                .withSummary(Summary.parse("Daily1 Summary"))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRuleElement()
                        .withFrequency(new Daily()));
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    static VEventImpl getDaily2()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withCategories(new Categories("group03"))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(90))
                .withDescription("Daily2 Description")
                .withSummary(Summary.parse("Daily2 Summary"))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRuleElement()
                        .withCount(6)
                        .withFrequency(new Daily()
                                .withInterval(3)));
    }

    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151201T095959 */
    static VEventImpl getDaily6()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0))
                .withDescription("Daily6 Description")
                .withSummary(Summary.parse("Daily6 Summary"))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRuleElement()
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.systemDefault())
                                .withZoneSameInstant(ZoneId.of("Z")))
                        .withFrequency(new Daily()
                                .withInterval(2)));
    }
    
    /** Individual - non repeatable VEvent */
    public static VEventImpl getIndividual1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30))
                .withDuration(Duration.ofMinutes(60))
                .withDescription("Individual Description")
                .withSummary(Summary.parse("Individual Summary"))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    // Whole day events
    static VEventImpl getIndividual2()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeStart(LocalDate.of(2015, 11, 11))
                .withDateTimeEnd(LocalDate.of(2015, 11, 12))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    public static VEventImpl getIndividualZoned()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
    }
    
    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    public static VEventImpl getDailyWithException1()
    {
        return getDaily2()
                .withExDate(new ExDate()
                        .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 15, 10, 0)));
    }

    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    static VEventImpl getWholeDayDaily3()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withCategories(new Categories("group06"))
                .withDateTimeStart(LocalDate.of(2015, 11, 9))
                .withDateTimeEnd(LocalDate.of(2015, 11, 11))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withRRule(new RecurrenceRuleElement()
                        .withUntil(LocalDate.of(2015, 11, 24))
                        .withFrequency(new Daily()
                                .withInterval(3)));
    }
}
