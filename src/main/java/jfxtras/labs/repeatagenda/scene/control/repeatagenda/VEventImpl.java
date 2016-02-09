package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgenda.AppointmentFactory;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.ICalendarAgendaUtilities.RRuleType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.rrule.RRule;
import jfxtras.scene.control.agenda.Agenda;
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
    public VEventImpl withAppointmentGroup(AppointmentGroup appointmentGroup) { setAppointmentGroup(appointmentGroup); return this; }

    /** appointmentGroups from Agenda.  It is used to synch categories to appointmentGroup, 
     * which is needed by the makeAppointment method 
     * @see #makeInstances() */
    public List<AppointmentGroup> getAppointmentGroups() { return appointmentGroups; }
    final private List<AppointmentGroup> appointmentGroups;

    /* below listeners ensures appointmentGroup description and categories match.  
     * added to categoriesProperty and appointmentGroups by the constructor.
     * appointmentGroups must be set
     */
    private final InvalidationListener categoriesListener = obs ->
    {
//        System.out.println("finding appointment group:" + getAppointmentGroups().isEmpty() + " " + getCategories());
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
    private Class<? extends Appointment> appointmentClass = Agenda.AppointmentImplLocal.class; // default Appointment class
    public void setAppointmentClass(Class<? extends Appointment> appointmentClass) { this.appointmentClass = appointmentClass; }
    public VEventImpl withAppointmentClass(Class<? extends Appointment> appointmentClass) { setAppointmentClass(appointmentClass); return this; }

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
    
    // Fluent methods
    public VEventImpl withDateTimeRecurrence(Temporal t) { setDateTimeRecurrence(t); return this; }
    public VEventImpl withDateTimeStamp(LocalDateTime t) { setDateTimeStamp(t); return this; }
    public VEventImpl withDateTimeStart(Temporal t) { setDateTimeStart(t); return this; }
    public VEventImpl withDateTimeEnd(Temporal t) { setDateTimeEnd(t); return this; }
    public VEventImpl withDescription(String s) { setDescription(s); return this; }
    public VEventImpl withDurationInNanos(Long l) { setDurationInNanos(l); return this; }
    public VEventImpl withRRule(RRule r) { setRRule(r); return this; }
    public VEventImpl withSequence(int i) { setSequence(i); return this; }
    public VEventImpl withSummary(String s) { setSummary(s); return this; }
    public VEventImpl withUniqueIdentifier(String s) { setUniqueIdentifier(s); return this; }
    
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
    
    public VEventImpl(List<AppointmentGroup> appointmentGroups)
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
        if (appointment.isWholeDay())
        {
            setDateTimeEnd(appointment.getEndLocalDateTime().toLocalDate());
            setDateTimeStart(appointment.getStartLocalDateTime().toLocalDate());
        } else
        {
            setDateTimeEnd(appointment.getEndLocalDateTime());
            setDateTimeStart(appointment.getStartLocalDateTime());
        }
        setDateTimeStamp(LocalDateTime.now());
        setDescription(appointment.getDescription());
        setLocation(appointment.getLocation());
        setSummary(appointment.getSummary());
        setUniqueIdentifier(getUidGeneratorCallback().call(null));
        instances().add(appointment);
        if (! makeErrorString().equals("")) throw new IllegalArgumentException(makeErrorString());
    }

    /** Deep copy all fields from source to destination 
     * @param <E>*/
    private static void copy(VEventImpl source, VEventImpl destination)
    {
        destination.setAppointmentGroup(source.getAppointmentGroup());
        destination.setAppointmentClass(source.getAppointmentClass());
        if (source.getStartRange() != null) destination.setStartRange(source.getStartRange());
        if (source.getEndRange() != null) destination.setEndRange(source.getEndRange());
        destination.setUidGeneratorCallback(source.getUidGeneratorCallback());
        source.instances().stream().forEach(a -> destination.instances().add(a));
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponent<Appointment> destination)
    {
        super.copyTo(destination);
        copy(this, (VEventImpl) destination);
    }
    
    // TODO - EITHER REMOVE OR OVERRIDE HASHCODE
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VEventImpl testObj = (VEventImpl) obj;
//        System.out.println("getAppointmentClass:" + getAppointmentClass().getSimpleName() + " " + testObj.getAppointmentClass().getSimpleName());
        boolean appointmentClassEquals = (getAppointmentClass() == null) ?
                (testObj.getAppointmentClass() == null) : getAppointmentClass().equals(testObj.getAppointmentClass());
        boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
