package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;

/**
 * Components with the following properties:
 * COMMENT, DTSTART
 * 
 * @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEventNewInt
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 */
public abstract class VComponentPrimaryBase<T> extends VComponentBase<T> implements VComponentPrimary
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
    @Override
    public ObservableList<Comment> getComments()
    {
        return comments;
    }
    private ObservableList<Comment> comments;
    @Override
    public void setComments(ObservableList<Comment> comments) { this.comments = comments; }
    /** add comma separated comments into separate comment objects */
    public T withComments(ObservableList<Comment> comments) { setComments(comments); return (T) this; }
    public T withComments(String...comments)
    {
//        if (this.comments == null)
//        {
//            this.comments = FXCollections.observableArrayList();
//        }
//        Arrays.stream(comments).forEach(c -> getComments().add(new Comment(c)));
        Arrays.stream(comments).forEach(c -> PropertyEnum.COMMENT.parse(this, c));
        return (T) this;
    }
    
    // TODO - MY BE OBSOLETE - IF NOT USED DELETE
    /* previous temporal backing DTSTART
     * 
     * Used to ensure the following date-time properties use the same Temporal class
     * and ZoneId (if using ZonedDateTime, null otherwise)
     * DTEND
     * RECURRENCE-ID
     * EXDATE (underlying collection of Temporals)
     * RDATE (underlying collection of Temporals)
     */
    @Deprecated
    private Temporal lastDtStart;
    @Deprecated
    Temporal lastDtStart() { return lastDtStart; }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     * @SEE VDateTime
     */
    @Override public ObjectProperty<DateTimeStart<? extends Temporal>> dateTimeStartProperty()
    {
        if (dateTimeStart == null)
        {
            dateTimeStart = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_START.toString());
        }
        return dateTimeStart;
    }
    private ObjectProperty<DateTimeStart<? extends Temporal>> dateTimeStart;
    @Override public DateTimeStart<? extends Temporal> getDateTimeStart() { return dateTimeStartProperty().get(); }
    @Override
    public void setDateTimeStart(DateTimeStart<? extends Temporal> dtStart)
    {
        dateTimeStartProperty().set(dtStart);
    }
    public void setDateTimeStart(Temporal temporal)
    {
        if (temporal instanceof LocalDate)
        {
            setDateTimeStart(new DateTimeStart<LocalDate>((LocalDate) temporal));            
        } else if (temporal instanceof LocalDateTime)
        {
            setDateTimeStart(new DateTimeStart<LocalDateTime>((LocalDateTime) temporal));            
        } else if (temporal instanceof ZonedDateTime)
        {
            setDateTimeStart(new DateTimeStart<ZonedDateTime>((ZonedDateTime) temporal));            
        } else
        {
            throw new DateTimeException("Only LocalDate, LocalDateTime and ZonedDateTime supported. "
                    + temporal.getClass().getSimpleName() + " is not supported");
        }
    }
    public T withDateTimeStart(DateTimeStart<? extends Temporal> dtStart) { setDateTimeStart(dtStart); return (T) this; }
    public T withDateTimeStart(String dtStart) { return withDateTimeStart(DateTimeUtilities.temporalFromString(dtStart)); }
    public T withDateTimeStart(Temporal temporal) { setDateTimeStart(temporal); return (T) this; }

    /*
     * CONSTRUCTORS
     */
    VComponentPrimaryBase() { }
    
    VComponentPrimaryBase(String contentLines)
    {
        super(contentLines);
    }
    
    /**
     * Changes Temporal type of some properties to match the input parameter.  The input
     * parameter should be based on the DTSTART property.
     * 
     * This method runs when dateTimeStart (DTSTART) changes DateTimeType
     * 
     * @param dateTimeType - new DateTimeType
     * @param zone - ZoneId of dateTimeType, null if not applicable
     */
    // HOW AM I GOING TO RUN THIS WHEN THE OTHER PROPERTIES ARE IN LOWER CLASSES?
    // DO I NEED LISTENERS?
    void ensureDateTimeTypeConsistency(DateTimeType dateTimeType, ZoneId zone)
    {
        // does something as overridden method in subclasses
        // RECURRENCE-ID (of children)
//        System.out.println("ensureDateTimeTypeConsistency:" + dateTimeType);
//        if (getRRule() != null && getRRule().recurrences() != null)
//        {
//            getRRule().recurrences().forEach(v ->
//            {
////                Temporal newDateTimeRecurrence = DateTimeType.changeTemporal(v.getDateTimeRecurrence(), dateTimeType);
//                Temporal newDateTimeRecurrence = dateTimeType.from(v.getDateTimeRecurrence(), zone);
//                v.setDateTimeRecurrence(newDateTimeRecurrence);
//            });
//        }
//        
//        // EXDATE
//        if (getExDate() != null)
//        {
//            Temporal firstTemporal = getExDate().getTemporals().iterator().next();
//            DateTimeType exDateDateTimeType = DateTimeType.of(firstTemporal);
//            if (dateTimeType != exDateDateTimeType)
//            {
//                Set<Temporal> newExDateTemporals = getExDate().getTemporals()
//                        .stream()
////                        .map(t -> DateTimeType.changeTemporal(t, dateTimeType))
//                        .map(t -> dateTimeType.from(t, zone))
//                        .collect(Collectors.toSet());
//                getExDate().getTemporals().clear();
//                getExDate().getTemporals().addAll(newExDateTemporals);
//            }
//        }
//        
//        // RDATE
//        if (getRDate() != null)
//        {
//            Temporal firstTemporal = getRDate().getTemporals().iterator().next();
//            DateTimeType rDateDateTimeType = DateTimeType.of(firstTemporal);
//            if (dateTimeType != rDateDateTimeType)
//            {
//                Set<Temporal> newRDateTemporals = getRDate().getTemporals()
//                        .stream()
////                        .map(t -> DateTimeType.changeTemporal(t, dateTimeType))
//                        .map(t -> dateTimeType.from(t, zone))
//                        .collect(Collectors.toSet());
//                getRDate().getTemporals().clear();
//                getRDate().getTemporals().addAll(newRDateTemporals);
//            }
//        }
//        
//        // RANGE
//        if (getStartRange() != null)
//        {
//            setStartRange(getStartRange());
//        }
//        if (getEndRange() != null)
//        {
//            setEndRange(getEndRange());
//        }
    }

}
