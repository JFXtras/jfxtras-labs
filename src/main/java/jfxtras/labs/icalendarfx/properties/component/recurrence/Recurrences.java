package jfxtras.labs.icalendarfx.properties.component.recurrence;

import java.time.temporal.Temporal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEvent;
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
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class Recurrences<T extends Temporal> extends PropertyBaseRecurrence<T, Recurrences<T>>
{       
//    public Recurrences(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    @SuppressWarnings("unchecked")
    public Recurrences(T...temporals)
    {
        super(FXCollections.observableSet(temporals));
    }
    
    public Recurrences(Recurrences<T> source)
    {
        super(source);
    }
    
    public Recurrences(ObservableSet<T> value)
    {
        super(value);
    }
    
    public Recurrences()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static <U extends Temporal> Recurrences<U> parse(String value)
    {
        Recurrences<U> property = new Recurrences<U>();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class Exceptions provided as parameter */
    public static <U extends Temporal> Recurrences<U> parse(Class<U> clazz, String value)
    {
        Recurrences<U> property = new Recurrences<U>();
        property.parseContent(value);
        clazz.cast(property.getValue().iterator().next()); // class check
        return property;
    }
}
