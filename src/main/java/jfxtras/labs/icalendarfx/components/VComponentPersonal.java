package jfxtras.labs.icalendarfx.components;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.properties.PropertyType;
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
    default void setDateTimeStamp(String dtStamp)
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
    default void setDateTimeStamp(DateTimeStamp dtStamp) { dateTimeStampProperty().set(dtStamp); }
    default void setDateTimeStamp(ZonedDateTime dtStamp)
    {
        if (getDateTimeStamp() == null)
        {
            setDateTimeStamp(new DateTimeStamp(dtStamp));
        } else
        {
            getDateTimeStamp().setValue(dtStamp);
        }
    }
    default T withDateTimeStamp(ZonedDateTime dtStamp)
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
    default T withDateTimeStamp(String dtStamp)
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
    default T withDateTimeStamp(DateTimeStamp dtStamp)
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
    ObjectProperty<Organizer> organizerProperty();
    Organizer getOrganizer();
    default void setOrganizer(Organizer organizer) { organizerProperty().set(organizer); }
    default void setOrganizer(String organizer)
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
    default T withOrganizer(String organizer)
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
    default T withOrganizer(Organizer organizer)
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
    ObservableList<RequestStatus> getRequestStatus();
    void setRequestStatus(ObservableList<RequestStatus> properties);
    default T withRequestStatus(ObservableList<RequestStatus> requestStatus) { setRequestStatus(requestStatus); return (T) this; }
    default T withRequestStatus(String...requestStatus)
    {
        Arrays.stream(requestStatus).forEach(c -> PropertyType.REQUEST_STATUS.parse(this, c));
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
    default void setUniqueIdentifier(String uniqueIdentifier)
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
    default void setUniqueIdentifier() { setUniqueIdentifier(getUidGeneratorCallback().call(null)); }
    default T withUniqueIdentifier(String uniqueIdentifier)
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
    default T withUniqueIdentifier(UniqueIdentifier uniqueIdentifier)
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
    
    /** Callback for creating unique uid values  */
    Callback<Void, String> getUidGeneratorCallback();
    void setUidGeneratorCallback(Callback<Void, String> uidCallback);
    
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
    UniformResourceLocator getUniformResourceLocator();
    default void setUniformResourceLocator(UniformResourceLocator url) { uniformResourceLocatorProperty().set(url); };
    default void setUniformResourceLocator(String url)
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
    default void setUniformResourceLocator(URI url) { setUniformResourceLocator(new UniformResourceLocator(url)); };
    default T withUniformResourceLocator(String url)
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
    default T withUniformResourceLocator(URI url)
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
    default T withUniformResourceLocator(UniformResourceLocator url)
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
}
