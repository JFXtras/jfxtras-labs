package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Weekly extends FrequencyAbstract {

    Weekly(LocalDateTime startLocalDateTime) {
        super(startLocalDateTime);
        // TODO Auto-generated constructor stub
    }

    private DayOfWeek weekStart;

    @Override
    public Stream<LocalDateTime> stream() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FrequencyEnum frequencyEnum() {
        // TODO Auto-generated method stub
        return null;
    }
    

}
