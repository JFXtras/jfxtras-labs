package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.components.VTodo;

/**
 * COMPLETED
 * Date-Time Completed
 * RFC 5545, 3.8.2.1, page 94
 * 
 * This property defines the date and time that a to-do was actually completed.
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * COMPLETED:19960401T150000Z
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VTodo
 */
public class DateTimeCompleted extends PropertyBaseUTCTime<DateTimeCompleted>
{
    public DateTimeCompleted(ZonedDateTime temporal)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(temporal, null);
    }

    public DateTimeCompleted(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public DateTimeCompleted(DateTimeCompleted source)
    {
        super(source);
    }

    @Override
    public boolean isValid()
    {
        if (! getValue().getZone().equals(ZoneId.of("Z")))
        {
            return false;
        }
        return super.isValid();
    }
    
}
