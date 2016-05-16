package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseButton;
import jfxtras.labs.icalendarfx.components.VComponentPrimary;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.RecurrenceRule3;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;
import jfxtras.test.TestUtil;

public class AgendaChangeTest extends AgendaTestAbstract
{
    @Override
    public Parent getRootNode()
    {
        return super.getRootNode();
    }
    
    /* Moves appointment to new time */
    @Test
    public void canDragAndDropAppointment()
    {
        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarComponents.getDaily1()));
        
        // move one appointment
        assertFind("#AppointmentRegularBodyPane2015-11-11/0");
        move("#hourLine11");
        press(MouseButton.PRIMARY);
        move("#hourLine15");
        release(MouseButton.PRIMARY);

        // change dialog
        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
        click("#changeDialogOkButton");

        // check change to state
        assertEquals(2, agenda.getVCalendar().getVEvents().size());
        assertEquals(6, agenda.appointments().size());
        Collections.sort(agenda.getVCalendar().getVEvents(), VComponentPrimary.VCOMPONENT_COMPARATOR);
        VEvent v0 = agenda.getVCalendar().getVEvents().get(0);
        VEvent v1 = agenda.getVCalendar().getVEvents().get(1);
//        RecurrenceRule3 r = ICalendarStaticVEvents.getDaily1().childComponentsWithRecurrenceIDs().add(v1);
//        VEvent expectedV0 = ICalendarStaticVEvents.getDaily1()
//                .withRRule(r);
        assertEquals(Arrays.asList(v1), v0.childComponentsWithRecurrenceIDs()); // has recurrence ID in child list
        
        VEvent expectedV1 = ICalendarComponents.getDaily1()
                .withRecurrenceRule((RecurrenceRule3) null)
                .withRecurrenceId(LocalDateTime.of(2015, 11, 11, 10, 0))
                .withDateTimeStart(LocalDateTime.of(2015, 11, 11, 14, 0))
                .withDateTimeEnd(LocalDateTime.of(2015, 11, 11, 15, 0))
                .withDateTimeStamp(v1.getDateTimeStamp())
                .withSequence(1);
        assertEquals(expectedV1, v1);
        
        // check appointment dates
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 10, 0)
              , LocalDateTime.of(2015, 11, 10, 10, 0)
              , LocalDateTime.of(2015, 11, 11, 14, 0)
              , LocalDateTime.of(2015, 11, 12, 10, 0)
              , LocalDateTime.of(2015, 11, 13, 10, 0)
              , LocalDateTime.of(2015, 11, 14, 10, 0)
              ));
        List<LocalDateTime> startDates = agenda.appointments()
                .stream()
                .map(a -> a.getStartLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        assertEquals(expectedDates, startDates);
        
        List<LocalDateTime> expectedEndDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 9, 11, 0)
              , LocalDateTime.of(2015, 11, 10, 11, 0)
              , LocalDateTime.of(2015, 11, 11, 15, 0)
              , LocalDateTime.of(2015, 11, 12, 11, 0)
              , LocalDateTime.of(2015, 11, 13, 11, 0)
              , LocalDateTime.of(2015, 11, 14, 11, 0)
              ));
        List<LocalDateTime> endDates = agenda.appointments()
                .stream()
                .map(a -> a.getEndLocalDateTime())
                .sorted()
                .collect(Collectors.toList());
        assertEquals(expectedEndDates, endDates);
    }
    
