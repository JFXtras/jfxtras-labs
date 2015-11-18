package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.freq.Daily;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarEditTest extends ICalendarTestAbstract
{

    /**
     * Tests a daily repeat event with start and end time edit ALL events
     */
    @Test
    public void editAllDailyTime()
    {
        VEventImpl vevent = getDaily2();
        vevent.setDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0));
        vevent.setDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<VEvent> vevents = new ArrayList<VEvent>(Arrays.asList(vevent));

        Set<Appointment> appointments = new TreeSet<Appointment>(getAppointmentComparator());
        Collection<RepeatableAppointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);

        assertEquals(3, appointments.size()); // check if there are only two appointments

        VEventImpl veventOld = new VEventImpl(vevent);
        
        // select appointment and apply changes
        Iterator<Appointment> appointmentIterator = appointments.iterator(); // skip first
        RepeatableAppointment selectedAppointment = (RepeatableAppointment) appointmentIterator.next();
        RepeatableAppointment appointmentOld = AppointmentFactory.newAppointment(selectedAppointment);
        LocalDate date = selectedAppointment.getStartLocalDateTime().toLocalDate();
        selectedAppointment.setStartLocalDateTime(date.atTime(9, 45)); // change start time
        selectedAppointment.setEndLocalDateTime(date.atTime(11, 0)); // change end time

        // TODO - MAKE CHANGES TO VEVENT
        WindowCloseType windowCloseType = vevent.edit(
                veventOld               // original VEvent
              , appointments            // collection of all appointments
              , vevents                 // collection of all VEvents
              , a -> VEventImpl.ChangeDialogResponse.ALL   // answer to edit dialog
              , null);                  // VEvents I/O callback
        assertEquals(WindowCloseType.CLOSE_WITH_CHANGE, windowCloseType); // check to see if close type is correct

        // Check Repeat
//        RepeatableAppointment a = new RepeatableAppointmentImpl()
//                .withAppointmentGroup(appointmentGroups.get(15))
//                .withSummary("Daily Appointment Fixed");
        VEventImpl expectedVEvent = new VEventImpl();
        expectedVEvent.setAppointmentClass(getClazz());
        expectedVEvent.setDateTimeStart(LocalDateTime.of(2015, 10, 7, 9, 45));
        expectedVEvent.setDurationInSeconds(4500);
        expectedVEvent.setRRule(new RRule().withCount(11));
        expectedVEvent.getRRule().setFrequency(new Daily().withInterval(3));
        assertEquals(expectedVEvent, vevent); // check to see if repeat rule changed correctly
        assertEquals(2, appointments.size()); // check if there are only two appointments
        
//        // Check Appointments
//        Iterator<Appointment> appointmentIteratorNew = appointments.iterator();
//        RepeatableAppointment editedAppointment1 = (RepeatableAppointment) appointmentIteratorNew.next();
//
//        RepeatableAppointment expectedAppointment1 = new RepeatableAppointmentImpl()
//            .withStartLocalDateTime(LocalDateTime.of(2015, 11, 3, 9, 45))
//            .withEndLocalDateTime(LocalDateTime.of(2015, 11, 3, 11, 0))
//            .withAppointmentGroup(appointmentGroups.get(15))
//            .withSummary("Daily Appointment Fixed")
//            .withRepeatMade(true)
//            .withRepeat(repeat);
//        assertEquals(expectedAppointment1, editedAppointment1); // Check to see if repeat-generated appointment changed correctly
//
//        RepeatableAppointment editedAppointment2 = (RepeatableAppointment) appointmentIteratorNew.next();
//        RepeatableAppointment expectedAppointment2 = new RepeatableAppointmentImpl()
//                .withStartLocalDateTime(LocalDate.of(2015, 11, 6).atTime(9, 45))
//                .withEndLocalDateTime(LocalDate.of(2015, 11, 6).atTime(11, 0))
//                .withAppointmentGroup(appointmentGroups.get(15))
//                .withSummary("Daily Appointment Fixed")
//                .withRepeatMade(true)
//                .withRepeat(repeat);
//        assertEquals(expectedAppointment2, editedAppointment2); // Check to see if repeat-generated appointment changed correctly
    }
}
