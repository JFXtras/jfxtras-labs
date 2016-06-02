package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
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
public class VEventPopupTests extends JFXtrasGuiTest
{
    private EditVEventTabPane editComponentPopup;
    
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
                AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
//                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        });

        TextField summary = find("#summaryTextField");
        assertEquals(vevent.getSummary().getValue(), summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 0), start.getLocalDateTime());
    }
    
    @Test
    public void canEditVEventWithPopup1()
    {
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
//                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
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
                AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
//                    appointment,
                    vevent,
                    vEvents,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
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
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2016, 5, 15, 8, 0));

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
        
        click("#saveComponentButton");
        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");

        assertEquals(2, vEvents.size());
        
        // Check component after dialog close
        assertEquals(LocalDateTime.of(2016, 5, 15, 8, 0), vevent.getDateTimeStart().getValue());
        assertEquals(LocalDateTime.of(2016, 5, 15, 9, 0), vevent.getDateTimeEnd().getValue());
        
        assertEquals("new summary", vevent.getSummary().getValue());
        assertEquals("new description", vevent.getDescription().getValue());
        assertEquals("new location", vevent.getLocation().getValue());
        assertEquals("new group name", vevent.getCategories().get(0).getValue().get(0));

        assertEquals(ICalendarStaticComponents.getDaily1(), vEvents.get(0));
        VEvent editedVEvent = new VEvent()
                .withRecurrenceId(LocalDateTime.of(2016, 5, 15, 10, 0))
                .withDescription("new description")
                .withSummary("new summary")
                .withLocation("new location")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 15, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 15, 9, 0))
                .withUniqueIdentifier("20150110T080000-0@jfxtras.org")
                .withSequence(1)
                .withDateTimeStamp(vEvents.get(1).getDateTimeStamp())
                .withCategories("new group name");
        assertEquals(editedVEvent, vEvents.get(1));
    }
    
    @Test
    public void canChangeFrequency()
    {
        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                AgendaTestAbstract.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE);
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
//                    appointment,
                    vevent,
                    vEvents,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS);
        });

        // Get properties
        click("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Check initial state
        RecurrenceRule2 rrule = vevent.getRecurrenceRule().getValue();
        Frequency f = rrule.getFrequency();
        assertEquals(FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        assertEquals(FrequencyType.DAILY, rrule.getFrequency().getValue());
        
        // Change property and verify state change
        // WEEKLY
        {
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
        // Days of the week properties
        CheckBox su = (CheckBox) find("#sundayCheckBox");
        CheckBox mo = (CheckBox) find("#mondayCheckBox");
        CheckBox tu = (CheckBox) find("#tuesdayCheckBox");
        CheckBox we = (CheckBox) find("#wednesdayCheckBox");
        CheckBox th = (CheckBox) find("#thursdayCheckBox");
        CheckBox fr = (CheckBox) find("#fridayCheckBox");
        CheckBox sa = (CheckBox) find("#saturdayCheckBox");
        
        // Get weekly properties
        assertEquals(FrequencyType.WEEKLY, f.getValue());
        assertEquals(1, rrule.byRules().size());
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);

        // Check initial state
        List<DayOfWeek> expectedDOW = Arrays.asList(DayOfWeek.SUNDAY);
        assertEquals(expectedDOW, rule.dayOfWeekWithoutOrdinalList());
        HBox weeklyHBox = find("#weeklyHBox");
        assertTrue(weeklyHBox.isVisible());       
        assertTrue(su.isSelected());
        assertFalse(mo.isSelected());
        assertFalse(tu.isSelected());
        assertFalse(we.isSelected());
        assertFalse(th.isSelected());
        assertFalse(fr.isSelected());
        assertFalse(sa.isSelected());
        
        // Toggle each day of week and check
        // Sunday already selected
        TestUtil.runThenWaitForPaintPulse( () -> mo.setSelected(true));
        assertTrue(mo.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.MONDAY));
        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        assertTrue(tu.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.TUESDAY));
        TestUtil.runThenWaitForPaintPulse( () -> we.setSelected(true));
        assertTrue(we.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.WEDNESDAY));
        TestUtil.runThenWaitForPaintPulse( () -> th.setSelected(true));
        assertTrue(th.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.THURSDAY));
        TestUtil.runThenWaitForPaintPulse( () -> fr.setSelected(true));
        assertTrue(fr.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.FRIDAY));
        TestUtil.runThenWaitForPaintPulse( () -> sa.setSelected(true));
        assertTrue(sa.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.SATURDAY));
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.values());
        List<DayOfWeek> daysOfWeek = rule.dayOfWeekWithoutOrdinalList();
        Collections.sort(daysOfWeek);
        assertEquals(allDaysOfWeek, daysOfWeek);
        }

        // MONTHLY
        {
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.MONTHLY));
        
        // Monthly properties
        RadioButton dayOfMonth = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeek = find("#dayOfWeekRadioButton");
//        Frequency2 f = rrule.getFrequency(); // refresh reference
        
        // Check initial state
        assertTrue(dayOfMonth.isSelected());
        assertFalse(dayOfWeek.isSelected());
        assertEquals(FrequencyType.MONTHLY, f.getValue());
        assertEquals(0, rrule.byRules().size());
        
        // Toggle monthly options and check
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeek.setSelected(true));
        assertFalse(dayOfMonth.isSelected());
        assertTrue(dayOfWeek.isSelected());
        assertEquals(1, rrule.byRules().size());
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);
        assertEquals("BYDAY=3SU", rule.toContent()); // 3nd Sunday of the month
        }
        
        // YEARLY
        {
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.YEARLY));
//        Frequency f = v.getRRule().getFrequency(); // refresh reference

        // Check initial state
        assertEquals(FrequencyType.YEARLY, f.getValue());
        assertEquals(1, rrule.byRules().size());
        }
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
