package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public final class ICalendarAgendaUtilities
{
    private ICalendarAgendaUtilities() {}

    final public static List<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS = IntStream.range(0, 24)
               .mapToObj(i -> new Agenda.AppointmentGroupImpl()
                     .withStyleClass("group" + i)
                     .withDescription("group" + (i < 10 ? "0" : "") + i))
               .collect(Collectors.toList());
    
    /**
     * Makes appointments from calendar component for Agenda that exist between displayed range
     * 
     * @param component - calendar component
     * @param startRange - start of displayed range
     * @param endRange - end of displayed range
     * @param appointmentGroups - from Agenda
     * @return
     */
    public static List<Appointment> makeAppointments(
            VEvent component,
            LocalDateTime startRange,
            LocalDateTime endRange,
            Collection<AppointmentGroup> appointmentGroups)
    {
        List<Appointment> appointments = new ArrayList<>();
        Boolean isWholeDay = component.getDateTimeStart().getValue() instanceof LocalDate;
        
        // Make start and end ranges in Temporal type that matches DTSTART
//        LocalDateTime startRange = getDateTimeRange().getStartLocalDateTime();
//        LocalDateTime endRange = getDateTimeRange().getEndLocalDateTime();
        final Temporal startRange2;
        final Temporal endRange2;
        if (isWholeDay)
        {
            startRange2 = startRange.toLocalDate();
            endRange2 = endRange.toLocalDate();            
        } else
        {
            startRange2 = component.getDateTimeStart().getValue().with(startRange);
            endRange2 = component.getDateTimeStart().getValue().with(endRange);            
        }
        component.streamRecurrences(startRange2, endRange2).forEach(startTemporal ->
        {
            // calculate date-time end
            final TemporalAmount adjustment;
            if (component.getDuration() != null)
            {
                adjustment = component.getDuration().getValue();
            } else if (component.getDateTimeEnd() != null)
            {
                Temporal dtstart = component.getDateTimeStart().getValue();
                Temporal dtend = component.getDateTimeEnd().getValue();
                if (dtstart instanceof LocalDate)
                {
                    adjustment = Period.between((LocalDate) dtstart, (LocalDate) dtend);                
                } else
                {
                    adjustment = Duration.between(dtstart, dtend);
                }
            } else
            {
                throw new RuntimeException("Either DTEND or DURATION must be set");
            }
            Temporal endTemporal = startTemporal.plus(adjustment);

            /* Find AppointmentGroup
             * control can only handle one category.  Checks only first category
             */
            final AppointmentGroup appointmentGroup;
            if (component.getCategories() != null)
            {
                String firstCategory = component.getCategories().get(0).getValue().get(0);
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
                    .withDescription( (component.getDescription() != null) ? component.getDescription().getValue() : null )
                    .withSummary( (component.getSummary() != null) ? component.getSummary().getValue() : null)
                    .withLocation( (component.getLocation() != null) ? component.getLocation().getValue() : null)
                    .withWholeDay(isWholeDay)
                    .withAppointmentGroup(appointmentGroup);
            appointments.add(appt);   // add appointments to return argument
        });
        return appointments;
    }
}
