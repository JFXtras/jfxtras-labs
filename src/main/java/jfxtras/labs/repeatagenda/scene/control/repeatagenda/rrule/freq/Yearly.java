package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Yearly extends FrequencyAbstract {

//    private ByRule defaultRule = new ByMonthDay(this);
    
    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.YEARLY; }
    
    Yearly(LocalDateTime startLocalDateTime) {
        super(startLocalDateTime);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Stream<LocalDateTime> stream(LocalDateTime start) {
        // TODO Auto-generated method stub
        return null;
    }

}
