package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentImplLocal2;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.EXDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.RDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonth;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByWeekNo;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Weekly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Yearly;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public abstract class ICalendarTestAbstract extends ICalendarAgendaTestAbstract
{
    public static final long NANOS_IN_SECOND = Duration.ofSeconds(1).toNanos();

    public final static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
    = javafx.collections.FXCollections.observableArrayList(
            IntStream
            .range(0, 23)
            .mapToObj(i -> 
            {
                ICalendarAgenda.AppointmentGroupImpl a = new ICalendarAgenda.AppointmentGroupImpl()
//                    .withKey(i)
                    .withDescription("group" + (i < 10 ? "0" : "") + i);
                a.setStyleClass("group" + i);
                return a;
            })
            .collect(Collectors.toList()));
    static ObservableList<AppointmentGroup> appointmentGroups = DEFAULT_APPOINTMENT_GROUPS;
    
    // Comparator for tree sort
    private static final Comparator<Appointment> APPOINTMENT_COMPARATOR = (a1, a2)
            -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
    public static final Comparator<Appointment> getAppointmentComparator() { return APPOINTMENT_COMPARATOR; }
    
    private static final Class<AppointmentImplLocal2> clazz = AppointmentImplLocal2.class;
    public Class<AppointmentImplLocal2> getClazz() { return clazz; }
    
    public static void refresh(List<VComponent<Appointment>> vComponents, List<Appointment> appointments)
    {
        vComponents.stream().forEach(v -> v.instances().clear());   
        appointments.clear();
        vComponents.stream().forEach(r ->
        {
            Collection<Appointment> newAppointments = r.makeInstances();
            appointments.addAll(newAppointments);
        });
    }
    
    /** FREQ=YEARLY; */
    protected static VEventImpl getYearly1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeCreated(LocalDateTime.of(2015, 11, 9, 8, 29));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 11, 9, 8, 30));
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeLastModified(LocalDateTime.of(2015, 11, 10, 18, 30));
        vEvent.setUniqueIdentifier("20151109T082900-0@jfxtras.org");
        vEvent.setAppointmentGroup(appointmentGroups.get(13));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        vEvent.setDescription("Yearly1 Description");
        vEvent.setSummary("Yearly1 Summary");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        return vEvent;
    }

    /** FREQ=YEARLY;BYDAY=SU; */
    protected static VEventImpl getYearly2()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 6, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(DayOfWeek.FRIDAY);
        yearly.addByRule(byRule);
        return vEvent;
    }
    
    /**Every Thursday, but only during June, July, and August, forever:
     * DTSTART;TZID=America/New_York:19970605T090000
     * RRULE:FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8
     * example in RFC 5545 iCalendar, page 129 */
    protected static VEventImpl getYearly3()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1997, 6, 5, 9, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(DayOfWeek.THURSDAY);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonth(Month.JUNE, Month.JULY, Month.AUGUST);
        yearly.addByRule(byRule2);
        return vEvent;
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    protected static VEventImpl getYearly4()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 1, 6, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(Month.JANUARY, Month.FEBRUARY);
        yearly.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    protected static VEventImpl getYearly5()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 10, 0, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(10);
        yearly.addByRule(byRule2);
        return vEvent;
    }

    /** RFC 5545 iCalendar, page 130 
     * Every 4 years, the first Tuesday after a Monday in November,
      forever (U.S. Presidential Election day):

       DTSTART;TZID=America/New_York:19961105T090000
       RRULE:FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;
        BYMONTHDAY=2,3,4,5,6,7,8 */
    protected static VEventImpl getYearly6()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1996, 11, 5, 0, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly()
                .withInterval(4);
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByDay(DayOfWeek.TUESDAY);
        yearly.addByRule(byRule2);
        Rule byRule3 = new ByMonthDay(2,3,4,5,6,7,8);
        yearly.addByRule(byRule3);
        return vEvent;
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    protected static VEventImpl getYearly7()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1997, 5, 19, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByDay(new ByDayPair(DayOfWeek.MONDAY, 20));
        yearly.addByRule(byRule);
        return vEvent;
    }
    
    /** FREQ=YEARLY;WKST=MO;BYWEEKNO=20;BYDAY=MO */
    protected static VEventImpl getYearly8()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1997, 5, 12, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        ByWeekNo byRule = new ByWeekNo(20);
        byRule.setWeekStart(DayOfWeek.MONDAY); // not needed, is default.
        yearly.addByRule(byRule);
        Rule byRule2 = new ByDay(DayOfWeek.MONDAY);
        yearly.addByRule(byRule2);

        return vEvent;
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 - start before first valid date */
    protected static VEventImpl getYearly9()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 1, 1, 0, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency yearly = new Yearly();
        rule.setFrequency(yearly);
        Rule byRule = new ByMonth(Month.NOVEMBER);
        yearly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(10); // use default repeat date from startLocalDateTime (10th of month)
        yearly.addByRule(byRule2);
        return vEvent;
    }
    
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected static VEventImpl getMonthly1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Monthly monthly = new Monthly();
        rule.setFrequency(monthly);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected static VEventImpl getMonthly2()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 29, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule by = new ByMonthDay()
                .withDaysOfMonth(-2);// repeats 2nd to last day
        monthly.addByRule(by);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYDAY=TU,WE,FR */
    protected static VEventImpl getMonthly3()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYDAY=-1SA */
    protected static VEventImpl getMonthly4()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(new ByDay.ByDayPair(DayOfWeek.SATURDAY, -1));
        monthly.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    protected static VEventImpl getMonthly5()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1997, 9, 2, 10, 0));
        vEvent.setDateTimeStamp(LocalDateTime.of(1997, 9, 1, 8, 30));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        vEvent.setUniqueIdentifier("19970901T083000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(DayOfWeek.FRIDAY);
        monthly.addByRule(byRule);
        Rule byRule2 = new ByMonthDay(13);
        monthly.addByRule(byRule2);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYMONTH=11,12;BYDAY=TU,WE,FR - start before first valid date */
    protected static VEventImpl getMonthly6()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 1, 10, 10, 0));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule1 = new ByMonth(Month.NOVEMBER, Month.DECEMBER);
        monthly.addByRule(byRule1);
        Rule byRule2 = new ByDay(DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        monthly.addByRule(byRule2);
        return vEvent;
    }
    
    /** FREQ=WEEKLY, Basic weekly stream */
    protected static VEventImpl getWeekly1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency weekly = new Weekly();
        rule.setFrequency(weekly);
        return vEvent;
    }

    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    protected static VEventImpl getWeekly2()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0));
        vEvent.setAppointmentGroup(appointmentGroups.get(3));
        vEvent.setDurationInNanos(2700L * NANOS_IN_SECOND);
        vEvent.setDescription("Weekly1 Description");
        vEvent.setSummary("Weekly1 Summary");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency weekly = new Weekly()
                .withInterval(2);
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  - start before first valid date */
    protected static VEventImpl getWeekly3()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0));
        vEvent.setAppointmentGroup(appointmentGroups.get(3));
        vEvent.setDurationInNanos(2700L * NANOS_IN_SECOND);
        vEvent.setDescription("Weekly1 Description");
        vEvent.setSummary("Weekly1 Summary");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency weekly = new Weekly();
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);
        return vEvent;
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    protected static VEventImpl getWeekly4()
    {
        VEventImpl vEvent = getWeekly2();
        vEvent.getRRule().setCount(11);
        return vEvent;
    }
    
    /** FREQ=DAILY, Basic daily stream */
    protected static VEventImpl getDaily1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        vEvent.setDescription("Daily1 Description");
        vEvent.setSummary("Daily1 Summary");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency daily = new Daily();
        rule.setFrequency(daily);
        return vEvent;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static VEventImpl getDaily2()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
        vEvent.setAppointmentGroup(appointmentGroups.get(3));
        vEvent.setDescription("Daily2 Description");
        vEvent.setSummary("Daily2 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withCount(6);
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        return vEvent;
    }

    /** FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 */
    protected static VEventImpl getDaily3()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0));
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withCount(10);
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        Rule byRule = new ByMonthDay()
                .withDaysOfMonth(9,10,11,12,13,14);
        daily.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    protected static VEventImpl getDaily4()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        Rule byRule = new ByMonthDay(9); // use default repeat date from startLocalDateTime (9th of month)
        daily.addByRule(byRule);
        return vEvent;
    }
    
    /** FREQ=DAILY;INVERVAL=2;BYDAY=FR */
    protected static VEventImpl getDaily5()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        Rule byRule = new ByDay(DayOfWeek.FRIDAY);
        daily.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000 */
    protected static VEventImpl getDaily6()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0));
//        vEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
        vEvent.setAppointmentGroup(appointmentGroups.get(3));
        vEvent.setDescription("Daily6 Description");
        vEvent.setSummary("Daily6 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withUntil(LocalDateTime.of(2015, 12, 1, 0, 0));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        return vEvent;
    }
    
    /** Individual - non repeatable VEvent */
    protected static VEventImpl getIndividual1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        vEvent.setAppointmentClass(clazz);
        return vEvent;
    }
    
    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    protected static VEventImpl getDailyWithException1()
    {
        VEventImpl vEvent = getDaily2();
//        EXDate exDate = new EXDate().withDates(LocalDateTime.of(2015, 11, 12, 10, 0)), LocalDateTime.of(2015, 11, 15, 10, 0));
        EXDate exDate = new EXDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        vEvent.setExDate(exDate);
        return vEvent;
    }

    protected static VEventImpl getRecurrence1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        RDate rDate = new RDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 14, 12, 0));
        vEvent.setRDate(rDate);
        return vEvent;
    }
    
    /** all-day appointments */
    protected static VEventImpl getWholeDayDaily1()
    {
        VEventImpl vEvent = new VEventImpl(DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 9));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        return vEvent;
    }

}
