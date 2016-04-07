package jfxtras.labs.icalendar.properties.component.time.end;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.properties.component.time.ZonedTimeAbstract;

/**
 * DTEND
 * Date-Time End (for UTC and zoned-date time)
 * RFC 5545, 3.8.2.2, page 95
 * 
 * This property specifies when the calendar component ends.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DTEND:19960401T150000Z
 * DTEND;TZID=America/Los_Angeles:20160307T053000
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VFreeBusy
 */
public class DTEndZonedDateTime extends ZonedTimeAbstract<DTEndZonedDateTime>
{
    public DTEndZonedDateTime(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DTEndZonedDateTime(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public DTEndZonedDateTime(DTEndZonedDateTime source)
    {
        super(source);
    }
}
