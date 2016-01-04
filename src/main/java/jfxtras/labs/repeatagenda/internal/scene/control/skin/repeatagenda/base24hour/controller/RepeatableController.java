package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javafx.beans.InvalidationListener;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.EXDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency.FrequencyType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Weekly;

/**
 * Makes pane for selection some repeatable rules
 * @author David Bal
 *
 * @param <T> type of recurrence instance (such as Agenda.Appointment)
 */
public class RepeatableController<T>
{

final private static int EXCEPTION_CHOICE_LIMIT = 50;
final private static int INITIAL_COUNT = 10;
final private static int INITIAL_INTERVAL = 1;
    
private VComponent<T> vComponent;
private Temporal dateTimeStartInstanceNew;

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
private final ObservableList<DayOfWeek> dayOfWeekList = FXCollections.observableArrayList();
private final Map<BooleanProperty, DayOfWeek> checkBoxDayOfWeekMap = new HashMap<>();
private Map<DayOfWeek, BooleanProperty> dayOfWeekCheckBoxMap;
@FXML private VBox monthlyVBox;
@FXML private Label monthlyLabel;
private ToggleGroup monthlyGroup;
@FXML private RadioButton dayOfMonthRadioButton;
@FXML private RadioButton dayOfWeekRadioButton;
@FXML private DatePicker startDatePicker;
@FXML private RadioButton endNeverRadioButton;
@FXML private RadioButton endAfterRadioButton;
@FXML private RadioButton untilRadioButton;
@FXML private Spinner<Integer> endAfterEventsSpinner;
@FXML private DatePicker untilDatePicker;
private ToggleGroup endGroup;
@FXML private Label repeatSummaryLabel;

@FXML ComboBox<Temporal> exceptionComboBox;
@FXML Button addExceptionButton;
@FXML ListView<Temporal> exceptionsListView; // EXDATE date/times to be skipped (deleted events)
@FXML Button removeExceptionButton;

private DateTimeFormatter getFormatter(Temporal t)
{
    if (t instanceof LocalDateTime)
    {
        return Settings.DATE_FORMAT_AGENDA_EXCEPTION;
    } else if (t instanceof LocalDate)
    {
        return Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY;
    } else throw new DateTimeException("Invalid Temporal type.  Must be LocalDate or LocalDateTime");
}

// DAY OF WEEK CHECKBOX LISTENER
private final ChangeListener<? super Boolean> dayOfWeekCheckBoxListener = (obs, oldSel, newSel) -> 
{
//    System.out.println("trigger day of week:");
    DayOfWeek dayOfWeek = checkBoxDayOfWeekMap.get(obs);
    ByDay rule = (ByDay) vComponent
            .getRRule()
            .getFrequency()
            .getByRuleByType(Rule.ByRules.BYDAY);
    if (newSel)
    {
        if (! dayOfWeekList.contains(dayOfWeek))
        {
            rule.addDayOfWeek(dayOfWeek);
            dayOfWeekList.add(dayOfWeek);
        }
    } else
    {
        if (dayOfWeekList.size() > 1)
        {
            rule.removeDayOfWeek(dayOfWeek);
            dayOfWeekList.remove(dayOfWeek);
        } else
        {// can't remove last day of week
            dayOfWeekCheckBoxMap.get(dayOfWeek).set(oldSel);
            canNotRemoveLastDayOfWeek(dayOfWeek);
        }
    }
//    refreshSummary();
//    System.out.println("make exception " + obs);
//    makeExceptionDates();
};

// Listener for dayOfWeekRadioButton when frequency if monthly
private ChangeListener<? super Boolean> dayOfWeekButtonListener = (obs2, oldSel2, newSel2) -> 
{
    if (newSel2)
    {
        Temporal start = dateTimeStartInstanceNew.with(TemporalAdjusters.firstDayOfMonth());
        int ordinalWeekNumber = 0;
        while (! VComponent.isBefore(dateTimeStartInstanceNew, start))
        {
            ordinalWeekNumber++;
            start = start.plus(1, ChronoUnit.WEEKS);
        }
        DayOfWeek dayOfWeek = DayOfWeek.from(dateTimeStartInstanceNew);
        Rule byDayRuleMonthly = new ByDay(new ByDayPair(dayOfWeek, ordinalWeekNumber));
        vComponent.getRRule().getFrequency().addByRule(byDayRuleMonthly);
    } else
    { // remove rule to reset to default behavior of repeat by day of month
        Rule r = vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
        vComponent.getRRule().getFrequency().getByRules().remove(r);
    }
    refreshSummary();
    // makeExceptionDates();
};


//MAKE EXCEPTION DATES LISTENER
final private InvalidationListener makeExceptionDatesAndSummaryListener = (obs) -> 
{
//   System.out.println("exceptions6:" + obs);
   refreshSummary();
   makeExceptionDates();
};
private void refreshSummary()
{
// System.out.println("refresh summary:");
 String summaryString = vComponent.getRRule().summary(vComponent.getDateTimeStart());
 repeatSummaryLabel.setText(summaryString);
}
private final ChangeListener<? super Boolean> neverListener = (obs, oldValue, newValue) ->
{
    if (newValue)
    {
        refreshSummary();
        System.out.println("make exception1 " + obs);
        makeExceptionDates();
    }
};

// FREQUENCY CHANGE LISTENER
private final ChangeListener<? super FrequencyType> frequencyListener = (obs, oldSel, newSel) -> 
{
    // Change Frequency if different.  Copy Interval, null ExDate
    if (vComponent.getRRule().getFrequency().frequencyType() != newSel)
    {
        Frequency newFrequency = newSel.newInstance();
        Integer interval = vComponent.getRRule().getFrequency().getInterval();
        if (interval > 1) newFrequency.setInterval(interval);
        vComponent.getRRule().setFrequency(newFrequency);
        exceptionsListView.getItems().clear();
        vComponent.setExDate(null);
    }

    // Setup monthlyVBox and weeklyHBox setting visibility
    switch (newSel)
    {
    case DAILY:
    case YEARLY:
        break;
    case MONTHLY:
        Rule r = vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
        vComponent.getRRule().getFrequency().getByRules().remove(r);
        dayOfMonthRadioButton.selectedProperty().set(true);
        dayOfWeekRadioButton.selectedProperty().set(false);
        break;
    case WEEKLY:
        Rule r2 = vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
        vComponent.getRRule().getFrequency().getByRules().remove(r2);
        if (dayOfWeekList.isEmpty())
        {
            DayOfWeek dayOfWeek = LocalDate.from(dateTimeStartInstanceNew).getDayOfWeek();
            vComponent.getRRule().getFrequency().addByRule(new ByDay(dayOfWeek)); // add days already clicked
            dayOfWeekCheckBoxMap.get(dayOfWeek).set(true);
        } else
        {
            vComponent.getRRule().getFrequency().addByRule(new ByDay(dayOfWeekList)); // add days already clicked            
        }
        break;
    case SECONDLY:
    case MINUTELY:
    case HOURLY:
        throw new IllegalArgumentException("Frequency " + newSel + " not implemented");
    default:
        break;
    }
    
    // visibility of monthlyVBox & weeklyHBox
    setFrequencyVisibility(newSel);
    
    if (intervalSpinner.getValue() == 1) {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringSingular());
    } else {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringPlural());
    }
    
    // Make summary and exceptions
    refreshSummary();
