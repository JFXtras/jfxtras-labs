package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.properties.PropertyTimeBase;

public class DateTimeStart extends PropertyTimeBase<DateTimeStart>
{
    public DateTimeStart(Temporal temporal)
    {
        super(temporal);
    }

    public DateTimeStart(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeStart(DateTimeStart source)
    {
        super(source);
    }
    
    public DateTimeStart()
    {
        super();
    }    
}
