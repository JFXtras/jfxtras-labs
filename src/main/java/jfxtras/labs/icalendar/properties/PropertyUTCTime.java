package jfxtras.labs.icalendar.properties;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

public class PropertyUTCTime<T> extends PropertyBase<T, ZonedDateTime>
{
    public PropertyUTCTime(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public PropertyUTCTime(String propertyString)
    {
        super(propertyString);
    }
    
    public PropertyUTCTime(PropertyUTCTime<T> source)
    {
        super(source);
    }
    
    public PropertyUTCTime()
    {
        super();
    }
    
    @Override
    public void setValue(ZonedDateTime value)
    {
        super.setValue(value);
    }
    
    @Override
    protected ZonedDateTime valueFromString(String propertyValueString)
    {
        return ZonedDateTime.parse(propertyValueString, DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER);
    }
    
    @Override
    protected String valueToString(ZonedDateTime value)
    {
        return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
    }
}
