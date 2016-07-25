package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.test.TestUtil;

public class MakeNewVEventsTest extends AgendaTestAbstract
{
    @Test
    public void canCreateSimpleVEvent()
    {
        // Draw new appointment
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id

        // find and edit properties
        TextField summaryTextField = (TextField) find("#summaryTextField");
        summaryTextField.setText("Edited summary");
        ComboBox<AppointmentGroup> appointmentGroupComboBox = find("#appointmentGroupComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> appointmentGroupComboBox.setValue(agenda.appointmentGroups().get(10)) );
        
        // create event
        click("#newAppointmentCreateButton");
        
        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = new VEvent()
                .withSummary("Edited summary")
                .withCategories("group10")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 11, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 12, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                ;
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canCreateAdvancedVEvent()
    {
        // Draw new appointment
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
        
        // do advanced edit
        click("#newAppointmentEditButton");
        TextField locationTextField = find("#locationTextField");
        locationTextField.setText("new location");
        click("#recurrenceRuleTab");
        click("#repeatableCheckBox");
        click("#saveRepeatButton");
        
        // verify event's creation
        assertEquals(1, agenda.getVCalendar().getVEvents().size());
        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
        VEvent expectedVEvent = new VEvent()
                .withSummary("New")
                .withCategories("group00")
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 11, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 12, 00).atZone(ZoneId.systemDefault()))
                .withDateTimeCreated(vEvent.getDateTimeCreated())
                .withDateTimeStamp(vEvent.getDateTimeStamp())
                .withUniqueIdentifier(vEvent.getUniqueIdentifier())
                .withLocation("new location")
                .withRecurrenceRule("RRULE:FREQ=WEEKLY;BYDAY=WE")
                ;
        System.out.println(vEvent.toContent());
        assertEquals(expectedVEvent, vEvent);
    }
}
