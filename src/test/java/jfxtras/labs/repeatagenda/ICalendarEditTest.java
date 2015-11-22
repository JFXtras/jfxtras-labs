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
     * Tests a daily repeat event with start and end time edit ALL events
     */
    @Test
    public void editAllDailyTime()
    {
        VEventImpl vevent = getDaily2();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
//      Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
//        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDurationInSeconds(durationInSeconds);

        // TODO - MAKE CHANGES TO VEVENT
        WindowCloseType windowCloseType = vevent.edit(
                selectedAppointment.getStartLocalDateTime()
              , null
              , durationInSeconds
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ALL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check Repeat
//        RepeatableAppointment a = new RepeatableAppointmentImpl()
//                .withAppointmentGroup(appointmentGroups.get(15))
//                .withSummary("Daily Appointment Fixed");
        VEventImpl expectedVEvent = new VEventImpl();
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 10, 7, 9, 45));
        expectedVEvent.setDurationInSeconds(4500);
        expectedVEvent.setRRule(new RRule().withCount(11));
        expectedVEvent.getRRule().setFrequency(new Daily().withInterval(3));
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        assertEquals(2, appointments.size()); // check if there are only two appointments
        
//        // Check Appointments
//        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
//        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
//
//        RepeatableAppointment expectedAppointment1 = new RepeatableAppointmentImpl()
//            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 3, 9, 45))
//            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 3, 11, 0))
//            .withAppointmentGroup(appointmentGroups.get(15))
//            .withSummary("Daily Appointment Fixed")
//            .withRepeatMade(true)
//            .withRepeat(repeat);
//        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
//
//        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
//        RepeatableAppointment expectedAppointment2 = new RepeatableAppointmentImpl()
//                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(9, 45))
//                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(11, 0))
//                .withAppointmentGroup(appointmentGroups.get(15))
//                .withSummary("Daily Appointment Fixed")
//                .withRepeatMade(true)
//                .withRepeat(repeat);
//        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }
    
    /**
     * Tests changing a daily repeat rule to an individual appointment
     */
    @Test
    public void editRepeatDailyToIndividual()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
//      Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
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
              , null
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
    
    
    /**
     * Tests a daily repeat event with day shift and start and end time edit ONE event
     */
    @Test
    public void editOneDailyTimeAndDate()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
//        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift appointment 1 day forward
        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
//        vevent.setDateTimeStart(newDate.atTime(9, 45)); // change start time
//        vevent.setDateTimeEnd(newDate.atTime(11, 0)); // change end time
        vevent.setSummary("Edited Summary");
        vevent.setAppointmentGroup(appointmentGroups.get(7));
        
//        System.out.println(vevent.getDateTimeStart());
//        System.exit(0);
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , selectedAppointment.getStartLocalDateTime()
              , durationInSeconds
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ONE                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));

        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

    }
    
}
