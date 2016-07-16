package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.Pair;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.CategorySelectionGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventTabPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.RecurrenceRuleVBox;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgendaUtilities;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultRecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.RecurrenceFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
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
public class VEventEditPopupTests extends JFXtrasGuiTest
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
        AssertNode.generateSource("n", n, null, false, jfxtras.test.AssertNode.A.XYWH);
        new AssertNode(n).assertXYWH(0.0, 0.0, 400.0, 570.0, 0.01);
    }
    
    @Test
    public void canDisplayPopupWithVEvent()
    {
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS); // default VComponent store - for Appointments, if other implementation used make new store
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
//                    appointment,
                    vevent,
                    Arrays.asList(vevent),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

        TextField summary = find("#summaryTextField");
        assertEquals(vevent.getSummary().getValue(), summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 0), start.getLocalDateTime());
    }
    
    @Test
    public void canSimpleEditVEvent()
    {
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1()
                .withLocation("Here");
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    Arrays.asList(vevent),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
//        TestUtil.sleep(3000);
        TextField summary = find("#summaryTextField");
        assertEquals(vevent.getSummary().getValue(), summary.getText());

        LocalDateTimeTextField start = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 0), start.getLocalDateTime());
    }
    
    // edit one repeating event to make an individual event
    @Test
    public void canEditDescriptibeProperties()
    {
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    vEvents,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

        // Get properties
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        LocalDateTimeTextField endDateTimeTextField = find("#endDateTimeTextField");
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        TextField summaryTextField = find("#summaryTextField");
        TextArea descriptionTextArea = find("#descriptionTextArea");
        TextField locationTextField = find("#locationTextField");
        TextField categoryTextField = find("#categoryTextField");
        CategorySelectionGridPane categorySelectionGridPane = find("#categorySelectionGridPane");
        
        // Check initial state
        assertEquals(LocalDateTime.of(2016, 5, 15, 10, 00), startDateTimeTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2016, 5, 15, 11, 00), endDateTimeTextField.getLocalDateTime());
        assertTrue(startDateTimeTextField.isVisible());
        assertTrue(endDateTimeTextField.isVisible());
        assertEquals("Daily1 Summary", summaryTextField.getText());
        assertEquals("Daily1 Description", descriptionTextArea.getText());
