package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
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

    /** Number of frequency periods elapsed before next occurrence, can't be set if until is used */
    Integer getCount();
    void setCount(Integer count);
    
    /** End date/time for repeat rule, can't be set if count is used. */
    LocalDateTime getUntil();
    void setUntil(LocalDateTime dateTime);
    
    /** Collection of BYxxx rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * The BYxxx rules must be applied in a specific order and can only be occur once */
    List<ByRule> getByRules();
    /** Adds new byRule to collection and ensures that type of rule isn't already present */
    void addByRule(ByRule byRule);
    // TODO - DO I WANT AN addByRule METHOD - THAT VERIFYS THE BYRULE ISN'T ALREADY THERE?
//    void setByRules(LinkedHashSet<ByRule> c);
    
    /** startLocalDateTime from parent Repeat object */
    LocalDateTime getStartLocalDateTime();
//    void setStartLocalDateTime(LocalDateTime startLocalDateTime);
    
    /** Enum of frequency rule
     *  Enables usage of switch in BYxxx rules for each FREQ rule */
    FrequencyEnum frequencyEnum();
    
//    /** Initial stream of dates */
//    void setInStream(Stream<LocalDateTime> inStream);
    
    /** Resulting stream of date/times by applying rules 
     * Starts on startDateTime, which must be a valid event date/time, not necessarily the
     * first date/time (DTSTART) in the sequence. */
    Stream<LocalDateTime> stream(LocalDateTime startDateTime);

    /** Resulting stream of date/times by applying rules 
     * Uses startLocalDateTime - first date/time in sequence (DTSTART) as a default starting point*/
    default Stream<LocalDateTime> stream()
    {
        return stream(getStartLocalDateTime());
    };

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
    
}


