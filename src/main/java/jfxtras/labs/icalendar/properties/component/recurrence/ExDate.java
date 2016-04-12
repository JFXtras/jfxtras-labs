package jfxtras.labs.icalendar.properties.component.recurrence;

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
@Deprecated
public class ExDate extends RecurrenceAbstract<ExDate>
{    
    // CONSTRUCTORS
    public ExDate() { }
    public ExDate(Temporal... dateOrDateTime) { super(dateOrDateTime); }
    
    /** Remove date/times in exDates set */
    public Stream<Temporal> stream(Stream<Temporal> inStream, Temporal startTemporal)
    {
        return inStream.filter(d -> ! getTemporals().contains(d));
    }
    
}
