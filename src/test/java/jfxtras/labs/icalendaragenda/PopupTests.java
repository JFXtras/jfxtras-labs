package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.AssertNode;
import jfxtras.test.JFXtrasGuiTest;
import jfxtras.test.TestUtil;

/**
 * Tests the edit popup without an Agenda instance.
 * 
 * @author David Bal
 *
 */
public class PopupTests extends JFXtrasGuiTest
{
    EditVEventTabPane appointmentPopup;
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        appointmentPopup = new EditVEventTabPane();
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        appointmentPopup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return appointmentPopup;
    }
    
    @Test
    public void canDisplayPopup()
    {
        Node n = find("#editDisplayableTabPane");
        //AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 600.0, 0.01);
    }
    
    @Test
    public void canDisplayPopupWithVEvent()
    {
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        VEvent vevent = ICalendarComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            appointmentPopup.setupData(
                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        });

        TextField summary = find("#summaryTextField");
        assertEquals(vevent.getSummary().getValue(), summary.getText());

        LocalDateTimeTextField start = find("#startTextField");
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 0), start.getLocalDateTime());
    }
    
    @Test
    public void makeEditPopup2()
    {
        final VCalendar calendar = new VCalendar();
        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(c -> System.out.println("Added:" + c.getSummary().getValue()));
                    // Note: making recurrences should be done here by a similar listener in production code
                }
            }
        };
        calendar.getVEvents().addListener(listener);
        
        final Collection<Appointment> appointments = new ArrayList<>();
        final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();    
        final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */

        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
//                appointments,
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK
//                vComponentAppointmentMap,
//                appointmentVComponentMap
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        VEvent vevent = ICalendarComponents.getDaily1();
        calendar.getVEvents().add(vevent);
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {            
            
            Stage editPopup = new Stage();
//            EditVEventDescriptiveVBox appointmentPopup = new EditVEventDescriptiveVBox();
//            EditRecurrenceRuleVBox appointmentPopup = new EditRecurrenceRuleVBox();
            EditVEventTabPane appointmentPopup = new EditVEventTabPane();
            Scene scene = new Scene(appointmentPopup);
            String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
            appointmentPopup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
            editPopup.setScene(scene);
            
            appointmentPopup.setupData(
                    appointment,
                    vevent,
                    calendar.getVEvents(),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
            
//        Stage editPopup = new EditVEventPopup(
//              appointment,
//              appointments,
//              vevent,
//              calendar.getVEvents(),
//              ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS
//              );

        editPopup.show();
        });
        
        Node n = find("#editDisplayableTabPane");
        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 600.0, 0.01);
//        closeCurrentWindow();
        
//        TestUtil.sleep(3000);
    }
}
