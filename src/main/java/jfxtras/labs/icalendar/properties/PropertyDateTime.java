package jfxtras.labs.icalendar.properties;

import java.time.LocalDateTime;

import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public abstract class PropertyDateTime<T> extends PropertyBase<T, LocalDateTime>
{
    public PropertyDateTime(LocalDateTime temporal)
    {
        super(temporal);
    }

    public PropertyDateTime(String propertyString)
    {
        super(propertyString);
    }
    
    public PropertyDateTime(PropertyDateTime<T> source)
    {
        super(source);
    }
    
    public PropertyDateTime()
    {
        super();
    }
    
    @Override
    protected LocalDateTime valueFromString(String propertyValueString)
    {
        return LocalDateTime.parse(propertyValueString, DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER);
    }
    
    @Override
    protected String valueToString(LocalDateTime value)
    {
        return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value);  
    }
}
