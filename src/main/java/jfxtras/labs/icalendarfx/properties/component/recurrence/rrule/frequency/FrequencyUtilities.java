package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

public class FrequencyUtilities
{
    /** Deep copy all fields from source to destination */
//    @Deprecated // replace with enum loop copy
//    public static void copy(Frequency source, Frequency destination)
//    {
//        destination = source.frequencyType().newInstance(source);
        // source.frequencyType().copyProperty(source, destination);

//        destination.setChronoUnit(source.getChronoUnit());
//        if (source.getInterval() != null) destination.setInterval(source.getInterval());
//        if (source.byRules() != null)
//        {
//         // TODO - replace with enum loop
//            source.byRules().stream().forEach(r ->
//            {
//                try {
//                    ByRule newRule = r.getClass().newInstance();
//                    ByRule.copy(r, newRule);
//                    destination.byRules().add(newRule);
////                    destination.addByRule(newRule);
//                } catch (InstantiationException | IllegalAccessException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
    
    // TODO - MOVE OUT OF CLASS INTO OWN FILE ENUM
    /** Enumeration of FREQ rules
     * 
     * Note: Enum value equals iCalendar property name so toString doesn't need to be overridden
     * 
     */
    public static enum FrequencyEnum
    {
        YEARLY (ChronoUnit.YEARS) {
            @Override public Frequency newInstance() { return new Yearly(); }
            @Override public Frequency newInstance(Frequency source) { return new Yearly(source); }
        },
        MONTHLY (ChronoUnit.MONTHS) {
            @Override public Frequency newInstance() { return new Monthly(); }
            @Override public Frequency newInstance(Frequency source) { return new Monthly(source); }
        },
        WEEKLY (ChronoUnit.WEEKS) {
            @Override public Frequency newInstance() { return new Weekly(); }
            @Override public Frequency newInstance(Frequency source) { return new Weekly(source); }
        },
        DAILY (ChronoUnit.DAYS) {
            @Override public Frequency newInstance() { return new Daily(); }
            @Override public Frequency newInstance(Frequency source) { return new Daily(source); }
        },
        HOURLY (ChronoUnit.HOURS) {
            @Override public Frequency newInstance() { return new Hourly(); }
            @Override public Frequency newInstance(Frequency source) { return new Hourly(source); }
        },
        MINUTELY (ChronoUnit.MINUTES) {
            @Override public Frequency newInstance() { return new Minutely(); }
            @Override public Frequency newInstance(Frequency source) { return new Minutely(source); }
        },
        SECONDLY (ChronoUnit.SECONDS) {
            @Override public Frequency newInstance() { return new Secondly(); }
            @Override public Frequency newInstance(Frequency source) { return new Secondly(source); }
        };
        
        ChronoUnit chronoUnit;
        FrequencyEnum(ChronoUnit chronoUnit)
        {
            this.chronoUnit = chronoUnit;
        }
        public ChronoUnit getChronoUnit() { return chronoUnit; }

        // Map to match up string name to enum
        private static Map<String, FrequencyEnum> propertyFromNameMap = makePropertiesFromNameMap();
        private static Map<String, FrequencyEnum> makePropertiesFromNameMap()
        {
            Map<String, FrequencyEnum> map = new HashMap<>();
            FrequencyEnum[] values = FrequencyEnum.values();
            for (int i=0; i<values.length; i++)
            {
                map.put(values[i].toString(), values[i]);
            }
            return map;
        }
        /** get enum from name */
        public static FrequencyEnum propertyFromName(String propertyName)
        {
            return propertyFromNameMap.get(propertyName.toUpperCase());
        }
        
//        /** sets parameter value */
//        public abstract void setValue(RRule rrule, String value);
        
        /** makes content line (RFC 5545 3.1) from a vComponent property  */
//        @Deprecated // I don't think this is needed - handled by RRuleProperty
//        public abstract String toParameterString(Frequency frequency);
//        
//        /** Copies property value from source to destination */
//        public abstract void copyProperty(Frequency source, Frequency destination);
        
        public abstract Frequency newInstance();
        /** return copy of Frequency */
        public abstract Frequency newInstance(Frequency source);
        
//        public abstract Frequency newInstance();
    }
}
