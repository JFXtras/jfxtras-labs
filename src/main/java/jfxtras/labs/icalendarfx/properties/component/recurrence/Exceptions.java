package jfxtras.labs.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;

import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournalInt;
import jfxtras.labs.icalendarfx.components.VTodoInt;

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
 * @see VTodoInt
 * @see VJournalInt
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
