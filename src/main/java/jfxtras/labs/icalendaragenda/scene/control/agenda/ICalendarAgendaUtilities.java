package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

public final class ICalendarAgendaUtilities
{
    private ICalendarAgendaUtilities() {}

    final public static List<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS = IntStream.range(0, 24)
               .mapToObj(i -> new Agenda.AppointmentGroupImpl()
                     .withStyleClass("group" + i)
                     .withDescription("group" + (i < 10 ? "0" : "") + i))
               .collect(Collectors.toList());
    
    final public static List<String> CATEGORIES = IntStream.range(0, 24)
            .mapToObj(i -> new String("group" + i))
            .collect(Collectors.toList());
}
    
  
