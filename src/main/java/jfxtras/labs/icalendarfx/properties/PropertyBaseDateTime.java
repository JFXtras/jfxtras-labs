package jfxtras.labs.icalendarfx.properties;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Collection;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendarfx.parameters.ParameterType;
import jfxtras.labs.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendarfx.parameters.ValueParameter;
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
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterType.TIME_ZONE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(timeZoneIdentifier);
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
            throw new DateTimeException(ParameterType.TIME_ZONE_IDENTIFIER.toString() + " can't be set for date-time of type " + getValue().getClass().getSimpleName());
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(TimeZoneIdentifierParameter.parse(value)); }
    public void setTimeZoneIdentifier(ZoneId zone) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone)); }
    public U withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (U) this; }
    public U withTimeZoneIdentifier(ZoneId zone) { setTimeZoneIdentifier(zone); return (U) this; }
    public U withTimeZoneIdentifier(String content) { ParameterType.TIME_ZONE_IDENTIFIER.parse(this, content); return (U) this; }        
    
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
        super.setValue(value);
        final Object element;
        if (value instanceof Collection)
        {
            Collection<?> collection = (Collection<?>) value;
            element = (collection.isEmpty()) ? null : collection.iterator().next();
        } else if (value instanceof Temporal)
        {
            element = value;
        } else
        {
            throw new DateTimeException("Unsupported type:" + value.getClass().getSimpleName());            
        }

        if (element != null)
        {
            if (element instanceof ZonedDateTime)
            {
                ZoneId zone = ((ZonedDateTime) element).getZone();
                if (! zone.equals(ZoneId.of("Z")))
                {
                    if (getValueType() != null && getValueType().getValue() == ValueType.DATE) setValueType((ValueParameter) null); // reset value type if previously set to DATE
                    setTimeZoneIdentifier(new TimeZoneIdentifierParameter(zone));
                }
            } else if ((element instanceof LocalDateTime) || (element instanceof LocalDate))
            {
                if (getTimeZoneIdentifier() != null)
                {
                    throw new DateTimeException("Only ZonedDateTime is permitted when specifying a Time Zone Identifier (" + element.getClass().getSimpleName() + ")");                            
                }
                if (element instanceof LocalDate)
                {
                    setValueType(ValueType.DATE); // must set value parameter to force output of VALUE=DATE
                } else
                {
                    if (getValueType() != null && getValueType().getValue() == ValueType.DATE) setValueType((ValueParameter) null); // reset value type if previously set to DATE
                }
            } else
            {
                throw new DateTimeException("Unsupported Temporal type:" + value.getClass().getSimpleName());            
            }
        }
    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = super.errors();
        if (getValue() != null && getValueType() != null)
        {
            if (getValue() instanceof LocalDate)
            {
                if (getValueType().getValue() != ValueType.DATE)
                {
                    errors.add(getPropertyName() + "'s value (" + getValue() + ") doesn't match its value type (" + getValueType().getValue()
                            + ").  For that value, the required value type is " + ValueType.DATE);
                }
            } else if (getValue() instanceof LocalDateTime || getValue() instanceof ZonedDateTime)
            {
                if (getValueType().getValue() != ValueType.DATE_TIME)
                {
                    errors.add(getPropertyName() + "'s value (" + getValue() + ") doesn't match its value type (" + getValueType().getValue()
                            + ").  For that value, the required value type is " + ValueType.DATE_TIME);
                }                
            }
        }
        return errors;
    }

}
