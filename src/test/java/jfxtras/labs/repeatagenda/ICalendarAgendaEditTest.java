package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarAgendaEditTest extends ICalendarTestAbstract
{
    @Test
    public void createAppointmentByDraggingAndEdit()
    {
        Assert.assertEquals(0, agenda.appointments().size() );
        
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        
        Assert.assertEquals(1, agenda.vComponents().size());

        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

        Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        
        find("#AppointmentRegularBodyPane2014-01-01/0"); // validate that the pane has the expected id
        
        // type value
        TextField summary = find("#summaryTextField");
        click("#summaryTextField");
        summary.selectAll();
        click("#summaryTextField").type("edited summary");
        click("#closeAppointmentButton"); // change focus
        
        Assert.assertEquals(1, agenda.appointments().size());
        Appointment a = agenda.appointments().get(0);
        assertEquals ("edited summary", a.getSummary());
//        TestUtil.sleep(3000);
    }
}