//    System.out.println("make exception2" + obs + " " + oldSel);
    makeExceptionDates();
};

private void setFrequencyVisibility(FrequencyType f)
{
    // Setup monthlyVBox and weeklyHBox setting visibility
    switch (f)
    {
    case DAILY:
    case YEARLY:
    {
        monthlyVBox.setVisible(false);
        monthlyLabel.setVisible(false);
        weeklyHBox.setVisible(false);
        weeklyLabel.setVisible(false);
        break;
    }
    case MONTHLY:
        monthlyVBox.setVisible(true);
        monthlyLabel.setVisible(true);
        weeklyHBox.setVisible(false);
        weeklyLabel.setVisible(false);
        break;
    case WEEKLY:
    {
        monthlyVBox.setVisible(false);
        monthlyLabel.setVisible(false);
        weeklyHBox.setVisible(true);
        weeklyLabel.setVisible(true);
        break;
    }
    case SECONDLY:
    case MINUTELY:
    case HOURLY:
        throw new IllegalArgumentException("Frequency " + f + " not implemented");
    default:
        break;
    }
}

private final ChangeListener<? super Integer> intervalSpinnerListener = (observable, oldValue, newValue) ->
{
    if (newValue == 1) {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringSingular());
    } else {
        frequencyLabel.setText(frequencyComboBox.valueProperty().get().toStringPlural());
    }
    vComponent.getRRule().getFrequency().setInterval(newValue);
    System.out.println("interval:" + newValue);
    refreshSummary();
    makeExceptionDates();
};

