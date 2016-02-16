package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

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
    public void canEditAll1()
    {
        VEventImpl vEvent = getDaily2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl vComponentOriginal = new VEventImpl(vEvent);
               
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = appointmentIterator.next();
        Temporal startOriginalInstance = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        Temporal startInstance = selectedAppointment.getStartLocalDateTime();
        Temporal endInstance = selectedAppointment.getEndLocalDateTime();
        Duration startShift = Duration.between(startOriginalInstance, startInstance);
        Temporal dtStart = vEvent.getDateTimeStart().plus(startShift);
        Duration duration = Duration.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vEvent.setDateTimeStart(dtStart);
        vEvent.setDuration(duration);
        
        vEvent.handleEdit(
                  vComponentOriginal
                , vComponents
                , startOriginalInstance
                , startInstance
                , endInstance
                , appointments
                , (m) -> ChangeDialogOption.ALL);
        
        // Check start date/times
        List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 9, 45)
              , LocalDateTime.of(2015, 11, 21, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);

        // Check edited VEvent
        VEventImpl expectedVEvent = getDaily2()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 45))
                .withDurationInNanos(Duration.ofMinutes(75))
                .withSequence(1);

        assertTrue(vEventIsEqualTo(expectedVEvent, vEvent)); // check to see if repeat rule changed correctly
        assertEquals(3, appointments.size()); // check if there are only two appointments
    }

    /**
     * Tests ONE event of a daily repeat event changing date and time
     */
    @Test
    public void canEditOne1()
    {
        // Individual Appointment
        VEventImpl vEvent = getDaily2();
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl vEventOriginal = new VEventImpl(vEvent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        Appointment selectedAppointment = appointmentIterator.next();
        LocalDateTime startOriginalInstance = selectedAppointment.getStartLocalDateTime();
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plus(1, ChronoUnit.DAYS);
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        LocalDateTime startInstance = selectedAppointment.getStartLocalDateTime();
        LocalDateTime endInstance = selectedAppointment.getEndLocalDateTime();
        Duration startShift = Duration.between(startOriginalInstance, startInstance);
        Temporal dtStart = vEvent.getDateTimeStart().plus(startShift);
        Duration duration = Duration.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        vEvent.setDateTimeStart(dtStart);
        vEvent.setDuration(duration);
        
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , appointments
              , (m) -> ChangeDialogOption.ONE);

        assertEquals(2, vComponents.size());

        List<LocalDateTime> madeDates = appointments.stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 16, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);

        Collections.sort(vComponents, VComponent.VCOMPONENT_COMPARATOR);
        VEventImpl vEvent0 = (VEventImpl) vComponents.get(0);
        VEventImpl vEvent1 = (VEventImpl) vComponents.get(1);
        VEventImpl expectedVEvent0 = getDaily2()
                .withRRule(new RRule()
                        .withCount(6)
                        .withFrequency(new Daily().withInterval(3))
                        .withRecurrences(vEvent1));
        assertTrue(vEventIsEqualTo(expectedVEvent0, vEvent0));
        
        VEventImpl expectedVEvent1 = getDaily2()
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 15, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 16, 9, 45))
                .withDateTimeStamp(vEvent1.getDateTimeStamp())
                .withDurationInNanos(Duration.ofMinutes(75))
                .withRRule(null)
                .withSequence(1);
        assertTrue(vEventIsEqualTo(expectedVEvent1, vEvent1));
    }

    @Test
    public void canEditThisAndFuture1()
    {
        VEventImpl vEvent = getDaily1();
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(7, appointments.size()); // check if there are only 3 appointments
        VEventImpl vEventOriginal = new VEventImpl(vEvent);
        
        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        appointmentIterator.next(); // skip second
        Appointment selectedAppointment = appointmentIterator.next();
        LocalDateTime startOriginalInstance = selectedAppointment.getStartLocalDateTime();
        
        // apply changes
        LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(newDate.atTime(10, 30)); // change end time
        LocalDateTime startInstance = selectedAppointment.getStartLocalDateTime();
        LocalDateTime endInstance = selectedAppointment.getEndLocalDateTime();
        long startShift = ChronoUnit.NANOS.between(startOriginalInstance, startInstance);
        Temporal dtStart = vEvent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
        long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
        Temporal dtEnd = dtStart.plus(duration, ChronoUnit.NANOS);
        vEvent.setDateTimeStart(dtStart);
        vEvent.setDateTimeEnd(dtEnd);

        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , appointments
              , (m) -> ChangeDialogOption.THIS_AND_FUTURE);
        
        assertEquals(2, vComponents.size());
        
        List<LocalDateTime> madeDates = appointments.stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 9, 45)
              , LocalDateTime.of(2015, 11, 18, 9, 45)
              , LocalDateTime.of(2015, 11, 19, 9, 45)
              , LocalDateTime.of(2015, 11, 20, 9, 45)
              , LocalDateTime.of(2015, 11, 21, 9, 45)
                ));
        assertEquals(expectedDates, madeDates);
        
        Collections.sort(vComponents, VComponent.VCOMPONENT_COMPARATOR);
        VEventImpl vEvent0 = (VEventImpl) vComponents.get(0);
        VEventImpl vEvent1 = (VEventImpl) vComponents.get(1);
        VEventImpl expectedVEvent0 = getDaily1()
                .withRRule(new RRule()
                        .withFrequency(new Daily())
                        .withUntil(LocalDateTime.of(2015, 11, 16, 23, 59, 59)));
        assertTrue(vEventIsEqualTo(expectedVEvent0, vEvent0));
        
        VEventImpl expectedVEvent1 = getDaily1()
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 17, 10, 30))
                .withDateTimeStamp(vEvent1.getDateTimeStamp())
                .withDateTimeStart(LocalDateTime.of(2015, 11, 17, 9, 45))
                .withRelatedTo("20150110T080000-0@jfxtras.org")
                .withSequence(1)
                .withUniqueIdentifier(vEvent1.getUniqueIdentifier());
        assertTrue(vEventIsEqualTo(expectedVEvent1, vEvent1));
    }

    /**
     * Tests changing a repeating event to an individual one
     */
    @Test
    public void canEditAll2()
    {
        // Individual Appointment
        VEventImpl vEvent = getDaily2();
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(3, appointments.size()); // check if there are only 3 appointments
        VEventImpl vEventOriginal = new VEventImpl(vEvent);

        // select appointment (get recurrence date)
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        appointmentIterator.next(); // skip first
        Appointment selectedAppointment = appointmentIterator.next();
        
        // apply changes
        vEvent.setRRule(null);
        LocalDateTime startOriginalInstance = selectedAppointment.getStartLocalDateTime();
        LocalDateTime startInstance = selectedAppointment.getStartLocalDateTime();
        LocalDateTime endInstance = selectedAppointment.getEndLocalDateTime();

        // Edit
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , appointments
              , (m) -> ChangeDialogOption.ALL);

        List<LocalDateTime> madeDates = appointments.stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 18, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        VEventImpl expectedVEvent = getDaily2()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 18, 10, 0))
                .withRRule(null)
                .withSequence(1);
        assertTrue(vEventIsEqualTo(expectedVEvent, vEvent));
    }
    
