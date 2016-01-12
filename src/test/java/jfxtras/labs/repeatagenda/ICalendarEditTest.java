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
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarEditTest extends ICalendarTestAbstract
{
    
    // Ensures duration is automatically updated correctly
    @Test // LocalDateTime
    public void canEditDuration()
    {
        VEventImpl v = getDaily1();
        Long expectedDuration = 7200L * NANOS_IN_SECOND;
        v.setDurationInNanos(expectedDuration);
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), v.getDateTimeStart());
        assertEquals(LocalDateTime.of(2015, 11, 9, 12, 0), v.getDateTimeEnd());
    }
    
    @Test // LocalDate
    public void canEditDuration2()
    {
        VEventImpl v = getIndividual2();
        Long expectedDuration = 48L * 3600L * NANOS_IN_SECOND;
        v.setDurationInNanos(expectedDuration);
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDate.of(2015, 11, 11), v.getDateTimeStart());
        assertEquals(LocalDate.of(2015, 11, 13), v.getDateTimeEnd());
    }

    @Test // LocalDateTime
    public void canEditDateTimeEnd()
    {
        VEventImpl v = getDaily1();
        v.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 12, 0));
        Long expectedDuration = 7200L * NANOS_IN_SECOND;
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), v.getDateTimeStart());
        assertEquals(LocalDateTime.of(2015, 11, 9, 12, 0), v.getDateTimeEnd());
    }
    
    @Test // LocalDate
    public void canEditDateTimeEnd2()
    {
        VEventImpl v = getIndividual2();
        v.setDateTimeEnd(LocalDate.of(2015, 11, 13));
        Long expectedDuration = 48L * 3600L * NANOS_IN_SECOND;
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDate.of(2015, 11, 11), v.getDateTimeStart());
        assertEquals(LocalDate.of(2015, 11, 13), v.getDateTimeEnd());
    }
    
    @Test // LocalDateTime
    public void canEditDateTimeStart()
    {
        VEventImpl v = getDaily1();
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 12, 0));
        Long expectedDuration = 3600L * NANOS_IN_SECOND;
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDateTime.of(2015, 11, 9, 12, 0), v.getDateTimeStart());
        assertEquals(LocalDateTime.of(2015, 11, 9, 13, 0), v.getDateTimeEnd());
    }
    
    @Test // LocalDate
    public void canEditDateTimeStart2()
    {
        VEventImpl v = getIndividual2();
        v.setDateTimeStart(LocalDate.of(2015, 11, 13));
        Long expectedDuration = 24L * 3600L * NANOS_IN_SECOND;
        assertEquals(expectedDuration, v.getDurationInNanos());
        assertEquals(LocalDate.of(2015, 11, 13), v.getDateTimeStart());
        assertEquals(LocalDate.of(2015, 11, 14), v.getDateTimeEnd());
    }
    
    /**
     * Tests editing start and end time of ALL events
     */
    @Test
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editAllDateTimeDaily2()
    {
        VEventImpl vevent = getDaily2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
               
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);

        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ALL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check edited VEvent
        VEventImpl expectedVEvent = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3));
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45));
        expectedVEvent.setDescription("Daily2 Description");
        expectedVEvent.setDurationInNanos(4500L * NANOS_IN_SECOND);
//        expectedVEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
//        expectedVEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        expectedVEvent.setRRule(new RRule().withCount(6));
        expectedVEvent.getRRule().setFrequency(new Daily().withInterval(3));
        expectedVEvent.setSummary("Daily2 Summary");
        expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
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
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editOneTimeAndDateDaily2()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);
        
