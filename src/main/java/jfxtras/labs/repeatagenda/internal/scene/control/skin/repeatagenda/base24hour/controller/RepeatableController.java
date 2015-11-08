package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import javafx.util.StringConverter;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public class RepeatableController {

final private static int EXCEPTION_CHOICE_LIMIT = 100;
    
//private RepeatableAppointment appointment;
private Repeat repeat;

@FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

@FXML private CheckBox repeatableCheckBox;
@FXML private GridPane repeatableGridPane;
@FXML private ComboBox<Repeat.Frequency> frequencyComboBox;
@FXML private Spinner<Integer> intervalSpinner;
@FXML private Label frequencyLabel;
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

@FXML ComboBox<LocalDateTime> exceptionComboBox;
@FXML Button addExceptionButton;
@FXML ListView<LocalDateTime> exceptionsListView;
@FXML Button removeExceptionButton;

@FXML private Button closeButton;
@FXML private Button cancelButton;
// TODO - DO I WANT A DELETE BUTTON?

//final private StringConverter<LocalDateTime> exceptionDateConverter = new StringConverter<LocalDateTime>()
//{
//    final private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.start"));
//    @Override public String toString(LocalDateTime d) {
//        return formatter.format(d);
//    }
//    @Override public LocalDateTime fromString(String string) {
//        throw new RuntimeException("not required for non editable ComboBox");
//    }
//};

final private InvalidationListener makeEndOnDateListener = (obs) -> repeat.makeUntilFromCount();

// This listener is executed when any repeat changes occur to ensure exception date list in exceptionComboBox is valid
final private InvalidationListener makeExceptionDatesListener = (obs) -> 
{
    List<LocalDateTime> exceptionDates = repeat.streamOfDatesEndless()
          .limit(EXCEPTION_CHOICE_LIMIT)
          .collect(Collectors.toList());
    exceptionComboBox.getItems().addAll(exceptionDates);
};

final private ChangeListener<? super Integer> frequencyListener = (observable, oldValue, newValue) ->
{
    if (newValue == 1) {
        frequencyLabel.setText(repeat.getFrequency().toStringSingular());
    } else {
        frequencyLabel.setText(repeat.getFrequency().toStringPlural());
    }
};

final private ChangeListener<? super LocalDate> startDateListener = ((observable, oldSelection, newSelection) ->
{
    LocalTime startTime = repeat.getStartLocalDateTime().toLocalTime();
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
    frequencyComboBox.setItems(intervalList);
    
    frequencyComboBox.getSelectionModel().selectedItemProperty().addListener((obs) -> {
        Repeat.Frequency selected = frequencyComboBox.getSelectionModel().getSelectedItem();
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
    
       // change frequencyLabel to be singular or plural
        if (repeat.getEndCriteria() == EndCriteria.AFTER) repeat.makeUntilFromCount();
        if (intervalSpinner.getValue() == 1) {
            frequencyLabel.setText(repeat.getFrequency().toStringSingular());
        } else {
            frequencyLabel.setText(repeat.getFrequency().toStringPlural());
        }
    });
    frequencyComboBox.setConverter(Repeat.Frequency.stringConverter);

//    // Listeners to change unitLabel
//    frequencyComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
//    {
//        if (repeat.getEndCriteria() == EndCriteria.AFTER) repeat.makeUntilFromCount();
//        if (intervalSpinner.getValue() == 1) {
//            frequencyLabel.setText(repeat.getFrequency().toStringSingular());
//        } else {
//            frequencyLabel.setText(repeat.getFrequency().toStringPlural());
//        }
//    });
    
    intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));

    intervalSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
    {
        if (newValue == 1) {
            frequencyLabel.setText(repeat.getFrequency().toStringSingular());
        } else {
            frequencyLabel.setText(repeat.getFrequency().toStringPlural());
        }
    });
    
    // Make frequencySpinner and only accept numbers (needs below two listeners)
    intervalSpinner.setEditable(true);
    intervalSpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, (event)  ->
    {
        if (event.getCode() == KeyCode.ENTER) {
            String s = intervalSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (! isNumber) {
                String lastValue = intervalSpinner.getValue().toString();
                intervalSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert("123");
            }
        }
    });
    intervalSpinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) ->
    {
        if (! isNowFocused) {
            int value;
            String s = intervalSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (isNumber) {
                value = Integer.parseInt(s);
                repeat.intervalProperty().unbind();
                repeat.setInterval(value);
            } else {
                String lastValue = intervalSpinner.getValue().toString();
                intervalSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert("123");
            }
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
                notNumberAlert("123");
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
                notNumberAlert("123");
            }
        }
    });
    
    
    final ChangeListener<? super LocalDate> endOnDateListener = ((observable, oldSelection, newSelection) ->
    {
        LocalTime endTime = repeat.getStartLocalDateTime().plusSeconds(repeat.getDurationInSeconds()).toLocalTime();
        repeat.setUntilLocalDateTime(newSelection.atTime(endTime));
    });
    endOnRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection) {
            if (repeat.getEndCriteria() == EndCriteria.UNTIL)
            { // if Repeat is ON already (initial condition) then set date in picker
                endOnDatePicker.setValue(repeat.getUntilLocalDateTime().toLocalDate());
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
    public void setupData(
            RepeatableAppointment appointment
          , LocalDateTimeRange dateTimeRange
          , Class<? extends RepeatableAppointment> appointmentClass
          , Class<? extends Repeat> repeatClass)
    {

        if (appointment.getRepeat() != null)
        { // get existing repeat
            repeat = appointment.getRepeat();
//            repeat = repeatMap.get(appointment);
        } else { // make new repeat
            repeat = RepeatFactory.newRepeat(repeatClass, appointmentClass);
            repeat.setLocalDateTimeDisplayRange(dateTimeRange);
            System.out.println("new repeat " + repeat );
//            repeatClass.newInstance();
//            if (false){
//            Callback<LocalDateTimeRange, Appointment> newAppointmentCallback = null;
//            repeat = new RepeatImpl(dateTimeRange, newAppointmentCallback);
//            }
//            repeat = RepeatFactory.newRepeat(dateTimeRange);
            repeat.setDefaults();
            System.out.println("copynondate fields " + appointment + " " + repeat.getAppointmentData());
            appointment.copyFieldsTo(repeat.getAppointmentData());
            repeat.setStartLocalDate(appointment.getStartLocalDateTime());
//            repeat.setStartLocalTime(appointment.getStartLocalDateTime().toLocalTime());
            int duration = (int) ChronoUnit.SECONDS.between(appointment.getStartLocalDateTime()
                    , appointment.getEndLocalDateTime());
            repeat.setDurationInSeconds(duration);
            DayOfWeek d = appointment.getStartLocalDateTime().getDayOfWeek();
            repeat.setDayOfWeek(d, true); // set default day of week for default Weekly appointment
        }

        // Setup exceptionComboBox string converter - must be done after initialization because resource bundle isn't instantiated before now
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.exception"));
        exceptionComboBox.setConverter(new StringConverter<LocalDateTime>()
        { // setup string converter
            @Override public String toString(LocalDateTime d) {
                return formatter.format(d);
            }
            @Override public LocalDateTime fromString(String string) {
                throw new RuntimeException("not required for non editable ComboBox");
            }
        });
        exceptionComboBox.valueProperty().addListener(obs -> addExceptionButton.setDisable(false)); // turn on add button when exception date is selected in combobox

     // Custom rendering of the table cell -  must be done after initialization because resource bundle isn't instantiated before now.
        exceptionsListView.setCellFactory(column -> {
            return new ListCell<LocalDateTime>() {
                @Override
                protected void updateItem(LocalDateTime item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format date.
                        setText(formatter.format(item));

//                        // TODO - Style invalid dates red (need to test every date to see if valid)
//                        if (repeat.isValidDateTime(item)) {
//                            setStyle("-fx-background-color: red");
//                        }
                    }
                }
            };
        });
        exceptionsListView.getSelectionModel().selectedItemProperty().addListener(obs -> removeExceptionButton.setDisable(false)); // turn on add button when exception date is selected in combobox        
        
        // Listeners to update exception dates
        repeatableCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        
        // REPEATABLE CHECKBOX
        repeatableCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if (newSelection) {
                appointment.setRepeat(repeat);
//                makeExceptionDates();
                setupBindings();
                repeatableGridPane.setDisable(false);
                startDatePicker.setDisable(false);
            } else {
                appointment.setRepeat(null);
                removeBindings();
                repeatableGridPane.setDisable(true);
                startDatePicker.setDisable(true);
            }
        });

        // Check repeatable box if appointment has a Repeat
        repeatableCheckBox.selectedProperty().set(appointment.getRepeat() != null);        
        startDatePicker.setValue(repeat.getStartLocalDateTime().toLocalDate());
