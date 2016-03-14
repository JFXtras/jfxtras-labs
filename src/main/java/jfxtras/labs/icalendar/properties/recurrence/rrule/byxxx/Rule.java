package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javafx.beans.property.ObjectProperty;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency;

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
    ByRuleParameter getByRuleType();
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
    @Deprecated // will probably use RRuleParameters instead
    static enum ByRuleParameter
    {
        BY_SECOND ("BYSECOND", 70) // Not implemented
      , BY_MINUTE ("BYMINUTE", 60) // Not implemented
      , BY_HOUR ("BYHOUR", 50) // Not implemented
      , BY_DAY ("BYDAY", 40)
      , BY_MONTH_DAY ("BYMONTHDAY", 30)
      , BY_YEAR_DAY ("BYYEARDAY", 20) // Not implemented
      , BY_WEEK_NUMBER ("BYWEEKNO", 10)
      , BY_MONTH ("BYMONTH", 0)
      , BY_SET_POSITION ("BYSETPOS", 80); // Not implemented
      
        // Map to match up name to enum
        private static Map<String, ByRuleParameter> propertyFromTagMap = makePropertiesFromNameMap();
        private static Map<String, ByRuleParameter> makePropertiesFromNameMap()
        {
            Map<String, ByRuleParameter> map = new HashMap<>();
            ByRuleParameter[] values = ByRuleParameter.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static ByRuleParameter propertyFromName(String propertyName)
        {
            return propertyFromTagMap.get(propertyName.toUpperCase());
        }
        
        /** Returns the iCalendar property name (e.g. LANGUAGE) */
        @Override public String toString() { return name; }
        
        private String name;
        private int sortOrder;
        public int getSortOrder() { return sortOrder; }

        ByRuleParameter(String name, int sortOrder)
        {
            this.sortOrder = sortOrder;
            this.name = name;
        }

        public String toParameterString(Frequency frequency)
        {
            return frequency.byRules().get(this).toString();
        }
        
        /*
         * ABSTRACT METHODS
         */
    }
}
