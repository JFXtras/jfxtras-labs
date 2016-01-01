package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class AgendaRenderAppointmentsTest extends ICalendarTestAbstract
{   
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void renderRegularAppointment()
    {
        String dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T12:00"))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });

        Node n = (Node)find("#AppointmentRegularBodyPane2015-11-11/0");
        System.out.println("getParent:" + n.getParent());

//      AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        String os = System.getProperty("os.name");
        if (os.equals("Linux"))
        {
            new AssertNode(n).assertXYWH(0.5, 402.5, 124.0, 81.0, 0.01);
        } else
        {
            new AssertNode(n).assertXYWH(0.5, 419.5, 125.0, 84.0, 0.01);
        }
        
        Assert.assertEquals(1, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20151111T100000" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
    }  

}
