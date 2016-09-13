package jfxtras.labs.icalendarfx.components;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
 *
 * @param <T> - implementation subclass
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 * @see VTimeZone
 */
public abstract class VPrimary<T> extends VCommon<T>
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
    public ObservableList<Comment> getComments() { return comments; }
    private ObservableList<Comment> comments;
    public void setComments(ObservableList<Comment> comments)
    {
        if (comments != null)
        {
            orderer().registerSortOrderProperty(comments);
        } else
        {
            orderer().unregisterSortOrderProperty(this.comments);
        }
        this.comments = comments;
    }
    public T withComments(ObservableList<Comment> comments) { setComments(comments); return (T) this; }
    public T withComments(String...comments)
    {
        Arrays.stream(comments).forEach(c -> PropertyType.COMMENT.parse(this, c));
        return (T) this;
    }
    public T withComments(Comment...comments)
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
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     */
    public ObjectProperty<DateTimeStart> dateTimeStartProperty()
    {
        if (dateTimeStart == null)
        {
            dateTimeStart = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_START.toString());
            orderer().registerSortOrderProperty(dateTimeStart);
            dateTimeStart.addListener((obs, oldValue, newValue) ->
            {
                if (oldValue == null) // only check consistency when first assignment
                {
                    dateTimeStartListenerHook();
                }
            });
        }
        return dateTimeStart;
    }
    
    // hook to be overridden in subclasses
    void dateTimeStartListenerHook() { }
    
    public DateTimeStart getDateTimeStart() { return (dateTimeStart == null) ? null : dateTimeStartProperty().get(); }
    private ObjectProperty<DateTimeStart> dateTimeStart;
    public void setDateTimeStart(String dtStart)
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
    public void setDateTimeStart(DateTimeStart dtStart) { dateTimeStartProperty().set(dtStart); }
    public void setDateTimeStart(Temporal temporal)
    {
        if (getDateTimeStart() == null)
        {
            setDateTimeStart(new DateTimeStart(temporal));
        } else
        {
            getDateTimeStart().setValue(temporal);
        }
    }
    public T withDateTimeStart(DateTimeStart dtStart)
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
    public T withDateTimeStart(String dtStart)
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
    public T withDateTimeStart(Temporal dtStart)
    {
//        if (getDateTimeStart() == null)
//        {
        setDateTimeStart(dtStart);
        return (T) this;
//        } else
//        {
//            throw new IllegalArgumentException("Property can only occur once in the calendar component");
//        }
    }
    
    /** Component is whole day if dateTimeStart (DTSTART) only contains a date (no time) */
    public boolean isWholeDay()
    {
        return ! getDateTimeStart().getValue().isSupported(ChronoUnit.NANOS);
    }
    
    /*
     * CONSTRUCTORS
     */
    VPrimary() { super(); }

    public VPrimary(VPrimary<T> source)
    {
        super(source);
    }
    
    /**
     * Sorts VComponents by DTSTART date/time
     */
    public final static Comparator<? super VPrimary<?>> VPRIMARY_COMPARATOR = (v1, v2) -> 
    {
        Temporal t1 = v1.getDateTimeStart().getValue();
        Temporal t2 = v2.getDateTimeStart().getValue();
        return DateTimeUtilities.TEMPORAL_COMPARATOR2.compare(t1, t2);
    };
}
