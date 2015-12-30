package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
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
        assertEquals(LocalTime.of(8, 0), LocalTime.from(v.getDateTimeStart()));
        
        LocalDateTimeTextField endTextField = find("#endTextField");
        System.out.println(v.getDateTimeEnd());
        endTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 9, 0));
        System.out.println(v.getDateTimeEnd());
        TestUtil.sleep(3000);
        assertEquals(LocalTime.of(9, 0), LocalTime.from(v.getDateTimeEnd()));
        
        TextField locationTextField = find("#locationTextField");
        locationTextField.setText("new location");
        assertEquals("new location", v.getLocation());
        
        TestUtil.sleep(3000);
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
