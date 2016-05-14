package jfxtras.labs.icalendarfx.properties.component.misc;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;

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
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 * @see VTimeZone
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class NonStandardProperty extends UnknownProperty<Object, NonStandardProperty>
{
//    public NonStandardProperty(CharSequence contentLine)
//    {
//        super(contentLine);
//    }
    
    public NonStandardProperty(Object value)
    {
        super(value);
    }
    
    public NonStandardProperty(NonStandardProperty source)
    {
        super(source);
    }
    
    public NonStandardProperty()
    {
        super();
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
    
    public static NonStandardProperty parse(String value)
    {
        NonStandardProperty property = new NonStandardProperty();
        property.parseContent(value);
        return property;
    }
}
