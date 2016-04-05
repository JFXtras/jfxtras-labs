package jfxtras.labs.icalendar.properties;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifier;
import jfxtras.labs.icalendar.parameters.ValueParameter.ValueType;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/**
 * Abstract class for all UTC and zoned-date-time classes
 * Contains the time zone identifier parameter
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see DTStartZonedDateTime
 */
public abstract class PropertyTimeZone<T> extends PropertyBase<T, ZonedDateTime>
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
    public TimeZoneIdentifier getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? null : timeZoneIdentifier.get(); }
    public ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString());
        }
        return timeZoneIdentifier;
    }
    private ObjectProperty<TimeZoneIdentifier> timeZoneIdentifier;
    public void setTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier)
    {
        if (timeZoneIdentifier != null)
        {
            timeZoneIdentifierProperty().set(timeZoneIdentifier);
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(new TimeZoneIdentifier(value)); }
    public T withTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (T) this; }
    public T withTimeZoneIdentifier(String content) { ParameterEnum.TIME_ZONE_IDENTIFIER.parse(this, content); return (T) this; }        
    
    /*
     * CONSTRUCTORS
     */
    
    public PropertyTimeZone(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public PropertyTimeZone(String propertyString)
    {
        super(propertyString);
    }
    
    public PropertyTimeZone(PropertyTimeZone<T> source)
    {
        super(source);
    }
    
//    public PropertyTimeZone()
//    {
//        super();
//    }
    
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
    public void setValue(ZonedDateTime value)
    {
        super.setValue(value);
        setTimeZoneIdentifier(new TimeZoneIdentifier().withValue(value.getZone()));
    }
    
    @Override
    protected ZonedDateTime valueFromString(String propertyValueString)
    {
        return ZonedDateTime.parse(propertyValueString, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);
    }
    
    @Override
    protected String valueToString(ZonedDateTime value)
    {
        ZoneId z = value.getZone();
        if (z.equals(ZoneId.of("Z")))
        {
            return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
        } else
        {
            return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value); // Time zone is added through TimeZoneIdentifier parameter
        }
    }
}
