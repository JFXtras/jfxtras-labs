package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.security.InvalidParameterException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjuster;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.ParserConfigurationException;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.Repeat.EndCriteria;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda.Appointment;

/**
 * Static methods that delete and edit an appointment or repeatable appointments.  Through the
 * use of callbacks I/O methods are called to write changes to storage as well as system
 * memory.
 * 
 * @author david
 *
 */
public final class RepeatableUtilities {
    
    private RepeatableUtilities() {}

    /**
     * If repeat criteria has changed display this alert to find out how to apply changes (one, all or future)
     * Can provide a custom choiceList, or omit the list and use the default choices.
     * 
     * @param resources
     * @param choiceList
     * @return
     */
    public static RepeatChange repeatChangeDialog(RepeatChange...choiceList)
    {
        ResourceBundle resources = Settings.resources;
        List<RepeatChange> choices;
        if (choiceList == null || choiceList.length == 0)
        { // use default choices
            choices = new ArrayList<RepeatChange>();
            choices.add(RepeatChange.ONE);
            choices.add(RepeatChange.ALL);
            choices.add(RepeatChange.FUTURE);
        } else { // use inputed choices
            choices = new ArrayList<RepeatChange>(Arrays.asList(choiceList));
        }
               
        ChoiceDialog<RepeatChange> dialog = new ChoiceDialog<>(choices.get(0), choices);
        dialog.setTitle(resources.getString("dialog.repeat.change.title"));
        dialog.setContentText(resources.getString("dialog.repeat.change.content"));
        dialog.setHeaderText(resources.getString("dialog.repeat.change.header"));

        Optional<RepeatChange> result = dialog.showAndWait();
        
        return (result.isPresent()) ? result.get() : RepeatChange.CANCEL;
    }
    
    /**
     * Alert to confirm delete appointments
     * 
     * @param resources
     * @param appointmentQuantity
     * @return
     */
    private static Boolean confirmDelete(String appointmentQuantity)
    {
        ResourceBundle resources = Settings.resources;
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(resources.getString("alert.repeat.delete.title"));
        alert.setContentText(resources.getString("alert.repeat.delete.content"));
        alert.setHeaderText(appointmentQuantity + " " + resources.getString("alert.repeat.delete.header"));

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }
    
    // TODO - REMOVE BELOW METHOD - ALL CALLS TO DELETE NEED I/O CALLBACKS
    /**
     * Handles deleting an appointment.  If appointment is repeatable displays a dialog
     * to find out if delete is for one, all, or future appointments.
     * Uses default callback for dialog
     * 
     * @param resources
     * @param appointments
     * @param appointment
     * @return 
     * @throws ParserConfigurationException 
     */
    public static WindowCloseType deleteAppointments(
              RepeatableAppointment appointment
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats) throws ParserConfigurationException
    {
        return deleteAppointments(
                appointment
              , appointments
              , repeats
//              , repeatMap
              , a -> repeatChangeDialog()
              , a -> confirmDelete(a)
              , null
              , null);
    }
    
