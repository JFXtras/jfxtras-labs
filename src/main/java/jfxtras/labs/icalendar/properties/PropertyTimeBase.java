package jfxtras.labs.icalendar.properties;

import java.time.ZoneId;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.parameters.ParameterEnum;
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
public class PropertyTimeBase<T> extends PropertyBase<T>
{
    @Override
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
    public Temporal getValue() { return value.get(); }
    public ObjectProperty<Temporal> valueProperty() { return value; }
    final private ObjectProperty<Temporal> value = new SimpleObjectProperty<Temporal>(this, propertyType().toString());
    public void setValue(Temporal temporal){ this.value.set(temporal); }
    public T withValue(Temporal temporal) { setValue(temporal); return (T) this; }
    
    /**
     * TZID: Time Zone Identifier
     * To specify the identifier for the time zone definition for
     * a time component in the property value.
     * The time zone should be one from the public-domain TZ database (TZDB) for 
     * interoperability with ZonedDateTime and ZoneID classes.
     * 
     * Example:
     * DTSTART;TZID=America/New_York:19980119T020000
     */
    public String getTimeZoneIdentifier() { return (timeZoneIdentifier == null) ? _timeZoneIdentifier : timeZoneIdentifier.get(); }
    public ObjectProperty<String> timeZoneIdentifierProperty()
    {
        if (timeZoneIdentifier == null)
        {
            timeZoneIdentifier = new SimpleObjectProperty<>(this, ParameterEnum.TIME_ZONE_IDENTIFIER.toString(), _timeZoneIdentifier);
        }
        return timeZoneIdentifier;
    }
    private String _timeZoneIdentifier;
    private ObjectProperty<String> timeZoneIdentifier;
    public void setTimeZoneIdentifier(String value)
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
    public T withTimeZoneIdentifier(String content) { setTimeZoneIdentifier(String.valueOf(content)); return (T) this; } 
    
    /*
     * CONSTRUCTORS
     */    
    protected PropertyTimeBase(String propertyString)
    {
        super(propertyString);
        ZoneId zone = ZoneId.of(getTimeZoneIdentifier());
        setValue(DateTimeUtilities.parse(getPropertyValueString(), zone));
    }

    // copy constructor
    public PropertyTimeBase(PropertyTimeBase<T> source)
    {
        super(source);
        if (getValue() != null)
        {
            setValue(source.getValue());
        }
    }

    public PropertyTimeBase()
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
