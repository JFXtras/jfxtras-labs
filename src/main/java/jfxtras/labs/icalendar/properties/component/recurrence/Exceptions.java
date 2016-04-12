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
 * RFC 5545 iCalendar 3.8.5.1, page 117.
 * 
 * This property defines the list of DATE-TIME exceptions for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class Exceptions<T extends Temporal> extends PropertyBaseRecurrence<T, Exceptions<T>>
{       
    public Exceptions(CharSequence contentLine)
    {
        super(contentLine);
    }

    public Exceptions(ObservableSet<T> value)
    {
        super(value);
    }
}