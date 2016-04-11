package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyBase;

/**
 * DURATION
 * RFC 5545, 3.8.4.6, page 116
 * 
 * This property defines a Uniform Resource Locator (URL) associated with the iCalendar object.
 *
 * Examples:
 * URL:http://example.com/pub/calendars/jsmith/mytime.ics
 * 
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public class UniformResourceLocator extends PropertyBase<URI,UniformResourceLocator>
{
    public UniformResourceLocator(String contentLine)
    {
        super(contentLine);
        URI.class.cast(getValue()); // ensure value class type matches parameterized type
    }
    
    public UniformResourceLocator(URI value)
    {
        super(value);
    }
    
    public UniformResourceLocator(UniformResourceLocator source)
    {
        super(source);
    }
}
