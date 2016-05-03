package jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.frequency;

/** MINUTELY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Minutely extends FrequencyAbstract<Minutely>
{
    // Constructor
    public Minutely() { super(FrequencyType.MINUTELY); }

    public Minutely(Frequency source) { super(source); }
}
