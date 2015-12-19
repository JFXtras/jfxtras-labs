package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.internal.scene.control.skin.agenda.AgendaWeekSkin;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class ICalendarAgendaDeleteTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void renderRegularAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2014-01-01T12:00"))
                .withAppointmentGroup(DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });
                
        Node n = (Node)find("#AppointmentRegularBodyPane2014-01-01/0");
//        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
//        new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        new AssertNode(n).assertXYWH(0.5, 402.5, 124.0, 81.0, 0.01);
    }
    
    @Test
    public void createAppointmentByDragging()
    {
        Assert.assertEquals(0, agenda.appointments().size() );
        
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        
        Assert.assertEquals(1, agenda.appointments().size() );
        Appointment a =  agenda.appointments().get(0);

        move("#hourLine11");
        press(MouseButton.SECONDARY);
        

        Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        
        find("#AppointmentRegularBodyPane2014-01-01/0"); // validate that the pane has the expected id
        TestUtil.sleep(3000);
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
