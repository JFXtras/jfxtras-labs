package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.temporal.Temporal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * RDATE: Set of date/times for recurring events, to-dos, journal entries.
 * 3.8.5.2, RFC 5545 iCalendar
 * Limitation: only DATE-TIME supported.  DATE and PERIOD are not supported.
 * 
 * @author David Bal
 * @param <U>
 *
 */
// Merge stream is untested
public class RDate extends RecurrenceComponentAbstract<RDate>
{

//    /** Add date/times in RDates set */
//    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream)
//    {
//        if (inStream == null)
//        {
//            return myStream();
//        }
//        return merge(inStream
//                , getLocalDateTimes().stream()
//                , (a1,a2) -> a1.compareTo(a2));        
//    }

    /** Add date/times in RDates set */
    public Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startTemporal)
    {
        if (inStream == null)
        {
            getTemporalStream().filter(d -> ! VComponent.isBefore(d, startTemporal));
        }
//        final Comparator<Temporal> comparator = (a1, a2) ->
//        {
//            if (startTemporal instanceof LocalDate)
//            {
//                return ((LocalDate) a1).compareTo((LocalDate) a2);
//            } else if (startTemporal instanceof LocalDateTime)
//            {
//                return ((LocalDateTime) a1).compareTo((LocalDateTime) a2);
//            } else throw new IllegalArgumentException("Unsupported Temporal class:" + startTemporal.getClass().getSimpleName());
//        };
        return merge(inStream
                   , getTemporalStream()
                   , VComponent.DATE_OR_DATETIME_TEMPORAL_COMPARATOR);
//                   , (a1,a2) -> a1.compareTo(a2));
    }
    
//    // stream of RDate start dates or date/times
//    private Stream<LocalDateTime> myStream()
//    {
//        return getTemporalStream()
//                .map(t ->
//                {
//                    if (getTemporalClass().equals(LocalDate.class))
//                    {
//                        return ((LocalDate) t).atStartOfDay();
//                    } else if (getTemporalClass().equals(LocalDateTime.class))
//                    {
//                        return (LocalDateTime) t;                            
//                    } else throw new IllegalArgumentException("Unsupported Temporal class:" + getTemporalClass().getSimpleName());
//                });
//    }
    
//    // stream of RDate start dates or date/times
//    private Stream<Temporal> myStream(Temporal startDateTime)
//    {
//        return getTemporalStream()
//                .filter(t -> 
//                {
//                    if (temporalClass().equals(LocalDate.class))
//                    {
//                        LocalDate start = startDateTime.toLocalDate();
//                        return ! ((LocalDate) t).isBefore(start);
//                    } else if (temporalClass().equals(LocalDateTime.class))
//                    {
//                        LocalDateTime start = (LocalDateTime) startDateTime;
//                        return ! ((LocalDateTime) t).isBefore(start);
//                    } else throw new IllegalArgumentException("Unsupported Temporal class:" + temporalClass().getSimpleName());
//                })
//                .map(t ->
//                {
//                    if (temporalClass().equals(LocalDate.class))
//                    {
//                        return ((LocalDate) t).atStartOfDay();
//                    } else if (temporalClass().equals(LocalDateTime.class))
//                    {
//                        return (LocalDateTime) t;                            
//                    } else throw new IllegalArgumentException("Unsupported Temporal class:" + temporalClass().getSimpleName());
//                });
//    }

    public Stream<Temporal> stream(Temporal startTemporal)
    {
//        return getLocalDateTimes()
//                .stream()
//                .filter(d -> ! d.isBefore(startDateTime));
        return getTemporalStream()
                .filter(d -> ! VComponent.isBefore(d, startTemporal));
    }

    
    private <T> Stream<T> merge(Stream<T> stream1, Stream<T> stream2, Comparator<T> comparator)
    {
            Iterator<T> iterator = new MergedIterator<T>(
                    stream1.iterator()
                  , stream2.iterator()
                  , comparator);
            Spliterator<T> spliterator = new SpliteratorAdapter<>(iterator);
            return StreamSupport.stream(spliterator, false);
    }
    
    /**
     * Recommend using with StreamSupport.stream(iteratorStream, false);
     */
    private class SpliteratorAdapter<T> extends Spliterators.AbstractSpliterator <T>
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
    private class MergedIterator<T> implements Iterator<T>
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

