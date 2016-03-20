package jfxtras.labs.icalendar.properties.recurrence.rrule;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.Frequency;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;

// TODO - SHOULD ALL RRULE PARAMETERS GO HERE? - BY RULE ONES TOO?
/**
 * RRule properties with the following data and methods:
 * 
 * @author David Bal
 *
 */
public enum RRuleParameter
{
    FREQUENCY ("FREQ") { // FREQUENCY needs to be first
        @Override
        public void setValue(RecurrenceRule rrule, String value)
        {
            if (rrule.getFrequency() == null)
            {
                rrule.setFrequency( FrequencyEnum.propertyFromName(value).newInstance() );                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
            }
        }

        @Override
        public String toParameterString(RecurrenceRule rrule)
        {
            return toString() + "=" + rrule.getFrequency().toString();
        }

        @Override
        public void copyProperty(RecurrenceRule source, RecurrenceRule destination)
        {
            Frequency copiedFrequency = source.getFrequency().frequencyType().newInstance(source.getFrequency()); // copy frequency
            destination.setFrequency(copiedFrequency);
        }

        @Override
        public boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2)
        {
            return r1.getFrequency().equals(r2.getFrequency());
        }
    },
    INTERVAL ("INTERVAL") { // TODO - SHOULD THIS GO UNDER FREQUENCY??? HOW? ITS THE ONLY PARAMETER THERE
        @Override
        public void setValue(RecurrenceRule rrule, String value)
        {
            if (rrule.getFrequency() != null)
            {
                if (rrule.getFrequency().getInterval() == 1)
                {
                    rrule.getFrequency().setInterval(Integer.parseInt(value));
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
                }
            } else
            {
                throw new RuntimeException(FREQUENCY + " must be set before " + this + " can be set");
            }
        }

        @Override
        public String toParameterString(RecurrenceRule rrule)
        {
            Integer interval = rrule.getFrequency().getInterval();
            return (interval > 1) ? toString() + "=" + interval.toString(): null; // 1 is default interval, therefore only output interval > 1
        }

        @Override
        public void copyProperty(RecurrenceRule source, RecurrenceRule destination)
        {
            destination.getFrequency().setInterval(source.getFrequency().getInterval());
        }

        @Override
        public boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2)
        {
            return r1.getFrequency().getInterval().equals(r2.getFrequency().getInterval());
        }
    },
    COUNT ("COUNT") {
        @Override
        public void setValue(RecurrenceRule rrule, String value)
        {
            if (rrule.getCount() == RecurrenceRule.INITIAL_COUNT)
            {
                if (rrule.getUntil() == null)
                {
                    rrule.setCount(Integer.parseInt(value));                    
                } else
                {
                    throw new IllegalArgumentException(toString() + " can't be set while " + UNTIL.toString() + " has a value");                                        
                }
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
            }
        }

        @Override
        public String toParameterString(RecurrenceRule rrule)
        {
            return (rrule.getCount() == RecurrenceRule.INITIAL_COUNT) ? null : toString() + "=" + rrule.getCount();
        }

        @Override
        public void copyProperty(RecurrenceRule source, RecurrenceRule destination)
        {
            destination.setCount(source.getCount());
        }

        @Override
        public boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2)
        {
            return r1.getCount().equals(r2.getCount());
        }
    },
    UNTIL ("UNTIL") {
        @Override
        public void setValue(RecurrenceRule rrule, String value)
        {
            if (rrule.getUntil() == null)
            {
                if (rrule.getCount() == RecurrenceRule.INITIAL_COUNT)
                {
                    System.out.println("until:" + value + " " + DateTimeUtilities.parse(value));
                    rrule.setUntil(DateTimeUtilities.parse(value));                    
                } else
                {
                    throw new IllegalArgumentException(toString() + " can't be set while " + COUNT.toString() + " has a value");                                        
                }
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");                    
            }
        }

        @Override
        public String toParameterString(RecurrenceRule rrule)
        {
            return (rrule.getUntil() == null) ? null : toString() + "=" + DateTimeUtilities.format(rrule.getUntil());
        }

        @Override
        public void copyProperty(RecurrenceRule source, RecurrenceRule destination)
        {
            if (source.getUntil() != null)
            {
                destination.setUntil(source.getUntil());
            }
        }

        @Override
        public boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2)
        {
            return (r1.getUntil() == null) ? (r2.getUntil() == null) : r1.getUntil().equals(r2.getUntil());
        }
    },
    WEEK_START ("WKST") { // TODO - THIS PROPERTY MAY BE BEST HANDLED BY LOCALE - NOT PROCESSED NOW
        // TODO - SUPPOSE TO COME AFTER BYRULES
        @Override
        public void setValue(RecurrenceRule rrule, String value)
        {
            throw new RuntimeException("not supported");
        }

        @Override
        public String toParameterString(RecurrenceRule rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyProperty(RecurrenceRule source, RecurrenceRule destination)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2)
        {
            // TODO Auto-generated method stub
            return true;
        }
    };
        
    // Map to match up name to enum
    private static Map<String, RRuleParameter> propertyFromNameMap = makePropertiesFromNameMap();
    private static Map<String, RRuleParameter> makePropertiesFromNameMap()
    {
        Map<String, RRuleParameter> map = new HashMap<>();
        RRuleParameter[] values = RRuleParameter.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get enum from name */
    public static RRuleParameter propertyFromName(String propertyName)
    {
        return propertyFromNameMap.get(propertyName.toUpperCase());
    }
    
    private String name;
//    private int sortOrder;
    
    RRuleParameter(String name)
    {
        this.name = name;
//        this.sortOrder = sortOrder;
    }
    
    /** Returns the iCalendar property name (e.g. LANGUAGE) */
    @Override public String toString() { return name; }
    
//    public int sortOrder() { return sortOrder; }
    
    /** sets parameter value */
    public abstract void setValue(RecurrenceRule rrule, String value);
    
    /** makes content line (RFC 5545 3.1) from a RRuleProperty property  */
    public abstract String toParameterString(RecurrenceRule rrule);
    
    /** Copies property value from source to destination */
    public abstract void copyProperty(RecurrenceRule source, RecurrenceRule destination);
    
    /** Checks is corresponding property is equal between r1 and r2 */
    public abstract boolean isPropertyEqual(RecurrenceRule r1, RecurrenceRule r2);
        
}
