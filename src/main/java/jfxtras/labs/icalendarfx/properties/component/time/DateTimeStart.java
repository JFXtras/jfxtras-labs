package jfxtras.labs.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VEventNewInt;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VTodoInt;
import jfxtras.labs.icalendarfx.properties.PropertyBaseDateTime;

/**
 * DTSTART
 * Date-Time Start (for local date only)
 * RFC 5545, 3.8.2.4, page 97
 * 
 * This property specifies when the calendar component begins.
 * 
 * Example:
 * DTSTART;VALUE=DATE:20160307
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEventNewInt
 * @see VTodoInt
 * @see VFreeBusy
 */
public class DateTimeStart<T extends Temporal> extends PropertyBaseDateTime<T,DateTimeStart<T>>
{
   public DateTimeStart(T temporal)
    {
        super(temporal);
    }

    public DateTimeStart(Class<T> clazz, CharSequence contentLine)
    {
        super(clazz, contentLine);
    }
    
    public DateTimeStart(DateTimeStart<T> source)
    {
        super(source);
    }
}
