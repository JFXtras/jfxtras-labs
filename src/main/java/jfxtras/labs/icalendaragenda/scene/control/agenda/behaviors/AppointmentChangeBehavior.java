package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import java.time.temporal.Temporal;
import java.util.List;

import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.CreateEditComponentPopupScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.deleters.Deleter;
import jfxtras.labs.icalendarfx.components.revisors.Reviser;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/** Encapsulates the revise and delete behavior of VComponents
 * Creates component popups and synchronizes changes from Appointment to VComponent
 * 
 * @author David Bal
 *
 */
public interface AppointmentChangeBehavior
{
    /** creates edit component popup scene */
    CreateEditComponentPopupScene getEditPopupScene(ICalendarAgenda agenda, Appointment appointment);

    /** handles calling {@link Reviser} for correct VComponent associated with appointment */
    void callRevisor(ICalendarAgenda agenda, Appointment appointment);

    /** handles calling {@link Deleter} 
     * @param <T>*/
    void callDeleter(VComponent vComponent, List<? extends VComponent> vComponents, Temporal startOriginalRecurrence);

//    void callDeleter(T vComponent, List<T> vComponents, Temporal startOriginalRecurrence);
}
