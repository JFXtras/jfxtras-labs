package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * RDATE: Set of date/times for recurring events, to-dos, journal entries.
 * 3.8.5.2, RFC 5545 iCalendar
 * 
 * @author David Bal
 *
 */
// Merge stream is untested
public class RDate extends RecurrenceComponent
{
    
    /** Remove date/times in exDates set */
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        return getDates().stream(); // only works if called first in chain of stream modifications
//        return merge(inStream, getDates().stream());
    }

        
    private <T> Stream<T> merge(Stream<T> stream1, Stream<T> stream2) {
            Iterator<T> iterator = new MergedIterator<T>(stream1.iterator(), stream2.iterator());
            Spliterator<T> spliterator = new SpliteratorAdapter<>(iterator);
            return StreamSupport.stream(spliterator, false);
    }
    
    /**
     * Recommend using with StreamSupport.stream(iteratorStream, false);
     */
        private class SpliteratorAdapter<T> extends Spliterators.AbstractSpliterator <T>{
     
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
    
    // TODO - this only concatenates iterators - need to merge sorted iterators instead
    private class MergedIterator<T> implements Iterator<T>
    {
        private final Iterator<T> is[];
        private int current;

        public MergedIterator(Iterator<T>... iterators)
        {
                is = iterators;
                current = 0;
        }

        public boolean hasNext()
        {
                while ( current < is.length && !is[current].hasNext() )
                        current++;

                return current < is.length;
        }

        public T next() {
                while ( current < is.length && !is[current].hasNext() )
                        current++;

                return is[current].next();
        }

        public void remove() { /* not implemented */ }

    }
}

