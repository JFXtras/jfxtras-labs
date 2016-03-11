package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import static java.time.temporal.ChronoUnit.MONTHS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** MONTHLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Monthly extends FrequencyAbstract<Monthly>
{  
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster monthlyAdjuster = (temporal) -> temporal.plus(getInterval(), MONTHS);
    @Override public TemporalAdjuster adjuster() { return monthlyAdjuster; }
        
    // Constructor
    public Monthly() { super(FrequencyType.MONTHLY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.MONTHS)); }
}
