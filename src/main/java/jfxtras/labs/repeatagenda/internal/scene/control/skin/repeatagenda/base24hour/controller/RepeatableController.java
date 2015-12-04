package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
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
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency.FrequencyType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Weekly;

public class RepeatableController<T> {

final private static int EXCEPTION_CHOICE_LIMIT = 50;
    
//private RepeatableAppointment appointment;
private VComponent<T> vComponent;
private Rule byRule;
private ByDay byDayRule; // is null if no ByDay rule is present
private LocalDateTime dateTimeStartInstanceNew;

@FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

@FXML private CheckBox repeatableCheckBox;
@FXML private GridPane repeatableGridPane;
@FXML private ComboBox<Frequency.FrequencyType> frequencyComboBox;
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
@FXML ListView<LocalDateTime> exceptionsListView; // list of date/times to be skipped (deleted events)
@FXML Button removeExceptionButton;

@FXML private Button closeButton;
@FXML private Button cancelButton;

// DAY OF WEEK LISTENERS
private final ChangeListener<? super Boolean> sundayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.SUNDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.SUNDAY);
    }
};
private final ChangeListener<? super Boolean> mondayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.MONDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.MONDAY);
    }
};
private final ChangeListener<? super Boolean> tuesdayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.TUESDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.TUESDAY);
    }
};
private final ChangeListener<? super Boolean> wednesdayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.WEDNESDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.WEDNESDAY);
    }
};
private final ChangeListener<? super Boolean> thursdayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.THURSDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.THURSDAY);
    }
};
private final ChangeListener<? super Boolean> fridayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.FRIDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.FRIDAY);
    }
};
private final ChangeListener<? super Boolean> saturdayListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        byDayRule.addDayOfWeek(DayOfWeek.SATURDAY);
    } else
    {
        byDayRule.removeDayOfWeek(DayOfWeek.SATURDAY);
    }
};

// Listener for dayOfWeekRadioButton when frequency if monthly
private ChangeListener<? super Boolean> dayOfWeekListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        System.out.println("add byDay:");
        WeekFields weekFields = WeekFields.of(Locale.getDefault());
        int ordinalWeekNumber = dateTimeStartInstanceNew.get(weekFields.weekOfMonth());
        DayOfWeek dayOfWeek = dateTimeStartInstanceNew.getDayOfWeek();
        byDayRule = new ByDay(new ByDayPair(dayOfWeek, ordinalWeekNumber));
        vComponent.getRRule().getFrequency().addByRule(byDayRule);
    } else
    { // remove rule to reset to default behavior of repeat by day of month
        System.out.println("remove byDay:");
        vComponent.getRRule().getFrequency().getByRules().clear();
    }
};

