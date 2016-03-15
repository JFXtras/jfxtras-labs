package jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency;

/** Enumeration of Byxxx rules parts
 * Contains values including the object's class and the order for processing Byxxx Rules
 * from RFC 5545 iCalendar page 44
 * The class is used to make new instances of the different Rules by matching RRULE property
 * to its matching class
 * */
public enum ByRuleParameter
{
    BY_SECOND ("BYSECOND", BySecond.class, 70)
    {
        @Override ByRule newInstance(String value) { return new BySecond(value); }
    }
  , BY_MINUTE ("BYMINUTE", ByMinute.class, 60)
    {
        @Override ByRule newInstance(String value) { return new ByMinute(value); }
    }
  , BY_HOUR ("BYHOUR", ByHour.class, 50)
    {
        @Override ByRule newInstance(String value) { return new ByHour(value); }
    }
  , BY_DAY ("BYDAY", ByDay.class, 40)
    {
        @Override ByRule newInstance(String value) { return new ByDay(value); }
    }
  , BY_MONTH_DAY ("BYMONTHDAY", ByMonthDay.class, 30)
    {
        @Override ByRule newInstance(String value) { return new ByMonthDay(value); }
    }
  , BY_YEAR_DAY ("BYYEARDAY", ByYearDay.class, 20)
    {
        @Override ByRule newInstance(String value) { return new ByYearDay(value); }
    }
  , BY_WEEK_NUMBER ("BYWEEKNO", ByWeekNumber.class, 10)
    {
        @Override ByRule newInstance(String value) { return new ByWeekNumber(value); }
    }
  , BY_MONTH ("BYMONTH", ByMonth.class, 0)
    {
        @Override ByRule newInstance(String value) { return new ByMonth(value); }
    }
  , BY_SET_POSITION ("BYSETPOS", BySetPosition.class, 80)
    {
        @Override ByRule newInstance(String value) { return new BySetPosition(value); }
    };
  
    // Map to match up name to enum
    private static Map<String, ByRuleParameter> propertyFromNameMap = makePropertiesFromNameMap();
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
        return propertyFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends ByRule>, ByRuleParameter> propertyFromClassMap = makePropertiesFromClassMap();
    private static Map<Class<? extends ByRule>, ByRuleParameter> makePropertiesFromClassMap()
    {
        Map<Class<? extends ByRule>, ByRuleParameter> map = new HashMap<>();
        ByRuleParameter[] values = ByRuleParameter.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from ByRule */
    public static ByRuleParameter propertyFromByRule(ByRule byRule)
    {
        return propertyFromClassMap.get(byRule.getClass());
    }
    
    /** Returns the iCalendar property name (e.g. LANGUAGE) */
    @Override public String toString() { return name; }
    
    private String name;
    private int sortOrder;
    private Class<? extends ByRule> myClass;
    public int getSortOrder() { return sortOrder; }

    ByRuleParameter(String name, Class<? extends ByRule> myClass, int sortOrder)
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
    /** sets parameter value */
//    public abstract void setValue(Frequency frequency, String value);
}
