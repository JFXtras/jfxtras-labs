package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RRuleElement;

/**
 * Interface for a rule that applies a modification to a Stream of start date/times, such
 * as BYxxx rules, in a recurring event (RRULE).
 * 
 * @author David Bal
 * @see ByRuleAbstract
 * @see ByMonth
 * @see ByWeekNumber
 * @see ByYearDay
 * @see ByMonthDay
 * @see ByDay
 * @see ByHour
 * @see ByMinute
 * @see BySecond
 * @see BySetPosition
 */
public interface ByRule<T> extends Comparable<ByRule<T>>, RRuleElement<T>
{
    
//    ByRuleType byRuleType();
    
    /** 
     * New stream of date/times made after applying rule that either filters out some date/times
     * or adds additional date/times.
     *  
     * @param inStream - Current stream to be added to or subtracted from
     * @param chronoUnit - ChronoUnit of last modification to inStream
     * @param startTemporal - start Temporal (date or date/time)
     * @return
     */
    Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startTemporal);

    Stream<Temporal> streamRecurrences(Stream<Temporal> inStream, ChronoUnit chronoUnit, Temporal dateTimeStart);

    ChronoUnit getChronoUnit();
    
//    boolean isValid(); // TODO - PUT IN VCALENDARELEMENT
    
//    void copyTo(ByRule destination);
//
//    /** Deep copy all fields from source to destination */
//    static void copy(ByRule source, ByRule destination)
//    {
//        source.copyTo(destination);
//    }
}
