package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.TemporalAdjuster;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;

/** DAILY frequency rule as defined by RFC 5545 iCalendar 3.3.10 p39 */
public class Daily extends FrequencyAbstract
{    
    // adjusts temporal parameter to become date/time of next event
    private final TemporalAdjuster dailyAdjuster = (temporal) -> temporal.plus(Period.ofDays(getInterval()));

    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.DAILY; }

    // TODO - Maybe I can put this method in the abstract as default and put a getter on the temporal adjuster.  This
    // depends on if the other Frequency rules use a temporal adjuster.  I don't know about weekly.
    @Override
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> stream = Stream.iterate(startDateTime, (a) -> { return a.with(dailyAdjuster); });
        for (ByRule rule : getByRules())
        {
            stream = rule.stream(stream, startDateTime);
        }
        return stream;
    }    
    
}
