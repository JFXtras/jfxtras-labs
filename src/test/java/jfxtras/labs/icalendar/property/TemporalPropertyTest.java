package jfxtras.labs.icalendar.property;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.labs.icalendar.utilities.DateTimeUtilities;

/** tests parsing ISO.8601.2004 date string properties */
@Deprecated // done in ValueType now
public class TemporalPropertyTest
{
    @Test
    public void canParseLocalDate()
    {
        String s = "DTSTART;VALUE=DATE:20160307";
        Temporal t = DateTimeUtilities.parse(s);
        assertEquals(t, LocalDate.of(2016, 3, 7));
    }
    
    @Test
    public void canParseZonedDateTimeUTC()
    {
        String s = "DTSTART;TZID=Etc/GMT:20160306T123000Z";
        Temporal t = DateTimeUtilities.parse(s);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 12, 30), ZoneId.of("Z")));
    }

    @Test
    public void canParseZonedDateTime()
    {
        String s = "DTEND;TZID=America/Los_Angeles:19970512T093000";
        Temporal t = DateTimeUtilities.parse(s);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(1997, 5, 12, 9, 30), ZoneId.of("America/Los_Angeles")));
    }
    
    /** tests converting ISO.8601.2004 date-time string to LocalDateTime */
    @Test
    public void canParseLocalDateTime()
    {
        String string = "DTSTART:20160306T123000";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, LocalDateTime.of(2016, 3, 6, 12, 30));
    }
    
    @Test
    public void canParseZonedDateTimeUTC2()
    {
        String string = "DTEND:20160306T123000Z";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 12, 30), ZoneId.of("Z")));
    }

    @Test
    public void canParseZonedDateTimeUTCAlone()
    {
        String string = "20160306T123000Z";
        Temporal t = DateTimeUtilities.parse(string);
        assertEquals(t, ZonedDateTime.of(LocalDateTime.of(2016, 3, 6, 12, 30), ZoneId.of("Z")));
    }
    
//    @Test
//    public void canParseDateList()
//    {
//        String string = "19970101,19970120,19970217,19970421";
//        List<Temporal> expectedTemporals = new ArrayList<>(Arrays.asList(
//                LocalDate.of(1997, 1, 1)
//              , LocalDate.of(1997, 1, 20)
//              , LocalDate.of(1997, 2, 17)
//              , LocalDate.of(1997, 4, 21)
//                ));
//        List<Temporal> temporals = Recurrence.parseTemporals(string);
//        assertEquals(expectedTemporals, temporals);
//    }
//
//    @Test
//    public void canParseZonedDateTimeList()
//    {
//        String string = "TZID=Europe/London:20160208T073000,20160210T073000,20160209T073000,20160213T073000";
//        List<Temporal> expectedTemporals = new ArrayList<>(Arrays.asList(
//                ZonedDateTime.of(LocalDateTime.of(2016, 2, 8, 7, 30), ZoneId.of("Europe/London"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 9, 7, 30), ZoneId.of("Europe/London"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 10, 7, 30), ZoneId.of("Europe/London"))
//              , ZonedDateTime.of(LocalDateTime.of(2016, 2, 13, 7, 30), ZoneId.of("Europe/London"))
//                ));
//        List<Temporal> temporals = Recurrence.parseTemporals(string);
//        Collections.sort(temporals, DateTimeUtilities.TEMPORAL_COMPARATOR);
//        assertEquals(expectedTemporals, temporals);
//    }

}
