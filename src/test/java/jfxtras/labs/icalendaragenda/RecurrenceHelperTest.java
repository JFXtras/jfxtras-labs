package jfxtras.labs.icalendaragenda;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper;
import jfxtras.labs.icalendaragenda.scene.control.agenda.RecurrenceHelper.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VComponentLocatable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class RecurrenceHelperTest
{
    @Test
    public void canEditAll()
    {
        final Collection<Appointment> appointments = new ArrayList<>();
        final Collection<VComponentLocatable<?>> vComponents = new ArrayList<>();
//        final Map<VComponentNew<?>, List<Appointment>> vComponentAppointmentMap = new WeakHashMap<>();    
        final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>();    
        final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */

        RecurrenceHelper<Appointment> recurrenceHelper = new RecurrenceHelper<Appointment>(
                appointments,
                MakeAppointmentsTest.MAKE_APPOINTMENT_TEST_CALLBACK,
                vComponentAppointmentMap,
                appointmentVComponentMap
                );
        recurrenceHelper.setStartRange(LocalDateTime.of(2016, 5, 15, 0, 0));
        recurrenceHelper.setEndRange(LocalDateTime.of(2016, 5, 22, 0, 0));
        
        // Note: All non-date/time properties are changed in component
        VEvent vComponentEdited = ICalendarComponents.getDaily1();
        vComponentAppointmentMap.put(System.identityHashCode(vComponentEdited), null);
        vComponents.add(vComponentEdited);
        VEvent vComponentOriginalCopy = new VEvent(vComponentEdited);
        System.out.println("start hashes:" + 
                " " + vComponentEdited.hashCode() + " " + vComponentOriginalCopy.hashCode());
        System.out.println(vComponentAppointmentMap.containsKey(vComponentEdited));
        vComponentEdited.setSummary("Edited summary");
        System.out.println(vComponentAppointmentMap.containsKey(vComponentEdited));
        
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
        List<Appointment> newAppointments = recurrenceHelper.makeRecurrences(vComponentEdited);
        Appointment appointment = newAppointments.get(0);
        Temporal startOriginalRecurrence = appointment.getStartTemporal();
        Temporal startRecurrence = LocalDateTime.of(2016, 5, 16, 9, 0);
        Temporal endRecurrence = LocalDateTime.of(2016, 5, 16, 10, 30);

        recurrenceHelper.handleEdit(
                vComponentEdited,
                vComponentOriginalCopy,
                vComponents,
                startOriginalRecurrence,
                startRecurrence,
                endRecurrence,
                (m) -> ChangeDialogOption.ALL);

        assertEquals(1, vComponents.size());
        
        List<Temporal> expectedStartDates = new ArrayList<>(Arrays.asList(
                LocalDateTime.of(2016, 5, 15, 9, 0),
                LocalDateTime.of(2016, 5, 16, 9, 0),
                LocalDateTime.of(2016, 5, 17, 9, 0),
                LocalDateTime.of(2016, 5, 18, 9, 0),
                LocalDateTime.of(2016, 5, 19, 9, 0),
                LocalDateTime.of(2016, 5, 20, 9, 0),
                LocalDateTime.of(2016, 5, 21, 9, 0)
                ));
        List<Temporal> madeStartDates = vComponentEdited.streamRecurrences(
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
        List<Temporal> madeEndDates = vComponentEdited.streamRecurrences(
                LocalDateTime.of(2016, 5, 15, 0, 0), LocalDateTime.of(2016, 5, 22, 0, 0))
                .map(t -> t.plus(Duration.ofMinutes(90)))
                .collect(Collectors.toList());
        assertEquals(expectedEndDates, madeEndDates);
        
        List<Appointment> a = recurrenceHelper.makeRecurrences(vComponentEdited);
        assertEquals(7, a.size());
        System.out.println("vComponentAppointmentMap:" + vComponentAppointmentMap.size() +
                " " + vComponentEdited.hashCode() + " " + vComponentOriginalCopy.hashCode());
        vComponentAppointmentMap.keySet().forEach(k -> System.out.println(k.hashCode()));
//        VComponentNew<?> i = vComponentAppointmentMap.entrySet().iterator().next().getKey();
        Integer i = vComponentAppointmentMap.entrySet().iterator().next().getKey();
        System.out.println("getting:" + vComponentAppointmentMap.containsKey(i) + " " + i.equals(vComponentEdited));
        System.out.println("getting:" + (i.hashCode() == vComponentEdited.hashCode()));
        assertEquals(7, vComponentAppointmentMap.get(System.identityHashCode(vComponentEdited)).size());
        Integer hash = System.identityHashCode(a.get(0));
        assertEquals(vComponentEdited, appointmentVComponentMap.get(hash));
        
        System.out.println(vComponentEdited.hashCode());
        System.out.println(vComponentOriginalCopy.hashCode());
    }
}
