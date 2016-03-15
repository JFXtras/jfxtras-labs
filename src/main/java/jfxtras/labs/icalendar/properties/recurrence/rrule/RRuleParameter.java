package jfxtras.labs.icalendar.properties.recurrence.rrule;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities;
import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyParameter;

/**
 * RRule properties with the following data and methods:
 * 
 * @author David Bal
 *
 */
public enum RRuleParameter
{
    COUNT ("COUNT") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            if (rrule.getCount() == null)
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
        public String toParameterString(RRule rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyProperty(RRule source, RRule destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    FREQUENCY ("FREQ") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            if (rrule.getFrequency() == null)
            {
                rrule.setFrequency( FrequencyParameter.propertyFromName(value).newInstance() );                
            } else
            {
                throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
            }
        }

        @Override
        public String toParameterString(RRule rrule)
        {
            return toString() + "=" + rrule.getFrequency().toString();
        }

        @Override
        public void copyProperty(RRule source, RRule destination)
        {
            FrequencyUtilities.copy(source.getFrequency(), destination.getFrequency());
        }
    },

    INTERVAL ("INTERVAL") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            if (rrule.getFrequency() != null)
            {
                if (rrule.getFrequency().getInterval() == 0)
                {
                    rrule.getFrequency().setInterval(Integer.parseInt(value));
                } else
                {
                    throw new IllegalArgumentException(toString() + " can only appear once in calendar component");
                }
            } else
            {
                throw new RuntimeException(FREQUENCY + "must be set before " + this + " can be set");
            }
        }

        @Override
        public String toParameterString(RRule rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyProperty(RRule source, RRule destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    UNTIL ("UNTIL") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            if (rrule.getUntil() == null)
            {
                if (rrule.getCount() == null)
                {
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
        public String toParameterString(RRule rrule)
        {
            return null;
        }

        @Override
        public void copyProperty(RRule source, RRule destination)
        {
            // TODO Auto-generated method stub
            
        }
    },
    WEEK_START ("WKST") { // TODO - THIS PROPERTY MAY BE BEST HANDLED BY LOCALE - NOT PROCESSED NOW
        @Override
        public void setValue(RRule rrule, String value)
        {
            throw new RuntimeException("not supported");
        }

        @Override
        public String toParameterString(RRule rrule)
        {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void copyProperty(RRule source, RRule destination)
        {
            // TODO Auto-generated method stub
            
        }
    };
//    BY_SECOND ("BYSECOND", 170) {
//        @Override
//        public void setValue(RRule rrule, String value)
//        {
//            // TODO Auto-generated method stub
//            
//        }
//
//        @Override
//        public String toParameterString(RRule rrule)
//        {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public void copyProperty(RRule source, RRule destination)
//        {
//            // TODO Auto-generated method stub
//            
//        }
//    } // Not implemented
//  , BY_MINUTE ("BYMINUTE", 160) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//} // Not implemented
//  , BY_HOUR ("BYHOUR", 150) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//} // Not implemented
//  , BY_DAY ("BYDAY", 140) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//}
//  , BY_MONTH_DAY ("BYMONTHDAY", 130) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//}
//  , BY_YEAR_DAY ("BYYEARDAY", 120) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//} // Not implemented
//  , BY_WEEK_NUMBER ("BYWEEKNO", 110) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//}
//  , BY_MONTH ("BYMONTH", 100) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        rrule.getFrequency().byRules().put(this, new ByMonth(value));
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//}
//  , BY_SET_POSITION ("BYSETPOS", 180) {
//    @Override
//    public void setValue(RRule rrule, String value)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//
//    @Override
//    public String toParameterString(RRule rrule)
//    {
//        // TODO Auto-generated method stub
//        return null;
//    }
//
//    @Override
//    public void copyProperty(RRule source, RRule destination)
//    {
//        // TODO Auto-generated method stub
//        
//    }
//}; // Not implemented
        
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
    
//    public int getSortOrder() { return sortOrder; }
    
    /** sets parameter value */
    public abstract void setValue(RRule rrule, String value);
    
    /** makes content line (RFC 5545 3.1) from a RRuleProperty property  */
    public abstract String toParameterString(RRule rrule);
    
    /** Copies property value from source to destination */
    public abstract void copyProperty(RRule source, RRule destination);
        
}
