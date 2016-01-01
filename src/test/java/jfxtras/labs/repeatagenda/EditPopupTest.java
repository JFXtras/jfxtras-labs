package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
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
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.vComponents().add(getIndividual1());
        });
        
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
        
        // Check editing properties
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
        
        click("#closeAppointmentButton");
//        TestUtil.sleep(3000);
        // Check appointment edited
        assertEquals(1, agenda.appointments().size());
        Appointment a = agenda.appointments().get(0);
        assertEquals(LocalDateTime.of(2015, 11, 11, 8, 0), a.getStartLocalDateTime());
        assertEquals(LocalDateTime.of(2015, 11, 11, 9, 0), a.getEndLocalDateTime());
        assertEquals("new summary", a.getSummary());
        assertEquals("new description", a.getDescription());
//        assertEquals("new location", a.getLocation());
        System.out.println("appointmentGroups2:" + appointmentGroups());

        AppointmentGroup group = agenda.appointmentGroups().get(11);
        System.out.println(group.getDescription() + " " + a.getAppointmentGroup().getDescription());
        assertEquals(group, a.getAppointmentGroup());
        assertEquals("new group name", group.getDescription());
    }
    
    // edit repeatable elements
    @Test
    @Ignore
    public void canEditVComponent2()
    {
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.vComponents().add(getDaily1());
        });
        
        VEvent<Appointment> v = (VEvent<Appointment>) agenda.vComponents().get(0);
        
        // Open edit popup
        move("#hourLine11"); // open edit popup
        press(MouseButton.SECONDARY);
        release(MouseButton.SECONDARY);
        click("#repeatableTab");

        // Get properties
        CheckBox repeatableCheckBox = find("#repeatableCheckBox");
        ComboBox<Frequency.FrequencyType> frequencyComboBox = find("#frequencyComboBox");
        RadioButton endNeverRadioButton = find("#endNeverRadioButton");
        
        // Check initial state
        assertTrue(repeatableCheckBox.isSelected());
        assertEquals(Frequency.FrequencyType.DAILY, frequencyComboBox.getSelectionModel().getSelectedItem());
        assertTrue(endNeverRadioButton.isSelected());
        
        click("#closeRepeatButton");

        ComboBox<ChangeDialogOptions> c = find("#edit_dialog_combobox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            c.getSelectionModel().select(ChangeDialogOptions.ALL);
        });
        click("#edit_dialog_button_ok");
        TestUtil.sleep(3000);
    }
    
    public void renderAppointmentByDragging()
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
    @Ignore
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
    @Ignore
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
                .withAppointmentGroup(DEFAULT_APPOINTMENT_GROUPS.get(0))
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
