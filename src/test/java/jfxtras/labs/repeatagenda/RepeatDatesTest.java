package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;


/**
 * Tests nextValidDate and NextAppointment TemporalAdjuster method to determine if
 * correct next appointment dates are returned
 */
public class RepeatDatesTest extends RepeatTestAbstract {

    // Variable date tests - based on LocalDate.now()
    @Test
    public void canGetFirstDateDailyM10()
    {
        Repeat r = getRepeatDaily();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyM10()
    {
        Repeat r = getRepeatWeekly();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyM10()
    {
        Repeat r = getRepeatMonthly();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(r.getStartLocalDate(), firstMatchDate);
    }
    
    // Fixed tests
    @Test
    public void canGetSecondDateWeeklyFixedM0()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(0));
        assertEquals(LocalDateTime.of(2015, 10, 9, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedM10()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().minusDays(10));
        assertEquals(LocalDateTime.of(2015, 10, 7, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetSecondDateDailyFixedP0()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDateTime.of(2015, 10, 10, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP0()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDateTime firstMatchDate = r
                .streamOfDatesEndless()
                .findFirst().get();
        assertEquals(LocalDateTime.of(2015, 10, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP1()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDateTime.of(2015, 10, 10, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP10()
    {
        Repeat r = getRepeatDailyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDateTime.of(2015, 10, 19, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateDailyFixedP100()
    {
        Repeat r = getRepeatDailyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDateTime.of(2016, 1, 17, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP0()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDateTime.of(2015, 10, 9, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP1()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDateTime.of(2015, 10, 9, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP10()
    {
        Repeat r = getRepeatWeeklyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDateTime.of(2015, 10, 21, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixedP100()
    {
        Repeat r = getRepeatWeeklyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDateTime.of(2016, 1, 20, 18, 0), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P0()
    {
        Repeat r = getRepeatWeeklyFixed2();
//        r.dayOfWeekMap.entrySet().stream().forEach(System.out::println);
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDateTime.of(2015, 10, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P1()
    {
        Repeat r = getRepeatWeeklyFixed2();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDateTime.of(2015, 10, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P10()
    {
        Repeat r = getRepeatWeeklyFixed2();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDateTime.of(2015, 10, 19, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateWeeklyFixed2P100()
    {
        Repeat r = getRepeatWeeklyFixed2();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDateTime.of(2016, 1, 15, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP0()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDateTime.of(2015, 11, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP1()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDateTime.of(2015, 11, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP10()
    {
        Repeat r = getRepeatMonthlyFixed();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDateTime.of(2015, 11, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixedP100()
    {
        Repeat r = getRepeatMonthlyFixed();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDateTime.of(2016, 2, 7, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P0()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(0));
        assertEquals(LocalDateTime.of(2015, 11, 19, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P1()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(1));
        assertEquals(LocalDateTime.of(2015, 11, 19, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P10()
    {
        Repeat r = getRepeatMonthlyFixed2();
//        System.out.println("r.getStartLocalDate() " + r.getStartLocalDate());
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(10));
        assertEquals(LocalDateTime.of(2015, 11, 19, 8, 45), firstMatchDate);
    }
    @Test
    public void canGetFirstDateMonthlyFixed2P100()
    {
        Repeat r = getRepeatMonthlyFixed2();
        LocalDateTime firstMatchDate = r.nextValidDateSlow(r.getStartLocalDate().plusDays(100));
        assertEquals(LocalDateTime.of(2016, 2, 18, 8, 45), firstMatchDate);
    }
    
    // List of dates tests
    @Test
    public void canListDailyFixed()
    {
        Repeat r = getRepeatDailyFixed();
        List<LocalDateTime> madeDates = r.streamOfDates()
            .collect(Collectors.toList());
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 7)
              , LocalDate.of(2015, 10, 10)
              , LocalDate.of(2015, 10, 13)
              , LocalDate.of(2015, 10, 16)
              , LocalDate.of(2015, 10, 19)
              , LocalDate.of(2015, 10, 22)
              , LocalDate.of(2015, 10, 25)
              , LocalDate.of(2015, 10, 28)
              , LocalDate.of(2015, 10, 31)
              , LocalDate.of(2015, 11, 3)
              , LocalDate.of(2015, 11, 6)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
            .map(a -> LocalDateTime.of(a, LocalTime.of(8, 45)))
            .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    @Test
    public void canListWeeklyFixed()
    {
        Repeat r = getRepeatWeeklyFixed();
        List<LocalDateTime> madeDates = r.streamOfDatesEndless()
            .limit(10)
            .collect(Collectors.toList());
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 7)
              , LocalDate.of(2015, 10, 9)
              , LocalDate.of(2015, 10, 14)
              , LocalDate.of(2015, 10, 16)
              , LocalDate.of(2015, 10, 21)
              , LocalDate.of(2015, 10, 23)
              , LocalDate.of(2015, 10, 28)
              , LocalDate.of(2015, 10, 30)
              , LocalDate.of(2015, 11, 4)
              , LocalDate.of(2015, 11, 6)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
                .map(a -> LocalDateTime.of(a, LocalTime.of(18, 0)))
                .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    
    @Test
    public void canListWeeklyFixed2()
    {
        Repeat r = getRepeatWeeklyFixed2();
        List<LocalDateTime> madeDates = r.streamOfDatesEndless()
            .limit(r.getCount())
            .collect(Collectors.toList());
//        madeDates.stream().forEach(System.out::println);
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 5)
              , LocalDate.of(2015, 10, 7)
              , LocalDate.of(2015, 10, 9)
              , LocalDate.of(2015, 10, 19)
              , LocalDate.of(2015, 10, 21)
              , LocalDate.of(2015, 10, 23)
              , LocalDate.of(2015, 11, 2)
              , LocalDate.of(2015, 11, 4)
              , LocalDate.of(2015, 11, 6)
              , LocalDate.of(2015, 11, 16)
              , LocalDate.of(2015, 11, 18)
              , LocalDate.of(2015, 11, 20)
              , LocalDate.of(2015, 11, 30)
              , LocalDate.of(2015, 12, 2)
              , LocalDate.of(2015, 12, 4)
              , LocalDate.of(2015, 12, 14)
              , LocalDate.of(2015, 12, 16)
              , LocalDate.of(2015, 12, 18)
              , LocalDate.of(2015, 12, 28)
              , LocalDate.of(2015, 12, 30)
              , LocalDate.of(2016, 1, 1)
              , LocalDate.of(2016, 1, 11)
              , LocalDate.of(2016, 1, 13)
              , LocalDate.of(2016, 1, 15)
              , LocalDate.of(2016, 1, 25)
              , LocalDate.of(2016, 1, 27)
              , LocalDate.of(2016, 1, 29)
              , LocalDate.of(2016, 2, 8)
              , LocalDate.of(2016, 2, 10)
              , LocalDate.of(2016, 2, 12)
              , LocalDate.of(2016, 2, 22)
              , LocalDate.of(2016, 2, 24)
              , LocalDate.of(2016, 2, 26)
              , LocalDate.of(2016, 3, 7)
              , LocalDate.of(2016, 3, 9)
              , LocalDate.of(2016, 3, 11)
              , LocalDate.of(2016, 3, 21)
              , LocalDate.of(2016, 3, 23)
              , LocalDate.of(2016, 3, 25)
              , LocalDate.of(2016, 4, 4)
              , LocalDate.of(2016, 4, 6)
              , LocalDate.of(2016, 4, 8)
              , LocalDate.of(2016, 4, 18)
              , LocalDate.of(2016, 4, 20)
              , LocalDate.of(2016, 4, 22)
              , LocalDate.of(2016, 5, 2)
              , LocalDate.of(2016, 5, 4)
              , LocalDate.of(2016, 5, 6)
              , LocalDate.of(2016, 5, 16)
              , LocalDate.of(2016, 5, 18)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
                .map(a -> LocalDateTime.of(a, LocalTime.of(8, 45)))
                .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    @Test
    public void canListMonthlyFixed()
    {
        Repeat r = getRepeatMonthlyFixed();
        List<LocalDateTime> madeDates = r.streamOfDates()
            .collect(Collectors.toList());
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 7)
              , LocalDate.of(2015, 11, 7)
              , LocalDate.of(2015, 12, 7)
              , LocalDate.of(2016, 1, 7)
              , LocalDate.of(2016, 2, 7)
              , LocalDate.of(2016, 3, 7)
              , LocalDate.of(2016, 4, 7)
              , LocalDate.of(2016, 5, 7)
              , LocalDate.of(2016, 6, 7)
              , LocalDate.of(2016, 7, 7)
              , LocalDate.of(2016, 8, 7)
              , LocalDate.of(2016, 9, 7)
              , LocalDate.of(2016, 10, 7)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
                .map(a -> LocalDateTime.of(a, LocalTime.of(8, 45)))
                .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    @Test
    public void canListMonthlyFixed2()
    {
        Repeat r = getRepeatMonthlyFixed2();
        List<LocalDateTime> madeDates = r.streamOfDates()
            .collect(Collectors.toList());
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 15)
              , LocalDate.of(2015, 11, 19)
              , LocalDate.of(2015, 12, 17)
              , LocalDate.of(2016, 1, 21)
              , LocalDate.of(2016, 2, 18)
              , LocalDate.of(2016, 3, 17)
              , LocalDate.of(2016, 4, 21)
              , LocalDate.of(2016, 5, 19)
              , LocalDate.of(2016, 6, 16)
              , LocalDate.of(2016, 7, 21)
              , LocalDate.of(2016, 8, 18)
              , LocalDate.of(2016, 9, 15)
              , LocalDate.of(2016, 10, 20)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
                .map(a -> LocalDateTime.of(a, LocalTime.of(8, 45)))
                .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    @Test
    public void canListYearlyFixed()
    {
        Repeat r = getRepeatYearlyFixed();
        List<LocalDateTime> madeDates = r.streamOfDatesEndless()
            .limit(10)
            .collect(Collectors.toList());
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 7)
              , LocalDate.of(2016, 10, 7)
              , LocalDate.of(2017, 10, 7)
              , LocalDate.of(2018, 10, 7)
              , LocalDate.of(2019, 10, 7)
              , LocalDate.of(2020, 10, 7)
              , LocalDate.of(2021, 10, 7)
              , LocalDate.of(2022, 10, 7)
              , LocalDate.of(2023, 10, 7)
              , LocalDate.of(2024, 10, 7)
              ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
                .map(a -> LocalDateTime.of(a, LocalTime.of(8, 45)))
                .collect(Collectors.toList());
        assertEquals(expectedDates2, madeDates);
    }
    
}
