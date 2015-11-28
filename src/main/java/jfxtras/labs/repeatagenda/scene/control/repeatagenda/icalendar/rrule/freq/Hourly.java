package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

/** HOURLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Hourly extends FrequencyAbstract
{
    // Constructor
    public Hourly() { super(FrequencyType.HOURLY, ChronoUnit.HOURS); }

    @Override
    public TemporalAdjuster getAdjuster() {
        // TODO Auto-generated method stub
        return null;
    }
}
