package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.VEvent;

// TODO - Needs to know how to make Appointments for agenda
// Will be like Repeat class
// Needs a makeAppointments method
// Needs to have copy methods to copy from VEvent to appointment and visa-versa
// Should this implement Appointment?

public class VEventImpl extends VEvent
{

    /**
     * Make appointments that should exist between startDate and endDate based on Repeat rules.
     * Adds those appointments to the input parameter appointments Collection.
     * Doesn't make Appointment for dates that are already represented as individual appointments
     * as specified in usedDates.
     * sets startDate and endDate to private fields
     * 
     * @param appointments
     * @param startDateTime
     * @param endDateTime
     * @return
     */
    public Collection<RepeatableAppointment> makeAppointments(
            LocalDateTime startDateTime
          , LocalDateTime endDateTime)
    {
        Set<RepeatableAppointment> appointments = new HashSet<RepeatableAppointment>();
//        LocalDateTime endDate = getRRule().getEndRangeDateTime();
//        LocalDateTime startDate = getRRule().getStartRangeDateTime();

        final LocalDateTime myEndDate;
        if (getRRule().getUntil() == null) {
            myEndDate = endDateTime;
        } else {
//            System.out.println(endDate + " " + getEndOnDate());
//            LocalDateTime endOnDateTime = getEndOnDate().plusDays(1).atStartOfDay().minusNanos(1);
            myEndDate = (endDateTime.isBefore(getRRule().getUntil())) ? endDateTime : getRRule().getUntil();
        }
        // Below may only be needed if startDate is before DTStart
        LocalDateTime myStartDate = nextValidDateSlow(startDateTime.minusNanos(1));
        System.out.println("myStartDate " + myStartDate + " " + myEndDate);
//        System.out.println("StartDate " + startDate + " " + endDate + " " + this.getStartLocalDate() + " " +  getEndOnDate() );

        if (! myStartDate.isAfter(myEndDate))
        { // create set of appointment dates already used, to be skipped in making more
//            System.out.println("make appointments");
            // TODO - going to change from searching appointments to adding exceptions and individual recurrences
            final Set<LocalDateTime> usedDates = appointments()
                    .stream()
                    .map(a -> a.getStartLocalDateTime())
                    .peek(a -> System.out.println("used " + a))
                    .collect(Collectors.toSet());
            
            final Iterator<RepeatableAppointment> i = stream(getDateTimeStart())                            // appointment iterator
//                    .iterate(myStartDate, (a) -> a.with(new NextAppointment())) // infinite stream of valid dates
//                    .filter(a -> ! usedDates.contains(a))                       // filter out dates already used
//                    .filter(a -> ! getExceptions().contains(a))               // filter out deleted dates
                    .map(myStartDateTime -> {                                                 // make new appointment
//                        LocalDateTime myStartDateTime = a;
                        LocalDateTime myEndDateTime = myStartDateTime.plusSeconds(getDurationInSeconds());
                        System.out.println("appointmentClass2 " + getRRule().getAppointmentClass());
                        RepeatableAppointment appt = AppointmentFactory.newAppointment(getRRule().getAppointmentClass());
//                        RepeatableAppointment appt = (RepeatableAppointment) getNewAppointmentCallback()
//                            .call(new LocalDateTimeRange(myStartDateTime, myEndDateTime));
                        appt.setStartLocalDateTime(myStartDateTime);
                        appt.setEndLocalDateTime(myEndDateTime);
//                        appt.setRepeat(this);
                        appt.setRepeatMade(true);
//                        appt.setAppointmentGroup(getAppointmentData().getAppointmentGroup());
//                        appt.setDescription(getAppointmentData().getDescription());
//                        appt.setSummary(getAppointmentData().getSummary());
                        return appt;
                    })
                    .iterator();                                                // make iterator
            
            while (i.hasNext())
            { // Process new appointments
                final RepeatableAppointment a = i.next();
//                System.out.println("a --- " + a + " " + a.getStartLocalDateTime());
//                System.out.println("times " + a.getStartLocalDateTime().toLocalDate() + " " + (myEndDate));
                if (a.getStartLocalDateTime().isAfter(myEndDate)) break; // exit loop when at end
//                System.out.println("add " + a.getStartLocalDateTime());
//                repeatMap.add(a, this);                                                // add appointment and repeat to repeatMap
                appointments.add(a);                                                   // add appointments to main collection
                appointments().add(a);                                              // add appointments to this repeat's collection
//                repeatMap.put(a, this);
            }
//            isNew = false; // when makeAppointments is run first time set isNew to false
        }
        
        return appointments;
    }
 
    /**
     * Returns next valid date time starting with inputed date.  If inputed date is valid it is returned.
     * Iterates from first date until it passes the inputDate.  This make take a long time if the date
     * is far in the future.
     * 
     * @param inputDate
     * @return
     */
    // TODO - If this method is necessary consider using cache of dates for faster retrieval
    public LocalDateTime nextValidDateSlow(LocalDateTime inputDate)
    {
        if (inputDate.isBefore(getDateTimeStart())) return getDateTimeStart();
        final Iterator<LocalDateTime> i = getRRule().stream(inputDate).iterator();                                                            // make iterator
        while (i.hasNext())
        { // find date
            LocalDateTime s = i.next();
            if (s.isAfter(inputDate)) return s; // exit loop when beyond date without match
        }
        throw new InvalidParameterException("Can't find valid date starting at " + inputDate);
    }

}
