package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyParameter;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract<Yearly>
{
    // Constructor
    public Yearly() { super(FrequencyParameter.YEARLY); }
}
