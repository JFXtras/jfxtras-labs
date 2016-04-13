package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;

/**
 * CN
 * Common Name
 * RFC 5545, 3.2.2, page 15
 * 
 * To specify the common name to be associated with the calendar user specified by the property.
 * 
 * Example:
 * ORGANIZER;CN="John Smith":mailto:jsmith@example.com
 * 
 * @author David Bal
 * @see Attendee
 * @see Organizer
 */
public class CommonName extends ParameterText<CommonName>
{
    public CommonName(String content)
    {
        super(content);
    }

    public CommonName(CommonName source)
    {
        super(source);
    }
}
