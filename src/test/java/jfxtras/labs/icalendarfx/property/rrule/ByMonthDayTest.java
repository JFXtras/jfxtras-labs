package jfxtras.labs.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByMonthDay;

public class ByMonthDayTest
{
    @Test
    public void canParseByMonth()
    {
        ByMonthDay element = new ByMonthDay(4,14);
        assertEquals(Month.APRIL, element.getValue().get(0));
        assertEquals("BYMONTH=4", element.toContent());
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=DAILY;BYMONTHDAY=4,5,7,8
     */
    @Test
    public void canStreamByMonthDay()
    {
        ByMonthDay element = new ByMonthDay(4,5,7,8);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.DAYS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2016, 5, 7, 10, 0)
              , LocalDateTime.of(2016, 5, 8, 10, 0)
              , LocalDateTime.of(2016, 6, 4, 10, 0)
              , LocalDateTime.of(2016, 6, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTHDAY=4,5
     */
    @Test
    public void canStreamByMonthDay2()
    {
        ByMonthDay element = new ByMonthDay(4,5);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2016, 6, 4, 10, 0)
              , LocalDateTime.of(2016, 6, 5, 10, 0)
              , LocalDateTime.of(2016, 7, 4, 10, 0)
              , LocalDateTime.of(2016, 7, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160510T100000
    RRULE:FREQ=MONTHLY;BYMONTHDAY=10,11,12
     */
    @Test
    public void canStreamByMonthDay3()
    {
        ByMonthDay element = new ByMonthDay(10,11,12);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 10, 10, 0);
        ChronoUnit frequency = ChronoUnit.MONTHS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 10, 10, 0)
              , LocalDateTime.of(2016, 5, 11, 10, 0)
              , LocalDateTime.of(2016, 5, 12, 10, 0)
              , LocalDateTime.of(2016, 6, 10, 10, 0)
              , LocalDateTime.of(2016, 6, 11, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).peek(System.out::println).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
}
