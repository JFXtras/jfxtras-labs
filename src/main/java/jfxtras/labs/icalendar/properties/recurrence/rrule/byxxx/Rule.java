package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import java.lang.reflect.InvocationTargetException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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
     * @param startTemporal - start Temporal (date or date/time)
     * @return
     */
    Stream<Temporal> stream(Stream<Temporal> inStream, ObjectProperty<ChronoUnit> chronoUnit, Temporal startTemporal);

    /** order to process rules */
    ByRuleType getByRuleType();
//    Integer getProcessOrder();

    void copyTo(Rule destination);

    /** Deep copy all fields from source to destination */
    static void copy(Rule source, Rule destination)
    {
        source.copyTo(destination);
    }
    
    /** Enumeration of Byxxx rules parts
     * Contains values including the object's class and the order for processing Byxxx Rules
     * from RFC 5545 iCalendar page 44
     * The class is used to make new instances of the different Rules by matching RRULE property
     * to its matching class
     * */
    static enum ByRuleType
    {
        BYSECOND (BySecond.class, 70) // Not implemented
      , BYMINUTE (ByMinute.class, 60) // Not implemented
      , BYHOUR (ByHour.class, 50) // Not implemented
      , BYDAY (ByDay.class, 40)
      , BYMONTHDAY (ByMonthDay.class, 30)
      , BYYEARDAY (ByYearDay.class, 20) // Not implemented
      , BYWEEKNO (ByWeekNo.class, 10)
      , BYMONTH (ByMonth.class, 0)
      , BYSETPOS (BySetPos.class, 80); // Not implemented
      
        private Class<? extends Rule> clazz;

        private int processOrder;
        public int getProcessOrder() { return processOrder; }

        ByRuleType(Class<? extends Rule> clazz, int processOrder)
        {
            this.clazz = clazz;
            this.processOrder = processOrder;
        }

        /** return new instance of the matching ByRule object */
        public Rule newInstance()
        {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        /** Returns new instance of the matching ByRule and populates its value
         * by parsing the String parameter */
        public Rule newInstance(String string) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
        {
            return clazz.getConstructor(String.class).newInstance(string);
        }
    }
}
