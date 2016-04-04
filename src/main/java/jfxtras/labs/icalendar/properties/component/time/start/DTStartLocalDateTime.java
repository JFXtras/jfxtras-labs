package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.properties.PropertyBase;

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
 */
public class DTStartLocalDateTime extends PropertyBase<DTStartLocalDateTime, LocalDateTime>
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
