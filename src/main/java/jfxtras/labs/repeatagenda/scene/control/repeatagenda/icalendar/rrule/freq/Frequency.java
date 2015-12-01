package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.List;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import javafx.util.StringConverter;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Settings;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;

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
    void setInterval(Integer interval);
    /** Set interval from strong containing INTERVAL=n where n is a number */
    void setInterval(String s);
    
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

    
    /** ChronoUnit of last modification to stream
     *  Enables usage of switch statement in BYxxx rules */
    ObjectProperty<ChronoUnit> getChronoUnit();
    void setChronoUnit(ObjectProperty<ChronoUnit> chronoUnit);
    
    Stream<LocalDateTime> stream(LocalDateTime startDateTime);
//    /**
//     * Return inital ChronoUnit for Frequency.  This value must be 
//     */
//    ChronoUnit getInitialChronoUnit();

    /** Which of the enum type FrenquencyType the implementing class represents */
    FrequencyType getFrequencyType();
        
    /** Temporal adjuster every class implementing Frequency must provide that modifies frequency dates 
     * For example, Weekly class advances the dates by INTERVAL Number of weeks. */
    TemporalAdjuster getAdjuster();

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
        
        public String toStringSingular() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_SINGULAR.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public String toStringPlural() {
            switch (this) {
            case DAILY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(DAILY);
            case WEEKLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(WEEKLY);
            case MONTHLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(MONTHLY);
            case YEARLY:
                return Settings.REPEAT_FREQUENCIES_PLURAL.get(YEARLY);
            default:
                return null;                
            }
        }
        
        public static StringConverter<FrequencyType> stringConverter
            = new StringConverter<FrequencyType>() {
            @Override public String toString(FrequencyType object) {
                switch (object) {
                case DAILY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.DAILY);
                case WEEKLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.WEEKLY);
                case MONTHLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.MONTHLY);
                case YEARLY:
                    return Settings.REPEAT_FREQUENCIES.get(FrequencyType.YEARLY);
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