//        // select appointment (get recurrence date)
//        Iterator<Appointment> appointmentIterator = appointments.iterator();
//        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//        
//        // apply changes
//        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(1); // shift appointment 1 day forward
//        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
//        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
//        vevent.setSummary("Edited Summary");
//        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
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
        
        // TODO - CHECK RECURRENCE ID
    }
    
    @Test  // TODO - FINISH TEST
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editFutureTimeAndDateDaily1()
    {
        VEventImpl vevent = getDaily1();
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(7, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        appointmentIterator.next(); // skip second
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.THIS_AND_FUTURE                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime() + " " + a.getEndLocalDateTime()));
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

    }
    
    /**
     * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
     * FREQ=DAILY;INVERVAL=3;COUNT=6
     */
    @Test
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editFutureTimeAndDateDaily2()
    {
        VEventImpl vevent = getDaily2();
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);
        vevent.setSummary("Edited Summary");
        vevent.setDescription("Edited Description");
        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
//        // select appointment (get recurrence date)
//        Iterator<Appointment> appointmentIterator = appointments.iterator();
//        appointmentIterator.next(); // skip first
//        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//        
//        // apply changes
//        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(1); // shift appointment 1 day backward
//        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
//        selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
////        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//        vevent.setSummary("Edited Summary");
//        vevent.setDescription("Edited Description");
//        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
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
        Appointment editedAppointment1 = (Appointment) appointmentIteratorNew.next();

        Appointment expectedAppointment1 = AppointmentFactory.newAppointment(getClazz())
            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
            .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
            .withDescription("Daily2 Description")
            .withSummary("Daily2 Summary");
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment2 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 9, 45))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 0))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment3 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 20, 9, 45))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 20, 11, 0))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
    }

    
    /**
     * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
     * FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000
     */
    @Test
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editFutureTimeAndDateDaily6()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily6();
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        Set<Appointment> appointments = new TreeSet<Appointment>((a,b) -> a.getStartLocalDateTime().compareTo(b.getStartLocalDateTime()));
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(4, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(2);
        selectedAppointment.setStartLocalDateTime(date.atTime(6, 0)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(7, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDurationInNanos(duration);
        vevent.setSummary("Edited Summary");
        vevent.setDescription("Edited Description");
        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
//        // select appointment (get recurrence date)
//        Iterator<Appointment> appointmentIterator = appointments.iterator();
//        appointmentIterator.next(); // skip first
//        appointmentIterator.next(); // skip second
//        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//        
//        // apply changes
//        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(2); // shift appointment 2 day backward
//        selectedAppointment.setStartLocalDateTime(newDate.atTime(6, 0)); // change start time
//        selectedAppointment.setEndLocalDateTime(newDate.atTime(7, 0)); // change end time
////        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//        vevent.setSummary("Edited Summary");
//        vevent.setDescription("Edited Description");
//        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
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
        Appointment editedAppointment1 = (Appointment) appointmentIteratorNew.next();

        Appointment expectedAppointment1 = AppointmentFactory.newAppointment(getClazz())
            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
            .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
            .withDescription("Daily6 Description")
            .withSummary("Daily6 Summary");
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment2 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 7, 0))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment3 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 10, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 30))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
                .withDescription("Daily6 Description")
                .withSummary("Daily6 Summary");
            assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment4 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment4 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 19, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 19, 7, 0))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment4, editedAppointment4); // Check to see if repeat-generated appointment changed correctly

        Appointment editedAppointment5 = (Appointment) appointmentIteratorNew.next();
        Appointment expectedAppointment5 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 21, 6, 0))
                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 21, 7, 0))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
                .withDescription("Edited Description")
                .withSummary("Edited Summary");
        assertEquals(expectedAppointment5, editedAppointment5); // Check to see if repeat-generated appointment changed correctly

        // All dates check
        LocalDateTime start2 = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime end2 = LocalDateTime.of(2015, 12, 31, 0, 0);
        appointments.clear();
        appointments.addAll(vevent.makeInstances(start2, end2));
        
        VEventImpl veventNew = (VEventImpl) vevents.get(1);
        appointments.addAll(veventNew.makeInstances(start2, end2));
        List<LocalDateTime> madeDates2 = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 6, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 6, 0)
              , LocalDateTime.of(2015, 11, 21, 6, 0)
              , LocalDateTime.of(2015, 11, 23, 6, 0)
              , LocalDateTime.of(2015, 11, 25, 6, 0)
              , LocalDateTime.of(2015, 11, 27, 6, 0)
              , LocalDateTime.of(2015, 11, 29, 6, 0)
              ));
        assertEquals(expectedDates2, madeDates2);

    }
    
    /**
     * Tests changing a daily repeat rule to an individual appointment
     */
    @Test
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editToIndividualDaily2()
    {
        // Individual Appointment
        VEventImpl vevent = getDaily2();
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);
        assertEquals(veventOld, vevent); // check to see if repeat rule changed correctly

        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        
        // apply changes
        vevent.setRRule(null);

        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                selectedAppointment.getStartLocalDateTime()
              , selectedAppointment.getStartLocalDateTime()
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , null                   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        VEventImpl expectedVEvent = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3));
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
        expectedVEvent.setDescription("Daily2 Description");
        expectedVEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
        expectedVEvent.setSummary("Daily2 Summary");
        expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly       
    }
    
    /**
     * Tests editing start and end time of ALL events
     */
    @Test
    public void editCancelDaily2()
    {
        VEventImpl vevent = getDaily2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        List<Appointment> appointments = new ArrayList<>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOld = selectedAppointment.getStartLocalDateTime();
        LocalDate dateOld = dateTimeOld.toLocalDate();
        selectedAppointment.setStartLocalDateTime(dateOld.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(dateOld.atTime(11, 0)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();

        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOld
              , dateTimeNew
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.CANCEL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITHOUT_CHANGE, windowCloseType); // check to see if close type is correct

        // Check edited VEvent
        VEventImpl expectedVEvent = veventOld;
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
    }
    
    @Test
    @Ignore // I'm removing edit and delete methods so this test is obsolete
    public void editWholeDay1()
    {
        VEventImpl vevent = getWholeDayDaily3();
        LocalDate start = LocalDate.of(2015, 11, 15);
        LocalDate end = LocalDate.of(2015, 11, 22);
        List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
        List<Appointment> appointments = new ArrayList<>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are 3 appointments
        VEventImpl veventOld = new VEventImpl(vevent);

        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        appointmentIterator.next(); // skip second
        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
        LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
        selectedAppointment.setStartLocalDateTime(selectedAppointment.getStartLocalDateTime().plusDays(1)); // change start time
        selectedAppointment.setEndLocalDateTime(selectedAppointment.getEndLocalDateTime().plusDays(1)); // change end time
        LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
        long startShift = ChronoUnit.DAYS.between(dateTimeOriginal, dateTimeNew);
        Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.DAYS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vevent.setDateTimeStart(dtStart);
        vevent.setDateTimeEnd(dtStart.plus(duration, ChronoUnit.DAYS));
//        
//        // select appointment (get recurrence date) // TODO - WHAT DOES APPOINTMENT DO?  APPEARS USELESS.
//        Iterator<Appointment> appointmentIterator = appointments.iterator();
//        appointmentIterator.next(); // skip first
//        appointmentIterator.next(); // skip second
//        Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//        
//        // apply changes
//        vevent.setDateTimeStart(LocalDate.of(2015, 11, 10)); // shift forward one day
//        vevent.setSummary("Edited Summary");
//        vevent.setDescription("Edited Description");
//        vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        
        // Edit
        WindowCloseType windowCloseType = vevent.edit(
                dateTimeOriginal
              , dateTimeNew
              , veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> ChangeDialogOptions.ALL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        
        // Check edited VEvent
        VEventImpl expectedVEvent = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        expectedVEvent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
        expectedVEvent.setDateTimeStart(LocalDate.of(2015, 11, 10));
        expectedVEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
        expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
        expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
        expectedVEvent.setDescription("Edited Description");
        expectedVEvent.setSummary("Edited Summary");
        expectedVEvent.setAppointmentClass(getClazz());
        RRule rule = new RRule()
                .withUntil(LocalDate.of(2015, 11, 24));
        expectedVEvent.setRRule(rule);
        Frequency daily = new Daily()
                .withInterval(3);
        rule.setFrequency(daily);

        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        assertEquals(3, appointments.size()); // check if there are only two appointments
    }
}
