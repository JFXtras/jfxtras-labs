package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVEvent;
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

        ReviserVEvent reviser = ((ReviserVEvent) vComponentOriginalCopy.newRevisor())
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
//        Collection<VEvent> newVComponents = ReviseComponentHelper.handleEdit(
//                vComponentOriginalCopy,
//                vComponentEdited,
//                startOriginalRecurrence,
//                startRecurrence,
//                endRecurrence,
////                shift,
//                (m) -> ChangeDialogOption.ALL);
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(vComponentEdited, myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 30), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());        
    }
    
//    @Test
//    public void canEditOne()
//    {
//        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
//        List<String> changes = new ArrayList<>();
//        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
//        {
//            while (change.next())
//            {
//                if (change.wasAdded())
//                {
//                    change.getAddedSubList().forEach(c -> changes.add("Added:" + c.getSummary().getValue()));
//                    // Note: making recurrences should be done here by a similar listener in production code
//                }
//            }
//        };
//        vComponents.addListener(listener);
//        
//        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
//        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
//        vComponentEdited.setSummary("Edited summary");
//
//        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
//        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
//        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
////        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);
//
//        Collection<VEvent> newVComponents = ReviseComponentHelper.handleEdit(
//                vComponentOriginalCopy,
//                vComponentEdited,
//                startOriginalRecurrence,
//                startRecurrence,
//                endRecurrence,
////                shift,
//                (m) -> ChangeDialogOption.ONE);
//        vComponents.addAll(newVComponents);
//
//        System.out.println(vComponentEdited.toContent());
//        System.out.println(vComponentOriginalCopy.toContent());
//
//        assertEquals(2, vComponents.size());
//        VEvent myComponentRepeats = vComponents.get(0);
//        assertEquals(vComponentOriginalCopy, myComponentRepeats);
//        VEvent myComponentIndividual = vComponents.get(1);
//        assertEquals(vComponentEdited, myComponentIndividual);
//        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentIndividual.getDateTimeStart().getValue());        
//        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentIndividual.getDateTimeEnd().getValue());        
//        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentRepeats.getDateTimeStart().getValue());        
//        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentRepeats.getDateTimeEnd().getValue()); 
//        assertEquals("Edited summary", myComponentIndividual.getSummary().getValue());
//
//        List<String> expectedChanges = Arrays.asList(
//                "Added:Daily1 Summary",
//                "Added:Edited summary"
//                );
//        assertEquals(expectedChanges, changes);
//    }
    
    @Test
    public void canEditOne()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
//        List<String> changes = new ArrayList<>();
//        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
//        {
//            while (change.next())
//            {
//                if (change.wasAdded())
//                {
//                    change.getAddedSubList().forEach(c -> changes.add("Added:" + c.getSummary().getValue()));
//                    // Note: making recurrences should be done here by a similar listener in production code
//                }
//            }
//        };
//        vComponents.addListener(listener);
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
//        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        ReviserVEvent reviser = ((ReviserVEvent) vComponentOriginalCopy.newRevisor())
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents);

        System.out.println(vComponentEdited.toContent());
        System.out.println(vComponentOriginalCopy.toContent());

        assertEquals(2, vComponents.size());
        VEvent myComponentRepeats = vComponents.get(0);
        assertEquals(vComponentOriginalCopy, myComponentRepeats);
        VEvent myComponentIndividual = vComponents.get(1);
        assertEquals(vComponentEdited, myComponentIndividual);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentIndividual.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentIndividual.getDateTimeEnd().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentRepeats.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentRepeats.getDateTimeEnd().getValue()); 
        assertEquals("Edited summary", myComponentIndividual.getSummary().getValue());
        vComponents.stream().forEach(v -> System.out.println(v.childComponents().size()));
        assertEquals(1, myComponentRepeats.childComponents().size());

