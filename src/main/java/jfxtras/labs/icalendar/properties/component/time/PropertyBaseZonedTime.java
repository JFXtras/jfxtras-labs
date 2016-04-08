package jfxtras.labs.icalendar.properties.component.time;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    
    public PropertyBaseZonedTime(ZonedDateTime temporal)
    {
        super(temporal, null);
    }

    public PropertyBaseZonedTime(CharSequence contentLine)
    {
        super(contentLine, null);
    }
    
    public PropertyBaseZonedTime(PropertyBaseZonedTime<U> source)
    {
        super(source, null);
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
        setTimeZoneIdentifier(new TimeZoneIdentifierParameter(value.getZone()));
    }
    
//    @Override
//    protected ZonedDateTime valueFromString(String propertyValueString)
//    {
//        return ZonedDateTime.parse(propertyValueString, DateTimeUtilities.ZONED_DATE_TIME_FORMATTER);
//    }
//    
//    @Override
//    protected String valueToString(ZonedDateTime value)
//    {
//        ZoneId z = value.getZone();
//        if (z.equals(ZoneId.of("Z")))
//        {
//            return DateTimeUtilities.ZONED_DATE_TIME_UTC_FORMATTER.format(value);
//        } else
//        {
//            return DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(value); // Time zone is added through TimeZoneIdentifier parameter
//        }
//    }
}
