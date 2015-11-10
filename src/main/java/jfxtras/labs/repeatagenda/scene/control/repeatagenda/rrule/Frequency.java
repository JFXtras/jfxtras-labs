package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule.ByRule;

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
    LinkedHashSet<ByRule> getByRules();
    void setByRules(LinkedHashSet<ByRule> c);
    
    /** startLocalDateTime from parent Repeat object */
    LocalDateTime getStartLocalDateTime();
//    void setStartLocalDateTime(LocalDateTime startLocalDateTime);
    
    /** Enum of frequency rule */
    FrequencyEnum frequencyEnum();
    
//    /** Initial stream of dates */
//    void setInStream(Stream<LocalDateTime> inStream);
    
    /** Resulting stream of date/times by applying rules */
    Stream<LocalDateTime> stream();



    /** Enum of implemented BYxxx rules */
    public static enum FrequencyEnum
    {
        DAILY
      , WEEKLY
      , MONTHLY
      , YEARLY
    }
    
}


