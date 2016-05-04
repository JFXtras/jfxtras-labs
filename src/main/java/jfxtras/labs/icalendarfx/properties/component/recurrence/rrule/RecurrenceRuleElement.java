package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule;

import java.time.temporal.Temporal;
import java.util.HashMap;
import java.util.Map;

import javafx.util.StringConverter;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByHour;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMinute;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.BySecond;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.BySetPosition;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByWeekNumber;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.Frequency2;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;

public enum RecurrenceRuleElement
{
    FREQUENCY ("FREQ") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.getFrequency();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            System.out.println("set frequency:");
            if (recurrenceRule.getFrequency() == null)
            {
                recurrenceRule.setFrequency(Frequency2.parse(content));                
                System.out.println("set frequency2:" + recurrenceRule.getFrequency());
            } else
            {
                throw new IllegalArgumentException(toString() + " can only occur once in a calendar component");
            }
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return FREQUENCY.toString() + "=" + ((Frequency2) object).toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) string;
                }
            };
        }
    },
    UNTIL ("UNTIL") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.getUntil();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return UNTIL.toString() + "=" + DateTimeUtilities.temporalToString((Temporal) object);
                }

                @Override
                public T fromString(String string)
                {
                    return (T) DateTimeUtilities.temporalFromString(string);
                }
            };
        }
    },
    COUNT ("COUNT") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.getCount();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    INTERVAL ("INTERVAL") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return (rrule.getInterval() == RecurrenceRule3.DEFAULT_INTERVAL) ? null : rrule.getInterval();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    WEEK_START ("WKST") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return (rrule.getWeekStart() == RecurrenceRule3.DEFAULT_WEEK_START) ? null : rrule.getWeekStart();
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MONTH ("BYMONTH") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMonth.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) string;
                }
            };
        }
    },
    BY_WEEK_NUMBER ("BYWEEKNO") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByWeekNumber.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_YEAR_DAY ("BYYEARDAY") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByYearDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MONTH_DAY ("BYMONTHDAY") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMonthDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_DAY ("BYDAY") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByDay.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) string;
                }
            };
        }
    },
    BY_HOUR ("BYHOUR") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByHour.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_MINUTE ("BYMINUTE") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(ByMinute.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_SECOND ("BYSECOND") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(BySecond.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            // TODO Auto-generated method stub
            return null;
        }
    },
    BY_SET_POSITION ("BYSETPOS") {
        @Override
        public Object getElement(RecurrenceRule3 rrule)
        {
            return rrule.lookupByRule(BySetPosition.class);
        }

        @Override
        public void parse(RecurrenceRule3 recurrenceRule, String content)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public <T> StringConverter<T> getConverter()
        {
            return new StringConverter<T>()
            {
                @Override
                public String toString(T object)
                {
                    return object.toString();
                }

                @Override
                public T fromString(String string)
                {
                    return (T) string;
                }
            };
        }
    };
    
    // Map to match up name to enum
    private static Map<String, RecurrenceRuleElement> enumFromNameMap = makeEnumFromNameMap();
    private static Map<String, RecurrenceRuleElement> makeEnumFromNameMap()
    {
        Map<String, RecurrenceRuleElement> map = new HashMap<>();
        RecurrenceRuleElement[] values = RecurrenceRuleElement.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    public static RecurrenceRuleElement enumFromName(String propertyName)
    {
        return enumFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    @Override
    public String toString() { return name; }
  
    RecurrenceRuleElement(String name)
    {
        this.name = name;
    }
 
    /*
     * ABSTRACT METHODS
     */
    /** Returns associated Element */
    abstract public Object getElement(RecurrenceRule3 rrule);
    
    abstract public void parse(RecurrenceRule3 recurrenceRule, String content);
    
    /** return default String converter associated with property value type */
    abstract public <T> StringConverter<T> getConverter();
    
//    abstract public String elementToString(RecurrenceRule3 recurrenceRule);



//    /** Parses string and sets property.  Called by {@link VComponentBase#parseContent()} */
//    abstract public void parse(VComponentNew<?> vComponent, String propertyContent);
//
//    /** copies the associated property from the source component to the destination component */
//    abstract public void copyProperty(VComponentNew<?> source, VComponentNew<?> destination);

}
