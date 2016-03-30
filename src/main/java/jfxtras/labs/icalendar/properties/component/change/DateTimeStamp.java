package jfxtras.labs.icalendar.properties.component.change;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class DateTimeStamp extends PropertyBase<DateTimeStamp, ZonedDateTime>
{    
    public DateTimeStamp(ZonedDateTime temporal)
    {
        setValue(temporal);
    }

    public DateTimeStamp(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeStamp(DateTimeStamp source)
    {
        super(source);
    }
    
    public DateTimeStamp()
    {
        super();
    }   
}
