package jfxtras.labs.icalendar.properties.component.time.end;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.properties.component.time.PropertyBaseDateTime;

/**
 * DTEND
 * Date-Time End (for local-date time)
 * RFC 5545, 3.8.2.2, page 95
 * 
 * This property specifies when the calendar component ends.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DTEND:19960401T150000
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VFreeBusy
 */
public class DTEndLocalDateTime extends PropertyBaseDateTime<DTEndLocalDateTime>
{
    public DTEndLocalDateTime(LocalDateTime temporal)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(temporal, null);
    }

    public DTEndLocalDateTime(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public DTEndLocalDateTime(DTEndLocalDateTime source)
    {
        super(source);
    }
}
