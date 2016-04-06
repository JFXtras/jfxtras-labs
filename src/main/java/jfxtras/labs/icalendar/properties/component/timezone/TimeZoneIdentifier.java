package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.ZoneId;

import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.component.descriptive.Status;

/**
 * TZID
 * Time Zone Identifier
 * RFC 5545, 3.8.3.1, page 102
 * 
 * To specify the identifier for the time zone definition for
 * a time component in the property value
 * 
 * EXAMPLE:
 * TZID:America/Los_Angeles
 * 
 * @author David Bal
 *
 */
public class TimeZoneIdentifier extends PropertyBase<Status, ZoneId>
{
    public TimeZoneIdentifier(String contentLine)
    {
        super(contentLine);
    }
    
    public TimeZoneIdentifier(TimeZoneIdentifier source)
    {
        super(source);
    }
    
    public TimeZoneIdentifier(ZoneId value)
    {
        super(value);
    }
    
    @Override
    protected ZoneId valueFromString(String propertyValueString)
    {
        return ZoneId.of(propertyValueString);
    }
}
