package jfxtras.labs.icalendar.parameters;

import java.time.ZoneId;

/**
 * TZID
 * Time Zone Identifier
 * RFC 5545, 3.2.19, page 27
 * 
 * To specify the identifier for the time zone definition for
 *  a time component in the property value.
 * 
 * Example:
 * DTSTART;TZID=America/New_York:19980119T020000
 * 
 * @author David Bal
 *
 */
public class TimeZoneIdentifier extends ParameterBase<TimeZoneIdentifier, ZoneId>
{
    public TimeZoneIdentifier()
    {
        super();
    }
    
    public TimeZoneIdentifier(String content)
    {
        super(ZoneId.of(content));
    }
    
    public TimeZoneIdentifier(TimeZoneIdentifier source)
    {
        super(source);
    }
}
