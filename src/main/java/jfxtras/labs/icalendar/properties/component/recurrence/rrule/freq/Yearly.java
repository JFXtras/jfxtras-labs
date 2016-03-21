package jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract<Yearly>
{
    // Constructors
    public Yearly() { super(FrequencyEnum.YEARLY); }
    
    public Yearly(Frequency source) { super(source); }
}
