package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;

import javafx.beans.property.SimpleObjectProperty;

/** SECONDLY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Secondly extends FrequencyAbstract<Secondly>
{
    // Constructor
    public Secondly() { super(FrequencyType.SECONDLY, new SimpleObjectProperty<ChronoUnit>(ChronoUnit.SECONDS)); }

    @Override
    public TemporalAdjuster adjuster() {
        // TODO Auto-generated method stub
        return null;
    }

}
