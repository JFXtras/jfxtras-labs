package jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.rrule.freq;

import static java.time.temporal.ChronoUnit.WEEKS;

import java.time.temporal.TemporalAdjuster;

/** WEEKLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Weekly extends FrequencyAbstract
{
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster weeklyAdjuster = (temporal) -> temporal.plus(getInterval(), WEEKS);
    @Override public TemporalAdjuster getAdjuster() { return weeklyAdjuster; }
    
    // Constructor
    public Weekly() { setChronoUnit(WEEKS); }
}
