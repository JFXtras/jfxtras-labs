package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.InvalidationListener;
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
import javafx.util.StringConverter;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.EXDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VDateTime;
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
private ToggleGroup monthlyGroup;
@FXML private RadioButton dayOfMonthRadioButton;
@FXML private RadioButton dayOfWeekRadioButton;
@FXML private DatePicker startDatePicker;
@FXML private RadioButton endNeverRadioButton;
@FXML private RadioButton endAfterRadioButton;
@FXML private RadioButton endOnRadioButton;
@FXML private Spinner<Integer> endAfterEventsSpinner;
@FXML private DatePicker endOnDatePicker;
private ToggleGroup endGroup;
@FXML private Label repeatSummaryLabel;

@FXML ComboBox<VDateTime> exceptionComboBox;
@FXML Button addExceptionButton;
@FXML ListView<VDateTime> exceptionsListView; // EXDATE date/times to be skipped (deleted events)
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
        endNeverRadioButton.selectedProperty().set(true);
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
        break;
    case WEEKLY:
        System.out.println("triggered weekly:");
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
        throw new IllegalArgumentException("Frequency " + newSel + " not implemented");
    default:
        break;
    }
    
    if (intervalSpinner.getValue() == 1) {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringSingular());
    } else {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringPlural());
    }
};

// MAKE EXCEPTION DATES LISTENER
final private InvalidationListener makeExceptionDatesListener = (obs) -> makeExceptionDates();

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
    
    // END AFTER LISTENERS
    endAfterEventsSpinner.valueProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (! vComponent.getRRule().countProperty().isBound()) {
            endAfterEventsSpinner.getValueFactory().setValue(vComponent.getRRule().getCount());
            vComponent.getRRule().countProperty().bind(endAfterEventsSpinner.valueProperty());   
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
            endAfterEventsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, vComponent.getRRule().getCount()));
            vComponent.getRRule().countProperty().bind(endAfterEventsSpinner.valueProperty());
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
                vComponent.getRRule().countProperty().unbind();
                vComponent.getRRule().setCount(value);
            } else {
                String lastValue = endAfterEventsSpinner.getValue().toString();
                endAfterEventsSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert("123");
            }
        }
    });
    
    // END ON LISTENERS
    final ChangeListener<? super LocalDate> endOnDateListener = ((observable, oldSelection, newSelection) ->
    {
        LocalTime endTime = dateTimeStartInstanceNew.toLocalTime();
        vComponent.getRRule().setUntil(newSelection.atTime(endTime));
    });
    endOnRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection)
        {
            if (vComponent.getRRule().getUntil() == null)
            { // new selection - use date/time one month in the future as default
                LocalTime time = vComponent.getDateTimeStart().getLocalDateTime().toLocalTime();
                LocalDateTime defaultEndOnDateTime = (dateTimeStartInstanceNew.equals(vComponent.getDateTimeStart())) ?
                        vComponent.getDateTimeStart().getLocalDate().plusMonths(1).atTime(time)
                      : dateTimeStartInstanceNew;
                vComponent.getRRule().setUntil(defaultEndOnDateTime);
            }
            endOnDatePicker.setValue(vComponent.getRRule().getUntil().toLocalDate());
            endOnDatePicker.setDisable(false);
            endOnDatePicker.valueProperty().addListener(endOnDateListener);
            endOnDatePicker.show();
        } else {
            endOnDatePicker.setDisable(true);
            endOnDatePicker.valueProperty().removeListener(endOnDateListener);
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

    // Monthly ToggleGroup
    monthlyGroup = new ToggleGroup();
    dayOfMonthRadioButton.setToggleGroup(monthlyGroup);
    dayOfWeekRadioButton.setToggleGroup(monthlyGroup);
    
    // End criteria ToggleGroup
    endGroup = new ToggleGroup();
    endNeverRadioButton.setToggleGroup(endGroup);
    endAfterRadioButton.setToggleGroup(endGroup);
    endOnRadioButton.setToggleGroup(endGroup);
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
            rRule = setDefaults(new RRule(), vComponent.getDateTimeStart().getLocalDateTime());
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
                frequencyComboBox.valueProperty().addListener(frequencyListener);
                repeatableGridPane.setDisable(false);
                startDatePicker.setDisable(false);
            } else
            {
                vComponent.setRRule(null);
//                appointment.setRepeat(null);
                frequencyComboBox.valueProperty().removeListener(frequencyListener);
                repeatableGridPane.setDisable(true);
                startDatePicker.setDisable(true);
            }
        });

        // EXCEPTIONS
        // Note: exceptionComboBox string converter must be setup done after the controller's initialization 
        // because the resource bundle isn't instantiated earlier.
        final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.exception"));
        final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern(resources.getString("date.format.agenda.exception"));
        exceptionComboBox.setConverter(new StringConverter<VDateTime>()
        { // setup string converter
            @Override public String toString(VDateTime d)
            {
                DateTimeFormatter myFormatter = (d.isWholeDay()) ? formatterDate : formatterDateTime;
                return myFormatter.format(d.getLocalDateTime());
            }
            @Override public VDateTime fromString(String string) { throw new RuntimeException("not required for non editable ComboBox"); }
        });
        exceptionComboBox.valueProperty().addListener(obs -> addExceptionButton.setDisable(false)); // turn on add button when exception date is selected in combobox
                
        exceptionsListView.getSelectionModel().selectedItemProperty().addListener(obs ->
        {
          removeExceptionButton.setDisable(false); // turn on add button when exception date is selected in combobox
        });
        
        // SETUP CONTROLLER'S DATA FROM VCOMPONENT
        repeatableCheckBox.selectedProperty().set(checkBox);
        frequencyComboBox.setValue(rRule.getFrequency().getFrequencyType());
        setDayOfWeek(vComponent);
        startDatePicker.setValue(vComponent.getDateTimeStart().getLocalDateTime().toLocalDate());
        if (vComponent.getExDate() != null) {
            List<VDateTime> collect = vComponent
                    .getExDate()
                    .getDates()
                    .stream()
//                    .map(d -> d.getLocalDateTime())
                    .collect(Collectors.toList());
            exceptionsListView.getItems().addAll(collect);
        }
        if (vComponent.getRRule().getCount() > 0)
        {
            endAfterRadioButton.selectedProperty().set(true);
        } else if (vComponent.getRRule().getUntil() != null)
        {
            endOnRadioButton.selectedProperty().set(true);
        } else
        {
            endNeverRadioButton.selectedProperty().set(true);
        }

        // Listeners to update exception dates
