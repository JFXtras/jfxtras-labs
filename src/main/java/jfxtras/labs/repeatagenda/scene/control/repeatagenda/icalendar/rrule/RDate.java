package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;

/**
 * 
 * @author David Bal
 *
 */
public class RDate implements Rule
{

    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream,
            LocalDateTime startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

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