//        TestUtil.sleep(3000);
        assertEquals("group05", categoryTextField.getText());
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

        TestUtil.runThenWaitForPaintPulse(() -> categorySelectionGridPane.setCategorySelected(11));
        assertEquals("group11", vevent.getCategories().get(0).getValue().get(0));
        
        categoryTextField.setText("new group name");
        assertEquals("new group name", vevent.getCategories().get(0).getValue().get(0));
        
        // Save changes
        click("#saveComponentButton");
        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");
        
        VEvent individualVEvent = vEvents.get(1);

        assertEquals(2, vEvents.size());
        
        // Check component after dialog close
        assertEquals(LocalDateTime.of(2016, 5, 15, 8, 0), individualVEvent.getDateTimeStart().getValue());
        assertEquals(LocalDateTime.of(2016, 5, 15, 9, 0), individualVEvent.getDateTimeEnd().getValue());
        
        assertEquals("new summary", individualVEvent.getSummary().getValue());
        assertEquals("new description", individualVEvent.getDescription().getValue());
        assertEquals("new location", individualVEvent.getLocation().getValue());
        assertEquals("new group name", individualVEvent.getCategories().get(0).getValue().get(0));

        assertEquals(ICalendarStaticComponents.getDaily1(), vEvents.get(0));
        VEvent editedVEvent = new VEvent()
                .withCategories("new group name")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 15, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 15, 9, 0))
                .withDescription("new description")
                .withSummary("new summary")
                .withDateTimeStamp(vEvents.get(1).getDateTimeStamp())
                .withUniqueIdentifier("20150110T080000-004@jfxtras.org")
                .withLocation("new location")
                .withRecurrenceId(LocalDateTime.of(2016, 5, 15, 10, 0))
                .withSequence(1)
                ;
        assertEquals(editedVEvent, individualVEvent);
    }
    
    @Test
    public void canChangeFrequency()
    {
        RecurrenceFactory<Appointment> recurrenceFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        recurrenceFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));        
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        ObservableList<VEvent> vEvents = FXCollections.observableArrayList(vevent);
        List<Appointment> newAppointments = recurrenceFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    vEvents,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
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

        // Check initial state
        assertEquals(FrequencyType.YEARLY, f.getValue());
        assertEquals(0, rrule.byRules().size());
        }
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));   
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1()
                .withLocation("Here");
        myCalendar.getVEvents().add(vevent);
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        click(wholeDayCheckBox);

        LocalDateTextField start = find("#startDateTextField");
        assertEquals(LocalDate.of(2016, 5, 15), start.getLocalDate());
        start.setLocalDate(LocalDate.of(2016, 5, 16)); // adds 1 day shift
        
        // Save changes
        click("#saveComponentButton");
        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");
        
        VEvent editedVEvent = myCalendar.getVEvents().get(0);
        assertEquals(LocalDate.of(2015, 11, 10), editedVEvent.getDateTimeStart().getValue());
        assertEquals(LocalDate.of(2015, 11, 11), editedVEvent.getDateTimeEnd().getValue());
    }
    
    @Test
    public void canChangeWholeDayToTimeBased()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getWholeDayDaily3();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        CheckBox wholeDayCheckBox = find("#wholeDayCheckBox");
        assertTrue(wholeDayCheckBox.isSelected());
        LocalDateTextField start1 = find("#startDateTextField");
        assertEquals(LocalDate.of(2015, 11, 11), start1.getLocalDate());
        click(wholeDayCheckBox); // turn off wholeDayCheckBox

        LocalDateTimeTextField start2 = find("#startDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 11, 10, 0), start2.getLocalDateTime());
        start2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 13, 0)); // adds 3 hour shift
        LocalDateTimeTextField end2 = find("#endDateTimeTextField");
        assertEquals(LocalDateTime.of(2015, 11, 12, 14, 0), end2.getLocalDateTime());
        end2.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 14, 0)); // make 1 hour long
        
        // Save changes
        click("#saveComponentButton");
        
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");
        
        VEvent veventExpected = new VEvent()
                .withCategories(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(6).getDescription())
                .withDateTimeStamp(ZonedDateTime.of(LocalDateTime.of(2015, 1, 10, 8, 0), ZoneOffset.UTC))
                .withUniqueIdentifier("20150110T080000-010@jfxtras.org")
                .withRecurrenceRule(new RecurrenceRule2()
                        .withUntil(LocalDate.of(2015, 11, 23))
                        .withFrequency(FrequencyType.DAILY)
                        .withInterval(3))
                .withDateTimeStart(new DateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 8, 13, 0), ZoneId.systemDefault())))
                .withDateTimeEnd(new DateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 8, 14, 0), ZoneId.systemDefault())))
                .withSequence(1);
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 13, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        veventExpected.getRecurrenceRule().getValue().setUntil(until);

        VEvent editedVEvent = myCalendar.getVEvents().get(0);
        assertEquals(veventExpected, editedVEvent);
    }
    
    @Test
    public void canMakeExceptionList()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        
        // Get properties
        click("#recurrenceRuleTab");
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");

        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
        LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
        List<LocalDateTime> expectedDates = Stream
                .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(RecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                .collect(Collectors.toList());
        assertEquals(expectedDates, exceptions);
    }
    
    @Test
    public void canMakeExceptionListWholeDay() // Whole day appointments
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        
        // Make whole day
        click("#wholeDayCheckBox");
        
        // check whole day fields
        LocalDateTextField startDateTextField = find("#startDateTextField");
        LocalDateTextField endDateTextField = find("#endDateTextField");
        assertEquals(LocalDate.of(2015, 11, 10), startDateTextField.getLocalDate());
        assertEquals(LocalDate.of(2015, 11, 11), endDateTextField.getLocalDate());

        // go back to time based
        click("#wholeDayCheckBox");
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        LocalDateTimeTextField endDateTimeTextField = find("#endDateTimeTextField");

        assertEquals(LocalDateTime.of(2015, 11, 10, 10, 0), startDateTimeTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 10, 11, 0), endDateTimeTextField.getLocalDateTime());

        // Make whole day again
        click("#wholeDayCheckBox");
        
        // Go to repeatable tab
        click("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");

        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
        LocalDate seed = LocalDate.of(2015, 11, 9);
        List<LocalDate> expectedDates = Stream
                .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(RecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                .collect(Collectors.toList());
        assertEquals(expectedDates, exceptions);
    }
    
    @Test
    public void canMakeExceptionListWeekly()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        
        // Go to repeatable tab
        click("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Change property and verify state change
        // Frequency - Weekly
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                  , LocalDateTime.of(2015, 11, 24, 10, 0)
                  , LocalDateTime.of(2015, 12, 1, 10, 0)
                  , LocalDateTime.of(2015, 12, 8, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        // Days of the week properties
        CheckBox su = (CheckBox) find("#sundayCheckBox");
        CheckBox mo = (CheckBox) find("#mondayCheckBox");
        CheckBox tu = (CheckBox) find("#tuesdayCheckBox");
        CheckBox we = (CheckBox) find("#wednesdayCheckBox");
        CheckBox th = (CheckBox) find("#thursdayCheckBox");
        CheckBox fr = (CheckBox) find("#fridayCheckBox");
        CheckBox sa = (CheckBox) find("#saturdayCheckBox");
        
        // Get weekly properties
        RecurrenceRule2 rrule = vevent.getRecurrenceRule().getValue();
        Frequency f = rrule.getFrequency();
        assertEquals(FrequencyType.WEEKLY, f.getValue());
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);

        // Check initial state
        List<DayOfWeek> expectedDOW = Arrays.asList(DayOfWeek.TUESDAY);
        assertEquals(expectedDOW, rule.dayOfWeekWithoutOrdinalList());
        HBox weeklyHBox = find("#weeklyHBox");
        assertTrue(weeklyHBox.isVisible());       
        assertFalse(su.isSelected());
        assertFalse(mo.isSelected());
        assertTrue(tu.isSelected());
        assertFalse(we.isSelected());
        assertFalse(th.isSelected());
        assertFalse(fr.isSelected());
        assertFalse(sa.isSelected());
        
        // Toggle each day of week and check
        TestUtil.runThenWaitForPaintPulse( () -> su.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                   LocalDateTime.of(2015, 11, 10, 10, 0)
                 , LocalDateTime.of(2015, 11, 15, 10, 0)
                 , LocalDateTime.of(2015, 11, 17, 10, 0)
                 , LocalDateTime.of(2015, 11, 22, 10, 0)
                 , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        TestUtil.runThenWaitForPaintPulse( () -> mo.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> we.setSelected(false)); // turn Wednesday off (initially on)
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                  , LocalDateTime.of(2015, 11, 17, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> th.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 16, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> fr.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> sa.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems()
                    .stream()
                    .limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        List<DayOfWeek> expectedDaysOfWeek = Arrays.asList(
                DayOfWeek.MONDAY
              , DayOfWeek.TUESDAY
              , DayOfWeek.THURSDAY
              , DayOfWeek.FRIDAY
              , DayOfWeek.SATURDAY
              , DayOfWeek.SUNDAY
              );
        List<DayOfWeek> daysOfWeek = rule.dayOfWeekWithoutOrdinalList();
        Collections.sort(daysOfWeek);
        assertEquals(expectedDaysOfWeek, daysOfWeek);        
    }

    @Test
    public void canMakeExceptionListMonthly()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        click("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        
        // Change property and verify state change
        // Frequency - Monthly
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.MONTHLY));
        RadioButton dayOfMonthRadioButton = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeekRadioButton = find("#dayOfWeekRadioButton");
        
        // Check initial state
        assertTrue(dayOfMonthRadioButton.isSelected());
        assertFalse(dayOfWeekRadioButton.isSelected());
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 12, 9, 10, 0)
                  , LocalDateTime.of(2016, 1, 9, 10, 0)
                  , LocalDateTime.of(2016, 2, 9, 10, 0)
                  , LocalDateTime.of(2016, 3, 9, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        // check dayOfWeekRadioButton and check state
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeekRadioButton.setSelected(true));
        assertFalse(dayOfMonthRadioButton.isSelected());
        assertTrue(dayOfWeekRadioButton.isSelected());
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 12, 8, 10, 0)
                  , LocalDateTime.of(2016, 1, 12, 10, 0)
                  , LocalDateTime.of(2016, 2, 9, 10, 0)
                  , LocalDateTime.of(2016, 3, 8, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
    }
    
    @Test
    public void canMakeExceptionListYearly()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        click("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        
        // Change property and verify state change
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.YEARLY));
        
        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, exceptions);
    }
    
    @Test
    public void canMakeExceptionListEndsCriteria()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        click("#recurrenceRuleTab");

        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        RadioButton endNeverRadioButton = find("#endNeverRadioButton");
        RadioButton endAfterRadioButton = find("#endAfterRadioButton");
        Spinner<Integer> endAfterEventsSpinner = find("#endAfterEventsSpinner");
        RadioButton untilRadioButton = find("#untilRadioButton");
        DatePicker untilDatePicker = find("#untilDatePicker");

        // Change property and verify state change
        // Ends After (COUNT)
        TestUtil.runThenWaitForPaintPulse( () -> endAfterRadioButton.setSelected(true) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(RecurrenceRuleVBox.INITIAL_COUNT)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        TestUtil.runThenWaitForPaintPulse( () -> endAfterEventsSpinner.getValueFactory().decrement(5) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(RecurrenceRuleVBox.INITIAL_COUNT-5)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        
        // Ends On (UNTIL)
        TestUtil.runThenWaitForPaintPulse( () -> untilRadioButton.setSelected(true));
        {
            LocalDateTime expectedUntil = LocalDateTime.of(2015, 11, 9, 10, 0).plus(RecurrenceRuleVBox.DEFAULT_UNTIL_PERIOD);
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            Iterator<LocalDateTime> i = Stream.iterate(seed, a -> a.plus(1, ChronoUnit.DAYS)).iterator();
            List<LocalDateTime> expectedDates = new ArrayList<>();
            while (i.hasNext())
            {
                LocalDateTime d = i.next();
                if (d.isAfter(expectedUntil)) break;
                expectedDates.add(d);
            }            
            assertEquals(expectedDates, exceptions);
        }
        TestUtil.runThenWaitForPaintPulse( () -> untilDatePicker.setValue(LocalDate.of(2015, 11, 13)) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                    ));      
            assertEquals(expectedDates, exceptions);
        }
        
        // Ends Never
        TestUtil.runThenWaitForPaintPulse( () -> endNeverRadioButton.setSelected(true));
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(RecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
    }
    
    @Test
    public void canAddException2()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(1);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        click("#recurrenceRuleTab");
        
        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ListView<Temporal> exceptionsListView = find("#exceptionsListView");
        
        // Add exceptions and check
        Temporal e1 = exceptionComboBox.getItems().get(2);
        TestUtil.runThenWaitForPaintPulse( () -> exceptionComboBox.getSelectionModel().select(e1) );
        click("#addExceptionButton");
        {
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            assertEquals(1, vExceptions.size());
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 11, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }

        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        // Add another exceptions and check
        Temporal e2 = exceptionComboBox.getItems().get(0);
        TestUtil.runThenWaitForPaintPulse( () -> exceptionComboBox.getSelectionModel().select(e2) );
        click("#addExceptionButton");
        {
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            assertEquals(2, vExceptions.size());
            List<LocalDateTime> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems()); 
        }

        { // verify added exceptions are not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
    }
    
    @Test
    public void canRemoveException2()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDailyWithException1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(0);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });
        click("#recurrenceRuleTab");

        // Get properties
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");
        ListView<Temporal> exceptionsListView = find("#exceptionsListView");
        
        { // verify initial state
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }

        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream()
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }

        // remove Exceptions one at a time, confirm gone and returned to exceptionComboBox
        TestUtil.runThenWaitForPaintPulse( () -> exceptionsListView.getSelectionModel().select(LocalDateTime.of(2015, 11, 12, 10, 0)));
        click ("#removeExceptionButton");
        { // verify new state
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 15, 10, 0)
                    ));
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }
        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 12, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
        
        TestUtil.runThenWaitForPaintPulse( () -> exceptionsListView.getSelectionModel().select(LocalDateTime.of(2015, 11, 15, 10, 0)));
        click ("#removeExceptionButton");
        { // verify new state
            Set<Temporal> vExceptions = vevent.getExceptionDates().get(0).getValue();
            List<Temporal> exceptions = vExceptions
                    .stream()
                    .map(a -> (LocalDateTime) a).sorted()
                    .collect(Collectors.toList());
            List<Temporal> expectedExceptions = new ArrayList<>(); // empty list
            assertEquals(expectedExceptions, exceptions);
            assertEquals(expectedExceptions, exceptionsListView.getItems());            
        }
        { // verify added exception is not in exceptionComboBox list
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().limit(5)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                      LocalDateTime.of(2015, 11, 9, 10, 0)
                    , LocalDateTime.of(2015, 11, 12, 10, 0)
                    , LocalDateTime.of(2015, 11, 15, 10, 0)
                    , LocalDateTime.of(2015, 11, 18, 10, 0)
                    , LocalDateTime.of(2015, 11, 21, 10, 0)
                    ));
            assertEquals(expectedDates, exceptions);
        }
    }
    
    @Test
    public void canEditThisAndFuture()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

       // edit property
       TextField summaryTextField = find("#summaryTextField");
       summaryTextField.setText("new summary");

       // save changes to THIS AND FUTURE
       click("#saveComponentButton");
       ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
       TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.THIS_AND_FUTURE));
       click("#changeDialogOkButton");

       // verify VComponent changes
       assertEquals(2, myCalendar.getVEvents().size());

       VEvent v0 = myCalendar.getVEvents().get(0);
       VEvent expectedV0 = ICalendarStaticComponents.getDaily1();
       ZonedDateTime until = ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
       expectedV0.getRecurrenceRule().getValue()
               .setUntil(until);
       assertEquals(expectedV0, v0);

       VEvent v1 = myCalendar.getVEvents().get(1);
       VEvent expectedV1 = new VEvent()
               .withCategories(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(5).getDescription())
               .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 10, 0))
               .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 11, 0))
               .withDescription("Daily1 Description")
               .withSummary("new summary")
               .withDateTimeStamp(v1.getDateTimeStamp()) // time stamp is time-based so copy it to guarantee equality.
               .withUniqueIdentifier(v1.getUniqueIdentifier()) // uid is time-based so copy it to guarantee equality.
               .withRecurrenceRule(new RecurrenceRule2()
                       .withFrequency(FrequencyType.DAILY))
               .withRelatedTo("20150110T080000-004@jfxtras.org")
               .withSequence(1);
      assertEquals(expectedV1, v1);

       // verify Appointment changes      
       {
           List<String> summaries = vComponentFactory.makeRecurrences(v0)
                   .stream()
                   .map(a -> a.getSummary())
                   .collect(Collectors.toList());
           List<String> summaries2 = vComponentFactory.makeRecurrences(v1)
                   .stream()
                   .map(a -> a.getSummary())
                   .collect(Collectors.toList());
           List<String> summariesAll = new ArrayList<>(summaries);
           summariesAll.addAll(summaries2);
           List<String> expectedSummaries = new ArrayList<>(Arrays.asList(
                   "Daily1 Summary"
                 , "Daily1 Summary"
                 , "new summary"
                 , "new summary"
                 , "new summary"
                 , "new summary"
                   ));
           assertEquals(expectedSummaries, summariesAll);
       }
    }

    @Test
    public void canCancelEdit()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

        // edit properties
        TextField summaryTextField = find("#summaryTextField");
        summaryTextField.setText("new summary");
        LocalDateTimeTextField startDateTimeTextField = find("#startDateTimeTextField");
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 10, 30));

        // cancel changes
        click("#saveComponentButton");
        click("#changeDialogCancelButton");
        click("#cancelComponentButton");
        
        // check return to original state
        VEvent expectedV0 = ICalendarStaticComponents.getDaily1();
        VEvent v0 = myCalendar.getVEvents().get(0);
        assertEquals(expectedV0, v0);
    }
    
    @Test
    public void canDeleteSeriesEdit()
    {
        VCalendar myCalendar = new VCalendar();
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        myCalendar.getVEvents().add(vevent);
        RecurrenceFactory<Appointment> vComponentFactory = new DefaultRecurrenceFactory(AgendaTestAbstract.DEFAULT_APPOINTMENT_GROUPS);
        vComponentFactory.setStartRange(LocalDateTime.of(2015, 11, 8, 0, 0));
        vComponentFactory.setEndRange(LocalDateTime.of(2015, 11, 15, 0, 0));
        List<Appointment> newAppointments = vComponentFactory.makeRecurrences(vevent);
        Appointment appointment = newAppointments.get(2);
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            editComponentPopup.setupData(
                    vevent,
                    myCalendar.getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    ICalendarAgendaUtilities.CATEGORIES);
        });

        // delete VComponent
        click("#deleteComponentButton");
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
        click("#changeDialogOkButton");

        // check return to original state
        assertEquals(0, myCalendar.getVEvents().size());
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
