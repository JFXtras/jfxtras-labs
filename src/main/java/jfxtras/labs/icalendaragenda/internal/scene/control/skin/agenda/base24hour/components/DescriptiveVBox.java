package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.TemporalUtilities;

/** Makes new TabPane for editing a {@link VComponentDisplayable} component
 * 
 * @author David Bal
 */
public abstract class DescriptiveVBox<T extends VComponentDisplayable<?>> extends VBox
{
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    public ResourceBundle getResources() { return resources; }

    @FXML GridPane timeGridPane;
    LocalDateTimeTextField startDateTimeTextField = new LocalDateTimeTextField(); // start of recurrence
    LocalDateTextField startDateTextField = new LocalDateTextField(); // start of recurrence when wholeDayCheckBox is selected
    
    @FXML Label endLabel;
    
    @FXML private CheckBox wholeDayCheckBox;
    @FXML TextField summaryTextField;
    @FXML TextArea descriptionTextArea;
    @FXML Label locationLabel;
    @FXML TextField locationTextField;
    @FXML private TextField groupTextField;
    @FXML private AppointmentGroupGridPane appointmentGroupGridPane;
    @FXML private Button saveComponentButton;
    @FXML private Button cancelComponentButton;
    @FXML private Button saveRepeatButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button deleteComponentButton;
    @FXML private Tab appointmentTab;
    @FXML private Tab repeatableTab;
    
    public DescriptiveVBox( )
    {
        super();
        loadFxml(DescriptiveVBox.class.getResource("EditDescriptive.fxml"), this);
        appointmentGroupGridPane.getStylesheets().addAll(getStylesheets());
        startDateTimeTextField.setId("startDateTimeTextField");
        startDateTextField.setId("startDateTextField");
    }
    
//    public void dispose()
//    {
//        vComponentEdited.getDateTimeStart().valueProperty().removeListener(dateTimeStartListener);
//    }

    final private ChangeListener<? super LocalDate> startDateTextListener = (observable, oldValue, newValue) -> synchStartDate(oldValue, newValue);

    /** Update startDateTimeTextField when startDateTextField changes */
    void synchStartDate(LocalDate oldValue, LocalDate newValue)
    {
        System.out.println("new value2:" + newValue);
        startRecurrenceProperty.set(newValue);
//        shiftAmount = Period.between(LocalDate.from(startOriginalRecurrence), newValue);
        startDateTimeTextField.localDateTimeProperty().removeListener(startDateTimeTextListener);
        LocalDateTime newDateTime = startDateTimeTextField.getLocalDateTime().with(newValue);
        startDateTimeTextField.setLocalDateTime(newDateTime);
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
    }
            
    final private ChangeListener<? super LocalDateTime> startDateTimeTextListener = (observable, oldValue, newValue) -> synchStartDateTime(oldValue, newValue);

    /** Update startDateTextField when startDateTimeTextField changes */
    void synchStartDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        System.out.println("new value:" + newValue);
        startRecurrenceProperty.set(newValue);
//        shiftAmount = Duration.between(DateTimeType.DATE_WITH_LOCAL_TIME.from(startOriginalRecurrence), newValue);
        startDateTextField.localDateProperty().removeListener(startDateTextListener);
        LocalDate newDate = LocalDate.from(startDateTimeTextField.getLocalDateTime());
        startDateTextField.setLocalDate(newDate);
        startDateTextField.localDateProperty().addListener(startDateTextListener);
//        throw new RuntimeException("hre:");
    }
    
    // Callback for LocalDateTimeTextField that is called when invalid date/time is entered
    protected final Callback<Throwable, Void> errorCallback = (throwable) ->
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date or Time");
        alert.setContentText("Please enter valid date and time");
        alert.showAndWait();
        return null;
    };
    
    T vComponentEdited;
