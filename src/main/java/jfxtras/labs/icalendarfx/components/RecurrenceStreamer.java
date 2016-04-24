package jfxtras.labs.icalendarfx.components;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRuleParameter;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public class RecurrenceStreamer
{
    // Variables for start date or date/time cache used as starting Temporal for stream
    private static final int CACHE_RANGE = 51; // number of values in cache
    private static final int CACHE_SKIP = 21; // store every nth value in the cache
    private int skipCounter = 0; // counter that increments up to CACHE_SKIP, indicates time to record a value, then resets to 0
    private Temporal[] temporalCache; // the start date or date/time cache
    private Temporal dateTimeStartLast; // last dateTimeStart, when changes indicates clearing the cache is necessary
    private RecurrenceRuleParameter rRuleLast; // last rRule, when changes indicates clearing the cache is necessary
    private int cacheStart = 0; // start index where cache values are stored (starts in middle)
    private int cacheEnd = 0; // end index where cache values are stored
    private VComponentRepeatable<?> component; // the VComponent

    RecurrenceStreamer(VComponentRepeatable<?> component)
    {
        this.component = component;
    }
    /**
     * finds previous stream Temporal before input parameter value
     * 
     * @param value
     * @return
     */
//    public Temporal previousStreamValue(Temporal value)
//    {
//        final Temporal start; 
//        if (cacheEnd == 0)
//        {
//            start = component.getDateTimeStart().getValue();
//        } else
//        { // try to get start from cache
//            Temporal m  = null;
//            for (int i=cacheEnd; i>cacheStart; i--)
//            {
//                if (DateTimeUtilities.isBefore(temporalCache[i], value))
//                {
//                    m = temporalCache[i];
//                    break;
//                }
//            }
//            start = (m != null) ? m : component.getDateTimeStart().getValue();
//        }
//        Iterator<Temporal> i = streamNoCache(start).iterator();
//        Temporal lastT = null;
//        while (i.hasNext())
//        {
//            Temporal t = i.next();
//            if (! DateTimeUtilities.isBefore(t, value)) break;
//            lastT = t;
//        }
//        return lastT;
//    }
    
    public Stream<Temporal> stream(Temporal start)
    {
        // adjust start to ensure its not before dateTimeStart
        final Temporal start2 = (DateTimeUtilities.isBefore(start, component.getDateTimeStart().getValue())) ? component.getDateTimeStart().getValue() : start;
        final Stream<Temporal> stream1; // individual or rrule stream
        final Temporal earliestCacheValue;
        final Temporal latestCacheValue;
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
        
        if (component.getRecurrenceRule() == null)
        { // if individual event
            stream1 = Arrays.asList((Temporal) component.getDateTimeStart().getValue())
                    .stream()
                    .filter(d -> ! DateTimeUtilities.isBefore(d, start2));
            earliestCacheValue = null;
            latestCacheValue = null;
        } else
        {
            // check if cache needs to be cleared (changes to RRULE or DTSTART)
            if ((dateTimeStartLast != null) && (rRuleLast != null))
            {
                boolean startChanged = ! component.getDateTimeStart().getValue().equals(dateTimeStartLast);
                boolean rRuleChanged = ! component.getRecurrenceRule().getValue().equals(rRuleLast);
                if (startChanged || rRuleChanged)
                {
                    temporalCache = null;
                    cacheStart = 0;
                    cacheEnd = 0;
                    skipCounter = 0;
                    dateTimeStartLast = component.getDateTimeStart().getValue();
                    rRuleLast = component.getRecurrenceRule().getValue();
                }
            } else
            { // save current DTSTART and RRULE for next time
                dateTimeStartLast = component.getDateTimeStart().getValue();
                rRuleLast = component.getRecurrenceRule().getValue();
            }
            
            final Temporal match;
            
            // use cache if available to find matching start date or date/time
            if (temporalCache != null)
            {
                // Reorder cache to maintain centered and sorted
                final int len = temporalCache.length;
                final Temporal[] p1;
                final Temporal[] p2;
                if (cacheEnd < cacheStart) // high values have wrapped from end to beginning
                {
                    p1 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                    p2 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
                } else if (cacheEnd > cacheStart) // low values have wrapped from beginning to end
                {
                    p2 = Arrays.copyOfRange(temporalCache, cacheStart, len);
                    p1 = Arrays.copyOfRange(temporalCache, 0, Math.min(cacheEnd+1,len));
                } else
                {
                    p1 = null;
                    p2 = null;
                }
                if (p1 != null)
                { // copy elements to accommodate wrap and restore sort order
                    int p1Index = 0;
                    int p2Index = 0;
                    for (int i=0; i<len; i++)
                    {
                        if (p1Index < p1.length)
                        {
                            temporalCache[i] = p1[p1Index];
                            p1Index++;
                        } else if (p2Index < p2.length)
                        {
                            temporalCache[i] = p2[p2Index];
                            p2Index++;
                        } else
                        {
                            cacheEnd = i;
                            break;
                        }
                    }
                }
    
                // Find match in cache
                latestCacheValue = temporalCache[cacheEnd];
                if ((! DateTimeUtilities.isBefore(start2, temporalCache[cacheStart])))
                {
                    Temporal m = latestCacheValue;
                    for (int i=cacheStart; i<cacheEnd+1; i++)
                    {
                        if (DateTimeUtilities.isAfter(temporalCache[i], start2))
                        {
                            m = temporalCache[i-1];
                            break;
                        }
                    }
                    match = m;
                } else
                { // all cached values too late - start over
                    cacheStart = 0;
                    cacheEnd = 0;
                    temporalCache[cacheStart] = component.getDateTimeStart().getValue();
                    match = component.getDateTimeStart().getValue();
                }
                earliestCacheValue = temporalCache[cacheStart];
            } else
            { // no previous cache.  initialize new array with dateTimeStart as first value.
                temporalCache = new Temporal[CACHE_RANGE];
                temporalCache[cacheStart] = component.getDateTimeStart().getValue();
                match = component.getDateTimeStart().getValue();
                earliestCacheValue = component.getDateTimeStart().getValue();
                latestCacheValue = component.getDateTimeStart().getValue();
            }
            stream1 = component.getRecurrenceRule().getValue().streamRecurrence(match);
        }
        
        // If present, add recurrence list
        final Stream<Temporal> stream2;
        if (component.getRecurrences() == null)
        {
            stream2 = stream1;
        } else
        {
            stream2 = RecurrenceStreamer.merge(stream1
                       , component.getRecurrences().stream().flatMap(r -> r.getValue().stream()) // should these process be put outside so only done once?
                       , temporalComparator);
        }
        
        final Stream<Temporal> stream3;
        if ((component instanceof VComponentDisplayable) && (((VComponentDisplayable<?>) component).getExceptions() != null))
        {
            /** Remove date/times in exDates set */
            List<? extends Temporal> ex = ((VComponentDisplayable<?>) component).getExceptions().stream().flatMap(r -> r.getValue().stream()).collect(Collectors.toList());
            stream3 = stream2.filter(d -> ! ex.contains(d));
        } else
        {
            stream3 = stream2;
        }
        
        Stream<Temporal> stream4 = stream3
                .peek(t ->
                { // save new values in cache
                    if (component.getRecurrenceRule() != null)
                    {
                        if (DateTimeUtilities.isBefore(t, earliestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheStart--;
                                if (cacheStart < 0) cacheStart = CACHE_RANGE - 1;
                                if (cacheStart == cacheEnd) cacheEnd--; // just overwrote oldest value - push cacheEnd down
                                temporalCache[cacheStart] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        if (DateTimeUtilities.isAfter(t, latestCacheValue))
                        {
                            if (skipCounter == CACHE_SKIP)
                            {
                                cacheEnd++;
                                if (cacheEnd == CACHE_RANGE) cacheEnd = 0;
                                if (cacheStart == cacheEnd) cacheStart++; // just overwrote oldest value - push cacheStart up
                                temporalCache[cacheEnd] = t;
                                skipCounter = 0;
                            } else skipCounter++;
                        }
                        // check if start or end needs to wrap
                        if (cacheEnd < 0) cacheEnd = CACHE_RANGE - 1;
                        if (cacheStart == CACHE_RANGE) cacheStart = 0;
                    }
                })
                .filter(t -> ! DateTimeUtilities.isBefore(t, start2)); // remove too early events;

        return stream4;
    }
    
//    default Stream<Temporal> getTemporalStream()
//    {
//        return getRecurrences().stream().flatMap(r -> r.getValue().stream());
//    }
//    
//    /** Add date/times in RDates set */
//    default Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startTemporal)
//    {
//        if (inStream == null)
//        {
//            getTemporalStream().filter(d -> ! DateTimeUtilities.isBefore(d, startTemporal));
//        }
//        return merge(inStream
//                   , getTemporalStream()
//                   , DateTimeUtilities.TEMPORAL_COMPARATOR);
//    }
    
    static <T> Stream<T> merge(Stream<T> stream1, Stream<T> stream2, Comparator<T> comparator)
    {
            Iterator<T> iterator = new MergedIterator<T>(
                    stream1.iterator()
                  , stream2.iterator()
                  , comparator);
            Spliterator<T> spliterator = new SpliteratorAdapter<>(iterator);
            return StreamSupport.stream(spliterator, false);
    }
    
    /*
     * Recommend using with StreamSupport.stream(iteratorStream, false);
     */
    static class SpliteratorAdapter<T> extends Spliterators.AbstractSpliterator <T>
    {
        private final Iterator<T> iterator;
     
        public SpliteratorAdapter(Iterator<T> iter) {
            super(Long.MAX_VALUE, 0);
            iterator = iter;
        }
     
        @Override
        public synchronized boolean tryAdvance(Consumer<? super T> action) {
            if(iterator.hasNext()) {
                action.accept(iterator.next());
                return true;
            }
            return false;
        }
    }        

    /** Merge two sorted iterators */
    static class MergedIterator<T> implements Iterator<T>
    {
        private final Iterator<T> iterator1;
        private final Iterator<T> iterator2;
        private final Comparator<T> comparator;
        private T next1;
        private T next2;
        
        public MergedIterator(Iterator<T> iterator1, Iterator<T> iterator2, Comparator<T> comparator)
        {
            this.iterator1 = iterator1;
            this.iterator2 = iterator2;
            this.comparator = comparator;
        }
        
        @Override
        public boolean hasNext()
        {
            return  iterator1.hasNext() || iterator2.hasNext() || (next1 != null) || (next2 != null);
        }

        @Override
        public T next()
        {
            if (iterator1.hasNext() && (next1 == null)) next1 = iterator1.next();
            if (iterator2.hasNext() && (next2 == null)) next2 = iterator2.next();
            T theNext;
            int result = (next1 == null) ? 1 :
                         (next2 == null) ? -1 :
                         comparator.compare(next1, next2);
            if (result > 0)
            {
                theNext = next2;
                next2 = null;
            } else if (comparator.compare(next1, next2) < 0)
            {
                theNext = next1;
                next1 = null;
            } else
            { // same element, return one, advance both
                theNext = next1;
                next1 = null;
                next2 = null;
            }
            return theNext;
        }
    }
}