//        boolean dateTimeRangeStartEquals = (getStartRange() == null) ?
//                (testObj.getStartRange() == null) : getStartRange().equals(testObj.getStartRange());
//        boolean dateTimeRangeEndEquals = (getEndRange() == null) ?
//                (testObj.getEndRange() == null) : getEndRange().equals(testObj.getEndRange());
//        boolean appointmentsEquals = (appointments() == null) ?
//                (testObj.appointments() == null) : appointments().equals(testObj.appointments());
        System.out.println("VEventImpl: " + appointmentClassEquals + " " +  appointmentGroupEquals + " "
                + " ");// + appointmentsEquals);
        return super.equals(obj) && appointmentClassEquals && appointmentGroupEquals;
    }
    
//    @Override
//    public Stream<Temporal> stream(Temporal startTemporal)
//    {
//        return super.stream(startTemporal);
////        Stream<Temporal> initialStream = super.stream(startTemporal);
//        // filter away too early (with Java 9 takeWhile these statements can be combined into one chained statement for greater elegance)
////        Stream<Temporal> filteredStream = initialStream
////                .filter(a -> (getDateTimeRangeStart() == null) ? true : ! VComponent.isBefore(a, getDateTimeRangeStart()));
//        // stop when too late
////        return takeWhile(filteredStream, a -> (getDateTimeRangeEnd() == null) ? true : ! VComponent.isAfter(a, getDateTimeRangeEnd()));
//    }
    
    /** Make iCalendar compliant string of VEvent calendar component */
    @Override
    public String toString()
    {
        String errors = makeErrorString();
//        if (! errors.equals("")) throw new RuntimeException(errors);
        @SuppressWarnings("rawtypes")
        Map<Property, String> properties = makePropertiesMap();
        String propertiesString = properties.entrySet()
                .stream() 
                .map(p -> p.getKey().getName() + ":" + p.getValue() + System.lineSeparator())
                .sorted()
                .collect(Collectors.joining());
        return "BEGIN:VEVENT" + System.lineSeparator() + propertiesString + "END:VEVENT";
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected Map<Property, String> makePropertiesMap()
    {
        Map<Property, String> properties = new HashMap<Property, String>();
        properties.putAll(super.makePropertiesMap());
//        if (getAppointmentGroup() != null) properties.put(appointmentGroupStyleClassProperty(), getAppointmentGroupStyleClass());
        return properties;
    }
    
    @Override
    public String makeErrorString()
    {
        String errors = super.makeErrorString();
//        if (getAppointmentClass() == null) errors += System.lineSeparator() + "Invalid VEventImpl.  appointmentClass must not be null.";
        return errors;
    }
    
    @Override
    public boolean isValid()
    {
        return super.makeErrorString().equals("");
    }
    
    /** Make new VEventImpl and populate properties by parsing a list of strings 
     * @param <E>*/
    public static VEventImpl parseVEvent(List<String> strings, List<AppointmentGroup> appointmentGroups)
    {
        VEventImpl vEvent = new VEventImpl(appointmentGroups);
        VEvent.parseVEvent(vEvent, strings); // parse VEvent properties into vEvent
        return vEvent;
    }
    
    /** Make new VEventImpl and populate properties by parsing a string with properties separated
     * a by lineSeparator (new line) */
    public static VEventImpl parseVEvent(String strings, List<AppointmentGroup> appointmentGroups)
    {
        List<String> stringsList = Arrays
                .stream(strings.split(System.lineSeparator()))
                .collect(Collectors.toList());
        return parseVEvent(stringsList, appointmentGroups);
    }

    /**
     * Start of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     * This is not a part of an iCalendar VEvent
     */
    @Override
    public Temporal getStartRange() { return startRange; }
    private Temporal startRange;
    @Override
    public void setStartRange(Temporal start)
    {
        this.startRange = VComponent.ofTemporal(start, getDateTimeStart().getClass()); // store start as same type as dateTimeStart
    }
    
    /**
     * End of range for which recurrence instances are generated.  Should match the dates displayed on the calendar.
     */
    @Override
    public Temporal getEndRange() { return endRange; }
    private Temporal endRange;
    @Override
    public void setEndRange(Temporal end)
    {
        this.endRange = VComponent.ofTemporal(end, getDateTimeStart().getClass()); // store start as same type as dateTimeStart
    }
    /**
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @return created appointments
     */
    @Override
    public List<Appointment> makeInstances()
    {
        if ((getStartRange() == null) || (getEndRange() == null)) throw new RuntimeException("Can't make instances without setting date/time range first");
        boolean wholeDay = getStartRange() instanceof LocalDate;
        List<Appointment> madeAppointments = new ArrayList<>();
        Stream<Temporal> removedTooEarly = stream(getStartRange()).filter(d -> ! VComponent.isBefore(d, getStartRange()));
        Stream<Temporal> removedTooLate = takeWhile(removedTooEarly, a -> ! VComponent.isAfter(a, getEndRange()));
        removedTooLate.forEach(t ->
        {
            LocalDateTime startLocalDateTime = VComponent.localDateTimeFromTemporal(t);
            Appointment appt = AppointmentFactory.newAppointment(getAppointmentClass());
            appt.setStartLocalDateTime(startLocalDateTime);
            final long nanos;
//            if (getDurationInNanos() == null)
//            {
                if (t instanceof LocalDate)
                {
                    if (getDateTimeEnd() != null) nanos = ChronoUnit.DAYS.between(getDateTimeStart(), getDateTimeEnd()) * VComponent.NANOS_IN_DAY;
                    else if (getDurationInNanos() != null) nanos = getDurationInNanos();
                    else throw new RuntimeException("Invalid VEvent: Neither DURATION or DTEND set");
                } else if (t instanceof LocalDateTime)
                {
                    if (getDateTimeEnd() != null) nanos = ChronoUnit.NANOS.between(getDateTimeStart(), getDateTimeEnd());
                    else if (getDurationInNanos() != null) nanos = getDurationInNanos();
                    else throw new RuntimeException("Invalid VEvent: Neither DURATION or DTEND set");
                } else throw new DateTimeException("Illegal Temporal type.  Only LocalDate and LocalDateTime are supported)");
//            } else nanos = getDurationInNanos();
            appt.setEndLocalDateTime(startLocalDateTime.plusNanos(nanos));
            appt.setDescription(getDescription());
            appt.setSummary(getSummary());
            appt.setAppointmentGroup(getAppointmentGroup());
            appt.setWholeDay(wholeDay);
            madeAppointments.add(appt);   // add appointments to return argument
            instances().add(appt); // add appointments to this repeat's collection
        });
        return madeAppointments;
    }
    
    /**
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @param startRange
     * @param endRange
     * @return created appointments
     */
    @Override
    public List<Appointment> makeInstances(Temporal startRange, Temporal endRange)
    {
        if (VComponent.isAfter(startRange, endRange)) throw new DateTimeException("startTemporal must be after endTemporal");
        setEndRange(endRange);
        setStartRange(startRange);
        return makeInstances();
    }
     
    private RRuleType getRRuleType(RRule rruleOld)
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
            } else
            {
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
