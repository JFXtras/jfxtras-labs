package jfxtras.labs.icalendaragenda.itip;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors.ReviserVEvent;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class ReviseWithRecurrencesTest
{
    @Test // edit ALL with 2 recurrences in date range
    public void canEditAllWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponents.add(vComponentOriginal);
        // make recurrence instances
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

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
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
//                "END:VCALENDAR" + System.lineSeparator() +
//                "BEGIN:VCALENDAR" + System.lineSeparator() +
//                "METHOD:CANCEL" + System.lineSeparator() +
//                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
//                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160517T083000" + System.lineSeparator() +
//                "DTEND:20160517T093000" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:recurrence summary" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "RECURRENCE-ID:20160517T100000" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
//                "BEGIN:VEVENT" + System.lineSeparator() +
//                "CATEGORIES:group05" + System.lineSeparator() +
//                "DTSTART:20160519T073000" + System.lineSeparator() +
//                "DTEND:20160519T083000" + System.lineSeparator() +
//                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
//                "SUMMARY:recurrence summary2" + System.lineSeparator() +
//                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
//                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
//                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
//                "RECURRENCE-ID:20160519T100000" + System.lineSeparator() +
//                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // edit ALL with a recurrence in date range
    public void canEditAllIgnoreRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        // make recurrence instances
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

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL_IGNORE_RECURRENCES)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> itipMessages = reviser.revise();

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
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
                "METHOD:PUBLISH" + System.lineSeparator() +
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
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = itipMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // with a recurrence in between new date range - remove special recurrence, replaces with normal recurrence
    public void canEditThisAndFutureWithRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);
        // make changes
        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();

        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        
        iTIPMessages.forEach(inputVCalendar -> mainVCalendar.processITIPMessage(inputVCalendar));
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent newVComponentFuture = vComponents.get(1);
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
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
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
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
                newVComponentFuture.getDateTimeStamp().toContent() + System.lineSeparator() +
                newVComponentFuture.getUniqueIdentifier().toContent() + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // with a recurrence in between new date range - special recurrence stays unmodified.
    public void canEditThisAndFutureAllIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make recurrence before
        VEvent vComponentRecurrenceBefore = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2015, 12, 25, 10, 0))
                .withSummary("recurrence summary before")
                .withDateTimeStart(LocalDateTime.of(2015, 12, 26, 0, 30))
                .withDateTimeEnd(LocalDateTime.of(2015, 12, 26, 2, 30));
        vComponents.add(vComponentRecurrenceBefore);
        
        // make recurrence after
        VEvent vComponentRecurrenceAfter = ICalendarStaticComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule2) null)
                .withRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0))
                .withSummary("recurrence summary after")
                .withDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30))
                .withDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrenceAfter);

        // make changes
        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE_IGNORE_RECURRENCES)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();

        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));

        iTIPMessages.forEach(inputVCalendar -> mainVCalendar.processITIPMessage(inputVCalendar));
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent newVComponentFuture = vComponents.get(2);

        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
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
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
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
                newVComponentFuture.getDateTimeStamp().toContent() + System.lineSeparator() +
                newVComponentFuture.getUniqueIdentifier().toContent() + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160517T083000" + System.lineSeparator() +
                "DTEND:20160517T093000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:recurrence summary after" + System.lineSeparator() +
                newVComponentFuture.getDateTimeStamp().toContent() + System.lineSeparator() +
                newVComponentFuture.getUniqueIdentifier().toContent() + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160517T090000" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        System.out.println(iTIPMessage);
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
    
    @Test // with a recurrence in between new date range, from whole-day to time-based - special recurrence stays unmodified.
    public void canEditWholeDayToTimeBasedThisAndFutureIgnoreRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getWholeDayDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDate.of(2016, 5, 17));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")));
        vComponentRecurrence.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")));
        vComponents.add(vComponentRecurrence);

        // make changes
        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDate.of(2016, 5, 15);
        Temporal startRecurrence = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 9, 0), ZoneId.of("Europe/London"));
        Temporal endRecurrence = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 30), ZoneId.of("Europe/London"));

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE_IGNORE_RECURRENCES)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));

        mainVCalendar.processITIPMessage(iTIPMessage);
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent newVComponentFuture = vComponents.get(1);
        VEvent newVComponentRecurrence = vComponents.get(2);
        
System.out.println(iTIPMessage);
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                "DTSTAMP:20150110T080000Z" + System.lineSeparator() +
                "UID:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "RRULE:FREQ=DAILY;UNTIL=20160514" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20151108" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20151109" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR" + System.lineSeparator() +
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                newVComponentFuture.getDateTimeStamp().toContent() + System.lineSeparator() +
                newVComponentFuture.getUniqueIdentifier().toContent() + System.lineSeparator() +
                "RRULE:FREQ=DAILY" + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160515T090000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160515T103000" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                "RELATED-TO:20150110T080000-010@jfxtras.org" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group06" + System.lineSeparator() +
                newVComponentRecurrence.getDateTimeStamp().toContent() + System.lineSeparator() +
                newVComponentRecurrence.getUniqueIdentifier().toContent() + System.lineSeparator() +
                "DTSTART;TZID=Europe/London:20160517T083000" + System.lineSeparator() +
                "ORGANIZER;CN=Issac Newton:mailto:isaac@greatscientists.org" + System.lineSeparator() +
                "DTEND;TZID=Europe/London:20160517T093000" + System.lineSeparator() +
                "RECURRENCE-ID;TZID=Europe/London:20160517T090000" + System.lineSeparator() +
                "SUMMARY:recurrence summary" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        assertEquals(expectediTIPMessage, iTIPMessage);
        
        assertEquals(3, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentOriginal = vComponents.get(0);
//        VEvent myComponentFuture = vComponents.get(1);
//        VEvent myComponentRecurrence = vComponents.get(2);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getWholeDayDaily1()
                .withSequence(1);
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDate.of(2016, 5, 14));
//        assertTrue(vComponentOriginalCopy == myComponentOriginal);
        assertEquals(expectedOriginalEdited, myComponentOriginal);

        VEvent expectedComponentFuture = ICalendarStaticComponents.getWholeDayDaily1();
        expectedComponentFuture.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 9, 0), ZoneId.of("Europe/London")));
        expectedComponentFuture.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 30), ZoneId.of("Europe/London")));
        expectedComponentFuture.setSummary("Edited summary");
        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        expectedComponentFuture.setRelatedTo(FXCollections.observableArrayList(relatedTo));
        expectedComponentFuture.setSequence(1);
        expectedComponentFuture.setUniqueIdentifier(new UniqueIdentifier(newVComponentFuture.getUniqueIdentifier()));
        expectedComponentFuture.setDateTimeStamp(new DateTimeStamp(newVComponentFuture.getDateTimeStamp()));
        assertEquals(expectedComponentFuture, newVComponentFuture);

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getWholeDayDaily1();
        expectedvComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        expectedvComponentRecurrence.setRecurrenceId(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 0), ZoneId.of("Europe/London")));
        expectedvComponentRecurrence.setSummary("recurrence summary");
        expectedvComponentRecurrence.setDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 8, 30), ZoneId.of("Europe/London")));
        expectedvComponentRecurrence.setDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2016, 5, 17, 9, 30), ZoneId.of("Europe/London")));
        expectedvComponentRecurrence.setUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()));
    }
}
