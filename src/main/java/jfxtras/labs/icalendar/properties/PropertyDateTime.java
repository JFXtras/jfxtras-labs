package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.properties.component.time.PropertyBaseDate;
import jfxtras.labs.icalendar.properties.component.time.PropertyBaseDateTime;
import jfxtras.labs.icalendar.properties.component.time.PropertyBaseUTCTime;
import jfxtras.labs.icalendar.properties.component.time.PropertyBaseZonedTime;

/**
 * Interface for all Date and Date-Time properties
 * 
 * @author David Bal
 *
 * @param <U> - property Temporal value type (LocalDate, LocalDateTime or ZonedDateTime)
 * @see PropertyBaseDate
 * @see PropertyBaseDateTime
 * @see PropertyBaseUTCTime
 * @see PropertyBaseZonedTime
 */
public interface PropertyDateTime<U> extends Property<U>
{
    /*
     * default Time Zone methods are overridden by classes that require them
     */
    default TimeZoneIdentifierParameter getTimeZoneIdentifier()
    {
        return null;
    }
    default ObjectProperty<TimeZoneIdentifierParameter> timeZoneIdentifierProperty()
    {
        return null;
    }
    default void setTimeZoneIdentifier(TimeZoneIdentifierParameter timeZoneIdentifier)
    {
        // do nothing - not implemented
    }
}
