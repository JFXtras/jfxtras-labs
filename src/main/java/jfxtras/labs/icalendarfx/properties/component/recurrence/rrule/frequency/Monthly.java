package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;

/** MONTHLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Monthly extends FrequencyAbstract<Monthly>
{         
    // Constructor
    public Monthly() { super(FrequencyType.MONTHLY); }
    
    public Monthly(Frequency source) { super(source); }
}
