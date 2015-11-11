package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.rrule.RRule;

public class ICalendarFreqStreamTest extends ICalendarRepeatTestAbstract {

    /** Tests daily stream with FREQ=MONTHLY */
    @Test
    public void monthlyStreamTest()
    {
        RRule f = getMonthlyStream();
        List<LocalDateTime> madeDates = f.stream().limit(5).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 1, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 3, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=MONTHLY;BYMONTHDAY=-2 */
    @Test
    public void monthlyStreamTest2()
    {
        RRule f = getMonthlyStream2();
        List<LocalDateTime> madeDates = f.stream().limit(5).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 29, 10, 0)
              , LocalDateTime.of(2015, 12, 30, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 28, 10, 0)
              , LocalDateTime.of(2016, 3, 30, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest()
    {
        RRule f = getDailyStream();
        List<LocalDateTime> madeDates = f.stream().limit(5).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=DAILY;INVERVAL=3;COUNT=6 */
    @Test
    public void dailyStreamTest2()
    {
        RRule f = getDailyStream2();
        List<LocalDateTime> madeDates = f.stream().collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9,10,11,12,13,14 */
    @Test
    public void dailyStreamTest3()
    {
        RRule f = getDailyStream3();
        List<LocalDateTime> madeDates = f.stream().limit(10).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 12, 10, 0)
              , LocalDateTime.of(2016, 1, 11, 10, 0)
              , LocalDateTime.of(2016, 1, 14, 10, 0)
              , LocalDateTime.of(2016, 2, 10, 10, 0)
              , LocalDateTime.of(2016, 2, 13, 10, 0)
              , LocalDateTime.of(2016, 3, 11, 10, 0)
              , LocalDateTime.of(2016, 3, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests daily stream with FREQ=DAILY;INVERVAL=2;BYMONTHDAY=9 */
    @Test
    public void dailyStreamTest4()
    {
        RRule f = getDailyStream4();
        List<LocalDateTime> madeDates = f.stream().limit(6).collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2016, 2, 9, 10, 0)
              , LocalDateTime.of(2016, 4, 9, 10, 0)
              , LocalDateTime.of(2016, 5, 9, 10, 0)
              , LocalDateTime.of(2016, 8, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
}
