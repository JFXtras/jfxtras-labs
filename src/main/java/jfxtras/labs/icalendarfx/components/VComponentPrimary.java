package jfxtras.labs.icalendarfx.components;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

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
public interface VComponentPrimary<T> extends VComponentCommon<T>
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
        Arrays.stream(comments).forEach(c -> PropertyType.COMMENT.parse(this, c));
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
    ObjectProperty<DateTimeStart> dateTimeStartProperty();
    DateTimeStart getDateTimeStart();
    default void setDateTimeStart(String dtStart)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(DateTimeStart.parse(dtStart));
        } else
        {
            DateTimeStart temp = DateTimeStart.parse(dtStart);
            if (temp.getValue().getClass().equals(getDateTimeStart().getValue().getClass()))
            {
                getDateTimeStart().setValue(temp.getValue());
            } else
            {
                setDateTimeStart(temp);
            }
        }
    }
    default void setDateTimeStart(DateTimeStart dtStart) { dateTimeStartProperty().set(dtStart); }
//    default void setDateTimeStart(Temporal temporal) { setDateTimeStart(new DateTimeStart(temporal)); }
    default void setDateTimeStart(Temporal temporal)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(new DateTimeStart(temporal));
        } else
        {
            getDateTimeStart().setValue(temporal);
        }
    }
    default T withDateTimeStart(DateTimeStart dtStart)
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
    
    /** Component is whole day if dateTimeStart (DTSTART) only contains a date (no time) */
    default boolean isWholeDay()
    {
        return ! getDateTimeStart().getValue().isSupported(ChronoUnit.NANOS);
    }

//    @Deprecated
//    default DateTimeType getDateTimeType() { return DateTimeType.of(getDateTimeStart().getValue()); };
//    @Deprecated
//    default ZoneId getZoneId()
//    {
////        if (getDateTimeType() == DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE)
//        if (getDateTimeStart().getValue() instanceof ZonedDateTime)
//        {
//            return ((ZonedDateTime) getDateTimeStart().getValue()).getZone();
//        }
//        return null;
//    }
    
    /**
     * Sorts VComponents by DTSTART date/time
     */
    final static Comparator<? super VComponentPrimary<?>> VCOMPONENT_COMPARATOR = (v1, v2) -> 
    {
        Temporal t1 = v1.getDateTimeStart().getValue();
        Temporal t2 = v2.getDateTimeStart().getValue();
        return DateTimeUtilities.TEMPORAL_COMPARATOR.compare(t1, t2);
    };
    
    /** If subclass has date-time properties (e.g. DTEND) that must be consistent with DTSTART
     * add a listener to dateTimeStartProperty() here to check for consistency
     */
    @Deprecated // rely on isValid instead
    void checkDateTimeStartConsistency();
}
