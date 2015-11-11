package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;


public class Daily extends FrequencyAbstract
{    
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster dailyAdjuster = (temporal) -> temporal.plus(Period.ofDays(getInterval()));

    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.DAILY; }

    // Constructor
    public Daily(LocalDateTime startLocalDateTime)
    {
        super(startLocalDateTime);
    }

    @Override
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream = Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(dailyAdjuster); });
//        if (! getByRules().isEmpty())
//        {
            System.out.println("processing rules");
            for (ByRule rule : getByRules())
            {
                stream = rule.stream(stream, startDateTime);
            }
//        }
        // Limit by COUNT
        System.out.println("getCount() " + getCount());
        Stream<LocalDateTime> streamLimited = (getCount() == 0) ? stream : stream.limit(getCount());
        return streamLimited;
//        return Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(new NextAppointment()); });
    }    
    
}
