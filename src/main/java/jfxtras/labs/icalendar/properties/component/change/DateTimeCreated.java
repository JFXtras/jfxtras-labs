package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyTimeBase;

public class DateTimeCreated extends PropertyTimeBase<DateTimeCreated, ZonedDateTime>
{
    public DateTimeCreated(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public DateTimeCreated(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeCreated(DateTimeCreated source)
    {
        super(source);
    }
    
    public DateTimeCreated()
    {
        super();
    }   
}
