package jfxtras.labs.icalendar.components;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities.DateTimeType;

/**
 * A calendar component that can contain a child event, such as an Alarm
 * 
 * @author David Bal
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 *  */
public interface VComponentPrimary extends VComponent
{
    /**
     *  COMMENT: RFC 5545 iCalendar 3.8.1.12. page 83
     * This property specifies non-processing information intended
      to provide a comment to the calendar user.
     * Example:
     * COMMENT:The meeting really needs to include both ourselves
         and the customer. We can't hold this meeting without them.
         As a matter of fact\, the venue for the meeting ought to be at
         their site. - - John
     * */
    Comment getComment();
    ObjectProperty<Comment> commentProperty();
    void setComment(Comment comment);

    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.  Can be either type LocalDate or LocalDateTime
     */
    Temporal getDateTimeStart();
    ObjectProperty<Temporal> dateTimeStartProperty();
    void setDateTimeStart(Temporal dtStart);
    default DateTimeType getDateTimeType() { return DateTimeType.of(getDateTimeStart()); };
    default ZoneId getZoneId()
    {
        if (getDateTimeType() == DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE)
        {
            return ((ZonedDateTime) getDateTimeStart()).getZone();
        }
        return null;
    }
}
