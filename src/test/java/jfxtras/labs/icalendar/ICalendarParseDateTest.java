package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendar.properties.recurrence.Recurrence;

public class ICalendarParseDateTest
{
    /** tests converting ISO.8601.2004 date string to LocalDate */
    @Test
    public void canParseLocalDate()
    {
        String string = "19980704";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, LocalDate.of(1998, 7, 4));
    }
    
    /** tests converting ISO.8601.2004 date-time string to LocalDateTime */
    @Test
    public void canParseLocalDateTime()
    {
        String string = "19980119T020000";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, LocalDateTime.of(1998, 1, 19, 2, 0));
    }
    
    /** tests converting ISO.8601.2004 date-time UTC string to ZonedLocalDate */
    @Test
    public void canParseZonedDateTimeUTC()
    {
        String string = "19980119T020000Z";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(1998, 1, 19, 2, 0), ZoneId.of("Z").normalized()));
    }
    
    /** tests converting ISO.8601.2004 date-time UTC string to ZonedLocalDate */
    @Test
    public void canParseZonedDateTime()
    {
        String string = "TZID=Europe/London:20160208T073000";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 7, 30), ZoneId.of("Europe/London")));
    }
    
    @Test
    public void canParseDateList()
    {
        String string = "19970101,19970120,19970217,19970421";
        List<Temporal> expectedTemporals = new ArrayList<>(Arrays.asList(
                LocalDate.of(1997, 1, 1)
              , LocalDate.of(1997, 1, 20)
              , LocalDate.of(1997, 2, 17)
              , LocalDate.of(1997, 4, 21)
                ));
        List<Temporal> temporals = Recurrence.parseTemporals(string);
        assertEquals(expectedTemporals, temporals);
    }

    @Test
    public void canParseZonedDateTimeList()
    {
        String string = "TZID=Europe/London:20160208T073000,20160210T073000,20160209T073000,20160213T073000";
        List<Temporal> expectedTemporals = new ArrayList<>(Arrays.asList(
                ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 7, 30), ZoneId.of("Europe/London"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 7, 30), ZoneId.of("Europe/London"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 7, 30), ZoneId.of("Europe/London"))
              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 7, 30), ZoneId.of("Europe/London"))
                ));
        List<Temporal> temporals = Recurrence.parseTemporals(string);
        Collections.sort(temporals, DateTimeUtilities.TEMPORAL_COMPARATOR);
        assertEquals(expectedTemporals, temporals);
    }
}
