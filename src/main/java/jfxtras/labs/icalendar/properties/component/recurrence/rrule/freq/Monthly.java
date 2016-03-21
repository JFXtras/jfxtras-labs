package jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.freq.FrequencyUtilities.FrequencyEnum;

/** MONTHLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Monthly extends FrequencyAbstract<Monthly>
{         
    // Constructor
    public Monthly() { super(FrequencyEnum.MONTHLY); }
    
    public Monthly(Frequency source) { super(source); }
}
