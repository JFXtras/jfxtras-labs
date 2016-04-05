package jfxtras.labs.icalendar.properties;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendar.properties.component.time.start.DateTimeStart;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Abstract class for all UTC zoned-date-time classes
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see DateTimeCompleted
 */
public abstract class PropertyUTCTime<T> extends PropertyBase<T, ZonedDateTime> implements DateTimeStart<ZonedDateTime>
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
