package jfxtras.labs.icalendarfx.components.deleters;

import java.time.temporal.Temporal;
import java.util.Map;

import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;

/**
 * Simple factory to create {@link Deleter} objects.  Two methods to create scenes
 * exist.  One takes only a VComponent as a parameter and builds an empty {@link Deleter}.
 * The second takes a VComponent and an array of parameters required to completely
 * initialize the {@link Deleter}.<br>
 * <br>
 * The types of objects in the params array are as follows:<br>
 * VComponent - component to edit<br>
 * Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption> - callback for user dialog<br>
 * Temporal - startOriginalRecurrence, start of selected recurrence<br>
 * 
 * @author David Bal
 *
 */
public class SimpleDeleterFactory
{
    public static Deleter newDeleter (VComponent vComponent, Object[] params)
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent)
                    .withVCalendar((VCalendar) params[0]) // can be null
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[1])
                    .withStartOriginalRecurrence((Temporal) params[2]);
        } else if (vComponent instanceof VTodo)
        {
            return new DeleterVTodo((VTodo) vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return new DeleterVJournal((VJournal) vComponent);            
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
    
    public static Deleter newDeleter (VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent);
        } else if (vComponent instanceof VTodo)
        {
            return new DeleterVTodo((VTodo) vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return new DeleterVJournal((VJournal) vComponent);            
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
