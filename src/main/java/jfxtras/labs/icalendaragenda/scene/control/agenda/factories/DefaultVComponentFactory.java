package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class DefaultVComponentFactory extends VComponentFactory<Appointment>
{
    @Override
    public VComponentDisplayable<?> createVComponent(Appointment appointment)
    {
        final VComponentDisplayable<?> newVComponent;
        ZonedDateTime dtCreated = ZonedDateTime.now(ZoneId.of("Z"));
        boolean hasEnd = appointment.getEndTemporal() != null;
        if (hasEnd)
        {
            newVComponent = new VEvent()
                    .withSummary(appointment.getSummary())
                    .withCategories(appointment.getAppointmentGroup().getDescription())
                    .withDateTimeStart(appointment.getStartTemporal())
                    .withDateTimeEnd(appointment.getEndTemporal())
                    .withDescription(appointment.getDescription())
                    .withLocation(appointment.getLocation())
                    .withDateTimeCreated(dtCreated)
                    .withDateTimeStamp(dtCreated)
                    .withUniqueIdentifier();
        } else
        {
            newVComponent = new VTodo()
                    .withSummary(appointment.getSummary())
                    .withCategories(appointment.getAppointmentGroup().getDescription())
                    .withDateTimeStart(appointment.getStartTemporal())
                    .withDescription(appointment.getDescription())
                    .withLocation(appointment.getLocation())
                    .withDateTimeCreated(dtCreated)
                    .withDateTimeStamp(dtCreated)
                    .withUniqueIdentifier();
        }
        /* Note: If other VComponents are to be supported other tests to determine
         * which type of VComponent the Appointment represents will need to be created.
         */
        return newVComponent;
    }
}
