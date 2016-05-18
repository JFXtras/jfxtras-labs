package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.controller;


import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda.VComponentFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEventOld;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;
import jfxtras.scene.control.agenda.TemporalUtilities;

/**
 * Make popup to edit VEvents
 * To add custom features extend into new class
 * Changes most properties to VEvent directly.  The exceptions are DTSTART and DTEND.  Those
 * are changed by the handleEdit method because most edits require the user to indicate if
 * changes are to ONE, ALL, or THIS_AND_FUTURE.
 * 
 * @author David Bal
 * @see RepeatableController
 */
public class AppointmentEditController extends Pane
{
//    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(10, 0); // default start time used when a whole-day event gets a time
    
    private Appointment appointment; // selected appointment
    private ZoneId zone; // ZoneIDd of DTSTART, null if not using ZonedDateTime
    private Temporal startInstance; // bound to startTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in startTextListener
    private Temporal endInstance; // bound to endTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in endTextListener
    private Temporal startOriginalInstance;
    private Temporal endInstanceOriginal;
    
    private VEventOld<Appointment,?> vEvent;
//    private VComponentNew<?> vEventOld;
    private VEventOld<Appointment,?> vEventOriginal;
    private Collection<Appointment> appointments;
    private Collection<VComponentDisplayable<?>> vComponents;
    private Callback<Collection<VComponentDisplayable<?>>, Void> vEventWriteCallback;
    private Stage popup;

    /** Indicates how the popup window closed */
//    private ObjectProperty<WindowCloseType> popupCloseType; // default to X, meaning click on X to close window
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    // TODO - TRY STACK PANE TO REPLACE LocalDateTimeTextField WITH LocalDateTextField WHEN WHOLE DAY
    @FXML private TabPane appointmentEditTabPane;
    public TabPane getAppointmentEditTabPane() { return appointmentEditTabPane; }
    @FXML private LocalDateTimeTextField startTextField; // start of instance
    @FXML private LocalDateTimeTextField endTextField; // end of instance
    @FXML private CheckBox wholeDayCheckBox;
    @FXML private TextField summaryTextField; // SUMMARY
    @FXML private TextArea descriptionTextArea; // DESCRIPTION
    @FXML private TextField locationTextField; // LOCATION
    @FXML private TextField groupTextField; // CATEGORIES
    @FXML private AppointmentGroupGridPane appointmentGroupGridPane;
    @FXML private Button saveAppointmentButton;
    @FXML private Button cancelAppointmentButton;
    @FXML private Button saveRepeatButton;
    @FXML private Button cancelRepeatButton;
    @FXML private Button deleteAppointmentButton;
    @FXML private RepeatableController<Appointment> repeatableController;
    @FXML private Tab appointmentTab;
    @FXML private Tab repeatableTab;
    
    private LocalDateTime lastStartTextFieldValue = null; // last LocalDateTime in startTextField
    private LocalTime lastStartTime = LocalTime.of(10, 0); // default time
    private TemporalAmount lastDuration = Duration.ofHours(1); // Default to one hour duration
    private Temporal lastDateTimeStart;
//    private Temporal lastDateTimeEnd;
    
