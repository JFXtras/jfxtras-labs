package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule.ByRules;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
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
public class AgendaEditPopupTest extends ICalendarTestAbstract
{
    private String dateTimeStamp;

    public Parent getRootNode()
    {
        Parent p = super.getRootNode();
        return p;
    }
    
    // edit non-repeatable elements
    @Test
//    @Ignore
    public void canEditVComponent1()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getIndividual1()));
        
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);
        
        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);

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
        assertEquals(LocalDateTime.of(2015, 11, 11, 10, 30), startTextField.getLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 11, 11, 30), endTextField.getLocalDateTime());
        assertEquals("Individual Summary", summaryTextField.getText());
        assertEquals("Individual Description", descriptionTextArea.getText());
        assertEquals("group05", groupTextField.getText());
        assertFalse(wholeDayCheckBox.isSelected());

        // Edit and check properties
        startTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 8, 0));
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), v.getDateTimeStart());
        
        endTextField.setLocalDateTime(LocalDateTime.of(2015, 11, 11, 9, 0));
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), v.getDateTimeEnd());
        
        wholeDayCheckBox.setSelected(true);
        assertEquals(LocalDate.of(2015, 11, 11), v.getDateTimeStart());
        assertEquals(LocalDate.of(2015, 11, 12), v.getDateTimeEnd());
        wholeDayCheckBox.setSelected(false);
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), v.getDateTimeStart());
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), v.getDateTimeEnd());
        
        summaryTextField.setText("new summary");
        assertEquals("new summary", v.getSummary());

        descriptionTextArea.setText("new description");
        assertEquals("new description", v.getDescription());
        
        locationTextField.setText("new location");
        assertEquals("new location", v.getLocation());
        
        appointmentGroupGridPane.setAppointmentGroupSelected(11);
        assertEquals("group11", v.getCategories());
        
        groupTextField.setText("new group name");
        assertEquals("new group name", v.getCategories());
        assertEquals("new group name", agenda.appointmentGroups().get(11).getDescription());
        
        click("#closeAppointmentButton");
        
        // Check appointment edited after close
        assertEquals(1, agenda.appointments().size());
        Appointment a = agenda.appointments().get(0);
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), a.getStartLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), a.getEndLocalDateTime());
        assertEquals("new summary", a.getSummary());
        assertEquals("new description", a.getDescription());
        assertEquals("new group name", a.getAppointmentGroup().getDescription());
    }
    
    @Test
