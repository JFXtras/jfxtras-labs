package jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;

public class ByMinute extends ByRuleAbstract
{
    public ByMinute(Frequency frequency)
    {
        super(frequency);
        setSortOrder(60);
        throw new RuntimeException("not implemented");
    }
    
    @Override
    public Stream<LocalDateTime> stream(Stream<LocalDateTime> inStream,
            LocalDateTime startDateTime) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void copyTo(Rule destination) {
        // TODO Auto-generated method stub
        
    }


}
