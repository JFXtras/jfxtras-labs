package jfxtras.labs.icalendar.components;

import java.time.DateTimeException;
import java.time.ZoneOffset;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.PropertyEnum;
import jfxtras.labs.icalendar.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendar.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendar.properties.component.relationship.Attendee;
import jfxtras.labs.icalendar.properties.component.relationship.Organizer;
import jfxtras.labs.icalendar.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendar.properties.component.relationship.UniqueIdentifier;

/**
 * 
* @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 */
public abstract class VComponentPersonalBase<T> extends VComponentPrimaryBase<T> implements VComponentPersonal
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
//    @Override
//    public ObjectProperty<Attendee> attendeeProperty()
//    {
//        if (attendee == null) attendee = new SimpleObjectProperty<Attendee>(this, PropertyEnum.ATTENDEE.toString(), _attendee);
//        return attendee;
//    }
//    private ObjectProperty<Attendee> attendee;
//    @Override public Attendee getAttendee() { return (attendee == null) ? _attendee : attendee.get(); }
//    private Attendee _attendee;
//    @Override
//    public void setAttendee(Attendee attendee)
//    {
//        if (this.attendee == null)
//        {
//            _attendee = attendee;
//        } else
//        {
//            this.attendee.set(attendee);            
//        }
//    }
//    public T withAttendee(Attendee attendee) { setAttendee(attendee); return (T) this; }
    @Override
    public ObservableList<Attendee> attendees()
    {
        if (attendees == null)
        {
            attendees = FXCollections.observableArrayList();
            attendees.addListener((InvalidationListener) (obs) ->
            {
                int size = attendees().size();
                if (size > 0)
                {
                    propertyMap().put(PropertyEnum.ATTENDEE, attendees());
                } else if (size == 0)
                {
                    propertyMap().remove(PropertyEnum.ATTENDEE);
                }
            });
        }
        return attendees;
    }
    private ObservableList<Attendee> attendees;
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    @Override
    public ObjectProperty<DateTimeStamp> dateTimeStampProperty() { return dateTimeStamp; }
    final private ObjectProperty<DateTimeStamp> dateTimeStamp = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_STAMP.toString());
    @Override
    public DateTimeStamp getDateTimeStamp() { return dateTimeStamp.get(); }
    @Override
    public void setDateTimeStamp(DateTimeStamp dtStamp)
    {
        if ((dtStamp != null) && ! (dtStamp.getValue().getOffset().equals(ZoneOffset.UTC)))
        {
            throw new DateTimeException("dateTimeStamp (DTSTAMP) must be specified in the UTC time format (Z)");
        }
        dateTimeStamp.set(dtStamp);
//        propertyMap().put(PropertyEnum.DATE_TIME_STAMP, new ArrayList<DateTimeStamp>(Arrays.asList(dtStamp)));
        propertyMap().put(PropertyEnum.DATE_TIME_STAMP, dtStamp);
    }
    public T withDateTimeStamp(DateTimeStamp dtStamp) { setDateTimeStamp(dtStamp); return (T) this; }

    /**
     * ORGANIZER: Organizer
     * RFC 5545 iCalendar 3.8.4.3 page 111
     * This property defines the organizer for a calendar component
     * 
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     */
    @Override
    public ObjectProperty<Organizer> organizerProperty()
    {
        if (organizer == null) organizer = new SimpleObjectProperty<Organizer>(this, PropertyEnum.ORGANIZER.toString(), _organizer);
        return organizer;
    }
    private ObjectProperty<Organizer> organizer;
    @Override public Organizer getOrganizer() { return (organizer == null) ? _organizer : organizer.get(); }
    private Organizer _organizer;
    @Override
    public void setOrganizer(Organizer organizer)
    {
        if (this.organizer == null)
        {
            _organizer = organizer;
        } else
        {
            this.organizer.set(organizer);            
        }
        propertyMap().put(PropertyEnum.ORGANIZER, organizer);
    }
    public T withOrganizer(Organizer organizer) { setOrganizer(organizer); return (T) this; }

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
//    @Override
//    public ObjectProperty<RequestStatus> requestStatusProperty()
//    {
//        if (requestStatus == null) requestStatus = new SimpleObjectProperty<RequestStatus>(this, PropertyEnum.ORGANIZER.toString(), _requestStatus);
//        return requestStatus;
//    }
//    private ObjectProperty<RequestStatus> requestStatus;
//    @Override public RequestStatus getRequestStatus() { return (requestStatus == null) ? _requestStatus : requestStatus.get(); }
//    private RequestStatus _requestStatus;
//    @Override
//    public void setRequestStatus(RequestStatus requestStatus)
//    {
//        if (this.requestStatus == null)
//        {
//            _requestStatus = requestStatus;
//        } else
//        {
//            this.requestStatus.set(requestStatus);            
//        }
//    }
//    public T withRequestStatus(RequestStatus requestStatus) { setRequestStatus(requestStatus); return (T) this; }
    @Override
    public ObservableList<RequestStatus> requestStatus()
    {
        if (requestStatus == null)
        {
            requestStatus = FXCollections.observableArrayList();
            requestStatus.addListener((InvalidationListener) (obs) ->
            {
                int size = requestStatus().size();
                if (size > 0)
                {
                    propertyMap().put(PropertyEnum.REQUEST_STATUS, requestStatus());
                } else if (size == 0)
                {
                    propertyMap().remove(PropertyEnum.REQUEST_STATUS);
                }
            });
        }
        return requestStatus;
    }
    private ObservableList<RequestStatus> requestStatus;

    /**
     * UID, Unique identifier
     * RFC 5545, iCalendar 3.8.4.7 page 117
     * A globally unique identifier for the calendar component.
     * required property
     * 
     * Example:
     * UID:19960401T080045Z-4000F192713-0052@example.com
     */
    @Override public ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty() { return uniqueIdentifier; }
    private ObjectProperty<UniqueIdentifier> uniqueIdentifier;
    @Override public UniqueIdentifier getUniqueIdentifier() { return uniqueIdentifier.get(); }
    @Override public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier)
    {
        this.uniqueIdentifier.set(uniqueIdentifier);
        propertyMap().put(PropertyEnum.UNIQUE_IDENTIFIER, uniqueIdentifier);
    }
    public void withUniqueIdentifier(UniqueIdentifier uniqueIdentifier) { setUniqueIdentifier(uniqueIdentifier); }

    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    @Override
    public ObjectProperty<UniformResourceLocator> uniformResourceLocatorProperty()
    {
        if (uniformResourceLocator == null) uniformResourceLocator = new SimpleObjectProperty<UniformResourceLocator>(this, PropertyEnum.ORGANIZER.toString(), _uniformResourceLocator);
        return uniformResourceLocator;
    }
    private ObjectProperty<UniformResourceLocator> uniformResourceLocator;
    @Override public UniformResourceLocator getUniformResourceLocator() { return (uniformResourceLocator == null) ? _uniformResourceLocator : uniformResourceLocator.get(); }
    private UniformResourceLocator _uniformResourceLocator;
    @Override
    public void setUniformResourceLocator(UniformResourceLocator url)
    {
        if (this.uniformResourceLocator == null)
        {
            _uniformResourceLocator = url;
        } else
        {
            this.uniformResourceLocator.set(url);            
        }
        propertyMap().put(PropertyEnum.UNIFORM_RESOURCE_LOCATOR, url);
    }
    public T withUniformResourceLocator(UniformResourceLocator uniformResourceLocator) { setUniformResourceLocator(uniformResourceLocator); return (T) this; }

}
