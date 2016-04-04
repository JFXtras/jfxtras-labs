package jfxtras.labs.icalendar.properties.component.descriptive;

import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTodo;
import jfxtras.labs.icalendar.properties.PropertyAlternateTextRepresentation;

/**
 * DESCRIPTION
 * RFC 5545 iCalendar 3.8.1.5. page 84
 * 
 * This property provides a more complete description of the
 * calendar component than that provided by the "SUMMARY" property.
 * 
 * Example:
 * DESCRIPTION:Meeting to provide technical review for "Phoenix"
 *  design.\nHappy Face Conference Room. Phoenix design team
 *  MUST attend this meeting.\nRSVP to team leader.
 *
 * Note: Only VJournal allows multiple instances of DESCRIPTION
 *   
 * @author David Bal
 * 
 * The property can be specified in following components:
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VAlarm
 */
public class Description extends PropertyAlternateTextRepresentation<Description, String>
{
    public Description(String contentLine)
    {
        super((CharSequence) contentLine);
    }

    public Description(Description source)
    {
        super(source);
    }
    
    public Description()
    {
        super();
    }
}
