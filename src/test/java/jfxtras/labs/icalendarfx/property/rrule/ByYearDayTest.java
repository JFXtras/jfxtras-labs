package jfxtras.labs.icalendarfx.property.rrule;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByYearDay;

public class ByYearDayTest
{
    @Test
    public void canParseByYearDay()
    {
        ByYearDay element = new ByYearDay(100,200,300);
        assertEquals(Arrays.asList(100,200,300), element.getValue());
        assertEquals("BYYEARDAY=100,200,300", element.toContent());
    }
    
    /*
    DTSTART:20160104T000000
    RRULE:FREQ=HOURLY;BYYEARDAY=-2,4
     */
    @Test
    public void canStreamByYearDay()
    {
        ByYearDay element = new ByYearDay(4,-2);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 1, 4, 0, 0);
        ChronoUnit frequency = ChronoUnit.HOURS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(8, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 1, 4, 0, 0)
              , LocalDateTime.of(2016, 1, 4, 8, 0)
              , LocalDateTime.of(2016, 1, 4, 16, 0)
              , LocalDateTime.of(2016, 12, 30, 0, 0)
              , LocalDateTime.of(2016, 12, 30, 8, 0)
              , LocalDateTime.of(2016, 12, 30, 16, 0)
              , LocalDateTime.of(2017, 1, 4, 0, 0)
              , LocalDateTime.of(2017, 1, 4, 8, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(8).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    /*
    DTSTART:20160505T100000
    RRULE:FREQ=YEARLY;BYMONTH=4,5
     */
    @Test
    public void canStreamByYearDay2()
    {
        ByYearDay element = new ByYearDay(4,5);
        LocalDateTime dateTimeStart = LocalDateTime.of(2016, 5, 5, 10, 0);
        ChronoUnit frequency = ChronoUnit.YEARS;
        TemporalAdjuster adjuster = (temporal) -> temporal.plus(1, frequency);
        Stream<Temporal> inStream = Stream.iterate(dateTimeStart, a -> a.with(adjuster));
        Stream<Temporal> recurrenceStream = element.streamRecurrences(inStream, frequency, dateTimeStart);
        List<LocalDateTime> expectedRecurrences = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 5, 10, 0)
              , LocalDateTime.of(2017, 4, 5, 10, 0)
              , LocalDateTime.of(2017, 5, 5, 10, 0)
              , LocalDateTime.of(2018, 4, 5, 10, 0)
              , LocalDateTime.of(2018, 5, 5, 10, 0)
                ));
        List<Temporal> madeRecurrences = recurrenceStream.limit(5).collect(Collectors.toList());
        assertEquals(expectedRecurrences, madeRecurrences);
    }
    
    @Test  (expected = IllegalArgumentException.class)
    public void canCatchOutOfRangeByYearDay()
    {
        new ByYearDay(1100,200,300);
    }
}
