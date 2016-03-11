package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import static java.time.temporal.ChronoUnit.WEEKS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** WEEKLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Weekly extends FrequencyAbstract<Weekly>
{
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster weeklyAdjuster = (temporal) -> temporal.plus(getInterval(), WEEKS);
    @Override public TemporalAdjuster adjuster() { return weeklyAdjuster; }
    
    // Constructor
    public Weekly() { super(FrequencyType.WEEKLY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.WEEKS)); }
}
