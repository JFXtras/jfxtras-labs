package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule.ByRule;

/** Repeat rule for frequency of Monthly */
public class Monthly extends FrequencyAbstract {   
        
    @Override
    public FrequencyEnum frequencyEnum() { return FrequencyEnum.MONTHLY; }
        
    @Override
    public Stream<LocalDateTime> stream()
    { // infinite stream of valid dates
        if (getByRules() == null)
        { // if no rules, return stream repeating monthly on day of month matching startLocalDateTime
            int dayOfMonth = getStartLocalDateTime().toLocalDate().getDayOfMonth();
            ByRule defaultRule = new ByMonthDay(this, dayOfMonth);
            return defaultRule.stream();
//            return Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });
        }
        // TODO - apply byRules one at a time
        return null;
    }

    // Constructor
    public Monthly(LocalDateTime startLocalDateTime)
    {
        super(startLocalDateTime);
    }

    /** adjusts temporal parameter to become date/time of next event */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            for (int i=0; i<getInterval(); i++)
            { // loops through interval
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
