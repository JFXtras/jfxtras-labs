package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Stream;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byrule.ByMonthDay;

public abstract class StreamTestAbstract {

    protected static Monthly MONTHLY = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0));
    protected static Stream<LocalDateTime> STREAM1 = 
            new ByMonthDay(MONTHLY
                    , new int[]{9, 15} ) // 9th and 15th day of the month
            .stream();
    
    protected static Stream<LocalDateTime> STREAM2 = 
            new ByMonthDay(MONTHLY
                    , 9 ) // 9nd day of the month
            .stream();
//    protected static Stream<LocalDateTime> STREAM2 = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0), 1).stream();
    protected static final Comparator<LocalDateTime> COMPARATOR = (a1, a2) -> a1.compareTo(a2);
}
