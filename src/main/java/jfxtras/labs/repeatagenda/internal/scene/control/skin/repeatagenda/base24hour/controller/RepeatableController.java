package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AlertsAndDialogs;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class RepeatableController {

//private RepeatableAppointment appointment;
private Repeat repeat;

@FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

@FXML private CheckBox repeatableCheckBox;
@FXML private GridPane repeatableGridPane;
@FXML private ComboBox<Repeat.Frequency> intervalComboBox;
@FXML private Spinner<Integer> frequencySpinner;
@FXML private Label unitLabel;
@FXML private Label eventLabel;
@FXML private Label weeklyLabel;
@FXML private HBox weeklyHBox;
@FXML private CheckBox sundayCheckBox;
@FXML private CheckBox mondayCheckBox;
@FXML private CheckBox tuesdayCheckBox;
@FXML private CheckBox wednesdayCheckBox;
@FXML private CheckBox thursdayCheckBox;
@FXML private CheckBox fridayCheckBox;
@FXML private CheckBox saturdayCheckBox;
@FXML private VBox monthlyVBox;
@FXML private Label monthlyLabel;
@FXML private RadioButton dayOfMonthRadioButton;
@FXML private RadioButton dayOfWeekRadioButton;
@FXML private DatePicker startDatePicker;
@FXML private RadioButton endNeverRadioButton;
@FXML private RadioButton endAfterRadioButton;
@FXML private Spinner<Integer> endAfterEventsSpinner;
@FXML private RadioButton endOnRadioButton;
@FXML private DatePicker endOnDatePicker;
private ToggleGroup endGroup;
@FXML private Label repeatSummaryLabel;

@FXML private Button closeButton;
@FXML private Button cancelButton;

final private InvalidationListener makeEndOnDateListener = (obs) -> repeat.makeUntilFromCount();

final private ChangeListener<? super Integer> frequencyListener = (observable, oldValue, newValue) ->
{
    if (newValue == 1) {
        unitLabel.setText(repeat.getFrequency().toStringSingular());
    } else {
        unitLabel.setText(repeat.getFrequency().toStringPlural());
    }
};

final private ChangeListener<? super LocalDate> startDateListener = ((observable, oldSelection, newSelection) ->
{
    LocalTime startTime = repeat.getStartLocalDate().toLocalTime();
    repeat.setStartLocalDate(newSelection.atTime(startTime));
});

@FXML public void initialize()
{
    
    // *********INTERVAL COMBOBOX**************
    final ObservableList<Repeat.Frequency> intervalList = FXCollections.observableArrayList();
    intervalList.add(Repeat.Frequency.DAILY);
    intervalList.add(Repeat.Frequency.WEEKLY);
    intervalList.add(Repeat.Frequency.MONTHLY);
    intervalList.add(Repeat.Frequency.YEARLY);
    intervalComboBox.setItems(intervalList);
    
    intervalComboBox.getSelectionModel().selectedItemProperty().addListener((change) -> {
        Repeat.Frequency selected = intervalComboBox.getSelectionModel().getSelectedItem();
        if (selected == Repeat.Frequency.DAILY || selected == Repeat.Frequency.YEARLY) {
            monthlyVBox.setVisible(false);
            monthlyLabel.setVisible(false);
            weeklyHBox.setVisible(false);
            weeklyLabel.setVisible(false);
        } else if (selected == Repeat.Frequency.WEEKLY) {
            monthlyVBox.setVisible(false);
            monthlyLabel.setVisible(false);
            weeklyHBox.setVisible(true);
            weeklyLabel.setVisible(true);
        } else if (selected == Repeat.Frequency.MONTHLY) {
            monthlyVBox.setVisible(true);
            monthlyLabel.setVisible(true);
            weeklyHBox.setVisible(false);
            weeklyLabel.setVisible(false);
        }
    });
    intervalComboBox.setConverter(Repeat.Frequency.stringConverter);

    frequencySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

    frequencySpinner.valueProperty().addListener((observable, oldValue, newValue) ->
    {
        if (newValue == 1) {
            unitLabel.setText(repeat.getFrequency().toStringSingular());
        } else {
            unitLabel.setText(repeat.getFrequency().toStringPlural());
        }
    });
    
    // Make frequencySpinner and only accept numbers (needs below two listeners)
    frequencySpinner.setEditable(true);
    frequencySpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, (event)  ->
    {
        if (event.getCode() == KeyCode.ENTER) {
            String s = frequencySpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (! isNumber) {
                String lastValue = frequencySpinner.getValue().toString();
                frequencySpinner.getEditor().textProperty().set(lastValue);
                AlertsAndDialogs.notNumberAlert("123");
            }
        }
    });
    frequencySpinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) ->
    {
        if (! isNowFocused) {
            int value;
            String s = frequencySpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (isNumber) {
                value = Integer.parseInt(s);
                repeat.intervalProperty().unbind();
                repeat.setInterval(value);
            } else {
                String lastValue = frequencySpinner.getValue().toString();
                frequencySpinner.getEditor().textProperty().set(lastValue);
                AlertsAndDialogs.notNumberAlert("123");
            }
        }
    });

    // Listeners to change unitLabel
    intervalComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
    {
        if (repeat.getEndCriteria() == EndCriteria.AFTER) repeat.makeUntilFromCount();
        if (frequencySpinner.getValue() == 1) {
            unitLabel.setText(repeat.getFrequency().toStringSingular());
        } else {
            unitLabel.setText(repeat.getFrequency().toStringPlural());
        }
    });
    
    // Day of week tooltips
    sundayCheckBox.setTooltip(new Tooltip(resources.getString("sunday")));
    mondayCheckBox.setTooltip(new Tooltip(resources.getString("monday")));
    tuesdayCheckBox.setTooltip(new Tooltip(resources.getString("tuesday")));
    wednesdayCheckBox.setTooltip(new Tooltip(resources.getString("wednesday")));
    thursdayCheckBox.setTooltip(new Tooltip(resources.getString("thursday")));
    fridayCheckBox.setTooltip(new Tooltip(resources.getString("friday")));
    saturdayCheckBox.setTooltip(new Tooltip(resources.getString("saturday")));
    
    final ToggleGroup monthlyGroup = new ToggleGroup();
    dayOfMonthRadioButton.setToggleGroup(monthlyGroup);
    dayOfWeekRadioButton.setToggleGroup(monthlyGroup);

    // End criteria ToggleGroup and change listeners
    endGroup = new ToggleGroup();
    endNeverRadioButton.setToggleGroup(endGroup);
    endAfterRadioButton.setToggleGroup(endGroup);
    endOnRadioButton.setToggleGroup(endGroup);
    
    endNeverRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) -> {
        if (newSelection) repeat.setEndCriteria(EndCriteria.NEVER); });

    endAfterEventsSpinner.valueProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (! repeat.countProperty().isBound()) {
            endAfterEventsSpinner.getValueFactory().setValue(repeat.getCount());
            repeat.countProperty().bind(endAfterEventsSpinner.valueProperty());   
        }
        if (newSelection == 1) {
            eventLabel.setText(resources.getString("event"));
        } else {
            eventLabel.setText(resources.getString("events"));
        }
    });
    
    endAfterRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection) {
            endAfterEventsSpinner.setDisable(false);
            eventLabel.setDisable(false);
            repeat.setEndCriteria(EndCriteria.AFTER);
            endAfterEventsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, repeat.getCount()));
            repeat.countProperty().bind(endAfterEventsSpinner.valueProperty());
            repeat.intervalProperty().removeListener(makeEndOnDateListener); // in case listener was previously added remove it to ensure only 1 is attached
            repeat.intervalProperty().addListener(makeEndOnDateListener);
        } else  {
            endAfterEventsSpinner.setValueFactory(null);
            endAfterEventsSpinner.setDisable(true);
            eventLabel.setDisable(true);
        }
    });

    // Make endAfterEventsSpinner and only accept numbers in text field (needs below two listeners)
    endAfterEventsSpinner.setEditable(true);
    endAfterEventsSpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, (event)  ->
    {
        if (event.getCode() == KeyCode.ENTER) {
            String s = endAfterEventsSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (! isNumber) {
                String lastValue = endAfterEventsSpinner.getValue().toString();
                endAfterEventsSpinner.getEditor().textProperty().set(lastValue);
                AlertsAndDialogs.notNumberAlert("123");
            }
        }
    });
    endAfterEventsSpinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) ->
    {
        if (! isNowFocused) {
            int value;
            String s = endAfterEventsSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (isNumber) {
                value = Integer.parseInt(s);
                repeat.countProperty().unbind();
                repeat.setCount(value);
            } else {
                String lastValue = endAfterEventsSpinner.getValue().toString();
                endAfterEventsSpinner.getEditor().textProperty().set(lastValue);
                AlertsAndDialogs.notNumberAlert("123");
            }
        }
    });
    
    
    final ChangeListener<? super LocalDate> endOnDateListener = ((observable, oldSelection, newSelection) ->
    {
        LocalTime endTime = repeat.getStartLocalDate().plusSeconds(repeat.getDurationInSeconds()).toLocalTime();
        repeat.setUntil(newSelection.atTime(endTime));
    });
    endOnRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection) {
            if (repeat.getEndCriteria() == EndCriteria.UNTIL)
            { // if Repeat is ON already (initial condition) then set date in picker
                endOnDatePicker.setValue(repeat.getUntil().toLocalDate());
            }
            repeat.setEndCriteria(EndCriteria.UNTIL); 
            endOnDatePicker.setDisable(false);
            endOnDatePicker.valueProperty().addListener(endOnDateListener);
//            repeat.endOnDateProperty().bind(endOnDatePicker.valueProperty());
            endOnDatePicker.show();
        } else {
            endOnDatePicker.setDisable(true);
            endOnDatePicker.valueProperty().removeListener(endOnDateListener);
//            repeat.endOnDateProperty().unbind();
        }
    });
    
}


