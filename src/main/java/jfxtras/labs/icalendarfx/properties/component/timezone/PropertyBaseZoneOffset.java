package jfxtras.labs.icalendarfx.properties.component.timezone;

import java.time.ZoneOffset;

import jfxtras.labs.icalendarfx.properties.PropertyBase;

public abstract class PropertyBaseZoneOffset<U> extends PropertyBase<ZoneOffset,U>
{
    public PropertyBaseZoneOffset(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseZoneOffset(PropertyBaseZoneOffset<U> source)
    {
        super(source);
    }
    
    public PropertyBaseZoneOffset(ZoneOffset value)
    {
        super(value);
    }
}
