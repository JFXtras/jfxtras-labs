package jfxtras.labs.icalendar.properties.component.time;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifier;
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
    default TimeZoneIdentifier getTimeZoneIdentifier()
    {
        return null;
    }
    default ObjectProperty<TimeZoneIdentifier> timeZoneIdentifierProperty()
    {
        return null;
    }
    default void setTimeZoneIdentifier(TimeZoneIdentifier timeZoneIdentifier)
    {
        // do nothing - not implemented
    }
}
