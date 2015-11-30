package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;

/**
 * Interface for a rule that applies a modification to a Stream of start date/times, such
 * as BYxxx rules, in a recurring event (RRULE).
 * 
 * @author David Bal
 * @see ByRuleAbstract
 * @see ByMonth
 * @see ByWeekNo
 * @see ByYearDay
 * @see ByMonthDay
 * @see ByDay
 * @see ByHour
 * @see ByMinute
 * @see BySecond
 * @see BySetPos
 */
public interface Rule extends Comparable<Rule>
{
    /** 
     * New stream of date/times made after applying rule that either filters out some date/times
     * or adds additional date/times.
     *  
     * @param inStream - Current stream to be added to or subtracted from
     * @param chronoUnit - ChronoUnit of last modification to inStream
     * @param startDateTime - start date/time
     * @return
     */
    Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream, ObjectProperty<ChronoUnit> chronoUnit, LocalDateTime startDateTime);

    /** order to process rules */
    Integer getProcessOrder();

    void copyTo(Rule destination);

    /** Deep copy all fields from source to destination */
    static void copy(Rule source, Rule destination)
    {
        source.copyTo(destination);
    }
    
    /** Enumeration of Byxxx rules parts
     * Is used to make new instances of the different Rules by matching RRULE property
     * to its matching class */
    static enum ByRules
    {
        BYSECOND (BySecond.class) // Not implemented
      , BYMINUTE (ByMinute.class) // Not implemented
      , BYHOUR (ByHour.class) // Not implemented
      , BYDAY (ByDay.class)
      , BYMONTHDAY (ByMonthDay.class)
      , BYYEARDAY (ByYearDay.class) // Not implemented
      , BYWEEKNO (ByWeekNo.class)
      , BYMONTH (ByMonth.class)
      , BYSETPOS (BySetPos.class); // Not implemented
      
        private Class<? extends Rule> clazz;
          
        ByRules(Class<? extends Rule> clazz)
        {
            this.clazz = clazz;
        }
          
//        public Rule newInstance()
//        {
//            try {
//                return clazz.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

        public Rule newInstance(String string) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
        {
            return clazz.getConstructor(String.class).newInstance(string);
        }
    }
}