// FREQUENCY CHANGE LISTENER
private final ChangeListener<? super FrequencyType> frequencyListener = (obs, oldSel, newSel) -> 
{
    // Change Frequency if different, reset Interval to 1
    if (vComponent.getRRule().getFrequency().getFrequencyType() != newSel)
    {
        vComponent.getRRule().setFrequency(newSel.newInstance());
        intervalSpinner.getEditor().textProperty().set("1");
        byDayRule = null;
        sundayCheckBox.selectedProperty().removeListener(sundayListener);
        mondayCheckBox.selectedProperty().removeListener(mondayListener);
        tuesdayCheckBox.selectedProperty().removeListener(tuesdayListener);
        wednesdayCheckBox.selectedProperty().removeListener(wednesdayListener);
        thursdayCheckBox.selectedProperty().removeListener(thursdayListener);
        fridayCheckBox.selectedProperty().removeListener(fridayListener);
        saturdayCheckBox.selectedProperty().removeListener(saturdayListener);
    }

    // Setup monthlyVBox and weeklyHBox setting visibility
    switch (newSel)
    {
    case DAILY:
    case YEARLY:
        monthlyVBox.setVisible(false);
        monthlyLabel.setVisible(false);
        weeklyHBox.setVisible(false);
        weeklyLabel.setVisible(false);
        break;
    case MONTHLY:
        monthlyVBox.setVisible(true);
        monthlyLabel.setVisible(true);
        weeklyHBox.setVisible(false);
        weeklyLabel.setVisible(false);
        boolean hasByDayRule = vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY) != null;
        dayOfMonthRadioButton.selectedProperty().set(! hasByDayRule);
        dayOfWeekRadioButton.selectedProperty().set(hasByDayRule);
        dayOfWeekRadioButton.selectedProperty().addListener(dayOfWeekListener);
        // TODO - IF DAY OF THE MONTH - USE BYMONTHDAY - NOT NEEDED - JUST MONTHLY REQUIRED
        // IF DAY OF THE WEEK - USE BYDAY WITH ORDINAL
        break;
    case WEEKLY:
        monthlyVBox.setVisible(false);
        monthlyLabel.setVisible(false);
        weeklyHBox.setVisible(true);
        weeklyLabel.setVisible(true);
        byDayRule = (ByDay) vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
        sundayCheckBox.selectedProperty().addListener(sundayListener);
        mondayCheckBox.selectedProperty().addListener(mondayListener);
        tuesdayCheckBox.selectedProperty().addListener(tuesdayListener);
        wednesdayCheckBox.selectedProperty().addListener(wednesdayListener);
        thursdayCheckBox.selectedProperty().addListener(thursdayListener);
        fridayCheckBox.selectedProperty().addListener(fridayListener);
        saturdayCheckBox.selectedProperty().addListener(saturdayListener);
        break;
    case SECONDLY:
    case MINUTELY:
    case HOURLY:
        throw new InvalidParameterException("Frequency " + newSel + " not implemented");
    default:
        break;
    }
    
    if (intervalSpinner.getValue() == 1) {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringSingular());
    } else {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringPlural());
    }

};

// INITIALIZATION - runs when FXML is initialized
@FXML public void initialize()
{
    // Setup frequencyComboBox items
    frequencyComboBox.setItems(FXCollections.observableArrayList(FrequencyType.implementedValues()));
    frequencyComboBox.setConverter(Frequency.FrequencyType.stringConverter);

    // INTERVAL SPINNER
    intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1));
    intervalSpinner.valueProperty().addListener((observable, oldValue, newValue) ->
    {
        if (newValue == 1) {
            frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringSingular());
        } else {
            frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringPlural());
        }
//        makeExceptionDates();
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
            if (isNumber)
            {
                value = Integer.parseInt(s);
                vComponent.getRRule().getFrequency().setInterval(value);
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
}


