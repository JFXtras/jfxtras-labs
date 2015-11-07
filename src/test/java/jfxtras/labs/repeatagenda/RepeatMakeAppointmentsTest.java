package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Tests if the makeAppointments is producing correct Appointments
 * 
 * @author David Bal
 *
 */
public class RepeatMakeAppointmentsTest extends RepeatTestAbstract {
    
    // Make Appointments tests
    @Test
    public void makeAppointmentsMonthly()
    {
        Repeat repeat = getRepeatMonthly();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);
        System.out.println("done");
    }
    @Test
    public void makeAppointmentsMonthlyOutsideDateBounds()
    {
        Repeat repeat = getRepeatMonthly();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 7, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        assertEquals(0, appointments.size());
    }
    @Test
    public void makeAppointmentsMonthly2()
    {
        Repeat repeat = getRepeatMonthly2();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDateTime startDate = LocalDateTime.of(2015, 12, 13, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 12, 20, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 17).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 17).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);     
    }
    @Test
    public void makeAppointmentsWeekly()
    {
        Repeat repeat = getRepeatWeekly();
        Set<RepeatableAppointment> appointments = new TreeSet<RepeatableAppointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 12, 13, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 12, 20, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<RepeatableAppointment> appointmentIterator = appointments.iterator();

        RepeatableAppointment madeAppointment1 = appointmentIterator.next();
        RepeatableAppointment expectedAppointment1 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 16).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment1, madeAppointment1);   

        RepeatableAppointment madeAppointment2 = appointmentIterator.next();
        RepeatableAppointment expectedAppointment2 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 18).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2);     
    }
    
    /**
     * Tests changing start and end date to see appointments get deleted and restored
     * Also confirms doesn't make appointments when outside repeat rule range
     */
    @Test
    public void makeAppointmentsMonthly3()
    {
        Repeat repeat = getRepeatMonthly2();
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        LocalDateTime startDate = LocalDateTime.of(2015, 12, 13, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 12, 20, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
        Appointment expectedAppointment = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 17).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 17).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment, madeAppointment);
        
        startDate = LocalDateTime.of(2015, 12, 6, 0, 0);
        endDate = LocalDateTime.of(2015, 12, 13, 0, 0);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        assertEquals(0, appointments.size());

        startDate = LocalDateTime.of(2016, 1, 17, 0, 0);
        endDate = LocalDateTime.of(2016, 1, 24, 0, 0);
        Collection<RepeatableAppointment> newAppointments2 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments2);
        assertEquals(1, appointments.size());
        
        Appointment madeAppointment2 = appointments.get(0);
        Appointment expectedAppointment2 = new RepeatableAppointmentImpl()
                .withStartLocalDateTime(LocalDate.of(2016, 1, 21).atTime(8, 45))
                .withEndLocalDateTime(LocalDate.of(2016, 1, 21).atTime(10, 15))
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2")
                .withRepeatMade(true)
                .withRepeat(repeat);
        assertEquals(expectedAppointment2, madeAppointment2); // Check to see if repeat-generated appointment changed correctly
        
        startDate = LocalDateTime.of(2015, 9, 1, 0, 0); // test dates before startDate to confirm doesn't make appointments
        endDate = LocalDateTime.of(2015, 10, 1, 0, 0);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        Collection<RepeatableAppointment> newAppointments3 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments3);
        assertEquals(0, appointments.size());

        startDate = LocalDateTime.of(2017, 9, 1, 0, 0); // test dates after endDate to confirm doesn't make appointments
        endDate = LocalDateTime.of(2017, 10, 1, 0, 0);
        repeat.removeOutsideRangeAppointments(appointments, startDate, endDate);
        Collection<RepeatableAppointment> newAppointments4 = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments4);
        assertEquals(0, appointments.size());
    }
}