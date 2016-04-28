package jfxtras.labs.icalendarfx.components;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

/**
 * Components with the following properties:
 * ATTENDEE, DTSTAMP, ORGANIZER, REQUEST-STATUS, UID, URL
 * 
 * @author David Bal
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 */
public interface VComponentPersonal<T> extends VComponentPrimary<T>, VComponentAttendee<T>
{ 
    /**
     * DTSTAMP: Date-Time Stamp
     * RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     * The value MUST be specified in the UTC time format.
     * 
     * Example:
     * DTSTAMP:19971210T080000Z
     */
    ObjectProperty<DateTimeStamp> dateTimeStampProperty();
    default DateTimeStamp getDateTimeStamp() { return dateTimeStampProperty().get(); }
    default void setDateTimeStamp(String dtStamp) { setDateTimeStamp(DateTimeStamp.parse(dtStamp)); }
    default void setDateTimeStamp(DateTimeStamp dtStamp) { dateTimeStampProperty().set(dtStamp); }
    default void setDateTimeStamp(ZonedDateTime dtStamp) { setDateTimeStamp(new DateTimeStamp(dtStamp)); }
    default T withDateTimeStamp(ZonedDateTime dtStamp) { setDateTimeStamp(new DateTimeStamp(dtStamp)); return (T) this; }
    default T withDateTimeStamp(String dtStamp) { setDateTimeStamp(new DateTimeStamp(dtStamp)); return (T) this; }
    default T withDateTimeStamp(DateTimeStamp dtStamp) { setDateTimeStamp(dtStamp); return (T) this; }

    /**
     * ORGANIZER: Organizer
     * RFC 5545 iCalendar 3.8.4.3 page 111
     * This property defines the organizer for a calendar component
     * 
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     */
    ObjectProperty<Organizer> organizerProperty();
    default Organizer getOrganizer() { return organizerProperty().get(); }
    default void setOrganizer(Organizer organizer) { organizerProperty().set(organizer); }
    default T withOrganizer(String content) { setOrganizer(Organizer.parse(content)); return (T) this; }
    default T withOrganizer(Organizer organizer) { setOrganizer(organizer); return (T) this; }

    /**
     * REQUEST-STATUS: Request Status
     * RFC 5545 iCalendar 3.8.8.3 page 141
     * This property defines the status code returned for a scheduling request.
     * 
     * Examples:
     * REQUEST-STATUS:2.0;Success
     * REQUEST-STATUS:3.7;Invalid calendar user;ATTENDEE:
     *  mailto:jsmith@example.com
     * 
     */
    ObservableList<RequestStatus> getRequestStatus();
    void setRequestStatus(ObservableList<RequestStatus> properties);
    /** add comma separated requestStatus into separate comment objects */
    default T withRequestStatus(ObservableList<RequestStatus> requestStatus) { setRequestStatus(requestStatus); return (T) this; }
    default T withRequestStatus(String...requestStatus)
    {
        Arrays.stream(requestStatus).forEach(c -> PropertyEnum.REQUEST_STATUS.parse(this, c));
        return (T) this;
    }
    default T withRequestStatus(RequestStatus...requestStatus)
    {
        if (getRequestStatus() == null)
        {
            setRequestStatus(FXCollections.observableArrayList(requestStatus));
        } else
        {
            getRequestStatus().addAll(requestStatus);
        }
        return (T) this;
    }

    /**
     * UID, Unique identifier
     * RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * required property
     * 
     * Example:
     * UID:19960401T080045Z-4000F192713-0052@example.com
     */
    ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty();
    default UniqueIdentifier getUniqueIdentifier() { return uniqueIdentifierProperty().get(); }
    default void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) { uniqueIdentifierProperty().set(uniqueIdentifier); }
    default void setUniqueIdentifier(String uniqueIdentifier) { setUniqueIdentifier(UniqueIdentifier.parse(uniqueIdentifier)); }
    default T withUniqueIdentifier(String uniqueIdentifier) { setUniqueIdentifier(uniqueIdentifier); return (T) this; }
    default T withUniqueIdentifier(UniqueIdentifier uniqueIdentifier) { setUniqueIdentifier(uniqueIdentifier); return (T) this; }

    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    ObjectProperty<UniformResourceLocator> uniformResourceLocatorProperty();
    default UniformResourceLocator getUniformResourceLocator() { return uniformResourceLocatorProperty().get(); }
    default void setUniformResourceLocator(UniformResourceLocator url) { uniformResourceLocatorProperty().set(url); };
    default void setUniformResourceLocator(URI url) { setUniformResourceLocator(new UniformResourceLocator(url)); };
    default T withUniformResourceLocator(String url) { setUniformResourceLocator(UniformResourceLocator.parse(url)); return (T) this; }
    default T withUniformResourceLocator(URI url) { setUniformResourceLocator(url); return (T) this; }
    default T withUniformResourceLocator(UniformResourceLocator url) { setUniformResourceLocator(url); return (T) this; }
}
