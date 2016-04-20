package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;

public interface VComponentLastModified<T>
{
    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    ZonedDateTime getDateTimeLastModified();
    ObjectProperty<ZonedDateTime> dateTimeLastModifiedProperty();
    void setDateTimeLastModified(ZonedDateTime dtLastModified);
}
