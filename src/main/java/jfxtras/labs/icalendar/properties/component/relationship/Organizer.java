package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendar.components.VEventNew;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;

/**
 * ORGANIZER
 * RFC 5545, 3.8.4.3, page 111
 * 
 * This property defines the organizer for a calendar component.
 * 
 * Example:
 * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
 * 
 * @author David Bal
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public class Organizer extends PropertyBaseCalendarUser<URI, Organizer>
{
    public Organizer(String contentLine)
    {
        super(contentLine);
        URI.class.cast(getValue()); // ensure value class type matches parameterized type
    }
    
    public Organizer(Organizer source)
    {
        super(source);
    }
}
