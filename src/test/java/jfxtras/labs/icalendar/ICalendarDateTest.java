package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.labs.icalendar.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.RRule;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.rrule.byxxx.Rule;
import jfxtras.labs.icalendar.rrule.freq.Frequency;
import jfxtras.labs.icalendar.rrule.freq.Weekly;

/*
 * Tests subset of recurrence set
 */
public class ICalendarDateTest extends ICalendarTestAbstract
{
    
    /** Tests daily stream with FREQ=YEARLY */
    @Test
    public void yearlyStreamTest1()
    {
        VEventMock e = getYearly1();
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
        VEventMock e = getYearly2();
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
        VEventMock e = getYearly3();
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
        VEventMock e = getYearly4();
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
        VEventMock e = getYearly5();
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
        VEventMock e = getYearly6();
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
        VEventMock e = getYearly7();
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
        VEventMock e = getYearly8();
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
    
    /** Tests daily stream with FREQ=MONTHLY */
    @Test
    public void monthlyStreamTest()
    {
        VEventMock e = getMonthly1();
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
        VEventMock e = getMonthly2();
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
        VEventMock e = getMonthly3();
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
        VEventMock e = getMonthly4();
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
        VEventMock e = getMonthly5();
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
        VEventMock e = getMonthly6();
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
        VEventMock e = getWeekly1();
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
        VEventMock e = getWeekly2();
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
        VEventMock e = getWeekly3();
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
        VEventMock e = getWeekly4();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
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
        VEventMock e = getWeekly5();
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
    
    @Test
    public void canStreamWeeklyZoned()
    {
        VEventMock e = getWeeklyZoned();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
        List<ZonedDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 20, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 23, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 25, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 27, 10, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 30, 10, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
    }
   
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyStreamTest1()
    {
        VEventMock e = getDaily1();
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
        VEventMock e = getDaily2();
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
        VEventMock e = getDaily3();
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
        VEventMock e = getDaily4();
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
        VEventMock e = getDaily5();
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
        VEventMock e = getDaily6();
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
        VEventMock e = getDaily7();
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
    public void dailyStreamTestJapanZone()
    {
        VEventMock e = getDailyJapanZone();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 10, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 12, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 14, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 15, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 16, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 17, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 8, 0), ZoneId.of("Japan"))
              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 8, 0), ZoneId.of("Japan"))
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void dailyStreamTestUTC()
    {
        VEventMock e = getDailyUTC();
        List<Temporal> madeDates = e
                .stream(e.getDateTimeStart())
                .map(z -> DateTimeType.DATE_WITH_LOCAL_TIME.from(z))
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
              , LocalDateTime.of(2015, 12, 1, 2, 0)
              ));
        assertEquals(expectedDates, madeDates);
    }
    
    /** Tests individual VEvent */
    @Test
    public void individualTest1()
    {
        VEventMock e = getIndividual1();
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
        VEventMock e = getDailyWithException1();
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
    }

    /** Tests VEvent with RDATE VEvent */
    @Test
    public void canStreamRDate()
    {
        VEventMock e = getRDate();
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
        VEventMock vevent = getWeekly2();
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
    
    // Google test
    @Test
    public void canStreamGoogleWithExDates()
    {
        VEventMock vevent = getGoogleWithExDates();
        Temporal start = ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles"));
        List<Temporal> madeDates = vevent
                .stream(start)
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 11, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 12, 30), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 12, 30), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    @Test
    public void canChangeGoogleWithExDatesToWholeDay()
    {
        VEventMock vevent = getGoogleWithExDates();
        vevent.setDateTimeStart(LocalDate.of(2016, 2, 7));
                
        List<Temporal> exDates = vevent.getExDate().getTemporals()
                .stream()
                .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR)
                .collect(Collectors.toList());
        List<Temporal> expectedExDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 2, 9)
              , LocalDate.of(2016, 2, 10)
              , LocalDate.of(2016, 2, 12)
                ));
        assertEquals(expectedExDates, exDates);
        
        Temporal start = LocalDate.of(2016, 2, 7);
        List<Temporal> madeDates = vevent
                .stream(start)
                .limit(5)
                .collect(Collectors.toList());
        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDate.of(2016, 2, 7)
              , LocalDate.of(2016, 2, 8)
              , LocalDate.of(2016, 2, 11)
              , LocalDate.of(2016, 2, 13)
              , LocalDate.of(2016, 2, 14)
                ));
        assertEquals(expectedDates, madeDates);
    }
    
    // ten years in future
    @Test
    public void getWeekly2FarFuture()
    {
        VEventMock vevent = getWeekly2();
        
        LocalDateTime start = LocalDateTime.of(2025, 11, 10, 0, 0);
        List<Temporal> madeDates = vevent
                .stream(start)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 10, 10, 0)
              , LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
        
        LocalDateTime start2 = LocalDateTime.of(2015, 11, 11, 0, 0);
        List<Temporal> madeDates2 = vevent
                .stream(start2)
                .limit(2)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates2 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
                ));
        assertEquals(expectedDates2, madeDates2);
        
        LocalDateTime start3 = LocalDateTime.of(2025, 11, 12, 0, 0);
        List<Temporal> madeDates3 = vevent
                .stream(start3)
                .limit(3)
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates3 = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2025, 11, 12, 10, 0)
              , LocalDateTime.of(2025, 11, 14, 10, 0)
              , LocalDateTime.of(2025, 11, 24, 10, 0)
                ));
        assertEquals(expectedDates3, madeDates3);

        LocalDateTime start4 = LocalDateTime.of(2025, 11, 17, 0, 0);
        List<Temporal> madeDates4 = vevent
                .stream(start4)
                .limit(3)
                .collect(Collectors.toList());
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
        VEventMock e = getWholeDayDaily2();
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
        VEventMock e = getWholeDayDaily3();
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
        VEventMock v = getDaily1();
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
        VEventMock v = getDaily1();

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
        VEventMock v = getDaily1();
        
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
        VEventMock v = getWholeDayDaily2()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
                .withDuration(Duration.ofMinutes(60));
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
            TemporalAmount duration = v.getDuration();
            List<Temporal> madeDates = v                
                    .stream(v.getDateTimeStart())
                    .map(t -> t.plus(duration)) // calculate end
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
        VEventMock v = getWeekly3();
        v.stream(v.getDateTimeStart()).limit(100).collect(Collectors.toList()); // set cache
        assertEquals(LocalDateTime.of(2016, 1, 20, 10, 0), v.previousStreamValue(LocalDateTime.of(2016, 1, 21, 10, 0)));

        VEventMock v2 = getWholeDayDaily2();
        assertEquals(LocalDate.of(2015, 11, 24), v2.previousStreamValue(LocalDate.of(2015, 12, 31)));
    }
    
    /*
     *  Tests for multi-part recurrence sets
     *  Children have RECURRENCE-ID
     */
    @Test
    public void canMakeRecurrenceSet1()
    {
        List<VEventMock> vComponents = getDailyWithRecurrence();
        VEventMock parent = vComponents.get(0);
        VEventMock recurrence = vComponents.get(1);
        List<Temporal> madeDates = new ArrayList<>();
        List<Temporal> madeDatesParent = parent
                .stream(parent.getDateTimeStart())
                .collect(Collectors.toList());
        List<Temporal> madeDatesRecurrence = recurrence
                .stream(recurrence.getDateTimeStart())
                .collect(Collectors.toList());
        madeDates.addAll(madeDatesParent);
        madeDates.addAll(madeDatesRecurrence);
        Collections.sort(madeDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
        List<LocalDateTime> expectedDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 22, 16, 0)
              , LocalDateTime.of(2015, 11, 23, 10, 0)
              , LocalDateTime.of(2015, 11, 25, 10, 0)
              , LocalDateTime.of(2015, 11, 27, 10, 0)
              , LocalDateTime.of(2015, 11, 29, 10, 0)
                ));
        assertEquals(expectedDates, madeDates);
    }
        
    @Test
    public void canStreamDatesGoogleParts()
    {
        List<VEventMock> vComponents = getGoogleRepeatableParts();
        VEventMock v0 = vComponents.get(0);
        VEventMock v1 = vComponents.get(1);
        VEventMock v2 = vComponents.get(2);
        List<Temporal> startDates = new ArrayList<>();
        List<Temporal> startDatesV0 = v0
                .stream(v0.getDateTimeStart())
                .collect(Collectors.toList());
        List<Temporal> startDatesV1 = v1
                .stream(v1.getDateTimeStart())
                .limit(10)
                .collect(Collectors.toList());
        List<Temporal> startDatesV2 = v2
                .stream(v2.getDateTimeStart())
                .collect(Collectors.toList());
        startDates.addAll(startDatesV0);
        startDates.addAll(startDatesV1);
        startDates.addAll(startDatesV2);
        Collections.sort(startDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
        List<ZonedDateTime> expectedStartDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 15, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 7, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 17, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 19, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 20, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 22, 11, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 11, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedStartDates, startDates);
        
        List<Temporal> endDates = new ArrayList<>();
        Duration d0 = Duration.between(v0.getDateTimeStart(), v0.getDateTimeEnd());
        List<Temporal> endDatesV0 = v0
                .stream(v0.getDateTimeStart())
                .map(t -> t.plus(d0))
                .collect(Collectors.toList());
        Duration d1 = Duration.between(v1.getDateTimeStart(), v1.getDateTimeEnd());
        List<Temporal> endDatesV1 = v1
                .stream(v1.getDateTimeStart())
                .map(t -> t.plus(d1))
                .limit(10)
                .collect(Collectors.toList());
        Duration d2 = Duration.between(v2.getDateTimeStart(), v2.getDateTimeEnd());
        List<Temporal> endDatesV2 = v2
                .stream(v2.getDateTimeStart())
                .map(t -> t.plus(d2))
                .collect(Collectors.toList());
        endDates.addAll(endDatesV0);
        endDates.addAll(endDatesV1);
        endDates.addAll(endDatesV2);
        Collections.sort(endDates, DateTimeUtilities.TEMPORAL_COMPARATOR);
        List<ZonedDateTime> expectedEndDates = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 14, 13, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 15, 13, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 16, 9, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 17, 13, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 18, 14, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 19, 14, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 20, 14, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 21, 14, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 22, 14, 0), ZoneId.of("America/Los_Angeles"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 23, 14, 0), ZoneId.of("America/Los_Angeles"))
                ));
        assertEquals(expectedEndDates, endDates);
    }
    
    // TODO - MOVE THESE TESTS TO AGENDA

