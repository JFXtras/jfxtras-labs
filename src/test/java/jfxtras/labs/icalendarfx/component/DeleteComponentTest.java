package jfxtras.labs.icalendarfx.component;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.deleters.DeleterVEvent;
import jfxtras.labs.icalendarfx.components.deleters.SimpleDeleterFactory;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;

public class DeleteComponentTest
{
    @Test
    public void canDeleteAll()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ALL)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();

        assertEquals(0, vComponents.size());
    }

    @Test
    public void canDeleteOne()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);

        Temporal startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.ONE)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily1()
                .withExceptionDates(startOriginalRecurrence);
        assertEquals(vComponentExpected, vComponents.get(0));
    }
    
    @Test
    public void canDeleteThisAndFuture()
    {
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        VEvent vComponentEdited = ICalendarStaticComponents.getDaily1();
        vComponents.add(vComponentEdited);

        LocalDateTime startOriginalRecurrence = LocalDateTime.of(2016, 5, 16, 10, 0);

        ((DeleterVEvent) SimpleDeleterFactory.newDeleter(vComponentEdited))
                .withDialogCallback((m) -> ChangeDialogOption.THIS_AND_FUTURE)
                .withStartOriginalRecurrence(startOriginalRecurrence)
                .withVComponents(vComponents)
                .delete();

        assertEquals(1, vComponents.size());
        VEvent vComponentExpected = ICalendarStaticComponents.getDaily1();
        ZonedDateTime until = startOriginalRecurrence
                .minusDays(1)
                .atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("Z"));
        vComponentExpected.getRecurrenceRule().getValue().setUntil(until);
        assertEquals(vComponentExpected, vComponents.get(0));
    }
}
