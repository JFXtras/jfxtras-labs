package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.components.VTodo;

/**
 * COMPLETED
 * Date-Time Completed
 * RFC 5545, 3.8.2.1, page 97
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
public class DateTimeCompleted extends PropertyUTCTime<DateTimeCompleted>
{
    public DateTimeCompleted(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeCompleted(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeCompleted(DateTimeCompleted source)
    {
        super(source);
    }
    
    public DateTimeCompleted()
    {
        super();
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
