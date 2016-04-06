package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.VCalendar;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.calendar.CalendarScale.CalendarScaleType;

/**
 * CALSCALE
 * Calendar Scale
 * RFC 5545, 3.7.1, page 76
 * 
 * This property defines the calendar scale used for the
 * calendar information specified in the iCalendar object.
 * 
 * Only allowed value is GREGORIAN
 * It is expected that other calendar scales will be defined in other specifications or by
 * future versions of this memo. 
 * 
 * Example:
 * CALSCALE:GREGORIAN
 * 
 * @author David Bal
 * @see VCalendar
 */
public class CalendarScale extends PropertyBase<CalendarScale, CalendarScaleType>
{
    public CalendarScale(String propertyString)
    {
        super(CalendarScaleType.valueOf(propertyString.toUpperCase()));
    }
    
    public CalendarScale(CalendarScale source)
    {
        super(source);
    }
    
    /** sets default value of GREGORIAN */
    public CalendarScale()
    {
        super(CalendarScaleType.GREGORIAN);
    }
    
    public enum CalendarScaleType
    {
        GREGORIAN;
    }
}
