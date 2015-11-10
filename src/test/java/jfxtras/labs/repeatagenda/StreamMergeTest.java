package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.junit.Test;

import com.google.common.collect.Iterators;

public class StreamMergeTest extends StreamTestAbstract {

    @Test
    public void mergeStreamTest1()
    {
        Stream<LocalDateTime> stream1 = STREAM1;
        Stream<LocalDateTime> stream2 = STREAM2;
        Comparator<LocalDateTime> comparator = COMPARATOR;
        Stream<LocalDateTime> s = StreamSupport.stream(Spliterators.spliteratorUnknownSize(
                Iterators.mergeSorted(
                  Arrays.asList(stream1.iterator(), stream2.iterator()),
                  comparator),
                Spliterator.ORDERED),
              false /* not parallel */ );
        s.limit(10).forEach(System.out::println);
    }
}
