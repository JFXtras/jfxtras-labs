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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
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
    public void editAllDateTimeDaily2()
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
        expectedVEvent.setAppointmentGroup(appointmentGroups.get(3));
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45));
        expectedVEvent.setDescription("Daily2 Description");
        expectedVEvent.setDurationInSeconds(4500);
        expectedVEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        expectedVEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        expectedVEvent.setRRule(new RRule().withCount(6));
        expectedVEvent.getRRule().setFrequency(new Daily().withInterval(3));
        expectedVEvent.setSummary("Daily2 Summary");
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
    public void editOneTimeAndDateDaily2()
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
     * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
     * FREQ=DAILY;INVERVAL=3;COUNT=6
     */
    @Test
    public void editFutureTimeAndDateDaily2()
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
        vevent.setDescription("Edited Description");
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
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime() + " " + a.getEndLocalDateTime()));
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 9, 45)
              , LocalDateTime.of(2015, 11, 20, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);

        // Check Appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();

        RepeatableAppointment expectedAppointment1 = new RepeatableAppointmentImpl()
            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withDescription("Daily2 Description")
            .withSummary("Daily2 Summary");
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 9, 45))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 20, 9, 45))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 20, 11, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }

    
    /**
     * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
     * FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000
     */
    @Test
    public void editFutureTimeAndDateDaily6()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily6();
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        Set<Appointment> appointments = new TreeSet<Appointment>((a,b) -> a.getStartLocalDateTime().compareTo(b.getStartLocalDateTime()));
        Collection<Appointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        assertEquals(4, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        appointmentIterator.next(); // skip second
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(2); // shift appointment 2 day backward
        selectedAppointment.setStartLocalDateTime(newDate.atTime(6, 0)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(7, 0)); // change end time
        int durationInSeconds = (int) ChronoUnit.SECONDS.between(
                selectedAppointment.getStartLocalDateTime()
              , selectedAppointment.getEndLocalDateTime());
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        vevent.setSummary("Edited Summary");
        vevent.setDescription("Edited Description");
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
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime() + " " + a.getEndLocalDateTime()));
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        List<LocalDateTime> madeDates = appointments
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 6, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 6, 0)
              , LocalDateTime.of(2015, 11, 21, 6, 0)
                ));
        assertEquals(expectedDates, madeDates);

        // Check Appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();

        RepeatableAppointment expectedAppointment1 = new RepeatableAppointmentImpl()
            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
            .withAppointmentGroup(appointmentGroups.get(3))
            .withDescription("Daily6 Description")
            .withSummary("Daily6 Summary");
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment2 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 7, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment3 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment3 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 10, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 30))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary");
            assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment4 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment4 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 19, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 19, 7, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment4, editedAppointment4); // Check to see if repeat-generated appointment changed correctly

        RepeatableAppointment editedAppointment5 = (RepeatableAppointment) appointmentIteratorNew.next();
        RepeatableAppointment expectedAppointment5 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 21, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 21, 7, 0))
                .withAppointmentGroup(appointmentGroups.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment5, editedAppointment5); // Check to see if repeat-generated appointment changed correctly

        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 1, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 12, 31, 0, 0));
        appointments.clear();
        appointments.addAll(vevent.makeAppointments());
        
        VEventImpl veventNew = (VEventImpl) vevents.get(1);
        veventNew.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 1, 0, 0));
        veventNew.setDateTimeRangeEnd(LocalDateTime.of(2015, 12, 31, 0, 0));
        appointments.addAll(veventNew.makeAppointments());
        
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));

    }
    
    /**
     * Tests changing a daily repeat rule to an individual appointment
     */
    @Test
    public void editToIndividualDaily2()
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
        assertEquals(veventOld, vevent); // check to see if repeat rule changed correctly

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
        expectedVEvent.setAppointmentGroup(appointmentGroups.get(3));
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        expectedVEvent.setDescription("Daily2 Description");
        expectedVEvent.setDurationInSeconds(5400);
        expectedVEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        expectedVEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        expectedVEvent.setSummary("Daily2 Summary");
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        
        vevents.stream().forEach(a -> System.out.println(a.getDateTimeStart()));
    }
    
}
