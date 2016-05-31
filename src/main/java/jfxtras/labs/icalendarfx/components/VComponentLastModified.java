package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;

public interface VComponentLastModified<T> extends VComponent<T>
{
    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    ObjectProperty<LastModified> dateTimeLastModifiedProperty();
    LastModified getDateTimeLastModified();
    default void setDateTimeLastModified(String lastModified) { setDateTimeLastModified(LastModified.parse(lastModified)); }
    default void setDateTimeLastModified(LastModified lastModified) { dateTimeLastModifiedProperty().set(lastModified); }
    default void setDateTimeLastModified(ZonedDateTime lastModified) { setDateTimeLastModified(new LastModified(lastModified)); }
    default T withDateTimeLastModified(ZonedDateTime lastModified)
    {
        if (getDateTimeLastModified() == null)
        {
            setDateTimeLastModified(lastModified);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeLastModified(String lastModified)
    {
        if (getDateTimeLastModified() == null)
        {
            setDateTimeLastModified(lastModified);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeLastModified(LastModified lastModified)
    {
        if (getDateTimeLastModified() == null)
        {
            setDateTimeLastModified(lastModified);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
}
