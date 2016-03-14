package jfxtras.labs.icalendar.properties.recurrence.rrule;

import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByMonth;
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
            // TODO Auto-generated method stub
            
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
            rrule.setFrequency( FrequencyParameter.propertyFromName(value).newInstance() );
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
            // TODO Auto-generated method stub
            
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
            // TODO Auto-generated method stub
            
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
    WEEK_START ("WKST") { // THIS PROPERTY MAY BE HANDLED BY LOCALE
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
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
    BY_SECOND ("BYSECOND") {
        @Override
        public void setValue(RRule rrule, String value)
        {
            // TODO Auto-generated method stub
            
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
    } // Not implemented
  , BY_MINUTE ("BYMINUTE") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
} // Not implemented
  , BY_HOUR ("BYHOUR") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
} // Not implemented
  , BY_DAY ("BYDAY") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
}
  , BY_MONTH_DAY ("BYMONTHDAY") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
}
  , BY_YEAR_DAY ("BYYEARDAY") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
} // Not implemented
  , BY_WEEK_NUMBER ("BYWEEKNO") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
}
  , BY_MONTH ("BYMONTH") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        rrule.getFrequency().byRules().put(this, new ByMonth(value));
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
}
  , BY_SET_POSITION ("BYSETPOS") {
    @Override
    public void setValue(RRule rrule, String value)
    {
        // TODO Auto-generated method stub
        
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
}; // Not implemented
        
    // Map to match up name to enum
    private static Map<String, RRuleParameter> propertyFromTagMap = makePropertiesFromNameMap();
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
        return propertyFromTagMap.get(propertyName.toUpperCase());
    }
    
    private String name;
    
    RRuleParameter(String name)
    {
        this.name = name;
    }
    
    /** Returns the iCalendar property name (e.g. LANGUAGE) */
    @Override public String toString() { return name; }
    
    /** sets parameter value */
    public abstract void setValue(RRule rrule, String value);
    
    /** makes content line (RFC 5545 3.1) from a RRuleProperty property  */
    public abstract String toParameterString(RRule rrule);
    
    /** Copies property value from source to destination */
    public abstract void copyProperty(RRule source, RRule destination);
        
}