//    List<T> vComponents;
//    TemporalAmount shiftAmount = Duration.ZERO; // shift amount to apply to edited component
    Temporal startOriginalRecurrence;
    /** Contains the start recurrence Temporal LocalDate or LocalDateTime */
    ObjectProperty<Temporal> startRecurrenceProperty = new SimpleObjectProperty<>();

    public void setupData(
//            Appointment appointment,
            T vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
//            List<T> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        startOriginalRecurrence = startRecurrence;
        vComponentEdited = vComponent;
        
        // Disable repeat rules for events with recurrence-id
        if (vComponentEdited.getRecurrenceDates() != null)
        { // recurrence recurrences can't add repeat rules (only parent can have repeat rules)
            repeatableTab.setDisable(true);
            repeatableTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }
        
        // String bindings
        if (vComponentEdited.getSummary() == null)
        {
            vComponentEdited.setSummary(Summary.parse(""));
        }
        summaryTextField.textProperty().bindBidirectional(vComponentEdited.getSummary().valueProperty());
        
        // START DATE/TIME
        startDateTimeTextField.setLocale(Locale.getDefault());
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
//        startDateTimeTextField.localDateTimeProperty().addListener(shiftAmountListener);
        startDateTextField.localDateProperty().addListener(startDateTextListener);
//        startDateTextField.localDateProperty().addListener(shiftAmountListener);
        final LocalDateTime start;
        if (startRecurrence.isSupported(ChronoUnit.NANOS))
        {
            start = TemporalUtilities.toLocalDateTime(startRecurrence);
        } else
        {
            start = LocalDate.from(startRecurrence).atTime(defaultStartTime);
        }
//        System.out.println("start:" + start + " " + startOriginalRecurrence + " " + appointment.getStartTemporal());
        startDateTimeTextField.setLocalDateTime(start);
        startDateTimeTextField.setParseErrorCallback(errorCallback);
        startDateTextField.setLocale(Locale.getDefault());
        startDateTextField.setLocalDate(LocalDate.from(startRecurrence));
        startDateTextField.setParseErrorCallback(errorCallback);
        
        // WHOLE DAY
        wholeDayCheckBox.setSelected(vComponentEdited.isWholeDay());
        handleWholeDayChange(vComponentEdited, wholeDayCheckBox.isSelected()); 
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) -> handleWholeDayChange(vComponent, newSelection));
        
        // APPOINTMENT GROUP
        appointmentGroupGridPane.appointmentGroupSelectedProperty().addListener(
            (observable, oldSelection, newSelection) ->
            {
                Integer i = appointmentGroupGridPane.getAppointmentGroupSelected();
                String newText = appointmentGroups.get(i).getDescription();
                groupTextField.setText(newText);
//                groupNameEdited.set(true); // TODO - HANDLE APPOINTMENT GROUP I/O
            });

        // store group name changes by each character typed
        groupTextField.textProperty().addListener((observable, oldSelection, newSelection) ->
        {
            int i = appointmentGroupGridPane.getAppointmentGroupSelected();
            appointmentGroups.get(i).setDescription(newSelection);
            appointmentGroupGridPane.updateToolTip(i, appointmentGroups);
            vComponentEdited.withCategories(newSelection);
        });
        appointmentGroupGridPane.setupData(vComponentEdited, appointmentGroups);
        
        vComponentEdited.getDateTimeStart().valueProperty().addListener(dateTimeStartListener);

//        vComponentEdited.dateTimeStartProperty().addListener((obs, oldValue, newValue) -> 
//        {
//            System.out.println("DTSTART changed:");
//            validateRecurrenceDates();
//        });

        
        //        System.out.println("cats3:" + vComponent.getCategories().size());

        // SETUP REPEATABLE CONTROLLER
//        repeatableController.setupData(vComponent, startRecurrence, popup);
        
//        // When Appointment tab is selected make sure start and end times are valid, adjust if not
//        appointmentEditTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
//        {
//            if (newValue == appointmentTab)
//            {
//                Runnable alertRunnable = validateStartRecurrence();
//                if (alertRunnable != null)
//                {
//                    Platform.runLater(alertRunnable); // display alert after tab change refresh
//                }
//            }
//        });
    }
    
