package jfxtras.labs.icalendarfx.components;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;

/**
 * Interface for {@link Attendee} property
 * 
 * @author David Bal
 *
 * @param <T>  concrete subclass
 */
public interface VComponentAttendee<T> extends VComponent
{
    /**
     * <p>This property defines an "Attendee" within a calendar component.<br>
     * RFC 5545 iCalendar 3.8.4.1 page 107</p>
     * 
     * <p>Examples:
     * <ul>
     * <l1>ATTENDEE;MEMBER="mailto:DEV-GROUP@example.com":<br>
     *  mailto:joecool@example.com
     * <l1>ATTENDEE;ROLE=REQ-PARTICIPANT;PARTSTAT=ACCEPTED;CN=Jane Doe<br>
     *  :mailto:jdoe@example.com
     *  </ul>
     */
    ObjectProperty<ObservableList<Attendee>> attendeesProperty();
    ObservableList<Attendee> getAttendees();
    void setAttendees(ObservableList<Attendee> properties);
    /**
     *  Sets the value of the {@link #attendeesProperty()} }
     *  
     *  @return - this class for chaining
     */
    default T withAttendees(ObservableList<Attendee> attendees) { setAttendees(attendees); return (T) this; }
    /**
     * Sets the value of the {@link #attendeesProperty()} from a vararg of {@link Attendee} objects.
     * 
     * @return - this class for chaining
     */    
    default T withAttendees(Attendee...attendees)
    {
//        if (getAttendees() == null)
//        {
            setAttendees(FXCollections.observableArrayList(attendees));
//        } else
//        {
//            getAttendees().addAll(attendees);
//        }
        return (T) this;
    }
    /**
     * <p>Sets the value of the {@link #attendeesProperty()} by parsing a vararg of
     * iCalendar content text representing individual {@link Attendee} objects.</p>
     * 
     * @return - this class for chaining
     */    
    default T withAttendees(String...attendees)
    {
        Arrays.stream(attendees).forEach(c -> PropertyType.ATTENDEE.parse(this, c));
        return (T) this;
    }
}
