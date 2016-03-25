package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller;


import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
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
import javafx.geometry.Orientation;
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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.components.VComponentDisplayableOld;
import jfxtras.labs.icalendar.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.RecurrenceRule;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByRule;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByRuleEnum;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Frequency;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.Weekly;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.FrequencyUtilities.FrequencyEnum;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;

/**
 * Makes pane for selection some repeatable rules
 * @author David Bal
 *
 * @param <T> type of recurrence instance (such as Agenda.Appointment)
 */
public class RepeatableController<T>
{

final public static int EXCEPTION_CHOICE_LIMIT = 50; // TODO - EXTEND WHEN BOTTOM OF LIST IS REACHED
final public static int INITIAL_COUNT = 10;
final public static Period DEFAULT_UNTIL_PERIOD = Period.ofMonths(1); // amount of time beyond start default for UNTIL (ends on) 
final private static int INITIAL_INTERVAL = 1;
    
private VComponentDisplayableOld<T> vComponent;
private Temporal dateTimeStartInstanceNew;

@FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader

@FXML private CheckBox repeatableCheckBox;
@FXML private GridPane repeatableGridPane;
@FXML private ComboBox<FrequencyEnum> frequencyComboBox;
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

@FXML private ComboBox<Temporal> exceptionComboBox;
@FXML private Button addExceptionButton;
@FXML private ListView<Temporal> exceptionsListView; // EXDATE date/times to be skipped (deleted events)
@FXML private Button removeExceptionButton;
private Temporal exceptionFirstTemporal;
private List<Temporal> exceptionMasterList = new ArrayList<>();

private DateTimeFormatter getFormatter(Temporal t)
{
    return t.isSupported(ChronoUnit.NANOS) ? Settings.DATE_FORMAT_AGENDA_EXCEPTION : Settings.DATE_FORMAT_AGENDA_EXCEPTION_DATEONLY;
}

// DAY OF WEEK CHECKBOX LISTENER
private final ChangeListener<? super Boolean> dayOfWeekCheckBoxListener = (obs, oldSel, newSel) -> 
{
//    System.out.println("trigger day of week:");
    DayOfWeek dayOfWeek = checkBoxDayOfWeekMap.get(obs);
    ByDay rule = (ByDay) vComponent
            .getRRule()
            .getFrequency()
            .lookupByRule(ByRuleEnum.BY_DAY);
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
};

// Listener for dayOfWeekRadioButton when frequency if monthly
private ChangeListener<? super Boolean> dayOfWeekButtonListener = (observable, oldSelection, newSelection) -> 
{
    if (newSelection)
    {
        int ordinalWeekNumber = DateTimeUtilities.weekOrdinalInMonth(dateTimeStartInstanceNew);
        DayOfWeek dayOfWeek = DayOfWeek.from(dateTimeStartInstanceNew);
        ByRule byDayRuleMonthly = new ByDay(new ByDayPair(dayOfWeek, ordinalWeekNumber));
        vComponent.getRRule().getFrequency().byRules().add(byDayRuleMonthly);
    } else
    { // remove rule to reset to default behavior of repeat by day of month
        ByRule r = vComponent.getRRule().getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
        vComponent.getRRule().getFrequency().byRules().remove(r);
    }
    refreshSummary();
    refreshExceptionDates();
};


//MAKE EXCEPTION DATES LISTENER
final private InvalidationListener makeExceptionDatesAndSummaryListener = (obs) -> 
{
   refreshSummary();
   refreshExceptionDates();
};
private void refreshSummary()
{
   String summaryString = makeSummary(vComponent.getRRule(), vComponent.getDateTimeStart());
   repeatSummaryLabel.setText(summaryString);
}
private final ChangeListener<? super Boolean> neverListener = (obs, oldValue, newValue) ->
{
    if (newValue)
    {
        refreshSummary();
        refreshExceptionDates();
    }
};

// FREQUENCY CHANGE LISTENER
private final ChangeListener<? super FrequencyEnum> frequencyListener = (obs, oldSel, newSel) -> 
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
        ByRule r = vComponent.getRRule().getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
        vComponent.getRRule().getFrequency().byRules().remove(r);
        dayOfMonthRadioButton.selectedProperty().set(true);
        dayOfWeekRadioButton.selectedProperty().set(false);
        break;
    case WEEKLY:
        ByRule r2 = vComponent.getRRule().getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
        vComponent.getRRule().getFrequency().byRules().remove(r2);
        if (dayOfWeekList.isEmpty())
        {
            DayOfWeek dayOfWeek = LocalDate.from(dateTimeStartInstanceNew).getDayOfWeek();
            vComponent.getRRule().getFrequency().byRules().add(new ByDay(dayOfWeek)); // add days already clicked
            dayOfWeekCheckBoxMap.get(dayOfWeek).set(true);
        } else
        {
            vComponent.getRRule().getFrequency().byRules().add(new ByDay(dayOfWeekList)); // add days already clicked            
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

    final String frequencyLabelText;
    if (intervalSpinner.getValue() == 1)
    {
        frequencyLabelText = Settings.REPEAT_FREQUENCIES_SINGULAR.get(frequencyComboBox.valueProperty().get());
    } else
    {
        frequencyLabelText = Settings.REPEAT_FREQUENCIES_PLURAL.get(frequencyComboBox.valueProperty().get());
    }
    frequencyLabel.setText(frequencyLabelText);
    
    // Make summary and exceptions
    refreshSummary();
    refreshExceptionDates();
};

private void setFrequencyVisibility(FrequencyEnum f)
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
    final String frequencyLabelText;
    if (intervalSpinner.getValue() == 1)
    {
        frequencyLabelText = Settings.REPEAT_FREQUENCIES_SINGULAR.get(frequencyComboBox.valueProperty().get());
    } else
    {
        frequencyLabelText = Settings.REPEAT_FREQUENCIES_PLURAL.get(frequencyComboBox.valueProperty().get());
    }
    frequencyLabel.setText(frequencyLabelText);
    vComponent.getRRule().getFrequency().setInterval(newValue);
    refreshSummary();
    refreshExceptionDates();
};

