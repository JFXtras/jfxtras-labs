package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;

/**
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see VComponentPersonal
 * @see VAlarm
 */
public interface VComponentAttendee<T> extends VComponentNew<T>
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
    default T withAttendees(ObservableList<Attendee> attendees) { setAttendees(attendees); return (T) this; }
    default T withAttendees(Attendee...attendees)
    {
        if (getAttendees() == null)
        {
            setAttendees(FXCollections.observableArrayList(attendees));
        } else
        {
            getAttendees().addAll(attendees);
        }
        return (T) this;
    }
    default T withAttendees(String...attendees)
    {
        Arrays.stream(attendees).forEach(c -> PropertyType.ATTENDEE.parse(this, c));
        return (T) this;
    }
}