//        List<String> expectedChanges = Arrays.asList(
//                "Added:Daily1 Summary",
//                "Added:Edited summary"
//                );
//        assertEquals(expectedChanges, changes);
        vComponents.stream().forEach(v -> System.out.println(v.childComponents().size()));

        
        // edit individual event (one with RecurrenceID)        
        Temporal startOriginalRecurrence2 = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal startRecurrence2 = LocalDateTime.of(2016, 5, 16, 12, 0);
        Temporal endRecurrence2 = LocalDateTime.of(2016, 5, 16, 13, 0);

        VEvent vComponentEditedCopy = new VEvent(vComponentEdited);
        ReviserVEvent reviser2 = ((ReviserVEvent) vComponentOriginalCopy.newRevisor())
                .withDialogCallback((m) -> null) // no dialog required
                .withEndRecurrence(endRecurrence2)
                .withStartOriginalRecurrence(startOriginalRecurrence2)
                .withStartRecurrence(startRecurrence2)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentEditedCopy);
        Collection<VEvent> newVComponents2 = reviser2.revise();
        vComponents.remove(vComponentEdited);
        vComponents.addAll(newVComponents2);
        assertEquals(2, vComponents.size());
        vComponents.stream().forEach(v -> System.out.println(v.childComponents().size()));
        assertEquals(1, myComponentRepeats.childComponents().size());
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
        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        ReviserVEvent reviser = ((ReviserVEvent) vComponentOriginalCopy.newRevisor())
                .withDialogCallback((m) -> ChangeDialogOption.CANCEL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
        
//        Collection<VEvent> newVComponents = ReviseComponentHelper.handleEdit(
//                vComponentOriginalCopy,
//                vComponentEdited,
//                startOriginalRecurrence,
//                startRecurrence,
//                endRecurrence,
////                shift,
//                (m) -> ChangeDialogOption.CANCEL);
        assertNull(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertTrue(vComponentEdited == myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Daily1 Summary", myComponent.getSummary().getValue());        
    }
    
    @Test
    public void canEditThisAndFuture()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        List<String> changes = new ArrayList<>();
        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(c -> changes.add("Added:" + c.getSummary().getValue()));
                    // Note: making recurrences should be done here by a similar listener in production code
                }
            }
        };
        vComponents.addListener(listener);
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponentEdited.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
//        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        ReviserVEvent reviser = ((ReviserVEvent) vComponentOriginalCopy.newRevisor())
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        Collection<VEvent> newVComponents = reviser.revise();
//        Collection<VEvent> newVComponents = ReviseComponentHelper.handleEdit(
//                vComponentOriginalCopy,
//                vComponentEdited,
//                startOriginalRecurrence,
//                startRecurrence,
//                endRecurrence,
////                shift,
//                (m) -> ChangeDialogOption.THIS_AND_FUTURE);
        vComponents.addAll(newVComponents);

        assertEquals(2, vComponents.size());
        VEvent myComponentFuture = vComponents.get(1);
        assertTrue(vComponentEdited == myComponentFuture);
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

        List<String> expectedChanges = Arrays.asList(
                "Added:Daily1 Summary",
                "Added:Edited summary"
                );
        assertEquals(expectedChanges, changes);
    }
    
    @Test
    public void canAddRRuleToAll()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        List<String> changes = new ArrayList<>();
        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(c -> changes.add("Added:" + c.getSummary().getValue()));
                    // Note: making recurrences should be done here by a similar listener in production code
                }
            }
        };
        vComponents.addListener(listener);
        
        VEvent vComponentOriginal = ICalendarStaticComponents.getIndividual1();
        VEvent vComponentEdited = new VEvent(vComponentOriginal);
        vComponentEdited.setSummary("Edited summary");
        vComponentEdited.setRecurrenceRule(new RecurrenceRule2()
                        .withFrequency(FrequencyType.WEEKLY)
                        .withByRules(new ByDay(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY)));

        Temporal startOriginalRecurrence = LocalDateTime.of(2015, 11, 11, 10, 30);
        Temporal startRecurrence = LocalDateTime.of(2015, 11, 13, 10, 30);
        Temporal endRecurrence = LocalDateTime.of(2015, 11, 13, 11, 30);
//        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        ReviserVEvent reviser = ((ReviserVEvent) vComponentOriginal.newRevisor())
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginal);
        Collection<VEvent> newVComponents = reviser.revise();
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(vComponentEdited, myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 13, 10, 30), myComponent.getDateTimeStart().getValue());        
        assertEquals(Duration.ofHours(1), myComponent.getDuration().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());        
    }
}
