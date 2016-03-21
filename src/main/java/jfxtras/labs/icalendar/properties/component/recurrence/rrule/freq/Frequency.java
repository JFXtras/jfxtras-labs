package jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq;

import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Iterator;
import java.util.Optional;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByRule;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.byxxx.ByRuleEnum;
import jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;
import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/** Interface for frequency rule that produces a stream of LocalDateTime start times for repeatable events 
 * FREQ rule as defined in RFC 5545 iCalendar 3.3.10 p37 (i.e. Daily, Weekly, Monthly, etc.)
 * @author David Bal
 * @see FrequencyAbstract
 * @see Yearly
 * @see Monthly
 * @see Weekly
 * @see Daily
 * @see Hourly
 * @see Minutely
 * @see Secondly */
public interface Frequency {

    /** Number of frequency periods elapsed before next occurrence. Defaults to 1*/
    Integer getInterval();
    /** Number of frequency periods elapsed before next occurrence. Defaults to 1*/
    void setInterval(Integer interval);
    /** Number of frequency periods elapsed before next occurrence. Defaults to 1*/
    IntegerProperty intervalProperty();
    
    /** Collection of rules that modify frequency rule (see RFC 5545, iCalendar 3.3.10 Page 42)
     * The rules include all BYxxx rules, EXDate and RDate lists.
     * The BYxxx rules must be applied in a specific order and can only be occur once 
     * BYxxx rule parts
      are applied to the current set of evaluated occurrences in the
      following order: BYMONTH, BYWEEKNO, BYYEARDAY, BYMONTHDAY, BYDAY,
      BYHOUR, BYMINUTE, BYSECOND and BYSETPOS; then COUNT and UNTIL are
      evaluated.*/
    ObservableList<ByRule> byRules();

    default public ByRule lookupByRule(ByRuleEnum byRuleType)
    {
        Optional<ByRule> rule = byRules()
                .stream()
                .filter(r -> r.byRuleType() == byRuleType)
                .findFirst();
        return (rule.isPresent()) ? rule.get() : null;
    }

    /** Resulting stream of start date/times by applying Frequency temporal adjuster and all, if any,
     * Rules.
     * Starts on startDateTime, which MUST be a valid occurrence date/time, but not necessarily the
     * first date/time (DTSTART) in the sequence. A later startDateTime can be used to more efficiently
     * get to later dates in the stream.
     * 
     * @param start - starting point of stream (MUST be a valid occurrence date/time)
     * @return
     */
    Stream<Temporal> stream(Temporal start);

    /** Which of the enum type FrenquencyType the implementing class represents */
    FrequencyEnum frequencyType();
        
    /** Temporal adjuster every class implementing Frequency must provide that modifies frequency dates 
     * For example, Weekly class advances the dates by INTERVAL Number of weeks. */
    TemporalAdjuster adjuster();

    /**
     * Find previous occurrence date to start the stream
     * 
     * @param dateTimeStart - DTSTART
     * @param start
     * @return
     */
    // TODO - FIX THIS - NOT WORKING FOR RANDOM DATES
    @Deprecated // may not be needed anymore
    default Temporal makeFrequencyOccurrence(Temporal dateTimeStart, Temporal start)
    {
        if (DateTimeUtilities.isBefore(start, dateTimeStart)) return dateTimeStart;
        Iterator<Temporal> i = Stream.iterate(start, a -> a.with(adjuster())).iterator();
        Temporal last = null;
        while (i.hasNext())
        {
            Temporal current = i.next();
            if (DateTimeUtilities.isAfter(current, start)) return last;
            last = current;
        }
        return null; // should never get here
    }
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid.
     */
    @Deprecated // put tests into enum
    default String makeErrorString()
    {
        StringBuilder builder = new StringBuilder();
        if (getInterval() < 1) builder.append(System.lineSeparator() + "Invalid RRule.  INTERVAL must be greater than or equal to 1.");
        switch (frequencyType())
        {
        case DAILY:
            if (lookupByRule(ByRuleEnum.BY_WEEK_NUMBER) != null)
            if (lookupByRule(ByRuleEnum.BY_WEEK_NUMBER) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (lookupByRule(ByRuleEnum.BY_YEAR_DAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            break;
        case MONTHLY:
            if (lookupByRule(ByRuleEnum.BY_WEEK_NUMBER) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (lookupByRule(ByRuleEnum.BY_YEAR_DAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            break;
        case WEEKLY:
            if (lookupByRule(ByRuleEnum.BY_WEEK_NUMBER) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (lookupByRule(ByRuleEnum.BY_YEAR_DAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            if (lookupByRule(ByRuleEnum.BY_MONTH_DAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYMONTHDAY not available when FREQ is " + frequencyType());
            break;
        case YEARLY:
            if ((lookupByRule(ByRuleEnum.BY_WEEK_NUMBER) != null) && (lookupByRule(ByRuleEnum.BY_DAY) != null))
            {
                ByDay byDay = (ByDay) lookupByRule(ByRuleEnum.BY_DAY);
                if (byDay.hasOrdinals()) builder.append(System.lineSeparator()
                        + "Invalid RRule. The BYDAY rule part MUST NOT be specified with a numeric value with the FREQ rule part set to YEARLY when the BYWEEKNO rule part is specified");
            }
            break;
        case HOURLY:
        case MINUTELY:
        case SECONDLY:
            builder.append(System.lineSeparator() + "Invalid RRule. " + frequencyType() + " not implemented.");
            break;
        default:
            builder.append(System.lineSeparator() + "Invalid RRule. " + frequencyType() + " unknown.");
            break;
        
        }
        return builder.toString();
    }    
}


