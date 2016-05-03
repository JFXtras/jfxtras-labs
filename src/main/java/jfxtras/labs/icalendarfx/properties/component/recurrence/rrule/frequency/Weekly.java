package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

/** WEEKLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Weekly extends FrequencyAbstract<Weekly>
{
    // Constructor
    public Weekly() { super(FrequencyType.WEEKLY); }
    
    public Weekly(Frequency source) { super(source); }
}
