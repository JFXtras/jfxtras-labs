package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;

/** MINUTELY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Minutely extends FrequencyAbstract<Minutely>
{
    // Constructor
    public Minutely() { super(FrequencyEnum.MINUTELY); }

    public Minutely(Frequency source) { super(source); }
}