private final ChangeListener<? super LocalDate> untilListener = (observable, oldSelection, newSelection) ->
{
    if (newSelection.isBefore(LocalDate.from(vComponent.getDateTimeStart())))
    {
        tooEarlyDateAlert(vComponent.getDateTimeStart());
        untilDatePicker.setValue(oldSelection);
    } else
    {
        Temporal until = findUntil(newSelection);
        if (! LocalDate.from(until).equals(newSelection))
        {
            notOccurrenceDateAlert(until);
            untilDatePicker.setValue(LocalDate.from(until));
        }
        vComponent.getRRule().setUntil(until);
        refreshSummary();
        refreshExceptionDates();
    }
};

private final ChangeListener<? super Boolean> untilRadioButtonListener = (observable, oldSelection, newSelection) ->
{
    if (newSelection)
    {
        if (vComponent.getRRule().getUntil() == null)
        { // new selection - use date/time one month in the future as default
            boolean isInstanceFarEnoughInFuture = DateTimeUtilities.isAfter(dateTimeStartInstanceNew, vComponent.getDateTimeStart().plus(DEFAULT_UNTIL_PERIOD));
            LocalDate defaultEndOnDateTime = (isInstanceFarEnoughInFuture) ? LocalDate.from(dateTimeStartInstanceNew) : LocalDate.from(vComponent.getDateTimeStart().plus(DEFAULT_UNTIL_PERIOD));
            Temporal until = findUntil(defaultEndOnDateTime); // adjust to actual occurrence
            vComponent.getRRule().setUntil(until);
        }
        untilDatePicker.setValue(LocalDate.from(vComponent.getRRule().getUntil()));
        untilDatePicker.setDisable(false);
        untilDatePicker.show();
        refreshExceptionDates();
    } else {
        vComponent.getRRule().setUntil(null);
        untilDatePicker.setDisable(true);
    }
};

/**
 * Finds closest instance, at least one instance past DTSTART, from initialUntilDate
 * 
 * @param initialUntilDate - selected date from untilDatePicker
 * @return - best match for until
 */
private Temporal findUntil(LocalDate initialUntilDate)
{
    Temporal timeAdjustedSelection = vComponent.getDateTimeStart().with(initialUntilDate);

    // find last instance that is not after initialUntilDate
    Iterator<Temporal> i = vComponent.getRRule().getFrequency().stream(vComponent.getDateTimeStart()).iterator();
    Temporal until = i.next();
    while (i.hasNext())
    {
        Temporal temporal = i.next();
        if (DateTimeUtilities.isAfter(temporal, timeAdjustedSelection)) break;
        until = temporal;
    }
    untilDatePicker.valueProperty().removeListener(untilListener);
    untilDatePicker.setValue(LocalDate.from(until));
    untilDatePicker.valueProperty().addListener(untilListener);
    
    return (until instanceof ZonedDateTime) ? DateTimeType.DATE_WITH_UTC_TIME.from(until) : until; // ensure type is DATE_WITH_UTC_TIME
}

