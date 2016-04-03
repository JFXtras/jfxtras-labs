package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.LocalDateTime;

public class DTStartLocalDateTime extends DateTimeStart<DTStartLocalDateTime, LocalDateTime>
{
    public DTStartLocalDateTime(LocalDateTime temporal)
    {
        super(temporal);
    }

    public DTStartLocalDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartLocalDateTime(DTStartLocalDateTime source)
    {
        super(source);
    }
    
    public DTStartLocalDateTime()
    {
        super();
    }    
}
