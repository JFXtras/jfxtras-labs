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
//    public Exceptions(CharSequence contentLine)
//    {
//        super(contentLine);
//    }

    @SuppressWarnings("unchecked")
    public Exceptions(T...temporals)
    {
        super(FXCollections.observableSet(temporals));
    }

    public Exceptions(Exceptions<T> source)
    {
        super(source);
    }
    
    public Exceptions(ObservableSet<T> value)
    {
        super(value);
    }
    
    public Exceptions()
    {
        super();
    }

    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static <U extends Temporal> Exceptions<U> parse(String value)
    {
        Exceptions<U> property = new Exceptions<U>();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class Exceptions provided as parameter */
    public static <U extends Temporal> Exceptions<U> parse(Class<U> clazz, String value)
    {
        Exceptions<U> property = new Exceptions<U>();
        property.parseContent(value);
        clazz.cast(property.getValue().iterator().next()); // class check
        return property;
    }
}