// /**
//  * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
//  * FREQ=DAILY;INVERVAL=3;COUNT=6
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editFutureTimeAndDateDaily2()
// {
//     VEventImpl vevent = getDaily2();
//     List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<Appointment> appointments = new ArrayList<Appointment>();
//     Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
//     appointments.addAll(newAppointments);
//     assertEquals(3, appointments.size()); // check if there are only 3 appointments
//     VEventImpl veventOld = new VEventImpl(vevent);
//     
//     // select appointment and apply changes
//     Iterator<Appointment> appointmentIterator = appointments.iterator();
//     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//     LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//     LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
//     selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
//     selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//     long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDurationInNanos(duration);
//     vevent.setSummary("Edited Summary");
//     vevent.setDescription("Edited Description");
//     vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     
////     // select appointment (get recurrence date)
////     Iterator<Appointment> appointmentIterator = appointments.iterator();
////     appointmentIterator.next(); // skip first
////     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
////     LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
////     
////     // apply changes
////     LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(1); // shift appointment 1 day backward
////     selectedAppointment.setStartLocalDateTime(newDate.atTime(9, 45)); // change start time
////     selectedAppointment.setEndLocalDateTime(newDate.atTime(11, 0)); // change end time
//////     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , appointments            // collection of all appointments
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.THIS_AND_FUTURE                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime() + " " + a.getEndLocalDateTime()));
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     List<LocalDateTime> madeDates = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
//     List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 9, 45)
//           , LocalDateTime.of(2015, 11, 20, 9, 45)
//             ));
//     assertEquals(expectedDates, madeDates);
//
//     // Check Appointments
//     Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
//     Appointment editedAppointment1 = (Appointment) appointmentIteratorNew.next();
//
//     Appointment expectedAppointment1 = AppointmentFactory.newAppointment(getClazz())
//         .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
//         .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
//         .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
//         .withDescription("Daily2 Description")
//         .withSummary("Daily2 Summary");
//     assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment2 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment2 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 9, 45))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 0))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment3 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment3 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 20, 9, 45))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 20, 11, 0))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
// }
//
// 
// /**
//  * Tests editing THIS_AND_FUTURE events of a daily repeat event changing date and time
//  * FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editFutureTimeAndDateDaily6()
// {
//     // Individual Appointment
//     VEventImpl vevent = getDaily6();
//     List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     Set<Appointment> appointments = new TreeSet<Appointment>((a,b) -> a.getStartLocalDateTime().compareTo(b.getStartLocalDateTime()));
//     Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
//     appointments.addAll(newAppointments);
//     assertEquals(4, appointments.size()); // check if there are only 3 appointments
//     VEventImpl veventOld = new VEventImpl(vevent);
//     
//     // select appointment and apply changes
//     Iterator<Appointment> appointmentIterator = appointments.iterator();
//     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//     LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//     LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(2);
//     selectedAppointment.setStartLocalDateTime(date.atTime(6, 0)); // change start time
//     selectedAppointment.setEndLocalDateTime(date.atTime(7, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//     long startShift = ChronoUnit.NANOS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.NANOS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDurationInNanos(duration);
//     vevent.setSummary("Edited Summary");
//     vevent.setDescription("Edited Description");
//     vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     
////     // select appointment (get recurrence date)
////     Iterator<Appointment> appointmentIterator = appointments.iterator();
////     appointmentIterator.next(); // skip first
////     appointmentIterator.next(); // skip second
////     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
////     LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
////     
////     // apply changes
////     LocalDate newDate = selectedAppointment.getStartLocalDateTime().toLocalDate().minusDays(2); // shift appointment 2 day backward
////     selectedAppointment.setStartLocalDateTime(newDate.atTime(6, 0)); // change start time
////     selectedAppointment.setEndLocalDateTime(newDate.atTime(7, 0)); // change end time
//////     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , appointments            // collection of all appointments
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.THIS_AND_FUTURE                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime() + " " + a.getEndLocalDateTime()));
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     List<LocalDateTime> madeDates = appointments
//             .stream()
//             .map(a -> a.getStartLocalDateTime())
//             .collect(Collectors.toList());
//     List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 6, 0)
//           , LocalDateTime.of(2015, 11, 17, 10, 0)
//           , LocalDateTime.of(2015, 11, 19, 6, 0)
//           , LocalDateTime.of(2015, 11, 21, 6, 0)
//             ));
//     assertEquals(expectedDates, madeDates);
//
//     // Check Appointments
//     Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
//     Appointment editedAppointment1 = (Appointment) appointmentIteratorNew.next();
//
//     Appointment expectedAppointment1 = AppointmentFactory.newAppointment(getClazz())
//         .withStartLocalDateTime(LocalDateTime.of(2015, 11, 15, 10, 0))
//         .withEndLocalDateTime(LocalDateTime.of(2015, 11, 15, 11, 30))
//         .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
//         .withDescription("Daily6 Description")
//         .withSummary("Daily6 Summary");
//     assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment2 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment2 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 6, 0))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 7, 0))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment3 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment3 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 17, 10, 0))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 17, 11, 30))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3))
//             .withDescription("Daily6 Description")
//             .withSummary("Daily6 Summary");
//         assertEquals(expectedAppointment3, editedAppointment3); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment4 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment4 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 19, 6, 0))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 19, 7, 0))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedAppointment4, editedAppointment4); // Check to see if repeat-generated appointment changed correctly
//
//     Appointment editedAppointment5 = (Appointment) appointmentIteratorNew.next();
//     Appointment expectedAppointment5 = AppointmentFactory.newAppointment(getClazz())
//             .withStartLocalDateTime(LocalDateTime.of(2015, 11, 21, 6, 0))
//             .withEndLocalDateTime(LocalDateTime.of(2015, 11, 21, 7, 0))
//             .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7))
//             .withDescription("Edited Description")
//             .withSummary("Edited Summary");
//     assertEquals(expectedAppointment5, editedAppointment5); // Check to see if repeat-generated appointment changed correctly
//
//     // All dates check
//     LocalDateTime start2 = LocalDateTime.of(2015, 11, 1, 0, 0);
//     LocalDateTime end2 = LocalDateTime.of(2015, 12, 31, 0, 0);
//     appointments.clear();
//     appointments.addAll(vevent.makeInstances(start2, end2));
//     
//     VEventImpl veventNew = (VEventImpl) vevents.get(1);
//     appointments.addAll(veventNew.makeInstances(start2, end2));
//     List<LocalDateTime> madeDates2 = appointments.stream().map(a -> a.getStartLocalDateTime()).collect(Collectors.toList());
//     
//     List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
//             LocalDateTime.of(2015, 11, 9, 10, 0)
//           , LocalDateTime.of(2015, 11, 11, 10, 0)
//           , LocalDateTime.of(2015, 11, 13, 10, 0)
//           , LocalDateTime.of(2015, 11, 15, 10, 0)
//           , LocalDateTime.of(2015, 11, 17, 6, 0)
//           , LocalDateTime.of(2015, 11, 17, 10, 0)
//           , LocalDateTime.of(2015, 11, 19, 6, 0)
//           , LocalDateTime.of(2015, 11, 21, 6, 0)
//           , LocalDateTime.of(2015, 11, 23, 6, 0)
//           , LocalDateTime.of(2015, 11, 25, 6, 0)
//           , LocalDateTime.of(2015, 11, 27, 6, 0)
//           , LocalDateTime.of(2015, 11, 29, 6, 0)
//           ));
//     assertEquals(expectedDates2, madeDates2);
//
// }
// 
// /**
//  * Tests changing a daily repeat rule to an individual appointment
//  */
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editToIndividualDaily2()
// {
//     // Individual Appointment
//     VEventImpl vevent = getDaily2();
//     List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<Appointment> appointments = new ArrayList<Appointment>();
//     Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
//     appointments.addAll(newAppointments);
//     assertEquals(3, appointments.size()); // check if there are only 3 appointments
//     VEventImpl veventOld = new VEventImpl(vevent);
//     assertEquals(veventOld, vevent); // check to see if repeat rule changed correctly
//
//     // select appointment (get recurrence date)
//     Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
//     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//     
//     // apply changes
//     vevent.setRRule(null);
//
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             selectedAppointment.getStartLocalDateTime()
//           , selectedAppointment.getStartLocalDateTime()
//           , veventOld               // original VEvent
//           , appointments            // collection of all appointments
//           , vevents                 // collection of all VEvents
//           , null                   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//
//     VEventImpl expectedVEvent = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
//     expectedVEvent.setAppointmentClass(getClazz());
//     expectedVEvent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3));
//     expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0));
//     expectedVEvent.setDescription("Daily2 Description");
//     expectedVEvent.setDurationInNanos(5400L * NANOS_IN_SECOND);
//     expectedVEvent.setSummary("Daily2 Summary");
//     expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
//     expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly       
// }
// 
// /**
//  * Tests editing start and end time of ALL events
//  */
// @Test
// @Ignore
// public void editCancelDaily2()
// {
//     VEventImpl vevent = getDaily2();
//     LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
//     LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
//     List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     List<Appointment> appointments = new ArrayList<>();
//     Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
//     appointments.addAll(newAppointments);
//     VEventImpl veventOld = new VEventImpl(vevent);
//     
//     // select appointment and apply changes
//     Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
//     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//     LocalDateTime dateTimeOld = selectedAppointment.getStartLocalDateTime();
//     LocalDate dateOld = dateTimeOld.toLocalDate();
//     selectedAppointment.setStartLocalDateTime(dateOld.atTime(9, 45)); // change start time
//     selectedAppointment.setEndLocalDateTime(dateOld.atTime(11, 0)); // change end time
//     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOld
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , appointments            // collection of all appointments
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.CANCEL   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITHOUT_CHANGE, windowCloseType); // check to see if close type is correct
//
//     // Check edited VEvent
//     VEventImpl expectedVEvent = veventOld;
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
// }
// 
// @Test
// @Ignore // I'm removing edit and delete methods so this test is obsolete
// public void editWholeDay1()
// {
//     VEventImpl vevent = getWholeDayDaily3();
//     LocalDate start = LocalDate.of(2015, 11, 15);
//     LocalDate end = LocalDate.of(2015, 11, 22);
//     List<VComponent<Appointment>> vevents = new ArrayList<>(Arrays.asList(vevent));
//     List<Appointment> appointments = new ArrayList<>();
//     Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
//     appointments.addAll(newAppointments);
//     assertEquals(3, appointments.size()); // check if there are 3 appointments
//     VEventImpl veventOld = new VEventImpl(vevent);
//
//     // select appointment and apply changes
//     Iterator<Appointment> appointmentIterator = appointments.iterator();
//     appointmentIterator.next(); // skip first
//     appointmentIterator.next(); // skip second
//     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
//     LocalDateTime dateTimeOriginal = selectedAppointment.getStartLocalDateTime();
//     selectedAppointment.setStartLocalDateTime(selectedAppointment.getStartLocalDateTime().plusDays(1)); // change start time
//     selectedAppointment.setEndLocalDateTime(selectedAppointment.getEndLocalDateTime().plusDays(1)); // change end time
//     LocalDateTime dateTimeNew = selectedAppointment.getStartLocalDateTime();
//     long startShift = ChronoUnit.DAYS.between(dateTimeOriginal, dateTimeNew);
//     Temporal dtStart = vevent.getDateTimeStart().plus(startShift, ChronoUnit.NANOS);
//     long duration = ChronoUnit.DAYS.between(selectedAppointment.getStartLocalDateTime(), selectedAppointment.getEndLocalDateTime());
//     vevent.setDateTimeStart(dtStart);
//     vevent.setDateTimeEnd(dtStart.plus(duration, ChronoUnit.DAYS));
////     
////     // select appointment (get recurrence date) // TODO - WHAT DOES APPOINTMENT DO?  APPEARS USELESS.
////     Iterator<Appointment> appointmentIterator = appointments.iterator();
////     appointmentIterator.next(); // skip first
////     appointmentIterator.next(); // skip second
////     Appointment selectedAppointment = (Appointment) appointmentIterator.next();
////     
////     // apply changes
////     vevent.setDateTimeStart(LocalDate.of(2015, 11, 10)); // shift forward one day
////     vevent.setSummary("Edited Summary");
////     vevent.setDescription("Edited Description");
////     vevent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     
//     // Edit
//     WindowCloseType windowCloseType = vevent.edit(
//             dateTimeOriginal
//           , dateTimeNew
//           , veventOld               // original VEvent
//           , appointments            // collection of all appointments
//           , vevents                 // collection of all VEvents
//           , a -> ChangeDialogOption.ALL   // answer to edit dialog
//           , null);                  // VEvents I/O callback
//     assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
//     
//     // Check edited VEvent
//     VEventImpl expectedVEvent = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
//     expectedVEvent.setAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(7));
//     expectedVEvent.setDateTimeStart(LocalDate.of(2015, 11, 10));
//     expectedVEvent.setDateTimeEnd(LocalDate.of(2015, 11, 12));
//     expectedVEvent.setDateTimeStamp(LocalDateTime.of(2015, 1, 10, 8, 0));
//     expectedVEvent.setUniqueIdentifier("20150110T080000-0@jfxtras.org");
//     expectedVEvent.setDescription("Edited Description");
//     expectedVEvent.setSummary("Edited Summary");
//     expectedVEvent.setAppointmentClass(getClazz());
//     RRule rule = new RRule()
//             .withUntil(LocalDate.of(2015, 11, 24));
//     expectedVEvent.setRRule(rule);
//     Frequency daily = new Daily()
//             .withInterval(3);
//     rule.setFrequency(daily);
//
//     assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
//     assertEquals(3, appointments.size()); // check if there are only two appointments
// }

}
