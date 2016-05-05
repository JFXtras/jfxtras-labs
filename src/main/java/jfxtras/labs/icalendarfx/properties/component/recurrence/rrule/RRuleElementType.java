package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

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
    FREQUENCY ("FREQ", Frequency2.class, 0) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.getFrequency();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            if (recurrenceRule.getFrequency() == null)
            {
                recurrenceRule.setFrequency(Frequency2.parse(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

//        @Override
//        public String elementToString(Object element)
//        {
//            return FREQUENCY.toString() + "=" + ((Frequency2) element).toString();
//        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return new StringConverter<T>()
//            {
//                @Override
//                public String toString(T object)
//                {
//                    return FREQUENCY.toString() + "=" + ((Frequency2) object).toString();
//                }
//
//                @Override
//                public T fromString(String string)
//                {
//                    return (T) string;
//                }
//            };
//        }
    },
    UNTIL ("UNTIL", Until.class, 0) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.getUntil();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            if (recurrenceRule.getUntil() == null)
            {
                recurrenceRule.setUntil(DateTimeUtilities.temporalFromString(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return new StringConverter<T>()
//            {
//                @Override
//                public String toString(T object)
//                {
//                    return UNTIL.toString() + "=" + DateTimeUtilities.temporalToString((Temporal) object);
//                }
//
//                @Override
//                public T fromString(String string)
//                {
//                    // TODO - NOT BEING USED - SHOULD CONVERTER BE REPLACED BY ONE METHOD?
//                    return (T) DateTimeUtilities.temporalFromString(string);
//                }
//            };
//        }
    },
    COUNT ("COUNT", Count.class, 0) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.getCount();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    INTERVAL ("INTERVAL", Interval.class, 0) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
//            return (rrule.getInterval() == RecurrenceRule3.DEFAULT_INTERVAL) ? null : rrule.getInterval();
            return rrule.getInterval();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    WEEK_START ("WKST", WeekStart.class, 0) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
//            return (rrule.getWeekStart() == null) ? RecurrenceRule3.DEFAULT_WEEK_START : rrule.getWeekStart();
            return rrule.getWeekStart();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_MONTH ("BYMONTH", ByMonth.class, 100) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMonth.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByMonth.class) == null)
            {
                recurrenceRule.byRules().add(ByMonth.parse(content));
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return new StringConverter<T>()
//            {
//                @Override
//                public String toString(T object)
//                {
//                    return object.toString();
//                }
//
//                @Override
//                public T fromString(String string)
//                {
//                    return (T) string;
//                }
//            };
//        }
    },
    BY_WEEK_NUMBER ("BYWEEKNO", ByWeekNumber.class, 110) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByWeekNumber.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_YEAR_DAY ("BYYEARDAY", ByYearDay.class, 120) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByYearDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_MONTH_DAY ("BYMONTHDAY", ByMonthDay.class, 130) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMonthDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_DAY ("BYDAY", ByDay.class, 140) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            if (recurrenceRule.lookupByRule(ByDay.class) == null)
            {
                recurrenceRule.byRules().add(ByDay.parse(content));                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return new StringConverter<T>()
//            {
//                @Override
//                public String toString(T object)
//                {
//                    return object.toString();
//                }
//
//                @Override
//                public T fromString(String string)
//                {
//                    return (T) string;
//                }
//            };
//        }
    },
    BY_HOUR ("BYHOUR", ByHour.class, 150) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByHour.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_MINUTE ("BYMINUTE", ByMinute.class, 160) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMinute.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_SECOND ("BYSECOND", BySecond.class, 170) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(BySecond.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
    },
    BY_SET_POSITION ("BYSETPOS", BySetPosition.class, 180) {
        @Override
        public RRuleElement<?> getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(BySetPosition.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

//        @Override
//        public <T> StringConverter<T> getConverter()
//        {
//            return new StringConverter<T>()
//            {
//                @Override
//                public String toString(T object)
//                {
//                    return object.toString();
//                }
//
//                @Override
//                public T fromString(String string)
//                {
//                    return (T) string;
//                }
//            };
//        }
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
    
    int sortOrder;
    public int sortOrder() { return sortOrder; }
    
    RRuleElementType(String name, Class<? extends RRuleElement<?>> myClass, int sortOrder)
    {
        this.name = name;
        this.myClass = myClass;
        this.sortOrder = sortOrder;
    }
 
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Element */
    abstract public RRuleElement<?> getElement(RecurrenceRule3 rrule);
    
    abstract public void parse(RecurrenceRule3 recurrenceRule, String content);
    
//    public String elementToString(Object object)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
    
    /** return default String converter associated with property value type */
//    @Deprecated
//    public <T> StringConverter<T> getConverter() {
//        return null; }



    
//    abstract public String elementToString(RecurrenceRule3 recurrenceRule);



//    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
//    abstract public void parse(VComponentNew<?> vComponent, String propertyContent);
//
//    /** copies the associated property from the source component to the destination component */
//    abstract public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination);

}
