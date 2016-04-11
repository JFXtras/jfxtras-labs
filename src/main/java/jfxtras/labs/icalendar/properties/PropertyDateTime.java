package jfxtras.labs.icalendar.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendar.properties.component.misc.UnknownProperty;

/**
 * Interface for all Date and Date-Time properties
 * 
 * @author David Bal
 *
 * @param <T> - property Temporal value type (LocalDate, LocalDateTime or ZonedDateTime)
 * @see PropertyBaseDateTime
 * @see UnknownProperty
 */
public interface PropertyDateTime<T> extends Property<T>
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