//END ON LISTENERS
private final ChangeListener<? super LocalDate> untilListener = (observable, oldSelection, newSelection) ->
{
    if (newSelection.isBefore(LocalDate.from(vComponent.getDateTimeStart())))
    {
        tooEarlyDateAlert();
        untilDatePicker.setValue(oldSelection);
    } else
    {
        Temporal lastTemporal = findUntil(newSelection);
        if (! LocalDate.from(lastTemporal).equals(newSelection)) notOccurrenceDateAlert(lastTemporal);
        vComponent.getRRule().setUntil(lastTemporal);
        refreshSummary();
//     System.out.println("make exception5 " + observable);
        makeExceptionDates();
    }
};

// Finds last occurrence on or before d
// Ensures the Until date is an actual occurrence date
private Temporal findUntil(Temporal d)
{
    final Temporal timeAdjustedSelection;
    if (vComponent.getDateTimeStart() instanceof LocalDateTime)
    {
        LocalTime time = LocalTime.from(vComponent.getDateTimeStart());
        timeAdjustedSelection = LocalDate.from(d).atTime(time);
    } else
    {
        timeAdjustedSelection = d;
    }
    vComponent.getRRule().setUntil(timeAdjustedSelection);
    Iterator<Temporal> iterator = vComponent.getRRule().stream(dateTimeStartInstanceNew).iterator();
    Temporal lastTemporal = null;
    while (iterator.hasNext()) { lastTemporal = iterator.next(); }
    untilDatePicker.valueProperty().removeListener(untilListener);
    untilDatePicker.setValue(LocalDate.from(lastTemporal));
    untilDatePicker.valueProperty().addListener(untilListener);
    return lastTemporal;
}

// listen for changes to start date/time (type may change requiring new exception date choices)
private final ChangeListener<? super Temporal> dateTimeStartToExceptionChangeListener = (obs, oldValue, newValue) ->
{
//    System.out.println("make exception3 " + obs);

    makeExceptionDates();
    // update existing exceptions
    if (! exceptionsListView.getItems().isEmpty())
    {
        List<Temporal> newItems = null;
        if (newValue.getClass().equals(LocalDate.class))
        {
            newItems = exceptionsListView.getItems()
                    .stream()
                    .map(d -> LocalDate.from(d))
                    .collect(Collectors.toList());
        } else if (newValue.getClass().equals(LocalDateTime.class))
        {
            LocalTime time = LocalDateTime.from(newValue).toLocalTime();
            newItems = exceptionsListView.getItems()
                    .stream()
                    .map(d -> LocalDate.from(d).atTime(time))
                    .collect(Collectors.toList());            
        }
        exceptionsListView.setItems(FXCollections.observableArrayList(newItems));
    }
};