/**
 * 
 * @param appointment
 * @param dateTimeRange : date range for current agenda skin
 */
    public void setupData(RepeatableAppointment appointment, LocalDateTimeRange dateTimeRange) {

//        this.appointment = appointment;
        if (appointment.getRepeat() != null)
        { // get existing repeat
            repeat = appointment.getRepeat();
        } else { // make new repeat
//            repeat = new Repeat();
            repeat = RepeatFactory.newRepeat(dateTimeRange);
            repeat.setDefaults();
            appointment.copyNonDateFieldsInto(repeat.getAppointmentData());
            repeat.setStartLocalDate(appointment.getStartLocalDateTime());
//            repeat.setStartLocalTime(appointment.getStartLocalDateTime().toLocalTime());
            int duration = (int) ChronoUnit.SECONDS.between(appointment.getStartLocalDateTime()
                    , appointment.getEndLocalDateTime());
            repeat.setDurationInSeconds(duration);
            DayOfWeek d = appointment.getStartLocalDateTime().getDayOfWeek();
            repeat.setDayOfWeek(d, true); // set default day of week for default Weekly appointment
        }
        
        //        setupAppointmentBindings();

//        if (repeat.getEndCriteria() == EndCriteria.AFTER)
//        {
//            System.out.println("repeat.getEndAfterEvents() " + repeat.getEndAfterEvents());
//            if (repeat.getEndAfterEvents() == 1) {
//                System.out.println("event");
//                eventLabel.setText(resources.getString("event"));
//            } else {
//                System.out.println("events");
//                eventLabel.setText(resources.getString("events"));
//            }
//        }

        // TODO - REMOVE CAST TO MYREPEAT
//        if (! ((MyRepeat)repeat).hasKey()) startDatePicker.setDisable(true);
        
        // REPEATABLE CHECKBOX
        repeatableCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if (newSelection) {
                appointment.setRepeat(repeat);
//                repeat.getAppointments().add(appointment);
                setupBindings();
                repeatableGridPane.setDisable(false);
                startDatePicker.setDisable(false);
            } else {
                appointment.setRepeat(null);
//                repeat.getAppointments().remove(appointment);
                removeBindings();
                repeatableGridPane.setDisable(true);
                startDatePicker.setDisable(true);
            }
        });

        // Check repeatable box if appointment has a Repeat
        repeatableCheckBox.selectedProperty().set(appointment.getRepeat() != null);        
    }
    
    private void setupBindings() {
        intervalComboBox.valueProperty().bindBidirectional(repeat.frequencyProperty());
        frequencySpinner.getValueFactory().setValue(repeat.getInterval());
        repeat.intervalProperty().bind(frequencySpinner.valueProperty());
        sundayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SUNDAY));
        mondayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.MONDAY));
        tuesdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.TUESDAY));
        wednesdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.WEDNESDAY));
        thursdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.THURSDAY));
        fridayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.FRIDAY));
        saturdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SATURDAY));
        dayOfMonthRadioButton.selectedProperty().bindBidirectional(repeat.repeatDayOfMonthProperty());
        dayOfWeekRadioButton.selectedProperty().bindBidirectional(repeat.repeatDayOfWeekProperty());
