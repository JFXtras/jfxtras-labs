package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper.CallbackTwoParameters;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VComponentRepeatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

@Deprecated
public class MakeAppointmentsTest
{
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeAppointmentsDailyTest1()
    {
//        Arrays.stream(VComponentUtilities.VComponentPropertyOld.values())
//        .forEach(p -> System.out.println(p + " " + p.isDialogRequired()));
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> newAppointments = makeAppointments(vevent, startRange, endRange);
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(1, ChronoUnit.HOURS))
                            .withSummary("Daily1 Summary");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    @Test
    public void makeAppointmentsDailyTest2()
    {
        LocalDate startDate = LocalDate.of(2015, 11, 15);
        VEvent vevent = new VEvent()
              .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(9, 45)), ZoneId.of("America/Los_Angeles")))
              .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneOffset.UTC))
              .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(1), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")))
              .withDescription("WeeklyZoned Description")
              .withRecurrenceRule(new RecurrenceRule3()
                      .withUntil(ZonedDateTime.of(LocalDateTime.of(startDate.plusDays(15), LocalTime.of(8, 15)), ZoneId.of("America/Los_Angeles")).withZoneSameInstant(ZoneId.of("Z")))
                      .withFrequency(FrequencyType.WEEKLY)
                      .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)))
              .withSummary(Summary.parse("WeeklyZoned Ends"))
              .withUniqueIdentifier("20150110T080000-1@jfxtras.org");
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> newAppointments = makeAppointments(vevent, startRange, endRange);
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 15), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 15), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 8, 15), ZoneId.of("America/Los_Angeles"))
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(90, ChronoUnit.MINUTES))
                            .withSummary("WeeklyZoned Ends");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    @Test
    public void makeAppointmentsDailyTest3()
    {
        LocalDate startDate = LocalDate.of(2015, 11, 15);
        VEvent vevent = new VEvent()
                .withCategories(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(15).getDescription())
                .withDateTimeStart(startDate)
                .withDateTimeEnd(startDate.plusDays(1))
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withDescription("LocalDate Description")
                .withSummary("LocalDate")
                .withUniqueIdentifier("20150110T080000-3@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule3()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withInterval(3));
        LocalDateTime startRange = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime endRange = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> newAppointments = makeAppointments(vevent, startRange, endRange);
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 15)
                ));

        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplTemporal()
                            .withStartTemporal(d)
                            .withEndTemporal(d.plus(1, ChronoUnit.DAYS))
                            .withSummary("LocalDate");
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(isEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    @Test
    public void makeAppointmentsDailyTest4()
    {
        final Collection<Appointment> appointments = new ArrayList<>();
        final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();    
        final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */

        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
//                appointments,
                MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE
//                vComponentAppointmentMap,
//               appointmentVComponentMap
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        System.out.println(appointments.size());
        newAppointments.stream().forEach(System.out::println);
    }
    
    /** Callback to make appointment from VComponent and Temporal */
    public static final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE = (vComponentEdited, startTemporal) ->
    {
        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
        VComponentLocatable<?> vComponentLocatable = (VComponentLocatable<?>) vComponentEdited;
        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
        Temporal endTemporal = startTemporal.plus(adjustment);

        // Make appointment
        Appointment appt = new Agenda.AppointmentImplTemporal()
                .withStartTemporal(startTemporal)
                .withEndTemporal(endTemporal)
                .withDescription( (vComponentLocatable.getDescription() != null) ? vComponentLocatable.getDescription().getValue() : null )
                .withSummary( (vComponentLocatable.getSummary() != null) ? vComponentLocatable.getSummary().getValue() : null)
                .withLocation( (vComponentLocatable.getLocation() != null) ? vComponentLocatable.getLocation().getValue() : null)
                .withWholeDay(isWholeDay);
        return appt;
    };
    
    /** Similar to {@link ICalendarAgenda#makeAppointments} */
    @Deprecated // replace with static in ReviseComponentHelper
    public static List<Appointment> makeAppointments(VEvent component, LocalDateTime startRange, LocalDateTime endRange)
    {
        List<Appointment> appointments = new ArrayList<>();
        Boolean isWholeDay = component.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
//        LocalDateTime startRange = getDateTimeRange().getStartLocalDateTime();
//        LocalDateTime endRange = getDateTimeRange().getEndLocalDateTime();
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = startRange.toLocalDate();
            endRange2 = endRange.toLocalDate();            
        } else
        {
            startRange2 = component.getDateTimeStart().getValue().with(startRange);
            endRange2 = component.getDateTimeStart().getValue().with(endRange);            
        }
        component.streamRecurrences(startRange2, endRange2).forEach(startTemporal ->
        {
            // calculate date-time end
            final TemporalAmount adjustment;
            if (component.getDuration() != null)
            {
                adjustment = component.getDuration().getValue();
            } else if (component.getDateTimeEnd() != null)
            {
                Temporal dtstart = component.getDateTimeStart().getValue();
                Temporal dtend = component.getDateTimeEnd().getValue();
                if (dtstart instanceof LocalDate)
                {
                    adjustment = Period.between((LocalDate) dtstart, (LocalDate) dtend);                
                } else
                {
                    adjustment = Duration.between(dtstart, dtend);
                }
            } else
            {
                throw new RuntimeException("Either DTEND or DURATION must be set");
            }
            Temporal endTemporal = startTemporal.plus(adjustment);

//            /* Find AppointmentGroup
//             * control can only handle one category.  Checks only first category
//             */
//            final AppointmentGroup appointmentGroup;
//            if (component.getCategories() != null)
//            {
//                String firstCategory = component.getCategories().get(0).getValue().get(0);
//                Optional<AppointmentGroup> myGroup = appointmentGroups()
//                        .stream()
//                        .filter(g -> g.getDescription().equals(firstCategory))
//                        .findAny();
//                appointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
//            } else
//            {
//                appointmentGroup = null;
//            }
            // Make appointment
            Appointment appt = new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(startTemporal)
                    .withEndTemporal(endTemporal)
                    .withDescription( (component.getDescription() != null) ? component.getDescription().getValue() : null )
                    .withSummary( (component.getSummary() != null) ? component.getSummary().getValue() : null)
                    .withLocation( (component.getLocation() != null) ? component.getLocation().getValue() : null)
                    .withWholeDay(isWholeDay);
//                    .withAppointmentGroup(appointmentGroup);
            appointments.add(appt);   // add appointments to return argument
        });
        return appointments;
    }
    
    public static boolean isEqualTo(Appointment a1, Appointment a2)
    {
        boolean startEquals = a1.getStartTemporal().equals(a2.getStartTemporal());
        if (! startEquals)
        {
            System.out.println("startTemporal not equal:" + a1.getStartTemporal() + ", " + a2.getStartTemporal());
        }
        boolean endEquals = a1.getEndTemporal().equals(a2.getEndTemporal());
        if (! endEquals)
        {
            System.out.println("endTemporal not equal:" + a1.getEndTemporal() + ", " + a2.getEndTemporal());
        }
        boolean summaryEquals = a1.getSummary().equals(a2.getSummary());
        if (! summaryEquals)
        {
            System.out.println("summary not equal:" + a1.getSummary() + ", " + a2.getSummary());
        }
        return startEquals && endEquals && summaryEquals;
    }
}
