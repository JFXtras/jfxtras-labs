package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.junit.Ignore;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.RepeatChange;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableUtilities.WindowCloseType;
import jfxtras.scene.control.agenda.Agenda.Appointment;

// Tests deleted appointments with a repeat rule
public class RepeatExceptionTest extends RepeatTestAbstract {

    /**
     * Tests a daily repeat event with start and end time edit ALL events
     */
    @Test
    @Ignore
    public void editAllDailyWithExceptions()
    {
        Repeat repeat = getRepeatDailyWithExceptions();
        Set<Repeat> repeats = new HashSet<Repeat>(Arrays.asList(repeat));
        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        LocalDateTime startDate = LocalDateTime.of(2015, 10, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2015, 11, 1, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
        appointments.addAll(newAppointments);
        assertEquals(6, appointments.size()); // check number of appointments
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator();
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();

        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate().plusDays(2);
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time
        WindowCloseType windowCloseType = RepeatableUtilities.editAppointments(
                selectedAppointment
              , appointmentOld
              , appointments
              , repeats
              , a -> RepeatChange.ALL
              , null
              , null);
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check Repeat
        ObservableList<LocalDateTime> exceptions = FXCollections.observableArrayList(
                Arrays.asList(LocalDateTime.of(2015, 10, 15, 9, 45)
                            , LocalDateTime.of(2015, 10, 18, 9, 45)
                            , LocalDateTime.of(2015, 10, 21, 9, 45)));
        RepeatableAppointment a = new RepeatableAppointmentImpl()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        Repeat expectedRepeat =  new RepeatImpl(RepeatableAppointmentImpl.class)
                .withStartLocalDate(LocalDateTime.of(2015, 10, 9, 9, 45))
                .withDurationInSeconds(4500)
                .withFrequency(Frequency.DAILY)
                .withInterval(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withCount(11)
                .withAppointmentData(a)
                .withExceptions(exceptions);
        assertEquals(expectedRepeat, repeat); // check to see if repeat rule changed correctly
        assertEquals(5, appointments.size()); // check if there are only two appointments
        
        List<LocalDate> expectedDates1 = new ArrayList<LocalDate>(Arrays.asList(
                LocalDate.of(2015, 10, 9)
              , LocalDate.of(2015, 10, 12)
              , LocalDate.of(2015, 10, 24)
              , LocalDate.of(2015, 10, 27)
              , LocalDate.of(2015, 10, 30)
                ));
        List<LocalDateTime> expectedDates2 = expectedDates1.stream()
            .map(a2 -> LocalDateTime.of(a2, LocalTime.of(9, 45)))
            .collect(Collectors.toList());
        assertEquals(expectedDates2, appointments);        
    }
}
