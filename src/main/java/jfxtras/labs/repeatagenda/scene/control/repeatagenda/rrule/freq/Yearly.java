package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.TemporalAdjuster;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract {

    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster yearlyAdjuster = (temporal) -> temporal.plus(getInterval(), YEARS);
    @Override public TemporalAdjuster getAdjuster() { return yearlyAdjuster; }

    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.YEARLY; }

    // Constructor
    public Yearly() { setChronoUnit(YEARS); }
}
