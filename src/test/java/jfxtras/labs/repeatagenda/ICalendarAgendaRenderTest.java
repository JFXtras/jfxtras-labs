package jfxtras.labs.repeatagenda;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class ICalendarAgendaRenderTest extends ICalendarTestAbstract
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

//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        String os = System.getProperty("os.name");
        if (os.equals("Linux"))
        {
            new AssertNode(n).assertXYWH(0.5, 402.5, 124.0, 81.0, 0.01);
        } else
        {
            new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        }
    }
}
