package jfxtras.labs.icalendar.properties;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifier;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;

/**
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see DTStartZonedDateTime
 */
public abstract class TimeZoneProperty<T> extends PropertyBase<T, ZonedDateTime>
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
    
    public TimeZoneProperty(ZonedDateTime temporal)
    {
        super(temporal);
    }

    public TimeZoneProperty(String propertyString)
    {
        super(propertyString, null);
    }
    
    public TimeZoneProperty(TimeZoneProperty<T> source)
    {
        super(source);
    }
    
    public TimeZoneProperty()
    {
        super();
    }
    
    @Override
    protected String getPropertyValueString()
    {
        return getTimeZoneIdentifier().getValue().toString() + ":" + super.getPropertyValueString();
    }

    @Override
    public void setValue(ZonedDateTime value)
    {
        super.setValue(value);
        setTimeZoneIdentifier(new TimeZoneIdentifier().withValue(value.getZone()));
    }
}
