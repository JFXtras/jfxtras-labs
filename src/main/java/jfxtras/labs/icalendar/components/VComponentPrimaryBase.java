package jfxtras.labs.icalendar.components;

import java.time.ZoneId;
import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import jfxtras.labs.icalendar.properties.PropertyEnum;
import jfxtras.labs.icalendar.properties.component.descriptive.Comment;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities.DateTimeType;

/**
 * 
* @author David Bal
 *
 * @param <T> - implementation subclass
 * @see VEvent
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 */
public class VComponentPrimaryBase<T> extends VComponentBase<T> implements VComponentPrimary
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
    public ObjectProperty<Comment> commentProperty()
    {
        if (comment == null) comment = new SimpleObjectProperty<Comment>(this, PropertyEnum.COMMENT.toString(), _comment);
        return comment;
    }
    private ObjectProperty<Comment> comment;
    @Override public Comment getComment() { return (comment == null) ? _comment : comment.get(); }
    private Comment _comment;
    @Override
    public void setComment(Comment comment)
    {
        if (this.comment == null)
        {
            _comment = comment;
        } else
        {
            this.comment.set(comment);            
        }
    }
    public T withComment(Comment comment) { setComment(comment); return (T) this; }

    /* DTSTART temporal class and ZoneId
     * 
     * Used to ensure the following date-time properties use the same Temporal class
     * and ZoneId (if using ZonedDateTime, null otherwise)
     * DTEND
     * RECURRENCE-ID
     * EXDATE (underlying collection of Temporals)
     * RDATE (underlying collection of Temporals)
     */
    private DateTimeType lastDtStartDateTimeType;
    DateTimeType lastDtStartDateTimeType() { return lastDtStartDateTimeType; }
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     * @SEE VDateTime
     */
    @Override
    public ObjectProperty<Temporal> dateTimeStartProperty() { return dateTimeStart; }
    final private ObjectProperty<Temporal> dateTimeStart = new SimpleObjectProperty<>(this, PropertyEnum.DATE_TIME_START.toString());
    @Override public Temporal getDateTimeStart() { return dateTimeStart.get(); }
    @Override
    public void setDateTimeStart(Temporal dtStart)
    {
        // check Temporal class is LocalDate, LocalDateTime or ZonedDateTime - others are not supported
        DateTimeType myDateTimeType = DateTimeType.of(dtStart);
        boolean changed = (lastDtStartDateTimeType != null) && (myDateTimeType != lastDtStartDateTimeType);
        lastDtStartDateTimeType = myDateTimeType;
        dateTimeStart.set(dtStart);
        
        // if type has changed then make all date-time properties the same
        if (changed)
        {
//            System.out.println("**********************start:" + dtStart);
            ensureDateTimeTypeConsistency(myDateTimeType, getZoneId());
        }
    }
    public T withDateTimeStart(Temporal dtStart) { setDateTimeStart(dtStart); return (T) this; }

    /**
     * Changes Temporal type of some properties to match the input parameter.  The input
     * parameter should be based on the DTSTART property.
     * 
     * This method runs when dateTimeStart (DTSTART) changes DateTimeType
     * 
     * @param dateTimeType - new DateTimeType
     * @param zone - ZoneId of dateTimeType, null if not applicable
     */
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
