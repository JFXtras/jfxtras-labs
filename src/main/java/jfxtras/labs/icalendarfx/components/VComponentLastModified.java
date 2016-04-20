package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.change.LastModified;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

public interface VComponentLastModified<T> extends VComponentNew<T>
{
    /**
     * LAST-MODIFIED: Date-Time Last Modified, from RFC 5545 iCalendar 3.8.7.3 page 138
     * This property specifies the date and time that the information associated with
     * the calendar component was last revised.
     * 
     * The property value MUST be specified in the UTC time format.
     */
    ObjectProperty<LastModified> dateTimeLastModifiedProperty();
    default LastModified getDateTimeLastModified() { return dateTimeLastModifiedProperty().get(); }
    default void setDateTimeLastModified(LastModified dtLastModified) { dateTimeLastModifiedProperty().set(dtLastModified); }
    default T withDateTimeLastModified(ZonedDateTime zonedDateTime) { setDateTimeLastModified(new LastModified(zonedDateTime)); return (T) this; }
    default T withDateTimeLastModified(String zonedDateTime) { setDateTimeLastModified(new LastModified(zonedDateTime)); return (T) this; }
    default T withDateTimeLastModified(LastModified dtLastModified) { setDateTimeLastModified(dtLastModified); return (T) this; }

}
