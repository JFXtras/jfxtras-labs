package jfxtras.labs.icalendar.properties.calendar;

import jfxtras.labs.icalendar.VCalendar;
import jfxtras.labs.icalendar.properties.PropertyBase;

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
public class Method extends PropertyBase<Method, String>
{
    public Method(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public Method(Method source)
    {
        super(source);
    }
}
