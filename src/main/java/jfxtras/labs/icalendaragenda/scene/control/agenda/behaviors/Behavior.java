package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.CreateEditComponentPopupScene;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/** Encapsulates the edit behavior of VComponents
 * Creates component popups and synchronizes changes from Appointment to VComponent
 * 
 * @author David Bal
 *
 */
public interface Behavior
{
    /** creates edit component popup scene */
    CreateEditComponentPopupScene getEditScene(Appointment appointment);

    /** handles calling Revisor for correct VComponent associated with appointment */
    void callRevisor(Appointment appointment);
}
