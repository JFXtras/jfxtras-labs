package jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.FrequencyUtilities.FrequencyEnum;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract<Daily>
{    
    // Constructor
    public Daily() { super(FrequencyEnum.DAILY); }
    
    public Daily(Frequency source) { super(source); }
}
