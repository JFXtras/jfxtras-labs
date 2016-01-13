package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Tests VEventImpl delete method
 * @author david
 *
 */
public class ICalendarDeleteTest extends ICalendarTestAbstract
{
    @Test
    @Ignore
    public void deleteVComponent1()
    {
//        ICalendarAgenda agenda = new ICalendarAgenda();
//        agenda.setSkin(new AgendaWeekSkin(agenda));
        // TODO - FIND WAY TO RUN LISTENER WITHOUT ALL OVERHEAD ABOVE? 
        VEventImpl vEvent = getIndividual1();
//        agenda.displayedLocalDateTime().set(LocalDateTime.of(2015, 11, 8, 0, 0));
        LocalDateTime start = LocalDateTime.of(2015, 11, 8, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 15, 0, 0);
//        agenda.vComponents().add(vEvent);
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(1, appointments.size());
        Appointment appointment = appointments.get(0);

        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                appointment.getStartLocalDateTime()
//              , appointments
              , vComponents
              , null
              , a -> true
              , null);

        refresh(vComponents, appointments); // normally, this is done by listener in ICalendarAgenda 

        assertEquals(0, vComponents.size());
        assertEquals(0, appointments.size());
    }

    @Test
    @Ignore
    public void deleteVComponent2()
    {
        VEventImpl vEvent = getIndividual1();
        VEventImpl vEvent2 = getWeekly2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 8, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 15, 0, 0);
        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent, vEvent2));
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vEvent.makeInstances(start, end);
        appointments.addAll(newAppointments);
        assertEquals(1, appointments.size());
//        vEvent2.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
//        vEvent2.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        Appointment appointment = appointments.get(0);
        Collection<Appointment> newAppointments2 = vEvent2.makeInstances(start, end);
        appointments.addAll(newAppointments2);

        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                appointment.getStartLocalDateTime()
//              , appointments
              , vComponents
              , null
              , a -> true
              , null);
        
        refresh(vComponents, appointments); // normally, this is done by listener in ICalendarAgenda 

        assertEquals(1, vComponents.size());
        assertEquals(2, appointments.size());
    }
}
