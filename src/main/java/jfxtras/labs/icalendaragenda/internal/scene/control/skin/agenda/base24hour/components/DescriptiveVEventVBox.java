package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.time.DurationProp;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DescriptiveVEventVBox extends DescriptiveLocatableVBox<VEvent>
{
    public DescriptiveVEventVBox()
    {
        super();
        endLabel.setText(getResources().getString("end.time"));
    }
    
    @Override
    public void setupData(
            VEvent vComponent,
            Temporal startRecurrence,
            Temporal endRecurrence,
            List<AppointmentGroup> appointmentGroups)
    // TODO - TRY TO REMOVE appointmentGroups - USE A LISTENER TO MATCH UP CATEGORY TO APPOINTMENT GROUP?
    // USE LIST<STRING> INSTEAD - JUST THE DESCRIPTIONS?
    {
        // Convert duration to date/time end - this controller can't handle VEvents with duration
        if (vComponent.getDuration() != null)
        {
            Temporal end = vComponent.getDateTimeStart().getValue().plus(vComponent.getDuration().getValue());
            vComponent.setDuration((DurationProp) null);
            vComponent.setDateTimeEnd(end);
        }
        
        super.setupData(vComponent, startRecurrence, endRecurrence, appointmentGroups);
    }
    
//    /* If startRecurrence isn't valid due to a RRULE change, changes startRecurrence and
//     * endRecurrence to closest valid values
//     */
//    @Override
//    void validateRecurrenceDates(Temporal oldValue, Temporal newValue)
//    {
//        super.validateRecurrenceDates(oldValue, newValue);
//        if (! vComponentEdited.isRecurrence(startRecurrenceProperty.get()))
//        {
//            TemporalAmount duration = DateTimeUtilities.temporalAmountBetween(oldValue, newValue);
//            LocalDateTime endNew = endDateTimeTextField.getLocalDateTime().plus(duration);
//            endDateTimeTextField.setLocalDateTime(endNew);
//            System.out.println("new end:" + endDateTimeTextField.getLocalDateTime());
//        }
//    }
}
