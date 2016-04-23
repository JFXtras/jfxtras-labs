package jfxtras.labs.icalendarfx.properties.component.recurrence;

import jfxtras.labs.icalendarfx.components.DaylightSavingTime;
import jfxtras.labs.icalendarfx.components.StandardTime;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyBase;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;

/**
 * RRULE
 * Recurrence Rule
 * RFC 5545 iCalendar 3.8.5.3, page 122.
 * 
 * This property defines a rule or repeating pattern for
 * recurring events, to-dos, journal entries, or time zone definitions.
 * 
 * Produces a stream of start date/times after applying all modification rules.
 * 
 * @author David Bal
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see DaylightSavingTime
 * @see StandardTime
 */
public class RecurrenceRule extends PropertyBase<RecurrenceRuleParameter, RecurrenceRule>
{
    public RecurrenceRule(CharSequence contentLine)
    {
        super(contentLine);
    }

    public RecurrenceRule(RecurrenceRuleParameter value)
    {
        super(value);
    }

//    protected Stream<Temporal> getTemporalStream()
//    {
//        return getValue().stream().flatMap(r -> r.getValue().stream());
//    }
//    
//    /** Add date/times in RDates set */
//    public Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startTemporal)
//    {
//        if (inStream == null)
//        {
//            getTemporalStream().filter(d -> ! DateTimeUtilities.isBefore(d, startTemporal));
//        }
//        return merge(inStream
//                   , getTemporalStream()
//                   , DateTimeUtilities.TEMPORAL_COMPARATOR);
//    }
//    
//    private <T> Stream<T> merge(Stream<T> stream1, Stream<T> stream2, Comparator<T> comparator)
//    {
//            Iterator<T> iterator = new MergedIterator<T>(
//                    stream1.iterator()
//                  , stream2.iterator()
//                  , comparator);
//            Spliterator<T> spliterator = new SpliteratorAdapter<>(iterator);
//            return StreamSupport.stream(spliterator, false);
//    }
}
