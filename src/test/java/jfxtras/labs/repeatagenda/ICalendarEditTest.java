package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarEditTest extends ICalendarTestAbstract
{

    /**
     * Tests editing start and end time of ALL events
     */
    @Test
    public void editAllDailyTime()
    {
        VEventImpl vevent = getDaily2();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        LocalDateTime dateTimeOld = selectedAppointment.getStartLocalDateTime();
        LocalDate dateOld = dateTimeOld.toLocalDate();
        selectedAppointment.setStartLocalDateTime(dateOld.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(dateOld.atTime(11, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();

        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOld
              , dateTimeNew
              , durationInSeconds
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ALL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check edited VEvent
        VEventImpl expectedVEvent = new VEventImpl();
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45));
        expectedVEvent.setDurationInSeconds(4500);
        expectedVEvent.setRRule(new RRule().withCount(6));
        expectedVEvent.getRRule().setFrequency(new Daily().withInterval(3));
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
    
    /**
     * Tests ONE event of a daily repeat event changing date and time
     */
    @Test
    public void editOneDailyTimeAndDate()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift appointment 1 day forward
        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        vevent.setSummary("Edited Summary");
        vevent.setAppointmentGroup(appointmentGroups.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
              , durationInSeconds
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ONE                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 16, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /**
     * Tests ONE event of a daily repeat event changing date and time
     */
    @Test
    public void editFutureTimeAndDateDailyRepeat()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(1); // shift appointment 1 day backward
        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        vevent.setSummary("Edited Summary");
        vevent.setAppointmentGroup(appointmentGroups.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
              , durationInSeconds
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.THIS_AND_FUTURE                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 9, 45)
              , LocalDateTime.of(2015, 11, 20, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /**
     * Tests changing a daily repeat rule to an individual appointment
     */
    @Test
    public void editToIndividualRepeatDaily()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);

        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        
        // apply changes
        vevent.setRRule(null);

        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                selectedAppointment.getStartLocalDateTime()
              , selectedAppointment.getStartLocalDateTime()
              , 0
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , null                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        VEventImpl expectedVEvent = new VEventImpl();
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        expectedVEvent.setDurationInSeconds(4500);
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        
        vevents.stream().forEach(a -> System.out.println(a.getDateTimeStart()));
    }
    
}
