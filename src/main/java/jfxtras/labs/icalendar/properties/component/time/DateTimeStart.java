package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import jfxtras.labs.icalendar.properties.PropertyBase;

public class DateTimeStart extends PropertyBase<DateTimeStart, Temporal>
{
    public DateTimeStart(Temporal temporal)
    {
        super();
        setValue(temporal);
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
