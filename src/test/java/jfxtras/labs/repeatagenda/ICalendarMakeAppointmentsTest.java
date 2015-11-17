package jfxtras.labs.repeatagenda;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAppointmentImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.VEventImpl;

public class ICalendarMakeAppointmentsTest extends ICalendarRepeatTestAbstract
{
    
    /** Tests daily stream with FREQ=DAILY */
    @Test
    public void dailyMakeAppointmentsTest1()
    {
        VEventImpl vevent = getDaily1()
                .withAppointmentClass(RepeatableAppointmentImpl.class)
                .withDateTimeRangeStart(LocalDateTime.of(2015, 11, 15, 0, 0))
                .withDateTimeRangeEnd(LocalDateTime.of(2015, 11, 22, 0, 0));
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
        Collection<RepeatableAppointment> newAppointments = vevent.makeAppointments();
        appointments.addAll(newAppointments);
        appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));

        
        //        List<LocalDateTime> expectedDates = new ArrayList<LocalDateTime>(Arrays.asList(
//                LocalDateTime.of(2015, 11, 15, 10, 0)
//              , LocalDateTime.of(2015, 11, 16, 10, 0)
//              , LocalDateTime.of(2015, 11, 17, 10, 0)
//              , LocalDateTime.of(2015, 11, 18, 10, 0)
//              , LocalDateTime.of(2015, 11, 19, 10, 0)
//              , LocalDateTime.of(2015, 11, 20, 10, 0)
//              , LocalDateTime.of(2015, 11, 21, 10, 0)
//                ));
//        assertEquals(expectedDates, madeDates);
    }
    
//    // Make Appointments tests
//    @Test
//    public void makeAppointmentsMonthly()
//    {
//        Repeat repeat = getRepeatMonthly();
//        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();
//        LocalDateTime startDate = LocalDateTime.of(2015, 11, 1, 0, 0);
//        LocalDateTime endDate = LocalDateTime.of(2015, 11, 8, 0, 0); // tests one week time range (inclusive of startDate, exclusive of endDate)
//        Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments(startDate, endDate);
//        appointments.addAll(newAppointments);
//        Appointment madeAppointment = (appointments.size() == 1) ? appointments.get(0) : null;
//        Appointment expectedAppointment = new RepeatableAppointmentImpl()
//                .withStartLocalDateTime(LocalDate.of(2015, 11, 7).atTime(8, 45))
//                .withEndLocalDateTime(LocalDate.of(2015, 11, 7).atTime(10, 15))
//                .withAppointmentGroup(appointmentGroups.get(9))
//                .withSummary("Monthly Appointment Fixed")
//                .withRepeatMade(true)
//                .withRepeat(repeat);
//        assertEquals(expectedAppointment, madeAppointment);
//        System.out.println("done");
//    }
    
}