    /**
     * Handles deleting an appointment.  If appointment is repeatable displays a dialog
     * to find out if delete is for one, all, or future appointments.
     * 
     * @param resources
     * @param appointments
     * @param appointment
     * @return 
     * @throws ParserConfigurationException 
     */
    public static WindowCloseType deleteAppointments(
              Appointment appointmentInput
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
//            , Map<Appointment, Repeat> repeatMap
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback
            , Callback<String, Boolean> confirmDeleteCallback
            , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
            , Callback<Collection<Repeat>, Void> writeRepeatsCallback) throws ParserConfigurationException
    {
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentInput;
        ResourceBundle resources = Settings.resources;
        final Repeat repeat = (appointment instanceof RepeatableAppointment) ? ((RepeatableAppointment) appointment).getRepeat() : null;
//        final Repeat repeat = repeatMap.get(appointment);
        final AppointmentType appointmentType = (repeat == null)
                ? AppointmentType.INDIVIDUAL : AppointmentType.WITH_EXISTING_REPEAT;
        boolean writeAppointments = false;
        boolean writeRepeats = false;
        
        RepeatChange changeResponse;
        RepeatChange[] choices = null;
        switch (appointmentType)
        {
        case INDIVIDUAL:
            if (confirmDeleteCallback.call("1"))
            { // remove individual appointment that has no repeat rule
                writeAppointments = removeOne(appointments, appointment);
                if (! writeAppointments) throw new IllegalArgumentException("Appointment can't be deleted - not found ("
                        + appointment.getSummary() + ")");
            }
            break;
        case WITH_EXISTING_REPEAT:
            // TODO - IF APPOINTMENT SELECTED IS LAST APPOINTMENT IN SERIES ONLY OFFER INDIVIDUAL AND ALL - NO FUTURE
            if (appointment.getStartLocalDateTime().toLocalDate().equals(repeat.getStartLocalDateTime()))
            {
                choices = new RepeatChange[] {RepeatChange.ONE, RepeatChange.ALL};
            }
            changeResponse = changeDialogCallback.call(choices);

            if (changeResponse == RepeatChange.CANCEL) return WindowCloseType.CLOSE_WITHOUT_CHANGE; // cancel selected
            final Predicate<? super Appointment> myFilter;
            final LocalDateTime startDate = appointment.getStartLocalDateTime();
            final String matchingAppointmentsString;
            final int matchingAppointments;
            int deletedAppointments = 0;
            switch (changeResponse)
            {
            case ONE:
                if (confirmDeleteCallback.call("1"))
                {
                    writeRepeats = removeOne(appointments, appointment);
                    if (writeRepeats) removeOne(repeat.appointments(), appointment);
                    if (startDate.equals(repeat.getUntilLocalDateTime()))
                    { // deleted appointment is on end date, adjust end date and number of appointments
                        repeat.setUntil(startDate.minusDays(1));
                        if (repeat.getEndCriteria().equals(EndCriteria.AFTER))
                        { // decrement end after events
                            repeat.setCount(repeat.getCount()-1);
                        }
                    } else { // deleted appointment is not end date, add date to deleted dates list
                        repeat.getExceptions().add(appointment.getStartLocalDateTime());
                    }
                    writeRepeats = true;
                }
                break;
            case ALL:
//                myFilter = (a) -> a.getRepeat() == repeat; // predicate to filter out all appointments with repeat
                myFilter = (a) -> {
//                    return repeatMap.get(a) == repeat;
                    RepeatableAppointment a2 = (RepeatableAppointment) a;
                    return a2.getRepeat() == repeat; // predicate to filter out all appointments with repeat
                };
                matchingAppointments = (int) repeat.streamOfDates().count();
                matchingAppointmentsString = (repeat.getEndCriteria() == EndCriteria.NEVER)
                        ? resources.getString("infinite") : Integer.toString(matchingAppointments);
                if (confirmDeleteCallback.call(matchingAppointmentsString))
                {
                    appointments.removeIf(myFilter);
//                    appointments.stream()
//                        .forEach(a -> {
//                            RepeatableAppoitment a2 = 
//                        });
                    deletedAppointments = matchingAppointments;
                    repeats.remove(repeat);
                    writeRepeats = true;
                }
                break;
            case FUTURE:
                myFilter = (a) ->
                { // predicate to filter out all appointments with repeat and are equal or after startDate
                    LocalDateTime myDate = a.getStartLocalDateTime();
                    RepeatableAppointment a2 = (RepeatableAppointment) a;
                    return ((a2.getRepeat() == repeat) && (myDate.isAfter(startDate) || myDate.equals(startDate)));
                };
                matchingAppointments = (int) repeat
                        .streamOfDates()
                        .filter(a -> (a.isAfter(startDate) || a.equals(startDate)))
                        .count();
                matchingAppointmentsString = (repeat.getEndCriteria() == EndCriteria.NEVER)
                        ? resources.getString("infinite") : Integer.toString(matchingAppointments);
                if (confirmDeleteCallback.call(matchingAppointmentsString))
                {
                    appointments.removeIf(myFilter);
                    deletedAppointments = matchingAppointments;
                    switch (repeat.getEndCriteria())
                    {
                        case NEVER: // convert to end ON
                            repeat.setEndCriteria(EndCriteria.UNTIL);
                            repeat.setUntil(startDate.minusDays(1));
//                            repeat.makeEndAfterEventsFromEndOnDate();
                            break;
                        case AFTER: // reduce quantity by deleted quantity
                            repeat.setCount(repeat.getCount() - deletedAppointments);
                            // drop through
                        case UNTIL:
                            repeat.setUntil(startDate.minusDays(1));
                            break;
                        default:
                            break;
                    }
                    repeat.updateAppointments(appointments, appointment);
                    writeRepeats = true;
                }
                break;
            default:
                break;
            }
            // Check if repeat has only one appointment and should become individual
            if (repeat.oneAppointmentToIndividual(repeats, appointments)) writeAppointments = true;
            break;
        default:
            break; // shouldn't get here (unknown AppointmentType)
        }
        
        // Write changes that occurred
//        if (writeAppointments && (writeAppointmentsCallback != null)) writeAppointmentsCallback.call(appointments); // write appointment changes
//        if (writeRepeats && (writeRepeatsCallback != null)) writeRepeatsCallback.call(repeats);                     // write repeat changes
        
        return (writeAppointments || writeRepeats) ? WindowCloseType.CLOSE_WITH_CHANGE : WindowCloseType.CLOSE_WITHOUT_CHANGE;
    }

