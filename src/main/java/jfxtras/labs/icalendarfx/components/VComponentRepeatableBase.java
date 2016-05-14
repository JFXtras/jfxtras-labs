package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleCache;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
/**
 * Contains following properties:
 * @see RecurrenceRule
 * @see Recurrences
 * 
 * @author David Bal
 *
 * @param <T> - concrete subclass
 * @see DaylightSavingTime
 * @see StandardTime
 */
public abstract class VComponentRepeatableBase<T> extends VComponentPrimaryBase<T> implements VComponentRepeatable<T>
{
    /**
     * RDATE
     * Recurrence Date-Times
     * RFC 5545 iCalendar 3.8.5.2, page 120.
     * 
     * This property defines the list of DATE-TIME values for
     * recurring events, to-dos, journal entries, or time zone definitions.
     * 
     * NOTE: DOESN'T CURRENTLY SUPPORT PERIOD VALUE TYPE
     * */
    @Override
    public ObservableList<Recurrences<? extends Temporal>> getRecurrenceDates() { return recurrenceDates; }
    private ObservableList<Recurrences<? extends Temporal>> recurrenceDates;
    @Override
    public void setRecurrenceDates(ObservableList<Recurrences<? extends Temporal>> recurrenceDates)
    {
        this.recurrenceDates = recurrenceDates;
        recurrenceDates.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
        checkRecurrencesConsistency(recurrenceDates, null);
    }

    /**
     * RRULE, Recurrence Rule
     * RFC 5545 iCalendar 3.8.5.3, page 122.
     * This property defines a rule or repeating pattern for recurring events, 
     * to-dos, journal entries, or time zone definitions
     * If component is not repeating the value is null.
     * 
     * Examples:
     * RRULE:FREQ=DAILY;COUNT=10
     * RRULE:FREQ=WEEKLY;UNTIL=19971007T000000Z;WKST=SU;BYDAY=TU,TH
     */
    @Override public ObjectProperty<RecurrenceRuleNew> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyType.UNIQUE_IDENTIFIER.toString());
        }
        return recurrenceRule;
    }
    @Override
    public RecurrenceRuleNew getRecurrenceRule() { return (recurrenceRule == null) ? null : recurrenceRuleProperty().get(); }
    private ObjectProperty<RecurrenceRuleNew> recurrenceRule;
    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }    
    
//    /**
//     * Start of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
//     * This property is not a part of the iCalendar standard
//     */
//    @Override
//    public Temporal getStartRange() { return startRange; }
//    private Temporal startRange;
//    @Override
//    public void setStartRange(Temporal startRange) { this.startRange = startRange; }
//    public T withStartRange(Temporal startRange) { setStartRange(startRange); return (T) this; }
//    
//    /**
//     * End of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
//     */
//    @Override
//    public Temporal getEndRange() { return endRange; }
//    private Temporal endRange;
//    @Override
//    public void setEndRange(Temporal endRange) { this.endRange = endRange; }
//    public T withEndRange(Temporal endRange) { setEndRange(endRange); return (T) this; }
    
    @Override
    public Stream<Temporal> streamRecurrenceDates(Temporal start)
    {
        Stream<Temporal> inStream = VComponentRepeatable.super.streamRecurrenceDates(start);
        if (getRecurrenceRule() == null)
        {
            return inStream; // no cache is no recurrence rule
        }
        return recurrenceStreamer().makeCache(inStream);   // make cache of start date/times
    }
    
//    /**
//     * Recurrence instances, represented as type R, that are bounded by {@link #startRange} and {@link #endRange}
//     * The elements of the list are created by calling {@link #makeRecurrences()}
//     */
//    @Override
//    public List<R> recurrences() { return recurrences; }
//    final private List<R> recurrences = new ArrayList<>();

    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceRuleCache streamer = new RecurrenceRuleCache(this);
    @Override
    public RecurrenceRuleCache recurrenceStreamer() { return streamer; }
}
