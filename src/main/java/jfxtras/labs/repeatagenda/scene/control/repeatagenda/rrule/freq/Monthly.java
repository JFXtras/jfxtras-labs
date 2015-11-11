package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;

/** Repeat rule for frequency of Monthly */
public class Monthly extends FrequencyAbstract {   
        
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster monthlyAdjuster = (temporal) -> temporal.plus(Period.ofMonths(getInterval()));

//    private ByRule defaultRule = new ByMonthDay(this);
    
    // Constructor
    public Monthly(LocalDateTime startLocalDateTime)
    {
        super(startLocalDateTime);
    }
    
    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.MONTHLY; }
        
    @Override
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream = Stream.iterate(getStartLocalDateTime(), (a) -> { return a.with(monthlyAdjuster); });
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
    }
    
}
