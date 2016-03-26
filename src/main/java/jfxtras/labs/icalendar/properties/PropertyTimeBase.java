package jfxtras.labs.icalendar.properties;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.component.change.DateTimeCreated;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.time.DateTimeCompleted;
import jfxtras.labs.icalendar.properties.component.time.DateTimeDue;
import jfxtras.labs.icalendar.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendar.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendar.properties.component.time.FreeBusyTime;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Base class for date/time properties
 * Contains optional VALUE parameter indicating DATE or DATE-TIME types.  Defaults to DATE-TIME
 * Contains optional time zone (TZID) parameter.
 * 
 * @author David Bal
 *
 * @param <T> - subclass
 * @see DateTimeCompleted
 * @see DateTimeCreated
 * @see DateTimeDue
 * @see DateTimeEnd
 * @see DateTimeStamp
 * @see DateTimeStart
 * @see FreeBusyTime
 */
public abstract class PropertyTimeBase<T,U extends Temporal> extends PropertyBase<T>
{
    /**
     * The value of the date/time property.
     * FORM #1: DATE WITH LOCAL TIME e.g. 19980118T230000 (LocalDateTime)
     * FORM #2: DATE WITH UTC TIME e.g. 19980119T070000Z (ZonedDateTime)
     * FORM #3: DATE WITH LOCAL TIME AND TIME ZONE REFERENCE e.g. TZID=America/New_York:19980119T020000 (ZonedDateTime)
     * FORM #4: DATE ONLY e.g. VALUE=DATE:19970304 (LocalDate)
     * 
     * Note: strings can contain optionally contain "VALUE" "=" ("DATE-TIME" / "DATE"))
     * before the date-time portion of the string.  If omitted the default type is used.
     * e.g. VALUE=DATE:19960401         VALUE=DATE-TIME:19980101T050000Z
     * 
     * Based on ISO.8601.2004
     */
    @Override public U getValue() { return value.get(); }
    public ObjectProperty<U> valueProperty() { return value; }
    final private ObjectProperty<U> value = new SimpleObjectProperty<>(this, propertyType().toString());
    public void setValue(U temporal)
    {
        if ((temporal instanceof LocalDate) || (temporal instanceof LocalDateTime) || (temporal instanceof ZonedDateTime))
        {
            this.value.set(temporal);
            if (temporal instanceof ZonedDateTime)
            {
                ZoneId zone = ((ZonedDateTime) temporal).getZone();
                if (! zone.normalized().equals(ZoneOffset.UTC))
                {
                    setTimeZoneIdentifier(zone);
                }
            }
        } else
        {
            throw new IllegalArgumentException("Unsupported Temporal class:" + temporal.getClass().getSimpleName());
        }
    }
    public T withValue(U temporal) { setValue(temporal); return (T) this; }
    
    /**
     * TZID: Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * The time zone should be one from the public-domain TZ database (TZDB) for 
     * interoperability with ZonedDateTime and ZoneId classes.
     * 
     * Example:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    public ZoneId getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? _timeZoneIdentifier : timeZoneIdentifier.get(); }
    public ObjectProperty<ZoneId> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString(), _timeZoneIdentifier);
        }
        return timeZoneIdentifier;
    }
    private ZoneId _timeZoneIdentifier;
    private ObjectProperty<ZoneId> timeZoneIdentifier;
    public void setTimeZoneIdentifier(String value)
    {
        setTimeZoneIdentifier(ZoneId.of(value));
    }
    public void setTimeZoneIdentifier(ZoneId value)
    {
        if (value != null)
        {
            parameters().add(ParameterEnum.TIME_ZONE_IDENTIFIER);
        } else
        {
            parameters().remove(ParameterEnum.TIME_ZONE_IDENTIFIER);            
        }
        if (this.timeZoneIdentifier == null)
        {
            _timeZoneIdentifier = value;
        } else
        {
            this.timeZoneIdentifier.set(value);
        }
    }
    public T withTimeZoneIdentifier(ZoneId content) { setTimeZoneIdentifier(content); return (T) this; }
    public T withTimeZoneIdentifier(String content) { setTimeZoneIdentifier(content); return (T) this; }
    
    /*
     * Overtide ValueType
     */
    final private static List<ValueType> SUPPORTED_VALUED_TYPES = Arrays.asList(ValueType.DATE, ValueType.DATE_TIME);
    @Override
    public void setValueType(ValueType value)
    {        
        if (SUPPORTED_VALUED_TYPES.contains(value))
        {
            final boolean typeAndValueMatch;
            if (getValue() != null)
            {
                typeAndValueMatch = (getValue() instanceof LocalDate) ? value == ValueType.DATE : value == ValueType.DATE_TIME;                
            } else
            {
                typeAndValueMatch = true;
            }
            if (typeAndValueMatch)
            {
                super.setValueType(value);                
            } else
            {
                throw new IllegalArgumentException("ValueType doesn't match Temporal value: " + value + ", " + getValue());                
            }
        } else
        {
            throw new IllegalArgumentException("Only ValueType values supported are: " + SUPPORTED_VALUED_TYPES);
        }
    }
    
    /*
     * CONSTRUCTORS
     */
    protected PropertyTimeBase(U temporal)
    {
        setValue(temporal);
    }
    
    protected PropertyTimeBase(String propertyString)
    {
        super(propertyString);
        ZoneId zone = getTimeZoneIdentifier();
        setValue((U) DateTimeUtilities.parse(getPropertyValueString(), zone));
    }

    // copy constructor
    protected PropertyTimeBase(PropertyTimeBase<T,U> source)
    {
        super(source);
        if (getValue() != null)
        {
            setValue(source.getValue());
        }
    }

    protected PropertyTimeBase()
    {
        super();
    }
    
    /*
     * OVERRIDDEN METHODS
     */
    
    @Override
    public String toContentLine()
    {
        return super.toContentLine() + ":" + DateTimeUtilities.format(getValue());
    }
}
