package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class VComponentFromAppointmentStore implements VComponentStore
{
    @Override
    public VComponent<?> createVComponent(Appointment appointment, VCalendar vCalendar)
    {
        final VComponent<?> newVComponent;
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
            vCalendar.getVEvents().add((VEvent) newVComponent);
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
            vCalendar.getVTodos().add((VTodo) newVComponent);
        }
        return newVComponent;
    }
}
