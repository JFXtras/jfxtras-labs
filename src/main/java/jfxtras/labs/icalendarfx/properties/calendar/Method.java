package jfxtras.labs.icalendarfx.properties.calendar;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.properties.PropertyBase;

/**
 * METHOD
 * RFC 5545, 3.7.2, page 77
 * 
 * This property defines the iCalendar object method associated with the calendar object.
 * 
 * No methods are defined by this specification.  This is the subject
 * of other specifications, such as the iCalendar Transport-
 * independent Interoperability Protocol (iTIP) defined by [2446bis]
 * 
 * Example:
 * METHOD:PUBLISH
 * 
 * @author David Bal
 * @see VCalendar
 */
public class Method extends PropertyBase<String, Method>
{
    public Method(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public Method(Method source)
    {
        super(source);
    }
}
