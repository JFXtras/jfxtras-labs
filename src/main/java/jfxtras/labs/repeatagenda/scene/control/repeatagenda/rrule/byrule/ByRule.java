package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.Frequency;

/**
 * BYxxx rule that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
 * The BYxxx rules must be applied in a specific order
 * @author David Bal
 *
 */
public interface ByRule {

    /** new stream of date/times after applying BYxxx rule */
    Stream<LocalDateTime> stream();
    
    /** parent frequency rule, set in constructor */
    Frequency getFrequency();
//    void setFrequency(FrequencyEnum f);
//    FrequencyRuleEnum getFrequency();
    
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
