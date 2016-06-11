package jfxtras.labs.icalendaragenda.scene.control.agenda.behavior;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupStage;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public interface Behavior
{
    void iCalendarEditPopup(Appointment appointment);
    
    EditComponentPopupStage<?> editPopup(Appointment appointment);
}
