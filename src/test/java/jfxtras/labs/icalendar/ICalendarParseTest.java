package jfxtras.labs.icalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendar.mocks.VEventMock;
import jfxtras.labs.icalendar.rrule.RRule;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.icalendar.rrule.byxxx.ByMonth;
import jfxtras.labs.icalendar.rrule.byxxx.ByMonthDay;
import jfxtras.labs.icalendar.rrule.byxxx.ByWeekNo;
import jfxtras.labs.icalendar.rrule.freq.Daily;
import jfxtras.labs.icalendar.rrule.freq.Frequency;
import jfxtras.labs.icalendar.rrule.freq.Monthly;
import jfxtras.labs.icalendar.rrule.freq.Yearly;

public class ICalendarParseTest extends ICalendarTestAbstract
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
        List<Temporal> temporals = RecurrenceComponent.parseTemporals(string);
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
        List<Temporal> temporals = RecurrenceComponent.parseTemporals(string);
        Collections.sort(temporals, VComponent.TEMPORAL_COMPARATOR);
        assertEquals(expectedTemporals, temporals);
    }
    
    /** tests parsing RRULE:FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU */
    @Test
    public void canParseRRule1()
    {
        String s = "FREQ=YEARLY;INTERVAL=2;BYMONTH=1;BYDAY=SU";
        RRule rRule = RRule.parseRRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Yearly()
                .withInterval(2);
        frequency.addByRule(new ByMonth(Month.JANUARY));
        frequency.addByRule(new ByDay(DayOfWeek.SUNDAY));
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule2()
    {
        String s = "FREQ=MONTHLY;BYDAY=SA;BYMONTHDAY=7,8,9,10,11,12,13";
        RRule rRule = RRule.parseRRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Monthly();
        frequency.addByRule(new ByDay(DayOfWeek.SATURDAY));
        frequency.addByRule(new ByMonthDay(7,8,9,10,11,12,13));
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }
    
    @Test
    public void canParseRRule3()
    {
        String s = "FREQ=YEARLY;BYWEEKNO=20;BYDAY=2MO,3MO";
        RRule rRule = RRule.parseRRule(s);
        RRule expectedRRule = new RRule();
        Frequency frequency = new Yearly();
        frequency.addByRule(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)));
        frequency.addByRule(new ByWeekNo(20));
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }

    @Test
    public void canParseRRule4()
    {
        String s = "FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000";
        RRule rRule = RRule.parseRRule(s);
        RRule expectedRRule = new RRule();
        expectedRRule.setUntil(LocalDateTime.of(2015, 12, 1, 10, 0));
        Frequency frequency = new Daily();
        frequency.setInterval(2);
        expectedRRule.setFrequency(frequency);
        assertEquals(expectedRRule, rRule);
    }
        
    /** Tests FREQ=YEARLY */
    @Test
    public void canParseYearly1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CATEGORIES:group13" + System.lineSeparator()
                              + "CREATED:20151109T082900Z" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "DURATION:PT1H" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000Z" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventMock vEvent = VEventMock.parse(vEventString);
        VEventMock expectedVEvent = getYearly1();
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }

    @Test
    public void canParseDaily3()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventMock vEvent = VEventMock.parse(vEventString);
        System.out.println(vEvent);
        VEventMock expectedVEvent = getDaily3();
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
        
    @Test
    public void canParseDailyUTC()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CATEGORIES:group03" + System.lineSeparator()
                              + "DESCRIPTION:DailyUTC Description" + System.lineSeparator()
                              + "DTEND:20151109T110000Z" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                              + "DTSTART:20151109T100000Z" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T100000Z" + System.lineSeparator()
                              + "SUMMARY:DailyUTC Summary" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventMock vEvent = VEventMock.parse(vEventString);
        VEventMock expectedVEvent = getDailyUTC();
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    /** Tests FREQ=YEARLY */
    @Test
    public void canParseDailyWithException1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group03" + System.lineSeparator()
                + "DESCRIPTION:Daily2 Description" + System.lineSeparator()
                + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                + "DTSTART:20151109T100000" + System.lineSeparator()
                + "DURATION:PT1H30M" + System.lineSeparator()
                + "EXDATE:20151112T100000,20151115T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "SUMMARY:Daily2 Summary" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        VEventMock vEvent = VEventMock.parse(vEventString);
        VEventMock expectedVEvent = getDailyWithException1();
        assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseWholeDay1()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTEND:VALUE=DATE:20151112" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000Z" + System.lineSeparator()
                          + "DTSTART:VALUE=DATE:20151109" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getWholeDayDaily1();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleIndividual()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART:20160214T123000Z" + System.lineSeparator()
            + "DTEND:20160214T150000Z" + System.lineSeparator()
            + "DTSTAMP:20160214T022532Z" + System.lineSeparator()
            + "UID:vpqej26mlpg3adcncqqs7t7a34@google.com" + System.lineSeparator()
            + "CREATED:20160214T022513Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T022513Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "SEQUENCE:0" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test1" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleIndividual();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleRepeat()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART;TZID=America/Los_Angeles:20160214T080000" + System.lineSeparator()
            + "DTEND;TZID=America/Los_Angeles:20160214T110000" + System.lineSeparator()
            + "RRULE:FREQ=WEEKLY;BYDAY=SU,TU,FR" + System.lineSeparator()
            + "DTSTAMP:20160214T022532Z" + System.lineSeparator()
            + "UID:im8hmpakeigu3d85j3vq9q8bcc@google.com" + System.lineSeparator()
            + "CREATED:20160214T022525Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T022525Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "SEQUENCE:0" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test2" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleRepeatable();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleRepeatWithExDates()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART;TZID=America/Los_Angeles:20160207T123000" + System.lineSeparator()
            + "DTEND;TZID=America/Los_Angeles:20160207T153000" + System.lineSeparator()
            + "RRULE:FREQ=DAILY;UNTIL=20160512T193000Z" + System.lineSeparator()
            + "EXDATE;TZID=America/Los_Angeles:20160210T123000" + System.lineSeparator()
            + "EXDATE;TZID=America/Los_Angeles:20160212T123000" + System.lineSeparator()
            + "EXDATE;TZID=America/Los_Angeles:20160209T123000" + System.lineSeparator()
            + "DTSTAMP:20160214T072231Z" + System.lineSeparator()
            + "UID:86801l7316n97h75cefk1ruc00@google.com" + System.lineSeparator()
            + "CREATED:20160214T022525Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T022525Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "SEQUENCE:0" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test3" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleWithExDates();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleRepeatablePart1()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART;TZID=America/Los_Angeles:20160214T110000" + System.lineSeparator()
            + "DTEND;TZID=America/Los_Angeles:20160214T130000" + System.lineSeparator()
            + "RRULE:FREQ=DAILY;UNTIL=20160218T185959Z" + System.lineSeparator()
            + "DTSTAMP:20160214T213637Z" + System.lineSeparator()
            + "UID:mrrfvnj5acdcvn13273on9nrhs@google.com" + System.lineSeparator()
            + "CREATED:20160214T193703Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T193717Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "SEQUENCE:0" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test4" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleRepeatablePart1();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleRepeatablePart2()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART;TZID=America/Los_Angeles:20160218T110000" + System.lineSeparator()
            + "DTEND;TZID=America/Los_Angeles:20160218T140000" + System.lineSeparator()
            + "RRULE:FREQ=DAILY;COUNT=6" + System.lineSeparator()
            + "DTSTAMP:20160214T213637Z" + System.lineSeparator()
            + "UID:mrrfvnj5acdcvn13273on9nrhs_R20160218T190000@google.com" + System.lineSeparator()
            + "CREATED:20160214T193703Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T193717Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "SEQUENCE:0" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test5" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleRepeatablePart2();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
    
    @Test
    public void canParseGoogleRepeatablePart3()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
            + "DTSTART;TZID=America/Los_Angeles:20160216T070000" + System.lineSeparator()
            + "DTEND;TZID=America/Los_Angeles:20160216T090000" + System.lineSeparator()
            + "DTSTAMP:20160214T213637Z" + System.lineSeparator()
            + "UID:mrrfvnj5acdcvn13273on9nrhs@google.com" + System.lineSeparator()
            + "CREATED:20160214T193703Z" + System.lineSeparator()
            + "DESCRIPTION:" + System.lineSeparator()
            + "LAST-MODIFIED:20160214T213226Z" + System.lineSeparator()
            + "LOCATION:" + System.lineSeparator()
            + "RECURRENCE-ID;TZID=America/Los_Angeles:20160216T110000" + System.lineSeparator()
            + "SEQUENCE:1" + System.lineSeparator()
            + "STATUS:CONFIRMED" + System.lineSeparator() // currently not supported
            + "SUMMARY:test6" + System.lineSeparator()
            + "TRANSP:OPAQUE" + System.lineSeparator() // currently not supported
            + "END:VEVENT";
    VEventMock vEvent = VEventMock.parse(vEventString);
    VEventMock expectedVEvent = getGoogleRepeatablePart3();
    assertTrue(VEventMock.isEqualTo(expectedVEvent, vEvent));
    }
}
