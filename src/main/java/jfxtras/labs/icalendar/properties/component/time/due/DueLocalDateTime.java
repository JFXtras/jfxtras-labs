package jfxtras.labs.icalendar.properties.component.time.due;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.component.time.PropertyDateTime;

/**
 * DUE
 * Date-Time Due (for local-date time)
 * RFC 5545, 3.8.2.3, page 96
 * 
 * This property defines the date and time that a to-do is expected to be completed.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DUE;19960401T150000
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VTodo
 */
public class DueLocalDateTime extends PropertyDateTime<DueLocalDateTime>
{
    public DueLocalDateTime(LocalDateTime temporal)
    {
        super(temporal);
    }

    public DueLocalDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DueLocalDateTime(DueLocalDateTime source)
    {
        super(source);
    }
}
