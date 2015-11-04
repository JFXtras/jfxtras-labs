package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class RepeatCopyTest extends RepeatTestAbstract
{
    @Test
    public void canCopyRepeatableAppointment()
    {
        Set<Appointment> appointments = getRepeatWeeklyFixedAppointments(
                LocalDateTime.of(2015, 11, 1, 0, 0)
              , LocalDateTime.of(2015, 11, 8, 0, 0));
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentCopy = AppointmentFactory.newAppointment(appointment);
        assertEquals(appointment, appointmentCopy); // check number of appointments
        assertTrue(appointment != appointmentCopy); // ensure not same reference
    }

    @Test
    public void canCopyNonRepeatableAppointment()
    {
//        RepeatableAppointment appointment = new RepeatableAppointmentImpl()
        RepeatableAppointment appointment = AppointmentFactory.newAppointment(RepeatableAppointmentImpl.class)
                .withStartLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 11, 4).atTime(18, 45))
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed")
                .withRepeatMade(true);
//        System.out.println("a class5 " + appointment.getClass());
        RepeatableAppointment appointmentCopy = AppointmentFactory.newAppointment(appointment);
        assertEquals(appointment, appointmentCopy); // check number of appointments
        assertTrue(appointment != appointmentCopy); // ensure not same reference
    }
    
    @Test
    public void canCopyRepeat()
    {
        Repeat repeat = getRepeatWeekly();
        Repeat repeatCopy = RepeatFactory.newRepeat(repeat);
        assertEquals(repeat, repeatCopy); // check number of appointments
        assertTrue(repeat != repeatCopy); // insure not same reference
    }
    
    // simulates canceling changes
    @Test
    public void canCopyFieldsToRepeatableAppointment()
    {
        Set<Appointment> appointments = getRepeatWeeklyFixedAppointments(
                LocalDateTime.of(2015, 11, 1, 0, 0)
              , LocalDateTime.of(2015, 11, 8, 0, 0));
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentIterator.next();
//        System.out.println(appointment.getClass());
//        System.exit(0);
        RepeatableAppointment appointmentCopy = AppointmentFactory.newAppointment(appointment);

        // apply changes to appointmentCopy
        LocalDate date = appointmentCopy.getStartLocalDateTime().toLocalDate().plusDays(1); // shift Wednesday appointments 1 day forward
        appointmentCopy.setStartLocalDateTime(date.atTime(3, 45)); // change start time
        appointmentCopy.setEndLocalDateTime(date.atTime(5, 10)); // change end time
        appointmentCopy.setSummary("Changed summary");
        appointmentCopy.setAppointmentGroup(appointmentGroups.get(12));
        Repeat repeat = appointmentCopy.getRepeat();
        repeat.setInterval(2);
        repeat.setDayOfWeek(DayOfWeek.FRIDAY, false);
        repeat.setDayOfWeek(DayOfWeek.THURSDAY, true);
        repeat.setEndCriteria(EndCriteria.UNTIL);
        repeat.setUntilLocalDateTime(LocalDateTime.of(2016, 11, 1, 0, 0));
        
        // Copy original back and check equality
        appointment.copyFieldsTo(appointmentCopy);
//        System.out.println(appointment.getRepeat().getEndCriteria());
//        System.out.println(appointmentCopy.getRepeat().getEndCriteria());
//        assertEquals(appointment.getRepeat(), appointmentCopy.getRepeat()); // check number of appointments        
//        System.exit(0);
        assertEquals(appointment, appointmentCopy); // check original and restored appointment equality
        assertTrue(appointment != appointmentCopy); // ensure not same reference

    }
    
}
