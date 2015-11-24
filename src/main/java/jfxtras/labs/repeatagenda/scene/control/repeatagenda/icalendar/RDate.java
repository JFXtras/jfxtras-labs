package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
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
 *
 */
// Merge stream is untested
public class RDate extends RecurrenceComponent<RDate>
{
            
    /** Remove date/times in exDates set */
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        return merge(inStream
                , getDates().stream().filter(d -> ! d.isBefore(startDateTime))
                , (a1, a2) -> a1.compareTo(a2));
    }

    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        return getDates().stream().filter(d -> ! d.isBefore(startDateTime));
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
//            System.out.println("result: " + result);
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

