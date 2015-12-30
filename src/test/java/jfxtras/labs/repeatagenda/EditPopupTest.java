package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;

import org.junit.Test;

import javafx.scene.Parent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
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
            agenda.vComponents().add(getIndividual1()); // doesn't work.  why?
        });
        
        System.out.println(agenda.appointments().size());

        
        TestUtil.sleep(3000);
    }
    
    @Test
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
