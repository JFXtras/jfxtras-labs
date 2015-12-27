package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.TestUtil;

public class ICalendarAgendaEditTest extends ICalendarTestAbstract
{
    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
        
        VEventImpl v = getDaily1();
//        System.out.println("start:" + agenda.getDateTimeRange().getStartLocalDateTime());
//        agenda.setDisplayedLocalDateTime(LocalDateTime.of(2015, 11, 8, 0, 0));

//        v.setDateTimeRangeStart(agenda.getDateTimeRange().getStartLocalDateTime());
//        v.setDateTimeRangeEnd(agenda.getDateTimeRange().getEndLocalDateTime());
        v.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 8, 0, 0));
        v.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 15, 0, 0));
        
//        Appointment expectedAppointment5 = AppointmentFactory.newAppointment(getClazz())
//                .withStartLocalDateTime(LocalDateTime.of(2015, 11, 9, 6, 0))
//                .withEndLocalDateTime(LocalDateTime.of(2015, 11, 9, 7, 0))
//                .withAppointmentGroup(appointmentGroups.get(7))
//                .withDescription("Edited Description")
//                .withSummary("Edited Summary");


        agenda.vComponents().add(v);
//        agenda.appointments().addAll(v.makeInstances());

        //        agenda.appointments().add(expectedAppointment5);
        return p;
    }
    
    @Test
    @Ignore
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
    
    @Test
    public void canEditDatetime2()
    {
        VEventImpl v = getDaily1();
        System.out.println("start:" + agenda.getDateTimeRange().getStartLocalDateTime());
//        agenda.setDisplayedLocalDateTime(LocalDateTime.of(2015, 11, 8, 0, 0));
        v.setDateTimeRangeStart(agenda.getDateTimeRange().getStartLocalDateTime());
        v.setDateTimeRangeEnd(agenda.getDateTimeRange().getEndLocalDateTime());
//        agenda.vComponents().add(v);
        System.out.println(agenda.appointments().size());

//        agenda.appointments().addAll(v.makeInstances());
        TestUtil.sleep(3000);
        
    }

}
