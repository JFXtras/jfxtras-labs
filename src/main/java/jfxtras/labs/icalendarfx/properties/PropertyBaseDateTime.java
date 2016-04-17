package jfxtras.labs.icalendarfx.properties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.parameters.ParameterEnum;
import jfxtras.labs.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendarfx.parameters.ValueType;
import jfxtras.labs.icalendarfx.properties.component.relationship.RecurrenceId;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

/**
 * Abstract class for all non-UTC date-time classes
 * Contains the time zone identifier parameter
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeStart
 * @see DateTimeEnd
 * @see RecurrenceId
 */
public abstract class PropertyBaseDateTime<T, U> extends PropertyBase<T,U> implements PropertyDateTime<T>
{
    /**
     * TZID
     * Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * 
     * Examples:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    @Override
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    @Override
    public ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString());
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifier;
    @Override
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        if ((getValue() == null) || (getValue() instanceof ZonedDateTime))
        {
            timeZoneIdentifierProperty().set(timeZoneIdentifier);
        } else
        {
            throw new DateTimeException(ParameterEnum.TIME_ZONE_IDENTIFIER.toString() + " can't be set for date-time of type " + getValue().getClass().getSimpleName());
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value)); }
    public void setTimeZoneIdentifier(ZoneId zone) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone)); }
    public U withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (U) this; }
    public U withTimeZoneIdentifier(ZoneId zone) { setTimeZoneIdentifier(zone); return (U) this; }
    public U withTimeZoneIdentifier(String content) { ParameterEnum.TIME_ZONE_IDENTIFIER.parse(this, content); return (U) this; }        
    
    /*
     * CONSTRUCTORS
     */
    protected PropertyBaseDateTime()
    {
        super();
    }
    
    public PropertyBaseDateTime(T temporal)
    {
        super(temporal);
    }

    public PropertyBaseDateTime(Class<T> clazz, CharSequence contentLine)
    {
        super(contentLine);
        clazz.cast(getValue()); // ensure value class type matches parameterized type
    }
    
    public PropertyBaseDateTime(PropertyBaseDateTime<T,U> source)
    {
        super(source);
    }
    
    /**
     * append time zone to front of time for parsing in DATE_ZONED_DATE_TIME parse method
     * @see ValueType
     */
    @Override
    protected String getPropertyValueString()
    {
        if (super.getPropertyValueString() == null)
        {
            return null;
        } else
        {
            String timeZone = (getTimeZoneIdentifier() != null) ? "[" + getTimeZoneIdentifier().getValue().toString() + "]" : "";
            return timeZone + super.getPropertyValueString();
        }
    }

    @Override
    public void setValue(T value)
    {
        final Object element;
        if (value instanceof Collection)
        {
            element = ((Collection<?>) value).iterator().next();
        } else
        {
            element = value;
        }

        if (element instanceof ZonedDateTime)
        {
            ZoneId zone = ((ZonedDateTime) element).getZone();
            setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone));
        } else if ((element instanceof LocalDateTime) || (element instanceof LocalDate))
        {
            if (getTimeZoneIdentifier() != null)
            {
                throw new DateTimeException("Only ZonedDateTime is permitted when specifying a Time Zone Identifier (" + element.getClass().getSimpleName() + ")");                            
            }
            if (element instanceof LocalDate)
            {
                setValueParameter(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
            }
        } else
        {
            throw new DateTimeException("Unsupported Temporal type:" + value.getClass().getSimpleName());            
        }
        super.setValue(value);
    }
}