/**
 * Add data that was unavailable at initialization time
 * 
 * @param rrule
 * @param dateTimeRange : date range for current agenda skin
 */
    public void setupData(
            VComponent<T> vComponent
          , LocalDateTime dateTimeStartInstanceNew)
    {
        this.vComponent = vComponent;
        this.dateTimeStartInstanceNew = dateTimeStartInstanceNew;
        
        // MAKE NEW RRULE IF NECESSARY
        RRule rRule;
        boolean checkBox = false;
        if (vComponent.getRRule() == null)
        {
            rRule = setDefaults(new RRule(), vComponent.getDateTimeStart());
            vComponent.setRRule(rRule);
        } else
        {
            checkBox = true;
            rRule = vComponent.getRRule();
        }
        
        // REPEATABLE CHECKBOX
        repeatableCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if (newSelection)
            {
                if (vComponent.getRRule() == null) vComponent.setRRule(rRule);
//                makeExceptionDates();
                setupBindings();
                repeatableGridPane.setDisable(false);
                startDatePicker.setDisable(false);
            } else
            {
                vComponent.setRRule(null);
//                appointment.setRepeat(null);
                removeBindings();
                repeatableGridPane.setDisable(true);
                startDatePicker.setDisable(true);
            }
        });

        // Initial settings
        repeatableCheckBox.selectedProperty().set(checkBox);
        frequencyComboBox.setValue(rRule.getFrequency().getFrequencyType());
        setDayOfWeek(vComponent);

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

    /** bind properties from vComponent and FXML properties */
    private void setupBindings()
    {
        frequencyComboBox.valueProperty().addListener(frequencyListener);
//        repeat.intervalProperty().bind(intervalSpinner.valueProperty());
//        sundayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SUNDAY));
//        mondayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.MONDAY));
//        tuesdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.TUESDAY));
//        wednesdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.WEDNESDAY));
//        thursdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.THURSDAY));
//        fridayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.FRIDAY));
//        saturdayCheckBox.selectedProperty().bindBidirectional(repeat.getDayOfWeekProperty(DayOfWeek.SATURDAY));
//        dayOfMonthRadioButton.selectedProperty().bindBidirectional(repeat.repeatDayOfMonthProperty());
//        dayOfWeekRadioButton.selectedProperty().bindBidirectional(repeat.repeatDayOfWeekProperty());
////        startDatePicker.valueProperty().bind(repeat.startLocalDateProperty());
//        startDatePicker.valueProperty().addListener(startDateListener);
//        repeat.countProperty().addListener(makeEndOnDateListener);
//        exceptionsListView.setItems(repeat.getExceptions());
    }

    /** unbind properties from vComponent and FXML properties */
    private void removeBindings()
    {
        frequencyComboBox.valueProperty().removeListener(frequencyListener);
    }

    /** Set day of week properties if FREQ=WEEKLY and has BYDAY byxxx rule 
     * This method is called only during setup */
    private void setDayOfWeek(VComponent<T> vComponent)
    {
        // Set day of week properties
        if (vComponent.getRRule().getFrequency().getFrequencyType() == FrequencyType.WEEKLY)
        {
            Optional<Rule> rule = vComponent.getRRule()
                    .getFrequency()
                    .getByRules()
                    .stream()
                    .filter(r -> r.getByRule() == Rule.ByRules.BYDAY)
                    .findFirst();
            if (rule.isPresent())
            {
                ByDay r = ((ByDay) rule.get());
                ((ByDay) rule.get()).getDayofWeekWithoutOrdinalList()
                        .stream()
                        .forEach(d -> 
                        {
                            switch(d)
                            {
                            case FRIDAY:
                                fridayCheckBox.selectedProperty().set(true);
                                break;
                            case MONDAY:
                                mondayCheckBox.selectedProperty().set(true);
                                break;
                            case SATURDAY:
                                saturdayCheckBox.selectedProperty().set(true);
                                break;
                            case SUNDAY:
                                sundayCheckBox.selectedProperty().set(true);
                                break;
                            case THURSDAY:
                                thursdayCheckBox.selectedProperty().set(true);
                                break;
                            case TUESDAY:
                                tuesdayCheckBox.selectedProperty().set(true);
                                break;
                            case WEDNESDAY:
                                wednesdayCheckBox.selectedProperty().set(true);
                                break;
                            default:
                                break;
                            }
                        });
    
            }
        }
    }
    
    
    /**
     * Default settings for a new RRule - weekly, repeats on day of week in dateTime
     */
    private static RRule setDefaults(RRule rRule, LocalDateTime dateTime)
    {
        rRule.setFrequency(new Weekly());
        rRule.getFrequency().addByRule(new ByDay(dateTime.getDayOfWeek()));
        return rRule;
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
    
    @FXML private void handleAddException()
    {
        LocalDateTime d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
//        makeExceptionDates();
//        exceptionComboBox.getItems().remove(d);
        Collections.sort(exceptionsListView.getItems()); // Maintain sorted list
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        System.out.println("Remove Exception");
        LocalDateTime d = exceptionsListView.getSelectionModel().getSelectedItem();
//        makeExceptionDates();
//        exceptionComboBox.getItems().add(d);
        exceptionsListView.getItems().remove(d);
        if (exceptionsListView.getSelectionModel().getSelectedItem() == null) removeExceptionButton.setDisable(true);
    }
    
}
