package jfxtras.labs.icalendar.properties.component.time.start;

import java.time.LocalDate;

public class DTStartLocalDate extends DateTimeStart<DTStartLocalDate, LocalDate>
{
    public DTStartLocalDate(LocalDate temporal)
    {
        super(temporal);
    }

    public DTStartLocalDate(String propertyString)
    {
        super(propertyString);
    }
    
    public DTStartLocalDate(DTStartLocalDate source)
    {
        super(source);
    }
    
    public DTStartLocalDate()
    {
        super();
    }    
}
