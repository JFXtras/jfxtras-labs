package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.Comparator;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.byxxx.ByRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.freq.Monthly;

public abstract class ICalendarRepeatTestAbstract
{
//    protected static Monthly MONTHLY = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0));
    
    /** FREQ=MONTHLY, Basic monthly stream, repeats 9th day of every month */
    protected static Frequency getMonthlyStream()
    {
        Monthly monthly = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0));
        return monthly;
    }

    /** FREQ=MONTHLY;BYMONTHDAY=-2, Monthly stream, negative day of month */
    protected static Frequency getMonthlyStream2()
    {
        Monthly monthly = new Monthly(LocalDateTime.of(2015, 11, 29, 10, 0));
        ByRule rule2 = new ByMonthDay(monthly, -2); // repeats 2nd to last day
        monthly.addByRule(rule2);
        return monthly;
    }

    /** FREQ=DAILY, Basic daily stream */
    protected static Frequency getDailyStream()
    {
        Daily daily = new Daily(LocalDateTime.of(2015, 11, 9, 10, 0));
        return daily;
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=6 */
    protected static Frequency getDailyStream2()
    {
        Daily daily = new Daily(LocalDateTime.of(2015, 11, 9, 10, 0));
        daily.setInterval(3);
        daily.setCount(6);
        return daily;
    }
    
//    protected static Stream<LocalDateTime> STREAM1 = 
//            new ByMonthDay(MONTHLY
//                    , new int[]{9, 15} ) // 9th and 15th day of the month
//            .stream();
    
//    protected static Stream<LocalDateTime> STREAM2 = 
//            new ByMonthDay(MONTHLY
//                    , 9 ) // 9nd day of the month
//            .stream();
//    protected static Stream<LocalDateTime> STREAM2 = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0), 1).stream();
    protected static final Comparator<LocalDateTime> COMPARATOR = (a1, a2) -> a1.compareTo(a2);
}
