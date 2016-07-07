package jfxtras.labs.icalendarfx.components.revisors;

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
