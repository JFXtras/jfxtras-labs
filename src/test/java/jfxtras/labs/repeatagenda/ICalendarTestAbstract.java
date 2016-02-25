package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.RDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponentProperty;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEventProperty;
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
    
    /**
     * Tests equality between two VEventImpl objects.  Treats v1 as expected.  Produces a JUnit-like
     * exception is objects are not equal.
     * 
     * @param v1 - expected VEventImpl
     * @param v2 - actual VEventImpl
     * @return - equality result
     */
    protected static <T> boolean vEventIsEqualTo(VEventImpl v1, VEventImpl v2)
    {
        List<String> changedProperties = new ArrayList<>();
        Arrays.stream(VComponentProperty.values())
        .forEach(p -> 
        {
            if (! (p.isPropertyEqual(v1, v2)))
            {
                changedProperties.add(p.toString() + " not equal" + p.getPropertyValue(v1) + " " + p.getPropertyValue(v2));
            }
        });
        
        Arrays.stream(VEventProperty.values())
        .forEach(p -> 
        {
            if (! (p.isPropertyEqual(v1, v2)))
            {
                changedProperties.add(p.toString() + " not equal" + p.getPropertyValue(v1) + " " + p.getPropertyValue(v2));
            }
        });
        
//        // VComponentAbstract properties
//        boolean categoriesEquals = (v1.getCategories() == null) ? (v2.getCategories() == null) : v1.getCategories().equals(v2.getCategories());
//        boolean commentEquals = (v1.getComment() == null) ? (v2.getComment() == null) : v1.getComment().equals(v2.getComment());
//        boolean dateTimeStampEquals = (v1.getDateTimeStamp() == null) ? (v2.getDateTimeStamp() == null) : v1.getDateTimeStamp().equals(v2.getDateTimeStamp());
//        boolean dateTimeStartEquals = (v1.getDateTimeStart() == null) ? (v2.getDateTimeStart() == null) : v1.getDateTimeStart().equals(v2.getDateTimeStart());
//        boolean sequenceEquals = v1.getSequence() == v2.getSequence();
//        boolean summaryEquals = (v1.getSummary() == null) ? (v2.getSummary() == null) : v1.getSummary().equals(v2.getSummary());
//        boolean uniqueIdentifierEquals = (v1.getUniqueIdentifier() == null) ? (v2.getUniqueIdentifier() == null) : v1.getUniqueIdentifier().equals(v2.getUniqueIdentifier());
//        boolean relatedToEquals = (v1.getRelatedTo() == null) ? (v2.getRelatedTo() == null) : v1.getRelatedTo().equals(v2.getRelatedTo());
//        boolean rruleEquals = (v1.getRRule() == null) ? (v2.getRRule() == null) : v1.getRRule().equals(v2.getRRule()); // goes deeper
//        boolean eXDatesEquals = (v1.getExDate() == null) ? (v2.getExDate() == null) : v1.getExDate().equals(v2.getExDate()); // goes deeper
//        boolean rDatesEquals = (v1.getRDate() == null) ? (v2.getRDate() == null) : v1.getRDate().equals(v2.getRDate()); // goes deeper
//        
//        // VEvent properties
//        boolean descriptionEquals = (v1.getDescription() == null) ? (v2.getDescription() == null) : v1.getDescription().equals(v2.getDescription());
//        boolean endPriorityEquals = v1.endPriority().equals(v2.endPriority());
//        boolean locationEquals = (v1.getLocation() == null) ? (v2.getLocation() == null) : v1.getLocation().equals(v2.getLocation());
//        final boolean endEquals;
//        switch (v1.endPriority())
//        {
//        case DTEND:
//            endEquals = v1.getDateTimeEnd().equals(v2.getDateTimeEnd());
//            break;
//        case DURATION:
//            endEquals = v1.getDuration().equals(v2.getDuration());
//            break;
//        default:
//            endEquals = false; // shouldn't get here
//            break;
//        }
        
        // VEventImpl properties
        boolean appointmentClassEquals = (v1.getAppointmentClass() == null) ? (v2.getAppointmentClass() == null) : v1.getAppointmentClass().equals(v2.getAppointmentClass());
        boolean appointmentGroupEquals = (v1.getAppointmentGroup() == null) ? (v2.getAppointmentGroup() == null) : v1.getAppointmentGroup().equals(v2.getAppointmentGroup());

        if ((changedProperties.size() == 0) && appointmentClassEquals && appointmentGroupEquals)
        {
            return true;
        } else
        {
            throw new RuntimeException("VEvent not Equal:"
                    + System.lineSeparator()
                    + "expecting:" + System.lineSeparator()
                    + v1 + System.lineSeparator()
                    + "but was:" + System.lineSeparator()
                    + v2 + System.lineSeparator()
                    + changedProperties.stream().collect(Collectors.joining(System.lineSeparator()))
                    );
        }
    }
    
    /**
     * Tests equality between two VEventImpl objects.  Treats v1 as expected.  Produces a JUnit-like
     * exception is objects are not equal.
     * 
     * @param v1 - expected VEventImpl
     * @param v2 - actual VEventImpl
     * @return - equality result
     */
    protected static <T> boolean AppointmentIsEqualTo(Appointment a1, Appointment a2)
    {
        boolean startEquals = a1.getStartLocalDateTime().equals(a2.getStartLocalDateTime());
        boolean endEquals = a1.getEndLocalDateTime().equals(a2.getEndLocalDateTime());
        boolean descriptionEquals = (a1.getDescription() == null) ? (a2.getDescription() == null) : a1.getDescription().equals(a2.getDescription());
        boolean locationEquals = (a1.getLocation() == null) ? (a2.getLocation() == null) : a1.getLocation().equals(a2.getLocation());
        boolean summaryEquals = (a1.getSummary() == null) ? (a2.getSummary() == null) : a1.getSummary().equals(a2.getSummary());
        boolean appointmentGroupEquals = (a1.getAppointmentGroup() == null) ? (a2.getAppointmentGroup() == null) : a1.getAppointmentGroup().equals(a2.getAppointmentGroup());
        return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals && startEquals && endEquals;
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
        vEvent.setDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 29), ZoneOffset.UTC));
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 30), ZoneOffset.UTC));
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        vEvent.setDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 18, 30), ZoneOffset.UTC));
        vEvent.setUniqueIdentifier("20151109T082900-0@jfxtras.org");
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13));
        vEvent.setDuration(Duration.ofHours(1));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(1997, 9, 1, 8, 30), ZoneOffset.UTC));
        vEvent.setDuration(Duration.ofHours(1));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setDuration(Duration.ofMinutes(90));
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
        vEvent.setDuration(Duration.ofMinutes(45));
        vEvent.setDescription("Weekly1 Description");
        vEvent.setSummary("Weekly1 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDuration(Duration.ofMinutes(45));
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
    
    /** FREQ=WEEKLY;BYDAY=MO,WE,FR  */
    public static VEventImpl getWeeklyZoned()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 45), ZoneId.of("America/Los_Angeles")))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles")))
                .withDescription("WeeklyZoned Description")
                .withRRule(new RRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY))))
                .withSummary("WeeklyZoned Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDuration(Duration.ofMinutes(90));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Daily2 Description");
        vEvent.setSummary("Daily2 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withUntil(LocalDateTime.of(2015, 11, 29, 10, 0));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        return vEvent;
    }
    
    public static VEventImpl getDailyUTC()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneOffset.UTC));
        vEvent.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 11, 0), ZoneOffset.UTC));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
        vEvent.setDescription("Daily6 Description");
        vEvent.setSummary("Daily6 Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        RRule rule = new RRule()
                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 12, 1, 9, 59, 59), ZoneOffset.UTC));
        vEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(2);
        rule.setFrequency(daily);
        return vEvent;
    }
    
    /** Individual - non repeatable VEvent */
    public static VEventImpl getIndividual1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 30));
        vEvent.setDuration(Duration.ofMinutes(60));
        vEvent.setDescription("Individual Description");
        vEvent.setSummary("Individual Summary");
        vEvent.setAppointmentClass(clazz);
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        return vEvent;
    }
    
    protected static VEventImpl getIndividual2()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 11));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(13));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        return vEvent;
    }
    
    public static VEventImpl getIndividualZoned()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London")));
        vEvent.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.of("Europe/London")));
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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

    protected static VEventImpl getRDate()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(60))
                .withRDate(new RDate()
                        .withTemporals(LocalDateTime.of(2015, 11, 12, 10, 0)
                                     , LocalDateTime.of(2015, 11, 14, 12, 0)));
        return vEvent;
    }
    
    /** all-day appointments */
    protected VEventImpl getWholeDayDaily1()
    {
        VEventImpl vEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setDateTimeStart(LocalDate.of(2015, 11, 9));
        vEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
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
        vEvent.setDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC));
        vEvent.setAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(6));
        vEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        vEvent.setAppointmentClass(clazz);
        RRule rule = new RRule()
