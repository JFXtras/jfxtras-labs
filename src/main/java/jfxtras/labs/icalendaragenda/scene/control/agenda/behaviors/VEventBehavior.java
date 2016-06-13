package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Collection;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditVEventPopupScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.VEventReviser;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VEventBehavior extends DisplayableBehavior<VEvent>
{
    public VEventBehavior(ICalendarAgenda agenda)
    {
        super(agenda);
    }

    @Override
    public EditComponentPopupScene getEditScene(Appointment appointment)
    {
        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            return new EditVEventPopupScene(
                    vComponent,
                    agenda.getVCalendar().getVEvents(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    agenda.getCategories());
        }
    }

    @Override
    public void callRevisor(Appointment appointment)
    {
        VEvent vComponent = (VEvent) agenda.appointmentVComponentMap().get(System.identityHashCode(appointment));
        VEvent vComponentOriginal = new VEvent(vComponent);
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
        } else
        {
            Temporal startOriginalRecurrence = agenda.appointmentStartOriginalMap().get(System.identityHashCode(appointment));
            System.out.println("startOriginalRecurrence:" + startOriginalRecurrence);
            final Temporal startRecurrence;
            final Temporal endRecurrence;

            boolean wasDateType = DateTimeType.of(startOriginalRecurrence).equals(DateTimeType.DATE);
            boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
            boolean isChangedToTimeBased = wasDateType && isNotDateType;
            boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
            if (isChangedToTimeBased)
            {
                startRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
                endRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
            } else if (isChangedToWholeDay)
            {
                startRecurrence = LocalDate.from(appointment.getStartTemporal());
                Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
                endRecurrence = (endInstanceTemp.equals(startRecurrence)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
            } else
            {
                startRecurrence = appointment.getStartTemporal();
                endRecurrence = appointment.getEndTemporal();            
            }
            
            VEventReviser newRevisor = (VEventReviser) vComponent.newRevisor();
            newRevisor.withDialogCallback(EditChoiceDialog.EDIT_DIALOG_CALLBACK)
                    .withEndRecurrence(endRecurrence)
                    .withStartOriginalRecurrence(startOriginalRecurrence)
                    .withStartRecurrence(startRecurrence)
                    .withVComponentEdited(vComponent)
                    .withVComponentOriginal(vComponentOriginal);
            Collection<VEvent> newVComponents = newRevisor.revise();
            agenda.getVCalendar().getVEvents().remove(vComponent);
            agenda.getVCalendar().getVEvents().addAll(newVComponents);
        }
        
    }
}
