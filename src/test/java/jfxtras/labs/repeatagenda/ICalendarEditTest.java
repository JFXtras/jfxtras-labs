package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities.ChangeDialogOption;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarEditTest extends ICalendarTestAbstract
{
    /**
     * Tests editing start and end time of ALL events
     */
    @Test
    public void editAllDateTimeDaily()
    {
        VEventImpl vevent = ICalendarTestAbstract.getDaily2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vevent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl vEventOriginal = new VEventImpl(vevent);
               
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = appointmentIterator.next();
        Temporal startOriginalInstance = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        Temporal startInstance = selectedAppointment.getStartLocalDateTime();
        Temporal endInstance = selectedAppointment.getEndLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(startOriginalInstance, startInstance);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);
        
        vevent.handleEdit(
                  vEventOriginal
                , vComponents
                , startOriginalInstance
                , startInstance
                , endInstance
                , appointments
                , (m) -> ChangeDialogOption.ALL);

        // Check edited VEvent
        VEventImpl expectedVEvent = new VEventImpl(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS)
                .withAppointmentClass(getClazz())
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45))
                .withDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0))
                .withDescription("Daily2 Description")
                .withDurationInNanos(4500L * NANOS_IN_SECOND)
                .withRRule(new RRule()
                        .withCount(6)
                        .withFrequency(new Daily().withInterval(3)))
                .withSequence(1)
                .withSummary("Daily2 Summary")
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org");

        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        assertEquals(3, appointments.size()); // check if there are only two appointments
        
        // Check start date/times
        List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 9, 45)
              , LocalDateTime.of(2015, 11, 21, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);
    }
}
