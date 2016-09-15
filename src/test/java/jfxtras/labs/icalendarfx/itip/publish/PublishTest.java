package jfxtras.labs.icalendarfx.itip.publish;

import static org.junit.Assert.assertEquals;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VPrimary;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

/**
 * Tests to demonstrate PUBLISH iTIP message ability
 * 
 * @author David Bal
 *
 */
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
        VCalendar mainVCalendar = new VCalendar();
        VEvent vComponent = ICalendarStaticComponents.getDaily1();
        mainVCalendar.addVComponent(vComponent);
        String publish = new String(
                "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "METHOD:PUBLISH" + System.lineSeparator() + 
                "PRODID:-//Example/ExampleCalendarClient//EN" + System.lineSeparator() + 
                "VERSION:2.0" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151108T100000" + System.lineSeparator() +
                "DTEND:20151108T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:revised summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        mainVCalendar.processITIPMessage(publish);
        
        String expectedContent = new String(
                "BEGIN:VCALENDAR" + System.lineSeparator() + 
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151108T100000" + System.lineSeparator() +
                "DTEND:20151108T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:revised summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() + 
                "END:VCALENDAR");
        assertEquals(expectedContent, mainVCalendar.toContent());
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
    
    @Test // edit an individual recurrence of a repeatable event twice
    public void canEditOneRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        System.out.println(vComponentOriginal);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T151835Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
//        System.out.println(iTIPMessage);
        
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent myComponentRepeats = vComponents.get(0);
        
        assertEquals(vComponentOriginal, myComponentRepeats);
        VEvent myComponentIndividual = vComponents.get(1);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentIndividual.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentIndividual.getDateTimeEnd().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentRepeats.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentRepeats.getDateTimeEnd().getValue()); 
        assertEquals("Edited summary", myComponentIndividual.getSummary().getValue());
                
        // Check child components
        assertEquals(Arrays.asList(myComponentIndividual), myComponentRepeats.recurrenceChildren());
        assertEquals(Collections.emptyList(), myComponentIndividual.recurrenceChildren());

        // 2nd edit - edit component with RecurrenceID (individual)       
        String iTIPMessage2 =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T120000" + System.lineSeparator() +
                "DTEND:20160516T130000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                "DTSTAMP:20160914T155333Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:2" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";        
        mainVCalendar.processITIPMessage(iTIPMessage2);

        assertEquals(2, vComponents.size());
        
        // Check child components
        VEvent myComponentIndividual2 = vComponents.get(1);
        assertEquals(Arrays.asList(myComponentIndividual2), myComponentRepeats.recurrenceChildren());
        assertEquals("new summary", myComponentIndividual2.getSummary().getValue());
        assertEquals(Collections.emptyList(), myComponentIndividual2.recurrenceChildren());
