package jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;

/** SECONDLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Secondly extends FrequencyAbstract<Secondly>
{
    // Constructor
    public Secondly() { super(FrequencyEnum.SECONDLY); }
    
    public Secondly(Frequency source) { super(source); }
}