//    /*
//     * t is Temporal in startTemporal and endTemporal
//     * a is what is the modified argument in the setStartLocalDateTime and setEndLocalDateTime.
//     * a is modified to be either LocalDate if Appointment isWholeDay is true and LocalDateTime otherwise.
//     */
//
//    @Test
//    public void canConvertLocalDateToLocalDate()
//    {
//        // LocalDate into LocalDate
//        {
//            Temporal t = LocalDate.of(2015, 11, 18);
//            TemporalAdjuster a = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            assertEquals(LocalDate.of(2015, 11, 19), actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToThaiBuddhistDate()
//    {
//        // LocalDate into ThaiBuddhistDate
//        {
//            Temporal t = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
//            TemporalAdjuster a = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 19));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToZonedDateTime()
//    {
//        // LocalDate into ZonedDateTime
//        {
//            Temporal t = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
//            TemporalAdjuster a = LocalDate.of(2015, 11, 19);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 0), ZoneId.of("Japan"));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateToLocalDateTime()
//    {
//        // LocalDate into LocalDateTime
//        {
//            Temporal t = LocalDateTime.of(2015, 11, 19, 5, 30);
//            TemporalAdjuster a = LocalDate.of(2015, 11, 18);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = LocalDateTime.of(2015, 11, 18, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToLocalDateTime()
//    {
//        // LocalDateTime into LocalDateTime
//        {
//            Temporal t = LocalDateTime.of(2015, 11, 19, 5, 30);
//            TemporalAdjuster a = LocalDateTime.of(2015, 11, 22, 11, 30);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = LocalDateTime.of(2015, 11, 22, 11, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToLocalDate()
//    {
//        // LocalDateTime into LocalDate
//        {
//            Temporal t = LocalDate.of(2015, 11, 18);
//            TemporalAdjuster a = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToZonedDateTime()
//    {
//        // LocalDateTime into ZonedDateTime
//        {
//            Temporal t = ZonedDateTime.of(LocalDateTime.of(2015, 11, 18, 5, 0), ZoneId.of("Japan"));
//            TemporalAdjuster a = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = ZonedDateTime.of(LocalDateTime.of(2015, 11, 19, 5, 30), ZoneId.of("Japan"));
//            assertEquals(expected, actual);
//        }
//    }
//    
//    @Test
//    public void canConvertLocalDateTimeToThaiBuddhistDate()
//    {
//        // LocalDateTime into ThaiBuddhistDate
//        {
//            Temporal t = ThaiBuddhistDate.from(LocalDate.of(2015, 11, 18));
//            TemporalAdjuster a = LocalDateTime.of(2015, 11, 19, 5, 30);
//            Temporal actual = TemporalType.from(t.getClass()).combine(t, a);
//            Temporal expected = LocalDateTime.of(2015, 11, 19, 5, 30);
//            assertEquals(expected, actual);
//        }
//    }
}
