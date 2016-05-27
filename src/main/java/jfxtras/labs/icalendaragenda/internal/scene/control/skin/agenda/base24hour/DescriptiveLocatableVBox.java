package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.TemporalUtilities;

public abstract class DescriptiveLocatableVBox<T extends VComponentLocatable<?>> extends DescriptiveVBox<T>
{
    protected LocalDateTimeTextField endDateTimeTextField = new LocalDateTimeTextField();; // end of recurrence
    protected LocalDateTextField endDateTextField = new LocalDateTextField();

    private ObjectProperty<Temporal> endOrDueProperty;
    private Temporal endRecurrence; // bound to endTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in endTextListener
    private Temporal endRecurrenceOriginal;
    
    public DescriptiveLocatableVBox()
    {
        super();
        endDateTimeTextField.setId("endDateTimeTextField");
        endDateTextField.setId("endDateTextField");
//        endLabel = new Label();
//        timeGridPane.add(endLabel, 0, 1);
//        timeGridPane.const
//        endTextField = new LocalDateTimeTextField();
//        endTextField.setId("endTextField");
//        timeGridPane.add(endTextField, 1, 1);
    }

    @Override
    public void setupData(
            Appointment appointment,
            T vComponent,
            List<T> vComponents,
            List<AppointmentGroup> appointmentGroups)
    {
        super.setupData(appointment, vComponent, vComponents, appointmentGroups);
        endRecurrenceOriginal = appointment.getEndTemporal();
        
        /*
         * Replace startTextListener with new version that automatically adjusts end time when start changes (keep duration)
         */
        startTextListener = (observable, oldSelection, newSelection) ->
        {
            LocalDateTime end = endDateTimeTextField.getLocalDateTime();
            if ((oldSelection != null) && (end != null))
            {
                TemporalAmount duration = Duration.between(oldSelection, end);
                endDateTimeTextField.setLocalDateTime(newSelection.plus(duration));
            }
            
            if (wholeDayCheckBox.isSelected())
            {
                startRecurrence = LocalDate.from(startDateTimeTextField.getLocalDateTime());
            } else
            {
                startRecurrence = vComponent.getDateTimeStart().getValue().with(startDateTimeTextField.getLocalDateTime());
            }
        };
        
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            endOrDueProperty.set(end);
//            vComponent.setDateTimeEnd(end);
        }
        
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescription().valueProperty());
        
        final ChangeListener<? super LocalDateTime> endTextlistener = (observable, oldSelection, newSelection) ->
        {
            if ((startDateTimeTextField.getLocalDateTime() != null) && newSelection.isBefore(startDateTimeTextField.getLocalDateTime()))
            {
                tooEarlyDateAlert(newSelection, startDateTimeTextField.getLocalDateTime());
                endDateTimeTextField.setLocalDateTime(oldSelection);
            }
            if (wholeDayCheckBox.isSelected())
            {
                endRecurrence = LocalDate.from(endDateTimeTextField.getLocalDateTime());
            } else
            {
//                endRecurrence = vEvent.getDateTimeType().from(endTextField.getLocalDateTime(), zone);
                endRecurrence = vComponent.getDateTimeStart().getValue().with(endDateTimeTextField.getLocalDateTime());
            }
        };
        
        // END DATE/TIME
        endDateTimeTextField.setLocale(Locale.getDefault());
        endDateTimeTextField.localDateTimeProperty().addListener(endTextlistener);
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
//        ChangeListener<? super LocalDate> startDateTextListener;
//        startDateTextField.localDateProperty().addListener(startDateTextListener);
        endDateTextField.setLocalDate(LocalDate.from(endDate));
        endDateTextField.setParseErrorCallback(errorCallback);
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
    
    @Override
    void handleWholeDayChange(T vComponent, Boolean newSelection)
    {
//        System.out.println("whole day2:" + newSelection);
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
    }
    
    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
     * endRecurrence to closest valid values
     */
    @Override
    Runnable validateStartRecurrence()
    {
//        Temporal actualRecurrence = vEvent.streamRecurrences(startRecurrence).findFirst().get();
        if (! vComponent.isRecurrence(startRecurrence))
        {
            Temporal recurrenceBefore = vComponent.previousStreamValue(startRecurrence);
            Optional<Temporal> optionalAfter = vComponent.streamRecurrences(startRecurrence).findFirst();
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
