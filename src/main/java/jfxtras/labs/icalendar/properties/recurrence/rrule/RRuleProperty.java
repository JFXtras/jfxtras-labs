package jfxtras.labs.icalendar.properties.recurrence.rrule;

import java.util.HashMap;
import java.util.Map;

/**
 * RRule properties with the following data and methods:
 * 
 * @author David Bal
 *
 */
public enum RRuleProperty
{
    FREQUENCY ("FREQ") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    COUNT ("COUNT") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    INTERVAL ("INTERVAL") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_SECOND ("BYSECOND") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_MINUTE ("BYMINUTE") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_HOUR ("BYHOUR") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_DAY ("BYDAY") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_MONTH_DAY ("BYMONTHDAY") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_YEAR_DAY ("BYYEARDAY") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_WEEK_NO ("BYWEEKNO") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_MONTH ("BYMONTH") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    BY_SET_POSITION ("BYSETPOS") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
    WEEK_START ("WKST") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
        }

        @Override
        public String makeParameterString(RRule rrule)
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
        
    // Map to match up string tag to TextPropertyParameter enum
    private static Map<String, RRuleProperty> propertyFromTagMap = makePropertiesFromNameMap();
    private static Map<String, RRuleProperty> makePropertiesFromNameMap()
    {
        Map<String, RRuleProperty> map = new HashMap<>();
        RRuleProperty[] values = RRuleProperty.values();
        for (int i=0; i<values.length; i++)
        {
            map.put(values[i].toString(), values[i]);
        }
        return map;
    }
    /** get TextPropertyParameter enum from property name */
    public static RRuleProperty propertyFromName(String propertyName)
    {
        return propertyFromTagMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    
    RRuleProperty(String name)
    {
        this.name = name;
    }
    
    /** Returns the iCalendar property name (e.g. LANGUAGE) */
    @Override public String toString() { return name; }
    
    /** sets parameter value */
    public abstract void setValue(RRule rrule, String value);
    
    /** makes content line (RFC 5545 3.1) from a RRuleProperty property  */
    public abstract String makeParameterString(RRule rrule);
    
    /** Copies property value from source to destination */
    public abstract void copyProperty(RRule source, RRule destination);
        
}
