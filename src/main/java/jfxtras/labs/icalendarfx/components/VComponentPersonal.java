package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

/**
 * Components with the following properties:
 * ATTENDEE, DTSTAMP, ORGANIZER, REQUEST-STATUS, UID, URL
 * 
 * @author David Bal
 * @see VEventNewInt
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public interface VComponentPersonal extends VComponentPrimary
{
    /**
     * ATTENDEE: Attendee
     * RFC 5545 iCalendar 3.8.4.1 page 107
     * This property defines an "Attendee" within a calendar component.
     * 
     * Examples:
     * ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":
     *  mailto:joecool@example.com
     * ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe
     *  :mailto:jdoe@example.com
     */
    ObservableList<Attendee> getAttendees();
    void setAttendees(ObservableList<Attendee> properties);
    
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
    DateTimeStamp getDateTimeStamp();
    ObjectProperty<DateTimeStamp> dateTimeStampProperty();
    void setDateTimeStamp(DateTimeStamp dtStamp);
    
    /**
     * ORGANIZER: Organizer
     * RFC 5545 iCalendar 3.8.4.3 page 111
     * This property defines the organizer for a calendar component
     * 
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     */
    Organizer getOrganizer();
    ObjectProperty<Organizer> organizerProperty();
    void setOrganizer(Organizer organizer);
    
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
    
    /**
     * UID, Unique identifier
     * RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * required property
     * 
     * Example:
     * UID:19960401T080045Z-4000F192713-0052@example.com
     */
    UniqueIdentifier getUniqueIdentifier();
    ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty();
    void setUniqueIdentifier(UniqueIdentifier uid);
    
    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    UniformResourceLocator getUniformResourceLocator();
    ObjectProperty<UniformResourceLocator> uniformResourceLocatorProperty();
    void setUniformResourceLocator(UniformResourceLocator uri);
}
