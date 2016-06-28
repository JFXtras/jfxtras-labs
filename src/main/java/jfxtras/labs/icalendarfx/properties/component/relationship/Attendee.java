package jfxtras.labs.icalendarfx.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendarfx.components.VAlarm;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;

/**
 * ATTENDEE
 * Attendee
 * RFC 5545, 3.8.4.1, page 107
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
public class Attendee extends PropertyBaseAttendee<URI, Attendee>
{
    public Attendee(URI value)
    {
        super(value);
    }
    
    public Attendee(Attendee source)
    {
        super(source);
    }
    
    Attendee() { }

    public static Attendee parse(String propertyContent)
    {
        Attendee property = new Attendee();
        property.parseContent(propertyContent);
        URI.class.cast(property.getValue()); // ensure value class type matches parameterized type
        return property;
    }
}
