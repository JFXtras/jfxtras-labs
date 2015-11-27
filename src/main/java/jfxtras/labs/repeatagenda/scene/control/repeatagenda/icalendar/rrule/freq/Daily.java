package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.temporal.TemporalAdjuster;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract
{    
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster dailyAdjuster = (temporal) -> temporal.plus(getInterval(), DAYS);
    @Override public TemporalAdjuster getAdjuster() { return dailyAdjuster; }

    // Constructor
    public Daily() { setChronoUnit(DAYS); }
    
    @Override
    public String toString()
    {
        return super.toString() + Frequencies.DAILY;
    }
}
