package jfxtras.labs.icalendar.properties.component.time;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndLocalDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartLocalDateTime;

/**
 * Abstract class for all local-date-time classes
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DTStartLocalDateTime
 * @see DTEndLocalDateTime
 * @see RecurrenceIDLocalDateTime
 */
@Deprecated
public abstract class PropertyBaseDateTime<U> extends PropertyBase<U, LocalDateTime> implements PropertyDateTime<LocalDateTime>
{
    public PropertyBaseDateTime(LocalDateTime temporal)
    {
        super(temporal);
    }

    public PropertyBaseDateTime(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseDateTime(PropertyBaseDateTime<U> source)
    {
        super(source);
    }
}
