package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper;
import jfxtras.labs.icalendarfx.components.VComponentLocatableBase;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.TemporalUtilities;

/**
 * Added dateTimeEnd or dateTimeDue
 * 
 * @author David Bal
 *
 * @param <T> subclass for VEvent or VTodo
 */
public abstract class DescriptiveLocatableVBox<T extends VComponentLocatableBase<?>> extends DescriptiveVBox<T>
{
    protected LocalDateTimeTextField endDateTimeTextField = new LocalDateTimeTextField(); // end of recurrence
    protected LocalDateTextField endDateTextField = new LocalDateTextField(); // end of recurrence when wholeDayCheckBox is selected
    
    public DescriptiveLocatableVBox()
    {
        super();
        endDateTimeTextField.setId("endDateTimeTextField");
        endDateTextField.setId("endDateTextField");
    }

    @Override
    @FXML void handleSave()
    {
        super.handleSave();
        System.out.println("start edit:" + startRecurrence + " " + endRecurrence);
        Collection<T> newVComponents = ReviseComponentHelper.handleEdit(
                vComponentEdited,
                vComponentOriginalCopy,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
                EditChoiceDialog.EDIT_DIALOG_CALLBACK
                );
        vComponents.addAll(newVComponents);
        System.out.println("newVComponents:" + newVComponents.size());
        System.out.println("contnen:" + vComponentEdited.toContent());
    }
    
    @Override
    void synchStartDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        super.synchStartDateTime(oldValue, newValue);
        LocalDateTime end = endDateTimeTextField.getLocalDateTime();
        if ((oldValue != null) && (end != null))
        {
            TemporalAmount duration = Duration.between(oldValue, end);
            endDateTimeTextField.setLocalDateTime(newValue.plus(duration));
        }
    }
    
    @Override
    void synchStartDate(LocalDate oldValue, LocalDate newValue)
    {
        super.synchStartDate(oldValue, newValue);
        LocalDate end = endDateTextField.getLocalDate();
        if ((oldValue != null) && (end != null))
        {
            TemporalAmount duration = Period.between(oldValue, end);
            endDateTextField.setLocalDate(newValue.plus(duration));
        }
    }
    
    final private ChangeListener<? super LocalDate> endDateTextListener = (observable, oldValue, newValue) -> synchEndDate(oldValue, newValue);

    /** Update endDateTimeTextField when endDateTextField changes */
    void synchEndDate(LocalDate oldValue, LocalDate newValue)
    {
        endRecurrence = newValue;
        endDateTimeTextField.localDateTimeProperty().removeListener(endDateTimeTextListener);
        LocalDateTime newDateTime = endDateTimeTextField.getLocalDateTime().with(newValue.minusDays(1));
        endDateTimeTextField.setLocalDateTime(newDateTime);
        endDateTimeTextField.localDateTimeProperty().addListener(endDateTimeTextListener);
    }

    final private ChangeListener<? super LocalDateTime> endDateTimeTextListener = (observable, oldValue, newValue) -> synchEndDateTime(oldValue, newValue);

    /** Update endDateTextField when endDateTimeTextField changes */
    void synchEndDateTime(LocalDateTime oldValue, LocalDateTime newValue)
    {
        endRecurrence = newValue;
        endDateTextField.localDateProperty().removeListener(endDateTextListener);
        LocalDate newDate = LocalDate.from(endDateTimeTextField.getLocalDateTime()).plusDays(1);
        endDateTextField.setLocalDate(newDate);
        endDateTextField.localDateProperty().addListener(endDateTextListener);
    }

    private Temporal endRecurrence; // bound to endTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in endTextListener
    private Temporal endRecurrenceOriginal;

    @Override
    public void setupData(
            Appointment appointment,
            T vComponent,
            List<T> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
        endRecurrenceOriginal = appointment.getEndTemporal();
        
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
//            endOrDueProperty.set(end);
//            vComponent.setDateTimeEnd(end);
        }
        
        if (vComponent.getDescription() == null)
        {
            vComponent.setDescription(Description.parse(""));
        }
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescription().valueProperty());
        if (vComponent.getLocation() == null)
        {
            vComponent.setLocation(Location.parse(""));
        }
        locationTextField.textProperty().bindBidirectional(vComponent.getLocation().valueProperty());
        
        /*
         * END DATE/TIME
         */
        endDateTimeTextField.setLocale(Locale.getDefault());
        final LocalDateTime endDateTime;
        final LocalDate endDate;
        if (endRecurrenceOriginal.isSupported(ChronoUnit.NANOS))
        {
            endDateTime = TemporalUtilities.toLocalDateTime(endRecurrenceOriginal);
            endDate = LocalDate.from(endRecurrenceOriginal).plusDays(1);
        } else
        {
            endDateTime = LocalDate.from(startOriginalRecurrence).atTime(defaultStartTime);
            endDate = LocalDate.from(endRecurrenceOriginal);
        }
        endDateTimeTextField.setLocalDateTime(endDateTime);
        endDateTimeTextField.setParseErrorCallback(errorCallback);
        endDateTextField.setLocalDate(LocalDate.from(endDate));
        endDateTextField.setParseErrorCallback(errorCallback);
        
        // Ensure end date/time is not before start date/time
        endDateTimeTextField.localDateTimeProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if ((startDateTimeTextField.getLocalDateTime() != null) && newSelection.isBefore(startDateTimeTextField.getLocalDateTime()))
            {
                tooEarlyDateAlert(newSelection, startDateTimeTextField.getLocalDateTime());
                endDateTimeTextField.setLocalDateTime(oldSelection);
            }
        });
        // Ensure end date is not before start date
        endDateTextField.localDateProperty().addListener((observable, oldSelection, newSelection) ->
        {
            if ((startDateTextField.getLocalDate() != null) && newSelection.minusDays(1).isBefore(startDateTextField.getLocalDate()))
            {
                tooEarlyDateAlert(newSelection, startDateTextField.getLocalDate());
                endDateTextField.setLocalDate(oldSelection);
            }
        });
    }
    
