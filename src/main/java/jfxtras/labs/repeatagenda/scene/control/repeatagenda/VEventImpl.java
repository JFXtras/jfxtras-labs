package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.RRuleType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

// TODO - Needs to know how to make Appointments for agenda
// Will be like Repeat class
// Needs a makeAppointments method
// Needs to have copy methods to copy from VEvent to appointment and visa-versa
// Should this implement Appointment?

/**
 * Concrete class as an example of VEvent.
 * This class creates and edits appointments for display in Agenda.
 * 
 * Special use:
 * 3.8.1.2.  Categories: contains data for Appointment.appointmentGroup
 * 
 * @author David Bal
 * @param <T>
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
    public boolean isNewRRule() { return appointments().size() == 0; } // new RRule has no appointments
    
    // CONSTRUCTORS
    /** Copy constructor */
    public VEventImpl(VEventImpl vevent) {
        super(vevent);
        copy(vevent, this);
    }
    
    public VEventImpl() { }

    /** Deep copy all fields from source to destination */
    private static void copy(VEventImpl source, VEventImpl destination)
    {
        if (source.getAppointmentGroup() != null) destination.setAppointmentGroup(source.getAppointmentGroup());
        if (source.getAppointmentClass() != null) destination.setAppointmentClass(source.getAppointmentClass());
        source.appointments().stream().forEach(a -> destination.appointments().add(a));
    }
    
    /** Deep copy all fields from source to destination */
    public void copyTo(VEventImpl destination)
    {
        copy(this, destination);
    }

    /**
     * Returns appointments that should exist between dateTimeRangeStart and dateTimeRangeEnd based on VEvent.
     * For convenience, sets VEvent dateTimeRangeStart and dateTimeRangeEnd prior to making appointments.
     * 
     * @param dateTimeRangeStart
     * @param dateTimeRangeEnd
     * @return
     */
    public Collection<Appointment> makeAppointments(
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
    public Collection<Appointment> makeAppointments()
    {
        List<Appointment> madeAppointments = new ArrayList<Appointment>();
        stream(getDateTimeStart())
                .forEach(d -> {
                    System.out.println("getAppointmentClass: " + getDurationInSeconds());
                    RepeatableAppointment appt = AppointmentFactory.newAppointment(getAppointmentClass());
                    appt.setStartLocalDateTime(d);
                    appt.setEndLocalDateTime(d.plusSeconds(getDurationInSeconds()));
                    appt.setRepeatMade(true);
                    appt.setDescription(getDescription());
                    appt.setSummary(getSummary());
                    appt.setAppointmentGroup(getAppointmentGroup());
                    madeAppointments.add(appt);   // add appointments to main collection
                    appointments().add(appt); // add appointments to this repeat's collection
                });

        return madeAppointments;
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
    // TODO - it may not be necessary, remove if possible for improved efficiency
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
    
    public static void refreshVEventAppointments(VEventImpl vevent)
    {
        
    }
    
    
    /**
     * Handles editing VEvent objects.
     * 
     * @param dateTimeOld - start date/time before edit
     * @param dateTimeNew - start date/time after edit
     * @param durationInSecondsNew - duration after edit
     * @param vEventOld - copy from vEventOld into this if edit is canceled
     * @param appointments - list of all appointments in agenda
     * @param vevents - collection of all VEvents (add new VEvents if change to ONE or FUTURE)
     * @param changeDialogCallback - called to make dialog to prompt user for scope of edit (usually ONE, ALL, OR THIS_AND_FUTURE).  Parameter can be a simple predicate to force selection for testing (example: a -> ChangeDialogOptions.ONE).
     * @param writeVEventsCallback - called to do VEvent I/O if necessary.
     * @return
     */
    public WindowCloseType edit(
              LocalDateTime dateTimeOld
            , LocalDateTime dateTimeNew
            , int durationInSecondsNew
            , VEventImpl vEventOld
            , List<Appointment> appointments
            , Collection<VEvent> vevents
            , Callback<ChangeDialogOptions[], ChangeDialogOptions> changeDialogCallback
            , Callback<Collection<VEvent>, Void> writeVEventsCallback)
    {
        // Check if start time and duration has changed because those values are not changed in the edit controller.
        boolean dateTimeNewEqual = dateTimeNew.toLocalTime().equals(vEventOld.getDateTimeStart().toLocalTime());
        boolean durationEqual = (durationInSecondsNew == vEventOld.getDurationInSeconds());
        if (dateTimeNewEqual && durationEqual && this.equals(vEventOld)) return WindowCloseType.CLOSE_WITHOUT_CHANGE;

        final RRuleType rruleType = getVEventType(vEventOld.getRRule());
        System.out.println("rruleType " + rruleType);
        boolean editedFlag = true;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            this.setRRule(null);
        case WITH_NEW_REPEAT:
        case INDIVIDUAL:            
            break;
        case WITH_EXISTING_REPEAT:
            // Check if changes between vEvent and vEventOld exist apart from RRule
//            VEvent tempVEvent = VEventFactory.newVEvent(vEventOld);
            VEvent tempVEvent = new VEventImpl((VEventImpl) vEventOld);
            tempVEvent.setRRule(getRRule());
            boolean onlyRRuleChanged = this.equals(tempVEvent);

            ChangeDialogOptions[] choices = null;
            if (onlyRRuleChanged) choices = new ChangeDialogOptions[] {ChangeDialogOptions.ALL, ChangeDialogOptions.FUTURE};
            ChangeDialogOptions changeResponse = changeDialogCallback.call(choices);
            switch (changeResponse)
            {
            case ALL:
                // Copy date/time data to this VEvent
                long secondsAdjustment = ChronoUnit.SECONDS.between(dateTimeOld, dateTimeNew);
                LocalDateTime newDateTimeStart = getDateTimeStart().plusSeconds(secondsAdjustment);
                setDateTimeStart(newDateTimeStart);
                setDurationInSeconds(durationInSecondsNew);
                break;
            case CANCEL:
                editedFlag = false;
                break;
            case FUTURE:
                break;
            case ONE:
                // Make new individual VEvent, save settings to it.  Add date to original as recurrence.

                // make new VEvent for individual event
                VEventImpl newVEvent = new VEventImpl(this);
                newVEvent.setRRule(null);
                newVEvent.setDateTimeStart(dateTimeNew);
                // TODO - add UID for parent
                vevents.add(newVEvent);
                appointments.addAll(newVEvent.makeAppointments());

                // modify this VEvent for recurrence
                vEventOld.copyTo(this);                
                System.out.println("recurrence: " + dateTimeOld.minusSeconds(durationInSecondsNew));
                getRRule().getRecurrences().add(dateTimeOld);
                // TODO - ADD individual date to list of recurrence dates

//                System.out.println("ONE: " + this.getDurationInSeconds() + " " + vEventOld.getDurationInSeconds());
//
//                appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                System.out.println("size0: " + appointments.size() + " " + newVEvent.makeAppointments().size());
//                newVEvent.makeAppointments().stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                Appointment a2 = newVEvent.makeAppointments().iterator().next();
//                System.out.println("contains " + appointments.contains(a2));
                
                System.out.println("size0-: " + appointments.size());
                appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                System.exit(0);
//                vevents.add(vEventOld);
//                veventRefreshAppointments.call()
//                this = vEventOld;
                
                break;
            }
            break;
        default:
            break;
        }
        // TODO - THIS MAY MEAN THIS HAS TO GO BACK TO IMPL - CAN USE CALLBACK
        // DOESN'T KNOW ABOUT APPOINTMENTS HERE
        
        if (editedFlag) // make these changes as long as CANCEL is not selected
        { // remove appointments from mail collection made by VEvent
            System.out.println("Edited flag:");
            appointments.removeIf(a -> {
                return appointments().stream().anyMatch(a2 -> a2 == a);
            });
//            Iterator<Appointment> i = appointments.iterator();
//            while (i.hasNext())
//            {
//                Appointment a = i.next();
//                if (appointments().contains(a)) i.remove();
//            }
            appointments().clear(); // clear VEvent's collection of appointments
            System.out.println("size1: " + appointments.size());
            appointments.addAll(makeAppointments()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
            System.out.println("size2: " + appointments.size());
            return WindowCloseType.CLOSE_WITH_CHANGE;
        } else
        {
            return WindowCloseType.CLOSE_WITHOUT_CHANGE;
        }
    }
    
    private RRuleType getVEventType(RRule rruleOld)
    {

        if (getRRule() == null)
        {
            if (rruleOld == null)
            { // doesn't have repeat or have old repeat either
                return RRuleType.INDIVIDUAL;
            } else {
                return RRuleType.HAD_REPEAT_BECOMING_INDIVIDUAL;
            }
        } else
        { // RRule != null
            if (rruleOld == null)
            {
                return RRuleType.WITH_NEW_REPEAT;                
            } else {
                return RRuleType.WITH_EXISTING_REPEAT;
            }
        }
    }
    
}
