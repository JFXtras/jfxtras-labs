package jfxtras.labs.icalendarfx.method.publish;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;

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
    
    @Test // new DTSTART and SUMMARY - individual
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
    
    /* edits a repeatable event, with one recurrence, with ALL selection.
     * The edit deletes the recurrence and edits the repeatable event.
     * 
     */
    @Test
    public void canProcessPublishReplaceRepeatable()
    {
        VCalendar mainVCalendar = new VCalendar();
       
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
//        System.out.println(vComponentEdited.toContent());
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        vComponents.addAll(vComponentEdited, vComponentRecurrence);
        
//        System.out.println(mainVCalendar.toContent());
        
//        // make changes
//        vComponentEdited.setSummary("Edited summary");
//
//        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
//        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
//        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        
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
        
        mainVCalendar.processVCalendar(VCalendar.parse(publish));
        mainVCalendar.processVCalendar(VCalendar.parse(cancel));
        
//        System.out.println(mainVCalendar.toContent());
        
        VCalendar expectedVCalendar = new VCalendar();        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSummary("Edited summary")
                .withSequence(1)
                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        expectedVCalendar.addVComponent(expectedVComponent);
        
        assertEquals(expectedVCalendar, mainVCalendar);
        
        //        VCalendar inputVCalendar = VCalendar.parse(publish);
//        main.processVCalendar(inputVCalendar);
//        String expectedContent = new String("BEGIN:VCALENDAR" + System.lineSeparator() + 
//                "BEGIN:VEVENT" + System.lineSeparator() + 
//                "ORGANIZER:mailto:a@example.com" + System.lineSeparator() + 
//                "DTSTART:19970701T200000Z" + System.lineSeparator() + 
//                "DTSTAMP:19970611T190000Z" + System.lineSeparator() + 
//                "SUMMARY:ST. PAUL SAINTS -VS- DULUTH-SUPERIOR DUKES" + System.lineSeparator() + 
//                "UID:0981234-1234234-23@example.com" + System.lineSeparator() + 
//                "END:VEVENT" + System.lineSeparator() + 
//                "END:VCALENDAR");
//        assertEquals(expectedContent, main.toContent());
    }
    
//    @Test // with a recurrence in between new date range - removed special recurrence, replaces with normal recurrence
//    public void canEditThisAndFuture()
//    {
//        VCalendar vCalendar = new VCalendar();
//        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
//        
//        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
//        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
//        vComponents.add(vComponentEdited);
//        // make recurrence
//        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
//        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
//        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
//        vComponentRecurrence.setSummary("recurrence summary");
//        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
//        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
//        vComponents.add(vComponentRecurrence);
//
//        // make changes
//        vComponentEdited.setSummary("Edited summary");
//
//        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
//        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
//        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
//
//        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
//                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
//                .withEndRecurrence(endRecurrence)
//                .withStartOriginalRecurrence(startOriginalRecurrence)
//                .withStartRecurrence(startRecurrence)
//                .withVComponents(vComponents)
//                .withVComponentEdited(vComponentEdited)
//                .withVComponentOriginal(vComponentOriginalCopy);
//        reviser.revise();
//
//        assertEquals(2, vComponents.size());
//        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
//        VEvent myComponentFuture = vComponents.get(1);
//        VEvent myComponentOriginal = vComponents.get(0);
//        
//        VEvent expectedOriginalEdited = ICalendarStaticComponents.getDaily1();
//        expectedOriginalEdited.getRecurrenceRule().getValue()
//            .setUntil(LocalDateTime.of(2016, 5, 15, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
//        assertTrue(vComponentOriginalCopy == myComponentOriginal);
//        assertEquals(expectedOriginalEdited, myComponentOriginal);
//        
//        VEvent expectedComponentFuture = ICalendarStaticComponents.getDaily1();
//        expectedComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
//        expectedComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30));
//        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
//        expectedComponentFuture.setRelatedTo(FXCollections.observableArrayList(relatedTo));
//        expectedComponentFuture.setSummary("Edited summary");
//        expectedComponentFuture.setSequence(1);
//        expectedComponentFuture.setUniqueIdentifier(new UniqueIdentifier(myComponentFuture.getUniqueIdentifier()));
//        expectedComponentFuture.setDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()));
//      
}
