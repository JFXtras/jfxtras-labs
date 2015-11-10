package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.Monthly;

public class StreamBasicTest extends StreamTestAbstract {

    @Test
    public void basicStreamTest()
    {
//        ByMonthDay m = new ByMonthDay(LocalDateTime.of(2015, 11, 9, 10, 0)
//                , monthly.stream()
//                , monthly.frequencyEnum()
//                , new int[]{1, 5} ); // 1st and 5th day of the month
//        .stream();
        Monthly monthly = new Monthly(LocalDateTime.of(2015, 11, 9, 10, 0));
        monthly.setInterval(1);
        monthly.stream().limit(10).forEach(System.out::println);
    }
}