//        startDatePicker.valueProperty().bind(repeat.startLocalDateProperty());
        startDatePicker.valueProperty().addListener(startDateListener);
        setEndGroup(repeat.getEndCriteria());
        repeat.countProperty().addListener(makeEndOnDateListener);
    }
    
    private void setupAppointmentBindings() {

//        // Setup bindings to appointment object
//        appointment.startLocalDateTimeProperty().addListener((obs) -> repeat.setStartLocalTime(appointment.getStartLocalDateTime().toLocalTime()));
//        appointment.endLocalDateTimeProperty().addListener((obs) -> repeat.setEndLocalTime(appointment.getEndLocalDateTime().toLocalTime()));
//        repeat.getAppointmentData().appointmentGroupProperty().bind(appointment.appointmentGroupProperty());
//        repeat.getAppointmentData().descriptionProperty().bind(appointment.descriptionProperty());
////        repeat.getAppointmentData().locationKeyProperty().bind(appointment.locationKeyProperty());
////        repeat.getAppointmentData().styleKeyProperty().bind(appointment.styleKeyProperty());
//        repeat.getAppointmentData().summaryProperty().bind(appointment.summaryProperty());
////        repeat.getAppointmentData().getStaffKeys().addAll(appointment.getStaffKeys());
    }
    
    private void removeBindings() {
        // Establish bindings to Repeat object
        intervalComboBox.valueProperty().unbindBidirectional(repeat.frequencyProperty());
        intervalComboBox.valueProperty().set(null);
        sundayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SUNDAY));
        mondayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.MONDAY));
        tuesdayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.TUESDAY));
        wednesdayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.WEDNESDAY));
        thursdayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.THURSDAY));
        fridayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.FRIDAY));
        saturdayCheckBox.selectedProperty().unbindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SATURDAY));
        dayOfMonthRadioButton.selectedProperty().unbindBidirectional(repeat.repeatDayOfMonthProperty());
        dayOfWeekRadioButton.selectedProperty().unbindBidirectional(repeat.repeatDayOfWeekProperty());
//        startDatePicker.valueProperty().unbind();
        startDatePicker.valueProperty().removeListener(startDateListener);
        endGroup.selectToggle(null);
        removeRepeatBindings();
    }
    
    public void removeRepeatBindings() {
        repeat.intervalProperty().unbind();
        repeat.countProperty().unbind();
        repeat.countProperty().removeListener(makeEndOnDateListener);
        repeat.intervalProperty().removeListener(makeEndOnDateListener);
    }
        
    private void setEndGroup(EndCriteria endCriteria) {
        switch (endCriteria) {
        case NEVER:
            endGroup.selectToggle(endNeverRadioButton);
            break;
        case AFTER:
            endGroup.selectToggle(endAfterRadioButton);
            break;
        case UNTIL:
            endGroup.selectToggle(endOnRadioButton);
            break;
        default:
            break;
        }
    }

}
