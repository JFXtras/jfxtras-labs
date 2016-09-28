package jfxtras.labs.icalendaragenda.popup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.popup.EditRecurrenceRuleVBox;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultRecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.RecurrenceFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.test.TestUtil;

/**
 * Tests the edit controls ability to handle EXDATE recurrence exceptions
 * 
 * @author David Bal
 *
 */
public class RecurrenceExceptionsTests extends VEventPopupTestBase
{
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
        });
        
        // Get properties
        click("#recurrenceRuleTab");
        ComboBox<Temporal> exceptionComboBox = find("#exceptionComboBox");

        // Check initial state
        List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
        LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
        List<LocalDateTime> expectedDates = Stream
                .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
                .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
                    .limit(EditRecurrenceRuleVBox.INITIAL_COUNT)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        TestUtil.runThenWaitForPaintPulse( () -> endAfterEventsSpinner.getValueFactory().decrement(5) );
        {
            List<Temporal> exceptions = exceptionComboBox.getItems().stream().collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 10, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(EditRecurrenceRuleVBox.INITIAL_COUNT-5)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, exceptions);
        }
        
        // Ends On (UNTIL)
        TestUtil.runThenWaitForPaintPulse( () -> untilRadioButton.setSelected(true));
        {
            LocalDateTime expectedUntil = LocalDateTime.of(2015, 11, 9, 10, 0).plus(EditRecurrenceRuleVBox.DEFAULT_UNTIL_PERIOD);
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
                    .limit(EditRecurrenceRuleVBox.EXCEPTION_CHOICE_LIMIT)
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
            getEditComponentPopup().setupData(
                    vevent,
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    AgendaTestAbstract.CATEGORIES);
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
}
