package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.recurrence.rrule.freq.FrequencyUtilities.FrequencyParameter;

/** HOURLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Hourly extends FrequencyAbstract<Hourly>
{
    // Constructor
    public Hourly() { super(FrequencyParameter.HOURLY); }
}