//    final private ChangeListener<? super Temporal> shiftAmountListener = (observable, oldValue, newValue) ->
//    {
//        if (newValue instanceof LocalDate)
//        {
//            shiftAmount = Period.between(LocalDate.from(startOriginalRecurrence), LocalDate.from(newValue));            
//        } else if (newValue instanceof LocalDateTime)
//        {
//            shiftAmount = Duration.between(DateTimeType.DATE_WITH_LOCAL_TIME.from(startOriginalRecurrence), newValue);
//        }
////        throw new RuntimeException("bad code:");
//    };
    
    /** Synch recurrence dates when DTSTART is modified (can occur when {@link synchStartDatePickerAndComponent#startDatePicker} changes */
    ChangeListener<? super Temporal> dateTimeStartListener = (obs, oldValue, newValue) -> 
    {
        // If recurrence before or equal to DTSTART adjust to match DTSTART
        // if recurrence is after DTSTART don't adjust it
        LocalDate d1 = LocalDate.from(startRecurrenceProperty.get());
        LocalDate d2 = LocalDate.from(newValue);
        LocalDate d3 = LocalDate.from(oldValue);
        if ((! DateTimeUtilities.isAfter(d1, d2)) || d1.equals(d3))
        {
            Temporal r = newValue.with(startDateTextField.getLocalDate());
            TemporalAmount shift = DateTimeUtilities.temporalAmountBetween(r, newValue);
            LocalDateTime startNew = startDateTimeTextField.getLocalDateTime().plus(shift);
//            startDateTimeTextField.localDateTimeProperty().removeListener(shiftAmountListener);
//            startDateTextField.localDateProperty().removeListener(shiftAmountListener);
//            TemporalAmount shiftAmountSave = shiftAmount;
            startDateTimeTextField.setLocalDateTime(startNew);
//            System.out.println("dtstart changed:" + oldValue + " " + newValue + " " + shiftAmount);

//            shiftAmount = shiftAmountSave;
//            startDateTimeTextField.localDateTimeProperty().addListener(shiftAmountListener);
//            startDateTextField.localDateProperty().addListener(shiftAmountListener);
//            System.out.println("recurrence:" + startRecurrenceProperty.get() + " " + shift);
//            startOriginalRecurrence = startOriginalRecurrence.plus(duration);
//            System.out.println("recurrence:" + startRecurrenceProperty.get() + " " + startOriginalRecurrence);
//            System.out.println("new start:" + startDateTimeTextField.getLocalDateTime());
//            System.out.println("DTSTART changed:" + vComponentEdited.getDateTimeStart().getValue() +
//                    startDateTimeTextField.getLocalDateTime() + " " + startDateTextField.getLocalDate()
//                    );
        } else
        {
//            shiftAmount = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
        }
    };
    
    //    private LocalDateTime lastStartTextFieldValue = null; // last LocalDateTime in startTextField
    protected LocalTime defaultStartTime = LocalTime.of(10, 0); // default time
