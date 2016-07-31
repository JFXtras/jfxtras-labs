package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.Temporal;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.ICalendarTestAbstract;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.deleters.DeleterVEvent;
import jfxtras.labs.icalendarfx.components.deleters.SimpleDeleterFactory;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.revisors.ReviserVEvent;
import jfxtras.labs.icalendarfx.components.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.FrequencyType;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule2;
import jfxtras.labs.icalendarfx.properties.component.relationship.RelatedTo;
import jfxtras.labs.icalendarfx.properties.component.relationship.UniqueIdentifier;

public class RecurrenceIDComponentEditTest
{
    @Test // with a recurrence in between new date range - removed special recurrence, replaces with normal recurrence
    public void canEditThisAndFuture()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponents.add(vComponentEdited);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
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
                .withVComponents(vComponents)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        reviser.revise();

        assertEquals(2, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentOriginal = vComponents.get(0);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getDaily1();
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDateTime.of(2016, 5, 15, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        assertTrue(vComponentOriginalCopy == myComponentOriginal);
        assertEquals(expectedOriginalEdited, myComponentOriginal);
        
        VEvent expectedComponentFuture = ICalendarStaticComponents.getDaily1();
        expectedComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
        expectedComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30));
        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        expectedComponentFuture.setRelatedTo(FXCollections.observableArrayList(relatedTo));
        expectedComponentFuture.setSummary("Edited summary");
        expectedComponentFuture.setSequence(1);
        expectedComponentFuture.setUniqueIdentifier(new UniqueIdentifier(myComponentFuture.getUniqueIdentifier()));
        expectedComponentFuture.setDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()));
        assertEquals(expectedComponentFuture, myComponentFuture);
    }
    
    @Test // with a recurrence in between new date range - special recurrence stays unmodified.
    public void canEditThisAndFutureAllIgnoreRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        vComponents.add(vComponentEdited);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

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
                .withVComponents(vComponents)
                .withVComponentEdited(vComponentEdited)
                .withVComponentOriginal(vComponentOriginalCopy);
        reviser.revise();

        assertEquals(3, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponentOriginal = vComponents.get(0);
        VEvent myComponentFuture = vComponents.get(1);
        VEvent myComponentRecurrence = vComponents.get(2);
        
        VEvent expectedOriginalEdited = ICalendarStaticComponents.getDaily1();
        expectedOriginalEdited.getRecurrenceRule().getValue()
            .setUntil(LocalDateTime.of(2016, 5, 15, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        assertTrue(vComponentOriginalCopy == myComponentOriginal);
        assertEquals(expectedOriginalEdited, myComponentOriginal);

        VEvent expectedComponentFuture = ICalendarStaticComponents.getDaily1();
        expectedComponentFuture.setDateTimeStart(LocalDateTime.of(2016, 5, 16, 9, 0));
        expectedComponentFuture.setDateTimeEnd(LocalDateTime.of(2016, 5, 16, 10, 30));
        RelatedTo relatedTo = RelatedTo.parse(vComponentEdited.getUniqueIdentifier().getValue());
        expectedComponentFuture.setRelatedTo(FXCollections.observableArrayList(relatedTo));
        expectedComponentFuture.setSummary("Edited summary");
        expectedComponentFuture.setSequence(1);
        expectedComponentFuture.setUniqueIdentifier(new UniqueIdentifier(myComponentFuture.getUniqueIdentifier()));
        expectedComponentFuture.setDateTimeStamp(new DateTimeStamp(myComponentFuture.getDateTimeStamp()));
        assertEquals(expectedComponentFuture, myComponentFuture);

        VEvent expectedvComponentRecurrence = ICalendarStaticComponents.getDaily1();
        expectedvComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        expectedvComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 9, 0));
        expectedvComponentRecurrence.setSummary("recurrence summary");
        expectedvComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        expectedvComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        expectedvComponentRecurrence.setUniqueIdentifier(new UniqueIdentifier(expectedComponentFuture.getUniqueIdentifier()));
        assertEquals(expectedvComponentRecurrence, myComponentRecurrence);
    }
    
    @Test // edit ALL with a recurrence in date range
    public void canEditAll()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        VEvent vComponent1Copy = new VEvent(vComponent1);
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponent1))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponents(vComponents)
                .withVComponentEdited(vComponent1)
                .withVComponentOriginal(vComponent1Copy);
        reviser.revise();

        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
    }
    
    @Test // edit ALL with a recurrence in date range
    public void canEditAllIgnoreRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        VEvent vComponent1Copy = new VEvent(vComponent1);
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviserVEvent reviser = ((ReviserVEvent) SimpleRevisorFactory.newReviser(vComponent1))
                .withDialogCallback((m) -> ChangeDialogOption.ALL_IGNORE_RECURRENCES)
                .withEndRecurrence(endRecurrence)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withStartRecurrence(startRecurrence)
                .withVComponents(vComponents)
                .withVComponentEdited(vComponent1)
                .withVComponentOriginal(vComponent1Copy);
        reviser.revise();

        assertEquals(2, vComponents.size());
        FXCollections.sort(vComponents, ICalendarTestAbstract.VCOMPONENT_COMPARATOR);
        VEvent myComponent1 = vComponents.get(0);
        VEvent myComponentRecurrence = vComponents.get(1);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withSequence(1);
        expectedVComponent.setDateTimeStart(LocalDateTime.of(2015, 11, 9, 9, 0));
        expectedVComponent.setDateTimeEnd(LocalDateTime.of(2015, 11, 9, 10, 30));
        assertEquals(expectedVComponent, myComponent1);
        
        assertEquals(vComponentRecurrence, myComponentRecurrence);
        assertEquals(LocalDateTime.of(2016, 5, 17, 9, 0), vComponentRecurrence.getRecurrenceId().getValue());
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentRecurrence))
                .withDialogCallback((m) -> null)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();
        
        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1()
                .withExceptionDates(LocalDateTime.of(2016, 5, 17, 10, 0));
        assertEquals(expectedVComponent, myComponent1);
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteAllWithRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponent1))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();
        
        assertEquals(0, vComponents.size());
    }
    
    @Test // makes sure when recurrence deleted the parent gets an EXDATE
    public void canDeleteThisAndFutureWithRecurrence()
    {
        VCalendar vCalendar = new VCalendar();
        final ObservableList<VEvent> vComponents = vCalendar.getVEvents();
        
        VEvent vComponent1 = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponent1);
        // make recurrence
        VEvent vComponentRecurrence = ICalendarStaticComponents.getDaily1();
        vComponentRecurrence.setRecurrenceRule((RecurrenceRule2) null);
        vComponentRecurrence.setRecurrenceId(LocalDateTime.of(2016, 5, 17, 10, 0));
        vComponentRecurrence.setSummary("recurrence summary");
        vComponentRecurrence.setDateTimeStart(LocalDateTime.of(2016, 5, 17, 8, 30));
        vComponentRecurrence.setDateTimeEnd(LocalDateTime.of(2016, 5, 17, 9, 30));
        vComponents.add(vComponentRecurrence);

        // make changes
        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 15, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponent1))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();
        
        assertEquals(1, vComponents.size());
        VEvent myComponent1 = vComponents.get(0);
        
        VEvent expectedVComponent = ICalendarStaticComponents.getDaily1();
        RecurrenceRule2 newRRule = new RecurrenceRule2()
                .withFrequency(FrequencyType.DAILY)
                .withUntil(LocalDateTime.of(2016, 5, 14, 10, 0).atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Z")));
        expectedVComponent.setRecurrenceRule(newRRule);
        assertEquals(expectedVComponent, myComponent1);
    }
}
