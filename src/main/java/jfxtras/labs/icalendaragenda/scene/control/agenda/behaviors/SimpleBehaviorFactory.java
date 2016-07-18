package jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;

/**
 * Simply factory to return correct behavior by {@link VComponent} class
 * 
 * @author David Bal
 *
 */
public class SimpleBehaviorFactory
{
    public static AppointmentChangeBehavior newBehavior(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new VEventBehavior();
        } else if (vComponent instanceof VTodo)
        {
            return new VTodoBehavior();
        } else if (vComponent instanceof VJournal)
        {
            return new VJournalBehavior();
        } else if (vComponent instanceof VFreeBusy)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VTimeZone)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof VAlarm)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof StandardTime)
        {
            throw new RuntimeException("not implemented");
        } else if (vComponent instanceof DaylightSavingTime)
        {
            throw new RuntimeException("not implemented");
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
}
