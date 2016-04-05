package jfxtras.labs.icalendar.properties.component.time.due;

import java.time.LocalDate;

import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.component.time.DateAbstract;

/**
 * DUE
 * Date-Time Due (for local-date)
 * RFC 5545, 3.8.2.3, page 96
 * 
 * This property defines the date and time that a to-do is expected to be completed.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DUE;VALUE=DATE:19980704
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VTodo
 */
public class DueLocalDate extends DateAbstract<DueLocalDate>
{
    public DueLocalDate(LocalDate temporal)
    {
        super(temporal);
    }

    public DueLocalDate(String propertyString)
    {
        super(propertyString);
    }
    
    public DueLocalDate(DueLocalDate source)
    {
        super(source);
    }
}
