package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.TemporalAdjuster;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract
{
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster yearlyAdjuster = (temporal) -> temporal.plus(getInterval(), YEARS);
    @Override public TemporalAdjuster getAdjuster() { return yearlyAdjuster; }

    // Constructor
    public Yearly() { setChronoUnit(YEARS); }

    @Override
    public String toString()
    {
        return super.toString() + Frequencies.YEARLY;
    }
}