// INITIALIZATION - runs when FXML is initialized
@FXML public void initialize()
{
    // REPEATABLE CHECKBOX
    repeatableCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection)
        {
            removeListeners();
            if (vComponent.getRRule() == null)
            {
                // setup new default RRule
                RRule rRule = new RRule()
                        .withFrequency(new Weekly()
                        .withByRules(new ByDay(DayOfWeek.from(dateTimeStartInstanceNew)))); // default RRule
                vComponent.setRRule(rRule);
                setInitialValues(vComponent);
            }
            vComponent.dateTimeStartProperty().addListener(dateTimeStartToExceptionChangeListener);
            repeatableGridPane.setDisable(false);
            startDatePicker.setDisable(false);
            addListeners();
        } else
        {
            vComponent.dateTimeStartProperty().removeListener(dateTimeStartToExceptionChangeListener);
            vComponent.setRRule(null);
            repeatableGridPane.setDisable(true);
            startDatePicker.setDisable(true);
        }
    });
    
    // DAY OF WEEK CHECK BOX LISTENERS (FOR WEEKLY)
    checkBoxDayOfWeekMap.put(sundayCheckBox.selectedProperty(), DayOfWeek.SUNDAY);
    checkBoxDayOfWeekMap.put(mondayCheckBox.selectedProperty(), DayOfWeek.MONDAY);
    checkBoxDayOfWeekMap.put(tuesdayCheckBox.selectedProperty(), DayOfWeek.TUESDAY);
    checkBoxDayOfWeekMap.put(wednesdayCheckBox.selectedProperty(), DayOfWeek.WEDNESDAY);
    checkBoxDayOfWeekMap.put(thursdayCheckBox.selectedProperty(), DayOfWeek.THURSDAY);
    checkBoxDayOfWeekMap.put(fridayCheckBox.selectedProperty(), DayOfWeek.FRIDAY);
    checkBoxDayOfWeekMap.put(saturdayCheckBox.selectedProperty(), DayOfWeek.SATURDAY);
    dayOfWeekCheckBoxMap = checkBoxDayOfWeekMap.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> e.getKey()));
    checkBoxDayOfWeekMap.entrySet().stream().forEach(entry -> entry.getKey().addListener(dayOfWeekCheckBoxListener));

    // DAY OF WEEK RAIO BUTTON LISTENER (FOR MONTHLY)
    dayOfWeekRadioButton.selectedProperty().addListener(dayOfWeekButtonListener);

    // Setup frequencyComboBox items
    frequencyComboBox.setItems(FXCollections.observableArrayList(FrequencyType.implementedValues()));
    frequencyComboBox.setConverter(Frequency.FrequencyType.stringConverter);

    // INTERVAL SPINNER
    // Make frequencySpinner and only accept numbers (needs below two listeners)
    intervalSpinner.setEditable(true);
    intervalSpinner.getEditor().addEventHandler(KeyEvent.KEY_PRESSED, (event)  ->
    {
        if (event.getCode() == KeyCode.ENTER)
        {
            String s = intervalSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (! isNumber)
            {
                System.out.println("new interval number:");
                String lastValue = intervalSpinner.getValue().toString();
                intervalSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert();
            }
        }
    });
    intervalSpinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) ->
    {
        if (! isNowFocused)
        {
            int value;
            String s = intervalSpinner.getEditor().textProperty().get();
            boolean isNumber = s.matches("[0-9]+");
            if (isNumber)
            {
                value = Integer.parseInt(s);
                System.out.println("new interval number2:");
                vComponent.getRRule().getFrequency().setInterval(value);
                refreshSummary();
                makeExceptionDates();
            } else {
                String lastValue = intervalSpinner.getValue().toString();
                intervalSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert();
            }
        }
    });
    
    startDatePicker.valueProperty().addListener((obs, oldValue, newValue) ->
    {
        if (vComponent.getDateTimeStart() instanceof LocalDate)
        {
            vComponent.setDateTimeStart(newValue);
        } else if (vComponent.getDateTimeStart() instanceof LocalDateTime)
        {
            LocalTime time = LocalTime.from(vComponent.getDateTimeStart());
            vComponent.setDateTimeStart(newValue.atTime(time));
        } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
    });
    
    // END AFTER LISTENERS
    endAfterEventsSpinner.valueProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (endAfterRadioButton.isSelected())
        {
              vComponent.getRRule().setCount(newSelection);
              refreshSummary();
               makeExceptionDates();
        }
        if (newSelection == 1) {
            eventLabel.setText(resources.getString("event"));
        } else {
            eventLabel.setText(resources.getString("events"));
        }
    });
    
    endAfterRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection)
        {
            endAfterEventsSpinner.setDisable(false);
            eventLabel.setDisable(false);
            vComponent.getRRule().setCount(endAfterEventsSpinner.getValue());
            refreshSummary();
            makeExceptionDates();
        } else
        {
            vComponent.getRRule().setCount(0);
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
                notNumberAlert();
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
                vComponent.getRRule().setCount(value);
            } else {
                String lastValue = endAfterEventsSpinner.getValue().toString();
                endAfterEventsSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert();
            }
        }
    });
    
    untilDatePicker.valueProperty().addListener(untilListener);
    untilRadioButton.selectedProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (newSelection)
        {
            if (vComponent.getRRule().getUntil() == null)
            { // new selection - use date/time one month in the future as default
                boolean isAfter = VComponent.isAfter(dateTimeStartInstanceNew, vComponent.getDateTimeStart().plus(1, ChronoUnit.MONTHS));
                Temporal defaultEndOnDateTime = isAfter ? dateTimeStartInstanceNew :
                    vComponent.getDateTimeStart().plus(1, ChronoUnit.MONTHS);
                Temporal matchingOccurrence = findUntil(defaultEndOnDateTime); // adjust to actual occurrence
                vComponent.getRRule().setUntil(matchingOccurrence);
            }
            untilDatePicker.setValue(LocalDate.from(vComponent.getRRule().getUntil()));
            untilDatePicker.setDisable(false);
            untilDatePicker.show();
        } else {
            vComponent.getRRule().setUntil(null);
            untilDatePicker.setDisable(true);
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
    untilRadioButton.setToggleGroup(endGroup);
}

