package jfxtras.labs.icalendarfx.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

public class ParseCalendarTest extends ICalendarTestAbstract
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
    
    
    @Test
    public void canParseGoogleRepeatable()
    {
        String vEventString1 = "BEGIN:VEVENT" + System.lineSeparator()
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
    
        String vEventString2 = "BEGIN:VEVENT" + System.lineSeparator()
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
    
        String vEventString3 = "BEGIN:VEVENT" + System.lineSeparator()
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
        
        VCalendar c = new VCalendar();
        c.importVComponent(vEventString1);
        c.importVComponent(vEventString2);
        c.importVComponent(vEventString3);
        assertEquals(3, c.getVEvents().size());
        assertEquals(1, c.getVEvents().get(0).recurrenceChildren().size());
        assertEquals(c.getVEvents().get(0), c.getVEvents().get(2).recurrenceParent());
        assertTrue(c.isValid());
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
    public void canIgnoreUnrecognizedLine()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "Ignore this line" + System.lineSeparator() +       
       "END:VCALENDAR";
        VCalendar v = VCalendar.parse(content);
        VCalendar expected = new VCalendar();
        assertEquals(expected, v);
        assertEquals(29, v.toContent().length());
    }
    
    @Test // has errors
    public void canParseBadVCalendar1()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "VERSION:2.0" + System.lineSeparator() +
       "PRODID:-//hacksw/handcal//NONSGML v1.0//EN" + System.lineSeparator() +
       "BEGIN:VEVENT" + System.lineSeparator() +
       "UID:19970610T172345Z-AF23B2@example.com" + System.lineSeparator() +
       "DTSTAMP:19970610T172345Z" + System.lineSeparator() +
       "DTSTART:19970714T170000Z" + System.lineSeparator() +
//       "DTSTART:19970714T170000Z" + System.lineSeparator() +
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
       "STATUS:CANCELLED" + System.lineSeparator() +
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
        
        VCalendar vCalendar = new VCalendar();
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        List<String> errors = vCalendar.parseContent(unfoldedLines, true);
        assertEquals(2, errors.size());
//        errors.forEach(System.out::println);
//        assertEquals(content, vCalendar.toContent());
    }
    
    @Test
    public void canIgnoreBadLine()
    {
            String content = "BEGIN:VCALENDAR" + System.lineSeparator() +
            "X-CUSTOM-PROP:THE DATA" + System.lineSeparator() +
            "IGNORE THIS LINE" + System.lineSeparator() +
            "END:VCALENDAR";
            VCalendar v = VCalendar.parse(content);            
            VCalendar expected = new VCalendar()
                    .withNonStandard("X-CUSTOM-PROP:THE DATA")
                    ;
            assertEquals(expected, v);
    }
    
    @Test
    public void canGetErrorMessageFromBadLine()
    {
            String content = "BEGIN:VCALENDAR" + System.lineSeparator() +
            "X-CUSTOM-PROP:THE DATA" + System.lineSeparator() +
            "IGNORE THIS LINE" + System.lineSeparator() +
            "UNKNOWN-PROP:SOMETHING" + System.lineSeparator() +
            "END:VCALENDAR";
            VCalendar v = new VCalendar();
            List<String> messages = v.parseContent(content);
            List<String> expectedMessages = Arrays.asList("Unknown line is ignored:IGNORE THIS LINE", "Unknown property is ignored:UNKNOWN-PROP:SOMETHING");
            assertEquals(expectedMessages, messages);
    }
    
    @Test (expected = IllegalArgumentException.class)
    public void canHandleInvalidCalendarProperty()
    {
        String content = 
       "BEGIN:VCALENDAR" + System.lineSeparator() +
       "CALSCALE:INVALID" + System.lineSeparator() +
       "END:VCALENDAR";

        VCalendar.parse(content);
    }
}
