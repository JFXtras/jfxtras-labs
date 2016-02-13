package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Ignore;
import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Weekly;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/*
 * Tests subset of recurrence set
 */
public class ICalendarDateTest extends ICalendarTestAbstract
{
    
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEventImpl e = getYearly1();
        List<Temporal> madeDates = e
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
    
    /** Tests daily stream with FREQ=YEARLY;BYDAY=SU */
    @Test
    public void yearlyStreamTest2()
    {
        VEventImpl e = getYearly2();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly3();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly4();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly5();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly6();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly7();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly8();
        List<Temporal> madeDates = e
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
        VEventImpl e = getYearly9();
        List<Temporal> madeDates = e
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
        VEventImpl e = getMonthly1();
        List<Temporal> madeDates = e
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
        VEventImpl e = getMonthly2();
        List<Temporal> madeDates = e
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
        VEventImpl e = getMonthly3();
        List<Temporal> madeDates = e
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
        VEventImpl e = getMonthly4();
        List<Temporal> madeDates = e
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
        VEventImpl e = getMonthly5();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .limit(6)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(1997, 6, 13, 10, 0)
              , LocalDateTime.of(1998, 2, 13, 10, 0)
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
        VEventImpl e = getMonthly6();
        List<Temporal> madeDates = e
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
        VEventImpl e = getWeekly1();
        List<Temporal> madeDates = e
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
        VEventImpl e = getWeekly2();
        List<Temporal> madeDates = e
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
        VEventImpl e = getWeekly3();
        List<Temporal> madeDates = e
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
    
    /** FREQ=WEEKLY;INTERVAL=2;COUNT=11;BYDAY=MO,WE,FR */
    @Test
    public void canStreamWeekly4()
    {
        VEventImpl e = getWeekly4();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
//                .peek(System.out::println)
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
              , LocalDateTime.of(2015, 12, 25, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test // tests starting on Sunday (1st day of week) with other day of the week
    public void canStreamWeekly5()
    {
        VEventImpl e = getWeekly5();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2016, 1, 3, 5, 0)
              , LocalDateTime.of(2016, 1, 6, 5, 0)
              , LocalDateTime.of(2016, 1, 10, 5, 0)
              , LocalDateTime.of(2016, 1, 13, 5, 0)
              , LocalDateTime.of(2016, 1, 17, 5, 0)
              , LocalDateTime.of(2016, 1, 20, 5, 0)
              , LocalDateTime.of(2016, 1, 24, 5, 0)
              , LocalDateTime.of(2016, 1, 27, 5, 0)
              , LocalDateTime.of(2016, 1, 31, 5, 0)
              , LocalDateTime.of(2016, 2, 3, 5, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
   
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEventImpl e = getDaily1();
        List<Temporal> madeDates = e
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
        VEventImpl e = getDaily2();
        List<Temporal> madeDates = e
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
        VEventImpl e = getDaily3();
        List<Temporal> madeDates = e
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
        VEventImpl e = getDaily4();
        List<Temporal> madeDates = e
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
        VEventImpl e = getDaily5();
        List<Temporal> madeDates = e
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
    
    @Test
    public void dailyStreamTest6()
    {
        VEventImpl e = getDaily6();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void dailyStreamTest7()
    {
        VEventImpl e = getDaily7();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void dailyStreamTestUTC()
    {
        VEventImpl e = getDailyUTC();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
//                .map(t -> ((ZonedDateTime) t).withZoneSameInstant(ZoneId.systemDefault()))
                .map(z -> VComponent.localDateTimeFromTemporal(z))
//                .peek(System.out::println)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 2, 0)
              , LocalDateTime.of(2015, 11, 11, 2, 0)
              , LocalDateTime.of(2015, 11, 13, 2, 0)
              , LocalDateTime.of(2015, 11, 15, 2, 0)
              , LocalDateTime.of(2015, 11, 17, 2, 0)
              , LocalDateTime.of(2015, 11, 19, 2, 0)
              , LocalDateTime.of(2015, 11, 21, 2, 0)
              , LocalDateTime.of(2015, 11, 23, 2, 0)
              , LocalDateTime.of(2015, 11, 25, 2, 0)
              , LocalDateTime.of(2015, 11, 27, 2, 0)
              , LocalDateTime.of(2015, 11, 29, 2, 0)
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests individual VEvent */
    @Test
    public void individualTest1()
    {
        VEventImpl e = getIndividual1();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 30)
                ));
        assertEquals(expectedDates, madeDates);
    }

    /** Tests VEvent with RRule and exception VEvent */
    @Test
    public void exceptionTest1()
    {
        VEventImpl e = getDailyWithException1();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
              , LocalDateTime.of(2015, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        e.getExDate().getTemporals().stream().forEach(System.out::println);
    }

    /** Tests VEvent with RRule and exception VEvent */
    @Test
    public void recurrenceTest1()
    {
        VEventImpl e = getRDate();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 12, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void getWeekly2ChangeRange()
    {
        VEventImpl vevent = getWeekly2();
        LocalDateTime start = LocalDateTime.of(2015, 11, 8, 0, 0);
        List<Temporal> madeDates = vevent
                .stream(start)
                .limit(2)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);

        LocalDateTime start2 = LocalDateTime.of(2015, 12, 6, 0, 0);
        List<Temporal> madeDates2 = vevent
                .stream(start2)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 12, 7, 10, 0)
              , LocalDateTime.of(2015, 12, 9, 10, 0)
              , LocalDateTime.of(2015, 12, 11, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
    }
    
    // ten years in future
    @Test
    public void getWeekly2FarFuture()
    {
        VEventImpl vevent = getWeekly2();
        
        LocalDateTime start = LocalDateTime.of(2025, 11, 10, 0, 0);
        long t1 = System.nanoTime();
        List<Temporal> madeDates = vevent
                .stream(start)
                .limit(3)
                .collect(Collectors.toList());
        long t2 = System.nanoTime();
        System.out.println("time:" + (t2-t1));
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 10, 10, 0)
              , LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        LocalDateTime start2 = LocalDateTime.of(2015, 11, 11, 0, 0);
        t1 = System.nanoTime();
        List<Temporal> madeDates2 = vevent
                .stream(start2)
                .limit(2)
                .collect(Collectors.toList());
        t2 = System.nanoTime();
        System.out.println("time:" + (t2-t1));
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
        
        LocalDateTime start3 = LocalDateTime.of(2025, 11, 12, 0, 0);
        t1 = System.nanoTime();
        List<Temporal> madeDates3 = vevent
                .stream(start3)
                .limit(3)
                .collect(Collectors.toList());
        t2 = System.nanoTime();
        System.out.println("time:" + (t2-t1));
        List<LocalDateTime> expectedDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
              , LocalDateTime.of(2025, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates3, madeDates3);

        LocalDateTime start4 = LocalDateTime.of(2025, 11, 17, 0, 0);
        t1 = System.nanoTime();
        List<Temporal> madeDates4 = vevent
                .stream(start4)
                .limit(3)
                .collect(Collectors.toList());
        t2 = System.nanoTime();
        System.out.println("time:" + (t2-t1));
        List<LocalDateTime> expectedDates4 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 24, 10, 0)
              , LocalDateTime.of(2025, 11, 26, 10, 0)
              , LocalDateTime.of(2025, 11, 28, 10, 0)
                ));
        assertEquals(expectedDates4, madeDates4);
    }

    
    // Whole day tests
    
    @Test
    public void makeDatesWholeDayDaily2()
    {
        VEventImpl e = getWholeDayDaily2();
        List<Temporal> madeDates = e               
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }

    @Test
    public void makeDatesWholeDayDaily3()
    {
        VEventImpl e = getWholeDayDaily3();
        List<Temporal> madeDates = e                
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2015, 11, 9)
              , LocalDate.of(2015, 11, 12)
              , LocalDate.of(2015, 11, 15)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 21)
              , LocalDate.of(2015, 11, 24)
                ));
        assertEquals(expectedDates, madeDates);
    }

    @Test // LocalDate
    public void canChangeToWholeDay()
    {
        VEventImpl v = getDaily1();
        v.setDateTimeStart(LocalDate.of(2015, 11, 9)); // change to whole-day
        v.setDateTimeEnd(LocalDate.of(2015, 11, 10));
        {
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDate.of(2015, 11, 9)
                  , LocalDate.of(2015, 11, 10)
                  , LocalDate.of(2015, 11, 11)
                  , LocalDate.of(2015, 11, 12)
                  , LocalDate.of(2015, 11, 13)
                  , LocalDate.of(2015, 11, 14)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        { // end dates
            List<Temporal> madeDates = v
                    .stream(v.getDateTimeEnd())
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDate> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDate.of(2015, 11, 10)
                  , LocalDate.of(2015, 11, 11)
                  , LocalDate.of(2015, 11, 12)
                  , LocalDate.of(2015, 11, 13)
                  , LocalDate.of(2015, 11, 14)
                  , LocalDate.of(2015, 11, 15)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        
        // Change back to date/time
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0)); // change to date/time
        v.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 11, 0)); // change to date/time
        { // start date/time
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 10, 10, 0)
                  , LocalDateTime.of(2015, 11, 11, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 13, 10, 0)
                  , LocalDateTime.of(2015, 11, 14, 10, 0)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        { // end date/time
            long duration = ChronoUnit.NANOS.between(v.getDateTimeStart(), v.getDateTimeEnd());
            List<Temporal> madeDates = v
                    .stream(v.getDateTimeStart())
                    .limit(6)
                    .map(t -> t.plus(duration, ChronoUnit.NANOS))
                    .collect(Collectors.toList());
            LocalDateTime seed = LocalDateTime.of(2015, 11, 9, 11, 0);
            List<LocalDateTime> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(6)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeStartStreamTest()
    {
        VEventImpl v = getDaily1();

        { // initialize stream
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }
        
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 10, 10, 0)); // change start
        { // make new stream
            List<Temporal> madeDates = v                
                    .stream(LocalDateTime.of(2015, 12, 9, 10, 0))
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 12, 9, 10, 0);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // request date beyond first cached date to test cache system
        v.stream( LocalDateTime.of(2016, 12, 25, 10, 0)).findFirst();
        
    }
    
    @Test // tests cached stream ability to reset when RRule and start changes
    public void canChangeRRuleStreamTest()
    {
        VEventImpl v = getDaily1();
        
        { // initialize stream
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(1, ChronoUnit.DAYS))
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // Change RRule
        RRule rule = new RRule();
        v.setRRule(rule);
        Frequency weekly = new Weekly().withInterval(2);;
        rule.setFrequency(weekly);
        Rule byRule = new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY);
        weekly.addByRule(byRule);

        { // check new repeatable stream
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(50)
                    .collect(Collectors.toList());
            Temporal seed = LocalDateTime.of(2015, 11, 9, 10, 00);
            List<Temporal> expectedDates = Stream
                    .iterate(seed, a -> a.plus(2, ChronoUnit.WEEKS))
                    .flatMap(d -> 
                    {
                        List<Temporal> days = new ArrayList<>();
                        days.add(d); // Mondays
                        days.add(d.plus(2, ChronoUnit.DAYS)); // Wednesdays
                        days.add(d.plus(4, ChronoUnit.DAYS)); // Fridays
                        return days.stream();
                    })
                    .limit(50)
                    .collect(Collectors.toList());
            assertEquals(expectedDates, madeDates);
        }

        // request date beyond first cached date to test cache system
        Temporal date = v.stream( LocalDateTime.of(2015, 12, 9, 10, 0)).findFirst().get();
        assertEquals(LocalDateTime.of(2015, 12, 9, 10, 0), date);
        
    }
    
    @Test // LocalDate
    public void canChangeFromWholeDay()
    {
        VEventImpl v = getWholeDayDaily2();
        v.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0)); // change to date/time
        v.setDurationInNanos(3600L * NANOS_IN_SECOND);
        { // start date/time
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 10, 0)
                  , LocalDateTime.of(2015, 11, 12, 10, 0)
                  , LocalDateTime.of(2015, 11, 15, 10, 0)
                  , LocalDateTime.of(2015, 11, 18, 10, 0)
                  , LocalDateTime.of(2015, 11, 21, 10, 0)
                  , LocalDateTime.of(2015, 11, 24, 10, 0)
                    ));
            assertEquals(expectedDates, madeDates);
        }
        { // end date/time
            long nanos = v.getDurationInNanos();
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .map(t -> t.plus(nanos, ChronoUnit.NANOS)) // calculate end
                    .limit(6)
                    .collect(Collectors.toList());
            List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                    LocalDateTime.of(2015, 11, 9, 11, 0)
                  , LocalDateTime.of(2015, 11, 12, 11, 0)
                  , LocalDateTime.of(2015, 11, 15, 11, 0)
                  , LocalDateTime.of(2015, 11, 18, 11, 0)
                  , LocalDateTime.of(2015, 11, 21, 11, 0)
                  , LocalDateTime.of(2015, 11, 24, 11, 0)
                    ));
            assertEquals(expectedDates, madeDates);
        }
    }
    
    @Test
    public void canFindLastStreamTemporal()
    {
        VEventImpl v = getWeekly3();
        v.stream(v.getDateTimeStart()).limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), v.previousStreamValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEventImpl v2 = getWholeDayDaily2();
        assertEquals(LocalDate.of(2015, 11, 24), v2.previousStreamValue(LocalDate.of(2015, 12, 31)));
    }
    
    /*
     *  Tests for multi-part recurrence sets
     *  Children have RECURRENCE-ID
     *  Branches have RELATED-TO
     */
    // TODO - ARE THESE NECESSARY NOW?

