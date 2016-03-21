package jfxtras.labs.icalendar.components;

import java.time.ZonedDateTime;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;

/**
 * VComponents that involve communication between people
 * 
 * @author David Bal
 * @see VEventOld
 * @see VTodoOld
 * @see VJournalOld
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
    Attendee getAttendee();
    ObjectProperty<Attendee> attendeeProperty();
    void setAttendee(Attendee attendee);
    
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
    ZonedDateTime getDateTimeStamp();
    ObjectProperty<ZonedDateTime> dateTimeStampProperty();
    void setDateTimeStamp(ZonedDateTime dtStamp);
    
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
    String getRequestStatus();
    StringProperty requestStatusProperty();
    void setRequestStatus(String uid);
    
    /**
     * UID, Unique identifier
     * RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * 
     * Example:
     * UID:19960401T080045Z-4000F192713-0052@example.com
     */
    String getUniqueIdentifier();
    StringProperty uniqueIdentifierProperty();
    void setUniqueIdentifier(String s);
    
    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    String getUniformResourceLocator();
    StringProperty uniformResourceLocatorProperty();
    void setUniformResourceLocator(String s);
}