    // Callback for LocalDateTimeTextField that is called when invalid date/time is entered
    private final Callback<Throwable, Void> errorCallback = (throwable) ->
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date or Time");
        alert.setContentText("Please enter valid date and time");
        alert.showAndWait();
        return null;
    };
    
    private final ChangeListener<? super LocalDateTime> endTextlistener = (observable, oldSelection, newSelection) ->
    {
        if ((startTextField.getLocalDateTime() != null) && newSelection.isBefore(startTextField.getLocalDateTime()))
        {
            tooEarlyDateAlert(newSelection, startTextField.getLocalDateTime());
            endTextField.setLocalDateTime(oldSelection);
        }
        if (wholeDayCheckBox.isSelected())
        {
            endInstance = LocalDate.from(endTextField.getLocalDateTime());
        } else
        {
            endInstance = vEvent.getDateTimeType().from(endTextField.getLocalDateTime(), zone);
        }
    };
    private final ChangeListener<? super LocalDateTime> startTextListener = (observable, oldSelection, newSelection) ->
    {
        LocalDateTime end = endTextField.getLocalDateTime();
        if ((oldSelection != null) && (end != null))
        {
            TemporalAmount duration = Duration.between(oldSelection, end);
            endTextField.setLocalDateTime(newSelection.plus(duration));
        }
        
        if (wholeDayCheckBox.isSelected())
        {
            startInstance = LocalDate.from(startTextField.getLocalDateTime());
        } else
        {
            startInstance = vEvent.getDateTimeType().from(startTextField.getLocalDateTime(), zone);
        }
    };
    
    @FXML public void initialize()
    {
    }
    
    public void setupData(
              Appointment appointment
            , VComponentDisplayable<?> vComponent
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , VCalendar vCalendar
            , List<AppointmentGroup> appointmentGroups
            , Callback<Collection<VComponentDisplayable<?>>, Void> vEventWriteCallback
            , Stage popup)
    {
        appointmentGroupGridPane.getStylesheets().addAll(ICalendarAgenda.ICALENDAR_STYLE_SHEET);

        startOriginalInstance = appointment.getStartTemporal();
        endInstanceOriginal = appointment.getEndTemporal();
        this.appointment = appointment;        
        this.appointments = appointments;
        this.vComponents = vComponents;
        this.popup = popup;
        this.vEventWriteCallback = vEventWriteCallback;
        vEvent = (VEventOld<Appointment,?>) vComponent;
        
        // Disable repeat rules for events with recurrence-id
        if (vComponent.getRecurrenceDates() != null)
        { // recurrence instances can't add repeat rules (only parent can have repeat rules)
            repeatableTab.setDisable(true);
            repeatableTab.setTooltip(new Tooltip(resources.getString("repeat.tab.unavailable")));
        }

        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vEvent.getDuration() != null)
        {
            Temporal end = vEvent.getDateTimeStart().plus(vEvent.getDuration());
            vEvent.setDuration(null);
            vEvent.setDateTimeEnd(end);
        }
        
        // Copy original VEvent
        vEventOriginal = (VEventOld<Appointment,?>) VComponentFactory.newVComponent(vEvent);
        
        // String bindings
        summaryTextField.textProperty().bindBidirectional(vEvent.getSummary().valueProperty());
//        summaryTextField.textProperty().bindBidirectional(vEvent.summaryProperty());
//        descriptionTextArea.textProperty().bindBidirectional(vEvent.getDescription().valueProperty());
        descriptionTextArea.textProperty().bindBidirectional(vEvent.descriptionProperty());
        locationTextField.textProperty().bindBidirectional(vEvent.locationProperty());
        
        // WHOLE DAY
        wholeDayCheckBox.setSelected(vEvent.isWholeDay());       
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            startTextField.localDateTimeProperty().removeListener(startTextListener);
            endTextField.localDateTimeProperty().removeListener(endTextlistener);
            if (newSelection)
            {
                // save previous values to restore in case whole-day is toggled off
                lastStartTextFieldValue = startTextField.getLocalDateTime();
                lastStartTime = lastStartTextFieldValue.toLocalTime();
                lastDuration = Duration.between(lastStartTextFieldValue, endTextField.getLocalDateTime());
                lastDateTimeStart = vEvent.getDateTimeStart();
                
                LocalDate newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart());
                vEvent.setDateTimeStart(newDateTimeStart);
                LocalDate newDateTimeEnd = LocalDate.from(vEvent.getDateTimeEnd()).plus(1, ChronoUnit.DAYS);
                vEvent.setDateTimeEnd(newDateTimeEnd);
                
                LocalDateTime start = LocalDate.from(startTextField.getLocalDateTime()).atStartOfDay();
                startTextField.setLocalDateTime(start);
                LocalDateTime end = LocalDate.from(endTextField.getLocalDateTime()).plus(1, ChronoUnit.DAYS).atStartOfDay();
                endTextField.setLocalDateTime(end);
            } else
            {
                final LocalDateTime start;
                if (lastStartTextFieldValue != null)
                {
                    start = lastStartTextFieldValue;
                } else
                {
                    start = LocalDate.from(startTextField.getLocalDateTime()).atTime(lastStartTime);
                }
                startTextField.setLocalDateTime(start);
                LocalDateTime end = start.plus(lastDuration);
                endTextField.setLocalDateTime(end);
                
                final Temporal newDateTimeStart;
                if (lastDateTimeStart != null)
                {
                    newDateTimeStart = lastDateTimeStart;
                } else
                {
                    newDateTimeStart = DateTimeUtilities.DEFAULT_DATE_TIME_TYPE.from(start, ZoneId.systemDefault());
                }
                
                final Temporal newDateTimeEnd = newDateTimeStart.plus(lastDuration);
                vEvent.setDateTimeStart(newDateTimeStart);
                vEvent.setDateTimeEnd(newDateTimeEnd);

                LocalDateTime newStartRange = LocalDate.from(vEvent.getStartRange()).atStartOfDay();
                LocalDateTime newEndRange = LocalDate.from(vEvent.getEndRange()).atStartOfDay();
                vEvent.setStartRange(newStartRange);
                vEvent.setEndRange(newEndRange);
            }
            startTextField.localDateTimeProperty().addListener(startTextListener);
            endTextField.localDateTimeProperty().addListener(endTextlistener);
        });

        // TIME ZONE
        updateZone(); // initialize
        vEvent.dateTimeStartProperty().addListener((obs) -> updateZone()); // setup listener to handle changes
        
        // END DATE/TIME
        Locale locale = Locale.getDefault();
        endTextField.setLocale(locale);
        endTextField.localDateTimeProperty().addListener(endTextlistener);
