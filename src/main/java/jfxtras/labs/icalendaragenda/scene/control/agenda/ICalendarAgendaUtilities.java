package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.Duration;
import java.time.LocalDate;
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
    public static List<Appointment> makeAppointments(List<VEvent> vEvents, Temporal startRange, Temporal endRange)
    {
        List<Appointment> appointments = new ArrayList<>();
        vEvents.forEach(component ->
        {
            Boolean isWholeDay = component.getDateTimeStart().getValue() instanceof LocalDate;
            component.streamRecurrences(startRange, endRange).forEach(startTemporal ->
            {
                final TemporalAmount adjustment;
                if (component.getDuration() != null)
                {
                    adjustment = component.getDuration().getValue();
                } else if (component.getDateTimeEnd() != null)
                {
                    adjustment = Duration.between(component.getDateTimeStart().getValue(),
                            component.getDateTimeEnd().getValue());
                } else
                {
                    throw new RuntimeException("Either DTEND or DURATION must be set");
                }
                Temporal endTemporal = startTemporal.minus(adjustment);
//                Optional<AppointmentGroup> myGroup = getAppointmentGroups()
//                        .stream()
////                        .peek(a -> System.out.println(a.getDescription()))
//                        .filter(g -> g.getDescription().equals(getCategories()))
//                        .findFirst();
//                AppointmentGroup getAppointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
                Appointment appt = new Agenda.AppointmentImplTemporal()
                        .withStartTemporal(startTemporal)
                        .withEndTemporal(endTemporal)
                        .withDescription(component.getDescription().getValue())
                        .withSummary(component.getSummary().getValue())
                        .withLocation(component.getLocation().getValue())
                        .withWholeDay(isWholeDay);
//                        .withAppointmentGroup(getAppointmentGroup);
                appointments.add(appt);   // add appointments to return argument
//                instances().add(appt); // add appointments to this object's collection
            });
        });
        return appointments;
    }
}
