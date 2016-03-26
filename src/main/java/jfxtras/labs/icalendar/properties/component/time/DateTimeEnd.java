package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.properties.PropertyTimeBase;

public class DateTimeEnd extends PropertyTimeBase<DateTimeEnd, Temporal>
{
    public DateTimeEnd(Temporal temporal)
    {
        super(temporal);
    }

    public DateTimeEnd(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeEnd(DateTimeEnd source)
    {
        super(source);
    }
    
    public DateTimeEnd()
    {
        super();
    }    
}