//    @Ignore
    public void canToggleRepeatableCheckBox()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties        
        CheckBox repeatableCheckBox = find("#repeatableCheckBox");

        // Check initial state
        assertTrue(repeatableCheckBox.isSelected());
        assertTrue(v.getRRule() != null);
        
        // Change property and verify state change
        TestUtil.runThenWaitForPaintPulse( () -> repeatableCheckBox.setSelected(false));
        assertTrue(v.getRRule() == null);
        
        closeCurrentWindow();
    }
    
    @Test
    public void canChangeFrequency()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties        
        ComboBox<Frequency.FrequencyType> frequencyComboBox = find("#frequencyComboBox");

        // Check initial state
        assertEquals(Frequency.FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        assertEquals(Frequency.FrequencyType.DAILY, v.getRRule().getFrequency().frequencyType());
        
        // Change property and verify state change
        // WEEKLY
        {
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(Frequency.FrequencyType.WEEKLY));

        // Days of the week properties
        CheckBox su = (CheckBox) find("#sundayCheckBox");
        CheckBox mo = (CheckBox) find("#mondayCheckBox");
        CheckBox tu = (CheckBox) find("#tuesdayCheckBox");
        CheckBox we = (CheckBox) find("#wednesdayCheckBox");
        CheckBox th = (CheckBox) find("#thursdayCheckBox");
        CheckBox fr = (CheckBox) find("#fridayCheckBox");
        CheckBox sa = (CheckBox) find("#saturdayCheckBox");
        
        // Get weekly properties
        Frequency f = v.getRRule().getFrequency();
        assertEquals(Frequency.FrequencyType.WEEKLY, f.frequencyType());
        assertEquals(1, f.getByRules().size());
        ByDay rule = (ByDay) f.getByRuleByType(ByRules.BYDAY);

        // Check initial state
        List<DayOfWeek> expectedDOW = Arrays.asList(DayOfWeek.WEDNESDAY);
        assertEquals(expectedDOW, rule.dayOfWeekWithoutOrdinalList());
        HBox weeklyHBox = find("#weeklyHBox");
        assertTrue(weeklyHBox.isVisible());       
        assertFalse(su.isSelected());
        assertFalse(mo.isSelected());
        assertFalse(tu.isSelected());
        assertTrue(we.isSelected());
        assertFalse(th.isSelected());
        assertFalse(fr.isSelected());
        assertFalse(sa.isSelected());
        
        // Toggle each day of week and check
        TestUtil.runThenWaitForPaintPulse( () -> su.setSelected(true));
        assertTrue(su.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.SUNDAY));
        TestUtil.runThenWaitForPaintPulse( () -> mo.setSelected(true));
        assertTrue(mo.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.MONDAY));
        TestUtil.runThenWaitForPaintPulse( () -> tu.setSelected(true));
        assertTrue(tu.isSelected());
        assertTrue(rule.dayOfWeekWithoutOrdinalList().contains(DayOfWeek.TUESDAY));
        // Wednesday already selected
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
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(Frequency.FrequencyType.MONTHLY));
        
        // Monthly properties
        RadioButton dayOfMonth = find("#dayOfMonthRadioButton");
        RadioButton dayOfWeek = find("#dayOfWeekRadioButton");
        Frequency f = v.getRRule().getFrequency(); // refresh reference
        
        // Check initial state
        assertTrue(dayOfMonth.isSelected());
        assertFalse(dayOfWeek.isSelected());
        assertEquals(Frequency.FrequencyType.MONTHLY, f.frequencyType());
        assertEquals(0, f.getByRules().size());
        
        // Toggle monthly options and check
        TestUtil.runThenWaitForPaintPulse(() -> dayOfWeek.setSelected(true));
        assertFalse(dayOfMonth.isSelected());
        assertTrue(dayOfWeek.isSelected());
        assertEquals(1, f.getByRules().size());
        ByDay rule = (ByDay) f.getByRuleByType(ByRules.BYDAY);
        assertEquals("BYDAY=2WE", rule.toString()); // 2nd Wednesday of the month
        }
        
        // YEARLY
        {
        TestUtil.runThenWaitForPaintPulse(() -> frequencyComboBox.getSelectionModel().select(Frequency.FrequencyType.YEARLY));
        Frequency f = v.getRRule().getFrequency(); // refresh reference

        // Check initial state
        assertEquals(Frequency.FrequencyType.YEARLY, f.frequencyType());
        assertEquals(0, f.getByRules().size());
        }

        closeCurrentWindow();
    }

    @Test
    public void canChangeInterval()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties
        Spinner<Integer> intervalSpinner = find("#intervalSpinner");

        // Check initial state
        assertEquals((Integer) 1, intervalSpinner.getValue());
        assertEquals((Integer) 1, v.getRRule().getFrequency().getInterval());
        
        // Change property and verify state change
        TestUtil.runThenWaitForPaintPulse( () -> intervalSpinner.getValueFactory().increment(3));       
        assertEquals((Integer) 4, intervalSpinner.getValue());
        assertEquals((Integer) 4, v.getRRule().getFrequency().getInterval());
        
        closeCurrentWindow();
    }
    
    @Test
    public void canChangeStartDate()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties        
        DatePicker startDatePicker = find("#startDatePicker");

        // Check initial state
        assertEquals(LocalDate.of(2015, 11, 9), startDatePicker.getValue());
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), v.getDateTimeStart());
        
        // Change property and verify state change
        TestUtil.runThenWaitForPaintPulse( () -> startDatePicker.setValue(LocalDate.of(2015, 11, 10)));
        assertEquals(LocalDateTime.of(2015, 11, 10, 10, 0), v.getDateTimeStart());
        
        closeCurrentWindow();
    }
    
    @Test
    public void canChangeEndsCriteria()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);

        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties
        RadioButton endNeverRadioButton = find("#endNeverRadioButton");
        RadioButton endAfterRadioButton = find("#endAfterRadioButton");
        Spinner<Integer> endAfterEventsSpinner = find("#endAfterEventsSpinner");
        RadioButton untilRadioButton = find("#untilRadioButton");
        DatePicker untilDatePicker = find("#untilDatePicker");

        // Check initial state
        assertTrue(endNeverRadioButton.isSelected());
        assertFalse(endAfterRadioButton.isSelected());
        assertFalse(untilRadioButton.isSelected());
        assertEquals((Integer) 0, v.getRRule().getCount());
        assertNull(v.getRRule().getUntil());
        
        // Change property and verify state change
        // Ends After (COUNT)
        TestUtil.runThenWaitForPaintPulse( () -> endAfterRadioButton.setSelected(true));
        assertFalse(endNeverRadioButton.isSelected());
        assertTrue(endAfterRadioButton.isSelected());
        assertFalse(untilRadioButton.isSelected());
        assertEquals((Integer) 10, v.getRRule().getCount());
        TestUtil.runThenWaitForPaintPulse( () -> endAfterEventsSpinner.getValueFactory().increment(5));
        assertEquals((Integer) 15, v.getRRule().getCount());

        // Ends On (UNTIL)
        TestUtil.runThenWaitForPaintPulse( () -> untilRadioButton.setSelected(true));
        assertFalse(endNeverRadioButton.isSelected());
        assertFalse(endAfterRadioButton.isSelected());
        assertTrue(untilRadioButton.isSelected());
        assertEquals(LocalDateTime.of(2015, 12, 9, 10, 0), v.getRRule().getUntil());
        TestUtil.runThenWaitForPaintPulse( () -> untilDatePicker.setValue(LocalDate.of(2016, 1, 1)));
        assertEquals(LocalDateTime.of(2016, 1, 1, 10, 0), v.getRRule().getUntil());
        
        closeCurrentWindow();
    }
    
    // edit repeatable elements
    @Test