//        System.out.println(mainVCalendar);
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
   
    @Test // divides one repeatable event into two.  First one ends with UNTIL
    public void canEditThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T173109Z" + System.lineSeparator() +
                "UID:20160914T103109-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515T170000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentFuture.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponentFuture.getSummary().getValue());
        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentOriginal.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentOriginal.getDateTimeEnd().getValue()); 
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        RecurrenceRule2 expectedRRule = ICalendarStaticComponents.getDaily1().getRecurrenceRule().getValue().withUntil(until);
        assertEquals(expectedRRule, myComponentOriginal.getRecurrenceRule().getValue());
    }
    
    @Test // change INTERVAL
    public void canEditThisAndFuture2()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);

        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "DTEND:20160516T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20160914T180627Z" + System.lineSeparator() +
                "UID:20160914T110627-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;INTERVAL=2" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T100000" + System.lineSeparator() +
                "DTEND:20151109T110000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515T170000Z" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
                
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentFuture.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponentFuture.getSummary().getValue());
        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentOriginal.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentOriginal.getDateTimeEnd().getValue()); 
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        RecurrenceRule2 expectedRRule = ICalendarStaticComponents.getDaily1().getRecurrenceRule().getValue().withUntil(until);
        assertEquals(expectedRRule, myComponentOriginal.getRecurrenceRule().getValue());
    }
    
    @Test
    public void canChangeToWholeDayAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDate.of(2015, 11, 8), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2015, 11, 9), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canChangeWholeDayToTimeBasedThisAndFuture()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponents.add(vComponentEdited);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20160914T200517Z" + System.lineSeparator() +
                "UID:20160914T130517-0jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART:20160516T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND:20160516T100000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160515" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);


        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent newVComponentOriginal = vComponents.get(0);
        VEvent newVComponentFuture = vComponents.get(1);

        VEvent expectedVComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        RecurrenceRule2 rrule = expectedVComponentOriginal.getRecurrenceRule().getValue();
        rrule.setUntil(LocalDate.of(2016, 5, 15));
        
        assertEquals(expectedVComponentOriginal, newVComponentOriginal);

        VEvent expectedVComponentFuture = ICalendarStaticComponents.getWholeDayDaily1();
        expectedVComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
        expectedVComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 0));
        expectedVComponentFuture.setUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()));
        expectedVComponentFuture.setDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));
        expectedVComponentFuture.setSummary("Edited summary");
        expectedVComponentFuture.withRelatedTo(vComponentOriginal.getUniqueIdentifier().getValue());
        expectedVComponentFuture.setSequence(1);
        
        assertEquals(expectedVComponentFuture, newVComponentFuture);
    }
    
    @Test
    public void canAddRRuleToAll()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividualZoned();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponents.add(vComponentEdited);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "CATEGORIES:group13" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20151113T090000" + System.lineSeparator() + // one hour earlier
                "DTEND;TZID=Europe/London:20151113T100000" + System.lineSeparator() + // one hour earlier
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-009@jfxtras.org" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RRULE:FREQ=WEEKLY;BYDAY=MO,WE,FR" + System.lineSeparator() + // added RRULE
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 9, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeStart().getValue());        
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeEnd().getValue());
        RecurrenceRule2 r = new RecurrenceRule2()
                .withFrequency(FrequencyType.WEEKLY)
                .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
        assertEquals(r, myComponent.getRecurrenceRule().getValue());
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test // edit ALL with 2 recurrences in date range
    public void canEditAllWithRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrences
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence2);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:CANCEL" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
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
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160519T073000" + System.lineSeparator() +
                "DTEND:20160519T083000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary2" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160519T100000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        mainVCalendar.processITIPMessage(iTIPMessage);
        
        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
    }
    
    @Test // edit ALL and ignore 2 recurrences in date range - tests changing Recurrence-ID of recurrences to match parent's change
    public void canEditAllIgnoreRecurrences()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrences
        VEvent vComponentRecurrence2 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence2);
        
        VEvent vComponentRecurrence3 = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 10, 0))
                .withSummary("recurrence summary2")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 19, 7, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 19, 8, 30));
        vComponents.add(vComponentRecurrence3);
        
        String iTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20151109T090000" + System.lineSeparator() +
                "DTEND:20151109T103000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Daily1 Summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160519T073000" + System.lineSeparator() +
                "DTEND:20160519T083000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary2" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160519T090000" + System.lineSeparator() +
                "END:VEVENT";

        mainVCalendar.processITIPMessage(iTIPMessage);
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        assertEquals(3, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        VEvent myComponent2 = vComponents.get(1);
        VEvent myComponent3 = vComponents.get(2);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
        
        VEvent expectedComponent2 = new VEvent(vComponentRecurrence2)
                .withSummary("recurrence summary")
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 9, 0))
                .withDateTimeStart("20160517T083000")
                .withDateTimeEnd("20160517T093000");
        assertEquals(expectedComponent2, myComponent2);
        
        VEvent expectedComponent3 = new VEvent(vComponentRecurrence3)
                .withSummary("recurrence summary2")
                .withRecurrenceId(LocalDateTime.of(2016, 5, 19, 9, 0))
                .withDateTimeStart("20160519T073000")
                .withDateTimeEnd("20160519T083000");
        assertEquals(expectedComponent3, myComponent3);
    }
    
    /* edits a repeatable event, with one recurrence, with THIS-AND-FUTURE selection.
     * The edit deletes the recurrence and edits the repeatable event.
     */
    // TODO - DOESN'T WORK - needs to split event into 2 - one with until, other one for future
    @Test
    @Ignore
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
