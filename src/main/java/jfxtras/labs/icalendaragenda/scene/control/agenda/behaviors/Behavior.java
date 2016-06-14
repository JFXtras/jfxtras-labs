package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupScene;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public interface Behavior<T extends VComponent<?>>
{
    EditComponentPopupScene getEditScene(Appointment appointment);

    void callRevisor(Appointment appointment);
}
