package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

public class DateTimeStartDateTime extends DateTimeStart<DateTimeStartDateTime, Temporal>
{
    public DateTimeStartDateTime(Temporal temporal)
    {
        super();
        setValue(temporal);
    }

    public DateTimeStartDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DateTimeStartDateTime(DateTimeStartDateTime source)
    {
        super(source);
    }
    
    public DateTimeStartDateTime()
    {
        super();
    }    
}