//        vComponent.getRRule().countProperty().addListener(makeExceptionDatesListener);
//        vComponent.getRRule().frequencyProperty().addListener(makeExceptionDatesListener);
//        vComponent.getRRule().untilProperty().addListener(makeExceptionDatesListener);
        frequencyComboBox.valueProperty().addListener(makeExceptionDatesListener);
        intervalSpinner.valueProperty().addListener(makeExceptionDatesListener);
        sundayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        mondayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        tuesdayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        wednesdayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        thursdayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        fridayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        saturdayCheckBox.selectedProperty().addListener(makeExceptionDatesListener);
        monthlyGroup.selectedToggleProperty().addListener(makeExceptionDatesListener);
//        dayOfMonthRadioButton.selectedProperty().addListener(makeExceptionDatesListener);
//        dayOfWeekRadioButton.selectedProperty().addListener(makeExceptionDatesListener);
        endGroup.selectedToggleProperty().addListener(makeExceptionDatesListener);
//        endNeverRadioButton.selectedProperty().addListener(makeExceptionDatesListener);
//        endAfterRadioButton.selectedProperty().addListener(makeExceptionDatesListener);
//        endOnRadioButton.selectedProperty().addListener(makeExceptionDatesListener);
        endAfterEventsSpinner.valueProperty().addListener(makeExceptionDatesListener);
        endOnDatePicker.valueProperty().addListener(makeExceptionDatesListener);
        
    }
    
        
    private void setEndGroup(EndCriteria endCriteria) {
        switch (endCriteria) {
        case NEVER:
            endGroup.selectToggle(endNeverRadioButton);
            break;
        case COUNT:
            endGroup.selectToggle(endAfterRadioButton);
            break;
        case UNTIL:
            endGroup.selectToggle(endOnRadioButton);
            break;
        default:
            break;
        }
    }

//    /** bind properties from vComponent and FXML properties */
//    private void setupBindings()
//    {
//        frequencyComboBox.valueProperty().addListener(frequencyListener);
//    }
//
//    /** unbind properties from vComponent and FXML properties */
//    private void removeBindings()
//    {
//        frequencyComboBox.valueProperty().removeListener(frequencyListener);
//    }

    /** Set day of week properties if FREQ=WEEKLY and has BYDAY rule 
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
    private RRule setDefaults(RRule rRule, LocalDateTime dateTime)
    {
        rRule.setFrequency(new Weekly());
        rRule.getFrequency().addByRule(new ByDay(dateTime.getDayOfWeek()));
        monthlyVBox.setVisible(false);
        monthlyLabel.setVisible(false);
        return rRule;
    }
    
    /** Make list of start date/times for exceptionComboBox */
    private void makeExceptionDates()
    {
        System.out.println("make exception date list");
        
        Stream<LocalDateTime> stream1 = vComponent
                .getRRule()
                .stream(vComponent.getDateTimeStart().getLocalDateTime());
        Stream<LocalDateTime> stream2 = (vComponent.getExDate() == null) ? stream1
                : vComponent.getExDate().stream(stream1, vComponent.getDateTimeStart().getLocalDateTime()); // remove exceptions
        List<VDateTime> exceptionDates = stream2
                .limit(EXCEPTION_CHOICE_LIMIT)
                .map(d -> new VDateTime(d))
                .collect(Collectors.toList());
        exceptionComboBox.getItems().clear();
        exceptionComboBox.getItems().addAll(exceptionDates);
    }
    
    @FXML private void handleAddException()
    {
        VDateTime d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
        if (vComponent.getExDate() == null) vComponent.setExDate(new EXDate());
        vComponent.getExDate().getDates().add(new VDateTime(d));
        makeExceptionDates();
//        exceptionComboBox.getItems().remove(d);
        Collections.sort(exceptionsListView.getItems()); // Maintain sorted list
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        System.out.println("Remove Exception");
        VDateTime d = exceptionsListView.getSelectionModel().getSelectedItem();
        vComponent.getExDate().getDates().remove(d);
        makeExceptionDates();
//        exceptionComboBox.getItems().add(d);
        exceptionsListView.getItems().remove(d);
        if (exceptionsListView.getSelectionModel().getSelectedItem() == null) removeExceptionButton.setDisable(true);
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
    
    // Designates how a repeat rule can end.  
    private enum EndCriteria
    {
        NEVER
      , COUNT
      , UNTIL;
    }
}
