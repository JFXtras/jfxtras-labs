package jfxtras.labs.icalendaragenda.scene.control.agenda;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public interface VComponentStore
{
    VComponent<?> createVComponent(Appointment appointment, VCalendar vCalendar);
}
