package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.component.time.PropertyDateTime;

/**
 * DTSTART
 * Date-Time Start (for local date time only)
 * RFC 5545, 3.8.2.4, page 97
 * 
 * This property specifies when the calendar component begins.
 * 
 * Example:
 * DTSTART;20160306T043000
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VFreeBusy
 */
public class DTStartLocalDateTime extends PropertyDateTime<DTStartLocalDateTime>
{
    public DTStartLocalDateTime(LocalDateTime temporal)
    {
        super(temporal);
    }

    public DTStartLocalDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartLocalDateTime(DTStartLocalDateTime source)
    {
        super(source);
    }
    
    public DTStartLocalDateTime()
    {
        super();
    }
}
