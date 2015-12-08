package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * EXDATE, list of DATE-TIME exceptions for recurring events as defined in 
 * RFC 5545 iCalendar 3.8.5.1, page 117.
 * Used as a part of a VComponent as defined by 3.6.1, page 52.
 * 
 * @author David Bal
 *
 */
public class EXDate extends RecurrenceComponentAbstract<EXDate>
{    
    /** Remove date/times in exDates set */
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
         Set<LocalDateTime> dates2 = getDates()
                 .stream()
                 .map(d -> d.getLocalDateTime())
                 .collect(Collectors.toSet());
        return inStream.filter(d -> ! dates2.contains(d));
    }
    
}
