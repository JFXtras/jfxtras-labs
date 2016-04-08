package jfxtras.labs.icalendar.properties.component.change;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class DateTimeCreated extends PropertyBase<DateTimeCreated, ZonedDateTime>
{
    @Override
    public void setValue(ZonedDateTime temporal)
    {
        ZoneId zone = temporal.getZone();
        if (zone.equals(ZoneId.of("Z")))
        {
            super.setValue(temporal);
        } else
        {
            throw new DateTimeException("ZoneId must be \"Z\"");
        }
    }
    
    public DateTimeCreated(ZonedDateTime temporal)
    {
        // null as string converter argument causes default converter from ValueType to be used
        super(temporal, null);
    }

    public DateTimeCreated(CharSequence contentLine)
    {
        // null as argument for string converter causes default converter from ValueType to be used
        super(contentLine, null);
    }
    
    public DateTimeCreated(DateTimeCreated source)
    {
        super(source);
    }
}
