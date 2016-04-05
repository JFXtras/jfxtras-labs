package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.component.time.PropertyTimeZone;

/**
 * DTSTART
 * Date-Time Start (for UTC and zoned-date time)
 * RFC 5545, 3.8.2.4, page 97
 * 
 * This property specifies when the calendar component begins.
 * 
 * Example:
 * DTSTART:19980118T073000Z
 * DTSTART;TZID=America/Los_Angeles:20160306T043000
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VFreeBusy
 */
public class DTStartZonedDateTime extends PropertyTimeZone<DTStartZonedDateTime>
{
    public DTStartZonedDateTime(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DTStartZonedDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartZonedDateTime(DTStartZonedDateTime source)
    {
        super(source);
    }
    
//    public DTStartZonedDateTime()
//    {
//        super();
//    }    
}

