package jfxtras.labs.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;

/**
 * RDATE
 * Recurrence Date-Times
 * RFC 5545 iCalendar 3.8.5.2, page 120.
 * 
 * This property defines the list of DATE-TIME values for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
 * 
 * @author David Bal
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class Recurrences<T extends Temporal> extends PropertyBaseRecurrence<T, Recurrences<T>>
{       
    public Recurrences(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    @SuppressWarnings("unchecked")
    public Recurrences(T...temporals)
    {
        super(FXCollections.observableSet(temporals));
    }

    public Recurrences(ObservableSet<T> value)
    {
        super(value);
    }
}
