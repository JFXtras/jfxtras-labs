package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyBaseUTC;

public class DateTimeCreated extends PropertyBaseUTC<DateTimeCreated>
{
    public DateTimeCreated(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeCreated(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public DateTimeCreated(DateTimeCreated source)
    {
        super(source);
    }
}
