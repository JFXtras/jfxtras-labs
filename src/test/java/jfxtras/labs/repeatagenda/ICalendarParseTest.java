package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.Temporal;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonth;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByWeekNo;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Yearly;

public class ICalendarParseTest extends ICalendarTestAbstract
{
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canParseDurationString()
    {
        VEventImpl v = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        String duration = "P15DT5H0M20S";
        v.setDurationInNanos(duration);
        System.out.println(v.getDurationInNanos() + " " + (1314020l * NANOS_IN_SECOND));
        assertTrue(v.getDurationInNanos() == (1314020l * NANOS_IN_SECOND));
    }
    
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canParseDurationString2()
    {
        VEventImpl v = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        String duration = "PT1H30M";
        v.setDurationInNanos(duration);
        assertTrue(v.getDurationInNanos() == (5400l * NANOS_IN_SECOND));
    }

    /** tests converting ISO.8601.2004 date time string to LocalDateTime */
    @Test
    public void canParseDateTimeString1()
    {
        VEventImpl v = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        String duration = "TZID=America/New_York:19980119T020000";
        LocalDateTime l = v.iCalendarDateTimeToLocalDateTime(duration);
        assertEquals(l, LocalDateTime.of(1998, 1, 19, 2, 0));
    }

    /** tests converting ISO.8601.2004 date string to LocalDateTime */
    @Test
    public void canParseDateTimeString2()
    {
        VEventImpl v = new VEventImpl(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        String duration = "VALUE=DATE:19980704";
        LocalDateTime l = v.iCalendarDateTimeToLocalDateTime(duration);
        assertEquals(l, LocalDateTime.of(1998, 7, 4, 0, 0));
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
                              + "CREATED:20151109T082900" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "DURATION:PT1H" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(vEventString, ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        vEvent.setAppointmentClass(getClazz());
        VEvent expectedVEvent = getYearly1();
        assertEquals(expectedVEvent, vEvent);
    }

    @Test
    public void canParseDaily3()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(vEventString, ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        VEventImpl expectedVEvent = getDaily3();
        assertEquals(expectedVEvent, vEvent);
    }
    
    /** FREQ=DAILY;INVERVAL=2;UNTIL=20151201T000000 */
    @Test
    public void canParseDaily6()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CATEGORIES:group03" + System.lineSeparator()
                              + "DESCRIPTION:Daily6 Description" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=2;UNTIL=20151201T000000" + System.lineSeparator()
                              + "SUMMARY:Daily6 Summary" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(vEventString, ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        VEventImpl expectedVEvent = getDaily6();
        assertEquals(expectedVEvent, vEvent);
    }
    
    /** Tests FREQ=YEARLY */
    @Test
    public void canParseDailyWithException1()
    {
        String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                + "CATEGORIES:group03" + System.lineSeparator()
                + "DESCRIPTION:Daily2 Description" + System.lineSeparator()
                + "DTSTAMP:20150110T080000" + System.lineSeparator()
                + "DTSTART:20151109T100000" + System.lineSeparator()
                + "DURATION:PT1H30M" + System.lineSeparator()
                + "EXDATE:20151112T100000,20151115T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "SUMMARY:Daily2 Summary" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(vEventString, ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
        VEventImpl expectedVEvent = getDailyWithException1();
        assertEquals(expectedVEvent, vEvent);
    }

    @Test
    public void canParseDate1()
    {
        Temporal date = LocalDate.of(2015, 11, 15);
        Temporal dateString = VComponent.parseTemporal("VALUE=DATE:20151115");
        assertEquals(dateString, date);
    }
    
    @Test
    public void canParseWholeDay1()
    {
    String vEventString = "BEGIN:VEVENT" + System.lineSeparator()
                          + "DTEND:VALUE=DATE:20151112" + System.lineSeparator()
                          + "DTSTAMP:20150110T080000" + System.lineSeparator()
                          + "DTSTART:VALUE=DATE:20151109" + System.lineSeparator()
                          + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                          + "END:VEVENT";
    VEventImpl vEvent = VEventImpl.parseVEvent(vEventString, ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS);
    VEventImpl expectedVEvent = getWholeDayDaily1();
    assertEquals(expectedVEvent, vEvent);
    }
}
