package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.TestUtil;

/**
 * Tests adding and removing VComponents outside the ICalendarAgenda implementation.
 * Inside ICalendarAgenda adding and removing VComponents is handled by removing
 * instances (Appointments) by Agenda through the popups.
 * 
 * The change vComponentsListener should only fire by changes made outside the implementation.
 *
 */
public class EditPopupTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
//        agenda.vComponents().add(getIndividual2());
        return p;
    }
    
    @Test
    public void canEditVComponent1()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.vComponents().add(getDaily1());
        });
        
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);
        
        // Open edit popup
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        LocalDateTimeTextField startTextField = find("#startTextField");
        startTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 8, 0));
        assertEquals(LocalDateTime.of(2015, 11, 9, 8, 0), LocalDateTime.from(v.getDateTimeStart()));
        
        LocalDateTimeTextField endTextField = find("#endTextField");
        endTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 9, 0));
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), LocalDateTime.from(v.getDateTimeEnd()));
        
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        wholeDayCheckBox.setSelected(true);
        assertEquals(LocalDate.of(2015, 11, 9), LocalDate.from(v.getDateTimeStart()));
        assertEquals(LocalDate.of(2015, 11, 10), LocalDate.from(v.getDateTimeEnd()));
        wholeDayCheckBox.setSelected(false);
        assertEquals(LocalDateTime.of(2015, 11, 9, 8, 0), LocalDateTime.from(v.getDateTimeStart()));
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), LocalDateTime.from(v.getDateTimeEnd()));
        
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        assertEquals("new summary", v.getSummary());

        TextArea descriptionTextArea = find("#descriptionTextArea");
        descriptionTextArea.setText("new description");
        assertEquals("new description", v.getDescription());
        
        TextField locationTextField = find("#locationTextField");
        locationTextField.setText("new location");
        assertEquals("new location", v.getLocation());

        TextField groupTextField = find("#groupTextField");
        groupTextField.setText("new group name");
        assertEquals("new group name", v.getCategories());

        AppointmentGroupGridPane appointmentGroupGridPane = find("#appointmentGroupGridPane");
        appointmentGroupGridPane.setAppointmentGroupSelected(11);
        assertEquals("group11", v.getCategories());
//        TestUtil.sleep(3000);
    }
    
    @Test
    @Ignore
    public void canEditVComponent2()
    {
        String dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T12:00"))
                .withAppointmentGroup(DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });
        
        System.out.println(agenda.appointments().size());

        
        TestUtil.sleep(3000);
    }

}
