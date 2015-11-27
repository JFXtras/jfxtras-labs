package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;

/** Interface for frequency rule that produces a stream of LocalDateTime start times for repeatable events 
 * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.) */
// Experimental
public interface Frequency {

    /** Number of frequency periods elapsed before next occurrence. Defaults to 1*/
    Integer getInterval();
    void setInterval(Integer interval);
    
    /** Collection of rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * The rules include all BYxxx rules, EXDate and RDate lists.
     * The BYxxx rules must be applied in a specific order and can only be occur once 
     * BYxxx rule parts
      are applied to the current set of evaluated occurrences in the
      following order: BYMONTH, BYWEEKNO, BYYEARDAY, BYMONTHDAY, BYDAY,
      BYHOUR, BYMINUTE, BYSECOND and BYSETPOS; then COUNT and UNTIL are
      evaluated.*/
    List<Rule> getRules();

    /** Adds new byRule to collection and ensures that type of rule isn't already present */
    void addByRule(Rule rule);

    
    /** Chrono unit of last modification to stream
     *  Enables usage of switch statement in BYxxx rules */
    ChronoUnit getChronoUnit();
    void setChronoUnit(ChronoUnit chronoUnit);

    
    /** Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     * Rules.
     * Starts on startDateTime, which must be a valid event date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream. */
    default Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream = Stream.iterate(startDateTime, (a) -> { return a.with(getAdjuster()); });
        Iterator<Rule> rulesIterator = getRules().stream().sorted().iterator();
        while (rulesIterator.hasNext())
        {
            Rule rule = rulesIterator.next();
            stream = rule.stream(stream, startDateTime);            
        }
        return stream;
    }
    
    /** Temporal adjuster every class implementing Frequency must provide that modifies frequency dates 
     * For example, Weekly class advances the dates by INTERVAL Number of weeks. */
    TemporalAdjuster getAdjuster();

    /** Enumeration of FREQ rules 
     * Is used to make new instances of the different Frequencies by matching FREQ property
     * to its matching class */
    public static enum Frequencies
    {
        YEARLY (Yearly.class)
      , MONTHLY (Monthly.class)
      , WEEKLY (Weekly.class)
      , DAILY (Daily.class)
      , HOURLY (Hourly.class) // Not implemented
      , MINUTELY (Minutely.class) // Not implemented
      , SECONDLY (Secondly.class);// Not implemented
      
        private Class<? extends Frequency> clazz;
          
        Frequencies(Class<? extends Frequency> clazz)
        {
            this.clazz = clazz;
        }
          
        public Frequency newInstance()
        {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /** Deep copy all fields from source to destination */
    static void copy(Frequency source, Frequency destination)
    {
        destination.setChronoUnit(source.getChronoUnit());
        if (source.getInterval() != null) destination.setInterval(source.getInterval());
        if (source.getRules() != null)
        {
//            System.out.println("rules size-" + source.getRules().size());
            source.getRules().stream().forEach(r ->
            {
//                System.out.println(r.getClass());
                try {
                    Rule newRule = r.getClass().newInstance();
                    Rule.copy(r, newRule);
                    destination.addByRule(newRule);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
    }


    
}


