package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.SimpleVComponentFactory;

public class ParseCalendarTest
{
    @Test
    public void canParseVCalendar()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "VERSION:2.0" + System.lineSeparator() +
       "PRODID:-//hacksw/handcal//NONSGML v1.0//EN" + System.lineSeparator() +
       "BEGIN:VEVENT" + System.lineSeparator() +
       "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
       "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
       "DTSTART:19970714T170000Z" + System.lineSeparator() +
       "DTEND:19970715T040000Z" + System.lineSeparator() +
       "SUMMARY:Bastille Day Party" + System.lineSeparator() +
       "END:VEVENT" + System.lineSeparator() +
       "BEGIN:VEVENT" + System.lineSeparator() +
       "SUMMARY:New Event" + System.lineSeparator() +
       "CLASS:PUBLIC" + System.lineSeparator() +
       "DTSTART;TZID=Etc/GMT:20150902T133000Z" + System.lineSeparator() +
       "DTEND;TZID=Etc/GMT:20150902T180000Z" + System.lineSeparator() +
       "PRIORITY:0" + System.lineSeparator() +
       "SEQUENCE:1" + System.lineSeparator() +
       "STATUS:CANCELLED" + System.lineSeparator() +
       "UID:dc654e79-cc85-449c-a1b2-71b2d20b80df" + System.lineSeparator() +
       "RECURRENCE-ID;TZID=Etc/GMT:20150902T133000Z" + System.lineSeparator() +
       "DTSTAMP:20150831T053218Z" + System.lineSeparator() +
       "ORGANIZER;CN=David Bal;SENT-BY=\"mailto:ddbal1@yahoo.com\":mailto:ddbal1@yaho" + System.lineSeparator() +
       " o.com" + System.lineSeparator() +
       "X-YAHOO-YID:testuser" + System.lineSeparator() +
       "TRANSP:OPAQUE" + System.lineSeparator() +
       "X-YAHOO-USER-STATUS:BUSY" + System.lineSeparator() +
       "X-YAHOO-EVENT-STATUS:BUSY" + System.lineSeparator() +
       "BEGIN:VALARM" + System.lineSeparator() +
       "ACTION:DISPLAY" + System.lineSeparator() +
       "DESCRIPTION:" + System.lineSeparator() +
       "TRIGGER;RELATED=START:-PT30M" + System.lineSeparator() +
       "END:VALARM" + System.lineSeparator() +
       "END:VEVENT" + System.lineSeparator() +
       "END:VCALENDAR";
        
//        Iterator<String> iterator = Arrays.stream(content.split(System.lineSeparator())).iterator();

        VCalendar vCalendar = VCalendar.parse(content);
        
//        System.out.println(vCalendar.toContent());
//        System.out.println(content);
//        System.out.println(vCalendar.toContent());
        assertEquals(content, vCalendar.toContent());
//        VEventNew e = vCalendar.getVEvents().get(1);
//        e.getNonStandardProperties().stream().forEach(System.out::println);
    }
    
    @Test (expected = NullPointerException.class)
    public void canParseNullCalendar()
    {
        VCalendar.parse(null);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canParseInvalidCalendar1()
    {
        VCalendar.parse("invalid calendar");
    }
    
    @Test
    public void canParseInvalidCalendar2()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "Ignore this line" + System.lineSeparator() +       
       "END:VCALENDAR";
        VCalendar v = VCalendar.parse(content);
        assertEquals(29, v.toContent().length());
    }

    @Test
    public void canParseEmptyCalendar1()
    {
        SimpleVComponentFactory.emptyVComponent("VEVENT");
    }
    
    @Test
    public void canParseEmptyCalendar2()
    {
        String content = "BEGIN:VEVENT" + System.lineSeparator() +
        "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
        "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
        "DTSTART:19970714T170000Z" + System.lineSeparator() +
        "DTEND:19970715T040000Z" + System.lineSeparator() +
        "SUMMARY:Bastille Day Party" + System.lineSeparator() +
        "END:VEVENT";
        SimpleVComponentFactory.emptyVComponent(content);
    }
}