//    @Ignore
    public void canEditVComponent2()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.vComponents().add(getDaily1()));
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);
        
        // Open edit popup
        move("#hourLine11");
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties
        CheckBox repeatableCheckBox = find("#repeatableCheckBox");
        ComboBox<Frequency.FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        Spinner<Integer> intervalSpinner = find("#intervalSpinner");
        RadioButton endNeverRadioButton = find("#endNeverRadioButton");
        
        // Check initial state
        assertTrue(repeatableCheckBox.isSelected());
        assertEquals(Frequency.FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        assertTrue(endNeverRadioButton.isSelected());
        assertEquals((Integer) 1, v.getRRule().getFrequency().getInterval());
        assertEquals((Integer) 0, v.getRRule().getCount());
        
        closeCurrentWindow();
    }
    
    private void renderAppointmentByDragging()
    {;
        Assert.assertEquals(0, agenda.appointments().size());
        move("#hourLine10");
        press(MouseButton.PRIMARY);
        move("#hourLine12");
        release(MouseButton.PRIMARY);
        dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        
        Assert.assertEquals(1, agenda.vComponents().size());
        Assert.assertEquals("2015-11-11T10:00", agenda.appointments().get(0).getStartLocalDateTime().toString() );
        Assert.assertEquals("2015-11-11T12:00", agenda.appointments().get(0).getEndLocalDateTime().toString() );
        find("#AppointmentRegularBodyPane2015-11-11/0"); // validate that the pane has the expected id
    }
        
    @Test
//    @Ignore
    public void renderRepeatableAppointmentByDragging()
    {
        renderAppointmentByDragging();
        // Open edit popup
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        
        // select repeat options (weekly by default)
        click("#repeatableTab");
        click("#repeatableCheckBox");
        click("#fridayCheckBox");
        click("#mondayCheckBox");
        click("#closeRepeatButton");
        
        Assert.assertEquals(2, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20151111T100000" + System.lineSeparator()
                + "RRULE:FREQ=WEEKLY;BYDAY=WE,FR,MO" + System.lineSeparator()
                + "SUMMARY:New" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
        
        List<LocalDateTime> dates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        Assert.assertEquals(expectedDates, dates);
    }
    
    @Test
//    @Ignore
    public void createRepeatableAppointment2()
    {
        renderAppointmentByDragging();
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
                
        // type value
        click("#repeatableTab");
        click("#repeatableCheckBox");
        click("#frequencyComboBox");
//        ComboBox<Frequency.FrequencyType> frequencyComboBox = (ComboBox<Frequency.FrequencyType>) find("#frequencyComboBox");
        click("Daily");
        click("#intervalSpinner");
        Spinner<Integer> interval = find("#intervalSpinner");
        interval.getEditor().selectAll();
        type("3");
        click("#endAfterRadioButton");
        click("#endAfterEventsSpinner");
        Spinner<Integer> after = find("#endAfterEventsSpinner");
        after.getEditor().selectAll();
        type("6");
        click("#closeRepeatButton");
        Assert.assertEquals(2, agenda.appointments().size());
        VComponent<Appointment> v = agenda.vComponents().get(0);
        
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group00" + System.lineSeparator()
                + "DTEND:20151111T120000" + System.lineSeparator()
                + "DTSTAMP:" + dateTimeStamp + System.lineSeparator()
                + "DTSTART:20151111T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "SUMMARY:New" + System.lineSeparator()
                + "UID:20151108T000000-0jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        Assert.assertEquals(expectedString, v.toString());
        
        List<LocalDateTime> dates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 10, 0)
                ));
        Assert.assertEquals(expectedDates, dates);
    }
    
    @Test
    @Ignore
    public void canEditVComponent3()
    {
        String dateTimeStamp = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.appointments().add( new ICalendarAgenda.AppointmentImplLocal2()
                .withStartLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T10:00"))
                .withEndLocalDateTime(TestUtil.quickParseLocalDateTimeYMDhm("2015-11-11T12:00"))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(0))
            );
        });
        
        System.out.println(agenda.appointments().size());

        
        TestUtil.sleep(3000);
    }
    
    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

}
