package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRuleNew;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceStreamer;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
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
    public ObservableList<Recurrences<? extends Temporal>> getRecurrences() { return recurrences; }
    private ObservableList<Recurrences<? extends Temporal>> recurrences;
    @Override
    public void setRecurrences(ObservableList<Recurrences<? extends Temporal>> recurrences)
    {
        this.recurrences = recurrences;
        recurrences.addListener(getRecurrencesConsistencyWithDateTimeStartListener());
        checkRecurrencesConsistency(recurrences, null);
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
    
    @Override
    public Stream<Temporal> streamRecurrences(Temporal start)
    {
        // get recurrence rule stream, or make a one-element stream from DTSTART if no recurrence rule is present
        final Stream<Temporal> stream1;
        if (getRecurrenceRule() == null)
        {
            stream1 = Arrays.asList((Temporal) getDateTimeStart().getValue()).stream();
        } else
        {
            Temporal cacheStart = streamer.getStartFromCache(start);
            stream1 = getRecurrenceRule().getValue().streamRecurrences(cacheStart);
        }
        
        // assign temporal comparator to match start type
        final Comparator<Temporal> temporalComparator;
        if (start instanceof LocalDate)
        {
            temporalComparator = (t1, t2) -> ((LocalDate) t1).compareTo((LocalDate) t2);
        } else if (start instanceof LocalDateTime)
        {
            temporalComparator = (t1, t2) -> ((LocalDateTime) t1).compareTo((LocalDateTime) t2);            
        } else if (start instanceof ZonedDateTime)
        {
            temporalComparator = (t1, t2) -> ((ZonedDateTime) t1).compareTo((ZonedDateTime) t2);
        } else
        {
            throw new DateTimeException("Unsupported Temporal type:" + start.getClass().getSimpleName());
        }
        
        // add recurrences, if present
        final Stream<Temporal> stream2 = (getRecurrences() == null) ? stream1 : RecurrenceStreamer.merge(
                stream1,
                getRecurrences()
                        .stream()
                        .flatMap(r -> r.getValue().stream())
                        .map(v -> (Temporal) v)
                        .filter(t -> ! DateTimeUtilities.isBefore(t, start)) // remove too early events;
//                        .sorted(temporalComparator)
                , temporalComparator);
        
        return stream2;
    }
        
    /*
     *  RECURRENCE STREAMER
     *  produces recurrence set
     */
    private RecurrenceStreamer streamer = new RecurrenceStreamer(this);
    @Override
    public RecurrenceStreamer recurrenceStreamer() { return streamer; }
    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }
}