    //TODO - STOP USING UPDATEAPPOINTMENTS - INSTEAD CLEAR ALL REPEAT-MADE APPOINTMENTS AND
    // MAKE NEW ONES - SHOULD BE MUCH EASIER AND LESS ERROR PRONE
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
    // Works for by drag-and-drop on the agenda and for editing from AppointmentEditController
    public static WindowCloseType editAppointments(
              RepeatableAppointment appointmentInput
            , RepeatableAppointment appointmentOldInput
            , Collection<Appointment> appointments
            , Collection<Repeat> repeats
//            , Map<Appointment, Repeat> repeatMap
            , Callback<RepeatChange[], RepeatChange> changeDialogCallback
//            , Collection<RepeatableAppointment> editedAppointments
//            , Collection<Repeat> editedRepeats)
            , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
            , Callback<Collection<Repeat>, Void> writeRepeatsCallback)
    {
        RepeatableAppointment appointment = (RepeatableAppointment) appointmentInput;
        RepeatableAppointment appointmentOld = (RepeatableAppointment) appointmentOldInput;
//        System.out.println("appointmentOld6  " + appointmentOld);
//        System.exit(0);

        final ResourceBundle resources = Settings.resources;
        Repeat repeat = appointment.getRepeat(); // repeat with new changes
//        final Repeat repeat = repeatMap.get(appointment); // repeat with new changes
//        final Repeat repeatOld = repeatMap.get(appointmentOld); // repeat with new changes
//        System.out.println("repeat1 " + repeat.getEndCriteria());
//        System.out.println("repeatOld1 " + repeatOld.getEndCriteria());

//        System.exit(0);
        final Repeat repeatOld = appointmentOld.getRepeat(); // repeat prior to changes
        Set<RepeatableAppointment> editedAppointmentsTemp = new HashSet<RepeatableAppointment>();
        
//        System.out.println(appointment + " " + appointmentOld);
//        System.exit(0);
        final boolean appointmentChanged = ! appointment.equals(appointmentOld);
        final boolean repeatChanged;
        if (repeatOld == null && repeat == null)
        {
            repeatChanged = false;
        } else if ((repeatOld == null && repeat != null) || (repeatOld != null && repeat == null))
        {
            repeatChanged = true;
        } else
        {
            repeatChanged = ! repeat.equals(repeatOld);
        }
        if (! appointmentChanged && ! repeatChanged) return WindowCloseType.CLOSE_WITHOUT_CHANGE;

        // Make temporal adjusters for time and/or day shift
        final LocalDateTime startDate = appointment.getStartLocalDateTime();
//        System.out.println("start date time " + startDate);
        final LocalDateTime startDateOld = appointmentOld.getStartLocalDateTime();
        final int dayShift = Period.between(startDateOld.toLocalDate(), startDate.toLocalDate()).getDays();
        final int startMinuteShift = (int) Duration.between(appointmentOld.getStartLocalDateTime()
                                               , appointment.getStartLocalDateTime()).toMinutes();
        System.out.println("startMinuteShift " + startMinuteShift);
        final int endMinuteShift = (int) Duration.between(appointmentOld.getEndLocalDateTime()
                , appointment.getEndLocalDateTime()).toMinutes();
        final TemporalAdjuster startTemporalAdjuster = temporal ->
        { // adjusts original startLocalDateTime to new
              LocalDateTime t = LocalDateTime.from(temporal);
              t = t.plusMinutes(startMinuteShift);
              return t;
        };
        final TemporalAdjuster endTemporalAdjuster = temporal ->
        { // adjusts original endLocalDateTime to new
              LocalDateTime t = LocalDateTime.from(temporal);
              t = t.plusMinutes(endMinuteShift);
              return t;
        };

        RepeatChange[] choices = null;
        RepeatChange changeResponse;
        boolean editedAppointmentsFlag = false;
        boolean editedRepeatsFlag = false;

        // FIND OUT WHICH TYPE OF APPOINTMENT IS BEING EDITED
        final AppointmentType appointmentType = makeAppointmentType(repeat, repeatOld);
        System.out.println("appointmentType " + appointmentType);
        switch (appointmentType)
        {
        case INDIVIDUAL:
            editedAppointmentsFlag = true;
            editedAppointmentsTemp.add(appointment);
            break;
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
        {
//            changeResponse = changeDialogCallback.call(choices);
//            switch (changeResponse)
//            {
//            case ONE: // remove repeatKey from appointment, add date to skip dates in repeat, write both
//                repeatOld.getExceptions().add(startDate);
//                break;
//            case ALL: // remove repeatKey from appointment, delete repeat
//                repeats.remove(repeat);
//                break;
//            case FUTURE: // change end of repeat to appointment date (I'm not sure what the user expects in this case)
//                final Repeat repeatOriginal = appointmentOld.getRepeat();
//                final Set<LocalDateTime> dates = repeatOriginal.getExceptions()
//                         .stream()
//                         .filter(a -> a.isBefore(startDate))
//                         .collect(Collectors.toSet());
//                repeatOriginal.setExceptions(dates);
//                repeatOriginal.setUntilLocalDateTime(startDate.minusDays(1));
//                switch (repeatOriginal.getEndCriteria())
//                {
//                case NEVER:
//                    repeatOriginal.setEndCriteria(EndCriteria.UNTIL);
//                    break;
//                case AFTER:
//                    repeatOriginal.makeCountFromUntil();
//                    break;
//                default:
//                    break;
//                }
//                break;
//            case CANCEL:
//                return WindowCloseType.CLOSE_WITHOUT_CHANGE;
//            }

            Iterator<RepeatableAppointment> appointmentIterator = repeatOld.appointments().iterator();
            while (appointmentIterator.hasNext())
            {
                Appointment myAppointment = appointmentIterator.next();
                if (myAppointment != appointment) removeOne(appointments, myAppointment);
            }
            repeats.remove(repeatOld);
            appointment.setRepeatMade(false);
            editedRepeatsFlag = true;
            editedAppointmentsFlag = true;
            editedAppointmentsTemp.add(appointment);
            break;
        }
        case WITH_NEW_REPEAT: // don't display edit dialog - just make appointments
//            System.out.println("WITH_NEW_REPEAT");
          repeat.unbindAll();
          appointment.setRepeat(repeat);
//          repeatMap.put(appointment, repeat);
          repeat.appointments().add(appointment);
//          System.out.println(repeat.getStartLocalDate());
//          System.exit(0);
          Collection<RepeatableAppointment> newAppointments = repeat.makeAppointments();
          System.out.println("newAppointments " + newAppointments.size() + " " + repeat.appointments());
          appointments.addAll(newAppointments);
          appointment.copyFieldsTo(repeat.getAppointmentData()); // copy any appointment changes (i.e. description, group, location, etc)
          repeats.add(repeat);
          appointment.setRepeatMade(true);
          editedRepeatsFlag = true;
          if (! appointmentOld.isRepeatMade()) {
              editedAppointmentsFlag = true; // only edit appointments if appointment existed previously
          }
          break;
        case WITH_EXISTING_REPEAT:
            if (! appointmentChanged) choices = new RepeatChange[] {RepeatChange.ALL, RepeatChange.FUTURE};
//            changeResponse = repeatChangeDialog(choices);
            changeResponse = changeDialogCallback.call(choices);
            switch (changeResponse)
            {
            case ONE:
                // TODO - NEED TO DECIDE HOW REPEATS KEEP TRACK OF RECURRANCES
                appointment.setRepeatMade(false);
                appointment.setRecurrance(appointmentOld.getStartLocalDateTime());
                if (startMinuteShift != 0 || endMinuteShift != 0)
                { // if appointment has new day or time make it individual
//                    appointment.setRepeat(null); // make appointment individual if time changes
//                    repeatMap.remove(appointment);
                    repeat.getExceptions().add(startDateOld); // TODO - THIS MAY BE WRONG - EXCEPTIONS ARE DELETED DATES, THIS IS A RECURRANCE
                    editedRepeatsFlag = true;
                }
//                writeAppointments = true;
                editedAppointmentsTemp.add(appointment);
                if (repeatOld != null)
                {
                    repeat.unbindAll();
                    repeatOld.copyFieldsTo(repeat);   // restore original repeat rule
                }
                break;
            case ALL:
                repeat.unbindAll();
                if (appointment.isRepeatMade())
                { // copy all appointment changes (i.e. description, group, location, etc)
                    appointment.copyFieldsTo(repeat.getAppointmentData());
//                    repeat.copyInto(appointment.getRepeat());
                } else { // copy non-unique appointment changes (i.e. description, group, location, etc)
//                    appointment.copyInto(repeat.getAppointmentData(), appointmentOld);
                    repeat.getAppointmentData().copyNonUniqueFieldsTo(appointment, appointmentOld);
//                    repeat.copyAppointmentInto(appointment, appointmentOld);
//                    appointment.copyInto(repeat.getAppointmentData(), appointmentOld);
                }
                switch (repeat.getFrequency())
                {
                case DAILY: // fall through
                case MONTHLY: // fall through
                case YEARLY:
                    repeat.adjustDateTime(true, startTemporalAdjuster, endTemporalAdjuster);
                    switch (repeat.getEndCriteria())
                    {
                    case AFTER:
                        repeat.makeUntilFromCount();
                        break;
                    case UNTIL:
//                        repeat.makeEndAfterEventsFromEndOnDate();
                        break;
                    case NEVER:
                        break;
                    }
//                    System.out.println("before " + repeat.getStartLocalDate() + " " + repeat.getStartLocalTime());
//                    repeat.adjustDateTime(true, startTemporalAdjuster, endTemporalAdjuster);
//                    System.out.println("after " + repeat.getStartLocalDate() + " " + repeat.getStartLocalTime());
                    break;
                case WEEKLY:
//                    System.out.println("dayShift " + dayShift);
                    if (dayShift != 0)
                    { // change selected day of week if there is a day shift
                        final DayOfWeek dayOfWeekOld = appointmentOld.getStartLocalDateTime().getDayOfWeek();
                        final DayOfWeek dayOfWeekNew = appointment.getStartLocalDateTime().getDayOfWeek();
                        repeat.setDayOfWeek(dayOfWeekOld, false);
                        repeat.setDayOfWeek(dayOfWeekNew, true);
                    }
//                    boolean adjustStartDate = dayShift == 0;
                    final DayOfWeek d1 = appointment.getStartLocalDateTime().getDayOfWeek();
                    final DayOfWeek d2 = startDate.getDayOfWeek();
                    boolean adjustStartDate = d1 == d2;
                    System.out.println("startDateOld " + startDateOld + " " + startDate + " " + adjustStartDate);
                    repeat.adjustDateTime(adjustStartDate, startTemporalAdjuster, endTemporalAdjuster);
                }
//                System.out.println("size1 " + appointments.size());
//                appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                System.exit(0);
//                repeat.updateAppointments(appointments, appointment, appointmentOld
//                        , startTemporalAdjuster, endTemporalAdjuster);
                
                editedAppointmentsTemp.addAll(repeat
                      .appointments()
                      .stream()
                      .filter(a -> ! a.isRepeatMade())
                      .collect(Collectors.toSet()));
//                System.out.println("size2 " + appointments.size());
                editedRepeatsFlag = true;
                break;
            case FUTURE:
                LocalDateTime newLastStartDateTime;
                repeat.unbindAll();
                // Copy changes to repeat  (i.e. description, group, location, etc)
                repeat.setStartLocalDate(startDate);
                if (appointment.isRepeatMade())
                { // copy all appointment changes
                    appointment.copyFieldsTo(repeat.getAppointmentData());
                } else { // copy non-unique appointment changes
                    repeat.getAppointmentData().copyNonUniqueFieldsTo(appointment, appointmentOld);
//                    repeat.copyAppointmentInto(appointment, appointmentOld);
//                    appointment.copyAppointmentInto(repeat.getAppointmentData(), appointmentOld);
                }
                
                // Split deleted dates between repeat and repeatOld
                repeatOld.getExceptions().clear();
                final Iterator<LocalDateTime> exceptionIterator = repeat.getExceptions().iterator();
                while (exceptionIterator.hasNext())
                {
                    LocalDateTime d = exceptionIterator.next();
                    if (d.isBefore(startDate))
                    {
                        exceptionIterator.remove();
                    } else {
                        repeatOld.getExceptions().add(d);
                    }
                }
                
                // Split recurrences between repeat and repeatOld
                repeatOld.getRecurrences().clear();
                final Iterator<LocalDateTime> recurrenceIterator = repeat.getRecurrences().iterator();
                while (recurrenceIterator.hasNext())
                {
                    LocalDateTime d = recurrenceIterator.next();
                    if (d.isBefore(startDate))
                    {
                        recurrenceIterator.remove();
                    } else {
                        repeatOld.getRecurrences().add(d);
                    }
                }
                
//                // Split appointments between repeat and repeatOld
//                repeatOld.appointments().clear();
//                Iterator<RepeatableAppointment> appointmentIterator = repeat.appointments().iterator();
//                while (appointmentIterator.hasNext())
//                {
//                    RepeatableAppointment a = appointmentIterator.next();
//                    if (a.getStartLocalDateTime().isBefore(startDate))
//                    {
//                        appointmentIterator.remove();
//                        repeatOld.appointments().add(a);
//                        a.setRepeat(repeatOld);
//                    }
//                }
                
                // Modify start and end date for repeat and repeatOld.  Adjust IntervalUnit specific data
                repeatOld.setEndCriteria(EndCriteria.UNTIL);
                repeatOld.setCount(0); // criteria changed to ON so 0 count events
                repeatOld.setUntil(repeatOld.previousValidDate(startDateOld));
//                boolean adjustStartDate;
                switch (repeat.getFrequency())
                {
                case DAILY: // fall through
                case MONTHLY: // fall through
                case YEARLY:
//                    adjustStartDate = startDate.equals(repeat.getStartLocalDate());
//                    System.out.println("repeat.getEndAfterEvents( " + repeat.getEndAfterEvents() + " " + repeat.getEndOnDate() + " " + dayShift);
                    repeat.adjustDateTime(false, startTemporalAdjuster, endTemporalAdjuster);
//                    System.out.println("future nulls4 " + repeat + repeat.getUntilLocalDateTime());
                    if (repeat.getEndCriteria() != EndCriteria.NEVER)
                    {
                        newLastStartDateTime = repeat.getUntilLocalDateTime().with(startTemporalAdjuster);
                        repeat.setUntil(newLastStartDateTime);
                    }
//                    if (repeat.getEndCriteria() == EndCriteria.AFTER) repeat.makeEndAfterEventsFromEndOnDate();

                    //                    System.out.println("repeat.getEndAfterEvents( " + repeat.getEndAfterEvents() + " " + repeat.getEndOnDate());
//                    switch (repeat.getEndCriteria())
//                    {
//                    case AFTER:
//                        repeat.makeEndOnDateFromEndAfterEvents();
//                        repeat.makeEndAfterEventsFromEndOnDate();
//                        break;
//                    case ON:
//                        repeat.makeEndAfterEventsFromEndOnDate();
//                        break;
//                    case NEVER:
//                        break;
//                    }
//                    System.out.println("start list");
//                    appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                    System.out.println("before update " + appointments.size());
//                    repeatOld.updateAppointments(appointments, appointment);
//                    appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                    System.out.println("after update " + appointments.size());
//                    System.out.println(repeat);
                    break;
                case WEEKLY:
                    final DayOfWeek dayOfWeekNew = appointment.getStartLocalDateTime().getDayOfWeek();
                    if (dayShift != 0)
                    { // change selected day of week if there is a day shift
                        final DayOfWeek dayOfWeekOld = appointmentOld.getStartLocalDateTime().getDayOfWeek();
                        System.out.println("day of week " + dayOfWeekOld + " " + dayOfWeekNew + " ");
                        repeat.setDayOfWeek(dayOfWeekOld, false);
                        repeat.setDayOfWeek(dayOfWeekNew, true);
                    }
                    // Adjust day and time if same day of week as edited day, otherwise just adjust time
                    final DayOfWeek lastDayOfWeek = repeat.getUntilLocalDateTime().getDayOfWeek();
                    if (lastDayOfWeek == dayOfWeekNew)
                    { // adjust date and time
                        newLastStartDateTime = repeat.getUntilLocalDateTime().with(startTemporalAdjuster);
                        repeat.setUntil(newLastStartDateTime);
                    } else
                    { // adjust time only
                        newLastStartDateTime = repeat.getUntilLocalDateTime().toLocalDate().atTime(startDate.toLocalTime());
                        repeat.setUntil(newLastStartDateTime);
                    }
                    
//                    final LocalDateTime earliestDate = getStartLocalDate();
                    // If editing a weekly repeat, moving one day past a different day can change the start date.  The method adjusts
                    // the start date if necessary
//                    System.out.println("startDate1 " + startDate + " " + startDateOld);
                    final DayOfWeek d1 = startDateOld.getDayOfWeek();
                    final Iterator<DayOfWeek> daysIterator = Stream
                        .iterate(d1, (a) ->  a.plus(1))              // infinite stream of days of the week
                        .limit(7)                                    // next valid day should be found within 7 days
                        .iterator();
                    int dayShift2 = 0;
                    while (daysIterator.hasNext())
                    {
                        DayOfWeek d = daysIterator.next();
                        if (repeat.getDayOfWeek(d)) break; // check if day of week is true
                        dayShift2++;
                    }
                    if (dayShift2 > 0)
                    {
                        LocalTime time = repeat.getStartLocalDateTime().toLocalTime();
                        LocalDate date = startDateOld.plusDays(dayShift2).toLocalDate();
                        repeat.setStartLocalDate(LocalDateTime.of(date, time));
                    }
                    
//                    final LocalDateTime oldEndLocalDateTime = repeatOld.getStartLocalDate().plusSeconds(repeat.getDurationInSeconds());
//                    final LocalDateTime newEndLocalDateTime = oldEndLocalDateTime.with(endTemporalAdjuster);
                    int newDuration = (int) ChronoUnit.SECONDS.between(appointment.getStartLocalDateTime(), appointment.getEndLocalDateTime());
                    repeat.setDurationInSeconds(newDuration);

                    ////                    adjustStartDate = startDate.equals(repeat.getStartLocalDate());
//                    repeat.adjustDateTime(false, startTemporalAdjuster, endTemporalAdjuster);
//                    System.exit(0);
                }
//                System.out.println("appointments1 " + repeat.getAppointments().size());
                if (repeat.getEndCriteria() != EndCriteria.NEVER) repeat.makeCountFromUntil();
//                repeat.updateAppointments(appointments, appointment, appointmentOld
//                        , startTemporalAdjuster, endTemporalAdjuster);
                System.out.println("appointments2 " + repeat.appointments().size());
                repeats.add(repeatOld);
//                appointments.stream().forEach(a -> System.out.println(a.getStartLocalDateTime()));
//                System.out.println("after update2 " + appointments.size());
//                System.out.println("repeatOld.getEndOnDate() " + repeatOld.getEndOnDate() + " " + repeatOld.getStartLocalDate());
//                System.out.println("repeat.getEndOnDate() " + repeat.getEndOnDate() + " " + repeat.getStartLocalDate());
//              System.exit(0);                
                editedRepeatsFlag = true;
                
                // TODO - IF I'M DELETING ALL APPOINTMENTS THEN WHY EDIT THEM?  I SHOULD EDIT REPEAT AND IGNORE CHANGES TO APPOINTMENTS
                // Delete appointments
                repeat.appointments()
                    .stream()
                    .filter(a -> a.isRepeatMade())
                    .forEach(a -> appointments.remove(a));
                repeat.appointments().clear();
                
                repeatOld.appointments()
                    .stream()
                    .filter(a -> a.isRepeatMade())
                    .forEach(a -> appointments.remove(a));
                repeatOld.appointments().clear();
                
                System.out.println("removed apopintments " + appointments.size());
                // Make new appointments
                appointments.addAll(repeat.makeAppointments());
                appointments.addAll(repeatOld.makeAppointments());
                System.out.println("added apopintments " + appointments.size());
                
                if (repeatOld.oneAppointmentToIndividual(repeats, appointments)) editedAppointmentsFlag = true;  // Check if any repeats have only one appointment and should become individual
                // TODO - CHANGES OF CONVERT TO ONE APPOINTMENT
                break;
            case CANCEL: // restore old appointment and repeat rule (use copyInto to avoid triggering change listeners)
                appointmentOld.copyFieldsTo(appointment);
                repeatOld.copyFieldsTo(repeat); // This one may be unnecessary if copy above is deep             
            
//                Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//                        .stream(DayOfWeek.values())
//                        .limit(7)
//                        .iterator();
//                    while (dayOfWeekIterator.hasNext())
//                    {
//                        DayOfWeek key = dayOfWeekIterator.next();
//                        boolean b1 = repeat.getDayOfWeekMap().get(key).get();
//                        boolean b2 = repeatOld.getDayOfWeekMap().get(key).get();
//                        System.out.println("day of week2 " + key + " " + b1 + " " + b2);
//                    }
                
//                System.out.println("repeatMap.size()- " + repeatMap.size());
//                System.exit(0);
//              Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//              .stream(DayOfWeek.values())
//              .limit(7)
//              .iterator();
//          while (dayOfWeekIterator.hasNext())
//          {
//              DayOfWeek key = dayOfWeekIterator.next();
//              boolean b1 = repeat.getDayOfWeekMap().get(key).get();
//              boolean b2 = repeatOld.getDayOfWeekMap().get(key).get();
//              System.out.println("copied day of week2 " + key + " " + b1 + " " + b2);
//          }
            default: // do nothing
                return WindowCloseType.CLOSE_WITHOUT_CHANGE;
            }
            // Check if any repeats have only one appointment and should become individual
            if (repeat.oneAppointmentToIndividual(repeats, appointments)) editedAppointmentsFlag = true;
            break;
        default:
            throw new InvalidParameterException("Invalid Appointment Type");
        }
        
        if (editedRepeatsFlag && (repeat != null))
        {
            // Delete appointments
//            System.out.println(repeat);
//            System.out.println(repeat.appointments());
            repeat.appointments()
                .stream()
                .filter(a -> a.isRepeatMade())
                .forEach(a -> appointments.remove(a));
            repeat.appointments().clear();

            // Make new appointments
            appointments.addAll(repeat.makeAppointments());
        }
        
        // Write changes that occurred
        System.out.println("write flags " + editedAppointmentsFlag +  " " + editedRepeatsFlag);
//        if (writeAppointments) AppointmentFactory.writeToFile(appointments);
//        if (writeRepeats) MyRepeat.writeToFile(repeats);

        if (editedAppointmentsFlag && (writeAppointmentsCallback != null)) writeAppointmentsCallback.call(appointments); // write appointment changes
        if (editedRepeatsFlag && (writeRepeatsCallback != null)) writeRepeatsCallback.call(repeats);                     // write repeat changes

//        if (editedAppointmentsFlag) editedAppointments.addAll(editedAppointmentsTemp);
//        if (editedRepeatsFlag) editedRepeats.add(repeat);                     // write repeat changes

        
        return (editedAppointmentsFlag || editedRepeatsFlag) ? WindowCloseType.CLOSE_WITH_CHANGE : WindowCloseType.CLOSE_WITHOUT_CHANGE;
        
    }

