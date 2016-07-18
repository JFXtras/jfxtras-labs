package jfxtras.labs.icalendarfx.components.deleters;

import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;

import javafx.util.Callback;
import javafx.util.Pair;
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

public class SimpleDeleterFactory
{
    public static Deleter newDeleter (VComponent vComponent, List<Object> args)
    {
        if (vComponent instanceof VEvent)
        {
            return new DeleterVEvent((VEvent) vComponent)
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) args.get(0))
                    .withStartOriginalRecurrence((Temporal) args.get(1));
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
