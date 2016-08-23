package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.junit.Test;

import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;

public class CatchDateTimeErrorTest
{
    @Test (expected = DateTimeException.class)
    public void canCatchInvalidExDates()
    {
        Thread.setDefaultUncaughtExceptionHandler((t1, e) ->
        {
            throw (RuntimeException) e;
        });
        new VEvent()
                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))))
                .withExceptionDates(new ExceptionDates(LocalDateTime.of(2016, 2, 12, 12, 30))) // invalid - stop processing
                .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                ;
    }
    
    @Test
    public void canCatchParseInvalidExDates()
    {
            String content = "BEGIN:VEVENT" + System.lineSeparator() +
            "DTSTART;TZID=America/Los_Angeles:20160207T123000" + System.lineSeparator() +
            "EXDATE;TZID=America/Los_Angeles:20160210T123000" + System.lineSeparator() +
            "EXDATE:20160212T123000" + System.lineSeparator() + // invalid - ignore
            "EXDATE;TZID=America/Los_Angeles:20160209T123000" + System.lineSeparator() +
            "END:VEVENT";
            VEvent v = VEvent.parse(content);
            
            VEvent expected = new VEvent()
                    .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 2, 7, 12, 30), ZoneId.of("America/Los_Angeles")))
                    .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 12, 30), ZoneId.of("America/Los_Angeles"))))
                    .withExceptionDates(new ExceptionDates(ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 12, 30), ZoneId.of("America/Los_Angeles"))))
                    ;
            assertEquals(expected, v);
    }
}
