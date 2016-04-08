package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class DateTimeStamp extends PropertyBase<DateTimeStamp, ZonedDateTime>
{    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(temporal, null);
    }

    public DateTimeStamp(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
}
