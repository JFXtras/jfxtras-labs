package jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.stream.Stream;

public class Weekly extends FrequencyAbstract
{
    @Override public FrequencyEnum frequencyEnum() { return FrequencyEnum.WEEKLY; }

    Weekly(LocalDateTime startLocalDateTime) {
        super(startLocalDateTime);
        // TODO Auto-generated constructor stub
    }

    private DayOfWeek weekStart;

    @Override
    public Stream<LocalDateTime> stream(LocalDateTime start) {
        // TODO Auto-generated method stub
        return null;
    }
}
