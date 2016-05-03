package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract<Yearly>
{
    // Constructors
    public Yearly() { super(FrequencyType.YEARLY); }
    
    public Yearly(Frequency source) { super(source); }
}