    // TODO - REMOVE BELOW METHOD - ALL CALL TO EDIT NEED I/O CALLBACKS
    /**
     * Edit repeatable appointment.
     * Uses default callbacks for dialog, write appointments and write repeats
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @return
     */
    public static WindowCloseType editAppointments(
              Collection<Appointment> appointments
            , RepeatableAppointment appointment
            , RepeatableAppointment appointmentOld
            , Collection<Repeat> repeats)
//            , Collection<RepeatableAppointment> editedAppointments
//            , Collection<Repeat> editedRepeats)
    {
        return editAppointments(
                appointment
              , appointmentOld
              , appointments
              , repeats
//              , repeatMap
              , a -> repeatChangeDialog()
//              , editedAppointments
//              , editedRepeats);
              , null
              , null);
    }
    
    
    /**
     * Edit repeatable appointment.
     * Uses default callbacks for dialog, write appointments and write repeats
     * 
     * @param appointments
     * @param appointment
     * @param appointmentOld
     * @param repeats
     * @return
     */
    public static WindowCloseType editAppointments(
            RepeatableAppointment appointment
          , RepeatableAppointment appointmentOld
          , Collection<Appointment> appointments
          , Collection<Repeat> repeats
//          , Map<Appointment, Repeat> repeatMap
          , Callback<Collection<Appointment>, Void> writeAppointmentsCallback
          , Callback<Collection<Repeat>, Void> writeRepeatsCallback)
    {
        return editAppointments(
                appointment
              , appointmentOld
              , appointments
              , repeats
//              , repeatMap
              , a -> repeatChangeDialog()
              , writeAppointmentsCallback
              , writeRepeatsCallback);
    }

    
    
    
    private static AppointmentType makeAppointmentType(Repeat repeat, Repeat repeatOld)
    {

        if (repeat == null)
        {
            if (repeatOld == null)
            { // doesn't have repeat or have old repeat either
                return AppointmentType.INDIVIDUAL;
            } else {
                return AppointmentType.HAD_REPEAT_BECOMING_INDIVIDUAL;
            }
        } else
        {
            if (repeat.isNew())
            {
                return AppointmentType.WITH_NEW_REPEAT;                
            } else {
                return AppointmentType.WITH_EXISTING_REPEAT;
            }
        }
    }

    private enum AppointmentType {
        INDIVIDUAL
      , WITH_EXISTING_REPEAT
      , WITH_NEW_REPEAT
      , HAD_REPEAT_BECOMING_INDIVIDUAL
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
    
    /**
     * Removes an element from a collection.
     * Similar to removeIf, but quits when one remove occurs
     * 
     * @param collection
     * @param element
     * @return
     */
    public static <T> boolean removeOne(Collection<T> collection, T element) {
        Iterator<T> i = collection.iterator();
        while (i.hasNext()) {
            T a = i.next();
            if (a == element) {
                i.remove();
                return true;
            }
        }
        return false;
    }
    
}
