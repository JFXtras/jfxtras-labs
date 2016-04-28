package jfxtras.labs.icalendarfx.components;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Components with the following properties:
 * COMMENT, DTSTART
 * 
 * @author David Bal
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 * @see VTimeZone
 *  */
public interface VComponentPrimary<T> extends VComponentNew<T>
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
    ObservableList<Comment> getComments();
    void setComments(ObservableList<Comment> properties);
    default T withComments(ObservableList<Comment> comments) { setComments(comments); return (T) this; }
    default T withComments(String...comments)
    {
        if (getComments() == null)
        {
            setComments(FXCollections.observableArrayList());
        }
        Arrays.stream(comments).forEach(c -> PropertyEnum.COMMENT.parse(this, c));
        return (T) this;
    }
    default T withComments(Comment...comments)
    {
        if (getComments() == null)
        {
            setComments(FXCollections.observableArrayList(comments));
        } else
        {
            getComments().addAll(comments);
        }
        return (T) this;
    }

    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.  Can be either type LocalDate or LocalDateTime
     */
    ObjectProperty<DateTimeStart<? extends Temporal>> dateTimeStartProperty();
    default DateTimeStart<? extends Temporal> getDateTimeStart() { return dateTimeStartProperty().get(); }
    default void setDateTimeStart(String dtStart) { setDateTimeStart(DateTimeStart.parse(dtStart)); }
    default void setDateTimeStart(DateTimeStart<? extends Temporal> dtStart) { dateTimeStartProperty().set(dtStart); }
    default void setDateTimeStart(Temporal temporal) { setDateTimeStart(new DateTimeStart<>(temporal)); }
    default T withDateTimeStart(DateTimeStart<? extends Temporal> dtStart)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(dtStart);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeStart(String dtStart)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(dtStart);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }
    default T withDateTimeStart(Temporal dtStart)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(dtStart);
            return (T) this;
        } else
        {
            throw new IllegalArgumentException("Property can only occur once in the calendar component");
        }
    }

    @Deprecated
    default DateTimeType getDateTimeType() { return DateTimeType.of(getDateTimeStart().getValue()); };
    default ZoneId getZoneId()
    {
//        if (getDateTimeType() == DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE)
        if (getDateTimeStart().getValue() instanceof ZonedDateTime)
        {
            return ((ZonedDateTime) getDateTimeStart().getValue()).getZone();
        }
        return null;
    }
    
    /** If subclass has date-time properties (e.g. DTEND) that must be consistent with DTSTART
     * add a listener to dateTimeStartProperty() here to check for consistency
     */
//    default void addDateTimeStartConsistencyListener() { };
    void checkDateTimeStartConsistency();
}
