package jfxtras.labs.icalendarfx.components.revisors;

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

public class SimpleRevisorFactory
{
    /** New reviser with all parameters packaged in an array */
    public static Reviser newReviser(VComponent vComponent, Object[] params)
    {
        if (vComponent instanceof VEvent)
        {
            return new ReviserVEvent((VEvent) vComponent)
//                    .withVCalendar((VCalendar) params[0]) // can be null
                    .withVComponents((List<VEvent>) params[0]) // can be null
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[1])
                    .withEndRecurrence((Temporal) params[2])
                    .withStartOriginalRecurrence((Temporal) params[3])
                    .withStartRecurrence((Temporal) params[4])
                    .withVComponentEdited((VEvent) params[5])
                    .withVComponentOriginal((VEvent) params[6]);
        } else if (vComponent instanceof VTodo)
        {
            return new ReviserVTodo((VTodo) vComponent)
                    .withVComponents((List<VTodo>) params[0]) // can be null
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[1])
                    .withEndRecurrence((Temporal) params[2])
                    .withStartOriginalRecurrence((Temporal) params[3])
                    .withStartRecurrence((Temporal) params[4])
                    .withVComponentEdited((VTodo) params[5])
                    .withVComponentOriginal((VTodo) params[6]);
        } else if (vComponent instanceof VJournal)
        {
            // Note: array is different - endRecurrence is omitted
            return new ReviserVJournal((VJournal) vComponent)
                    .withVComponents((List<VJournal>) params[0]) // can be null
                    .withDialogCallback((Callback<Map<ChangeDialogOption, Pair<Temporal, Temporal>>, ChangeDialogOption>) params[1])
                    .withStartOriginalRecurrence((Temporal) params[2])
                    .withStartRecurrence((Temporal) params[3])
                    .withVComponentEdited((VJournal) params[4])
                    .withVComponentOriginal((VJournal) params[5]);
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
    
    /** New reviser without parameters */
    public static Reviser newReviser(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return new ReviserVEvent((VEvent) vComponent);
        } else if (vComponent instanceof VTodo)
        {
            return new ReviserVTodo((VTodo) vComponent);            
        } else if (vComponent instanceof VJournal)
        {
            return new ReviserVJournal((VJournal) vComponent);            
        } else if (vComponent instanceof VFreeBusy)
        {
            return new ReviserVFreeBusy((VFreeBusy) vComponent);            
        } else if (vComponent instanceof VTimeZone)
        {
            return new ReviserVTimeZone((VTimeZone) vComponent);            
        } else if (vComponent instanceof VAlarm)
        {
            return new ReviserVAlarm((VAlarm) vComponent);            
        } else if (vComponent instanceof StandardTime)
        {
            return new ReviserStandardTime((StandardTime) vComponent);            
        } else if (vComponent instanceof DaylightSavingTime)
        {
            return new ReviserDaylightSavingTime((DaylightSavingTime) vComponent);            
        } else
        {
            throw new RuntimeException("Unsupported VComponent type" + vComponent.getClass());
        }
    }
}
