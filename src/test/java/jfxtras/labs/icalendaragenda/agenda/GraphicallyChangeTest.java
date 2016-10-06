package jfxtras.labs.icalendaragenda.agenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import jfxtras.labs.icalendaragenda.AgendaTestAbstract;
import jfxtras.labs.icalendaragenda.ICalendarStaticComponents;
import jfxtras.labs.icalendaragenda.scene.control.agenda.editors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VPrimary;
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.test.TestUtil;

public class GraphicallyChangeTest extends AgendaTestAbstract
{
    @Test
    public void canMoveIndividual()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getIndividual1());
        });
        
        // drag to new location
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine9");
        release(MouseButton.PRIMARY);
        
        // check appointment
        assertEquals(1, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 11, 8, 30)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        assertEquals(1, agenda.getVCalendar().getAllVComponents().size());
        VEvent expectedVEvent = ICalendarStaticComponents.getIndividual1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 8, 30))
                .withSequence(1);
        VEvent editedVEvent = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent, editedVEvent);
    }
    
    @Test
    public void canMoveOneOfRepeatable()
    {
        // create appointment
        TestUtil.runThenWaitForPaintPulse( () -> {
            agenda.getVCalendar().getVEvents().add(ICalendarStaticComponents.getDaily1());
        });
        
        // drag to new location
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine9");
        release(MouseButton.PRIMARY);
        
        ComboBox<ChangeDialogOption> comboBox = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> {
            comboBox.getSelectionModel().select(ChangeDialogOption.ONE);
        });
        click("#changeDialogOkButton");
        
        // check appointment
        assertEquals(6, agenda.appointments().size());
        List<Temporal> expectedStarts = Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0),
                LocalDateTime.of(2015, 11, 10, 10, 0),
                LocalDateTime.of(2015, 11, 11, 8, 0),
                LocalDateTime.of(2015, 11, 12, 10, 0),
                LocalDateTime.of(2015, 11, 13, 10, 0),
                LocalDateTime.of(2015, 11, 14, 10, 0)
                );
        List<Temporal> starts = agenda.appointments().stream()
                .map(a -> a.getStartTemporal())
                .sorted(DateTimeUtilities.TEMPORAL_COMPARATOR2)
                .collect(Collectors.toList());
        assertEquals(expectedStarts, starts);
        
        // check VEvent
        Collections.sort(agenda.getVCalendar().getVEvents(), VPrimary.VPRIMARY_COMPARATOR);
        assertEquals(2, agenda.getVCalendar().getAllVComponents().size());
        VEvent expectedVEvent0 = ICalendarStaticComponents.getDaily1();
        VEvent editedVEvent0 = agenda.getVCalendar().getVEvents().get(0);
        assertEquals(expectedVEvent0, editedVEvent0);

        VEvent editedVEvent1 = agenda.getVCalendar().getVEvents().get(1);
        VEvent expectedVEvent1 = ICalendarStaticComponents.getDaily1()
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 8, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 9, 0))
                .withDateTimeStamp(new DateTimeStamp(editedVEvent1.getDateTimeStamp())) // copy DTSTAMP, because it depends on current date/time
                .withRecurrenceRule((RecurrenceRule) null)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withSequence(1);
        assertEquals(expectedVEvent1, editedVEvent1);
        
        // check DTSTAMP
        String dtstamp = editedVEvent1.getDateTimeStamp().toContent();
        String expectedDTStamp = new DateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z"))).toContent();
        assertEquals(expectedDTStamp.substring(0, 16), dtstamp.substring(0, 16)); // check date, month and time

    }
}
