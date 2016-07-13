package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVEvent;
import jfxtras.labs.icalendarfx.components.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.byxxx.ByDay;

/**
 * Tests editing and deleting components.  Uses a stub for the dialog callback to designate
 * the scope of the change - ONE, ALL or THIS_AND_FUTURE
 * 
 * @author David Bal
 *
 */
public class ReviseComponentTest
{
    @Test
    public void canEditAll()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
//        vComponents.add(vComponentOriginal);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
//        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 30), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());        
    }
    
    @Test
    public void canEditOne()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
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
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent myComponentRepeats = vComponents.get(0);
        assertEquals(vComponentOriginalCopy, myComponentRepeats);
        VEvent myComponentIndividual = vComponents.get(1);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentIndividual.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentIndividual.getDateTimeEnd().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentRepeats.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentRepeats.getDateTimeEnd().getValue()); 
        assertEquals("Edited summary", myComponentIndividual.getSummary().getValue());
                
        // Check child components
        assertEquals(Arrays.asList(myComponentIndividual), myComponentRepeats.childComponents());
        assertEquals(Collections.emptyList(), myComponentIndividual.childComponents());

        // 2nd edit - edit component with RecurrenceID (individual)
        Temporal startOriginalRecurrence2 = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal startRecurrence2 = LocalDateTime.of(2016, 5, 16, 12, 0);
        Temporal endRecurrence2 = LocalDateTime.of(2016, 5, 16, 13, 0);

        VEvent vComponentEditedCopy = new VEvent(myComponentIndividual);
        ReviserVEvent reviser2 = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEditedCopy))
                .withDialogCallback((m) -> null) // no dialog required
                .withEndRecurrence(endRecurrence2)
                .withStartOriginalRecurrence(startOriginalRecurrence2)
                .withStartRecurrence(startRecurrence2)
                .withVComponentEdited(myComponentIndividual)
                .withVComponentOriginal(vComponentEditedCopy);
        Collection<VEvent> newVComponents2 = reviser2.revise();
        vComponents.remove(vComponentEditedCopy);
        vComponents.addAll(newVComponents2);
        assertEquals(2, vComponents.size());
        
        // Check child components
        VEvent myComponentIndividual2 = vComponents.get(1);
        assertEquals(Arrays.asList(myComponentIndividual2), myComponentRepeats.childComponents());
        assertEquals(Collections.emptyList(), myComponentIndividual2.childComponents());
    }
    
    @Test
    public void canEditCancel()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentOriginalCopy.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.CANCEL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        
        assertNull(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertTrue(vComponentEdited == myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Daily1 Summary", myComponent.getSummary().getValue());        
    }
    
    @Test // change date and time
    public void canEditThisAndFuture()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
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
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertTrue(vComponentOriginalCopy == myComponentOriginal);
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
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");
        vComponentEdited.getRecurrenceRule().getValue().setInterval(2);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertTrue(vComponentOriginalCopy == myComponentOriginal);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentFuture.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponentFuture.getSummary().getValue());
        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentOriginal.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentOriginal.getDateTimeEnd().getValue()); 
        Temporal until = ZonedDateTime.of(LocalDateTime.of(2016, 5, 15, 10, 0), ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z"));
        RecurrenceRule2 expectedRRule = ICalendarStaticComponents.getDaily1().getRecurrenceRule().getValue().withUntil(until);
        assertEquals(expectedRRule, myComponentOriginal.getRecurrenceRule().getValue());
    }
    
    @Test // change INTERVAL too
    public void canChangeToWholeDayThisAndFuture()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");
        vComponentEdited.getRecurrenceRule().getValue().setInterval(2);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDate.of(2016, 5, 16);
        Temporal endRecurrence = LocalDate.of(2016, 5, 17);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        assertTrue(vComponentOriginalCopy == myComponentOriginal);
        assertEquals(LocalDate.of(2016, 5, 16), myComponentFuture.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2016, 5, 17), myComponentFuture.getDateTimeEnd().getValue());        
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
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDate.of(2016, 5, 15); // shifts back 1 day
        Temporal endRecurrence = LocalDate.of(2016, 5, 16);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDate.of(2015, 11, 8), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2015, 11, 9), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canChangeWholeDayOne()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentOriginalCopy = new VEvent(vComponentOriginal);
        vComponentOriginal.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDate.of(2016, 5, 16);
        Temporal endRecurrence = LocalDate.of(2016, 5, 17);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentOriginal)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentOriginal);
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent repeatComponent = vComponents.get(0);
        VEvent individualComponent = vComponents.get(1);
        assertEquals(LocalDate.of(2016, 5, 16), individualComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDate.of(2016, 5, 17), individualComponent.getDateTimeEnd().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 0), individualComponent.getRecurrenceId().getValue());        
        assertEquals("Edited summary", individualComponent.getSummary().getValue());
        assertEquals(repeatComponent, vComponentOriginalCopy);
        assertNull(individualComponent.getRecurrenceRule());
    }
    
    @Test
    public void canAddRRuleToAll()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividualZoned();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        
        vComponentEdited.setSummary("Edited summary");
        vComponentEdited.setRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        Temporal startOriginalRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.of("Europe/London"));
        Temporal startRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 9, 0), ZoneId.of("Europe/London"));
        Temporal endRecurrence = ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London"));

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginal))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentOriginal);
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 9, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeStart().getValue());        
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("Europe/London")), myComponent.getDateTimeEnd().getValue());    
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canEditIndividual()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginal))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentOriginal);
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(LocalDateTime.of(2016, 5, 16, 11, 30), myComponent.getDateTimeStart().getValue());        
        assertEquals(Duration.ofMinutes(60), myComponent.getDuration().getValue());    
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
    
    @Test
    public void canEditIndividual2() // with other components present
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        vComponents.add(vComponentOriginal);
        vComponents.add(ICalendarStaticComponents.getDaily1());
        VEvent vComponentEdited = new VEvent(vComponentOriginal);

        vComponentEdited.setSummary("Edited summary");
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 11, 30);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 12, 30);
        
        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponentOriginal))
                .withDialogCallback((m) -> null) // no dialog for edit individual
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentOriginal);
        vComponents.addAll(newVComponents);
        assertEquals(2, vComponents.size());
        VEvent myComponent = vComponents.get(1);
        assertEquals(LocalDateTime.of(2016, 5, 16, 11, 30), myComponent.getDateTimeStart().getValue());        
        assertEquals(Duration.ofMinutes(60), myComponent.getDuration().getValue());    
        assertEquals("Edited summary", myComponent.getSummary().getValue());
    }
}
