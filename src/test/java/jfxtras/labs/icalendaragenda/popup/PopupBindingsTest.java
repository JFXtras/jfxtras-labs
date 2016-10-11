package jfxtras.labs.icalendaragenda.popup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.agenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.CategorySelectionGridPane;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.test.TestUtil;

public class PopupBindingsTest extends VEventPopupTestBase
{
    @Test
    public void canEditDescribableProperties()
    {
        VEvent vevent = ICalendarStaticComponents.getDaily1();
        
        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    categories());
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

        // Make changes
        startDateTimeTextField.setLocalDateTime(LocalDateTime.of(2016, 5, 15, 8, 0));
        summaryTextField.setText("new summary");
        descriptionTextArea.setText("new description");
        locationTextField.setText("new location");
        TestUtil.runThenWaitForPaintPulse(() -> categorySelectionGridPane.setCategorySelected(11));
        assertEquals("group11", categoryTextField.getText());
        categoryTextField.setText("new group name");

        // save to all
        click("#saveComponentButton");
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ALL);
        });
        click("#changeDialogOkButton");
        
        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        
        VEvent expectedRevisedVEvent = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withSummary("new summary")
                .withDescription("new description")
                .withLocation("new location")
                .withCategories(FXCollections.observableArrayList(new Categories("new group name")))
                .withSequence(1);
        assertEquals(expectedRevisedVEvent, revisedVEvent);
    }
    
    @Test
    public void canChangeFrequencyWeekly()
    {       
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        click("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Check initial state
        assertEquals(FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        
        // Change property and verify state change
        // WEEKLY
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.WEEKLY));
        // Days of the week properties
        CheckBox su = (CheckBox) find("#sundayCheckBox");
        CheckBox mo = (CheckBox) find("#mondayCheckBox");
        CheckBox tu = (CheckBox) find("#tuesdayCheckBox");
        CheckBox we = (CheckBox) find("#wednesdayCheckBox");
        CheckBox th = (CheckBox) find("#thursdayCheckBox");
        CheckBox fr = (CheckBox) find("#fridayCheckBox");
        CheckBox sa = (CheckBox) find("#saturdayCheckBox");

        // Check initial state
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
        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        assertTrue(tu.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> we.setSelected(true));
        assertTrue(we.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> th.setSelected(true));
        assertTrue(th.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> fr.setSelected(true));
        assertTrue(fr.isSelected());
        TestUtil.runThenWaitForPaintPulse( () -> sa.setSelected(true));
        assertTrue(sa.isSelected());

        click("#saveRepeatButton");

        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        RecurrenceRule2 rrule = revisedVEvent.getRecurrenceRule().getValue();
        
        // check frequency
        Frequency f = rrule.getFrequency();
        assertEquals(FrequencyType.WEEKLY, f.getValue());
        
        // check ByDay
        assertEquals(1, rrule.byRules().size());
        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);

        
        List<DayOfWeek> allDaysOfWeek = Arrays.asList(DayOfWeek.values());
        List<DayOfWeek> daysOfWeek = rule.dayOfWeekWithoutOrdinalList();
        Collections.sort(daysOfWeek);
        assertEquals(allDaysOfWeek, daysOfWeek);
    }
    
    @Test
    public void canChangeFrequencyMonthly()
    {       
        VEvent vevent = ICalendarStaticComponents.getDaily1();

        TestUtil.runThenWaitForPaintPulse( () ->
        {
            getEditComponentPopup().setupData(
                    vevent,
                    LocalDateTime.of(2016, 5, 15, 10, 0),
                    LocalDateTime.of(2016, 5, 15, 11, 0),
                    AgendaTestAbstract.CATEGORIES);
        });

        // Get properties
        click("#recurrenceRuleTab");
        ComboBox<FrequencyType> frequencyComboBox = find("#frequencyComboBox");
            
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.MONTHLY));
        
        // Monthly properties
        RadioButton dayOfMonth = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeek = find("#dayOfWeekRadioButton");
        
        // Check initial state
        assertTrue(dayOfMonth.isSelected());
        assertFalse(dayOfWeek.isSelected());
//        assertEquals(FrequencyType.MONTHLY, f.getValue());
//        assertEquals(0, rrule.byRules().size());
        
        // Toggle monthly options and check
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeek.setSelected(true));
        assertFalse(dayOfMonth.isSelected());
        assertTrue(dayOfWeek.isSelected());
//        assertEquals(1, rrule.byRules().size());
//        ByDay rule = (ByDay) rrule.lookupByRule(ByDay.class);
//        assertEquals("BYDAY=3SU", rule.toContent()); // 3nd Sunday of the month
        
        click("#saveRepeatButton");
        TestUtil.sleep(3000);
        // test iTIP message
        List<VCalendar> messages = getEditComponentPopup().iTIPMessagesProperty().get();
        assertEquals(1, messages.size());
        VCalendar message = messages.get(0);
        assertEquals(1, message.getVEvents().size());
        VEvent revisedVEvent = message.getVEvents().get(0);
        RecurrenceRule2 rrule = revisedVEvent.getRecurrenceRule().getValue();
        System.out.println(message);
//        // YEARLY
//        {
//        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(FrequencyType.YEARLY));
//
//        // Check initial state
////        assertEquals(FrequencyType.YEARLY, f.getValue());
////        assertEquals(0, rrule.byRules().size());
//        }
//        
//        click("#cancelRepeatButton");
////        closeCurrentWindow();
    }
}