//    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
//    private void tooEarlyDateAlert(LocalDateTime t1, LocalDateTime t2)
//    {
//        Alert alert = new Alert(AlertType.ERROR);
//        alert.setTitle("Invalid Date Selection");
//        alert.setHeaderText("End must be after start");
//        alert.setContentText(Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t1) + " is not after" + System.lineSeparator() + Settings.DATE_FORMAT_AGENDA_EXCEPTION.format(t2));
//        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
//        alert.getButtonTypes().setAll(buttonTypeOk);
//        alert.showAndWait();
//    }
    
    // Displays an alert notifying UNTIL date is not an occurrence and changed to 
    private void tooEarlyDateAlert(Temporal t1, Temporal t2)
    {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Invalid Date Selection");
        alert.setHeaderText("End must be after start");
        alert.setContentText(t1 + " is not after" + System.lineSeparator() + t2);
        ButtonType buttonTypeOk = new ButtonType("OK", ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeOk);
        alert.showAndWait();
    }
    
    @Override
    void handleWholeDayChange(T vComponent, Boolean newSelection)
    {
        endDateTimeTextField.localDateTimeProperty().removeListener(endDateTimeTextListener);
        endDateTextField.localDateProperty().removeListener(endDateTextListener);
        super.handleWholeDayChange(vComponent, newSelection);
        if (newSelection)
        {
            timeGridPane.getChildren().remove(endDateTimeTextField);
            timeGridPane.add(endDateTextField, 1, 1);
        } else
        {
            timeGridPane.getChildren().remove(endDateTextField);
            timeGridPane.add(endDateTimeTextField, 1, 1);
        }
        endDateTextField.localDateProperty().addListener(endDateTextListener);
        endDateTimeTextField.localDateTimeProperty().addListener(endDateTimeTextListener);
    }
    
    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
     * endRecurrence to closest valid values
     */
    @Override
    Runnable validateStartRecurrence()
    {
//        Temporal actualRecurrence = vEvent.streamRecurrences(startRecurrence).findFirst().get();
        if (! vComponentEdited.isRecurrence(startRecurrence))
        {
            Temporal recurrenceBefore = vComponentEdited.previousStreamValue(startRecurrence);
            Optional<Temporal> optionalAfter = vComponentEdited.streamRecurrences(startRecurrence).findFirst();
            Temporal newStartRecurrence = (optionalAfter.isPresent()) ? optionalAfter.get() : recurrenceBefore;
            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(startRecurrence, endRecurrence);
            Temporal newEndRecurrence = newStartRecurrence.plus(duration);
            Temporal startRecurrenceBeforeChange = startRecurrence;
            startDateTimeTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newStartRecurrence));
            endDateTimeTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newEndRecurrence));
            startOriginalRecurrence = startRecurrence;
            return () -> startRecurrenceChangedAlert(startRecurrenceBeforeChange, newStartRecurrence);
        }
        return null;
    }
}
