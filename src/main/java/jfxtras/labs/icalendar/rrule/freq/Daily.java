package jfxtras.labs.icalendar.rrule.freq;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract<Daily>
{    
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster dailyAdjuster = (temporal) -> temporal.plus(getInterval(), DAYS);
    @Override public TemporalAdjuster adjuster() { return dailyAdjuster; }

    // Constructor
    public Daily() { super(FrequencyType.DAILY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.DAYS)); }
}
