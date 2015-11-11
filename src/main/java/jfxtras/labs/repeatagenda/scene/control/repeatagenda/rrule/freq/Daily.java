package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;


public class Daily extends FrequencyAbstract
{    
    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.DAILY; }

    // Constructor
    public Daily(LocalDateTime startLocalDateTime)
    {
        super(startLocalDateTime);
    }

    @Override
    public Stream<LocalDateTime> stream()
    {
        Stream<LocalDateTime> s;
        if (getByRules().isEmpty())
        { // if no rules, return stream repeating monthly on day of month matching startLocalDateTime
            s = Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });
        } else
        {
            System.out.println("processing rules");
            s = Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });
            for (ByRule r : getByRules())
            {
                s = r.stream(s);
            }
//            return s;
        }
        System.out.println("getCount() " + getCount());
        Stream<LocalDateTime> s2 = (getCount() == 0) ? s : s.limit(getCount());
        return s2;
//        return Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });
    }    
    
    /** adjusts temporal parameter to become date/time of next event */
    private class NextAppointment implements TemporalAdjuster
    {
        @Override
        public Temporal adjustInto(Temporal temporal)
        {
            return temporal.plus(Period.ofDays(getInterval()));
        }
    }

}
