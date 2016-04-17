package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.FrequencyUtilities.FrequencyEnum;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract<Yearly>
{
    // Constructors
    public Yearly() { super(FrequencyEnum.YEARLY); }
    
    public Yearly(Frequency source) { super(source); }
}
