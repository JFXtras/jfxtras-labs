package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentGroupGridPane;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.VComponentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.RRuleType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

/**
 * Make popup to edit VEvents
 * 
 * @author David Bal
 * @see RepeatableController
 */
public class AppointmentEditController
{
//    private static final LocalTime DEFAULT_START_TIME = LocalTime.of(10, 0); // default start time used when a whole-day event gets a time
    
    private Appointment appointment; // selected appointment
    private LocalDateTime dateTimeInstanceStartOriginal;
    private LocalDateTime dateTimeInstanceEndOriginal;
    
    private VEvent<Appointment> vEvent;
//    private VComponent<Appointment> vEventOld;
    private VEvent<Appointment> vEventOld;
    private Collection<Appointment> appointments;
    private Collection<VComponent<Appointment>> vComponents;
    private Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback;
    private Stage popup;

    /** Indicates how the popup window closed */
    private ObjectProperty<WindowCloseType> popupCloseType; // default to X, meaning click on X to close window
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    // TODO - TRY STACK PANE TO REPLACE LocalDateTimeTextField WITH LocalDateTextField WHEN WHOLE DAY
    @FXML private LocalDateTimeTextField startTextField; // DTSTART
    @FXML private LocalDateTimeTextField endTextField; // DTEND
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
    @FXML private Tab repeatableTab;
    
    private LocalDateTime lastDateTimeStart = null;
    private LocalDateTime lastDateTimeEnd = null;

