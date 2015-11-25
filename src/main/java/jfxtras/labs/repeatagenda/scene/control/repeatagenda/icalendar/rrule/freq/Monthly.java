package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.temporal.TemporalAdjuster;

/** MONTHLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Monthly extends FrequencyAbstract
{  
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster monthlyAdjuster = (temporal) -> temporal.plus(getInterval(), MONTHS);
    @Override public TemporalAdjuster getAdjuster() { return monthlyAdjuster; }
        
    // Constructor
    public Monthly() { setChronoUnit(MONTHS); }
    
    @Override
    public String toString() { return "FREQ=MONTHLY"; }
}
