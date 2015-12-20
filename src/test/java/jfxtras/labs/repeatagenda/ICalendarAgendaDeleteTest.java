package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarAgendaDeleteTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
        
    @Test
    @Ignore // TODO - FIX, FIGURE OUT TESTFX
    public void deleteVComponent1()
    {
        ICalendarAgenda agenda = new ICalendarAgenda();
        agenda.setSkin(new AgendaWeekSkin(agenda));
        // TODO - FIND WAY TO RUN LISTENER WITHOUT ALL OVERHEAD ABOVE? 
        VEventImpl vEvent = getIndividual1();
        agenda.displayedLocalDateTime().set(LocalDateTime.of(2015, 11, 8, 0, 0));
//        vEvent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
//        vEvent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        agenda.vComponents().add(vEvent);
//        List<VComponent<Appointment>> vComponents = new ArrayList<>(Arrays.asList(vEvent));
//        List<Appointment> appointments = new ArrayList<Appointment>();
//        Collection<Appointment> newAppointments = vEvent.makeInstances();
//        appointments.addAll(newAppointments);
        assertEquals(1, agenda.appointments().size());
        Appointment appointment = agenda.appointments().get(0);

        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                appointment.getStartLocalDateTime()
//              , appointments
              , agenda.vComponents()
              , null
              , a -> true
              , null);
        
        assertEquals(0, agenda.vComponents().size());
        assertEquals(0, agenda.appointments().size());
    }
}
