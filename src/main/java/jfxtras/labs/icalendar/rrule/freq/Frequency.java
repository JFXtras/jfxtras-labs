package jfxtras.labs.icalendar.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.icalendar.VComponent;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.icalendar.rrule.byxxx.Rule.ByRules;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.Settings;

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
    List<Rule> getByRules();
    /** Adds new byRule to collection and ensures that type of rule isn't already present */
    void addByRule(Rule rule);
    /** return ByRule object from byRules list by enum type.  Returns null if not present */
    Rule getByRuleByType(Rule.ByRules byRule);
    
    /** ChronoUnit of last modification to stream
     *  Enables usage of switch statement in BYxxx rules */
    ObjectProperty<ChronoUnit> getChronoUnit();
    void setChronoUnit(ObjectProperty<ChronoUnit> chronoUnit);

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
    FrequencyType frequencyType();
        
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
    default Temporal makeFrequencyOccurrence(Temporal dateTimeStart, Temporal start)
    {
        if (VComponent.isBefore(start, dateTimeStart)) return dateTimeStart;
        Iterator<Temporal> i = Stream.iterate(start, a -> a.with(adjuster())).iterator();
        Temporal last = null;
        while (i.hasNext())
        {
            Temporal current = i.next();
            if (VComponent.isAfter(current, start)) return last;
            last = current;
        }
        return null; // should never get here
    }
    /**
     * Checks to see if object contains required properties.  Returns empty string if it is
     * valid.  Returns string of errors if not valid.
     */
    default String makeErrorString()
    {
        StringBuilder builder = new StringBuilder();
        if (getInterval() < 1) builder.append(System.lineSeparator() + "Invalid RRule.  INTERVAL must be greater than or equal to 1.");
        switch (frequencyType())
        {
        case DAILY:
            if (getByRuleByType(ByRules.BYWEEKNO) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (getByRuleByType(ByRules.BYYEARDAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            break;
        case MONTHLY:
            if (getByRuleByType(ByRules.BYWEEKNO) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (getByRuleByType(ByRules.BYYEARDAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            break;
        case WEEKLY:
            if (getByRuleByType(ByRules.BYWEEKNO) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYWEEKNO not available when FREQ is " + frequencyType());
            if (getByRuleByType(ByRules.BYYEARDAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYYEARDAY not available when FREQ is " + frequencyType());
            if (getByRuleByType(ByRules.BYMONTHDAY) != null) builder.append(System.lineSeparator() + "Invalid RRule. BYMONTHDAY not available when FREQ is " + frequencyType());
            break;
        case YEARLY:
            if ((getByRuleByType(ByRules.BYWEEKNO) != null) && (getByRuleByType(ByRules.BYDAY) != null))
            {
                ByDay byDay = (ByDay) getByRuleByType(ByRules.BYDAY);
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
    
    /** Enumeration of FREQ rules 
     * Is used to make new instances of the different Frequencies by matching FREQ property
     * to its matching class */
    public static enum FrequencyType
    {
        YEARLY (Yearly.class)
      , MONTHLY (Monthly.class)
      , WEEKLY (Weekly.class)
      , DAILY (Daily.class)
      , HOURLY (Hourly.class) // Not implemented
      , MINUTELY (Minutely.class) // Not implemented
      , SECONDLY (Secondly.class);// Not implemented
      
        private Class<? extends Frequency> clazz;
          
        FrequencyType(Class<? extends Frequency> clazz)
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

        /** return array of implemented FrequencyTypes */
        public static FrequencyType[] implementedValues()
        {
            return new FrequencyType[] { DAILY, WEEKLY, MONTHLY, YEARLY };
        }
                
        public String toStringSingular()
        {
            switch (this)
            {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(DAILY); // day
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(WEEKLY); // week
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(MONTHLY); // month
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(YEARLY); // year
            default:
                return null;                
            }
        }
        
        public String toStringPlural()
        {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(DAILY); // days
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(WEEKLY); // weeks
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(MONTHLY); // months
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(YEARLY); // years
            default:
                return null;                
            }
        }
        
        public static StringConverter<FrequencyType> stringConverter = new StringConverter<FrequencyType>()
        {
            @Override public String toString(FrequencyType object) {
                switch (object) {
                case DAILY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.DAILY); // Daily
                case WEEKLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.WEEKLY); // Weekly
                case MONTHLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.MONTHLY); // Monthly
                case YEARLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.YEARLY); // Yearly
                default:
                    return null;                
                }
            }
            @Override public FrequencyType fromString(String string) {
                throw new RuntimeException("not required for non editable ComboBox");
            }
        };
    }

    /** Deep copy all fields from source to destination */
    static void copy(Frequency source, Frequency destination)
    {
        destination.setChronoUnit(source.getChronoUnit());
        if (source.getInterval() != null) destination.setInterval(source.getInterval());
        if (source.getByRules() != null)
        {
            source.getByRules().stream().forEach(r ->
            {
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


