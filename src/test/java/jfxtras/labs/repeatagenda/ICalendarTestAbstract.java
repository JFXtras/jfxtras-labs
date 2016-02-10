package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
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
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public abstract class ICalendarTestAbstract
{
    public final static long NANOS_IN_SECOND = Duration.ofSeconds(1).toNanos();
    
    // Comparator for tree sort
//    private final Comparator<Appointment> APPOINTMENT_COMPARATOR = (a1, a2)
//            -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
//    public final Comparator<Appointment> getAppointmentComparator() { return APPOINTMENT_COMPARATOR; }
    
    private final static Class<Agenda.AppointmentImplLocal> clazz = Agenda.AppointmentImplLocal.class;
    public Class<Agenda.AppointmentImplLocal> getClazz() { return clazz; }
    
    protected static <T> boolean vEventIsEqualTo(VEventImpl v1, VEventImpl v2)
    {
        // VComponentAbstract properties
        boolean categoriesEquals = (v1.getCategories() == null) ? (v2.getCategories() == null) : v1.getCategories().equals(v2.getCategories());
        boolean commentEquals = (v1.getComment() == null) ? (v2.getComment() == null) : v1.getComment().equals(v2.getComment());
        boolean dateTimeStampEquals = (v1.getDateTimeStamp() == null) ? (v2.getDateTimeStamp() == null) : v1.getDateTimeStamp().equals(v2.getDateTimeStamp());
        boolean dateTimeStartEquals = (v1.getDateTimeStart() == null) ? (v2.getDateTimeStart() == null) : v1.getDateTimeStart().equals(v2.getDateTimeStart());
        boolean locationEquals = (v1.getLocation() == null) ? (v2.getLocation() == null) : v1.getLocation().equals(v2.getLocation());
        boolean sequenceEquals = v1.getSequence() == v2.getSequence();
        boolean summaryEquals = (v1.getSummary() == null) ? (v2.getSummary() == null) : v1.getSummary().equals(v2.getSummary());
        boolean uniqueIdentifierEquals = (v1.getUniqueIdentifier() == null) ? (v2.getUniqueIdentifier() == null) : v1.getUniqueIdentifier().equals(v2.getUniqueIdentifier());
        boolean relatedToEquals = (v1.getRelatedTo() == null) ? (v2.getRelatedTo() == null) : v1.getRelatedTo().equals(v2.getRelatedTo());
        boolean rruleEquals = (v1.getRRule() == null) ? (v2.getRRule() == null) : v1.getRRule().equals(v2.getRRule()); // goes deeper
        boolean eXDatesEquals = (v1.getExDate() == null) ? (v2.getExDate() == null) : v1.getExDate().equals(v2.getExDate()); // goes deeper
        boolean rDatesEquals = (v1.getRDate() == null) ? (v2.getRDate() == null) : v1.getRDate().equals(v2.getRDate()); // goes deeper
        
        // VEvent properties
        boolean descriptionEquals = (v1.getDescription() == null) ? (v2.getDescription() == null)
                : v1.getDescription().equals(v2.getDescription());
        boolean endPriorityEquals = v1.endPriority().equals(v2.endPriority());
        final boolean endEquals;
        switch (v1.endPriority())
        {
        case DTEND:
            endEquals = v1.getDateTimeEnd().equals(v2.getDateTimeEnd());
            break;
        case DURATION:
            endEquals = v1.getDurationInNanos().equals(v2.getDurationInNanos());
            break;
        default:
            endEquals = false; // shouldn't get here
            break;
        }
        
        // VEventImpl properties
        boolean appointmentClassEquals = (v1.getAppointmentClass() == null) ? (v2.getAppointmentClass() == null) : v1.getAppointmentClass().equals(v2.getAppointmentClass());
        boolean appointmentGroupEquals = (v1.getAppointmentGroup() == null) ? (v2.getAppointmentGroup() == null) : v1.getAppointmentGroup().equals(v2.getAppointmentGroup());

        if (categoriesEquals && commentEquals && dateTimeStampEquals && dateTimeStartEquals && locationEquals
                && summaryEquals && uniqueIdentifierEquals && rruleEquals && eXDatesEquals && rDatesEquals && relatedToEquals
                && sequenceEquals && descriptionEquals && endPriorityEquals && endEquals && appointmentClassEquals && appointmentGroupEquals)
        {
            return true;
        } else
        {
            throw new RuntimeException("VEvent not Equal:"
                    + System.lineSeparator()
                    + "expecting:" + System.lineSeparator()
                    + v1 + System.lineSeparator()
                    + "but was:" + System.lineSeparator()
                    + v2);
        }
    }
    
//    public void refresh(List<VComponent<Appointment>> vComponents, List<Appointment> appointments)
//    {
//        vComponents.stream().forEach(v -> v.instances().clear());   
//        appointments.clear();
//        vComponents.stream().forEach(r ->
//        {
//            Collection<Appointment> newAppointments = r.makeInstances();
//            appointments.addAll(newAppointments);
//        });
//    }
    
    /** FREQ=YEARLY; */
    protected VEventImpl getYearly1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeCreated(LocalDateTime.of(2015, 11, 9, 8, 29));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 11, 9, 8, 30));
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeLastModified(LocalDateTime.of(2015, 11, 10, 18, 30));
        vEvent.setUniqueIdentifier("20151109T082900-0@jfxtras.org");
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13));
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
    protected VEventImpl getYearly2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly3()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly4()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly5()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly6()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly7()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly8()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getYearly9()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getMonthly1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Monthly monthly = new Monthly();
        rule.setFrequency(monthly);
        return vEvent;
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected VEventImpl getMonthly2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getMonthly3()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getMonthly4()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getMonthly5()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(1997, 6, 13, 10, 0));
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
    protected VEventImpl getMonthly6()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 3, 10, 0));
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
    
    /** FREQ=MONTHLY;BYDAY=3MO */
    protected VEventImpl getMonthly7()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency monthly = new Monthly();
        rule.setFrequency(monthly);
        Rule byRule = new ByDay(new ByDay.ByDayPair(DayOfWeek.MONDAY, 3));
        monthly.addByRule(byRule);
        return vEvent;
    }
    
    
    /** FREQ=WEEKLY, Basic weekly stream */
    protected VEventImpl getWeekly1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2));
        vEvent.setDurationInNanos(2700L * NANOS_IN_SECOND);
        vEvent.setDescription("Weekly1 Description");
        vEvent.setSummary("Weekly1 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency weekly = new Weekly()
                .withInterval(2);
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);
        return vEvent;
    }

    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    protected VEventImpl getWeekly3()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 7, 10, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDurationInNanos(2700L * NANOS_IN_SECOND);
        vEvent.setDescription("Weekly3 Description");
        vEvent.setSummary("Weekly3 Summary");
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
    protected VEventImpl getWeekly4()
    {
        VEventImpl vEvent = getWeekly2();
        vEvent.getRRule().setCount(11);
        return vEvent;
    }
    
    protected VEventImpl getWeekly5()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2016, 1, 3, 5, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2016, 1, 3, 7, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Weekly5 Description");
        vEvent.setSummary("Weekly5 Summary");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency weekly = new Weekly();
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(DayOfWeek.SUNDAY, DayOfWeek.WEDNESDAY);
        weekly.addByRule(byRule);
        return vEvent;        
    }

    
    /** FREQ=DAILY, Basic daily stream */
    public static VEventImpl getDaily1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Daily1 Description");
        vEvent.setSummary("Daily1 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule();
        vEvent.setRRule(rule);
        Frequency daily = new Daily();
        rule.setFrequency(daily);
        return vEvent;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static VEventImpl getDaily2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
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
    protected VEventImpl getDaily3()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getDaily4()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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
    protected VEventImpl getDaily5()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
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

    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151201T095959 */
    protected static VEventImpl getDaily6()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Daily6 Description");
        vEvent.setSummary("Daily6 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withUntil(LocalDateTime.of(2015, 12, 1, 9, 59, 59));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        return vEvent;
    }
    
    /* FREQ=DAILY;INVERVAL=2;UNTIL=20151129T100000 */
    protected static VEventImpl getDaily7()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Daily6 Description");
        vEvent.setSummary("Daily6 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withUntil(LocalDateTime.of(2015, 11, 29, 10, 0));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        return vEvent;
    }
    
    /** Individual - non repeatable VEvent */
    protected static VEventImpl getIndividual1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        vEvent.setDescription("Individual Description");
        vEvent.setSummary("Individual Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        return vEvent;
    }
    
    protected static VEventImpl getIndividual2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 11));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        return vEvent;
    }
    
    /** FREQ=DAILY;INVERVAL=3;COUNT=6
     *  EXDATE=20151112T100000,20151115T100000 */
    public static VEventImpl getDailyWithException1()
    {
        VEventImpl vEvent = getDaily2();
        ExDate exDate = new ExDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 15, 10, 0));
        vEvent.setExDate(exDate);
        return vEvent;
    }

    protected VEventImpl getRDate()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDurationInNanos(3600L * NANOS_IN_SECOND);
        RDate rDate = new RDate().withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0), LocalDateTime.of(2015, 11, 14, 12, 0));
        vEvent.setRDate(rDate);
        return vEvent;
    }
    
    /** all-day appointments */
    protected VEventImpl getWholeDayDaily1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 9));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        return vEvent;
    }
    
    /* FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected VEventImpl getWholeDayDaily2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 9));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule()
                .withCount(6);
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        return vEvent;
    }

    /* FREQ=DAILY;INVERVAL=3;UNTIL=20151124 */
    protected static VEventImpl getWholeDayDaily3()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 9));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 11));
        vEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(6));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule()
                .withUntil(LocalDate.of(2015, 11, 24));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);
        return vEvent;
    }

    /*
     *  Tests for multi-part recurrence sets
     *  Children have RECURRENCE-ID
     *  Branches have RELATED-TO
     */
    
    public static Collection<VComponent<Appointment>> getRecurrenceSetDaily1()
    {
        Set<VComponent<Appointment>> recurrenceSet = new LinkedHashSet<>();
        VEventImpl parent = getDaily1();

        VEventImpl child1 = getDaily1()
                .withRRule(null)
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 10, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 10, 15, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 10, 17, 0));

        VEventImpl child2 = getDaily1()
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

    
    // TODO below methods are not being use - may be deleted in future
    
    // branch of getDaily6
    protected static VEventImpl getBranch1()
    {
        VEventImpl v = new VEventImpl(getDaily6());
        v.setDateTimeStart(LocalDateTime.of(2015, 12, 1, 12, 0));
        v.setDateTimeEnd(LocalDateTime.of(2015, 12, 1, 13, 0));
        v.setRelatedTo(v.getUniqueIdentifier());
        v.setUniqueIdentifier("20151201T080000-0@jfxtras.org");
        v.getRRule().setUntil(LocalDateTime.of(2015, 12, 13, 11, 59, 59));
        return v;
    }
    
    // branch of getDaily6
    protected static VEventImpl getBranch2()
    {
        VEventImpl v = new VEventImpl(getDaily6());
        v.setDateTimeStart(LocalDateTime.of(2015, 12, 14, 6, 0));
        v.setDateTimeEnd(LocalDateTime.of(2015, 12, 14, 8, 0));
        v.setRelatedTo(v.getUniqueIdentifier());
        v.setUniqueIdentifier("20151214T080000-0@jfxtras.org");
        v.getRRule().setUntil(null);
        return v;
    }

    // child of getDaily6
    protected static VEventImpl getChild1()
    {
        VEventImpl v = new VEventImpl(getDaily6());
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 22, 16, 0));
        v.setDateTimeEnd(LocalDateTime.of(2015, 11, 22, 18, 0));
        v.setDateTimeRecurrence(LocalDateTime.of(2015, 11, 21, 10, 0));
        v.setRRule(null);
        return v;
    }
    
    protected static Collection<VComponent<Appointment>> getRecurrenceSet()
    {
        Set<VComponent<Appointment>> recurrenceSet = new LinkedHashSet<>();
        VEventImpl parent = getDaily6();
        VEventImpl branch1 = getBranch1();
        VEventImpl branch2 = getBranch2();
        VEventImpl child = getChild1();
        
        recurrenceSet.add(parent);
        recurrenceSet.add(branch1);
        recurrenceSet.add(branch2);
        recurrenceSet.add(child);
        parent.getRRule().recurrences().add(child);

        return recurrenceSet;
    }
}
