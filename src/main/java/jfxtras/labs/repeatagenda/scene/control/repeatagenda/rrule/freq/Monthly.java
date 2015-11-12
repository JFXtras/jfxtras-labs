package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.temporal.TemporalAdjuster;

/** MONTHLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Monthly extends FrequencyAbstract
{  
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster monthlyAdjuster = (temporal) -> temporal.plus(getInterval(), MONTHS);
    @Override public TemporalAdjuster getAdjuster() { return monthlyAdjuster; }

    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.MONTHLY; }
        
    // Constructor
    public Monthly() { setChronoUnit(MONTHS); }    
}
