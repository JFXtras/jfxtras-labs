package jfxtras.labs.icalendarfx.components;

import java.time.temporal.Temporal;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.Recurrences;
/**
 * 
 * @author David Bal
 *
 * @param <T>
 * @see DaylightSavingTime
 * @see StandardTime
 */
@Deprecated // not being used I think
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
//        validateRecurrencesConsistencyWithDTStart();
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
    @Override public ObjectProperty<RecurrenceRule> recurrenceRuleProperty()
    {
        if (recurrenceRule == null)
        {
            recurrenceRule = new SimpleObjectProperty<>(this, PropertyEnum.UNIQUE_IDENTIFIER.toString());
        }
        return recurrenceRule;
    }
    private ObjectProperty<RecurrenceRule> recurrenceRule;

//    protected Stream<Temporal> getTemporalStream()
//    {
//        return getRecurrences().stream().flatMap(r -> r.getValue().stream());
//    }
//    
////    // put in subclass
////    @Override
////    public Stream<Temporal> streamRecurrence(Temporal startTemporal)
////    {
////        return getTemporalStream()
////                .filter(d -> ! DateTimeUtilities.isBefore(d, startTemporal));
////    }
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
    
    /*
     * CONSTRUCTORS
     */
    VComponentRepeatableBase() { }
    
    VComponentRepeatableBase(String contentLines)
    {
        super(contentLines);
    }
    
//    /*
//     * Recommend using with StreamSupport.stream(iteratorStream, false);
//     */
//    private class SpliteratorAdapter<T> extends Spliterators.AbstractSpliterator <T>
//    {
//        private final Iterator<T> iterator;
//     
//        public SpliteratorAdapter(Iterator<T> iter) {
//            super(Long.MAX_VALUE, 0);
//            iterator = iter;
//        }
//     
//        @Override
//        public synchronized boolean tryAdvance(Consumer<? super T> action) {
//            if(iterator.hasNext()) {
//                action.accept(iterator.next());
//                return true;
//            }
//            return false;
//        }
//    }        
//
//    /** Merge two sorted iterators */
//    private class MergedIterator<T> implements Iterator<T>
//    {
//        private final Iterator<T> iterator1;
//        private final Iterator<T> iterator2;
//        private final Comparator<T> comparator;
//        private T next1;
//        private T next2;
//        
//        public MergedIterator(Iterator<T> iterator1, Iterator<T> iterator2, Comparator<T> comparator)
//        {
//            this.iterator1 = iterator1;
//            this.iterator2 = iterator2;
//            this.comparator = comparator;
//        }
//        
//        @Override
//        public boolean hasNext()
//        {
//            return  iterator1.hasNext() || iterator2.hasNext() || (next1 != null) || (next2 != null);
//        }
//
//        @Override
//        public T next()
//        {
//            if (iterator1.hasNext() && (next1 == null)) next1 = iterator1.next();
//            if (iterator2.hasNext() && (next2 == null)) next2 = iterator2.next();
//            T theNext;
//            int result = (next1 == null) ? 1 :
//                         (next2 == null) ? -1 :
//                         comparator.compare(next1, next2);
//            if (result > 0)
//            {
//                theNext = next2;
//                next2 = null;
//            } else if (comparator.compare(next1, next2) < 0)
//            {
//                theNext = next1;
//                next1 = null;
//            } else
//            { // same element, return one, advance both
//                theNext = next1;
//                next1 = null;
//                next2 = null;
//            }
//            return theNext;
//        }
//    }
}
