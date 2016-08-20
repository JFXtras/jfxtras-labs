package jfxtras.labs.icalendarfx.components;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
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
public abstract class VComponentPersonalBase<T> extends VComponentPrimaryBase<T> implements VComponentAttendee<T>
{
    /*
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
    public ObjectProperty<ObservableList<Attendee>> attendeesProperty()
    {
        if (attendees == null)
        {
            attendees = new SimpleObjectProperty<>(this, PropertyType.ATTENDEE.toString());
        }
        return attendees;
    }
    private ObjectProperty<ObservableList<Attendee>> attendees;
    @Override
    public ObservableList<Attendee> getAttendees() { return (attendees == null) ? null : attendees.get(); }
    @Override
    public void setAttendees(ObservableList<Attendee> attendees)
    {
        if (attendees != null)
        {
            orderer().registerSortOrderProperty(attendees);
        } else
        {
            orderer().unregisterSortOrderProperty(this.attendees.get());
        }
        attendeesProperty().set(attendees);
    }
    
    /**
     * DTSTAMP: Date-Time Stamp, from RFC 5545 iCalendar 3.8.7.2 page 137
     * This property specifies the date and time that the instance of the
     * iCalendar object was created
     */
    public ObjectProperty<DateTimeStamp> dateTimeStampProperty()
    {
        if (dateTimeStamp == null)
        {
            dateTimeStamp = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_STAMP.toString());
            orderer().registerSortOrderProperty(dateTimeStamp);
        }
        return dateTimeStamp;
    }
    private ObjectProperty<DateTimeStamp> dateTimeStamp;
    public DateTimeStamp getDateTimeStamp() { return dateTimeStampProperty().get(); }
    public void setDateTimeStamp(String dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(DateTimeStamp.parse(dtStamp));
        } else
        {
            DateTimeStamp temp = DateTimeStamp.parse(dtStamp);
            if (temp.getValue().getClass().equals(getDateTimeStamp().getValue().getClass()))
            {
                getDateTimeStamp().setValue(temp.getValue());
            } else
            {
                setDateTimeStamp(temp);
            }
        }
    }
    public void setDateTimeStamp(DateTimeStamp dtStamp) { dateTimeStampProperty().set(dtStamp); }
    public void setDateTimeStamp(ZonedDateTime dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(new DateTimeStamp(dtStamp));
        } else
        {
            getDateTimeStamp().setValue(dtStamp);
        }
    }
    public T withDateTimeStamp(ZonedDateTime dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(dtStamp);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withDateTimeStamp(String dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(dtStamp);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withDateTimeStamp(DateTimeStamp dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(dtStamp);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

    /**
     * ORGANIZER: Organizer
     * RFC 5545 iCalendar 3.8.4.3 page 111
     * This property defines the organizer for a calendar component
     * 
     * Example:
     * ORGANIZER;CN=John Smith:mailto:jsmith@example.com
     */
    public ObjectProperty<Organizer> organizerProperty()
    {
        if (organizer == null)
        {
            organizer = new SimpleObjectProperty<Organizer>(this, PropertyType.ORGANIZER.toString());
            orderer().registerSortOrderProperty(organizer);
        }
        return organizer;
    }
    public Organizer getOrganizer() { return (organizer == null) ? null : organizerProperty().get(); }
    private ObjectProperty<Organizer> organizer;
    public void setOrganizer(Organizer organizer) { organizerProperty().set(organizer); }
    public void setOrganizer(String organizer)
    {
        if (getOrganizer() == null)
        {
            setOrganizer(Organizer.parse(organizer));
        } else
        {
            Organizer temp = Organizer.parse(organizer);
            getOrganizer().setValue(temp.getValue());
        }
    }
    public T withOrganizer(String organizer)
    {
        if (getOrganizer() == null)
        {
            setOrganizer(organizer);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withOrganizer(Organizer organizer)
    {
        if (getOrganizer() == null)
        {
            setOrganizer(organizer);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

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
    public ObservableList<RequestStatus> getRequestStatus()
    {
        return requestStatus;
    }
    private ObservableList<RequestStatus> requestStatus;
    public void setRequestStatus(ObservableList<RequestStatus> requestStatus)
    {
        if (requestStatus != null)
        {
            orderer().registerSortOrderProperty(requestStatus);
        } else
        {
            orderer().unregisterSortOrderProperty(this.requestStatus);
        }
        this.requestStatus = requestStatus;
    }
    public T withRequestStatus(ObservableList<RequestStatus> requestStatus) { setRequestStatus(requestStatus); return (T) this; }
    public T withRequestStatus(String...requestStatus)
    {
        Arrays.stream(requestStatus).forEach(c -> PropertyType.REQUEST_STATUS.parse(this, c));
        return (T) this;
    }
    public T withRequestStatus(RequestStatus...requestStatus)
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
    public ObjectProperty<UniqueIdentifier> uniqueIdentifierProperty()
    {
        if (uniqueIdentifier == null)
        {
            uniqueIdentifier = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(uniqueIdentifier);
        }
        return uniqueIdentifier;
    }
    private ObjectProperty<UniqueIdentifier> uniqueIdentifier;
    public UniqueIdentifier getUniqueIdentifier() { return uniqueIdentifierProperty().get(); }
    public void setUniqueIdentifier(UniqueIdentifier uniqueIdentifier) { uniqueIdentifierProperty().set(uniqueIdentifier); }
    public void setUniqueIdentifier(String uniqueIdentifier)
    {
        if (getUniqueIdentifier() == null)
        {
            setUniqueIdentifier(UniqueIdentifier.parse(uniqueIdentifier));
        } else
        {
            UniqueIdentifier temp = UniqueIdentifier.parse(uniqueIdentifier);
            getUniqueIdentifier().setValue(temp.getValue());
        }
    }
    /** Set uniqueIdentifier by calling uidGeneratorCallback */
    public void setUniqueIdentifier() { setUniqueIdentifier(getUidGeneratorCallback().call(null)); }
    public T withUniqueIdentifier(String uniqueIdentifier)
    {
        if (getUniqueIdentifier() == null)
        {
            setUniqueIdentifier(uniqueIdentifier);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withUniqueIdentifier(UniqueIdentifier uniqueIdentifier)
    {
        if (getUniqueIdentifier() == null)
        {
            setUniqueIdentifier(uniqueIdentifier);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    /** Assign UID by using UID generator callback */
    public T withUniqueIdentifier()
    {
        if (getUniqueIdentifier() == null)
        {
            setUniqueIdentifier(getUidGeneratorCallback().call(null));
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
   
    /** Callback for creating unique uid values  */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0; // TODO - FIND WAY TO UPDATE WHEN PARSING A CALENDAR, USE X-PROP?
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    /** set UID callback generator.  This MUST be set before using the no-arg withUniqueIdentifier if
     * not using default callback.
     */
    public T withUidGeneratorCallback(Callback<Void, String> uidCallback)
    {
        setUidGeneratorCallback(uidCallback);
        return (T) this;
    }

    
    /**
     * URL: Uniform Resource Locator
     * RFC 5545 iCalendar 3.8.4.6 page 116
     * This property defines a Uniform Resource Locator (URL)
     * associated with the iCalendar object
     * 
     * Example:
     * URL:http://example.com/pub/calendars/jsmith/mytime.ics
     */
    public ObjectProperty<UniformResourceLocator> uniformResourceLocatorProperty()
    {
        if (uniformResourceLocator == null)
        {
            uniformResourceLocator = new SimpleObjectProperty<>(this, PropertyType.UNIFORM_RESOURCE_LOCATOR.toString());
            orderer().registerSortOrderProperty(uniformResourceLocator);
        }
        return uniformResourceLocator;
    }
    public UniformResourceLocator getUniformResourceLocator() { return (uniformResourceLocator == null) ? null : uniformResourceLocatorProperty().get(); }
    private ObjectProperty<UniformResourceLocator> uniformResourceLocator;
    public void setUniformResourceLocator(UniformResourceLocator url) { uniformResourceLocatorProperty().set(url); };
    public void setUniformResourceLocator(String url)
    {
        if (getUniformResourceLocator() == null)
        {
            setUniformResourceLocator(UniformResourceLocator.parse(url));
        } else
        {
            UniformResourceLocator temp = UniformResourceLocator.parse(url);
            getUniformResourceLocator().setValue(temp.getValue());
        }
    }
    public void setUniformResourceLocator(URI url) { setUniformResourceLocator(new UniformResourceLocator(url)); };
    public T withUniformResourceLocator(String url)
    {
        if (getUniformResourceLocator() == null)
        {
            setUniformResourceLocator(url);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withUniformResourceLocator(URI url)
    {
        if (getUniformResourceLocator() == null)
        {
            setUniformResourceLocator(url);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    public T withUniformResourceLocator(UniformResourceLocator url)
    {
        if (getUniformResourceLocator() == null)
        {
            setUniformResourceLocator(url);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    
    /*
     * CONSTRUCTORS
     */
    VComponentPersonalBase() { super(); }
    
//    VComponentPersonalBase(String contentLines)
//    {
//        super(contentLines);
//    }
    
    public VComponentPersonalBase(VComponentPersonalBase<T> source)
    {
        super(source);
    }
    
    @Override
    public List<String> parseContent(Iterator<String> unfoldedLineIterator, boolean useRequestStatus)
    {
        List<String> statusErrors = super.parseContent(unfoldedLineIterator, useRequestStatus);
        if (statusErrors != null)
        {
            if (statusErrors.isEmpty())
            {
                withRequestStatus("2.0;Success");
            } else
            { // Set REQUEST-STATUS for each message
                setRequestStatus(FXCollections.observableArrayList());
                statusErrors.stream().forEach(e -> getRequestStatus().add(RequestStatus.parse(e)));
            }
        }
        return statusErrors;
    }
    
    @Override
    public List<String> errors()
    {
//        List<String> errors = new ArrayList<>();
        List<String> errors = super.errors();
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
