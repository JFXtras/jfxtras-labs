package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
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
    
    @Test
    public void createRepeatableAppointmentByDragging()
    {
        Assert.assertEquals(0, agenda.appointments().size());
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        String dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        Assert.assertEquals(1, agenda.vComponents().size());

        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

        Assert.assertEquals("2014-01-01T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2014-01-01T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        
        find("#AppointmentRegularBodyPane2014-01-01/0"); // validate that the pane has the expected id
        
        // type value
        click("#repeatableTab");
        click("#repeatableCheckBox");
        click("#fridayCheckBox");
        click("#mondayCheckBox");
        click("#closeRepeatButton");
        
        Assert.assertEquals(2, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20140101T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20140101T100000" + System.lineSeparator()
                + "RRULE:FREQ=WEEKLY;BYDAY=WE,FR,MO" + System.lineSeparator()
                + "SUMMARY:New" + System.lineSeparator()
                + "UID:20140101T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
        
        List<LocalDateTime> dates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2014, 1, 1, 10, 0)
              , LocalDateTime.of(2014, 1, 3, 10, 0)
                ));
        Assert.assertEquals(expectedDates, dates);
    }
    

}