    // Callback for LocalDateTimeTextField that is called when invalid date/time is entered
    private final Callback<Throwable, Void> errorCallback = (throwable) ->
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date or Time");
        alert.setContentText("Please enter valid date and time");
        alert.showAndWait();
        return null;
    };
    
    private final ChangeListener<? super LocalDateTime> startTextListener = (observable, oldSelection, newSelection) ->
    {
//        if (newSelection.isAfter(endTextField.getLocalDateTime()))
//        {
//            tooLateDateAlert(newSelection, endTextField.getLocalDateTime());
//            startTextField.setLocalDateTime(oldSelection);
//        } else
//        {
            Temporal newDateTimeStart = adjustStartEndTemporal(
                    vEvent.getDateTimeStart()
                  , oldSelection
                  , newSelection);
            vEvent.setDateTimeStart(newDateTimeStart);
//        }
    };
    private final ChangeListener<? super LocalDateTime> endTextlistener = (observable, oldSelection, newSelection) ->
    {
        if (newSelection.isBefore(startTextField.getLocalDateTime()))
        {
            tooEarlyDateAlert(newSelection, startTextField.getLocalDateTime());
            endTextField.setLocalDateTime(oldSelection);
        } else
        {
            Temporal newDateTimeEnd = adjustStartEndTemporal(
                    vEvent.getDateTimeEnd()
                  , oldSelection
                  , newSelection);
            vEvent.setDateTimeEnd(newDateTimeEnd);
        }
    };
    // Change time and shift dates for start and end edits
    private Temporal adjustStartEndTemporal(Temporal input, LocalDateTime oldSelection, LocalDateTime newSelection)
    {
        long dayShift = ChronoUnit.DAYS.between(oldSelection, newSelection);
        System.out.println("dayShift:" + dayShift);
        if (input instanceof LocalDate)
        {
            return input.plus(dayShift, ChronoUnit.DAYS);
        } else if (input instanceof LocalDateTime)
        {
            LocalTime time = newSelection.toLocalTime();
            return ((LocalDateTime) input).toLocalDate()
                    .atTime(time)
                    .plus(dayShift, ChronoUnit.DAYS);
        } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
    }
    
    @FXML public void initialize() { }
    
    public void setupData(
              Appointment appointment
            , VComponent<Appointment> vComponent
            , LocalDateTimeRange dateTimeRange
            , Collection<Appointment> appointments
            , Collection<VComponent<Appointment>> vComponents
            , List<AppointmentGroup> appointmentGroups
            , Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback
            , Stage popup)
    {
        dateTimeInstanceStartOriginal = appointment.getStartLocalDateTime();
        dateTimeInstanceEndOriginal = appointment.getEndLocalDateTime();
        this.appointment = appointment;        
        this.appointments = appointments;
        this.vComponents = vComponents;
        this.popup = popup;
//        VComponent<Appointment> vComponentOld = VComponentFactory.newVComponent(vComponent);
        this.vEventWriteCallback = vEventWriteCallback;
        vEvent = (VEvent<Appointment>) vComponent;
//        this.popupCloseType = popupCloseType;

        // Copy original VEvent
        vEventOld = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent);
        
        // String bindings
        summaryTextField.textProperty().bindBidirectional(vEvent.summaryProperty());
        descriptionTextArea.textProperty().bindBidirectional(vEvent.descriptionProperty());
        locationTextField.textProperty().bindBidirectional(vEvent.locationProperty());
        
        // WHOLE DAY
        boolean wholeDay = vEvent.getDateTimeStart() instanceof LocalDate;
        wholeDayCheckBox.setSelected(wholeDay);       
        wholeDayCheckBox.selectedProperty().addListener((observable, oldSelection, newSelection) ->
        {
            startTextField.localDateTimeProperty().removeListener(startTextListener);
            endTextField.localDateTimeProperty().removeListener(endTextlistener);
            if (newSelection)
            {
                lastDateTimeStart = startTextField.getLocalDateTime();
                lastDateTimeEnd = endTextField.getLocalDateTime();
//                LocalDateTime startDate = startTextField.getLocalDateTime().toLocalDate().atStartOfDay();
//                startTextField.setLocalDateTime(startDate);
                
//                LocalTime localTime = endTextField.getLocalDateTime().toLocalTime();
//                final LocalDateTime endDate;
//                final long days;
//                if (localTime.equals(LocalTime.of(0, 0)))
//                {
//                    days = 0;
//                    endDate = endTextField.getLocalDateTime();
//                } else
//                {
//                    days = 1;
//                    endDate = endTextField.getLocalDateTime().toLocalDate().plusDays(days).atStartOfDay();
//                }
//                endTextField.setLocalDateTime(endDate);
                
                LocalDate newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart());
                vEvent.setDateTimeStart(newDateTimeStart);
                
                LocalDateTime s = LocalDate.from(vEvent.getDateTimeStart()).atStartOfDay();
                startTextField.setLocalDateTime(s);

                LocalDateTime e = LocalDate.from(vEvent.getDateTimeEnd()).atStartOfDay();
                endTextField.setLocalDateTime(e);
                
//                LocalDate newDateTimeEnd = LocalDate.from(vEvent.getDateTimeEnd());
//                vEvent.setDateTimeEnd(newDateTimeEnd.plus(days, ChronoUnit.DAYS));

                LocalDate newStartRange = LocalDate.from(vEvent.getStartRange());
                LocalDate newEndRange = LocalDate.from(vEvent.getEndRange());
                vEvent.setStartRange(newStartRange);
                vEvent.setEndRange(newEndRange);
            } else
            {
                if ((lastDateTimeStart != null) && (lastDateTimeEnd != null))
                {
                    startTextField.setLocalDateTime(lastDateTimeStart);
                    endTextField.setLocalDateTime(lastDateTimeEnd);
                    
//                    LocalTime timeStart = lastDateTimeStart.toLocalTime();
//                    LocalDateTime newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart()).atTime(timeStart);
                    vEvent.setDateTimeStart(lastDateTimeStart);
                    
//                    LocalTime timeEnd = lastDateTimeEnd.toLocalTime();
//                    long days = (timeEnd.equals(LocalTime.of(0, 0))) ? 0 : 1;
//                    LocalDateTime newDateTimeEnd = LocalDate
//                            .from(vEvent.getDateTimeEnd())
//                            .minus(days, ChronoUnit.DAYS)
//                            .atTime(timeEnd);
                    vEvent.setDateTimeEnd(lastDateTimeEnd);
                } else
                {
                    LocalDateTime newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart()).atTime(vEvent.getLastStartTime());
                    vEvent.setDateTimeStart(newDateTimeStart);
                    startTextField.setLocalDateTime(newDateTimeStart);
                    LocalDateTime newDateTimeEnd = (LocalDateTime) vEvent.getDateTimeEnd();
                    endTextField.setLocalDateTime(newDateTimeEnd);
//                    LocalDateTime newDateTimeEnd = LocalDate.from(vEvent.getDateTimeEnd()).atStartOfDay();
//                    vEvent.setDateTimeEnd(newDateTimeEnd);
                }
                LocalDateTime newStartRange = LocalDate.from(vEvent.getStartRange()).atStartOfDay();
                LocalDateTime newEndRange = LocalDate.from(vEvent.getEndRange()).atStartOfDay();
                vEvent.setStartRange(newStartRange);
                vEvent.setEndRange(newEndRange);
            }
            startTextField.localDateTimeProperty().addListener(startTextListener);
            endTextField.localDateTimeProperty().addListener(endTextlistener);
        });
        
        // START DATE/TIME
        Locale locale = Locale.getDefault();
        startTextField.setLocale(locale);
        startTextField.setLocalDateTime(dateTimeInstanceStartOriginal);
        startTextField.setParseErrorCallback(errorCallback);
        startTextField.localDateTimeProperty().addListener(startTextListener);
        vEvent.dateTimeStartProperty().addListener((obs, oldValue, newValue) ->
        {
            startTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(newValue));
        });
        
        // END DATE/TIME
        endTextField.setLocale(locale);
        endTextField.setLocalDateTime(dateTimeInstanceEndOriginal);
        endTextField.setParseErrorCallback(errorCallback);
        endTextField.localDateTimeProperty().addListener(endTextlistener);
        vEvent.dateTimeEndProperty().addListener((obs, oldValue, newValue) ->
        {
            endTextField.localDateTimeProperty().removeListener(endTextlistener);
            endTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(newValue));
            endTextField.localDateTimeProperty().addListener(endTextlistener);
        });
        
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
//            System.out.println("appointmentGroups1:" + appointmentGroups);
            appointmentGroups.get(i).setDescription(newSelection);
            appointmentGroupGridPane.updateToolTip(i, appointmentGroups);
            vEvent.setCategories(newSelection);
            // TODO - ensure groupTextField is unique description text
