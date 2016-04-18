package jfxtras.labs.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VTodoInt;
import jfxtras.labs.icalendarfx.properties.PropertyBaseDateTime;

/**
 * DUE
 * Date-Time Due (for local-date)
 * RFC 5545, 3.8.2.3, page 96
 * 
 * This property defines the date and time that a to-do is expected to be completed.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DUE;VALUE=DATE:19980704
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VTodoInt
 */
public class DateTimeDue<T extends Temporal> extends PropertyBaseDateTime<T, DateTimeDue<T>>
{    
   public DateTimeDue(T temporal)
    {
        super(temporal);
    }

    public DateTimeDue(Class<T> clazz, CharSequence contentLine)
    {
        super(clazz, contentLine);
    }
    
    public DateTimeDue(DateTimeDue<T> source)
    {
        super(source);
    }
}
