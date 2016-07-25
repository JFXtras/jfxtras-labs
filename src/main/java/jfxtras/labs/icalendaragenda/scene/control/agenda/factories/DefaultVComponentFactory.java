package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Default factory to create a {@link VComponentDisplayable} from {@link Appointment}
 * 
 * @author David Bal
 *
 */
public class DefaultVComponentFactory extends VComponentFactory<Appointment>
{
    @Override
    public VComponentDisplayable<?> createVComponent(Appointment appointment)
    {
        final VComponentDisplayable<?> newVComponent;
        ZonedDateTime dtCreated = ZonedDateTime.now(ZoneId.of("Z"));
        String summary = ((appointment.getSummary() == null) || appointment.getSummary().isEmpty()) ? null : appointment.getSummary();
        String description = ((appointment.getDescription() == null) || appointment.getDescription().isEmpty()) ? null : appointment.getDescription();
        String category = (appointment.getAppointmentGroup() == null) ? null : appointment.getAppointmentGroup().getDescription();
        String location = ((appointment.getLocation() == null) || appointment.getLocation().isEmpty()) ? null : appointment.getLocation();

        boolean hasEnd = appointment.getEndTemporal() != null;
        if (hasEnd)
        {
            newVComponent = new VEvent()
                    .withSummary(summary)
                    .withCategories(category)
                    .withDateTimeStart(appointment.getStartTemporal())
                    .withDateTimeEnd(appointment.getEndTemporal())
                    .withDescription(description)
                    .withLocation(location)
                    .withDateTimeCreated(dtCreated)
                    .withDateTimeStamp(dtCreated)
                    .withUniqueIdentifier(); // using default UID generator
        } else
        {
            newVComponent = new VTodo()
                    .withSummary(summary)
                    .withCategories(category)
                    .withDateTimeStart(appointment.getStartTemporal())
                    .withDescription(description)
                    .withLocation(location)
                    .withDateTimeCreated(dtCreated)
                    .withDateTimeStamp(dtCreated)
                    .withUniqueIdentifier();
        }
        /* Note: If other VComponents are to be supported then other tests to determine
         * which type of VComponent the Appointment represents will need to be created.
         */
        return newVComponent;
    }
}
