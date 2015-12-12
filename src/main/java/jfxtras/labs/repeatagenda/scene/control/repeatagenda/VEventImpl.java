package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceDialog;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.ChangeDialogOptions;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.RRuleType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarUtilities.WindowCloseType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VDateTime;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Concrete class as an example of a VEvent.
 * This class creates and edits appointments for display in Agenda.
 * 
 * 
 * @author David Bal
 *
 */
public class VEventImpl extends VEvent<Appointment>
{

//    // TODO - I THINK I'M GOING TO USE CATEGORIES INSTEAD OF BELOW X CUSTOM ELEMENT
//    /**
//     * X-APPOINTMENT-GROUP
//     * Contains the AppointmentGroup from Agenda.
//     * Non-Standard iCalendar Property (3.8.8.2 in RFC 5545 iCalendar)
//     * The css StyleClass is the value portion of this property outputed by toString.
//     * StyleClass must be unique for each AppointmentGroup.
//     */
//    public StringProperty appointmentGroupStyleClassProperty() { return appointmentGroupStyleClass; }
//    private StringProperty appointmentGroupStyleClass = new SimpleStringProperty(this, "X-APPOINTMENT-GROUP");
//    public void setAppointmentGroupStyleClass(String appointmentGroupStyleClass) { this.appointmentGroupStyleClass.set(appointmentGroupStyleClass); }
//    public String getAppointmentGroupStyleClass() { return appointmentGroupStyleClass.get(); }
//
    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroup; }
    private ObjectProperty<AppointmentGroup> appointmentGroup = new SimpleObjectProperty<AppointmentGroup>(this, "CATEGORIES");
    public void setAppointmentGroup(AppointmentGroup appointmentGroup) { this.appointmentGroup.set(appointmentGroup); }
    public AppointmentGroup getAppointmentGroup() { return appointmentGroup.get(); }

    /** appointmentGroups from Agenda.  It is used to synch categories to appointmentGroup, 
     * which is needed by the makeAppointment method 
     * @see #makeInstances() */
    public ObservableList<AppointmentGroup> getAppointmentGroups() { return appointmentGroups; }
    final private ObservableList<AppointmentGroup> appointmentGroups;

    /* below listeners ensures appointmentGroup description and categories match.  
     * added to categoriesProperty and appointmentGroups by the constructor.
     * appointmentGroups must be set
     */
    private final InvalidationListener categoriesListener = obs ->
    {
        System.out.println("finding appointment group:" + getAppointmentGroups().isEmpty() + " " + getCategories());
        if (! getAppointmentGroups().isEmpty() && getCategories() != null)
        {
            Optional<AppointmentGroup> myGroup = getAppointmentGroups()
                    .stream()
//                    .peek(a -> System.out.println(a.getDescription()))
                    .filter(g -> g.getDescription().equals(getCategories()))
                    .findFirst();
//            if (! myGroup.isPresent()) System.out.println("no matched group:");
            if (myGroup.isPresent()) setAppointmentGroup(myGroup.get());                
        }
    };
    private final ChangeListener<? super AppointmentGroup> appointmentGroupListener = 
            (obs, oldValue, newValue) -> setCategories(newValue.getDescription());
    
    /**
     *  VEventImpl doesn't know how to make an appointment.  An appointment factory makes new appointments.  The Class of the appointment
     * is an argument for the AppointmentFactory.  The appointmentClass is set in the constructor.  A RRule object is not valid without
     * the appointmentClass.
     */
    public Class<? extends Appointment> getAppointmentClass() { return appointmentClass; }
    private Class<? extends Appointment> appointmentClass = ICalendarAgenda.AppointmentImplLocal2.class; // default
    public void setAppointmentClass(Class<? extends Appointment> appointmentClass) { this.appointmentClass = appointmentClass; }
//    public VEventImpl withAppointmentClass(Class<? extends RepeatableAppointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }

    /**
     * The currently generated instances of the recurrence set.
     * 3.8.5.2 defines the recurrence set as the complete set of recurrence instances for a
     * calendar component.  As many RRule definitions are infinite sets, a complete representation
     * is not possible.  The set only contains the events inside the bounds of 
     */
    @Override
    public Set<Appointment> instances() { return instances; }
    final private Set<Appointment> instances = new HashSet<>();
