package jfxtras.labs.icalendar.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;

/**
 * ATTENDEE
 * Attendee
 * RFC 5545, 3.8.4.1
 * 
 * This property defines an "Attendee" within a calendar component.
 * 
 * Example:
 * ATTENDEE;ROLE=REQ-PARTICIPANT;DELEGATED-FROM="mailto:bob@
 *  example.com";PARTSTAT=ACCEPTED;CN=Jane Doe:mailto:jdoe@
 *  example.com
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
public class Attendee extends PropertyBaseAttendee<Attendee, URI>
{    
    public Attendee(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public Attendee(URI value)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(value, null);
    }
    
    public Attendee(Attendee source)
    {
        super(source);
    }
}
