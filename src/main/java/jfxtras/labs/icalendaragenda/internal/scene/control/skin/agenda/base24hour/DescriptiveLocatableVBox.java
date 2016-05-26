package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.TemporalUtilities;

public abstract class DescriptiveLocatableVBox<T extends VComponentLocatable<?>> extends DescriptiveVBox<T>
{
    private ObjectProperty<Temporal> endOrDueProperty;
    private Temporal endRecurrence; // bound to endTextField, but adjusted to be DateTimeType identical to VComponent DTSTART, updated in endTextListener
    private Temporal endRecurrenceOriginal;
    
    private final ChangeListener<? super LocalDateTime> endTextlistener = (observable, oldSelection, newSelection) ->
    {
        if ((startTextField.getLocalDateTime() != null) && newSelection.isBefore(startTextField.getLocalDateTime()))
        {
            tooEarlyDateAlert(newSelection, startTextField.getLocalDateTime());
            endTextField.setLocalDateTime(oldSelection);
        }
        if (wholeDayCheckBox.isSelected())
        {
            endRecurrence = LocalDate.from(endTextField.getLocalDateTime());
        } else
        {
//            endRecurrence = vEvent.getDateTimeType().from(endTextField.getLocalDateTime(), zone);
            endRecurrence = vComponent.getDateTimeStart().getValue().with(endTextField.getLocalDateTime());
        }
    };
    
    public DescriptiveLocatableVBox()
    {
        super();
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

        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            endOrDueProperty.set(end);
//            vComponent.setDateTimeEnd(end);
        }
        
        descriptionTextArea.textProperty().bindBidirectional(vComponent.getDescription().valueProperty());
        
        // END DATE/TIME
        Locale locale = Locale.getDefault();
        endTextField.setLocale(locale);
        endTextField.localDateTimeProperty().addListener(endTextlistener);
//        endTextField.setLocalDateTime(DateTimeType.localDateTimeFromTemporal(endRecurrenceOriginal));
        endTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(endRecurrenceOriginal));
        endTextField.setParseErrorCallback(errorCallback);
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
            startTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newStartRecurrence));
            endTextField.setLocalDateTime(TemporalUtilities.toLocalDateTime(newEndRecurrence));
            startOriginalRecurrence = startRecurrence;
            return () -> startRecurrenceChangedAlert(startRecurrenceBeforeChange, newStartRecurrence);
        }
        return null;
    }
}
