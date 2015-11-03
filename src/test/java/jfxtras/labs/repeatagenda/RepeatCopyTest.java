package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
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
        Repeat repeat = getRepeatWeekly();
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentIterator.next();
        System.out.println("a repeat3 " + appointment.getRepeat());
//        System.exit(0);
        RepeatableAppointment appointmentCopy = AppointmentFactory.newAppointment(appointment);
        assertEquals(appointment, appointmentCopy); // check number of appointments
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
    }
    
    @Test
    public void canCopyRepeat()
    {
        Repeat repeat = getRepeatWeekly();
        Repeat repeatCopy = RepeatFactory.newRepeat(repeat);
        assertEquals(repeat, repeatCopy); // check number of appointments
        
    }
    
    
}
