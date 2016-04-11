package jfxtras.labs.icalendar.properties;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;

/**
 * Abstract class for all UTC zoned-date-time classes
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeCompleted
 */
public abstract class PropertyBaseUTC<U> extends PropertyBase<ZonedDateTime,U>
{
    /*
     * CONSTRUCTORS
     */
    protected PropertyBaseUTC()
    {
        super();
    }
    
    public PropertyBaseUTC(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public PropertyBaseUTC(CharSequence contentLine)
    {
        super(contentLine);
    }
    
    public PropertyBaseUTC(PropertyBaseUTC<U> source)
    {
        super(source);
    }

    @Override
    public void setValue(ZonedDateTime value)
    {
        ZoneId zone = value.getZone();
        if (! zone.equals(ZoneId.of("Z")))
        {
            throw new DateTimeException("Unsupported ZoneId:" + zone + " only Z supported");
        }
        super.setValue(value);
    }
    
    @Override
    public boolean isValid()
    {
        ZoneId zone = getValue().getZone();
        if (! zone.equals(ZoneId.of("Z")))
        {
            return false;
        }
        return super.isValid();
    }
}
