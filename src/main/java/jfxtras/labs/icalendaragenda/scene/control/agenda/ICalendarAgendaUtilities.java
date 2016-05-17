package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;
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
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @return created appointments
     */
    // TODO - MAYBE PUT IT ICALENDAR AGENDA - CAN I LOSE startRange, endRange AND getAppointmentGroups as parameters?
    public static List<Appointment> makeAppointments(VEvent component, LocalDateTime startRange, LocalDateTime endRange)
    {
//        System.out.println("make appointmetns:" + component.getSummary() + " " + component.getDateTimeStart());
        List<Appointment> appointments = new ArrayList<>();
        Boolean isWholeDay = component.getDateTimeStart().getValue() instanceof LocalDate;
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
            System.out.println("startTemporal1:" + startTemporal);
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
//                adjustment = Duration.between(component.getDateTimeStart().getValue(),
//                        component.getDateTimeEnd().getValue());
            } else
            {
                throw new RuntimeException("Either DTEND or DURATION must be set");
            }
            Temporal endTemporal = startTemporal.plus(adjustment);
//                Optional<AppointmentGroup> myGroup = getAppointmentGroups()
//                        .stream()
////                        .peek(a -> System.out.println(a.getDescription()))
//                        .filter(g -> g.getDescription().equals(getCategories()))
//                        .findFirst();
//                AppointmentGroup getAppointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
            Appointment appt = new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(startTemporal)
                    .withEndTemporal(endTemporal)
                    .withDescription( (component.getDescription() != null) ? component.getDescription().getValue() : null )
                    .withSummary( (component.getSummary() != null) ? component.getSummary().getValue() : null)
                    .withLocation( (component.getLocation() != null) ? component.getLocation().getValue() : null)
                    .withWholeDay(isWholeDay);
//                        .withAppointmentGroup(getAppointmentGroup);
            appointments.add(appt);   // add appointments to return argument
        });
        System.out.println("made appointmetns:" + appointments.size());
        return appointments;
    }
}
