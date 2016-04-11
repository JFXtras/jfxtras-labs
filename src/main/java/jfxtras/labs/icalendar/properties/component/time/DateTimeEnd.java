package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.properties.PropertyBaseDateTime;

/**
 * DTEND
 * Date-Time End (for local-date)
 * RFC 5545, 3.8.2.2, page 95
 * 
 * This property specifies when the calendar component ends.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DTEND;VALUE=DATE:19980704
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VFreeBusy
 */
public class DateTimeEnd<T extends Temporal> extends PropertyBaseDateTime<T, DateTimeEnd<T>>
{    
   public DateTimeEnd(T temporal)
    {
        super(temporal);
    }

    public DateTimeEnd(Class<T> clazz, CharSequence contentLine)
    {
        super(clazz, contentLine);
    }
    
    public DateTimeEnd(DateTimeEnd<T> source)
    {
        super(source);
    }
}
