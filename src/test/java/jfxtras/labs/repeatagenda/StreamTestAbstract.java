package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rules.byrules.ByMonthDay;

public abstract class StreamTestAbstract {

    protected static Stream<LocalDateTime> STREAM1 = new ByMonthDay(LocalDateTime.of(2015, 11, 9, 10, 0), 1).stream();
    protected static Stream<LocalDateTime> STREAM2 = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0), 1).stream();
    protected static final Comparator<LocalDateTime> COMPARATOR = (a1, a2) -> a1.compareTo(a2);
}
