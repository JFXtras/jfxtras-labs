package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
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
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK);
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
    public void canEditVEventWithPopup1()
    {
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK);
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
    
    // edit non-repeatable elements
    @Test
    public void canEditDescriptibeProperties()
    {
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarComponents.getDaily1();
        System.out.println(vevent.getCategories().size());
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

        // Get properties
        LocalDateTimeTextField startTextField = find("#startTextField");
        LocalDateTimeTextField endTextField = find("#endTextField");
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField groupTextField = find("#groupTextField");
        AppointmentGroupGridPane appointmentGroupGridPane = find("#appointmentGroupGridPane");
        
        // Check initial state
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 00), startTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2016, 5, 15, 11, 00), endTextField.getLocalDateTime());
        assertEquals("Daily1 Summary", summaryTextField.getText());
        assertEquals("Daily1 Description", descriptionTextArea.getText());
        System.out.println(vevent.getCategories().size());
        assertEquals("group05", groupTextField.getText());
        assertFalse(wholeDayCheckBox.isSelected());

        // Edit and check properties
        startTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 8, 0));
        endTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 9, 0));

        wholeDayCheckBox.setSelected(true);
        assertEquals(LocalDateTime.of(2015, 11, 11, 0, 0), startTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 12, 0, 0), endTextField.getLocalDateTime());
        wholeDayCheckBox.setSelected(false);
//        assertEquals(LocalDateTime.of(2015, 11, 11, 10, 30), vevent.getDateTimeStart().getValue());
//        assertEquals(LocalDateTime.of(2015, 11, 11, 11, 30), vevent.getDateTimeEnd().getValue());
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), startTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), endTextField.getLocalDateTime());
        
        summaryTextField.setText("new summary");
        assertEquals("new summary", vevent.getSummary());

        descriptionTextArea.setText("new description");
        assertEquals("new description", vevent.getDescription());
        
        locationTextField.setText("new location");
        assertEquals("new location", vevent.getLocation());

        TestUtil.runThenWaitForPaintPulse(() -> appointmentGroupGridPane.setAppointmentGroupSelected(11));
        assertEquals("group11", vevent.getCategories());
        
        groupTextField.setText("new group name");
        assertEquals("new group name", vevent.getCategories());
//        assertEquals("new group name", agenda.appointmentGroups().get(11).getDescription());
        
        click("#saveAppointmentButton");
        // Check appointment edited after close
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), vevent.getDateTimeStart());
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), vevent.getDateTimeEnd());
        
//        assertEquals(1, agenda.appointments().size());
//        Appointment a = agenda.appointments().get(0);
//        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), a.getStartLocalDateTime());
//        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), a.getEndLocalDateTime());
//        assertEquals("new summary", a.getSummary());
//        assertEquals("new description", a.getDescription());
//        assertEquals("new group name", a.getAppointmentGroup().getDescription());
        
//        closeCurrentWindow();
        TestUtil.sleep(3000);
    }
}