// listen for changes to start date/time (type may change requiring new exception date choices)
private final ChangeListener<? super Temporal> dateTimeStartToExceptionChangeListener = (obs, oldValue, newValue) ->
{
    exceptionMasterList.clear();
    refreshExceptionDates();
    // update existing exceptions
    if (! exceptionsListView.getItems().isEmpty())
    {
        List<Temporal> newItems = null;
        if (newValue.isSupported(ChronoUnit.SECONDS))
        {
            LocalTime time = LocalDateTime.from(newValue).toLocalTime();
            newItems = exceptionsListView.getItems()
                    .stream()
                    .map(d -> LocalDate.from(d).atTime(time))
                    .collect(Collectors.toList());        
        } else if (newValue.isSupported(ChronoUnit.DAYS))
        {
            newItems = exceptionsListView.getItems()
                    .stream()
                    .map(d -> LocalDate.from(d))
                    .collect(Collectors.toList());
        } else
        {
            throw new DateTimeException("Unsupported Temporal class:" + newValue.getClass());
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
                RecurrenceRule rRule = new RecurrenceRule()
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

    // Setup frequencyComboBox items
    
    FrequencyEnum[] supportedFrequencyProperties = new FrequencyEnum[] { FrequencyEnum.DAILY,
                                                                                 FrequencyEnum.WEEKLY,
                                                                                 FrequencyEnum.MONTHLY,
                                                                                 FrequencyEnum.YEARLY };
    frequencyComboBox.setItems(FXCollections.observableArrayList(supportedFrequencyProperties));
    frequencyComboBox.setConverter(new StringConverter<FrequencyEnum>()
    {
        @Override public String toString(FrequencyEnum frequencyType)
        {
            return Settings.REPEAT_FREQUENCIES.get(frequencyType);
        }
        @Override public FrequencyEnum fromString(String string) {
            throw new RuntimeException("not required for non editable ComboBox");
        }
    });

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
                vComponent.getRRule().getFrequency().setInterval(value);
                refreshSummary();
                refreshExceptionDates();
            } else {
                String lastValue = intervalSpinner.getValue().toString();
                intervalSpinner.getEditor().textProperty().set(lastValue);
                notNumberAlert();
            }
        }
    });
    
    startDatePicker.valueProperty().addListener((obs, oldValue, newValue) ->
    {
        if (oldValue != null)
        {
            if (vComponent.getDateTimeStart().isSupported(ChronoUnit.NANOS))
            {
                long d = ChronoUnit.DAYS.between(oldValue, newValue);
                Temporal start = vComponent.getDateTimeStart().plus(d, ChronoUnit.DAYS);
                vComponent.setDateTimeStart(start);
            } else
            {
                vComponent.setDateTimeStart(newValue);                
            }
            refreshExceptionDates();
        }
    });
    startDatePicker.focusedProperty().addListener((obs, wasFocused, isNowFocused) ->
    {
        if (! isNowFocused)
        {
            try {
                String s = startDatePicker.getEditor().getText();
                LocalDate d = startDatePicker.getConverter().fromString(s);
                startDatePicker.setValue(d);
            } catch (DateTimeParseException e)
            {
                // display alert and return original date if can't parse
                String exampleDate = startDatePicker.getConverter().toString(LocalDate.now());
                notDateAlert(exampleDate);
                LocalDate d = startDatePicker.getValue();
                String s = startDatePicker.getConverter().toString(d);
                startDatePicker.getEditor().setText(s);
            }
        }
    });
    
    // END AFTER LISTENERS
    endAfterEventsSpinner.valueProperty().addListener((observable, oldSelection, newSelection) ->
    {
        if (endAfterRadioButton.isSelected())
        {
              vComponent.getRRule().setCount(newSelection);
              refreshSummary();
              refreshExceptionDates();
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
            refreshExceptionDates();
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
        if (! isNowFocused)
        {
            int value;
            String s = endAfterEventsSpinner.getEditor().getText();
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
    untilRadioButton.selectedProperty().addListener(untilRadioButtonListener);
    
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
            VComponentDisplayableOld<T> vComponent
          , Temporal dateTimeStartInstanceNew
          , Stage stage)
    {
        this.vComponent = vComponent;
        this.dateTimeStartInstanceNew = dateTimeStartInstanceNew;
        if (! isSupported(vComponent))
        {
            throw new RuntimeException("Unsupported VComponent");
        }
        
        // EXCEPTIONS
        // Note: exceptionComboBox string converter must be setup after the controller's initialization 
        // because the resource bundle isn't instantiated earlier.
        exceptionFirstTemporal = vComponent.getDateTimeStart();
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

        // Handle exception scroll events - add and remove data when scrolling to top and bottom
        stage.setOnShown(event ->
        {
            for (Node node: exceptionComboBox.lookupAll(".scroll-bar")) {
                if (node instanceof ScrollBar) {
                    final ScrollBar bar = (ScrollBar) node;
                    if (bar.getOrientation() == Orientation.VERTICAL)
                    {
                        bar.valueProperty().addListener((ChangeListener<Number>) (value, oldValue, newValue) -> 
                        {
                            if (((double) newValue > 0.9) && ((double) oldValue < 0.9))
                            {
                                // add data to bottom
                                int elements = exceptionComboBox.getItems().size();
                                exceptionFirstTemporal = exceptionComboBox.getItems().get(elements/3);
                                makeExceptionDates();
                                bar.setValue(.5);
                            } else if (((double) newValue < 0.1) && ((double) oldValue > 0.1))
                            {
                                // add data to top
                                int elements = exceptionComboBox.getItems().size();
                                Temporal firstElement = exceptionComboBox.getItems().get(0);
                                int indexInMasterList = exceptionMasterList.indexOf(firstElement);
                                int newIndex = Math.max((indexInMasterList - elements/3), 0);
                                if (newIndex < indexInMasterList)
                                {
                                    exceptionFirstTemporal = exceptionMasterList.get(newIndex);
                                    makeExceptionDates();
                                    bar.setValue(.5);
                                }
                            }
                        });
                    }
                }
            }
        });

                
        // SETUP CONTROLLER'S INITIAL DATA FROM RRULE
        boolean isRepeatable = (vComponent.getRRule() != null);
        if (isRepeatable)
        {
            setInitialValues(vComponent);
        }
        repeatableCheckBox.selectedProperty().set(isRepeatable);
        
        // DAY OF WEEK RAIO BUTTON LISTENER (FOR MONTHLY)
        dayOfWeekRadioButton.selectedProperty().addListener(dayOfWeekButtonListener);

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
    private void setInitialValues(VComponentDisplayableOld<T> vComponent)
    {
        int initialInterval = (vComponent.getRRule().getFrequency().getInterval() > 0) ?
                vComponent.getRRule().getFrequency().getInterval() : INITIAL_INTERVAL;
        intervalSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, initialInterval));
        FrequencyEnum frequencyType = vComponent.getRRule().getFrequency().frequencyType();
        frequencyComboBox.setValue(frequencyType); // will trigger frequencyListener
        switch(frequencyType)
        {
        case MONTHLY:
            ByDay rule = (ByDay) vComponent.getRRule().getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
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
        
        // ExDates
        if (vComponent.getExDate() != null)
        {
            List<Temporal> collect = vComponent
                    .getExDate()
                    .getTemporals()
                    .stream()
                    .collect(Collectors.toList());
            exceptionsListView.getItems().addAll(collect);
        }
        
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
        refreshSummary();
        refreshExceptionDates(); // Should this be here? - TODO - CHECK # OF CALLS
    }

    /** Set day of week properties if FREQ=WEEKLY and has BYDAY rule 
     * This method is called only during setup */
    private void setDayOfWeek(RecurrenceRule rRule)
    {
        // Set day of week properties
        if (rRule.getFrequency().frequencyType() == FrequencyEnum.WEEKLY)
        {
            ByRule rule = rRule.getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
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
    private void refreshExceptionDates()
    {
        exceptionFirstTemporal = vComponent.getDateTimeStart();
        makeExceptionDates();
    }
    
    /** Make list of start date/times for exceptionComboBox */
    private void makeExceptionDates()
    {
        final Temporal dateTimeStart = exceptionFirstTemporal; // vComponent.getDateTimeStart();
        final Stream<Temporal> stream1 = vComponent.stream(dateTimeStart);
        Stream<Temporal> stream2 = (vComponent.getExDate() == null) ? stream1
                : vComponent.getExDate().stream(stream1, dateTimeStart); // remove exceptions
        final Stream<Temporal> stream3; 
        if (DateTimeType.of(dateTimeStart) == DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE)
        {
            stream3 = stream2.map(t -> ((ZonedDateTime) t).withZoneSameInstant(ZoneId.systemDefault()));
        } else
        {
            stream3 = stream2;
        }
        Temporal lastExceptionInMasterList = (exceptionMasterList.isEmpty()) ? dateTimeStart.with(LocalDate.MIN) : exceptionMasterList.get(exceptionMasterList.size()-1);
        List<Temporal> exceptionDates = stream3
              .limit(EXCEPTION_CHOICE_LIMIT)
              .peek(t ->
              {
                  if (DateTimeUtilities.isAfter(t, lastExceptionInMasterList))
                  {
                      exceptionMasterList.add(t);
                  }
              })
              .collect(Collectors.toList());
        exceptionComboBox.getItems().clear();
        exceptionComboBox.getItems().addAll(exceptionDates);
    }
    
    @FXML private void handleAddException()
    {
        Temporal d = exceptionComboBox.getValue();
        exceptionsListView.getItems().add(d);
        if (vComponent.getExDate() == null) vComponent.setExDate(new ExDate());
        vComponent.getExDate().getTemporals().add(d);
        refreshExceptionDates();
        Collections.sort(exceptionsListView.getItems(),DateTimeUtilities.TEMPORAL_COMPARATOR); // Maintain sorted list
        if (exceptionComboBox.getValue() == null) addExceptionButton.setDisable(true);
    }

    @FXML private void handleRemoveException()
    {
        Temporal d = exceptionsListView.getSelectionModel().getSelectedItem();
        vComponent.getExDate().getTemporals().remove(d);
        refreshExceptionDates();
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
    private static void tooEarlyDateAlert(Temporal t)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("Event can't end before it begins.");
        alert.setContentText("Must be after " + Settings.DATE_FORMAT_AGENDA_DATEONLY.format(t));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private static void notOccurrenceDateAlert(Temporal temporal)
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
    private static void canNotRemoveLastDayOfWeek(DayOfWeek d)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Modification");
        alert.setHeaderText("Can't remove " + Settings.DAYS_OF_WEEK_MAP.get(d) + ".");
        alert.setContentText("Weekly repeat must have at least one selected day");
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        
        // set id for testing
        alert.getDialogPane().setId("last_day_of_week_alert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("last_day_of_week_alert_button_ok");
        
        alert.showAndWait();
    }
    
    private static void notDateAlert(String exampleDate)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date");
        alert.setHeaderText("Please enter valid date.");
        alert.setContentText("Example date format:" + exampleDate);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    /**
     * Produce easy to read summary of repeat rule
     * Is limited to producing strings for following repeat rules:
     * Any individual Frequency (FREQ)
     * COUNT and UNTIL properties
     * MONTHLY and WEEKLY with ByDay Byxxx rule
     * 
     * For example:
     * RRULE:FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR produces:
     * "Every 2 weeks on Monday, Wednesday, Friday, 11 times"
     * 
     * @param startTemporal LocalDate or LocalDateTime of start date/time (DTSTART)
     * @return Easy to read summary of repeat rule
     */
    public static String makeSummary(RecurrenceRule rRule, Temporal startTemporal)
    {
        StringBuilder builder = new StringBuilder();
        if (rRule.getCount() == 1) return (Settings.resources == null) ? "Once" : Settings.resources.getString("rrule.summary.once");
        
        final String frequencyText;
        if (rRule.getFrequency().getInterval() == 1)
        {
            frequencyText = Settings.REPEAT_FREQUENCIES.get(rRule.getFrequency().frequencyType());
        } else if (rRule.getFrequency().getInterval() > 1)
        {
            String every = (Settings.resources == null) ? "Every" : Settings.resources.getString("rrule.summary.every");
            builder.append(every + " ");
            builder.append(rRule.getFrequency().getInterval() + " ");
            frequencyText = Settings.REPEAT_FREQUENCIES_PLURAL.get(rRule.getFrequency().frequencyType());
        } else
        {
            throw new RuntimeException("Interval can't be less than 1");
        }
        builder.append(frequencyText);
        
        // NOTE: only ByRule allowed for this control is ByDay - others are not supported by this control
        ByDay byDay = (ByDay) rRule.getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
        switch (rRule.getFrequency().frequencyType())
        {
        case DAILY: // add nothing else
            break;
        case MONTHLY:
            int dayOfMonth = LocalDate.from(startTemporal).getDayOfMonth();
            String onDay = (Settings.resources == null) ? "on day" : Settings.resources.getString("rrule.summary.on.day");
            if (byDay == null)
            {
                builder.append(" " + onDay + " " + dayOfMonth);
            } else
            {
                String onThe = (Settings.resources == null) ? "on the" : Settings.resources.getString("rrule.summary.on.the");
                builder.append(" " + onThe + " " + byDaySummary(byDay));
            }
            break;
        case WEEKLY:
        {
            String on = (Settings.resources == null) ? "on" : Settings.resources.getString("rrule.summary.on");
            if (byDay == null)
            {
                DayOfWeek dayOfWeek = LocalDate.from(startTemporal).getDayOfWeek();
                String dayOfWeekString = Settings.DAYS_OF_WEEK_MAP.get(dayOfWeek);
                builder.append(" " + on + " " + dayOfWeekString);
            } else
            {
                builder.append(" " + on + " " + byDaySummary(byDay));
            }
            break;
        }
        case YEARLY:
        {
            String on = (Settings.resources == null) ? "on" : Settings.resources.getString("rrule.summary.on");
            String monthAndDay = Settings.DATE_FORMAT_AGENDA_MONTHDAY.format(startTemporal);
            builder.append(" " + on + " " + monthAndDay);
            break;
        }
        case HOURLY:
        case MINUTELY:
        case SECONDLY:
            throw new IllegalArgumentException("Not supported:" + rRule.getFrequency().frequencyType());
        default:
            break;
        }
        
        if (rRule.getCount() > 0)
        {
            String times = (Settings.resources == null) ? "times" : Settings.resources.getString("rrule.summary.times");
            builder.append(", " + rRule.getCount() + " " + times);
        } else if (rRule.getUntil() != null)
        {
            String until = (Settings.resources == null) ? "until" : Settings.resources.getString("rrule.summary.until");
            String date = Settings.DATE_FORMAT_AGENDA_DATEONLY.format(rRule.getUntil());
            builder.append(", " + until + " " + date);
        }
        return builder.toString();
    }
    
    private boolean isSupported(VComponentDisplayableOld<?> vComponent)
    {
        RecurrenceRule rRule = vComponent.getRRule();
        if (rRule == null)
        {
            return true;
        }
        ByDay byDay = (ByDay) rRule.getFrequency().lookupByRule(ByRuleEnum.BY_DAY);
        int byRulesSize = rRule.getFrequency().byRules().size();
        int unsupportedRules = (byDay == null) ? byRulesSize : byRulesSize-1;
        if (unsupportedRules > 0)
        {
            String unsupportedByRules = rRule.getFrequency().byRules()
                    .stream()
//                    .map(e -> e.getValue())
                    .filter(b -> b.byRuleType() != ByRuleEnum.BY_DAY)
                    .map(b -> b.byRuleType().toString())
//                    .filter(b -> b.getByRuleType() != ByRuleParameter.BY_DAY)
//                    .map(b -> b.getByRuleType().toString())
                    .collect(Collectors.joining(","));
            System.out.println("RRULE contains unsupported ByRule" + ((unsupportedRules > 1) ? "s:" : ":") + unsupportedByRules);
            return false;
        }
        return true;
    }
    
    /**
     * Produces an easy to ready summary for ByDay rule with only one ByDayPair.
     * Returns null for more than one ByDayPair.
     * Example: third Monday
     * 
     * @return easy to read summary of rule
     */
    private static String byDaySummary(ByDay byDay)
    {
        StringBuilder builder = new StringBuilder();
        for (ByDayPair b : byDay.getByDayPairs())
        {
            int ordinal = b.getOrdinal();
            DayOfWeek dayOfWeek = b.getDayOfWeek();
            String ordinalString = (ordinal > 0) ? Settings.ORDINALS.get(ordinal) + " " : "";
            String dayOfWeekString = Settings.DAYS_OF_WEEK_MAP.get(dayOfWeek);
            if (builder.length() > 0) builder.append(", ");
            builder.append(ordinalString + dayOfWeekString);            
        }
        return builder.toString();
    }    
}
