package jfxtras.labs.icalendarfx.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendarfx.components.VEventNewInt;
import jfxtras.labs.icalendarfx.components.VJournalInt;
import jfxtras.labs.icalendarfx.components.VTodoInt;
import jfxtras.labs.icalendarfx.properties.PropertyBaseUTC;

/**
 * DTSTAMP
 * Date-Time Stamp
 * RFC 5545, 3.8.7.2, page 137
 * 
 * In the case of an iCalendar object that specifies a
 * "METHOD" property, this property specifies the date and time that
 * the instance of the iCalendar object was created.  In the case of
 * an iCalendar object that doesn't specify a "METHOD" property, this
 * property specifies the date and time that the information
 * associated with the calendar component was last revised in the
 * calendar store.
 * 
 * In the case of an iCalendar object that doesn't specify a "METHOD"
 * property, this property is equivalent to the "LAST-MODIFIED" property.
 * 
 * The value MUST be specified as a date with UTC time.
 * 
 * Example:
 * DTSTAMP:19971210T080000Z
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 */
public class DateTimeStamp extends PropertyBaseUTC<DateTimeStamp>
{    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeStamp(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
}
