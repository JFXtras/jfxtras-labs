package jfxtras.labs.icalendarfx.components;

import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;

@Deprecated // put into abstract class when done building VTodo
public interface VTodoInt<T> extends VComponentLocatable<T>
{
    /**
     * COMPLETED: Date-Time Completed
     * RFC 5545 iCalendar 3.8.2.1 page 94
     * This property defines the date and time that a to-do was
     * actually completed.
     * The value MUST be specified in the UTC time format.
     * 
     * Example:
     * COMPLETED:19960401T150000Z
     */
    ZonedDateTime getDateTimeCompleted();
    ObjectProperty<ZonedDateTime> dateTimeCompletedProperty();
    void setDateTimeCompleted(ZonedDateTime dateTimeCompleted);
    
    /**
     * DUE: Date-Time Due
     * RFC 5545 iCalendar 3.8.2.3 page 96
     * This property defines the date and time that a to-do is
     * expected to be completed.
     * the value type of this property MUST be the same as the "DTSTART" property
     * 
     * Example:
     * DUE:TZID=America/Los_Angeles:19970512T090000
     */
    Temporal getDateTimeDue();
    ObjectProperty<Temporal> dateTimeDueProperty();
    void setDateTimeDue(Temporal dateTimeDue);
    
    /**
     * PERCENT-COMPLETE
     * RFC 5545 iCalendar 3.8.1.8. page 88
     * 
     * This property is used by an assignee or delegatee of a
     * to-do to convey the percent completion of a to-do to the
     * "Organizer".
     * 
     * Example:
     * PERCENT-COMPLETE:39
     */
    int getPercentComplete();
    IntegerProperty percentCompleteProperty();
    void setPercentComplete(int percentComplete);
}
