package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Pair;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.LocalDateTextField;
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
    EditVEventTabPane editComponentPopup;
    
    @Override
    public Parent getRootNode()
    {
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", Locale.getDefault());
        Settings.setup(resources);
        editComponentPopup = new EditVEventTabPane();
        String agendaSheet = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
        editComponentPopup.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET, agendaSheet);
        return editComponentPopup;
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
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
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
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        });

        TextField summary = find("#summaryTextField");
        assertEquals(vevent.getSummary().getValue(), summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
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
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        });

        // Get properties
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        LocalDateTimeTextField endDateTimeTextField = find("#endDateTimeTextField");
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField groupTextField = find("#groupTextField");
        AppointmentGroupGridPane appointmentGroupGridPane = find("#appointmentGroupGridPane");
        
        // Check initial state
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 00), startDateTimeTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2016, 5, 15, 11, 00), endDateTimeTextField.getLocalDateTime());
        assertTrue(startDateTimeTextField.isVisible());
        assertTrue(endDateTimeTextField.isVisible());
        assertEquals("Daily1 Summary", summaryTextField.getText());
        assertEquals("Daily1 Description", descriptionTextArea.getText());
        assertEquals("group05", groupTextField.getText());
        assertFalse(wholeDayCheckBox.isSelected());

        // Edit and check properties
        TestUtil.runThenWaitForPaintPulse( () -> wholeDayCheckBox.setSelected(true) );
        LocalDateTextField startDateTextField = find("#startDateTextField");
        LocalDateTextField endDateTextField = find("#endDateTextField");
        assertEquals(LocalDate.of(2016, 5, 15), startDateTextField.getLocalDate());
        assertEquals(LocalDate.of(2016, 5, 16), endDateTextField.getLocalDate());
        assertTrue(startDateTextField.isVisible());
        assertTrue(endDateTextField.isVisible());
        TestUtil.runThenWaitForPaintPulse( () -> wholeDayCheckBox.setSelected(false) );
        assertTrue(startDateTimeTextField.isVisible());
        assertTrue(endDateTimeTextField.isVisible());
        
        summaryTextField.setText("new summary");
        assertEquals("new summary", vevent.getSummary().getValue());

        descriptionTextArea.setText("new description");
        assertEquals("new description", vevent.getDescription().getValue());
        
        locationTextField.setText("new location");
        assertEquals("new location", vevent.getLocation().getValue());

        TestUtil.runThenWaitForPaintPulse(() -> appointmentGroupGridPane.setAppointmentGroupSelected(11));
        assertEquals("group11", vevent.getCategories().get(0).getValue().get(0));
        
        groupTextField.setText("new group name");
        assertEquals("new group name", vevent.getCategories().get(0).getValue().get(0));
//        assertEquals("new group name", agenda.appointmentGroups().get(11).getDescription());
//        TestUtil.runThenWaitForPaintPulse( () ->  EditChoiceDialog.EDIT_DIALOG_CALLBACK.call(EXAMPLE_MAP));
        
        click("#saveComponentButton");
        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");
        
//        // Check appointment edited after close
//        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), vevent.getDateTimeStart());
//        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), vevent.getDateTimeEnd());
        
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
    private static final Map<ChangeDialogOption, Pair<Temporal,Temporal>> EXAMPLE_MAP = makeExampleMap();
    private static Map<ChangeDialogOption, Pair<Temporal,Temporal>> makeExampleMap()
    {
        Map<ChangeDialogOption, Pair<Temporal,Temporal>> exampleMap = new LinkedHashMap<>();
        exampleMap.put(ChangeDialogOption.ALL, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), null));
        exampleMap.put(ChangeDialogOption.ONE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 5, 25), LocalDate.of(2016, 5, 25)));
        exampleMap.put(ChangeDialogOption.THIS_AND_FUTURE, new Pair<Temporal, Temporal>(LocalDate.of(2016, 6, 25), null));
        return exampleMap;
    }
}