//            groupNameEdited.set(true);
        });
        appointmentGroupGridPane.setupData(vComponent, appointmentGroups);

        // SETUP REPEATABLE CONTROLLER
        repeatableController.setupData(vComponent, startTextField.getLocalDateTime());
    }

    // AFTER CLICK SAVE VERIFY REPEAT IS VALID, IF NOT PROMPT.
    @FXML private void handleSaveButton()
    {
        // adjust DTSTART if first occurrence is not equal to it
        Temporal t1 = vEvent.stream(vEvent.getDateTimeStart()).findFirst().get();
        final Temporal first;
        if (vEvent.getExDate() != null)
        {            
            Temporal t2 = Collections.min(vEvent.getExDate().getTemporals(), VComponent.TEMPORAL_COMPARATOR);
            first = (VComponent.isBefore(t1, t2)) ? t1 : t2;
        } else
        {
          first = t1;
        }
        long dayShift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), first);
//        System.out.println("dayShift:" + dayShift);
        if (dayShift > 0) vEvent.setDateTimeStart(first);
        
        // TODO - REMOVE INVALID EXCEPTIONS
        
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        
        final RRuleType rruleType = ICalendarUtilities.getRRuleType(vEvent.getRRule(), vEventOld.getRRule());
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            vEvent.setRRule(null);
            vEvent.setRDate(null);
            vEvent.setExDate(null);
        case WITH_NEW_REPEAT:
        case INDIVIDUAL:
            break;
        case WITH_EXISTING_REPEAT:
            if (! vEvent.equals(vEventOld)) // if changes occurred
            {
                ChangeDialogOptions changeResponse = ICalendarUtilities.repeatChangeDialog();
                switch (changeResponse)
                {
                case ALL:
                    appointments.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
                    vEvent.instances().clear(); // clear VEvent's outdated collection of appointments
                    appointments.addAll(vEvent.makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
                    break;
                case CANCEL:
                    vEventOld.copyTo(vEvent); // return to original vEvent
                    break;
                case THIS_AND_FUTURE:
                    vComponents.add(vEventOld);
                    LocalDateTime dateTimeInstanceStartNew = appointment.getStartLocalDateTime();
                    thisAndFuture(vEvent, vEventOld, dateTimeInstanceStartOriginal, dateTimeInstanceStartNew);
                    break;
                case ONE:
                    break;
                default:
                    break;
                }
            }
        }
        // refresh instances (appointments)
        appointments.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
        vEvent.instances().clear();
        appointments.addAll(vEvent.makeInstances());

        popup.close();

        
//        final ICalendarUtilities.WindowCloseType result = vEvent.edit(
//                dateTimeInstanceStartOriginal
//              , appointment.getStartLocalDateTime()
//              , vEventOld
//              , appointments
//              , vComponents
//              , a -> ICalendarUtilities.repeatChangeDialog()
//              , vEventWriteCallback);
//        popupCloseType.set(result);
//        if (popupCloseType.get() == WindowCloseType.CLOSE_WITHOUT_CHANGE)
//        {
//        }
    }
    
    // Update changing this and future instances in VComponent 
    // This is done by ending the previous VComponent with a UNTIL date or date/time and
    // starting a new VComponent from the selected instance.  EXDATE, RDATE and RECURRENCES are split
    // between both VComponents.
    // vEvent is edited VEvent with new settings, vEventOld has former settings.

    private void thisAndFuture(
              VEvent<Appointment> vEvent
            , VEvent<Appointment> vEventOld
            , LocalDateTime dateTimeInstanceStartOriginal
            , LocalDateTime dateTimeInstanceStartNew)
    {
        // Adjust old VEvent
        if (vEventOld.getRRule().getCount() != null) vEventOld.getRRule().setCount(0);
        LocalDateTime newDateTime = dateTimeInstanceStartOriginal.toLocalDate().atStartOfDay().minusNanos(1);
        vEventOld.getRRule().setUntil(newDateTime);
        vEventOld.setUniqueIdentifier();
        
        // Adjust new VEvent
        vEvent.setDateTimeStart(dateTimeInstanceStartNew);
        
        // Split EXDates dates between this and newVEvent
        if (vEvent.getExDate() != null)
        {
            vEvent.getExDate().getTemporals().clear();
            final Iterator<Temporal> exceptionIterator = vEventOld.getExDate().getTemporals().iterator();
            while (exceptionIterator.hasNext())
            {
                Temporal d = exceptionIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, dateTimeInstanceStartNew);
//                if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                if (result < 0)
                {
                    exceptionIterator.remove();
                } else {
                    vEvent.getExDate().getTemporals().add(d);
                }
            }
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
            if (vEventOld.getExDate().getTemporals().isEmpty()) vEventOld.setExDate(null);
        }

        // Split recurrence date/times between this and newVEvent
        if (vEvent.getRDate() != null)
        {
            vEvent.getRDate().getTemporals().clear();
            final Iterator<Temporal> recurrenceIterator = vEventOld.getRDate().getTemporals().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, dateTimeInstanceStartNew);
