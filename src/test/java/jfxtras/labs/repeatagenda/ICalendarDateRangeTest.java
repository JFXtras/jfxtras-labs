package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;

public class ICalendarDateRangeTest extends ICalendarTestAbstract
{
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEventImpl vevent = getDaily1();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<LocalDateTime> madeDates = vevent
                .stream(vevent.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=DAILY - too early, empty stream */
    @Test
    public void dailyStreamTest2()
    {
        VEventImpl vevent = getDaily1();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2014, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2014, 11, 22, 0, 0));
        List<LocalDateTime> madeDates = vevent
                .stream(vevent.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>();
        assertEquals(expectedDates, madeDates);
    }

    /** FREQ=DAILY;INVERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14 - too late, beyond range */
    @Test
    public void dailyStreamTest3()
    {
        VEventImpl vevent = getDaily3();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2016, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2016, 11, 22, 0, 0));
        List<LocalDateTime> madeDates = vevent
                .stream(vevent.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>();
        assertEquals(expectedDates, madeDates);
    }

}
