package jfxtras.labs.icalendarfx.properties.component.relationship;

import java.net.URI;

import jfxtras.labs.icalendarfx.components.VAlarmInt;
import jfxtras.labs.icalendarfx.components.VEventNewInt;
import jfxtras.labs.icalendarfx.components.VJournalInt;
import jfxtras.labs.icalendarfx.components.VTodoInt;

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
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VAlarmInt
 */
public class Attendee extends PropertyBaseAttendee<URI, Attendee>
{    
    public Attendee(CharSequence contentLine)
    {
        super(contentLine);
        URI.class.cast(getValue()); // ensure value class type matches parameterized type
    }
    
    public Attendee(URI value)
    {
        super(value);
    }
    
    public Attendee(Attendee source)
    {
        super(source);
    }
}
