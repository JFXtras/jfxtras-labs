package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.LocalDate;

import jfxtras.labs.icalendar.properties.PropertyDate;

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
 */
public class DTStartLocalDate extends PropertyDate<DTStartLocalDate>
{
    public DTStartLocalDate(LocalDate temporal)
    {
        super(temporal);
    }

    public DTStartLocalDate(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartLocalDate(DTStartLocalDate source)
    {
        super(source);
    }
    
    public DTStartLocalDate()
    {
        super();
    }
}
