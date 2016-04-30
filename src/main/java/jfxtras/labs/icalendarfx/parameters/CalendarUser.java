package jfxtras.labs.icalendarfx.parameters;

import jfxtras.labs.icalendarfx.parameters.CalendarUser.CalendarUserType;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;

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
 * @see Organizer
 */
public class CalendarUser extends ParameterBase<CalendarUser, CalendarUserType>
{
    private String unknownValue;

    public CalendarUser()
    {
        super(CalendarUserType.INDIVIDUAL); // default value
    }

    public CalendarUser(CalendarUserType type)
    {
        super(type);
    }
    
    public CalendarUser(String content)
    {
        super(CalendarUserType.valueOfWithUnknown(content));
        if (getValue() == CalendarUserType.UNKNOWN)
        {
            unknownValue = content;
        }
    }

    public CalendarUser(CalendarUser source)
    {
        super(source);
        unknownValue = source.unknownValue;
    }
    
    @Override
    public String toContent()
    {
        String value = (getValue() == CalendarUserType.UNKNOWN) ? unknownValue : getValue().toString();
        String parameterName = myParameterEnum().toString();
        return ";" + parameterName + "=" + value;
    }
    
    public enum CalendarUserType
    {
        INDIVIDUAL, // default is INDIVIDUAL
        GROUP,
        RESOURCE,
        ROOM,
        UNKNOWN;
        
        static CalendarUserType valueOfWithUnknown(String value)
        {
            CalendarUserType match;
            try
            {
                match = valueOf(value);
            } catch (Exception e)
            {
                match = UNKNOWN;
            }
            return match;
        }

    }
}
