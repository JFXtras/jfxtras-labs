package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.ZonedDateTime;

public class DTStartZonedDateTime extends DateTimeStart<DTStartZonedDateTime, ZonedDateTime>
{
    public DTStartZonedDateTime(ZonedDateTime temporal)
    {
        super();
    }

    public DTStartZonedDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartZonedDateTime(DTStartZonedDateTime source)
    {
        super(source);
    }
    
    public DTStartZonedDateTime()
    {
        super();
    }    
}
