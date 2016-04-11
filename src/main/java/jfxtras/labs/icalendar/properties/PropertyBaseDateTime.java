package jfxtras.labs.icalendar.properties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;

/**
 * Abstract class for all non-UTC date-time classes
 * Contains the time zone identifier parameter
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DateTimeStart
 * @see DateTimeEnd
 * @see RecurrenceIDZonedDateTime
 */
public abstract class PropertyBaseDateTime<T extends Temporal, U> extends PropertyBase<T,U>
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
    public TimeZoneIdentifierParameter getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    public ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString());
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifier;
    public void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        if ((getValue() == null) || (getValue() instanceof ZonedDateTime))
        {
            if (timeZoneIdentifier != null)
            {
                timeZoneIdentifierProperty().set(timeZoneIdentifier);
            }
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
        String timeZone = (getTimeZoneIdentifier() != null) ? getTimeZoneIdentifier().getValue().toString() + ":" : "";
        return timeZone + super.getPropertyValueString();
    }

    @Override
    public void setValue(T value)
    {
        if (value instanceof ZonedDateTime)
        {
            ZoneId zone = ((ZonedDateTime) value).getZone();
            setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone));
        } else if ((value instanceof LocalDateTime) || (value instanceof LocalDate))
        {
            if (getTimeZoneIdentifier() != null)
            {
                throw new DateTimeException("Only ZonedDateTime is permitted when specifying a Time Zone Identifier");                            
            }
            if (value instanceof LocalDate)
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
