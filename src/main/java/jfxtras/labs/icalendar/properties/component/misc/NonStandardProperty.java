package jfxtras.labs.icalendar.properties.component.misc;

import jfxtras.labs.icalendar.components.DaylightSavingTime;
import jfxtras.labs.icalendar.components.StandardTime;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTimeZone;
import jfxtras.labs.icalendar.components.VTodo;

/**
 * Non-Standard Properties
 * RFC 5545, 3.8.8.2, page 140
 * 
 * Any property name with a "X-" prefix
 * 
 * Examples:
 * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
 *  org/mysubj.au
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 * @see VTimeZone
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class NonStandardProperty extends UnknownProperty<Object, NonStandardProperty>
{
    public NonStandardProperty(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public NonStandardProperty(Object value)
    {
        super(value);
    }
    
    public NonStandardProperty(NonStandardProperty source)
    {
        super(source);
    }
    
    @Override
    public boolean isValid()
    {
        if (! getPropertyName().substring(0, 2).equals("X-"))
        {
            return false;
        }
        return super.isValid();
    }
}
