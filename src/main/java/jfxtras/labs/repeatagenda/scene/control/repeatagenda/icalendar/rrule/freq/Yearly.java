package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import static java.time.temporal.ChronoUnit.YEARS;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** YEARLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Yearly extends FrequencyAbstract<Yearly>
{
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster yearlyAdjuster = (temporal) -> temporal.plus(getInterval(), YEARS);
    @Override public TemporalAdjuster adjuster() { return yearlyAdjuster; }

    // Constructor
    public Yearly() { super(FrequencyType.YEARLY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.YEARS)); }
}