//    public VEventImpl withAppointments(Collection<RepeatableAppointment> s) { appointments().addAll(s); return this; }
    public boolean isNewRRule() { return instances().size() == 0; } // new RRule has no appointments
    
    /*
     * CONSTRUCTORS
     */
    /** Copy constructor */
    public VEventImpl(VEventImpl vevent) {
        super(vevent);
        this.appointmentGroups = vevent.getAppointmentGroups();
        categoriesProperty().addListener(categoriesListener);
        appointmentGroup.addListener(appointmentGroupListener);
        copy(vevent, this);
    }
    
    public VEventImpl(ObservableList<AppointmentGroup> appointmentGroups)
    {
        super();
        this.appointmentGroups = appointmentGroups;
        categoriesProperty().addListener(categoriesListener);
        appointmentGroup.addListener(appointmentGroupListener);
    }
    
    /**
     * makes new VEventImpl by copying properties from appointment
     * 
     * @param appointment - from Agenda
     */
    public VEventImpl(Appointment appointment, ObservableList<AppointmentGroup> appointmentGroups)
    {
        this(appointmentGroups);
        setCategories(appointment.getAppointmentGroup().getDescription());
        setDateTimeEnd(new VDateTime(appointment.getEndLocalDateTime()));
        setDateTimeStamp(LocalDateTime.now());
        setDateTimeStart(new VDateTime(appointment.getStartLocalDateTime()));
        setDescription(appointment.getDescription());
        setLocation(appointment.getLocation());
        setSummary(appointment.getSummary());
        setUniqueIdentifier(getUidGeneratorCallback().call(null));
        instances().add(appointment);
        if (! validityCheck().equals("")) throw new IllegalArgumentException(validityCheck());
    }

    /** Deep copy all fields from source to destination 
     * @param <E>*/
    private static void copy(VEventImpl source, VEventImpl destination)
    {
        if (source.getAppointmentGroup() != null) destination.setAppointmentGroup(source.getAppointmentGroup());
        if (source.getAppointmentClass() != null) destination.setAppointmentClass(source.getAppointmentClass());
        if (source.getDateTimeRangeStart() != null) destination.setDateTimeRangeStart(source.getDateTimeRangeStart());
        if (source.getDateTimeRangeEnd() != null) destination.setDateTimeRangeEnd(source.getDateTimeRangeEnd());
        source.instances().stream().forEach(a -> destination.instances().add(a));
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent destination)
    {
        super.copyTo(destination);
        copy(this, (VEventImpl) destination);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VEventImpl testObj = (VEventImpl) obj;
        System.out.println("getAppointmentClass:" + getAppointmentClass() + " " + testObj.getAppointmentClass());
        boolean appointmentClassEquals = (getAppointmentClass() == null) ?
                (testObj.getAppointmentClass() == null) : getAppointmentClass().equals(testObj.getAppointmentClass());
        boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
        boolean dateTimeRangeStartEquals = (getDateTimeRangeStart() == null) ?
                (testObj.getDateTimeRangeStart() == null) : getDateTimeRangeStart().equals(testObj.getDateTimeRangeStart());
        boolean dateTimeRangeEndEquals = (getDateTimeRangeEnd() == null) ?
                (testObj.getDateTimeRangeEnd() == null) : getDateTimeRangeEnd().equals(testObj.getDateTimeRangeEnd());
//        boolean appointmentsEquals = (appointments() == null) ?
//                (testObj.appointments() == null) : appointments().equals(testObj.appointments());
        System.out.println("VEventImpl: " + appointmentClassEquals + " " + dateTimeRangeStartEquals + " " + appointmentGroupEquals + " " + dateTimeRangeEndEquals
                + " ");// + appointmentsEquals);
        return super.equals(obj) && appointmentClassEquals && dateTimeRangeStartEquals && appointmentGroupEquals
                && dateTimeRangeEndEquals;
    }
    
    @Override
    public Stream<LocalDateTime> stream(LocalDateTime startDateTime)
    {
        Stream<LocalDateTime> initialStream = super.stream(startDateTime);
        // filter away too early (with Java 9 takeWhile these statements can be combined into one chained statement for greater elegance)
        Stream<LocalDateTime> filteredStream = initialStream
                .filter(a -> (getDateTimeRangeStart() == null) ? true : ! a.isBefore(getDateTimeRangeStart()));
        // stop when too late
        return takeWhile(filteredStream, a -> (getDateTimeRangeEnd() == null) ? true : ! a.isAfter(getDateTimeRangeEnd()));
    }
    
    /** Make iCalendar compliant string of VEvent calendar component */
    @Override
    public String toString()
    {
        String errors = validityCheck();
        if (! errors.equals("")) throw new IllegalArgumentException(errors);
        Map<Property, String> properties = makePropertiesMap();
        String propertiesString = properties.entrySet()
                .stream() 
                .map(p -> p.getKey().getName() + ":" + p.getValue() + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }
    
    @Override
    protected Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();
        properties.putAll(super.makePropertiesMap());
//        if (getAppointmentGroup() != null) properties.put(appointmentGroupStyleClassProperty(), getAppointmentGroupStyleClass());
        return properties;
    }
    
    @Override
    public String validityCheck()
    {
        String errors = super.validityCheck();
//        if (getAppointmentClass() == null) errors += System.lineSeparator() + "Invalid VEventImpl.  appointmentClass must not be null.";
        return errors;
    }
    
    /** Make new VEventImpl and populate properties by parsing a list of strings 
     * @param <E>*/
    public static VEventImpl parseVEvent(List<String> strings, ObservableList<AppointmentGroup> appointmentGroups)
    {
        VEventImpl vEvent = new VEventImpl(appointmentGroups);
        VEvent.parseVEvent(vEvent, strings); // parse VEvent properties into vEvent
        System.out.println("categories:" + vEvent.getCategories());
//        Iterator<String> stringsIterator = strings.iterator();
//        while (stringsIterator.hasNext())
//        {
//            String line = stringsIterator.next();
//            String property = line.substring(0, line.indexOf(":"));
//            String value = line.substring(line.indexOf(":") + 1).trim();
//            if (property.equals(vEvent.appointmentGroupProperty().getName()))
//            { // X-APPOINTMENT-GROUP
//                vEvent.setAppointmentGroupStyleClass(property[1]);
//                stringsIterator.remove();
//            }
//        }
        return vEvent;
    }
    
    /** Make new VEventImpl and populate properties by parsing a string with properties separated
     * a by lineSeparator (new line) */
    public static VEventImpl parseVEvent(String strings, ObservableList<AppointmentGroup> appointmentGroups)
    {
        List<String> stringsList = Arrays
                .stream(strings.split(System.lineSeparator()))
                .collect(Collectors.toList());
        return parseVEvent(stringsList, appointmentGroups);
    }

    /**
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @return created appointments
     */
    @Override
    public Collection<Appointment> makeInstances()
    {
//        System.out.println("range: " + getDateTimeRangeStart() + " " + getDateTimeRangeEnd());
        if ((getDateTimeRangeStart() == null) || (getDateTimeRangeStart() == null)) throw new IllegalArgumentException("can't make instances without setting date/time range first");
        List<Appointment> madeAppointments = new ArrayList<>();
        stream(getDateTimeStart().getLocalDateTime())
                .forEach(d -> {
                    Appointment appt = AppointmentFactory.newAppointment(getAppointmentClass());
                    appt.setStartLocalDateTime(d);
                    appt.setEndLocalDateTime(d.plusSeconds(getDurationInSeconds()));
//                    appt.setRepeatMade(true);
                    appt.setDescription(getDescription());
                    appt.setSummary(getSummary());
                    appt.setAppointmentGroup(getAppointmentGroup());
                    madeAppointments.add(appt);   // add appointments to main collection
                    instances().add(appt); // add appointments to this repeat's collection
                });

        return madeAppointments;
    }
 
    
    
    /**
     * Handles editing VEvent objects.
     * 
     * @param dateTimeStartInstanceOld - start date/time of selected instance before edit
     * @param dateTimeStartInstanceNew - start date/time of selected instance after edit
     * @param dateTimeEndInstanceNew - end date/time of selected instance after edit
     * @param vEventOld - copy from vEventOld into this if edit is canceled
     * @param appointments - list of all appointments in agenda (sorted by start date/time)
     * @param vEvents - collection of all VEvents (add new VEvents if change to ONE or FUTURE)
     * @param changeDialogCallback - called to make dialog to prompt user for scope of edit (usually ONE, ALL, OR THIS_AND_FUTURE).  Parameter can be a simple predicate to force selection for testing (example: a -> ChangeDialogOptions.ONE).
     * @param writeVEventsCallback - called to do VEvent I/O if necessary.
     * @return
     */
    @Override
    public WindowCloseType edit(
              LocalDateTime dateTimeStartInstanceOld
            , LocalDateTime dateTimeStartInstanceNew
            , LocalDateTime dateTimeEndInstanceNew
            , VComponent<Appointment> vEventOld
            , Collection< Appointment> appointments
            , Collection<VComponent<Appointment>> vEvents
            , Callback<ChangeDialogOptions[], ChangeDialogOptions> changeDialogCallback
            , Callback<Collection<VComponent<Appointment>>, Void> writeVEventsCallback)
    {
        // Check if start time and duration has changed because those values are not changed in the edit controller.
        final long durationInSeconds = ChronoUnit.SECONDS.between(dateTimeStartInstanceNew, dateTimeEndInstanceNew);
        final VEventImpl vEventOld2 = (VEventImpl) vEventOld;
//        System.out.println(dateTimeStartInstanceNew + " " + vEventOld2.getDateTimeStart());
        boolean dateTimeNewSame = dateTimeStartInstanceNew.toLocalTime().equals(vEventOld2.getDateTimeStart().getLocalDateTime().toLocalTime());
        boolean durationSame = (durationInSeconds == vEventOld2.getDurationInSeconds());
        if (dateTimeNewSame && durationSame && this.equals(vEventOld)) return WindowCloseType.CLOSE_WITHOUT_CHANGE;

        final RRuleType rruleType = getRRuleType(vEventOld.getRRule());
        System.out.println("rruleType " + rruleType);
        boolean editedFlag = true;
        switch (rruleType)
        {
        case HAD_REPEAT_BECOMING_INDIVIDUAL:
            setRRule(null);
            setRDate(null);
            setExDate(null);
        case WITH_NEW_REPEAT:
        case INDIVIDUAL:            
            break;
        case WITH_EXISTING_REPEAT:
            // Check if changes between vEvent and vEventOld exist apart from RRule
//            VEvent tempVEvent = VEventFactory.newVEvent(vEventOld);
            VEvent<Appointment> tempVEvent = new VEventImpl((VEventImpl) vEventOld);
            tempVEvent.setRRule(getRRule());
            boolean onlyRRuleChanged = this.equals(tempVEvent);

            ChangeDialogOptions[] choices = null;
            if (onlyRRuleChanged) choices = new ChangeDialogOptions[] {ChangeDialogOptions.ALL, ChangeDialogOptions.THIS_AND_FUTURE};
            ChangeDialogOptions changeResponse = changeDialogCallback.call(choices);
            switch (changeResponse)
            {
                case ALL:
                    // Copy date/time data to this VEvent
                    long secondsAdjustment = ChronoUnit.SECONDS.between(dateTimeStartInstanceOld, dateTimeStartInstanceNew);
                    LocalDateTime newDateTimeStart = getDateTimeStart().getLocalDateTime().plusSeconds(secondsAdjustment);
                    getDateTimeStart().setLocalDateTime(newDateTimeStart);
                    setDurationInSeconds(durationInSeconds);
                    break;
                case CANCEL:
//                    System.out.println("cancel:");
                    editedFlag = false;
//                    vEventOld.copyTo(this);
                    break;
                case THIS_AND_FUTURE:
                { // this is edited VEvent, vEventOld is former settings, with UNTIL set at start for this.
                    
                    // Remove appointments
                    appointments.removeIf(a -> vEventOld.instances().stream().anyMatch(a2 -> a2 == a));
                    
                    // modify old VEvent
                    vEvents.add(vEventOld);
                    if (vEventOld2.getRRule().getCount() != null) vEventOld2.getRRule().setCount(0);
                    vEventOld2.getRRule().setUntil(dateTimeStartInstanceOld.minusSeconds(1));
                    vEventOld.instances().clear();
                    appointments.addAll(vEventOld.makeInstances()); // add vEventOld part of new appointments
                    
                    // Split EXDates dates between this and newVEvent
                    if (getExDate() != null)
                    {
                        getExDate().getDates().clear();
                        final Iterator<VDateTime> exceptionIterator = vEventOld2.getExDate().getDates().iterator();
                        while (exceptionIterator.hasNext())
                        {
                            VDateTime d = exceptionIterator.next();
                            if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                            {
                                exceptionIterator.remove();
                            } else {
                                vEventOld2.getExDate().getDates().add(d);
                            }
                        }
                    }

                    // Split instance dates between this and newVEvent
                    if (getRRule().getInstances() != null)
                    {
                        getRRule().getInstances().clear();
                        final Iterator<LocalDateTime> instanceIterator = vEventOld2.getRRule().getInstances().iterator();
                        while (instanceIterator.hasNext())
                        {
                            LocalDateTime d = instanceIterator.next();
                            if (d.isBefore(dateTimeStartInstanceNew))
                            {
                                instanceIterator.remove();
                            } else {
                                vEventOld2.getRRule().getInstances().add(d);
                            }
                        }
                    }
                    
                    // Split recurrence date/times between this and newVEvent
                    if (getRDate() != null)
                    {
                        getRDate().getDates().clear();
                        final Iterator<VDateTime> recurrenceIterator = vEventOld2.getRDate().getDates().iterator();
                        while (recurrenceIterator.hasNext())
                        {
                            VDateTime d = recurrenceIterator.next();
                            if (d.getLocalDateTime().isBefore(dateTimeStartInstanceNew))
                            {
                                recurrenceIterator.remove();
                            } else {
                                vEventOld2.getRDate().getDates().add(d);
                            }
                        }
                    }

                    // Modify this (edited) VEvent
                    getDateTimeStart().setLocalDateTime(dateTimeStartInstanceNew);
                    setDurationInSeconds(durationInSeconds);
                    
                    // Modify COUNT for this (the edited) VEvent
                    if (getRRule().getCount() > 0)
                    {
                        final int newCount = (int) instances()
                                .stream()
                                .map(a -> a.getStartLocalDateTime())
                                .filter(d -> ! d.isBefore(dateTimeStartInstanceNew))
                                .count();
                        getRRule().setCount(newCount);
                    }

                    break;
                }
                case ONE:
                { // Make new individual VEvent, save settings to it.  Add date to original as recurrence.
                    VEventImpl newVEvent = new VEventImpl(this);
//                    System.out.println("change one: " + newVEvent.getDateTimeStart().hashCode() + " " + this.getDateTimeStart().hashCode());
//                    System.exit(0);
                    vEvents.add(newVEvent);
                    newVEvent.getDateTimeStart().setLocalDateTime(dateTimeStartInstanceNew);
                    // TODO - need new UID for newVEvent.  Do it here or in constructor?
                    newVEvent.setRRule(null);
                    appointments.addAll(newVEvent.makeInstances());
    
                    // modify this VEvent for recurrence
                    vEventOld2.copyTo(this);                
                    getRRule().getInstances().add(dateTimeStartInstanceOld);
                    break;
                }
            }
            break;
        default:
            throw new RuntimeException("Unsupported RRuleType: " + rruleType);
        }
        // TODO - THIS MAY MEAN THIS HAS TO GO BACK TO IMPL - CAN USE CALLBACK
        // DOESN'T KNOW ABOUT APPOINTMENTS HERE
        
        if (editedFlag) // make these changes as long as CANCEL is not selected
        { // remove appointments from mail collection made by VEvent
            System.out.println("Edited flag:");
            appointments.removeIf(a -> instances().stream().anyMatch(a2 -> a2 == a));

            //            Iterator<Appointment> i = appointments.iterator();
//            while (i.hasNext())
//            {
//                Appointment a = i.next();
//                if (appointments().contains(a)) i.remove();
//            }
            instances().clear(); // clear VEvent's collection of appointments
//            System.out.println("size1: " + appointments.size());
            appointments.addAll(makeInstances()); // make new appointments and add to main collection (added to VEvent's collection in makeAppointments)
//            System.out.println("size2: " + appointments.size());
            return WindowCloseType.CLOSE_WITH_CHANGE;
        } else
        {
            return WindowCloseType.CLOSE_WITHOUT_CHANGE;
        }
    }
    
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
     * Options available when changing a repeatable appointment
     * ONE: Change only selected appointment
     * ALL: Change all appointments with repeat rule
     * FUTURE: Change future appointments with repeat rule
     */
    public enum RepeatChange {
        ONE, ALL, FUTURE, CANCEL;

        @Override
        public String toString() {
            return Settings.REPEAT_CHANGE_CHOICES.get(this);
        }
    }
    
    private RRuleType getRRuleType(RRule rruleOld)
    {
System.out.println("rules:" + getRRule() + " " + rruleOld);
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
    
    // takeWhile - From http://stackoverflow.com/questions/20746429/limit-a-stream-by-a-predicate
    // will be obsolete with Java 9
    public static <T> Spliterator<T> takeWhile(
            Spliterator<T> splitr, Predicate<? super T> predicate) {
          return new Spliterators.AbstractSpliterator<T>(splitr.estimateSize(), 0) {
            boolean stillGoing = true;
            @Override public boolean tryAdvance(Consumer<? super T> consumer) {
              if (stillGoing) {
                boolean hadNext = splitr.tryAdvance(elem -> {
                  if (predicate.test(elem)) {
                    consumer.accept(elem);
                  } else {
                    stillGoing = false;
                  }
                });
                return hadNext && stillGoing;
              }
              return false;
            }
          };
        }

        static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
           return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
        }
}
