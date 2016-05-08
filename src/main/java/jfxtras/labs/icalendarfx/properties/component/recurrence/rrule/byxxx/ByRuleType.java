package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Frequency;

/** Enumeration of Byxxx rules parts
 * Contains values including the object's class and the order for processing Byxxx Rules
 * from RFC 5545 iCalendar page 44
 * The class is used to make new instances of the different Rules by matching RRULE property
 * to its matching class
 * */
@Deprecated
public enum ByRuleType
{
    BY_SECOND ("BYSECOND", BySecond.class, 170)
    {
        @Override ByRule newInstance(String value) { return new BySecond(value); }
        @Override public ByRule newInstance(ByRule source) { return new BySecond((BySecond) source); }
    }
  , BY_MINUTE ("BYMINUTE", ByMinute.class, 160)
    {
        @Override ByRule newInstance(String value) { return new ByMinute(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByMinute((ByMinute) source); }
    }
  , BY_HOUR ("BYHOUR", ByHour.class, 150)
    {
        @Override ByRule newInstance(String value) { return ByHour.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByHour((ByHour) source); }
    }
  , BY_DAY ("BYDAY", ByDay.class, 140)
    {
        @Override ByRule newInstance(String value) { return ByDay.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return null; }
    }
  , BY_MONTH_DAY ("BYMONTHDAY", ByMonthDay.class, 130)
    {
        @Override ByRule newInstance(String value) { return ByMonthDay.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByMonthDay((ByMonthDay) source); }
    }
  , BY_YEAR_DAY ("BYYEARDAY", ByYearDay.class, 120)
    {
        @Override ByRule newInstance(String value) { return ByYearDay.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByYearDay((ByYearDay) source); }
    }
  , BY_WEEK_NUMBER ("BYWEEKNO", ByWeekNumber.class, 110)
    {
        @Override ByRule newInstance(String value) { return ByWeekNumber.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByWeekNumber((ByWeekNumber) source); }
    }
  , BY_MONTH ("BYMONTH", ByMonth.class, 100)
    {
        @Override ByRule newInstance(String value) { return ByMonth.parse(value); }
        @Override public ByRule newInstance(ByRule source) { return new ByMonth((ByMonth) source); }
    }
  , BY_SET_POSITION ("BYSETPOS", BySetPosition.class, 180)
    {
        @Override ByRule newInstance(String value) { return new BySetPosition(value); }
        @Override public ByRule newInstance(ByRule source) { return new BySetPosition((BySetPosition) source); }
    };
  
    // Map to match up name to enum
    private static Map<String, ByRuleType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, ByRuleType> makeEnumFromNameMap()
    {
        Map<String, ByRuleType> map = new HashMap<>();
        ByRuleType[] values = ByRuleType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get enum from name */
    public static ByRuleType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends ByRule>, ByRuleType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends ByRule>, ByRuleType> makeEnumFromClassMap()
    {
        Map<Class<? extends ByRule>, ByRuleType> map = new HashMap<>();
        ByRuleType[] values = ByRuleType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from ByRule */
    static ByRuleType enumFromClass(Class<? extends ByRule> byRuleClass)
    {
        return enumFromClassMap.get(byRuleClass);
    }
    
    /** Returns the iCalendar property name (e.g. LANGUAGE) */
    @Override public String toString() { return name; }
    
    private String name;
    private int sortOrder;
    private Class<? extends ByRule> myClass;
    public int sortOrder() { return sortOrder; }

    ByRuleType(String name, Class<? extends ByRule> myClass, int sortOrder)
    {
        this.sortOrder = sortOrder;
        this.name = name;
        this.myClass = myClass;
    }

    public String toParameterString(Frequency frequency)
    {
        return frequency.lookupByRule(this).toString();
    }
    
    /** Add ByRule to Frequency's ByRule map.  Parses string value
     * into new ByRule */
    public void setValue(Frequency frequency, String value)
    {
        if (frequency.lookupByRule(this) == null)
        {
            frequency.byRules().add(newInstance(value));
        } else
        {
            throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
        }
    }
    
    /*
     * ABSTRACT METHODS
     */
    abstract ByRule newInstance(String value);
    /** return copy of ByRule */
    public abstract ByRule newInstance(ByRule source);
    /** sets parameter value */
//    public abstract void setValue(Frequency frequency, String value);
}
