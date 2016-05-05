package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract<Daily>
{    
    // Constructor
    public Daily() { super(FrequencyType.DAILY); }
    
    public Daily(Frequency source) { super(source); }
}