//    /* Moves appointment to new time - ZonedDateTime */
//    @Test
//    public void canDragAndDropZonedAppointment()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticVEvents.getWeeklyZoned()));
//        
//        // move one appointment
//        assertFind("#AppointmentRegularBodyPane2015-11-11/0");
//        move("#hourLine10");
//        press(MouseButton.PRIMARY);
//        move("#hourLine15");
//        release(MouseButton.PRIMARY);
//
//        // change dialog
//        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
//        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ONE));
//        click("#changeDialogOkButton");
//
//        // check change to state
//        assertEquals(2, agenda.getVCalendar().getVEvents().size());
//        assertEquals(3, agenda.appointments().size());
//        Collections.sort(agenda.getVCalendar().getVEvents(), VComponent.VCOMPONENT_COMPARATOR);
//        VEvent v0 = agenda.getVCalendar().getVEvents().get(0);
//        VEvent v1 = agenda.getVCalendar().getVEvents().get(1);
//        RecurrenceRule3 r = ICalendarStaticVEvents.getWeeklyZoned().getRRule().withRecurrences(v1);
//        VEvent expectedV0 = ICalendarStaticVEvents.getWeeklyZoned()
//                .withRRule(r);
//        assertTrue(VEvent.isEqualTo(expectedV0, v0));
//        
//        VEvent expectedV1 = ICalendarStaticVEvents.getWeeklyZoned()
//                .withRRule(null)
//                .withDateTimeRecurrence(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 00), ZoneId.of("America/Los_Angeles")))
//                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 15, 0), ZoneId.of("America/Los_Angeles")))
//                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 15, 45), ZoneId.of("America/Los_Angeles")))
//                .withDateTimeStamp(v1.getDateTimeStamp())
//                .withSequence(1);
//        assertTrue(VEvent.isEqualTo(expectedV1, v1));
//        
//        // check appointment dates
//        List<Temporal> expectedDates = new ArrayList<>(Arrays.asList(
//                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 15, 0), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 0), ZoneId.of("America/Los_Angeles"))
//              ));
//        List<Temporal> startDates = agenda.appointments()
//                .stream()
//                .map(a -> a.getStartTemporal())
//                .sorted()
//                .collect(Collectors.toList());
//        assertEquals(expectedDates, startDates);
//        
//        List<Temporal> expectedEndDates = new ArrayList<>(Arrays.asList(
//                ZonedDateTime.of(LocalDateTime.of(2015, 11, 9, 10, 45), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 15, 45), ZoneId.of("America/Los_Angeles"))
//              , ZonedDateTime.of(LocalDateTime.of(2015, 11, 13, 10, 45), ZoneId.of("America/Los_Angeles"))
//              ));
//        List<Temporal> endDates = agenda.appointments()
//                .stream()
//                .map(a -> a.getEndTemporal())
//                .sorted()
//                .collect(Collectors.toList());
//        assertEquals(expectedEndDates, endDates);
//    }
//    
//    @Test
//    public void canDragAndDropWholedayToTimeBased()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticVEvents.getIndividual2()));
//        
//        // move one appointment
//        move("#AppointmentWholedayHeaderPane2015-11-11/0"); 
//        press(MouseButton.PRIMARY);
//        move("#hourLine10");
//        release(MouseButton.PRIMARY);
//        
//        assertEquals(1, agenda.getVCalendar().getVEvents().size());
//        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
//        VEvent expectedVEvent = ICalendarStaticVEvents.getIndividual2()
//                .withSequence(1)
//                .withDateTimeStart(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 10, 0), ZoneId.systemDefault()))
//                .withDateTimeEnd(ZonedDateTime.of(LocalDateTime.of(2015, 11, 11, 11, 0), ZoneId.systemDefault()));
//        assertTrue(VEvent.isEqualTo(expectedVEvent, vEvent));
//    }
//    
//    @Test
//    public void canDragAndDropTimeBasedToWholeDay()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticVEvents.getIndividual1()));
//        
//        // move one appointment
//        move("#AppointmentRegularBodyPane2015-11-11/0"); 
//        press(MouseButton.PRIMARY);
//        move("#DayHeader2015-11-12"); // header of next day
//        release(MouseButton.PRIMARY);
//        
//        assertEquals(1, agenda.getVCalendar().getVEvents().size());
//        VEvent vEvent = agenda.getVCalendar().getVEvents().get(0);
//        System.out.println(vEvent);
//        VEvent expectedVEvent = ICalendarStaticVEvents.getIndividual1()
//                .withSequence(1)
//                .withDateTimeStart(LocalDate.of(2015, 11, 12))
//                .withDuration(Period.ofDays(1));
//        assertTrue(VEvent.isEqualTo(expectedVEvent, vEvent));
//    }
//    
//    @Test
//    public void canExpandAppoindment()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticVEvents.getDaily1()));
//        
//        // expand time
//        move("#AppointmentRegularBodyPane2015-11-11/0 .DurationDragger"); 
//        press(MouseButton.PRIMARY);
//        move("#hourLine15");
//        release(MouseButton.PRIMARY);
//        
//        // delete VComponent
//        ComboBox<ChangeDialogOption> c = find("#changeDialogComboBox");
//        TestUtil.runThenWaitForPaintPulse( () -> c.getSelectionModel().select(ChangeDialogOption.ALL));
//        click("#changeDialogOkButton");
//
//        // check change to state
//        assertEquals(1, agenda.getVCalendar().getVEvents().size());
//        assertEquals(6, agenda.appointments().size());
//        VEvent v = agenda.getVCalendar().getVEvents().get(0);
//        VEvent expectedV = ICalendarStaticVEvents.getDaily1()
//                .withDateTimeStart(LocalDateTime.of(2015, 11, 9, 10, 0))
//                .withDateTimeEnd(LocalDateTime.of(2015, 11, 9, 15, 0))
//                .withSequence(1);
//        assertTrue(VEvent.isEqualTo(expectedV, v));
//        
//        // check appointment dates
//        List<LocalDateTime> expectedStartDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 11, 9, 10, 0)
//              , LocalDateTime.of(2015, 11, 10, 10, 0)
//              , LocalDateTime.of(2015, 11, 11, 10, 0)
//              , LocalDateTime.of(2015, 11, 12, 10, 0)
//              , LocalDateTime.of(2015, 11, 13, 10, 0)
//              , LocalDateTime.of(2015, 11, 14, 10, 0)
//              ));
//        List<LocalDateTime> startDates = agenda.appointments()
//                .stream()
//                .map(a -> a.getStartLocalDateTime())
//                .sorted()
//                .collect(Collectors.toList());
//        assertEquals(expectedStartDates, startDates);
//
//        List<LocalDateTime> expectedEndDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 11, 9, 15, 0)
//              , LocalDateTime.of(2015, 11, 10, 15, 0)
//              , LocalDateTime.of(2015, 11, 11, 15, 0)
//              , LocalDateTime.of(2015, 11, 12, 15, 0)
//              , LocalDateTime.of(2015, 11, 13, 15, 0)
//              , LocalDateTime.of(2015, 11, 14, 15, 0)
//              ));
//        List<LocalDateTime> endDates = agenda.appointments()
//                .stream()
//                .map(a -> a.getEndLocalDateTime())
//                .sorted()
//                .collect(Collectors.toList());
//        assertEquals(expectedEndDates, endDates);
//    }
//    
//    @Test
//    public void canCancelAppoindmentDragAndDrop()
//    {
//        TestUtil.runThenWaitForPaintPulse( () -> agenda.getVCalendar().getVEvents().add(ICalendarStaticVEvents.getDaily1()));
//
//        // move one appointment
//        assertFind("#AppointmentRegularBodyPane2015-11-11/0");
//        move("#hourLine10");
//        press(MouseButton.PRIMARY);
//        move("#hourLine15");
//        release(MouseButton.PRIMARY);
//
//        // change dialog
//        click("#changeDialogCancelButton");
//
//        // check return to original state
//        assertEquals(1, agenda.getVCalendar().getVEvents().size());
//        assertEquals(6, agenda.appointments().size());
//        VEvent v = agenda.getVCalendar().getVEvents().get(0);
//        VEvent expectedV = ICalendarStaticVEvents.getDaily1();
//        assertTrue(VEvent.isEqualTo(expectedV, v));
//        
//        // check appointment dates
//        List<LocalDateTime> expectedStartDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 11, 9, 10, 0)
//              , LocalDateTime.of(2015, 11, 10, 10, 0)
//              , LocalDateTime.of(2015, 11, 11, 10, 0)
//              , LocalDateTime.of(2015, 11, 12, 10, 0)
//              , LocalDateTime.of(2015, 11, 13, 10, 0)
//              , LocalDateTime.of(2015, 11, 14, 10, 0)
//              ));
//        List<LocalDateTime> startDates = agenda.appointments()
//                .stream()
//                .map(a -> a.getStartLocalDateTime())
//                .sorted()
//                .collect(Collectors.toList());
//        assertEquals(expectedStartDates, startDates);
//    }
}