/**
 * Add data that was unavailable at initialization time
 * 
 * @param vComponent
 * @param dateTimeStartInstanceNew : start date-time for edited event
 */
    public void setupData(
            VComponent<T> vComponent
          , Temporal dateTimeStartInstanceNew)
    {
        this.vComponent = vComponent;
        this.dateTimeStartInstanceNew = dateTimeStartInstanceNew;

        // EXCEPTIONS
        // Note: exceptionComboBox string converter must be setup done after the controller's initialization 
        // because the resource bundle isn't instantiated earlier.
        exceptionComboBox.setConverter(new StringConverter<Temporal>()
        { // setup string converter
            @Override public String toString(Temporal temporal)
            {
                DateTimeFormatter myFormatter = getFormatter(temporal);
                return myFormatter.format(temporal);
            }
            @Override public Temporal fromString(String string) { throw new RuntimeException("not required for non editable ComboBox"); }
        });
        exceptionComboBox.valueProperty().addListener(obs -> addExceptionButton.setDisable(false)); // turn on add button when exception date is selected in combobox
                
        exceptionsListView.getSelectionModel().selectedItemProperty().addListener(obs ->
        {
          removeExceptionButton.setDisable(false); // turn on add button when exception date is selected in combobox
        });
        
        // Format Temporal in exceptionsListView to LocalDate or LocalDateTime
        final Callback<ListView<Temporal>, ListCell<Temporal>> temporalCellFactory = new Callback<ListView<Temporal>,  ListCell<Temporal>>()
        {
            @Override 
            public ListCell<Temporal> call(ListView<Temporal> list)
            {
                return new ListCell<Temporal>()
                {
                    @Override public void updateItem(Temporal temporal, boolean empty)
                    {
                        super.updateItem(temporal, empty);
                        if (temporal == null || empty)
                        {
                            setText(null);
                            setStyle("");
                        } else
                        {
                            // Format date.
                            DateTimeFormatter myFormatter = getFormatter(temporal);
                            setText(myFormatter.format(temporal));
                        }
                    }
                };
            }
        };
        exceptionsListView.setCellFactory(temporalCellFactory);
                
        // SETUP CONTROLLER'S INITIAL DATA FROM RRULE
        boolean isRepeatable = (vComponent.getRRule() != null);
        if (isRepeatable)
        {
            System.out.println("setup previous rrule" + vComponent);
            setInitialValues(vComponent);
            if (vComponent.getExDate() != null) // add existing ExDate values to exceptionsListView (WHY HERE AND NOT IN SETINITIAL VALUES?)
            {
                List<Temporal> collect = vComponent
                        .getExDate()
                        .getTemporals()
                        .stream()
                        .collect(Collectors.toList());
                exceptionsListView.getItems().addAll(collect);
            }
        }
        repeatableCheckBox.selectedProperty().set(isRepeatable);

        // LISTENERS TO BE ADDED AFTER INITIALIZATION
        addListeners(); // Listeners to update exception dates
        frequencyComboBox.valueProperty().addListener(frequencyListener);
    }
    
    private void addListeners()
    {
        endNeverRadioButton.selectedProperty().addListener(neverListener);
        intervalSpinner.valueProperty().addListener(intervalSpinnerListener);
        dayOfWeekList.addListener(makeExceptionDatesAndSummaryListener);
    }
    
    private void removeListeners()
    {
        endNeverRadioButton.selectedProperty().removeListener(neverListener);
        intervalSpinner.valueProperty().removeListener(intervalSpinnerListener);
        dayOfWeekList.removeListener(makeExceptionDatesAndSummaryListener);
    }
    
    /* Set controls to values in rRule */
    private void setInitialValues(VComponent<T> vComponent)
    {
        int initialInterval = (vComponent.getRRule().getFrequency().getInterval() > 0) ?
                vComponent.getRRule().getFrequency().getInterval() : INITIAL_INTERVAL;
        intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialInterval));
        FrequencyType frequencyType = vComponent.getRRule().getFrequency().frequencyType();
        frequencyComboBox.setValue(frequencyType); // will trigger frequencyListener
        switch(frequencyType)
        {
        case MONTHLY:
            ByDay rule = (ByDay) vComponent.getRRule().getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
            if (rule == null)
            {
                dayOfMonthRadioButton.selectedProperty().set(true);
            } else
            {
                dayOfWeekRadioButton.selectedProperty().set(true);
            }
            break;
        case WEEKLY:
            setDayOfWeek(vComponent.getRRule());
            break;
        default:
            break;
        }
        setFrequencyVisibility(frequencyType);
        
