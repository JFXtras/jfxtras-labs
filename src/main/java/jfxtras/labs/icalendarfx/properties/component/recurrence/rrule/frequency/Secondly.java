package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency.FrequencyUtilities.FrequencyEnum;

/** SECONDLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Secondly extends FrequencyAbstract<Secondly>
{
    // Constructor
    public Secondly() { super(FrequencyEnum.SECONDLY); }
    
    public Secondly(Frequency source) { super(source); }
}
