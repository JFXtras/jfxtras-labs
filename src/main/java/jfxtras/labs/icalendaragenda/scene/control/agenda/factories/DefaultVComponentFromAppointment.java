package jfxtras.labs.icalendaragenda.scene.control.agenda.factories;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.Collection;
import java.util.Optional;

import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public class DefaultVComponentFromAppointment extends VComponentBaseFactory<Appointment>
{
    private Collection<AppointmentGroup> appointmentGroups;

    /** Callback to make Appointment from VComponent and start Temporal */
    @Override
    CallbackTwoParameters<VComponentDisplayable<?>, Temporal, Appointment> recurrenceCallBack()
    {
        return (vComponent, startTemporal) ->
        {
            Boolean isWholeDay = vComponent.getDateTimeStart().getValue() instanceof LocalDate;
            final String description;
            final Temporal endTemporal;
            final String location;
            if (vComponent instanceof VComponentLocatable<?>)
            { // VTODO and VEVENT
                VComponentLocatable<?> vComponentLocatable = (VComponentLocatable<?>) vComponent;
                description = (vComponentLocatable.getDescription() != null) ? vComponentLocatable.getDescription().getValue() : null;
                location = (vComponentLocatable.getLocation() != null) ? vComponentLocatable.getLocation().getValue() : null;
                TemporalAmount adjustment = vComponentLocatable.getActualDuration();
                endTemporal = startTemporal.plus(adjustment);
            } else if (vComponent instanceof VJournal)
            {
                VJournal vJournal = (VJournal) vComponent;
                description = (vJournal.getDescriptions() != null) ? vJournal.getDescriptions().get(0).getValue() : null;
                location = null;
                endTemporal = null;
            } else
            {
                throw new RuntimeException("Unsupported VComponent type:" + vComponent.getClass());
            }
    
            /* Find AppointmentGroup (Agenda's version of CATEGORY)
             * control can only handle one category.  Checks only first category
             */
            final AppointmentGroup appointmentGroup;
            if (vComponent.getCategories() != null)
            {
                String firstCategory = vComponent.getCategories().get(0).getValue().get(0);
                Optional<AppointmentGroup> myGroup = appointmentGroups
                        .stream()
                        .filter(g -> g.getDescription().equals(firstCategory))
                        .findAny();
                appointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
            } else
            {
                appointmentGroup = null;
            }
            
            // Make appointment
            Appointment appt = new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(startTemporal)
                    .withEndTemporal(endTemporal)
                    .withDescription(description)
                    .withSummary( (vComponent.getSummary() != null) ? vComponent.getSummary().getValue() : null)
                    .withLocation(location)
                    .withWholeDay(isWholeDay)
                    .withAppointmentGroup(appointmentGroup);
            return appt;
        };
    }
            
    public DefaultVComponentFromAppointment(Collection<AppointmentGroup> appointmentGroups)
    {
        super();
        this.appointmentGroups = appointmentGroups;
    }

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
        return newVComponent;
    }
}
