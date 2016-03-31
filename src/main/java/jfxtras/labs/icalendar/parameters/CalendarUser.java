package jfxtras.labs.icalendar.parameters;

import jfxtras.labs.icalendar.parameters.CalendarUser.CalendarUserType;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;

/**
 * CUTYPE
 * Calendar User Type
 * RFC 5545, 3.2.3, page 16
 * 
 * To identify the type of calendar user specified by the property.
 * 
 * Example:
 * ATTENDEE;CUTYPE=GROUP:mailto:ietf-calsch@example.org
 * 
 * @author David Bal
 * @see Attendee
 */
public class CalendarUser extends ParameterBase<CalendarUser, CalendarUserType>
{
    public CalendarUser()
    {
        super();
    }
  
    public CalendarUser(String content)
    {
        super(CalendarUserType.valueOf(extractValue(content)));
    }

    public CalendarUser(CalendarUser source)
    {
        super(source);
    }
    
    public enum CalendarUserType
    {
        INDIVIDUAL, // default is INDIVIDUAL
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN;

    }
}
