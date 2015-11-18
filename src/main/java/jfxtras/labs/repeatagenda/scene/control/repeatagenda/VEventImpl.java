package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

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

public class VEventImpl extends VEvent
{

    private AppointmentGroup appointmentGroup;
    public void setAppointmentGroup(AppointmentGroup appointmentGroup) { this.appointmentGroup = appointmentGroup; this.setCategories(appointmentGroup.getStyleClass()); }
    public AppointmentGroup getAppointmentGroup() { return appointmentGroup; }
    
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
                    appt.setStartLocalDateTime(d);
                    appt.setEndLocalDateTime(d.plusSeconds(getDurationInSeconds()));
                    appt.setRepeatMade(true);
                    appt.setDescription(getDescription());
                    appt.setSummary(getSummary());
                    appt.setAppointmentGroup(getAppointmentGroup());
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
    

    // its already edited by RepeatableController
    // changes to be made if ONE or FUTURE is selected.
    // change back if CANCEL
    public void edit(
            VEventImpl veventOld // change back if cancel
            , Collection<Appointment> appointments // remove affected appointments
            , Collection<VEvent> vevents // add new VEvents if change to one or future
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback // force change selection for testing
            , Callback<Collection<VEvent>, Void> writeVEventsCallback) // I/O callback
    {
        
    }
    
    /**
     * Edit appointments with parameters for the callbacks.
     * To do testing the two write callbacks should be set to stubs that do nothing.  Also, the changeDialogCallback
     * should be sent to return the RepeatChange option being tested (i.e. ALL).
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @param changeDialogCallback - code for the choice dialog selecting editing ALL, FUTURE, or ONE.  For testing return the RepeatChange being tested
     * @param writeAppointmentsCallback - code for writing appointments IO.  For testing do nothing
     * @param writeRepeatsCallback - code for writing repeats IO.  For testing do nothing
     * @return
     */
    /**
     * Edit appointments 
     * 
     * @param appointmentInput
     * @param appointmentOldInput
     * @param appointments
     * @param repeats
     * @param changeDialogCallback
     * @param writeAppointmentsCallback
     * @param writeRepeatsCallback
     * @return
     */
    // Can I edit only appointment here and use some callback to edit VEvent?
    // Goal is to make method generic enough that appointment making class can change and it stay the same.
    //
    public static WindowCloseType editVEvent(
              RepeatableAppointment appointmentInput
            , RepeatableAppointment appointmentOldInput
            , Collection<Appointment> appointments
            , Collection<VEvent> vevents
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback
            , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
            , Callback<Collection<Repeat>, Void> writeRepeatsCallback)
    {
        return null;
        
    }


    public static WindowCloseType editVEvent(
            RepeatableAppointment selectedAppointment,
            RepeatableAppointment appointmentOld, Set<Appointment> appointments,
            List<VEvent> vevents,
            Callback<RepeatChange[], RepeatChange> changeDialogCallback,
            Object writeAppointmentsCallback, Object writeRepeatsCallback) {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Options available when changing a repeatable appointment
     * ONE: Change only selected appointment
     * ALL: Change all appointments with repeat rule
     * FUTURE: Change future appointments with repeat rule
     * @author David Bal
     *
     */
    public enum RepeatChange {
        ONE, ALL, FUTURE, CANCEL;

        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
    public enum WindowCloseType
    {
        X, CANCEL, CLOSE_WITH_CHANGE, CLOSE_WITHOUT_CHANGE
    }


}
