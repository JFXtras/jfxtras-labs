package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarComponents;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper;
import jfxtras.labs.icalendarfx.components.ReviseComponentHelper.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;

public class ReviseComponentTest
{
    @Test
    public void canEditAll()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        
        VEvent vComponentOriginal = ICalendarComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEditedCopy = new VEvent(vComponentOriginal);
        vComponentEditedCopy.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviseComponentHelper.handleEdit(
                vComponentEditedCopy,
                vComponentOriginal,
                vComponents,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
                (m) -> ChangeDialogOption.ALL);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(vComponentEditedCopy, myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 9, 9, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 30), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Edited summary", myComponent.getSummary().getValue());        
    }
    
    @Test
    public void canEditOne()
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
                    // Note: making recurrences should be done by a similar listener in production code
                }
            }
        };
        vComponents.addListener(listener);
        
        VEvent vComponentOriginal = ICalendarComponents.getDaily1();
//        vComponents.add(vComponentOriginal);
        VEvent vComponentEditedCopy = new VEvent(vComponentOriginal);
        vComponentEditedCopy.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviseComponentHelper.handleEdit(
                vComponentEditedCopy,
                vComponentOriginal,
                vComponents,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
                (m) -> ChangeDialogOption.ONE);

        assertEquals(2, vComponents.size());
        VEvent myComponentIndividual = vComponents.get(1);
        assertEquals(vComponentEditedCopy, myComponentIndividual);
        VEvent myComponentRepeats = vComponents.get(0);
        assertEquals(vComponentOriginal, myComponentRepeats);
        assertEquals(LocalDateTime.of(2016, 5, 16, 9, 0), myComponentIndividual.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2016, 5, 16, 10, 30), myComponentIndividual.getDateTimeEnd().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponentRepeats.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponentRepeats.getDateTimeEnd().getValue()); 
        assertEquals("Edited summary", myComponentIndividual.getSummary().getValue());

        List<String> expectedChanges = Arrays.asList(
                "Added:Daily1 Summary",
                "Added:Edited summary"
                );
        assertEquals(expectedChanges, changes);
    }
    
    @Test
    public void canEditCancel()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        
        VEvent vComponentOriginal = ICalendarComponents.getDaily1();
        vComponents.add(vComponentOriginal);
        VEvent vComponentEditedCopy = new VEvent(vComponentOriginal);
        vComponentEditedCopy.setSummary("Edited summary");

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        ReviseComponentHelper.handleEdit(
                vComponentEditedCopy,
                vComponentOriginal,
                vComponents,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
                (m) -> ChangeDialogOption.CANCEL);

        assertEquals(1, vComponents.size());
        VEvent myComponent = vComponents.get(0);
        assertEquals(vComponentOriginal, myComponent);
        assertEquals(LocalDateTime.of(2015, 11, 9, 10, 0), myComponent.getDateTimeStart().getValue());        
        assertEquals(LocalDateTime.of(2015, 11, 9, 11, 0), myComponent.getDateTimeEnd().getValue());        
        assertEquals("Daily1 Summary", myComponent.getSummary().getValue());        
    }
}
