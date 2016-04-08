package jfxtras.labs.icalendar.properties.component.timezone;

import java.net.URI;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class TimeZoneURL extends PropertyBase<TimeZoneURL, URI>
{
    public TimeZoneURL(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public TimeZoneURL(TimeZoneURL source)
    {
        super(source);
    }
    
    public TimeZoneURL(URI value)
    {
        super(value);
    }
}
