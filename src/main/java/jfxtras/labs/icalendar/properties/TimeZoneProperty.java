package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifier;

public class TimeZoneProperty<T,U> extends PropertyBase<T, U>
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
    
    public TimeZoneProperty(U temporal)
    {
        super();
        setValue(temporal);
    }

    public TimeZoneProperty(String propertyString)
    {
        super(propertyString);
    }
    
    public TimeZoneProperty(TimeZoneProperty<T,U> source)
    {
        super(source);
    }
    
    public TimeZoneProperty()
    {
        super();
    }   
}
