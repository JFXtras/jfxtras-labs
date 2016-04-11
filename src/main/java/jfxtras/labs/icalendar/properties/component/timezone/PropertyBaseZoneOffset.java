package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.ZoneOffset;

import jfxtras.labs.icalendar.properties.PropertyBase;

public abstract class PropertyBaseZoneOffset<T> extends PropertyBase<T, ZoneOffset>
{
    public PropertyBaseZoneOffset(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseZoneOffset(PropertyBaseZoneOffset<T> source)
    {
        super(source);
    }
    
    public PropertyBaseZoneOffset(ZoneOffset value)
    {
        super(value);
    }
}
