package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
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
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.DateTimeType;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.RDate;
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
    // TODO - THESE CALLBACKS MAY BE OBSOLETE - IF AppointmentImplTemporal IS IN AGENDA
    // USE REFLECTION FROM APPOINTMENT CLASS INSTEAD?
    /*
     *  STATIC CALLBACKS FOR MAKING NEW APPOINTMENTS
     *  One for each of the four date and date-time options in iCalendar:
     *  Date
     *  Date with local time
     *  Date with UTC time
     *  Date with local time and time zone instance
     *  see iCalendar RFC 5545, page 32-33
     *  
     *  Only date-time properties are set in the callbacks.  The other properties
     *  are set in makeInstances method.
     */
    private final static Callback<StartEndPair, Appointment> TEMPORAL_INSTANCE = (p) ->
    {
        return new Agenda.AppointmentImplTemporal()
                .withStartTemporal(p.getDateTimeStart())
                .withEndTemporal(p.getDateTimeEnd());
    };
    
    /** For DATE type (whole-day Appointments) */
    private final static Callback<StartEndPair, Appointment> NEW_DATE_INSTANCE = (p) ->
    {
        LocalDateTime s = LocalDate.from(p.getDateTimeStart()).atStartOfDay();
        LocalDateTime e = LocalDate.from(p.getDateTimeEnd()).atStartOfDay();
        return new Agenda.AppointmentImplTemporal()
                .withStartTemporal(s)
                .withEndTemporal(e);
//        return new Agenda.AppointmentImplLocal()
//                .withStartLocalDateTime(s)
//                .withEndLocalDateTime(e);
    };

    /** For DATE_WITH_LOCAL_TIME */
    private final static Callback<StartEndPair, Appointment> NEW_DATE_WITH_LOCAL_TIME_INSTANCE = (p) ->
    {
        return new Agenda.AppointmentImplTemporal()
                .withStartTemporal(p.getDateTimeStart())
                .withEndTemporal(p.getDateTimeEnd());
//        return new Agenda.AppointmentImplLocal()
//                .withStartLocalDateTime(LocalDateTime.from(p.getDateTimeStart()))
//                .withEndLocalDateTime(LocalDateTime.from(p.getDateTimeEnd()));
    };

    /** For DATE_WITH_UTC_TIME */
    private final static Callback<StartEndPair, Appointment> NEW_DATE_WITH_UTC_TIME_INSTANCE = (p) ->
    {
        final ZonedDateTime s;
        if (p.getDateTimeStart() instanceof ZonedDateTime)
        {
            s = ((ZonedDateTime) p.getDateTimeStart()).withZoneSameInstant(ZoneId.of("Z"));
        } else
        {
            s = LocalDateTime.from(p.getDateTimeStart()).atZone(ZoneId.of("Z"));
        }

        final ZonedDateTime e;
        if (p.getDateTimeEnd() instanceof ZonedDateTime)
        {
            e = ((ZonedDateTime) p.getDateTimeEnd()).withZoneSameInstant(ZoneId.of("Z"));
        } else
        {
            e = LocalDateTime.from(p.getDateTimeEnd()).atZone(ZoneId.of("Z"));
        }
        return new Agenda.AppointmentImplTemporal()
                .withStartTemporal(s)
                .withEndTemporal(e);
//        return new Agenda.AppointmentImplZoned()
//                .withStartZonedDateTime(s)
//                .withEndZonedDateTime(e);
    };
    
    /** For DATE_WITH_LOCAL_TIME_AND_TIME_ZONE */
    private final static Callback<StartEndPair, Appointment> NEW_DATE_WITH_LOCAL_TIME_AND_TIME_ZONE_INSTANCE = (p) ->
    {
        return new Agenda.AppointmentImplTemporal()
                .withStartTemporal(p.getDateTimeStart())
                .withEndTemporal(p.getDateTimeEnd());
//        return new Agenda.AppointmentImplZoned()
//                .withStartZonedDateTime((ZonedDateTime) p.getDateTimeStart())
//                .withEndZonedDateTime((ZonedDateTime) p.getDateTimeEnd());
    };

    // Map to match up DateTimeType to Callback;
    private static final Map<DateTimeType, Callback<StartEndPair, Appointment>> DATE_TIME_MAKE_INSTANCE_MAP = defaultDateTimeInstanceMap();
    private static Map<DateTimeType, Callback<StartEndPair, Appointment>> defaultDateTimeInstanceMap()
    {
        Map<DateTimeType, Callback<StartEndPair, Appointment>> map = new HashMap<>();
        map.put(DateTimeType.DATE, NEW_DATE_INSTANCE);
        map.put(DateTimeType.DATE_WITH_LOCAL_TIME, NEW_DATE_WITH_LOCAL_TIME_INSTANCE);
        map.put(DateTimeType.DATE_WITH_UTC_TIME, NEW_DATE_WITH_UTC_TIME_INSTANCE);
        map.put(DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE, NEW_DATE_WITH_LOCAL_TIME_AND_TIME_ZONE_INSTANCE);
        return map;
    }

    
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
        if (! getAppointmentGroups().isEmpty() && getCategories() != null)
        {
            Optional<AppointmentGroup> myGroup = getAppointmentGroups()
                    .stream()
//                    .peek(a -> System.out.println(a.getDescription()))
                    .filter(g -> g.getDescription().equals(getCategories()))
                    .findFirst();
            if (myGroup.isPresent()) setAppointmentGroup(myGroup.get());                
        }
    };
    private final ChangeListener<? super AppointmentGroup> appointmentGroupListener = 
            (obs, oldValue, newValue) -> setCategories(newValue.getDescription());
    
    /**
     *  VEventImpl doesn't know how to make an appointment.  New appointments are instantiated via reflection, so they
     *  must have a no-arg constructor.
     */
    public Class<? extends Appointment> getAppointmentClass() { return appointmentClass; }
    private Class<? extends Appointment> appointmentClass = Agenda.AppointmentImplTemporal.class; // default Appointment class
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
    public boolean isNewRRule() { return instances().size() == 0; } // new RRule has no appointments
    
    // Fluent methods for chaining
    public VEventImpl withDateTimeCreated(ZonedDateTime t) { setDateTimeCreated(t); return this; }
    public VEventImpl withDateTimeLastModified(ZonedDateTime t) { setDateTimeLastModified(t); return this; }
    public VEventImpl withDateTimeRecurrence(Temporal t) { setDateTimeRecurrence(t); return this; }
    public VEventImpl withDateTimeStamp(ZonedDateTime t) { setDateTimeStamp(t); return this; }
    public VEventImpl withDateTimeStart(Temporal t) { setDateTimeStart(t); return this; }
    public VEventImpl withDateTimeEnd(Temporal t) { setDateTimeEnd(t); return this; }
    public VEventImpl withDescription(String s) { setDescription(s); return this; }
    public VEventImpl withDuration(Duration d) { setDuration(d); return this; }
    public VEventImpl withExDate(ExDate e) { setExDate(e); return this; }
    public VEventImpl withRDate(RDate r) { setRDate(r); return this; }
    public VEventImpl withRelatedTo(String s) { setRelatedTo(s); return this; }
    public VEventImpl withRRule(RRule r) { setRRule(r); return this; }
    public VEventImpl withSequence(int i) { setSequence(i); return this; }
    public VEventImpl withSummary(String s) { setSummary(s); return this; }
    public VEventImpl withUniqueIdentifier(String s) { setUniqueIdentifier(s); return this; }
    
    /*
     * CONSTRUCTORS
     */
    /** Copy constructor */
    public VEventImpl(VEventImpl vevent)
    {
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
     * makes new VEventImpl by copying properties from appointment.
     * stores start and end date/times as ZonedDateTime in default time zone
     * 
     * @param appointment - from Agenda
     */
    public VEventImpl(Appointment appointment, ObservableList<AppointmentGroup> appointmentGroups)
    {
        this(appointmentGroups);
        setCategories(appointment.getAppointmentGroup().getDescription());
//        if (appointment.isWholeDay())
//        {
//            setDateTimeEnd(appointment.getEndTemporal());
//            setDateTimeStart(appointment.getEndTemporal());
//        } else
//        {
//            Temporal start;
//            Temporal end;
//            start = appointment.getStartTemporal();
//            end = appointment.getEndTemporal();
////            try
////            {
////                start = appointment.getStartZonedDateTime();
////                end = appointment.getEndZonedDateTime();
////            } catch (Exception e)
////            {
////                start = appointment.getStartLocalDateTime();
////                end = appointment.getEndLocalDateTime();
////            }
//            setDateTimeEnd(end);
//            setDateTimeStart(start);
//        }
        // TODO - WHAT TO DO IF APPOINTMENT IS LOCALDATETIME???
        // MAKE DEFAULT ZONED DATE TIME?
        setDateTimeStart(appointment.getStartTemporal());
        setDateTimeEnd(appointment.getEndTemporal());
        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        setDescription(appointment.getDescription());
        setLocation(appointment.getLocation());
        setSummary(appointment.getSummary());
        setUniqueIdentifier(getUidGeneratorCallback().call(null));
        instances().add(appointment);
        if (! errorString().equals("")) throw new IllegalArgumentException(errorString());
    }

    /** Deep copy all fields from source to destination 
     * */
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
    
//    @Override
//    public String errorString()
//    {
//        String errors = super.errorString();
//        return errors;
//    }
    
    @Override
    public boolean isValid()
    {
        return super.errorString().equals("");
    }
        
    /** Make new VEventImpl and populate properties by parsing a string of line-separated
     * content lines
     *  */
    public static VEventImpl parse(String string, List<AppointmentGroup> appointmentGroups)
    {
        VEventImpl vEvent = new VEventImpl(appointmentGroups);
        return (VEventImpl) VEvent.parseVEvent(vEvent, string);
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
//        Callback<StartEndPair, Appointment> newInstanceCallback = DATE_TIME_MAKE_INSTANCE_MAP.get(getDateTimeType());
//        Callback<StartEndPair, Appointment> newInstanceCallback = TEMPORAL_INSTANCE;
        List<Appointment> madeAppointments = new ArrayList<>();
//        System.out.println("makeinstances:" + getStartRange() + " " + getEndRange());
        Stream<Temporal> removedTooEarly = stream(getStartRange()).filter(d -> ! VComponent.isBefore(d, getStartRange())); // inclusive
        Stream<Temporal> removedTooLate = takeWhile(removedTooEarly, a -> VComponent.isBefore(a, getEndRange())); // exclusive
        removedTooLate.forEach(temporalStart ->
        {
            TemporalAmount duration = endPriority().getDuration(this);
            System.out.println("make duration:" + duration);
            Temporal temporalEnd = temporalStart.plus(duration);
//            final Temporal temporalEnd;
//            switch (endPriority())
//            {
//            case DTEND:
//                final TemporalAmount duration;
//                if (isWholeDay())
//                {
//                    duration = Period.between(LocalDate.from(getDateTimeStart()), LocalDate.from(getDateTimeEnd()));
//                } else
//                {
//                    duration = Duration.between(getDateTimeStart(), getDateTimeEnd());
//                }
//                temporalEnd = temporalStart.plus(duration);
//                break;
//            case DURATION:
//                temporalEnd = temporalStart.plus(getDuration());                
//                break;
//            default:
//                throw new RuntimeException("Unknown EndPriority");
//            }
//            Appointment appt = newInstanceCallback.call(new StartEndPair(temporalStart, temporalEnd));
            Appointment appt = null;
            try { appt = getAppointmentClass().newInstance(); }
            catch (Exception e) { e.printStackTrace(); }
            appt.setStartTemporal(temporalStart);
            appt.setEndTemporal(temporalEnd);
            appt.setDescription(getDescription());
            appt.setSummary(getSummary());
            appt.setAppointmentGroup(getAppointmentGroup());
            appt.setWholeDay(isWholeDay());
            madeAppointments.add(appt);   // add appointments to return argument
            instances().add(appt); // add appointments to this object's collection
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
