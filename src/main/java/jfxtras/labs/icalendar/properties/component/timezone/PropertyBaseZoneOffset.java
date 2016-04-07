package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import jfxtras.labs.icalendar.properties.PropertyBase;

public abstract class PropertyBaseZoneOffset<T> extends PropertyBase<T, ZoneOffset>
{
    private final static DateTimeFormatter ZONE_OFFSET_FORMATTER = new DateTimeFormatterBuilder()
            .appendOffset("+HHMM", "+0000")
            .toFormatter();

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
    
    @Override
    protected ZoneOffset valueFromString(String propertyValueString)
    {
        return ZoneOffset.of(propertyValueString);
    }
    
    @Override
    protected String valueToString(ZoneOffset value)
    {
        return ZONE_OFFSET_FORMATTER.format(value);
    }
}
