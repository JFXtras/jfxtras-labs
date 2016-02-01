package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaEditUtilities.ChangeDialogOption;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.TestUtil;

public class AgendaChangeTest extends AgendaTestAbstract
{
    @Override
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    @Test
    //@Ignore
    public void canDragAndDropAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(ICalendarTestAbstract.getDaily1()));
        
        // move one appointment
        assertFind("#AppointmentRegularBodyPane2015-11-11/0");
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine15");
        release(MouseButton.PRIMARY);

        // change dialog
        ComboBox<ChangeDialogOption> c = find("#edit_dialog_combobox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#edit_dialog_button_ok");

        // check return to original state
        assertEquals(2, agenda.vComponents().size());
        assertEquals(6, agenda.appointments().size());
        Collections.sort(agenda.vComponents(), VComponent.VCOMPONENT_COMPARATOR);
        VComponent<Appointment> v1 = agenda.vComponents().get(0);
        VComponent<Appointment> v2 = agenda.vComponents().get(1);
        RRule r = ICalendarTestAbstract.getDaily1().getRRule().withRecurrences(v2);
        VComponent<Appointment> expectedV1 = ICalendarTestAbstract.getDaily1()
                .withRRule(r);
        assertEquals(expectedV1, v1);
        
        VComponent<Appointment> expectedV2 = ICalendarTestAbstract.getDaily1()
                .withRRule(null)
                .withDateTimeRecurrence(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 14, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 15, 0))
                .withDateTimeStamp(v2.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedV2, v2);
    }
    
    @Test
    //@Ignore
    public void canExpandAppoindment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(ICalendarTestAbstract.getDaily1()));
        
        // expand time
        move("#AppointmentRegularBodyPane2015-11-11/0 .DurationDragger"); 
        press(MouseButton.PRIMARY);
        move("#hourLine15");
        release(MouseButton.PRIMARY);
        
        // delete VComponent
        ComboBox<ChangeDialogOption> c = find("#edit_dialog_combobox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#edit_dialog_button_ok");

        // check return to original state
        assertEquals(1, agenda.vComponents().size());
        agenda.appointments().stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
        assertEquals(6, agenda.appointments().size());
        VComponent<Appointment> v1 = agenda.vComponents().get(0);
        VComponent<Appointment> expectedV1 = ICalendarTestAbstract.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 14, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 15, 0));
        System.out.println(expectedV1);
        System.out.println(v1);
        assertEquals(expectedV1, v1);
        
    }
}
