package jfxtras.labs.icalendarfx.properties;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.parameters.TimeZoneIdentifierParameter;
import jfxtras.labs.icalendarfx.properties.component.misc.UnknownProperty;

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
