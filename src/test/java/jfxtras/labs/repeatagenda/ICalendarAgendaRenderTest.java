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
import javafx.scene.control.Spinner;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.TestUtil;

public class ICalendarAgendaRenderTest extends ICalendarTestAbstract
{
    private String dateTimeStamp;
    
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    public void renderRegularAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T12:00"))
                .withAppointmentGroup(DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });
                
        Node n = (Node)find("#AppointmentRegularBodyPane2015-11-11/0");

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
    public void renderAppointmentByDragging()
    {;
        Assert.assertEquals(0, agenda.appointments().size());
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        
        Assert.assertEquals(1, agenda.vComponents().size());
        Assert.assertEquals("2015-11-11T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2015-11-11T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
    }

        
    @Test
    public void renderRepeatableAppointmentByDragging()
    {
        renderAppointmentByDragging();
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        // select repeat options (weekly by default)
        click("#repeatableTab");
        click("#repeatableCheckBox");
        click("#fridayCheckBox");
        click("#mondayCheckBox");
        click("#closeRepeatButton");
        
        Assert.assertEquals(2, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20151111T100000" + System.lineSeparator()
                + "RRULE:FREQ=WEEKLY;BYDAY=WE,FR,MO" + System.lineSeparator()
                + "SUMMARY:New" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
        
        List<LocalDateTime> dates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        Assert.assertEquals(expectedDates, dates);
    }
    
    @Test
    public void createRepeatableAppointment2()
    {
        renderAppointmentByDragging();
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
                
        // type value
        click("#repeatableTab");
        click("#repeatableCheckBox");
        click("#frequencyComboBox");
//        ComboBox<Frequency.FrequencyType> frequencyComboBox = (ComboBox<Frequency.FrequencyType>) find("#frequencyComboBox");
        click("Daily");
        click("#intervalSpinner");
        Spinner<Integer> interval = find("#intervalSpinner");
        interval.getEditor().selectAll();
        type("3");
        click("#endAfterRadioButton");
        click("#endAfterEventsSpinner");
        Spinner<Integer> after = find("#endAfterEventsSpinner");
        after.getEditor().selectAll();
        type("6");
        click("#closeRepeatButton");
        Assert.assertEquals(2, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20151111T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "SUMMARY:New" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
        
        List<LocalDateTime> dates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2014, 1, 1, 10, 0)
              , LocalDateTime.of(2014, 1, 4, 10, 0)
                ));
        Assert.assertEquals(expectedDates, dates);
    }
    

}
