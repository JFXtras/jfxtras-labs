package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.byrules;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ByMonthDay implements ByRule
{
    
    /** Start date/time of repeat rule */
    private LocalDateTime startLocalDateTime;
    private int interval;
    
    public ByMonthDay(LocalDateTime startLocalDateTime, int interval)
    {
        this.startLocalDateTime = startLocalDateTime;
        this.interval = interval;
    }

    final private BooleanProperty repeatDayOfMonth = new SimpleBooleanProperty(this, "dayOfMonth", true); // default option
    protected Boolean isRepeatDayOfMonth() { return repeatDayOfMonth.getValue(); }
    public BooleanProperty repeatDayOfMonthProperty() { return repeatDayOfMonth; }
    private void setRepeatDayOfMonth(Boolean repeatDayOfMonth) { this.repeatDayOfMonth.set(repeatDayOfMonth); }

    @Override
    public Stream<LocalDateTime> stream()
    { // infinite stream of valid dates
        return Stream.iterate(startLocalDateTime, (a) -> { return a.with(new NextAppointment()); });
    }

    /**
     * Adjust date to become next date based on the Repeat rule.  Needs a input temporal on a valid date.
     * 
     * @return
     */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            for (int i=0; i<interval; i++)
            { // loop that counts number of valid dates for total time interval (repeatFrequency)
                temporal = temporal.with(new TemporalAdjuster()
                { // anonymous inner class that finds next valid date
                    @Override
                    public Temporal adjustInto(Temporal temporal)
                    {
                        return temporal.plus(Period.ofMonths(1));
                    }
                });
            }; // end of looping anonymous inner class
        return temporal;
        }
    }
}