//        endTextField.setLocalDateTime(DateTimeType.localDateTimeFromTemporal(endInstanceOriginal));
        endTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(endInstanceOriginal));
        endTextField.setParseErrorCallback(errorCallback);
        
        // START DATE/TIME
        startTextField.setLocale(locale);
        startTextField.localDateTimeProperty().addListener(startTextListener);
        startTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(startOriginalInstance));
        startTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(startOriginalInstance));
        startTextField.setParseErrorCallback(errorCallback);
        
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
            vEvent.getCategories().setValue(Arrays.asList(newSelection));
            // TODO - ensure groupTextField has unique description text
//            groupNameEdited.set(true);
        });
        appointmentGroupGridPane.setupData(vComponent, appointmentGroups);

        // SETUP REPEATABLE CONTROLLER
        repeatableController.setupData(vComponent, startInstance, popup);
        
        // When Appointment tab is selected make sure start and end times are valid, adjust if not
        appointmentEditTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            if (newValue == appointmentTab)
            {
                Runnable alertRunnable = validateStartInstance();
                if (alertRunnable != null)
                {
                    Platform.runLater(alertRunnable); // display alert after tab change refresh
                }
            }
        });
    }

    /* If startInstance isn't valid due to a RRULE change, changes startInstance and
     * endInstance to closest valid values
     */
    private Runnable validateStartInstance()
    {
        if (! vEvent.isStreamValue(startInstance))
        {
            Temporal instanceBefore = vEvent.previousStreamValue(startInstance);
            Optional<Temporal> optionalAfter = vEvent.stream(startInstance).findFirst();
            Temporal newStartInstance = (optionalAfter.isPresent()) ? optionalAfter.get() : instanceBefore;
            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startInstance, endInstance);
            Temporal newEndInstance = newStartInstance.plus(duration);
            Temporal startInstanceBeforeChange = startInstance;
            startTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newStartInstance));
            endTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newEndInstance));
            startOriginalInstance = startInstance;
            return () -> startInstanceChangedAlert(startInstanceBeforeChange, newStartInstance);
        }
        return null;
    }

    private void updateZone()
    {
        zone = (vEvent.getDateTimeStart() instanceof ZonedDateTime) ? ZoneId.from(vEvent.getDateTimeStart()) : null;
    }
    
    @FXML private void handleSave()
    {
        System.out.println("summary text:" + vEvent.getSummary().getValue());
        System.out.println("summary text:" + vEventOriginal.getSummary().getValue());
//        vEvent.handleEdit(
//                vEventOriginal
//              , vComponents
//              , startOriginalInstance
//              , startInstance
//              , endInstance
//              , appointments
//              , EditChoiceDialog.EDIT_DIALOG_CALLBACK);
        popup.close();
    }
    
    // Checks to see if start date has been changed, and a date shift is required, and then runs ordinary handleSave method.
    @FXML private void handleRepeatSave()
    {
        handleSave();
    }
    
    @FXML private void handleCancelButton()
    {
        vEventOriginal.copyTo(vEvent);
        popup.close();
    }

    @FXML private void handleDeleteButton()
    {
//        vEvent.handleDelete(
//                vComponents
//              , startInstance
//              , appointment
//              , appointments
//              , DeleteChoiceDialog.DELETE_DIALOG_CALLBACK);
        popup.close();
    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private void tooEarlyDateAlert(Temporal t1, Temporal t2)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("End must be after start");
        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is not after" + System.lineSeparator() + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    /* Displays an alert notifying that startInstance has changed due to changes in the Repeat tab.
     * These changes can include the day of the week is not valid or the start date has shifted.
     * The closest valid date is substituted.
    */
    // TODO - PUT COMMENTS IN RESOURCES
    private void startInstanceChangedAlert(Temporal t1, Temporal t2)
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
}
