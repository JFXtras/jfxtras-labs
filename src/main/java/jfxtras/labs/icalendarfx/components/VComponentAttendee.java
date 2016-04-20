package jfxtras.labs.icalendarfx.components;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.component.relationship.Attendee;

public interface VComponentAttendee<T>
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
}
