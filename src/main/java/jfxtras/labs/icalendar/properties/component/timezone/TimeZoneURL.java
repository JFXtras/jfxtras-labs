package jfxtras.labs.icalendar.properties.component.timezone;

import java.net.URI;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class TimeZoneURL extends PropertyBase<URI,TimeZoneURL>
{
    public TimeZoneURL(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine);
    }
    
    public TimeZoneURL(TimeZoneURL source)
    {
        super(source);
    }
    
    public TimeZoneURL(URI value)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(value);
    }
}
