package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.VEvent;

public class ICalendarFreqStreamTest extends ICalendarRepeatTestAbstract
{
    
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEvent e = getYearlyStream1();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2016, 11, 9, 10, 0)
              , LocalDateTime.of(2017, 11, 9, 10, 0)
              , LocalDateTime.of(2018, 11, 9, 10, 0)
              , LocalDateTime.of(2019, 11, 9, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest2()
    {
        VEvent e = getYearlyStream2();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 4, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;BYDAY=TH;BYMONTH=6,7,8 */
    @Test
    public void yearlyStreamTest3()
    {
        VEvent e = getYearlyStream3();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(20)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 5, 9, 0)
              , LocalDateTime.of(1997, 6, 12, 9, 0)
              , LocalDateTime.of(1997, 6, 19, 9, 0)
              , LocalDateTime.of(1997, 6, 26, 9, 0)
              , LocalDateTime.of(1997, 7, 3, 9, 0)
              , LocalDateTime.of(1997, 7, 10, 9, 0)
              , LocalDateTime.of(1997, 7, 17, 9, 0)
              , LocalDateTime.of(1997, 7, 24, 9, 0)
              , LocalDateTime.of(1997, 7, 31, 9, 0)
              , LocalDateTime.of(1997, 8, 7, 9, 0)
              , LocalDateTime.of(1997, 8, 14, 9, 0)
              , LocalDateTime.of(1997, 8, 21, 9, 0)
              , LocalDateTime.of(1997, 8, 28, 9, 0)
              , LocalDateTime.of(1998, 6, 4, 9, 0)
              , LocalDateTime.of(1998, 6, 11, 9, 0)
              , LocalDateTime.of(1998, 6, 18, 9, 0)
              , LocalDateTime.of(1998, 6, 25, 9, 0)
              , LocalDateTime.of(1998, 7, 2, 9, 0)
              , LocalDateTime.of(1998, 7, 9, 9, 0)
              , LocalDateTime.of(1998, 7, 16, 9, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;BYMONTH=1,2 */
    @Test
    public void yearlyStreamTest4()
    {
        VEvent e = getYearlyStream4();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 1, 6, 10, 0)
              , LocalDateTime.of(2015, 2, 6, 10, 0)
              , LocalDateTime.of(2016, 1, 6, 10, 0)
              , LocalDateTime.of(2016, 2, 6, 10, 0)
              , LocalDateTime.of(2017, 1, 6, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 */
    @Test
    public void yearlyStreamTest5()
    {
        VEvent e = getYearlyStream5();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2016, 11, 10, 0, 0)
              , LocalDateTime.of(2017, 11, 10, 0, 0)
              , LocalDateTime.of(2018, 11, 10, 0, 0)
              , LocalDateTime.of(2019, 11, 10, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;INTERVAL=4;BYMONTH=11;BYDAY=TU;BYMONTHDAY=2,3,4,5,6,7,8
     * (U.S. Presidential Election day) */
    @Test
    public void yearlyStreamTest6()
    {
        VEvent e = getYearlyStream6();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1996, 11, 5, 0, 0)
              , LocalDateTime.of(2000, 11, 7, 0, 0)
              , LocalDateTime.of(2004, 11, 2, 0, 0)
              , LocalDateTime.of(2008, 11, 4, 0, 0)
              , LocalDateTime.of(2012, 11, 6, 0, 0)
              , LocalDateTime.of(2016, 11, 8, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;BYDAY=20MO */
    @Test
    public void yearlyStreamTest7()
    {
        VEvent e = getYearlyStream7();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 19, 10, 0)
              , LocalDateTime.of(1998, 5, 18, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=YEARLY;BYWEEKNO=20;BYDAY=MO */
    @Test
    public void yearlyStreamTest8()
    {
//        Locale oldLocale = Locale.getDefault();
//        Locale.setDefault(Locale.FRANCE); // has Monday as first day of week system.  US is Sunday which causes an error.
        VEvent e = getYearlyStream8();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 5, 12, 10, 0)
              , LocalDateTime.of(1998, 5, 11, 10, 0)
              , LocalDateTime.of(1999, 5, 17, 10, 0)
              , LocalDateTime.of(2000, 5, 15, 10, 0)
              , LocalDateTime.of(2001, 5, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
//        Locale.setDefault(oldLocale);
    }
    
    /** FREQ=YEARLY;BYMONTH=11;BYMONTHDAY=10 - start before first valid date */
    @Test
    public void yearlyStreamTest9()
    {
        VEvent e = getYearlyStream9();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 0, 0)
              , LocalDateTime.of(2016, 11, 10, 0, 0)
              , LocalDateTime.of(2017, 11, 10, 0, 0)
              , LocalDateTime.of(2018, 11, 10, 0, 0)
              , LocalDateTime.of(2019, 11, 10, 0, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests daily stream with FREQ=MONTHLY */
    @Test
    public void monthlyStreamTest()
    {
        VEvent e = getMonthlyStream1();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
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
        VEvent e = getMonthlyStream2();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 29, 10, 0)
              , LocalDateTime.of(2015, 12, 30, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 28, 10, 0)
              , LocalDateTime.of(2016, 3, 30, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=MONTHLY;BYDAY=TU,WE,FR */
    @Test
    public void monthlyStreamTest3()
    {
        VEvent e = getMonthlyStream3();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests daily stream with FREQ=MONTHLY;BYDAY=-1SA */
    @Test
    public void monthlyStreamTest4()
    {
        VEvent e = getMonthlyStream4();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 28, 10, 0)
              , LocalDateTime.of(2015, 12, 26, 10, 0)
              , LocalDateTime.of(2016, 1, 30, 10, 0)
              , LocalDateTime.of(2016, 2, 27, 10, 0)
              , LocalDateTime.of(2016, 3, 26, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** FREQ=MONTHLY;BYDAY=FR;BYMONTHDAY=13 Every Friday the 13th, forever: */
    @Test
    public void monthlyStreamTest5()
    {
        VEvent e = getMonthlyStream5();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1998, 2, 13, 10, 0)
              , LocalDateTime.of(1998, 3, 13, 10, 0)
              , LocalDateTime.of(1998, 11, 13, 10, 0)
              , LocalDateTime.of(1999, 8, 13, 10, 0)
              , LocalDateTime.of(2000, 10, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests daily stream with FREQ=MONTHLY;BYDAY=TU,WE,FR - start before first valid date */
    @Test
    public void monthlyStreamTest6()
    {
        VEvent e = getMonthlyStream6();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(13)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 3, 10, 0)
              , LocalDateTime.of(2015, 11, 4, 10, 0)
              , LocalDateTime.of(2015, 11, 6, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 1, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=WEEKLY */
    @Test
    public void weeklyStreamTest1()
    {
        VEvent e = getWeeklyStream1();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 30, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest2()
    {
        VEvent e = getWeeklyStream2();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 21, 10, 0)
              , LocalDateTime.of(2015, 12, 23, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** FREQ=WEEKLY;BYDAY=MO,WE,FR */
    @Test
    public void weeklyStreamTest3()
    {
        VEvent e = getWeeklyStream3();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
   
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEvent e = getDailyStream1();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(5)
                .collect(Collectors.toList());
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
        VEvent e = getDailyStream2();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
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
        VEvent e = getDailyStream3();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
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
        VEvent e = getDailyStream4();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(6)
                .collect(Collectors.toList());
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
    
    /** Tests daily stream with FREQ=DAILY;INVERVAL=2;BYDAY=FR*/
    @Test
    public void dailyStreamTest5()
    {
        VEvent e = getDailyStream5();
        List<LocalDateTime> madeDates = e.getRRule()
                .stream(e.getDateTimeStart())
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
              , LocalDateTime.of(2015, 12, 25, 10, 0)
              , LocalDateTime.of(2016, 1, 8, 10, 0)
              , LocalDateTime.of(2016, 1, 22, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
}
