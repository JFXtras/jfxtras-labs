package jfxtras.labs.icalendar.properties.recurrence.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** HOURLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Hourly extends FrequencyAbstract<Hourly>
{
    // Constructor
    public Hourly() { super(FrequencyType.HOURLY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.HOURS)); }

    @Override
    public TemporalAdjuster adjuster() {
        // TODO Auto-generated method stub
        return null;
    }
}
