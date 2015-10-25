package jfxtras.labs.repeatagenda;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.Frequency;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.MonthlyRepeat;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatImpl;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;
import jfxtras.scene.control.agenda.Agenda.LocalDateTimeRange;

public abstract class RepeatTestAbstract {
    
    // Comparator for tree sort
    private static final Comparator<Appointment> APPOINTMENT_COMPARATOR = (a1, a2)
            -> a1.getStartLocalDateTime().compareTo(a2.getStartLocalDateTime());
    public final Comparator<Appointment> getAppointmentComparator() { return APPOINTMENT_COMPARATOR; }
    
    public final static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
    = javafx.collections.FXCollections.observableArrayList(
            IntStream
            .range(0, 23)
            .mapToObj(i -> new RepeatableAgenda.AppointmentGroupImpl()
//                   .withStyleClass("group" + i) // skipped due to static variable problem with junit
                   .withKey(i)
                   .withDescription("group" + (i < 10 ? "0" : "") + i))
            .collect(Collectors.toList()));
    ObservableList<AppointmentGroup> appointmentGroups = DEFAULT_APPOINTMENT_GROUPS;
    
    public Repeat getRepeatWeekly()
    {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        hour = (hour < 21) ? hour+3 : Math.max(hour-3,0);
        int minute = now.getMinute();
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(5))
                .withSummary("Weekly Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(hour, minute)))
                .withDurationInSeconds(7200)
//                .withStartLocalTime(LocalTime.now().plusHours(3))
//                .withEndLocalTime(LocalTime.now().plusHours(5))
                .withEndCriteria(EndCriteria.NEVER)
                .withFrequency(Frequency.WEEKLY)
                .withDayOfWeek(LocalDate.now().getDayOfWeek(), true)
                .withDayOfWeek(LocalDate.now().plusDays(2).getDayOfWeek(), true)
                .withAppointmentData(a1);
    }

    public Repeat getRepeatMonthly()
    {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        hour = (hour > 5) ? hour-5 : Math.min(23,hour+5);
        int minute = now.getMinute();
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDateTime.of(LocalDate.now().minusDays(1), LocalTime.of(hour, minute)))
                .withDurationInSeconds(9000)
//                .withStartLocalTime(LocalTime.now().minusHours(5))
//                .withEndLocalTime(LocalTime.now().minusHours(3))
                .withEndCriteria(EndCriteria.UNTIL)
                .withUntil(LocalDateTime.of(LocalDate.now().minusDays(1).plusMonths(3),LocalTime.of(hour, minute).plusSeconds(9000)))
                .withFrequency(Frequency.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatDaily()
    {
        LocalTime now = LocalTime.now();
        int hour = now.getHour();
        hour = (hour < 20) ? hour+4 : Math.max(hour-4,0);
        int minute = now.getMinute();
        RepeatableAppointment a3 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Variable");
        return new RepeatImpl()
                .withStartLocalDate(LocalDateTime.of(LocalDate.now().minusDays(2), LocalTime.of(hour, minute)))
                .withDurationInSeconds(7200)
//                .withStartLocalDate(LocalDate.now().minusDays(2))
//                .withStartLocalTime(LocalTime.now().plusHours(4))
//                .withEndLocalTime(LocalTime.now().plusHours(7))
                .withEndCriteria(EndCriteria.AFTER)
                .withFrequency(Frequency.DAILY)
                .withInterval(2)
                .withCount(5)
                .withAppointmentData(a3);
    }

    public Repeat getRepeatDailyFixed()
    {
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 7, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withFrequency(Frequency.DAILY)
                .withInterval(3)
                .withEndCriteria(EndCriteria.AFTER)
                .withCount(11)
                .withAppointmentData(a);
    }
    
    public Repeat getRepeatWeeklyFixed()
    {
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 7, 18, 0))
                .withDurationInSeconds(2700)
//                .withStartLocalTime(LocalTime.of(18, 0))
//                .withEndLocalTime(LocalTime.of(18, 45))
                .withEndCriteria(EndCriteria.NEVER)
                .withFrequency(Frequency.WEEKLY)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withAppointmentData(a1);
    }
    
    public Repeat getRepeatWeeklyFixed2()
    {
        RepeatableAppointment a1 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(3))
                .withSummary("Weekly Appointment Fixed2");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 5, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withFrequency(Frequency.WEEKLY)
                .withDayOfWeek(DayOfWeek.MONDAY, true)
                .withDayOfWeek(DayOfWeek.WEDNESDAY, true)
                .withDayOfWeek(DayOfWeek.FRIDAY, true)
                .withInterval(2)
                .withEndCriteria(EndCriteria.AFTER)
                .withCount(50)
                .withAppointmentData(a1);
    }
    
    public Repeat getRepeatMonthlyFixed()
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed");
        // TODO - REPLACE MAKE APPOINTMENT RANGE WITH WITH METHODS - REMOVE FROM CONSTRUCTOR
        return RepeatFactory.newRepeat(new LocalDateTimeRange(LocalDateTime.of(2015, 10, 4, 0, 0), LocalDateTime.of(2015, 10, 10, 0, 0)))
                .withStartLocalDate(LocalDateTime.of(2015, 10, 7, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.UNTIL)
                .withUntil(LocalDateTime.of(2016, 10, 7, 10, 15))
                .withFrequency(Frequency.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_MONTH)
                .withAppointmentData(a2);
    }

    public Repeat getRepeatMonthlyFixed2() // repeat every third Thursday
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(9))
                .withSummary("Monthly Appointment Fixed2");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 15, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.UNTIL)
                .withUntil(LocalDateTime.of(2016, 10, 20, 10, 15))
                .withFrequency(Frequency.MONTHLY)
                .withMonthlyRepeat(MonthlyRepeat.DAY_OF_WEEK)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatYearlyFixed()
    {
        RepeatableAppointment a2 = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(22))
                .withSummary("Yearly Appointment Fixed");
        return RepeatFactory.newRepeat()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 7, 8, 45))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 45))
//                .withEndLocalTime(LocalTime.of(10, 15))
                .withEndCriteria(EndCriteria.NEVER)
                .withFrequency(Frequency.YEARLY)
                .withAppointmentData(a2);
    }
    
    public Repeat getRepeatDaily2()
    {
        RepeatableAppointment a = AppointmentFactory.newAppointment()
                .withAppointmentGroup(appointmentGroups.get(15))
                .withSummary("Daily Appointment Fixed2");
        return new RepeatImpl()
                .withStartLocalDate(LocalDateTime.of(2015, 10, 18, 8, 0))
                .withDurationInSeconds(5400)
//                .withStartLocalTime(LocalTime.of(8, 0))
//                .withEndLocalTime(LocalTime.of(9, 30))
                .withEndCriteria(EndCriteria.AFTER)
                .withFrequency(Frequency.DAILY)
                .withInterval(2)
                .withCount(5)
                .withAppointmentData(a);
    }
}
