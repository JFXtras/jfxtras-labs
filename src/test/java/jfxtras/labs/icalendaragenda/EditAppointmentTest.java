package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.editors.ReviseComponentHelper;
import jfxtras.labs.icalendarfx.components.editors.ReviseComponentHelper.ChangeDialogOption;
import jfxtras.scene.control.agenda.Agenda.Appointment;

@Deprecated // do in icalendarfx
public class EditAppointmentTest
{
    @Test
    @Ignore
    public void canEditAll()
    {
        final Collection<Appointment> appointments = new ArrayList<>();
        final ObservableList<VEvent> vComponents = FXCollections.observableArrayList();
        ListChangeListener<? super VComponentLocatable<?>> listener  = (ListChangeListener.Change<? extends VComponentLocatable<?>> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    List<? extends VComponentLocatable<?>> added = change.getAddedSubList();
                    System.out.println("ADDED:" + added);
                    // TODO - MAKE APPOINTMENTS - UPDATE MAPS
                }
            }
        };
        vComponents.addListener(listener);
        final Map<VComponent<?>, List<Appointment>> vComponentAppointmentMap = new WeakHashMap<>();
//        final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();    
        final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */

        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
//                appointments,
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK_LOCATABLE
//                vComponentAppointmentMap,
//                appointmentVComponentMap
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        // Note: All non-date/time properties are changed in component
        VEvent vComponentOriginal = ICalendarStaticComponents.getDaily1();
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vComponentOriginal);
        vComponentAppointmentMap.put(vComponentOriginal, newAppointments);
        vComponents.add(vComponentOriginal);
        VEvent vComponentEditedCopy = new VEvent(vComponentOriginal);
        System.out.println("start hashes:" + 
                " " + vComponentOriginal.hashCode() + " " + vComponentEditedCopy.hashCode());
        System.out.println(vComponentAppointmentMap.containsKey(vComponentOriginal));
        vComponentEditedCopy.setSummary("Edited summary");
//        System.out.println(vComponentAppointmentMap.containsKey(vComponentEdited));
        
//        Map<ObjectProperty<Integer>, String> testMap = new HashMap<>();
//        ObjectProperty<Integer> i1 = new SimpleObjectProperty<>(6);
//        System.out.println(i1.hashCode());
//        testMap.put(i1, "t1");
//        i1.set(7);
//        System.out.println(i1.hashCode());
//        System.out.println(testMap.containsKey(i1));
//        System.out.println(testMap.keySet().iterator().next());
//        
//        System.exit(0);
        // Note: All date/time properties are changed in recurrence        
        Appointment appointment = newAppointments.get(0);
        Temporal startOriginalRecurrence = appointment.getStartTemporal();
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);
        TemporalAmount shift = Duration.between(startOriginalRecurrence, startRecurrence);

        Collection<VEvent> newVComponents = ReviseComponentHelper.handleEdit(
                vComponentOriginal,
                vComponentEditedCopy,
//                vComponents,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
//                shift,
                (m) -> ChangeDialogOption.ALL);
        vComponents.addAll(newVComponents);

        assertEquals(1, vComponents.size());
        assertEquals(vComponentEditedCopy, vComponents.get(0));
        
        List<Temporal> expectedStartDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 15, 9, 0),
                LocalDateTime.of(2016, 5, 16, 9, 0),
                LocalDateTime.of(2016, 5, 17, 9, 0),
                LocalDateTime.of(2016, 5, 18, 9, 0),
                LocalDateTime.of(2016, 5, 19, 9, 0),
                LocalDateTime.of(2016, 5, 20, 9, 0),
                LocalDateTime.of(2016, 5, 21, 9, 0)
                ));
        List<Temporal> madeStartDates = vComponentEditedCopy.streamRecurrences(
                LocalDateTime.of(2016, 5, 15, 0, 0), LocalDateTime.of(2016, 5, 22, 0, 0))
                .collect(Collectors.toList());
        assertEquals(expectedStartDates, madeStartDates);
        
        List<Temporal> expectedEndDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 15, 10, 30),
                LocalDateTime.of(2016, 5, 16, 10, 30),
                LocalDateTime.of(2016, 5, 17, 10, 30),
                LocalDateTime.of(2016, 5, 18, 10, 30),
                LocalDateTime.of(2016, 5, 19, 10, 30),
                LocalDateTime.of(2016, 5, 20, 10, 30),
                LocalDateTime.of(2016, 5, 21, 10, 30)
                ));
        List<Temporal> madeEndDates = vComponentEditedCopy.streamRecurrences(
                LocalDateTime.of(2016, 5, 15, 0, 0), LocalDateTime.of(2016, 5, 22, 0, 0))
                .map(t -> t.plus(Duration.ofMinutes(90)))
                .collect(Collectors.toList());
        assertEquals(expectedEndDates, madeEndDates);
        
        List<Appointment> a = recurrenceHelper.makeRecurrences(vComponentEditedCopy);
        assertEquals(7, a.size());
//        vComponents.
        vComponentAppointmentMap.keySet().forEach(k -> System.out.println(k.hashCode()));
//        VComponentNew<?> i = vComponentAppointmentMap.entrySet().iterator().next().getKey();
        VComponent<?> i = vComponentAppointmentMap.entrySet().iterator().next().getKey();
        System.out.println("vComponentAppointmentMap:" + vComponentAppointmentMap.size() +
                " " + vComponentEditedCopy.hashCode() + " " + vComponentOriginal.hashCode() + " " + i.hashCode());
        System.out.println("getting:" + vComponentAppointmentMap.containsKey(i) + " " + i.equals(vComponentEditedCopy));
        System.out.println("getting:" + (i.hashCode() == vComponentEditedCopy.hashCode()));
        assertEquals(7, vComponentAppointmentMap.get(System.identityHashCode(vComponentEditedCopy)).size());
        Integer hash = System.identityHashCode(a.get(0));
        assertEquals(vComponentEditedCopy, appointmentVComponentMap.get(hash));
        
        System.out.println(vComponentEditedCopy.hashCode());
        System.out.println(vComponentOriginal.hashCode());
    }
}
