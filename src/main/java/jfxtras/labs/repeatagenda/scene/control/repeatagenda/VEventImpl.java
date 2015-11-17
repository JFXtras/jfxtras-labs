package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.vevent.VEvent;

// TODO - Needs to know how to make Appointments for agenda
// Will be like Repeat class
// Needs a makeAppointments method
// Needs to have copy methods to copy from VEvent to appointment and visa-versa
// Should this implement Appointment?

/**
 * Concrete class as an example of VEvent.
 * This class creates appointments from VEvent objects for display in Agenda.
 * You can create other classes to make events for other uses.
 * 
 * Special use:
 * 3.8.1.2.  Categories  . . . . . . . . . . . . . . . . . . .  82 - Yes (home for appointmentGroup)
 * 
 * @author David Bal
 *
 */

public class VEventImpl extends VEvent<VEventImpl>
{

    /**
     *  VEventImpl doesn't know how to make an appointment.  An appointment factory makes new appointments.  The Class of the appointment
     * is an argument for the AppointmentFactory.  The appointmentClass is set in the constructor.  A RRule object is not valid without
     * the appointmentClass.
     */
    public Class<? extends RepeatableAppointment> getAppointmentClass() { return appointmentClass; }
    private Class<? extends RepeatableAppointment> appointmentClass;
    public void setAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { this.appointmentClass = appointmentClass; }
    public VEventImpl withAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }

    /**
     * The currently generated events of the recurrence set.
     * 3.8.5.2 defines the recurrence set as the complete set of recurrence instances for a
     * calendar component.  As many RRule definitions are infinite sets, a complete representation
     * is not possible.  The set only contains the events inside the bounds of 
     */
    public Set<RepeatableAppointment> appointments() { return myAppointments; }
    final private Set<RepeatableAppointment> myAppointments = new HashSet<RepeatableAppointment>();
//    public VEventImpl withAppointments(Collection<RepeatableAppointment> s) { appointments().addAll(s); return this; }
    
    
    /**
     * Returns appointments that should exist between dateTimeRangeStart and dateTimeRangeEnd based on VEvent.
     * For convenience, sets VEvent dateTimeRangeStart and dateTimeRangeEnd prior to making appointments.
     * 
     * @param dateTimeRangeStart
     * @param dateTimeRangeEnd
     * @return
     */
    public Collection<RepeatableAppointment> makeAppointments(
            LocalDateTime dateTimeRangeStart
          , LocalDateTime dateTimeRangeEnd)
    {
        setDateTimeRangeStart(dateTimeRangeStart);
        setDateTimeRangeEnd(dateTimeRangeEnd);
        return makeAppointments();
    }
    /**
     * Returns appointments that should exist between dateTimeRangeStart and dateTimeRangeEnd based on VEvent.
     * Uses dateTimeRange previously set in VEvent.
     * 
     * @return created appointments
     */
    public Collection<RepeatableAppointment> makeAppointments()
    {
        List<RepeatableAppointment> appointments = new ArrayList<RepeatableAppointment>();

            stream(getDateTimeStart())
                .forEach(d -> {
                    RepeatableAppointment appt = AppointmentFactory.newAppointment(getAppointmentClass());
                    // TODO - chain "with" methods
                    appt.setStartLocalDateTime(d);
                    appt.setEndLocalDateTime(d.plusSeconds(getDurationInSeconds()));
                    appt.setRepeatMade(true);
                    appt.setDescription(getDescription());
                    appt.setSummary(getSummary());
                    // TODO - appointmentGroup
                    appointments.add(appt);   // add appointments to main collection
                    appointments().add(appt); // add appointments to this repeat's collection
                });

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
