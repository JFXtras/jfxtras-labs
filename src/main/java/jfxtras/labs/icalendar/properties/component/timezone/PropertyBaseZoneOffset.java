package jfxtras.labs.icalendar.properties.component.timezone;

import java.time.ZoneOffset;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.properties.PropertyBase;

public abstract class PropertyBaseZoneOffset<T> extends PropertyBase<T, ZoneOffset>
{
    public PropertyBaseZoneOffset(CharSequence contentLine, StringConverter<ZoneOffset> converter)
    {
        super(contentLine, converter);
    }
    
    public PropertyBaseZoneOffset(PropertyBaseZoneOffset<T> source)
    {
        super(source);
    }
    
    public PropertyBaseZoneOffset(ZoneOffset value, StringConverter<ZoneOffset> converter)
    {
        super(value, converter);
    }
}
