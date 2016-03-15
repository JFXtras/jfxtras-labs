package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import jfxtras.labs.icalendar.properties.recurrence.rrule.byxxx.ByRule;

public class FrequencyUtilities
{
    /** Deep copy all fields from source to destination */
    @Deprecated // replace with enum loop copy
    public static void copy(Frequency source, Frequency destination)
    {
//        destination.setChronoUnit(source.getChronoUnit());
        if (source.getInterval() != null) destination.setInterval(source.getInterval());
        if (source.byRules() != null)
        {
         // TODO - replace with enum loop
            source.byRules().stream().forEach(r ->
            {
                try {
                    ByRule newRule = r.getClass().newInstance();
                    ByRule.copy(r, newRule);
                    destination.byRules().add(newRule);
//                    destination.addByRule(newRule);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        }
    }
    
    /** Enumeration of FREQ rules
     * 
     * Note: Enum value equals iCalendar property name so toString doesn't need to be overridden
     * 
     */
    public static enum FrequencyParameter
    {
        YEARLY (ChronoUnit.YEARS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Yearly();
            }
        },
        MONTHLY (ChronoUnit.MONTHS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Monthly();
            }
        },
        WEEKLY (ChronoUnit.WEEKS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Weekly();
            }
        },
        DAILY (ChronoUnit.DAYS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Daily();
            }
        },
        HOURLY (ChronoUnit.HOURS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Hourly();

            }
        },
        MINUTELY (ChronoUnit.MINUTES) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Minutely();
            }
        },
        SECONDLY (ChronoUnit.SECONDS) {
            @Override
            public String toParameterString(Frequency frequency)
            {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public void copyProperty(Frequency source, Frequency destination)
            {
                // TODO Auto-generated method stub
                
            }

            @Override
            public Frequency newInstance()
            {
                return new Secondly();
            }
        };
        
        ChronoUnit chronoUnit;
        FrequencyParameter(ChronoUnit chronoUnit)
        {
            this.chronoUnit = chronoUnit;
        }
        public ChronoUnit getChronoUnit() { return chronoUnit; }

        // Map to match up string name to enum
        private static Map<String, FrequencyParameter> propertyFromNameMap = makePropertiesFromNameMap();
        private static Map<String, FrequencyParameter> makePropertiesFromNameMap()
        {
            Map<String, FrequencyParameter> map = new HashMap<>();
            FrequencyParameter[] values = FrequencyParameter.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static FrequencyParameter propertyFromName(String propertyName)
        {
            return propertyFromNameMap.get(propertyName.toUpperCase());
        }
        
//        /** sets parameter value */
//        public abstract void setValue(RRule rrule, String value);
        
        /** makes content line (RFC 5545 3.1) from a vComponent property  */
        @Deprecated // I don't think this is needed - handled by RRuleProperty
        public abstract String toParameterString(Frequency frequency);
        
        /** Copies property value from source to destination */
        public abstract void copyProperty(Frequency source, Frequency destination);
        
        public abstract Frequency newInstance();
    }
}
