package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendarfx.parameters.ParameterType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public enum RRuleElementType
{
    FREQUENCY ("FREQ", Frequency.class, 0, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.getFrequency();
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.getFrequency() == null)
            {
                recurrenceRule.setFrequency(Frequency.parse(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.setFrequency(new Frequency(source.getFrequency()));
        }
    },
    INTERVAL ("INTERVAL", Interval.class, 0, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.getInterval();
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.getInterval() == null)
            {
                recurrenceRule.setInterval(Integer.parseInt(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.setInterval(new Interval(source.getInterval()));
        }
    },
    UNTIL ("UNTIL", Until.class, 0, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.getUntil();
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.getUntil() == null)
            {
                recurrenceRule.setUntil(DateTimeUtilities.temporalFromString(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.setUntil(new Until(source.getUntil()));
        }
    },
    COUNT ("COUNT", Count.class, 0, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.getCount();
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.getCount() == null)
            {
                recurrenceRule.setCount(Integer.parseInt(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.setCount(new Count(source.getCount()));
        }
    },
    WEEK_START ("WKST", WeekStart.class, 0, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.getWeekStart();
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.getWeekStart() == null)
            {
                DayOfWeek dayOfWeek = DateTimeUtilities.dayOfWeekFromAbbreviation(content);
                recurrenceRule.setWeekStart(dayOfWeek);
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.setWeekStart(new WeekStart(source.getWeekStart()));
        }
    },
    BY_MONTH ("BYMONTH", ByMonth.class, 100, ChronoUnit.MONTHS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByMonth.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByMonth.class) == null)
            {
                recurrenceRule.byRules().add(ByMonth.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByMonth((ByMonth) source.lookupByRule(ByMonth.class)));
        }
    },
    BY_WEEK_NUMBER ("BYWEEKNO", ByWeekNumber.class, 110, ChronoUnit.DAYS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByWeekNumber.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByWeekNumber.class) == null)
            {
                recurrenceRule.byRules().add(ByWeekNumber.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByWeekNumber((ByWeekNumber) source.lookupByRule(ByWeekNumber.class)));
        }
    },
    BY_YEAR_DAY ("BYYEARDAY", ByYearDay.class, 120, ChronoUnit.DAYS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByYearDay.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByYearDay.class) == null)
            {
                recurrenceRule.byRules().add(ByYearDay.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByYearDay((ByYearDay) source.lookupByRule(ByYearDay.class)));
        }
    },
    BY_MONTH_DAY ("BYMONTHDAY", ByMonthDay.class, 130, ChronoUnit.DAYS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByMonthDay.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByMonthDay.class) == null)
            {
                recurrenceRule.byRules().add(ByMonthDay.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByMonthDay((ByMonthDay) source.lookupByRule(ByMonthDay.class)));
        }
    },
    BY_DAY ("BYDAY", ByDay.class, 140, ChronoUnit.DAYS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByDay.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByDay.class) == null)
            {
                recurrenceRule.byRules().add(ByDay.parse(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByDay((ByDay) source.lookupByRule(ByDay.class)));
        }
    },
    BY_HOUR ("BYHOUR", ByHour.class, 150, ChronoUnit.HOURS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByHour.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByHour.class) == null)
            {
                recurrenceRule.byRules().add(ByHour.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByWeekNumber((ByWeekNumber) source.lookupByRule(ByWeekNumber.class)));
        }
    },
    BY_MINUTE ("BYMINUTE", ByMinute.class, 160, ChronoUnit.MINUTES) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(ByMinute.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByMinute.class) == null)
            {
                recurrenceRule.byRules().add(ByMinute.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new ByMinute((ByMinute) source.lookupByRule(ByMinute.class)));
        }
    },
    BY_SECOND ("BYSECOND", BySecond.class, 170, ChronoUnit.SECONDS) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(BySecond.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(BySecond.class) == null)
            {
                recurrenceRule.byRules().add(BySecond.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new BySecond((BySecond) source.lookupByRule(BySecond.class)));
        }
    },
    BY_SET_POSITION ("BYSETPOS", BySetPosition.class, 180, null) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule2 rrule)
        {
            return rrule.lookupByRule(BySetPosition.class);
        }

        @Override
        public void parse(RecurrenceRule2 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(BySetPosition.class) == null)
            {
                recurrenceRule.byRules().add(BySetPosition.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination)
        {
            destination.byRules().add(new BySetPosition((BySetPosition) source.lookupByRule(BySetPosition.class)));
        }
    };
    
    // Map to match up name to enum
    private static Map<String, RRuleElementType> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, RRuleElementType> makeEnumFromNameMap()
    {
        Map<String, RRuleElementType> map = new HashMap<>();
        RRuleElementType[] values = RRuleElementType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static RRuleElementType enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    // Map to match up class to enum
    private static Map<Class<? extends RRuleElement<?>>, RRuleElementType> enumFromClassMap = makeEnumFromClassMap();
    private static Map<Class<? extends RRuleElement<?>>, RRuleElementType> makeEnumFromClassMap()
    {
        Map<Class<? extends RRuleElement<?>>, RRuleElementType> map = new HashMap<>();
        RRuleElementType[] values = RRuleElementType.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].myClass, values[i]);
        }
        return map;
    }
    /** get enum from map */
    public static RRuleElementType enumFromClass(Class<? extends RRuleElement> myClass)
    {
        RRuleElementType p = enumFromClassMap.get(myClass);
        if (p == null)
        {
            throw new IllegalArgumentException(ParameterType.class.getSimpleName() + " does not contain an enum to match the class:" + myClass.getSimpleName());
        }
        return p;
    }
    
    private String name;
    @Override
    public String toString() { return name; }
    
    private Class<? extends RRuleElement<?>> myClass;
    
    private int sortOrder;
    public int sortOrder() { return sortOrder; }
    
    private ChronoUnit chronoUnit;
    public ChronoUnit getChronoUnit() { return chronoUnit; }
    
    RRuleElementType(String name, Class<? extends RRuleElement<?>> myClass, int sortOrder, ChronoUnit chronoUnit)
    {
        this.name = name;
        this.myClass = myClass;
        this.sortOrder = sortOrder;
        this.chronoUnit = chronoUnit;
    }
 
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Element */
    abstract public RRuleElement<?> getElement(RecurrenceRule2 rrule);
    
    abstract public void parse(RecurrenceRule2 recurrenceRule, String content);
    
    /** copies the associated element from the source RecurrenceRule to the destination RecurrenceRule */
    abstract public void copyElement(RecurrenceRule2 source, RecurrenceRule2 destination);
}