//    @Test
//    public void canStreamBranch1()
//    {
//        VEventImpl e = getBranch1();
//        List<Temporal> madeDates = e
//                .stream(e.getDateTimeStart())
//                .collect(Collectors.toList());
//        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 12, 1, 12, 0)
//              , LocalDateTime.of(2015, 12, 3, 12, 0)
//              , LocalDateTime.of(2015, 12, 5, 12, 0)
//              , LocalDateTime.of(2015, 12, 7, 12, 0)
//              , LocalDateTime.of(2015, 12, 9, 12, 0)
//              , LocalDateTime.of(2015, 12, 11, 12, 0)
//              ));
//        assertEquals(expectedDates, madeDates);
//    }
//
//    @Test
//    public void canStreamBranch2()
//    {
//        VEventImpl e = getBranch2();
//        List<Temporal> madeDates = e
//                .stream(e.getDateTimeStart())
//                .limit(6)
//                .collect(Collectors.toList());
//        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 12, 14, 6, 0)
//              , LocalDateTime.of(2015, 12, 16, 6, 0)
//              , LocalDateTime.of(2015, 12, 18, 6, 0)
//              , LocalDateTime.of(2015, 12, 20, 6, 0)
//              , LocalDateTime.of(2015, 12, 22, 6, 0)
//              , LocalDateTime.of(2015, 12, 24, 6, 0)
//              ));
//        assertEquals(expectedDates, madeDates);
//    }
    
    @Test
    public void canStreamChild1()
    {
        VEventImpl e = getChild1();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 22, 16, 0)
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    @Ignore // TODO - FIXTHIS - ONLY GET CHILD EVENTS
    public void canFindRecurrenceSet1()
    {
        List<VComponent<Appointment>> vComponents = new ArrayList<>();
        vComponents.add(getMonthly2());
//        vComponents.add(getBranch1());
        vComponents.add(getDaily6());
//        VEventImpl branch2 = getBranch2();
//        vComponents.add(branch2);
        vComponents.add(getYearly1());
        vComponents.add(getChild1());
        vComponents.add(getWeekly3());
        
//        Collection<VComponent<Appointment>> rs = VComponent.findRelatedVComponents(vComponents, branch2);
//        assertEquals(4, rs.size());
        
//        rs.iterator();
    }
}
