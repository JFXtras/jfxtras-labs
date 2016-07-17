package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;

public abstract class DisplayableBehavior<T extends VComponentDisplayable<T>> implements AppointmentChangeBehavior
{
    ICalendarAgenda agenda;
    
    public DisplayableBehavior(ICalendarAgenda agenda) { this.agenda = agenda; }

}
