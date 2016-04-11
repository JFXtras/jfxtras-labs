package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyBaseUTC;

public class DateTimeStamp extends PropertyBaseUTC<DateTimeStamp>
{    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeStamp(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
}