//        repeatableCheckBox.selectedProperty().set(repeatMap.containsKey(appointment));
    }
    
    private void setupBindings() {
        frequencyComboBox.valueProperty().bindBidirectional(repeat.frequencyProperty());
        intervalSpinner.getValueFactory().setValue(repeat.getInterval());
        repeat.intervalProperty().bind(intervalSpinner.valueProperty());
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
        exceptionsListView.setItems(repeat.getExceptions());
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
        frequencyComboBox.valueProperty().unbindBidirectional(repeat.frequencyProperty());
        frequencyComboBox.valueProperty().set(null);
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
    
    // Displays an alert notifying user number input is not valid
    private static void notNumberAlert(String validFormat)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Number");
        alert.setHeaderText("Please enter valid numbers.");
        alert.setContentText("Accepted format: " + validFormat);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        Optional<ButtonType> result = alert.showAndWait();
    }
    
//    private void makeExceptionDates()
//    {
//        List<LocalDateTime> exceptionDates = repeat.streamOfDates()
//                .limit(EXCEPTION_CHOICE_LIMIT)
//                .collect(Collectors.toList());
//          exceptionComboBox.getItems().addAll(exceptionDates);
//    }
    
    @FXML private void handleAddException()
    {
        LocalDateTime d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
        exceptionComboBox.getItems().remove(d);
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        System.out.println("Remove Exception");
        LocalDateTime d = exceptionsListView.getSelectionModel().getSelectedItem();
        exceptionComboBox.getItems().add(d);
        exceptionsListView.getItems().remove(d);
        if (exceptionsListView.getSelectionModel().getSelectedItem() == null) removeExceptionButton.setDisable(true);
    }


}
