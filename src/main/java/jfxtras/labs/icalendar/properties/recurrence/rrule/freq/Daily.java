package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyParameter;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract<Daily>
{    
//    // adjusts temporal parameter to become date/time of next event
//    @Override public TemporalAdjuster adjuster() { return (temporal) -> temporal.plus(getInterval(), DAYS); }

    // Constructor
    public Daily() { super(FrequencyParameter.DAILY); }
}
