package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;

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
public abstract class UTCTimeAbstract<T> extends PropertyBase<T, ZonedDateTime> implements DateTime<ZonedDateTime>
{
    public UTCTimeAbstract(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public UTCTimeAbstract(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public UTCTimeAbstract(UTCTimeAbstract<T> source)
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
