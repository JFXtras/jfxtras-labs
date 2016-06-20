package jfxtras.labs.icalendarfx.properties.calendar;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.VCalendarElement;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale.CalendarScaleType;

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
public class CalendarScale extends PropertyBase<CalendarScaleType, CalendarScale> implements VCalendarElement
{
    public static final CalendarScaleType DEFAULT_CALENDAR_SCALE = CalendarScaleType.GREGORIAN;
    
    public CalendarScale(CalendarScale source)
    {
        super(source);
    }

    /** sets default value of GREGORIAN */
    public CalendarScale()
    {
       super(DEFAULT_CALENDAR_SCALE);
    }
    
    public enum CalendarScaleType
    {
        GREGORIAN;
    }

    public static CalendarScale parse(String string)
    {
        CalendarScale property = new CalendarScale();
        property.parseContent(string);
        return property;
    }    
}
