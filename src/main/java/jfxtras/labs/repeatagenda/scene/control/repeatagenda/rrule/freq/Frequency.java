package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;

/** Interface for frequency rule that produces a stream of LocalDateTime start times for repeatable events 
 * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) */
// Experimental
public interface Frequency {

    /** Number of frequency periods elapsed before next occurrence. Defaults to 1*/
    Integer getInterval();
    void setInterval(Integer interval);
    
    /** Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * The BYxxx rules must be applied in a specific order and can only be occur once */
    List<ByRule> getByRules();

    /** Adds new byRule to collection and ensures that type of rule isn't already present */
    void addByRule(ByRule byRule);

    
    /** Enum of frequency rule
     *  Enables usage of switch in BYxxx rules for each FREQ rule */
    @Deprecated
    FrequencyEnum frequencyEnum();

    
    /** Resulting stream of date/times by applying rules 
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
//    Stream<LocalDateTime> stream(LocalDateTime startDateTime);

    default Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream = Stream.iterate(startDateTime, (a) -> { return a.with(getAdjuster()); });
        for (ByRule rule : getByRules())
        {
            stream = rule.stream(stream, startDateTime);
        }
        return stream;
    }
    
    TemporalAdjuster getAdjuster();

//    /** Resulting stream of date/times by applying rules 
//     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point*/
//    default Stream<LocalDateTime> stream()
//    {
//        return stream(getStartLocalDateTime());
//    };

    /** Enumeration of FREQ rules */
    public static enum FrequencyEnum
    {
        YEARLY
      , MONTHLY
      , WEEKLY
      , DAILY
      , HOURLY // Not implemented
      , MINUTELY // Not implemented
      , SECONDLY // Not implemented
      
    }

    ChronoUnit getChronoUnit();
    void setChronoUnit(ChronoUnit chronoUnit);
    
}


