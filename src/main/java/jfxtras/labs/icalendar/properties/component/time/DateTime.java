package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.properties.Property;

/**
 * Interface for all Date and Date-Time properties
 * 
 * @author David Bal
 *
 * @param <U> - property Temporal value type (LocalDate, LocalDateTime or ZonedDateTime)
 */
public interface DateTime<U extends Temporal> extends Property<U>
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
