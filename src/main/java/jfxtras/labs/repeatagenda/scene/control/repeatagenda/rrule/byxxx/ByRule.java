package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * @author David Bal
 *
 */
public interface ByRule {

    /** New stream of date/times made after applying BYxxx rule
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime);

    /** New stream of date/times made after applying BYxxx rule
     * Stream is infinite if COUNT or UNTIL not present or ends when COUNT or UNTIL condition
     * is met.
     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point */
    default Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream)
    {
        return stream(inStream, getFrequency().getStartLocalDateTime());
    };
    
    /** parent FREQuency rule */
    Frequency getFrequency();
    
    //I may not need this method -use flatmap instead
    public static Stream<LocalDateTime> mergeStream(Stream<LocalDateTime> stream1, Stream<LocalDateTime> stream2)
    {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
            Iterators.mergeSorted(
              Arrays.asList(stream1.iterator(), stream2.iterator()),
              (a1, a2) -> a1.compareTo(a2)),
            Spliterator.ORDERED),
          false /* not parallel */ );
    }
}
