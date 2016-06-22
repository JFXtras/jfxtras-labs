package jfxtras.labs.icalendarfx.components;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;
import jfxtras.labs.icalendarfx.properties.component.relationship.Organizer;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniformResourceLocator;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

/**
 * Components with the following properties:
 * ATTENDEE, DTSTAMP, ORGANIZER, REQUEST-STATUS, UID, URL
 * 
 * @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 */
public abstract class VComponentPersonalBase<T> extends VComponentPrimaryBase<T> implements VComponentPersonal<T>
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
    @Override
    public ObservableList<Attendee> getAttendees() { return attendees; }
    private ObservableList<Attendee> attendees;
    @Override
    public void setAttendees(ObservableList<Attendee> attendees)
    {
        if (attendees != null)
        {
            registerSortOrderProperty(attendees);
        } else
        {
            unregisterSortOrderProperty(this.attendees);
        }
        this.attendees = attendees;
    }
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    @Override
    public ObjectProperty<DateTimeStamp> dateTimeStampProperty()
    {
        if (dateTimeStamp == null)
        {
            dateTimeStamp = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_STAMP.toString());
            registerSortOrderProperty(dateTimeStamp);
        }
        return dateTimeStamp;
    }
    private ObjectProperty<DateTimeStamp> dateTimeStamp;

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
        if (organizer == null)
        {
            organizer = new SimpleObjectProperty<Organizer>(this, PropertyType.ORGANIZER.toString());
            registerSortOrderProperty(organizer);
        }
        return organizer;
    }
    @Override
    public Organizer getOrganizer() { return (organizer == null) ? null : organizerProperty().get(); }
    private ObjectProperty<Organizer> organizer;

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
    @Override
    public ObservableList<RequestStatus> getRequestStatus()
    {
        return requestStatus;
    }
    private ObservableList<RequestStatus> requestStatus;
    @Override
    public void setRequestStatus(ObservableList<RequestStatus> requestStatus)
    {
        if (requestStatus != null)
        {
            registerSortOrderProperty(requestStatus);
        } else
        {
            unregisterSortOrderProperty(this.requestStatus);
        }
        this.requestStatus = requestStatus;
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
    @Override public ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty()
    {
        if (uniqueIdentifier == null)
        {
            uniqueIdentifier = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
            registerSortOrderProperty(uniqueIdentifier);
        }
        return uniqueIdentifier;
    }
    private ObjectProperty<UniqueIdentifier> uniqueIdentifier;

    /** Callback for creating unique uid values  */
    @Override
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0; // TODO - FIND WAY TO UPDATE WHEN PARSING A CALENDAR, USE X-PROP?
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    @Override
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }

    
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
        if (uniformResourceLocator == null)
        {
            uniformResourceLocator = new SimpleObjectProperty<>(this, PropertyType.UNIFORM_RESOURCE_LOCATOR.toString());
            registerSortOrderProperty(uniformResourceLocator);
        }
        return uniformResourceLocator;
    }
    @Override
    public UniformResourceLocator getUniformResourceLocator() { return (uniformResourceLocator == null) ? null : uniformResourceLocatorProperty().get(); }
    private ObjectProperty<UniformResourceLocator> uniformResourceLocator;

    /*
     * CONSTRUCTORS
     */
    VComponentPersonalBase() { super(); }
    
    VComponentPersonalBase(String contentLines)
    {
        super(contentLines);
    }
    
    public VComponentPersonalBase(VComponentPersonalBase<T> source)
    {
        super(source);
    }

//    @Override
//    public boolean isValid()
//    {
//        boolean isDateTimeStampPresent = getDateTimeStamp() != null;
//        boolean isUniqueIdentifier = getUniqueIdentifier() != null;
//        return isDateTimeStampPresent && isUniqueIdentifier;
//    }
    
    @Override
    public List<String> errors()
    {
        List<String> errors = new ArrayList<>();
//        List<String> errors = super.errors();
        if (getDateTimeStamp() == null)
        {
            errors.add("DTSTAMP is not present.  DTSTAMP is REQUIRED and MUST NOT occur more than once");
        }
        if (getUniqueIdentifier() == null)
        {
            errors.add("UID is not present.  UID is REQUIRED and MUST NOT occur more than once");
        }
        return errors;
    }
}
