package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
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
    // CONSTRUCTORS
    public EXDate() { }
    public EXDate(Temporal... dateOrDateTime) { super(dateOrDateTime); }
    
    /** Remove date/times in exDates set */
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, LocalDateTime startDateTime)
    {
        return inStream.filter(d ->
        {
//            System.out.println("EXDate:" + d);
            return ! getLocalDateTimes().contains(d);
        });
    }
    
}
