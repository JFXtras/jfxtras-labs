package jfxtras.labs.icalendaragenda.editors.revise;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.ICalendarAgenda;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors.ReviserVEvent;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VPrimary;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

public class ReviseOneTest
{
    @Test
    public void canEditOneRecurrence()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();

        iTIPMessages.forEach(inputVCalendar -> mainVCalendar.processITIPMessage(inputVCalendar));
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        vComponents.forEach(System.out::println);
        VEvent myComponentIndividual = vComponents.get(1);
        
        String expectediTIPMessage =
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
                myComponentIndividual.getDateTimeStamp().toContent() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);

        // 2nd edit - edit component with RecurrenceID (individual)
        VEvent vComponentEditedIndividual = new VEvent(myComponentIndividual);
        VEvent vComponentIndividualCopy = new VEvent(myComponentIndividual);
        
        vComponentEditedIndividual.setSummary("new summary");
        Temporal startOriginalRecurrence2 = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal startRecurrence2 = LocalDateTime.of(2016, 5, 16, 12, 0);
        Temporal endRecurrence2 = LocalDateTime.of(2016, 5, 16, 13, 0);

        ReviserVEvent reviser2 = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentIndividualCopy))
                .withDialogCallback((m) -> null) // no dialog required
                .withEndRecurrence(endRecurrence2)
                .withStartOriginalRecurrence(startOriginalRecurrence2)
                .withStartRecurrence(startRecurrence2)
                .withVComponentEdited(vComponentEditedIndividual)
                .withVComponentOriginal(vComponentIndividualCopy);
        iTIPMessages = reviser2.revise();
        
        iTIPMessages.forEach(inputVCalendar -> mainVCalendar.processITIPMessage(inputVCalendar));
        
        expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:REQUEST" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART:20160516T120000" + System.lineSeparator() +
                "DTEND:20160516T130000" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:new summary" + System.lineSeparator() +
                myComponentIndividual.getDateTimeStamp().toContent() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:2" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
 
    @Test
    public void canChangOneWholeDayToTimeBased()
    {
        VCalendar mainVCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = mainVCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDate.of(2016, 5, 16);
        Temporal endRecurrence = LocalDate.of(2016, 5, 17);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        List<VCalendar> iTIPMessages = reviser.revise();
        
        iTIPMessages.forEach(inputVCalendar -> mainVCalendar.processITIPMessage(inputVCalendar));
        assertEquals(2, vComponents.size());
        Collections.sort(vComponents, VPrimary.VPRIMARY_COMPARATOR);
        VEvent individualComponent = vComponents.get(1);
        
        String expectediTIPMessage =
                "BEGIN:VCALENDAR" + System.lineSeparator() +
                "METHOD:PUBLISH" + System.lineSeparator() +
                "PRODID:" + ICalendarAgenda.PRODUCT_IDENTIFIER + System.lineSeparator() +
                "VERSION:" + Version.DEFAULT_ICALENDAR_SPECIFICATION_VERSION + System.lineSeparator() +
                "BEGIN:VEVENT" + System.lineSeparator() +
                "CATEGORIES:group05" + System.lineSeparator() +
                "DTSTART;VALUE=DATE:20160516" + System.lineSeparator() +
                "DTEND;VALUE=DATE:20160517" + System.lineSeparator() +
                "DESCRIPTION:Daily1 Description" + System.lineSeparator() +
                "SUMMARY:Edited summary" + System.lineSeparator() +
                individualComponent.getDateTimeStamp().toContent() + System.lineSeparator() +
                "UID:20150110T080000-004@jfxtras.org" + System.lineSeparator() +
                "ORGANIZER;CN=Papa Smurf:mailto:papa@smurf.org" + System.lineSeparator() +
                "RECURRENCE-ID:20160516T100000" + System.lineSeparator() +
                "SEQUENCE:1" + System.lineSeparator() +
                "END:VEVENT" + System.lineSeparator() +
                "END:VCALENDAR";
        String iTIPMessage = iTIPMessages.stream()
                .map(v -> v.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        assertEquals(expectediTIPMessage, iTIPMessage);
    }
}
