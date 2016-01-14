package jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.controller;


import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

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
    private VEvent<Appointment> vEventOriginal;
    private Collection<Appointment> appointments;
    private Collection<VComponent<Appointment>> vComponents;
    private Callback<Collection<VComponent<Appointment>>, Void> vEventWriteCallback;
    private Stage popup;

    /** Indicates how the popup window closed */
//    private ObjectProperty<WindowCloseType> popupCloseType; // default to X, meaning click on X to close window
    
    @FXML private ResourceBundle resources; // ResourceBundle that was given to the FXMLLoader
    // TODO - TRY STACK PANE TO REPLACE LocalDateTimeTextField WITH LocalDateTextField WHEN WHOLE DAY
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
    @FXML private Tab repeatableTab;
    
    private LocalDateTime lastDateTimeStart = null;
//    private LocalDateTime lastDateTimeEnd = null;

    public static final long DEFAULT_DURATION = 3600L * Duration.ofSeconds(1).toNanos();
    private LocalTime lastStartTime = LocalTime.of(10, 0); // default time
    private long lastDuration = DEFAULT_DURATION; // Default to one hour duration
    
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
            System.out.println("newDateTimeEnd:" + newDateTimeEnd);
            vEvent.setDateTimeEnd(newDateTimeEnd);
        }
    };
    private final ChangeListener<? super LocalDateTime> startTextListener = (observable, oldSelection, newSelection) ->
    {
//        System.out.println("old start-controller:" + vEvent.getDateTimeStart() + " " + oldSelection + " " + newSelection);
        Temporal newDateTimeStart = adjustStartEndTemporal(
                vEvent.getDateTimeStart()
              , oldSelection
              , newSelection);
        vEvent.setDateTimeStart(newDateTimeStart);
        
        // adjust endTextField (maintain duration)
        LocalDateTime end = endTextField.getLocalDateTime();
        long duration = ChronoUnit.NANOS.between(oldSelection, end);
//        System.out.println("duration:" + duration + " " + end);
//        endTextField.localDateTimeProperty().removeListener(endTextlistener);
        endTextField.setLocalDateTime(newSelection.plus(duration, ChronoUnit.NANOS));
//        endTextField.localDateTimeProperty().addListener(endTextlistener);
    };
    // Change time and shift dates for start and end edits
    private Temporal adjustStartEndTemporal(Temporal input, LocalDateTime oldSelection, LocalDateTime newSelection)
    {
        long dayShift = ChronoUnit.DAYS.between(oldSelection, newSelection);
        System.out.println("adjust:" + input + " " + dayShift);
        if (input instanceof LocalDate)
        {
            return input.plus(dayShift, ChronoUnit.DAYS);
        } else if (input instanceof LocalDateTime)
        {
            LocalTime time = newSelection.toLocalTime();
            return LocalDate.from(input)
                    .atTime(time)
                    .plus(dayShift, ChronoUnit.DAYS);
        } else throw new DateTimeException("Illegal Temporal type (" + input.getClass().getSimpleName() + ").  Only LocalDate and LocalDateTime are supported)");
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
        this.vEventWriteCallback = vEventWriteCallback;
        vEvent = (VEvent<Appointment>) vComponent;

        // Convert duration to date/time end
        if (vEvent.getDurationInNanos() != null)
        {
            final Temporal end;
            if (vEvent.getDateTimeStart() instanceof LocalDate)
            {
                long days = vEvent.getDurationInNanos() / VComponent.NANOS_IN_DAY;
                end = vEvent.getDateTimeStart().plus(days, ChronoUnit.DAYS);                
            } else if (vEvent.getDateTimeStart() instanceof LocalDateTime)
            {
                end = vEvent.getDateTimeStart().plus(vEvent.getDurationInNanos(), ChronoUnit.NANOS);
            } else throw new DateTimeException("Illegal Temporal type (" + vEvent.getDateTimeStart().getClass().getSimpleName() + ").  Only LocalDate and LocalDateTime are supported)");
            Long l = null;
            vEvent.setDurationInNanos(l);
            vEvent.setDateTimeEnd(end);
        }
        
        // Copy original VEvent
        vEventOriginal = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent);
        
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
                lastStartTime = lastDateTimeStart.toLocalTime();
                lastDuration = ChronoUnit.NANOS.between(lastDateTimeStart, endTextField.getLocalDateTime());
                LocalDate newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart());
                vEvent.setDateTimeStart(newDateTimeStart);
                LocalDate newDateTimeEnd = LocalDate.from(vEvent.getDateTimeEnd()).plus(1, ChronoUnit.DAYS);
                vEvent.setDateTimeEnd(newDateTimeEnd);
                
                LocalDateTime start = LocalDate.from(startTextField.getLocalDateTime()).atStartOfDay();
                startTextField.setLocalDateTime(start);
                LocalDateTime end = LocalDate.from(endTextField.getLocalDateTime()).plus(1, ChronoUnit.DAYS).atStartOfDay();
                endTextField.setLocalDateTime(end);

                LocalDate newStartRange = LocalDate.from(vEvent.getStartRange());
                LocalDate newEndRange = LocalDate.from(vEvent.getEndRange());
                vEvent.setStartRange(newStartRange);
                vEvent.setEndRange(newEndRange);
            } else
            {
                final LocalDateTime start;
                if (lastDateTimeStart != null)
                {
                    start = lastDateTimeStart;
                    startTextField.setLocalDateTime(lastDateTimeStart);
                } else
                {
                    start = LocalDate.from(startTextField.getLocalDateTime()).atTime(lastStartTime);
                    vEvent.setDateTimeStart(start);
                    startTextField.setLocalDateTime(start);
                }
                LocalDateTime end = start.plus(lastDuration, ChronoUnit.NANOS);
                endTextField.setLocalDateTime(end);
                
                LocalDateTime newDateTimeStart = LocalDate.from(vEvent.getDateTimeStart()).atTime(lastStartTime);
                LocalDateTime newDateTimeEnd = newDateTimeStart.plus(lastDuration, ChronoUnit.NANOS);
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
        
        // START DATE/TIME
        Locale locale = Locale.getDefault();
        startTextField.setLocale(locale);
        startTextField.setLocalDateTime(dateTimeInstanceStartOriginal);
        startTextField.setParseErrorCallback(errorCallback);
        startTextField.localDateTimeProperty().addListener(startTextListener);
//        vEvent.durationInNanosProperty().addListener((obs, oldValue, newValue) ->
//        {
//            endTextField.localDateTimeProperty().removeListener(endTextlistener);
//            LocalDateTime newEnd = startTextField.getLocalDateTime().plus((long) newValue, ChronoUnit.NANOS);
//            endTextField.setLocalDateTime(newEnd);
//            endTextField.localDateTimeProperty().addListener(endTextlistener);
//        });
        
        // END DATE/TIME
        endTextField.setLocale(locale);
        endTextField.setLocalDateTime(dateTimeInstanceEndOriginal);
        endTextField.setParseErrorCallback(errorCallback);
        endTextField.localDateTimeProperty().addListener(endTextlistener);
//        vEvent.dateTimeEndProperty().addListener((obs, oldValue, newValue) ->
//        {
//            endTextField.localDateTimeProperty().removeListener(endTextlistener);
//            endTextField.setLocalDateTime(VComponent.localDateTimeFromTemporal(newValue));
//            endTextField.localDateTimeProperty().addListener(endTextlistener);
//        });
        
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
    @FXML private void handleSave()
    {
        final RRuleType rruleType = ICalendarUtilities.getRRuleType(vEvent.getRRule(), vEventOriginal.getRRule());
        System.out.println("rrule: " + rruleType);
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            vEvent.setRRule(null);
            vEvent.setRDate(null);
            vEvent.setExDate(null);
        case WITH_NEW_REPEAT:
        case INDIVIDUAL:
        {
            updateAppointments();
            break;
        }
        case WITH_EXISTING_REPEAT:
            if (! vEvent.equals(vEventOriginal)) // if changes occurred
            {
                ChangeDialogOptions changeResponse = ICalendarUtilities.repeatChangeDialog();
                switch (changeResponse)
                {
                case ALL:
                {
                    updateAppointments();
                    break;
                }
                case CANCEL:
                    vEventOriginal.copyTo(vEvent); // return to original vEvent
                    break;
                case THIS_AND_FUTURE:
                {
                    LocalDateTime dateTimeInstanceStartNew = appointment.getStartLocalDateTime();
                    VEvent<Appointment> vEventNew = thisAndFuture(vEvent, vEventOriginal, dateTimeInstanceStartOriginal, dateTimeInstanceStartNew);
                    vComponents.add(vEventNew);
                    if (! vEventNew.isValid()) throw new RuntimeException(vEventNew.makeErrorString());
                    break;
                }
                case ONE:
                {
                    if (wholeDayCheckBox.isSelected())
                    {
                        LocalDate start = LocalDate.from(startTextField.getLocalDateTime());
                        vEvent.setDateTimeStart(start);
                        LocalDate end = LocalDate.from(endTextField.getLocalDateTime());
                        vEvent.setDateTimeEnd(end);
                        System.out.println("s,e:" + start + " " + end);
                    } else
                    {
                        vEvent.setDateTimeStart(startTextField.getLocalDateTime());
                        vEvent.setDateTimeEnd(endTextField.getLocalDateTime());
                    }
                    vEvent.setRRule(null);
                    vEvent.setDateTimeRecurrence(dateTimeInstanceStartOriginal);
    
                  // Add recurrence to original vEvent
                    vEventOriginal.getRRule().getRecurrences().add(dateTimeInstanceStartOriginal);

                    // Check for validity
                    if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
                    if (! vEventOriginal.isValid()) throw new RuntimeException(vEventOriginal.makeErrorString());
                    
                    // Remove old appointments, add back ones
                    vComponents.add(vEventOriginal);
                    Collection<Appointment> appointmentsTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
                    appointmentsTemp.addAll(appointments);
                    appointmentsTemp.removeIf(a -> vEventOriginal.instances().stream().anyMatch(a2 -> a2 == a));
                    vEventOriginal.instances().clear(); // clear vEventOriginal outdated collection of appointments
                    appointmentsTemp.addAll(vEventOriginal.makeInstances()); // make new appointments and add to main collection (added to vEventNew's collection in makeAppointments)
                    vEvent.instances().clear(); // clear vEvent outdated collection of appointments
                    appointmentsTemp.addAll(vEvent.makeInstances()); // add vEventOld part of new appointments
                    appointments.clear();
                    appointments.addAll(appointmentsTemp);
                    System.out.println(vEventOriginal);
                    break;
                }
                default:
                    break;
                }
            }
        }
        if (! vEvent.isValid()) throw new RuntimeException(vEvent.makeErrorString());
        System.out.println(vEvent);
        popup.close();
    }

    private void updateAppointments()
    {
        Collection<Appointment> appointmentsTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        appointmentsTemp.addAll(appointments);
        appointmentsTemp.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
        vEvent.instances().clear(); // clear VEvent's outdated collection of appointments
        appointmentsTemp.addAll(vEvent.makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
        appointments.clear();
        appointments.addAll(appointmentsTemp);
    }
    
    /* This and Future option is complicate so it has been moved into its own method.
     * 
     * Changing this and future instances in VComponent is done by ending the previous
     * VComponent with a UNTIL date or date/time and starting a new VComponent from 
     * the selected instance.  EXDATE, RDATE and RECURRENCES are split between both
     * VComponents.  vEventNew has new settings, vEvent has former settings.
     */
    private VEvent<Appointment> thisAndFuture(
              VEvent<Appointment> vEvent
            , VEvent<Appointment> vEventOld
            , LocalDateTime dateTimeInstanceStartOriginal
            , LocalDateTime dateTimeInstanceStartNew)
    {
        
        VEvent<Appointment> vEventNew = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent);
        vEventOld.copyTo(vEvent);
        
        // Adjust original VEvent
        if (vEvent.getRRule().getCount() != null) vEvent.getRRule().setCount(0);
        LocalDateTime newDateTime = dateTimeInstanceStartOriginal.minusNanos(1);
        vEvent.getRRule().setUntil(newDateTime);
        
        // Adjust new VEvent
        long shift = ChronoUnit.DAYS.between(vEventNew.getDateTimeStart(), dateTimeInstanceStartNew);
        Temporal dtEnd = vEventNew.getDateTimeEnd().plus(shift, ChronoUnit.DAYS);
        vEventNew.setDateTimeEnd(dtEnd);
        vEventNew.setDateTimeStart(dateTimeInstanceStartNew);
        vEventNew.setUniqueIdentifier();
        vEventNew.setRelatedTo(vEvent.getUniqueIdentifier());
        
        // Split EXDates dates between this and newVEvent
        if (vEventNew.getExDate() != null)
        {
            vEventNew.getExDate().getTemporals().clear();
            final Iterator<Temporal> exceptionIterator = vEvent.getExDate().getTemporals().iterator();
            while (exceptionIterator.hasNext())
            {
                Temporal d = exceptionIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, dateTimeInstanceStartNew);
//                if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                if (result < 0)
                {
                    exceptionIterator.remove();
                } else {
                    vEventNew.getExDate().getTemporals().add(d);
                }
            }
            if (vEventNew.getExDate().getTemporals().isEmpty()) vEventNew.setExDate(null);
            if (vEvent.getExDate().getTemporals().isEmpty()) vEvent.setExDate(null);
        }

        // Split recurrence date/times between this and newVEvent
        if (vEventNew.getRDate() != null)
        {
            vEventNew.getRDate().getTemporals().clear();
            final Iterator<Temporal> recurrenceIterator = vEvent.getRDate().getTemporals().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                int result = VComponent.TEMPORAL_COMPARATOR.compare(d, dateTimeInstanceStartNew);
//                if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                if (result < 0)
                {
                    recurrenceIterator.remove();
                } else {
                    vEventNew.getRDate().getTemporals().add(d);
                }
            }
            if (vEventNew.getRDate().getTemporals().isEmpty()) vEventNew.setRDate(null);
            if (vEvent.getRDate().getTemporals().isEmpty()) vEvent.setRDate(null);
        }

        // Split instance dates between this and newVEvent
        if (vEventNew.getRRule().getRecurrences() != null)
        {
            vEventNew.getRRule().getRecurrences().clear();
            final Iterator<Temporal> recurrenceIterator = vEvent.getRRule().getRecurrences().iterator();
            while (recurrenceIterator.hasNext())
            {
                Temporal d = recurrenceIterator.next();
                if (VComponent.isBefore(d, dateTimeInstanceStartNew))
                {
                    recurrenceIterator.remove();
                } else {
                    vEventNew.getRRule().getRecurrences().add(d);
                }
            }
        }
        
        // Modify COUNT for this (the edited) vEventNew
        if (vEventNew.getRRule().getCount() > 0)
        {
            final int newCount = (int) vEventNew.instances()
                    .stream()
                    .map(a -> a.getStartLocalDateTime())
                    .filter(d -> ! d.isBefore(dateTimeInstanceStartNew))
                    .count();
            vEventNew.getRRule().setCount(newCount);
        }

        // Remove old appointments, add back ones
        Collection<Appointment> appointmentsTemp = new ArrayList<>(); // use temp array to avoid unnecessary firing of Agenda change listener attached to appointments
        appointmentsTemp.addAll(appointments);
        appointmentsTemp.removeIf(a -> vEvent.instances().stream().anyMatch(a2 -> a2 == a));
        vEvent.instances().clear(); // clear vEvent outdated collection of appointments
        appointmentsTemp.addAll(vEvent.makeInstances()); // make new appointments and add to main collection (added to vEventNew's collection in makeAppointments)
        vEventNew.instances().clear(); // clear vEventNew's outdated collection of appointments
        appointmentsTemp.addAll(vEventNew.makeInstances()); // add vEventOld part of new appointments
        appointments.clear();
        appointments.addAll(appointmentsTemp);
        
        return vEventNew;
    }
    
    @FXML private void handleRepeatSave()
    {
        // adjust DTSTART if first occurrence is not equal to it
        Temporal t1 = vEvent.stream(vEvent.getDateTimeStart()).findFirst().get();
        final Temporal start;
        if (vEvent.getExDate() != null)
        {            
            Temporal t2 = Collections.min(vEvent.getExDate().getTemporals(), VComponent.TEMPORAL_COMPARATOR);
            start = (VComponent.isBefore(t1, t2)) ? t1 : t2;
        } else
        {
          start = t1;
        }
        long dayShift = ChronoUnit.DAYS.between(vEvent.getDateTimeStart(), start);
        System.out.println("dayShift:" + dayShift + " " + start + " " + vEvent.getDateTimeEnd());
        if (dayShift > 0)
        {
            vEvent.setDateTimeStart(start);
            long dayShift2 = ChronoUnit.DAYS.between(vEventOriginal.getDateTimeStart(), vEvent.getDateTimeStart());
            Temporal end = vEvent.getDateTimeEnd().plus(dayShift2, ChronoUnit.DAYS);
            vEvent.setDateTimeEnd(end);
        }
        handleSave();
    }
    
    @FXML private void handleCancelButton()
    {
        popup.close();
    }

    @FXML private void handleDeleteButton()
    {
        Temporal dateOrDateTime = (appointment.isWholeDay()) ? 
                appointment.getStartLocalDateTime().toLocalDate()
              : appointment.getStartLocalDateTime();
                
//        final ICalendarUtilities.WindowCloseType result = vEvent.delete( // TODO - FIX THIS - MOVE METHOD HERE
//                dateOrDateTime
////              , appointments
//              , vComponents
//              , a -> ICalendarUtilities.repeatChangeDialog()
//              , a -> ICalendarUtilities.confirmDelete(a)
//              , vEventWriteCallback);

        popup.close();
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
}