//                if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                if (result < 0)
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRDate().getTemporals().add(d);
                }
            }
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
            if (vEventOld.getRDate().getTemporals().isEmpty()) vEventOld.setRDate(null);
        }

        // Split instance dates between this and newVEvent
        if (vEvent.getRRule().getRecurrences() != null)
        {
            vEvent.getRRule().getRecurrences().clear();
            final Iterator<Temporal> recurrenceIterator = vEventOld.getRRule().getRecurrences().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                if (VComponent.isBefore(d, dateTimeInstanceStartNew))
                {
                    recurrenceIterator.remove();
                } else {
                    vEvent.getRRule().getRecurrences().add(d);
                }
            }
        }
        
//        // Modify this (edited) VEvent - DON'T NEED - DONE IN CONTROLLER
//        System.out.println("modify this vEvent:" + durationInNanos);
//        setDateTimeStart(dateTimeStartInstanceNew);
//        setDurationInNanos(durationInNanos);
//        System.out.println("done modify this vEvent:");
        
        // Modify COUNT for this (the edited) VEvent
        if (vEvent.getRRule().getCount() > 0)
        {
            final int newCount = (int) vEvent.instances()
                    .stream()
                    .map(a -> a.getStartLocalDateTime())
                    .filter(d -> ! d.isBefore(dateTimeInstanceStartNew))
                    .count();
            vEvent.getRRule().setCount(newCount);
        }

        // Remove old appointments, add back ones
        appointments.removeIf(a -> vEventOld.instances().stream().anyMatch(a2 -> a2 == a));
        vEventOld.instances().clear();
        appointments.addAll(vEventOld.makeInstances()); // add vEventOld part of new appointments
        
        System.out.println("veventold:" + appointments.size());
        vEventOld.instances().stream().forEach(System.out::println);
        
    }
    
    @FXML private void handleCancelButton()
    {
        popup.close();

//        vEventOld.copyTo(vEvent);
//        popupCloseType.set(WindowCloseType.CANCEL);
//        System.out.println("equal:" + vEventOld.getRRule() + " " + vEvent.getRRule());
    }

    @FXML private void handleDeleteButton()
    {
//        windowCloseType.set(WindowCloseType.X);
//        System.out.println("delete:" + vEvent.getRRule());
//        LocalDateTime dateTimeStartInstanceNew = startTextField.getLocalDateTime();
        Temporal dateOrDateTime = (appointment.isWholeDay()) ? 
                appointment.getStartLocalDateTime().toLocalDate()
              : appointment.getStartLocalDateTime();
        final ICalendarUtilities.WindowCloseType result = vEvent.delete(
                dateOrDateTime
//              , appointments
              , vComponents
              , a -> ICalendarUtilities.repeatChangeDialog()
              , a -> ICalendarUtilities.confirmDelete(a)
              , vEventWriteCallback);
        System.out.println("result: " + result);
        popup.close();
//        System.out.println("delete:" + vComponents.size() + " " + appointments.size());
//        popupCloseType.set(result);
    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private void tooEarlyDateAlert(Temporal t1, Temporal t2)
    {
        System.out.println("tooearly:");
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("End must be after start");
        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is not after" + System.lineSeparator() + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }

//    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
//    private void tooLateDateAlert(Temporal t1, Temporal t2)
//    {
//        System.out.println("toolate:");
//        Alert alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Invalid Date Selection");
//        alert.setHeaderText("Start must be before end");
//        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is not before" + System.lineSeparator() + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
//        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeOk);
//        alert.showAndWait();
//    }
}
