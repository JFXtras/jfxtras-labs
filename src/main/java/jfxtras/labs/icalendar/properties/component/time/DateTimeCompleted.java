package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyUTCTime;

public class DateTimeCompleted extends PropertyUTCTime<DateTimeCompleted>
{
    public DateTimeCompleted(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeCompleted(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeCompleted(DateTimeCompleted source)
    {
        super(source);
    }
    
    public DateTimeCompleted()
    {
        super();
    }

    @Override
    public boolean isValid()
    {
        if (! getValue().getZone().equals(ZoneId.of("Z")))
        {
            return false;
        }
        return super.isValid();
    }
    
}
