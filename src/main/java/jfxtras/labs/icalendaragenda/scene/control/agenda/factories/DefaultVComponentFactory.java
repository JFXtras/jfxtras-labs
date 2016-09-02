package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VDisplayableBase;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Default factory to create a {@link VDisplayableBase} from {@link Appointment}
 * 
 * @author David Bal
 *
 */
public class DefaultVComponentFactory extends VComponentFactory<Appointment>
{
    @Override
    public VDisplayableBase<?> createVComponent(Appointment appointment)
    {
        final VDisplayableBase<?> newVComponent;
        ZonedDateTime dtCreated = ZonedDateTime.now(ZoneId.of("Z"));
        String summary = ((appointment.getSummary() == null) || appointment.getSummary().isEmpty()) ? null : appointment.getSummary();
        String description = ((appointment.getDescription() == null) || appointment.getDescription().isEmpty()) ? null : appointment.getDescription();
        String category = (appointment.getAppointmentGroup() == null) ? null : appointment.getAppointmentGroup().getDescription();
        String location = ((appointment.getLocation() == null) || appointment.getLocation().isEmpty()) ? null : appointment.getLocation();
        Temporal dtstart = (appointment.isWholeDay()) ? LocalDate.from(appointment.getStartTemporal()) : appointment.getStartTemporal();
        Temporal dtend = (appointment.isWholeDay()) ? LocalDate.from(appointment.getEndTemporal()) : appointment.getEndTemporal();
        
        boolean hasEnd = appointment.getEndTemporal() != null;
        if (hasEnd)
        {
            newVComponent = new VEvent()
                    .withSummary(summary)
                    .withCategories(category)
                    .withDateTimeStart(dtstart)
                    .withDateTimeEnd(dtend)
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
                    .withDateTimeStart(dtstart)
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
