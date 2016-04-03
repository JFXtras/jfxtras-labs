package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.TimeZoneProperty;

public class DTStartZonedDateTime extends TimeZoneProperty<DTStartZonedDateTime>
{
    public DTStartZonedDateTime(ZonedDateTime temporal)
    {
        super(temporal);
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

