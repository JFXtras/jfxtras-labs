package jfxtras.labs.icalendarfx.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.properties.PropertyBaseDateTime;

/**
 * DTEND
 * Date-Time End (for local-date)
 * RFC 5545, 3.8.2.2, page 95
 * 
 * This property specifies when the calendar component ends.
 * 
 * The value type of this property MUST be the same as the "DTSTART" property, and
 * its value MUST be later in time than the value of the "DTSTART" property.
 * 
 * Example:
 * DTEND;VALUE=DATE:19980704
 * 
 * @author David Bal
 *
 * The property can be specified in following components:
 * @see VEvent
 * @see VFreeBusy
 */
public class DateTimeEnd extends PropertyBaseDateTime<Temporal, DateTimeEnd>
{    
   public DateTimeEnd(Temporal temporal)
    {
        super(temporal);
    }

//    public DateTimeEnd(Class<T> clazz, CharSequence contentLine)
//    {
//        super(clazz, contentLine);
//    }
    
    public DateTimeEnd(DateTimeEnd source)
    {
        super(source);
    }
    
    public DateTimeEnd()
    {
        super();
    }
    
    /** Parse string to Temporal.  Not type safe.  Implementation must
     * ensure parameterized type is the same as date-time represented by String parameter */
    public static DateTimeEnd parse(String value)
    {
        DateTimeEnd property = new DateTimeEnd();
        property.parseContent(value);
        return property;
    }
    
    /** Parse string with Temporal class explicitly provided as parameter */
    public static DateTimeEnd parse(Class<? extends Temporal> clazz, String value)
    {
        DateTimeEnd property = new DateTimeEnd();
        property.parseContent(value);
        clazz.cast(property.getValue()); // class check
        return property;
    }
}
