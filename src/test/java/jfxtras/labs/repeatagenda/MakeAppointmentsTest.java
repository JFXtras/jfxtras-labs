package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class MakeAppointmentsTest extends ICalendarTestAbstract
{

    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeAppointmentsDailyTest1()
    {
        VEventImpl vevent = getDaily1();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> newAppointments = vevent.makeInstances(start, end);
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 10, 0)
              , LocalDateTime.of(2015, 11, 16, 10, 0)
              , LocalDateTime.of(2015, 11, 17, 10, 0)
              , LocalDateTime.of(2015, 11, 18, 10, 0)
              , LocalDateTime.of(2015, 11, 19, 10, 0)
              , LocalDateTime.of(2015, 11, 20, 10, 0)
              , LocalDateTime.of(2015, 11, 21, 10, 0)
                ));
        List<Appointment> expectedAppointments = expectedDates
                .stream()
                .map(d -> {
                    return new Agenda.AppointmentImplLocal()
                            .withStartLocalDateTime(d)
                            .withEndLocalDateTime(d.plusSeconds(3600))
                            .withDescription("Daily1 Description")
                            .withSummary("Daily1 Summary")
                            .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(3));
                })
                .collect(Collectors.toList());
        for (int i=0; i<expectedAppointments.size(); i++)
        {
            assertTrue(AppointmentIsEqualTo(expectedAppointments.get(i), newAppointments.get(i)));
        }
    }
    
    /** FREQ=WEEKLY;INTERVAL=2;BYDAY=MO,WE,FR */
    @Test
    public void makeAppointmentsWeeklyTest1()
    {
        VEventImpl vevent = getWeekly2();
        vevent.setAppointmentClass(getClazz());
        LocalDateTime start = LocalDateTime.of(2015, 12, 20, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 12, 27, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        appointments.addAll(newAppointments);

        Iterator<Appointment> appointmentIterator = appointments.iterator();

        Appointment madeAppointment1 = appointmentIterator.next();
        Appointment expectedAppointment1 = new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 21).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 21).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertTrue(AppointmentIsEqualTo(expectedAppointment1, madeAppointment1)); 
        
        Appointment madeAppointment2 = appointmentIterator.next();
        Appointment expectedAppointment2 = new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 23).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 23).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertTrue(AppointmentIsEqualTo(expectedAppointment2, madeAppointment2));

        Appointment madeAppointment3 = appointmentIterator.next();
        Appointment expectedAppointment3 = new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(LocalDate.of(2015, 12, 25).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 25).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgendaUtilities.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertTrue(AppointmentIsEqualTo(expectedAppointment3, madeAppointment3));     
    }
    
    @Test
    public void makeAppointmentsWholeDayTest1()
    {
        VEventImpl vevent = getWholeDayDaily3();
        vevent.setAppointmentClass(getClazz());
        LocalDate start = LocalDate.of(2015, 11, 15);
        LocalDate end = LocalDate.of(2015, 11, 21);
        List<Appointment> appointments = vevent.makeInstances(start, end);

        List<LocalDateTime> dates = appointments.stream()
                .map(a -> a.getStartLocalDateTime())
                .collect(Collectors.toList());
        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
                LocalDateTime.of(2015, 11, 15, 0, 0)
              , LocalDateTime.of(2015, 11, 18, 0, 0)
              , LocalDateTime.of(2015, 11, 21, 0, 0)
                ));        
        assertEquals(expectedDates, dates);
        
        assertTrue(appointments.stream().map(a -> a.isWholeDay()).allMatch(a -> a == true)); // verify all appointment are wholeDay
    }
    
}