//        // ExDates
//        if (vComponent.getExDate() != null)
//        {
//            List<Temporal> collect = vComponent
//                    .getExDate()
//                    .getTemporals()
//                    .stream()
//                    .collect(Collectors.toList());
//            exceptionsListView.getItems().addAll(collect);
//        }
        
        int initialCount = (vComponent.getRRule().getCount() > 0) ? vComponent.getRRule().getCount() : INITIAL_COUNT;
        endAfterEventsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 1000, initialCount));
        if (vComponent.getRRule().getCount() > 0)
        {
            endAfterRadioButton.selectedProperty().set(true);
        } else if (vComponent.getRRule().getUntil() != null)
        {
            untilRadioButton.selectedProperty().set(true);
        } else
        {
            endNeverRadioButton.selectedProperty().set(true);
        }
        
        startDatePicker.setValue(LocalDate.from(vComponent.getDateTimeStart()));
        makeExceptionDates(); // Should this be here - TODO - CHECK # OF CALLS
    }

    /** Set day of week properties if FREQ=WEEKLY and has BYDAY rule 
     * This method is called only during setup */
    private void setDayOfWeek(RRule rRule)
    {
        // Set day of week properties
        if (rRule.getFrequency().frequencyType() == FrequencyType.WEEKLY)
        {
            Rule rule = rRule.getFrequency().getByRuleByType(Rule.ByRules.BYDAY);
            ((ByDay) rule).dayOfWeekWithoutOrdinalList()
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
    
    /** Make list of start date/times for exceptionComboBox */
    private void makeExceptionDates()
    {
//        System.out.println("make exception date list" + vComponent.getDateTimeStart().getClass().getSimpleName());
        
        final Temporal dateTimeStart = vComponent.getDateTimeStart();
        Stream<Temporal> stream1 = vComponent
//                .getRRule()
                .stream(dateTimeStart);
        Stream<Temporal> stream2 = (vComponent.getExDate() == null) ? stream1
                : vComponent.getExDate().stream(stream1, dateTimeStart); // remove exceptions
        Class<? extends Temporal> clazz = vComponent.getDateTimeStart().getClass();
        List<Temporal> exceptionDates = null;
        if (clazz.equals(LocalDate.class))
        {
            exceptionDates = stream2
                    .limit(EXCEPTION_CHOICE_LIMIT)
//                    .map(d -> d.toLocalDate())
                    .collect(Collectors.toList());
        } else if (clazz.equals(LocalDateTime.class))
        {
            exceptionDates = stream2
                    .limit(EXCEPTION_CHOICE_LIMIT)
                    .collect(Collectors.toList());
        } else throw new DateTimeException("Invalid Temporal class: " +
                clazz.getSimpleName() + " Only LocalDate and LocalDateTime accepted.");

        exceptionComboBox.getItems().clear();
        exceptionComboBox.getItems().addAll(exceptionDates);
    }
    
    @FXML private void handleAddException()
    {
        Temporal d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
        if (vComponent.getExDate() == null) vComponent.setExDate(new EXDate());
        vComponent.getExDate().getTemporals().add(d);
        makeExceptionDates();
        Collections.sort(exceptionsListView.getItems(),VComponent.TEMPORAL_COMPARATOR); // Maintain sorted list
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        Temporal d = exceptionsListView.getSelectionModel().getSelectedItem();
        vComponent.getExDate().getTemporals().remove(d);
        makeExceptionDates();
        exceptionsListView.getItems().remove(d);
        if (exceptionsListView.getSelectionModel().getSelectedItem() == null) removeExceptionButton.setDisable(true);
    }
    
    
    // Displays an alert notifying user number input is not valid
    private static void notNumberAlert()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Number");
        alert.setHeaderText("Please enter valid numbers.");
        alert.setContentText("Must greater than or equal to 1");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    // Displays an alert notifying UNTIL date is too early
    private void tooEarlyDateAlert()
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("Event can't end before it begins.");
        TemporalAccessor d = vComponent.getDateTimeStart();
        alert.setContentText("Must be after " + Settings.DATE_FORMAT_AGENDA_DATEONLY.format(d));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private void notOccurrenceDateAlert(Temporal temporal)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("Not an occurrence date");
        alert.setContentText("Date has been changed to  " + Settings.DATE_FORMAT_AGENDA_DATEONLY.format(temporal));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    // Displays an alert notifying last day of week can not be removed for weekly frequency
    private void canNotRemoveLastDayOfWeek(DayOfWeek d)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Can't remove " + Settings.DAYS_OF_WEEK_MAP.get(d) + ".");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("last_day_of_week_alert");
        List<Node> buttons = ICalendarUtilities.getAllNodes(alert.getDialogPane(), Button.class);
        ((Button) buttons.get(0)).setId("last_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
    
}
