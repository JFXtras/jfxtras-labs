package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

/** SECONDLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Secondly extends FrequencyAbstract<Secondly>
{
    // Constructor
    public Secondly() { super(FrequencyType.SECONDLY); }
    
    public Secondly(Frequency source) { super(source); }
}
