package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.WindowCloseType;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class RepeatDeleteTest extends RepeatTestAbstract {

    /**
     * Tests deleting one appointment from a repeat rule
     * @throws ParserConfigurationException 
     */
    @Test
    public void deleteOneWeeklyTimeAndDate() throws ParserConfigurationException
    {
        Repeat repeat = getRepeatWeeklyFixed2();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments
        newAppointments.stream().forEach(a -> System.out.println(a.getEndLocalDateTime()));
        // select appointment and apply changes (should be undone with cancel)
        Appointment selectedAppointment = appointmentIterator.next();
        
        WindowCloseType windowCloseType = RepeatableUtilities.deleteAppointments(
                selectedAppointment
              , appointments
              , repeats
              , a -> RepeatChange.ONE // delete one appointment
              , a -> true             // Are you sure - true
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(2, appointments.size()); // check number of appointments

        // Check Repeat
        RepeatableAppointment a = new RepeatableAppointmentImpl()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2");
        Repeat expectedRepeat = new RepeatImpl(RepeatableAppointmentImpl.class)
                .withStartLocalDate(LocalDateTime.of(2015, 10, 5, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withFrequency(Frequency.WEEKLY)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withInterval(2)
                .withEndCriteria(EndCriteria.AFTER)
                .withCount(50)
                .withExceptions(new HashSet<LocalDateTime>(Arrays.asList(LocalDateTime.of(2015, 11, 2, 8, 45))))
                .withAppointmentData(a);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        
        // Check appointments
        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();

        Appointment editedAppointment1 = appointmentIteratorNew.next();
        Appointment expectedAppointment1 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
                       
        Appointment editedAppointment2 = appointmentIteratorNew.next();
        Appointment expectedAppointment2 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }

    /**
     * Tests deleting one appointment from a repeat rule
     * @throws ParserConfigurationException 
     */
    @Test
    public void deleteAllWeeklyTimeAndDate() throws ParserConfigurationException
    {
        Repeat repeat = getRepeatWeeklyFixed2();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        assertEquals(3, appointments.size()); // check number of appointments

        // select appointment and apply changes (should be undone with cancel)
        Appointment selectedAppointment = appointmentIterator.next();
        
        WindowCloseType windowCloseType = RepeatableUtilities.deleteAppointments(
                selectedAppointment
              , appointments
              , repeats
              , a -> RepeatChange.ALL // delete one appointment
              , a -> true             // Are you sure - true
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct
        assertEquals(0, appointments.size()); // check number of appointments
        assertEquals(0, repeats.size()); // check number of repeats

        System.out.println(repeat);
    }

}