//    protected TemporalAmount lastDuration = Duration.ofHours(1); // Default to one hour duration
//    protected Temporal lastDateTimeStart;
    
    /*
     * When
     */
    void handleWholeDayChange(T vComponent, Boolean newSelection)
    {
        startDateTimeTextField.localDateTimeProperty().removeListener(startDateTimeTextListener);
        startDateTextField.localDateProperty().removeListener(startDateTextListener);
        if (newSelection)
        {
            timeGridPane.getChildren().remove(startDateTimeTextField);
            timeGridPane.add(startDateTextField, 1, 0);
        } else
        {
            timeGridPane.getChildren().remove(startDateTextField);
            timeGridPane.add(startDateTimeTextField, 1, 0);
        }
        startDateTextField.localDateProperty().addListener(startDateTextListener);
        startDateTimeTextField.localDateTimeProperty().addListener(startDateTimeTextListener);
    }
    
    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
     * endRecurrence to closest valid values
     */
    void synchRecurrenceDates(Temporal oldValue, Temporal newValue)
    {
        if (! vComponentEdited.isRecurrence(startRecurrenceProperty.get()))
        {
//            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
            TemporalAmount shift = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
//            System.out.println("synch amount:" + oldValue + " " + newValue + " " + shiftAmount);
            LocalDateTime startNew = startDateTimeTextField.getLocalDateTime().plus(shift);
//            startDateTextField.localDateProperty().removeListener(startDateTextListener);
//            startDateTextField.localDateProperty().removeListener(shiftAmountListener);
//            TemporalAmount shiftAmountSave = shiftAmount;
            startDateTimeTextField.setLocalDateTime(startNew);
//            shiftAmount = shiftAmountSave;
//            startDateTextField.localDateProperty().addListener(startDateTextListener);
//            startDateTextField.localDateProperty().addListener(shiftAmountListener);
//            startDateTimeTextField.setLocalDateTime(startNew);
//            startOriginalRecurrence = startOriginalRecurrence.plus(duration);
//            Temporal recurrenceBefore = vComponentEdited.previousStreamValue(startRecurrenceProperty.get());
//            Optional<Temporal> optionalAfter = vComponentEdited.streamRecurrences(startRecurrenceProperty.get()).findFirst();
//            Temporal newStartRecurrence = (optionalAfter.isPresent()) ? optionalAfter.get() : recurrenceBefore;
//            startDateTimeTextField.setLocalDateTime((LocalDateTime) DateTimeUtilities.DateTimeType.DATE_WITH_LOCAL_TIME.from(newStartRecurrence));
//            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
//            Temporal endNew = endDateTimeTextField.getLocalDateTime().plus(duration);
            System.out.println("new start:" + startDateTimeTextField.getLocalDateTime());
        }
    }
    
    
//    // TODO - FIX THIS
//    @Deprecated
//    Runnable validateStartRecurrence()
//    {
////        Temporal actualRecurrence = vEvent.streamRecurrences(startRecurrence).findFirst().get();
//        if (! vComponentEdited.isRecurrence(startRecurrenceProperty.get()))
//        {
//            Temporal recurrenceBefore = vComponentEdited.previousStreamValue(startRecurrenceProperty.get());
//            Optional<Temporal> optionalAfter = vComponentEdited.streamRecurrences(startRecurrenceProperty.get()).findFirst();
//            Temporal newStartRecurrence = (optionalAfter.isPresent()) ? optionalAfter.get() : recurrenceBefore;
////            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startRecurrence, endRecurrence);
////            Temporal newEndRecurrence = newStartRecurrence.plus(duration);
//            Temporal startRecurrenceBeforeChange = startRecurrenceProperty.get();
//            startDateTimeTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newStartRecurrence));
////            endTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newEndRecurrence));
//            startOriginalRecurrence = startRecurrenceProperty.get();
//            return () -> startRecurrenceChangedAlert(startRecurrenceBeforeChange, newStartRecurrence);
//        }
//        return null;
//    }
    
    /* Displays an alert notifying that startInstance has changed due to changes in the Repeat tab.
     * These changes can include the day of the week is not valid or the start date has shifted.
     * The closest valid date is substituted.
    */
    // TODO - PUT COMMENTS IN RESOURCES
    protected void startRecurrenceChangedAlert(Temporal t1, Temporal t2)
    {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.getDialogPane().setId("startInstanceChangedAlert");
        alert.getDialogPane().lookupButton(ButtonType.OK).setId("startInstanceChangedAlertOkButton");
        alert.setHeaderText("Time not valid due to repeat rule change");
        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is no longer valid." + System.lineSeparator()
                    + "It has been replaced by " + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    protected static void loadFxml(URL fxmlFile, Object rootController)
    {
        FXMLLoader loader = new FXMLLoader(fxmlFile);
        loader.setController(rootController);
        loader.setRoot(rootController);
        loader.setResources(Settings.resources);
        try {
            loader.load();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
 
