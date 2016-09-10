package jfxtras.labs.icalendarfx.itip.publish;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

public class PublishTest
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
        main.processITIPMessage(inputVCalendar);
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
    
    @Test
    public void canReviseWithPublish()
    {
        throw new RuntimeException("not implemented");
    }
    
    @Test // the time has been changed, an end time has been added, and the sequence number has been adjusted.
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
        main.processITIPMessage(inputVCalendar);
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
    
    /* edits a repeatable event, with one recurrence, with ALL selection.
     * The edit deletes the recurrence and edits the repeatable event.
     */
    @Test
    public void canProcessPublishReplaceRepeatableAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        vComponents.addAll(vComponentEdited, vComponentRecurrence);
                
        // Publish change to ALL VEvents (recurrence gets deleted)
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151109T090000" + System.lineSeparator() +
              "DTEND:20151109T103000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:Edited summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "RRULE:FREQ=DAILY" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "SEQUENCE:1" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        String cancel = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "METHOD:CANCEL" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        
        mainVCalendar.processITIPMessage(VCalendar.parse(publish));
        mainVCalendar.processITIPMessage(VCalendar.parse(cancel));
        
        VCalendar expectedVCalendar = new VCalendar();        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        expectedVCalendar.addVComponent(expectedVComponent);
        
        assertEquals(expectedVCalendar, mainVCalendar);
    }
    
    /* edits a repeatable event, with one recurrence, with ALL-IGNORE-RECURRENCES selection.
     * Only edits the repeatable event.
     */
    @Test
    public void canProcessPublishReplaceRepeatableAllIgnoreRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2015, 11, 12, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2015, 11, 12, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2015, 11, 12, 9, 30));
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        vComponents.addAll(vComponentEdited, vComponentRecurrence);
                
        // Publish change to ALL VEvents (recurrence gets deleted)
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              // PARENT
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151109T090000" + System.lineSeparator() +
              "DTEND:20151109T103000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:Edited summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "RRULE:FREQ=DAILY" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "SEQUENCE:1" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              // RECURRENCE CHILD
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151112T083000" + System.lineSeparator() +
              "DTEND:20151112T093000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:recurrence summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "RECURRENCE-ID:20151112T090000" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        
        mainVCalendar.processITIPMessage(VCalendar.parse(publish));
        
        VCalendar expectedVCalendar = new VCalendar();        
        expectedVCalendar.addVComponent(new VEvent(vComponentRecurrence));
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        VEvent vComponentRecurrence2 = new VEvent(vComponentRecurrence)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 12, 9, 0));
        expectedVCalendar.addAllVComponents(expectedVComponent, vComponentRecurrence2);
        assertEquals(expectedVCalendar, mainVCalendar);        
    }
   
    /* edits a repeatable event, with one recurrence, with THIS-AND-FUTURE selection.
     * The edit deletes the recurrence and edits the repeatable event.
     */
    @Test
    public void canProcessPublishReplaceThisAndFutureRepeatable()
    {
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        vComponents.addAll(vComponentEdited, vComponentRecurrence);
        
        // MODIFY ORIGINAL WITH UNTIL DATE
        // ADD NEW VEVENT STARTING ON EDITED RECURRENCE
        // CANCEL RECURRENCE
//        throw new RuntimeException("not implemented");
        
        // Publish change to ALL VEvents (recurrence gets deleted)
        String publish = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
              "METHOD:PUBLISH" + System.lineSeparator() + 
              "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
              "VERSION:2.0" + System.lineSeparator() + 
              "BEGIN:VEVENT" + System.lineSeparator() +
              "CATEGORIES:group05" + System.lineSeparator() +
              "DTSTART:20151109T090000" + System.lineSeparator() +
              "DTEND:20151109T103000" + System.lineSeparator() +
              "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
              "SUMMARY:Edited summary" + System.lineSeparator() +
              "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
              "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
              "RRULE:FREQ=DAILY" + System.lineSeparator() +
              "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
              "SEQUENCE:1" + System.lineSeparator() +
              "END:VEVENT" + System.lineSeparator() + 
              "END:VCALENDAR");
        String cancel = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
                "METHOD:CANCEL" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        
        mainVCalendar.processITIPMessage(VCalendar.parse(publish));
        mainVCalendar.processITIPMessage(VCalendar.parse(cancel));
        
        VCalendar expectedVCalendar = new VCalendar();        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        expectedVCalendar.addVComponent(expectedVComponent);
        
        assertEquals(expectedVCalendar, mainVCalendar);
    }
}
