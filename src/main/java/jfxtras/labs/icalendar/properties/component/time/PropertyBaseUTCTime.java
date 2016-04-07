package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Abstract class for all UTC zoned-date-time classes
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see DateTimeCompleted
 */
public abstract class PropertyBaseUTCTime<T> extends PropertyBase<T, ZonedDateTime> implements PropertyDateTime<ZonedDateTime>
{
    public PropertyBaseUTCTime(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public PropertyBaseUTCTime(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseUTCTime(PropertyBaseUTCTime<T> source)
    {
        super(source);
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
