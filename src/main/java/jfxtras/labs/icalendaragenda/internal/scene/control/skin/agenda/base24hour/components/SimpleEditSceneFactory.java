package jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components;

import java.time.temporal.Temporal;
import java.util.List;

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

/**
 * Simple factory to create {@link EditDisplayableScene} objects.  Two methods to create scenes
 * exist.  One takes only a VComponent as a parameter and builds an empty {@link EditDisplayableScene}.
 * The second takes a VComponent and an array of parameters required to completely
 * initialize the {@link EditDisplayableScene}.<br>
 * <br>
 * The types of objects in the params array are as follows:<br>
 * VComponent - component to edit<br>
 * VCalendar - parent VCalendar<br>
 * Temporal - startRecurrence, start of selected recurrence<br>
 * Temporal - endRecurrence - end of selected recurrence<br>
 * List-String - available category names<br>
 * 
 * @author David Bal
 *
 */
public class SimpleEditSceneFactory
{
    public static EditDisplayableScene newScene (VComponent vComponent, Object[] params)
    {
        if (vComponent instanceof VEvent)
        {
            return new EditVEventScene()
                    .setupData(
                        (VEvent) params[1],         // vComponent - component to edit
                        ((VCalendar) params[0]).getVEvents(),         // vComponents - collection of components that vComponent is a member
                        (Temporal) params[2],       // startRecurrence - start of selected recurrence
                        (Temporal) params[3],       // endRecurrence - end of selected recurrence
                        (List<String>) params[4]    // categories - available category names
                    );
        } else if (vComponent instanceof VTodo)
        {
            return new EditVTodoScene()
                    .setupData(
                        (VTodo) params[0],         // vComponent - component to edit
                        ((VCalendar) params[0]).getVTodos(),         // vComponents - collection of components that vComponent is a member
                        (Temporal) params[2],       // startRecurrence - start of selected recurrence
                        (Temporal) params[3],       // endRecurrence - end of selected recurrence
                        (List<String>) params[4]    // categories - available category names
                    );
           
        } else if (vComponent instanceof VJournal)
        {
            return new EditVJournalScene()
                    .setupData(
                        (VJournal) params[0],         // vComponent - component to edit
                        ((VCalendar) params[0]).getVJournals(),         // vComponents - collection of components that vComponent is a member
                        (Temporal) params[2],       // startRecurrence - start of selected recurrence
                        (Temporal) params[3],       // endRecurrence - end of selected recurrence
                        (List<String>) params[4]    // categories - available category names
                    );
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
    
    public static EditDisplayableScene newScene (VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new EditVEventScene();
        } else if (vComponent instanceof VTodo)
        {
            return new EditVTodoScene();
        } else if (vComponent instanceof VJournal)
        {
            return new EditVJournalScene();
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
