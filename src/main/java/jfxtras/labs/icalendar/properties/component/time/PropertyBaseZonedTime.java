package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.parameters.ValueType;
import jfxtras.labs.icalendar.properties.PropertyBase;
import jfxtras.labs.icalendar.properties.PropertyDateTime;
import jfxtras.labs.icalendar.properties.component.relationship.recurrenceid.RecurrenceIDZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.end.DTEndZonedDateTime;
import jfxtras.labs.icalendar.properties.component.time.start.DTStartZonedDateTime;

/**
 * Abstract class for all zoned-date-time classes
 * Contains the time zone identifier parameter
 * 
 * @author David Bal
 *
 * @param <U> - implementation class
 * @see DTStartZonedDateTime
 * @see DTEndZonedDateTime
 * @see RecurrenceIDZonedDateTime
 */
public abstract class PropertyBaseZonedTime<U> extends PropertyBase<U, ZonedDateTime> implements PropertyDateTime<ZonedDateTime>
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
        if (timeZoneIdentifier != null)
        {
            timeZoneIdentifierProperty().set(timeZoneIdentifier);
        }
    }
    public void setTimeZoneIdentifier(String value) { setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value)); }
    public U withTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier) { setTimeZoneIdentifier(timeZoneIdentifier); return (U) this; }
    public U withTimeZoneIdentifier(String content) { ParameterEnum.TIME_ZONE_IDENTIFIER.parse(this, content); return (U) this; }        
    
    /*
     * CONSTRUCTORS
     */
    
    public PropertyBaseZonedTime(ZonedDateTime temporal, StringConverter<ZonedDateTime> converter)
    {
        super(temporal, converter);
    }

    public PropertyBaseZonedTime(CharSequence contentLine, StringConverter<ZonedDateTime> converter)
    {
        super(contentLine, converter);
    }
    
    public PropertyBaseZonedTime(PropertyBaseZonedTime<U> source)
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
    public void setValue(ZonedDateTime value)
    {
        super.setValue(value);
        setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value.getZone()));
    }
}
