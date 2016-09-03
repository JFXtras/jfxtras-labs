package jfxtras.labs.icalendarfx.calendar.method.publish;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import jfxtras.labs.icalendarfx.VCalendar;

public class ProcessPublishTest
{
    @Test
    public void canProcessPublish()
    {
        VCalendar main = new VCalendar();
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() + 
              "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
              "DTSTART:19970701T200000Z" + System.lineSeparator() + 
              "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
              "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
              "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        VCalendar inputVCalendar = VCalendar.parse(publish);
        main.processVCalendar(inputVCalendar);
        String expectedContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970701T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        assertEquals(expectedContent, main.toContent());
    }
    
    @Test // new DTSTART and SUMMARY
    public void canProcessPublishToReplace()
    {
        String mainContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970705T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        VCalendar main = VCalendar.parse(mainContent);
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() + 
              "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
              "DTSTART:19970701T200000Z" + System.lineSeparator() + 
              "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
              "DTEND:19970701T230000Z" + System.lineSeparator() + 
              "SEQUENCE:1" + System.lineSeparator() + 
              "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
              "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        VCalendar inputVCalendar = VCalendar.parse(publish);
        main.processVCalendar(inputVCalendar);
        String expectedContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() + 
                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
                "DTSTART:19970701T200000Z" + System.lineSeparator() + 
                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
                "DTEND:19970701T230000Z" + System.lineSeparator() + 
                "SEQUENCE:1" + System.lineSeparator() + 
                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        assertEquals(expectedContent, main.toContent());
    }
}
