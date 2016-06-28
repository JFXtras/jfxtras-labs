package jfxtras.labs.icalendarfx.components;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Comment;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;

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
public abstract class VComponentPrimaryBase<T> extends VComponentCommonBase<T> implements VComponentPrimary<T>
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
    public ObservableList<Comment> getComments() { return comments; }
    private ObservableList<Comment> comments;
    @Override
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
    
    /**
     * DTSTART: Date-Time Start, from RFC 5545 iCalendar 3.8.2.4 page 97
     * Start date/time of repeat rule.  Used as a starting point for making the Stream<LocalDateTime> of valid
     * start date/times of the repeating events.
     * Can contain either a LocalDate (DATE) or LocalDateTime (DATE-TIME)
     */
    @Override
    public ObjectProperty<DateTimeStart> dateTimeStartProperty()
    {
        if (dateTimeStart == null)
        {
            dateTimeStart = new SimpleObjectProperty<>(this, PropertyType.DATE_TIME_START.toString());
            orderer().registerSortOrderProperty(dateTimeStart);
        }
        return dateTimeStart;
    }
    @Override
    public DateTimeStart getDateTimeStart() { return (dateTimeStart == null) ? null : dateTimeStartProperty().get(); }
    private ObjectProperty<DateTimeStart> dateTimeStart;

    /*
     * CONSTRUCTORS
     */
    VComponentPrimaryBase() { super(); }
    
    VComponentPrimaryBase(String contentLines)
    {
        super(contentLines);
    }

    public VComponentPrimaryBase(VComponentPrimaryBase<T> source)
    {
        super(source);
    }
}
