package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;

import javafx.util.StringConverter;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyDateTime;

/**
 * Abstract class for all UTC zoned-date-time classes
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeCompleted
 */
public abstract class PropertyBaseUTCTime<U> extends PropertyBase<U, ZonedDateTime> implements PropertyDateTime<ZonedDateTime>
{
    public PropertyBaseUTCTime(ZonedDateTime temporal, StringConverter<ZonedDateTime> converter)
    {
        super(temporal, converter);
    }

    public PropertyBaseUTCTime(CharSequence contentLine, StringConverter<ZonedDateTime> converter)
    {
        super(contentLine, converter);
    }
    
    public PropertyBaseUTCTime(PropertyBaseUTCTime<U> source)
    {
        super(source);
    }
    
    @Override
    public void setValue(ZonedDateTime value)
    {
        super.setValue(value);
    }
}
