package jfxtras.labs.icalendar.properties.component.recurrence;

import java.time.temporal.Temporal;

import javafx.collections.ObservableSet;
import jfxtras.labs.icalendar.components.DaylightSavingTime;
import jfxtras.labs.icalendar.components.StandardTime;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;

/**
 * EXDATE
 * Exception Date-Times
 * RFC 5545 iCalendar 3.8.5.2, page 120.
 * 
 * This property defines the list of DATE-TIME values for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class Recurrences<T extends Temporal> extends PropertyBaseRecurrence<T, Exceptions<T>>
{       
    public Recurrences(CharSequence contentLine)
    {
        super(contentLine);
    }

    public Recurrences(ObservableSet<T> value)
    {
        super(value);
    }
}
