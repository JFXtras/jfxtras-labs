package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

/**
 * ORGANIZER
 * RFC 5545, 3.8.4.3
 * 
 * This property defines the organizer for a calendar component.
 * 
 * Example:
 * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
 * 
 * @author David Bal
 *
 */
public class Organizer extends PropertyBaseCalendarUser<Organizer, URI>
{
    public Organizer(String contentLine)
    {
        super(contentLine);
    }
    
    public Organizer(Organizer source)
    {
        super(source);
    }
}
