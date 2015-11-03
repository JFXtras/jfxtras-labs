package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class RepeatCopyTest extends RepeatTestAbstract
{
    /**
     * Tests a weekly repeat by adding a new day of the week
     */
    @Test
    public void canCopyRepeatableAppointment()
    {
        Repeat repeat = getRepeatWeekly();
//        r.makeAppointments();
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);

        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentIterator.next();
        
        RepeatableAppointment appointmentCopy = AppointmentFactory.newRepeatableAppointment(appointment);
        
//        appointment.copyInto(appointmentCopy);
        assertEquals(appointment, appointmentCopy); // check number of appointments
        
    }
}
