package jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency;

import jfxtras.labs.icalendar.properties.component.recurrence.rrule.frequency.FrequencyUtilities.FrequencyEnum;

/** WEEKLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Weekly extends FrequencyAbstract<Weekly>
{
    // Constructor
    public Weekly() { super(FrequencyEnum.WEEKLY); }
    
    public Weekly(Frequency source) { super(source); }
}
