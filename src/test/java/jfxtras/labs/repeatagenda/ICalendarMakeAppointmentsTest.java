package jfxtras.labs.repeatagenda;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;
import jfxtras.scene.control.agenda.Agenda.Appointment;

public class ICalendarMakeAppointmentsTest extends ICalendarTestAbstract
{

    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void makeAppointmentsDailyTest1()
    {
        VEventImpl vevent = getDaily1();
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 22, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        System.out.println("newAppointments:" + newAppointments.size());
        newAppointments.forEach(a -> System.out.println(a.getStartLocalDateTime()));
        appointments.addAll(newAppointments);       
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
                    return AppointmentFactory.newAppointment(getClazz())
                            .withStartLocalDateTime(d)
                            .withEndLocalDateTime(d.plusSeconds(3600))
                            .withDescription("Daily1 Description")
                            .withSummary("Daily1 Summary")
                            .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(3));
                })
                .collect(Collectors.toList());
        assertEquals(expectedAppointments, appointments);
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

        Appointment madeAppointment1 = (Appointment) appointmentIterator.next();
        Appointment expectedAppointment1 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDate.of(2015, 12, 21).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 21).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertEquals(expectedAppointment1, madeAppointment1); 
        
        Appointment madeAppointment2 = (Appointment) appointmentIterator.next();
        Appointment expectedAppointment2 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDate.of(2015, 12, 23).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 23).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertEquals(expectedAppointment2, madeAppointment2);

        Appointment madeAppointment3 = (Appointment) appointmentIterator.next();
        Appointment expectedAppointment3 = AppointmentFactory.newAppointment(getClazz())
                .withStartLocalDateTime(LocalDate.of(2015, 12, 25).atTime(10, 0))
                .withEndLocalDateTime(LocalDate.of(2015, 12, 25).atTime(10, 45))
                .withAppointmentGroup(ICalendarAgenda.DEFAULT_APPOINTMENT_GROUPS.get(2))
                .withSummary("Weekly1 Summary")
                .withDescription("Weekly1 Description");
        assertEquals(expectedAppointment3, madeAppointment3);     
    }
    
    @Test
    public void makeAppointmentsWholeDayTest1()
    {
        VEventImpl vevent = getWholeDayDaily3();
        vevent.setAppointmentClass(getClazz());
        LocalDateTime start = LocalDateTime.of(2015, 11, 15, 0, 0);
        LocalDateTime end = LocalDateTime.of(2015, 11, 21, 0, 0);
        List<Appointment> appointments = new ArrayList<Appointment>();
        Collection<Appointment> newAppointments = vevent.makeInstances(start, end);
        newAppointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
    }
}