//                .withUntil(ZonedDateTime.of(LocalDateTime.of(2015, 11, 29, 10, 0), ZoneOffset.UTC));
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
    
    protected static List<VEventImpl> getDailyWithRecurrence()
    {
        List<VEventImpl> recurrenceSet = new ArrayList<>();
        VEventImpl parent = getDaily6();
        VEventImpl child = getChild1();
        
        recurrenceSet.add(parent);
        recurrenceSet.add(child);
        parent.getRRule().recurrences().add(child);

        return recurrenceSet;
    }
    
    /* Example Google individual appointment */
    protected static VEventImpl getGoogleIndividual()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 15, 0), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneOffset.UTC))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 13), ZoneOffset.UTC))
                .withSummary("test1")
                .withUniqueIdentifier("vpqej26mlpg3adcncqqs7t7a34@google.com");
    }
    
    /* Example Google repeatable appointment */
    public static VEventImpl getGoogleRepeatable()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 32), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 8, 0), ZoneId.of("America/Los_Angeles")))
                .withRRule(new RRule()
                        .withFrequency(new Weekly()
                                .withByRules(new ByDay(DayOfWeek.SUNDAY, DayOfWeek.TUESDAY, DayOfWeek.FRIDAY))))
                .withSummary("test2")
                .withUniqueIdentifier("im8hmpakeigu3d85j3vq9q8bcc@google.com");
    }
    
    /* Example Google repeatable appointment with EXDATE*/
    protected static VEventImpl getGoogleWithExDates()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 6, 50, 24), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 15, 30), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 2, 25, 25), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 7, 22, 31), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExDate(new ExDate(
                            ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 12, 12, 30), ZoneId.of("America/Los_Angeles"))
                          , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withRRule(new RRule()
                        .withFrequency(new Daily())
                        .withUntil(ZonedDateTime.of(LocalDateTime.of(2016, 5, 12, 19, 30, 0), ZoneOffset.UTC)))
                .withSummary("test3")
                .withUniqueIdentifier("86801l7316n97h75cefk1ruc00@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Parent*/
    protected static VEventImpl getGoogleRepeatablePart1()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 13, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRRule(new RRule()
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
    protected static VEventImpl getGoogleRepeatablePart2()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
                .withDateTimeCreated(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 3), ZoneOffset.UTC))
                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 14, 0), ZoneId.of("America/Los_Angeles")))
                .withDateTimeLastModified(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 19, 37, 17), ZoneOffset.UTC))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 21, 36, 37), ZoneOffset.UTC))
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 11, 0), ZoneId.of("America/Los_Angeles")))
                .withRelatedTo("mrrfvnj5acdcvn13273on9nrhs@google.com")
                .withRRule(new RRule()
                        .withFrequency(new Daily())
                        .withCount(6))
                .withSummary("test5")
                .withUniqueIdentifier("mrrfvnj5acdcvn13273on9nrhs_R20160218T190000@google.com");
    }
    
    /* Example Google repeatable appointment with 3 parts 
     * Recurrence */
    protected static VEventImpl getGoogleRepeatablePart3()
    {
        return new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(clazz)
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
    
    protected static List<VEventImpl> getGoogleRepeatableParts()
    {
        List<VEventImpl> vComponents = new ArrayList<>();
        VEventImpl p1 = getGoogleRepeatablePart1();
        VEventImpl p2 = getGoogleRepeatablePart2();
        VEventImpl p3 = getGoogleRepeatablePart3();
        vComponents.add(p1);
        vComponents.add(p2);
        vComponents.add(p3);
        p1.getRRule().withRecurrences(p3);
        return vComponents;
    }
}
