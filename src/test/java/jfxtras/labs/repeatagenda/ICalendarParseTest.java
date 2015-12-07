package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VDateTime;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByDay.ByDayPair;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonth;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByMonthDay;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.byxxx.ByWeekNo;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Monthly;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Yearly;

public class ICalendarParseTest extends ICalendarTestAbstract
{
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canParseDurationString()
    {
        VEventImpl v = new VEventImpl();
        String duration = "P15DT5H0M20S";
        v.setDurationInSeconds(duration);
        assertTrue(v.getDurationInSeconds() == 1314020);
    }
    
    /** tests converting ISO.8601.2004 duration string to duration in seconds */
    @Test
    public void canParseDurationString2()
    {
        VEventImpl v = new VEventImpl();
        String duration = "PT1H30M";
        v.setDurationInSeconds(duration);
        assertTrue(v.getDurationInSeconds() == 5400);
    }

    /** tests converting ISO.8601.2004 date time string to LocalDateTime */
    @Test
    public void canParseDateTimeString1()
    {
        VEventImpl v = new VEventImpl();
        String duration = "TZID=America/New_York:19980119T020000";
        LocalDateTime l = v.iCalendarDateTimeToLocalDateTime(duration);
        assertEquals(l, LocalDateTime.of(1998, 1, 19, 2, 0));
    }

    /** tests converting ISO.8601.2004 date string to LocalDateTime */
    @Test
    public void canParseDateTimeString2()
    {
        VEventImpl v = new VEventImpl();
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
//        System.out.println("rRule:"+rRule.toString());
        RRule expectedRRule = new RRule();
        Frequency frequency = new Yearly();
        frequency.addByRule(new ByDay(new ByDayPair(DayOfWeek.MONDAY, 2), new ByDayPair(DayOfWeek.MONDAY, 3)));
        frequency.addByRule(new ByWeekNo(20));
        expectedRRule.setFrequency(frequency);
//        System.out.println("expectedRRule:"+expectedRRule.toString());
        assertEquals(expectedRRule, rRule);
    }
    
    /** Tests FREQ=YEARLY */
    @Test
    public void yearly1ToString()
    {
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "CREATED:20151109T082900" + System.lineSeparator()
                              + "DESCRIPTION:Yearly1 Description" + System.lineSeparator()
                              + "DTSTAMP:20151109T083000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "DURATION:PT1H" + System.lineSeparator()
                              + "LAST-MODIFIED:20151110T183000" + System.lineSeparator()
                              + "RRULE:FREQ=YEARLY" + System.lineSeparator()
                              + "SUMMARY:Yearly1 Summary" + System.lineSeparator()
                              + "UID:20151109T082900-0@jfxtras.org" + System.lineSeparator()
                              + "X-APPOINTMENT-GROUP:group13" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(expectedString);        
        vEvent.getAppointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        VEvent expectedVEvent = getYearly1();
        assertEquals(expectedVEvent, vEvent);
    }

    @Test
    public void daily3ToString()
    {
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                              + "DTEND:20151109T110000" + System.lineSeparator()
                              + "DTSTAMP:20150110T080000" + System.lineSeparator()
                              + "DTSTART:20151109T100000" + System.lineSeparator()
                              + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=10;BYMONTHDAY=9,10,11,12,13,14" + System.lineSeparator()
                              + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                              + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(expectedString);
        vEvent.getAppointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        VEvent expectedVEvent = getDaily3();
        assertEquals(expectedVEvent, vEvent);
    }
    /** Tests FREQ=YEARLY */
    @Test
    public void dailyWithException1ToString()
    {
        String expectedString = "BEGIN:VEVENT" + System.lineSeparator()
                + "DESCRIPTION:Daily2 Description" + System.lineSeparator()
                + "DTSTAMP:20150110T080000" + System.lineSeparator()
                + "DTSTART:20151109T100000" + System.lineSeparator()
                + "DURATION:PT1H30M" + System.lineSeparator()
                + "EXDATE:20151112T100000,20151115T100000" + System.lineSeparator()
                + "RRULE:FREQ=DAILY;INTERVAL=3;COUNT=6" + System.lineSeparator()
                + "SUMMARY:Daily2 Summary" + System.lineSeparator()
                + "UID:20150110T080000-0@jfxtras.org" + System.lineSeparator()
                + "X-APPOINTMENT-GROUP:group3" + System.lineSeparator()
                + "END:VEVENT";
        VEventImpl vEvent = VEventImpl.parseVEvent(expectedString);
        vEvent.getAppointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);
        VEvent expectedVEvent = getDailyWithException1();
        assertEquals(expectedVEvent, vEvent);
    }
    
    @Test
    public void canParseVDateTime()
    {
        VDateTime expectedDateTime = new VDateTime(LocalDateTime.of(2015, 11, 15, 10, 0));
        VDateTime dateTime = VDateTime.parseDateTime("20151115T100000");
//        System.out.println(expectedDateTime.getLocalDateTime() + " "+ dateTime.getLocalDateTime());
        assertEquals(expectedDateTime, dateTime);
    }
}
