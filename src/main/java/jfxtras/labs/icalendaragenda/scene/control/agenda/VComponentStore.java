package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.util.List;

import jfxtras.labs.icalendarfx.VCalendar;

public interface VComponentStore<V, R>
{
    V createVComponent(R recurrence, VCalendar vCalendar);
    
    /**
     * Makes appointments from VEVENT or VTODO for Agenda
     * Appointments are made between displayed range
     * 
     * @param vComponent - calendar component
     * @return created appointments
     */
    List<R> makeRecurrences(V vComponent);

}
