package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.RepeatMenu;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda;

public class RepeatableAgenda extends Agenda {
    
    private static String AGENDA_STYLE_CLASS = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
    final public static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
        = javafx.collections.FXCollections.observableArrayList(
                IntStream
                .range(0, 24)
                .mapToObj(i -> new RepeatableAgenda.AppointmentGroupImpl()
                       .withStyleClass("group" + i)
                       .withKey(i)
                       .withDescription("group" + (i < 10 ? "0" : "") + i))
                .collect(Collectors.toList()));

    private LocalDateTimeRange dateTimeRange; // date range of current skin
    public LocalDateTimeRange getDateTimeRange() { return dateTimeRange; }
    
    /** Repeat rules */
    private Collection<Repeat> repeats;
    public Collection<Repeat> repeats() { return repeats; }
    public void setRepeats(Collection<Repeat> repeatRules)
    {
        this.repeats = repeatRules;
        if (getAppointmentsIndividual() != null)
        { // In cast individual appointments are set first collect individual appointments that are recurrences and add to repeat appointment list
            repeats().stream().forEach(r ->
                { // each repeat
                    Set<RepeatableAppointment> s = getAppointmentsIndividual() // add individual appointments to repeat, if its a recurrance of a repeat
                            .stream()
                            .map(a -> (RepeatableAppointment) a)
                            .filter(a -> a.getRepeat() != null)
                            .filter(a -> a.getRepeat().equals(this))
//                            .filter(a -> repeatMap.containsKey(a))
//                            .filter(a -> repeatMap.get(a).equals(this))
                            .collect(Collectors.toSet());
                    r.appointments().addAll(s);
                });
//            repeats().stream().forEach(a -> a.collectAppointments(getAppointmentsIndividual())); // add individual appointments that have repeat rules to their Repeat objects
        }

    }
    
    // Extended repeat class used by the implementor - used to instantiate new repeat objects
    private Class<? extends Repeat> repeatClass = RepeatImpl.class; // default class, change if other implementation is used
    Class<? extends Repeat> getRepeatClass() { return repeatClass; }
    public void setRepeatClass(Class<? extends Repeat> clazz) { repeatClass = clazz; }

    // Extended appointment class used by the implementor - used to instantiate new appointment objects
    private Class<? extends RepeatableAppointment> appointmentClass = RepeatableAppointmentImpl.class; // set to default class, change if using own implementation
    Class<? extends RepeatableAppointment> getAppointmentClass() { return appointmentClass; }
    public void setAppointmentClass(Class<? extends RepeatableAppointment> clazz) { appointmentClass = clazz; }

    // I/O callbacks, must be set to provide I/O functionality, null by default
    private Callback<Collection<Appointment>, Void> appointmentWriteCallback = null;
    public void setAppointmentWriteCallback(Callback<Collection<Appointment>, Void> appointmentWriteCallback) { this.appointmentWriteCallback = appointmentWriteCallback; }
    private Callback<Collection<Repeat>, Void> repeatWriteCallback = null;
    public void setRepeatWriteCallback(Callback<Collection<Repeat>, Void> repeatWriteCallback) { this.repeatWriteCallback = repeatWriteCallback; }
    private Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback = null;
    public void setAppointmentGroupWriteCallback(Callback<Collection<AppointmentGroup>, Void> appointmentWriteCallback) { this.appointmentGroupWriteCallback = appointmentGroupWriteCallback; }

    
    /** Individual appointments - kept updated with appointments */
    private Collection<Appointment> appointmentsIndividual = new HashSet<Appointment>(); //FXCollections.observableArrayList();
    public Collection<Appointment> getAppointmentsIndividual() { return appointmentsIndividual; }
    public void setIndividualAppointments(Collection<? extends Appointment> list)
    {
//        appointmentsIndividual = list;
        appointmentsIndividual.addAll(list);
        if (repeats() != null)
        { // In cast individual appointments are set first
            repeats().stream().forEach(r ->
            { // each repeat
                Set<RepeatableAppointment> s = getAppointmentsIndividual() // add individual appointments to repeat, if its a recurrance of a repeat
                        .stream()
                        .map(a -> (RepeatableAppointment) a)
                        .filter(a -> a.getRepeat() != null)
                        .filter(a -> a.getRepeat().equals(this))
//                        .filter(a -> repeatMap.containsKey(a))
//                        .filter(a -> repeatMap.get(a).equals(this))
                        .collect(Collectors.toSet());
                r.appointments().addAll(s);
            });
        }
    }
//   
//    /** Repeat-made appointments - kept updated with appointments */
//    private ObservableList<T> appointmentsRepeatMade = FXCollections.observableArrayList();
//    public Collection<T> getAppointmentsRepeatMade() { return appointmentsRepeatMade; }
//    public void setRepeatMadeAppointments(Collection<T> list) { repeatMadeAppointments = list; }
    
    /**
     * Constructor with individualAppointments collection and repeats collection provided.
     * These objects will be automatically be kept current with Agenda's data.
     * 
     * @param individualAppointments
     * @param repeats
     */
    public RepeatableAgenda(
            Collection<Appointment> individualAppointments
          , Collection<Repeat> repeats
          , Class<? extends Repeat> repeatClass)
    {
        this();
        getAppointmentsIndividual().addAll(individualAppointments);
        setRepeats(repeats);
        this.repeatClass = repeatClass;
    }
    
    public RepeatableAgenda()
    {
        super();

        // setup default ResourceBundle
        Locale myLocale = Locale.getDefault();
        // TODO - GET PATH BETTER WAY
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.Bundle", myLocale);
        Settings.setup(resources);
//        System.out.println(resources);
//        System.exit(0);

        // Listen for changes to appointments (additions and deletions)
        appointments().addListener((ListChangeListener.Change<? extends Appointment> change)
            -> {
                while (change.next())
                {
                    if (change.wasReplaced())
                    {
                        List<? extends Appointment> removedAppointments = change.getRemoved();
                        Set<Appointment> removedIndividualAppointments = removedAppointments.stream()
                                .map(a -> ((RepeatableAppointment) a))
                                .filter(a -> ! a.isRepeatMade())
                                .peek(a -> System.out.println("removed individual " + a.getStartLocalDateTime()))
                                .collect(Collectors.toSet());
                            getAppointmentsIndividual().removeAll(removedIndividualAppointments);
                    }
                    if (change.wasAdded())
                    {
                        List<? extends Appointment> addedAppointments = change.getAddedSubList();
                        Set<Appointment> newIndividualAppointments = addedAppointments.stream()
                            .map(a -> ((RepeatableAppointment) a))
                            .filter(a -> ! a.isRepeatMade())
                            .peek(a -> System.out.println("added individual " + a.getStartLocalDateTime()))
                            .collect(Collectors.toSet());
                        getAppointmentsIndividual().addAll(newIndividualAppointments);
                    }
                }
            });
        
        // Change edit popup to provide one with repeat options
        setEditAppointmentCallback((Appointment appointment) ->
        {
            Stage repeatMenu = new RepeatMenu(
                    (RepeatableAppointment) appointment
                    , dateTimeRange
                    , appointments()
                    , repeats()
//                    , repeatMap
                    , appointmentGroups()
                    , appointmentClass
                    , repeatClass
                    , appointmentWriteCallback   // write appointment callback initialized to null
                    , appointmentGroupWriteCallback
                    , repeatWriteCallback // write repeat callback initialized to null
                    , a -> { this.refresh(); return null; }); // refresh agenda
            repeatMenu.show();
            return null;
        });
        
        // manage repeat-made appointments when the range changes
        setLocalDateTimeRangeCallback(dateTimeRange -> {
            this.dateTimeRange = dateTimeRange;
            LocalDateTime startDate = dateTimeRange.getStartLocalDateTime();
            LocalDateTime endDate = dateTimeRange.getEndLocalDateTime();
            appointments().removeIf(a -> ((RepeatableAppointment) a).isRepeatMade());

            repeats().stream().forEach(r ->
            { // remove repeat-made appointments, leave individual appointment recurrences
                Set<RepeatableAppointment> s = r.appointments()
                        .stream()
                        .filter(a -> a.getRepeat() == r)
//                        .filter(a -> ! repeatMap.containsKey(a))
                        .collect(Collectors.toSet());
                r.appointments().removeAll(s);
            });
            
//            repeats().stream().forEach(r -> {
//                r.getAppointments().clear());   
//            }
//            repeatMap.clear();
            repeats().stream().forEach(r ->
            { // Make new repeat-made appointments inside range
                Collection<RepeatableAppointment> newAppointments = r.makeAppointments(startDate, endDate);
                appointments().addAll(newAppointments);
            });
            return null; // return argument for the Callback
        });
    }
    
    // MAYBE I CAN GET RID OF THE BELOW INTERFACE AND JUST JUST APPOINTMENT
    // I JUST NEED TO KEEP A LIST OF REPEAT-MADE APPOINTMENTS HERE AND
    // COPY THEM TO APPOINTMENT LIST
    
// i can simulate isRepeatMade by checking to see if is in the list of repeat-made appointmetns
// I can simulate getRepeat by searching list of repeats for the appointment
// Are those alternatives too expensive?
// What do I do about the copy and equals methods?
    
    public boolean isRepeatMade(Appointment a)
    {
        return ! appointmentsIndividual.contains(a);
    }

    
    /** Contains all the appointment data - no repeatable information
    *   Like Appointment, but contains extra fields - no repeat object */
    static public interface Appointment3
    {
        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Unique identifier as defined by iCalendar RFC 5545, 3.8.4.7 */
        String getUID();
        void setUID(String uid);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Number of times appointment was edited.  Sequence as defined by iCalendar RFC 5545, 3.8.7.4 */
        int getSequence();
        void setSequence(int sequence);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Last date/time object was revised.  Date-Time Stamp as defined by iCalendar RFC 5545, 3.8.7.2 */
        LocalDateTime getDTStamp();
        void setDTStamp(LocalDateTime dtStamp);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Created date/time as defined by iCalendar RFC 5545, 3.8.7.1 */
        LocalDateTime getCreated();
        void setCreated(LocalDateTime created);

        
        // DEFAULTS - SHOULD BE INHERETED, BUT AREN'T - PROBLEM MAY BE RETROLAMBDA
        // ----
        // Calendar
        
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default Calendar getStartTime() {
            throw new RuntimeException("Not implemented");
        }
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default void setStartTime(Calendar c) {
            throw new RuntimeException("Not implemented");
        }
        
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default Calendar getEndTime() {
            throw new RuntimeException("Not implemented");
        }
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default void setEndTime(Calendar c) {
            throw new RuntimeException("Not implemented");
        }
        
        // ----
        // ZonedDateTime
        
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default ZonedDateTime getStartZonedDateTime() {
            return DateTimeToCalendarHelper.createZonedDateTimeFromCalendar(getStartTime());
        }
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default void setStartZonedDateTime(ZonedDateTime v) {
            setStartTime(DateTimeToCalendarHelper.createCalendarFromZonedDateTime(v));
        }
        
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default ZonedDateTime getEndZonedDateTime() {
            return DateTimeToCalendarHelper.createZonedDateTimeFromCalendar(getEndTime());
        }
        /** End is exclusive */
        default void setEndZonedDateTime(ZonedDateTime v) {
            setEndTime(DateTimeToCalendarHelper.createCalendarFromZonedDateTime(v));
        }
        
        // ----
        // LocalDateTime 
        
        /** This is what Agenda uses to render the appointments */
        default LocalDateTime getStartLocalDateTime() {
            return getStartZonedDateTime().toLocalDateTime();
        }
        /** This is what Agenda uses to render the appointments */
        default void setStartLocalDateTime(LocalDateTime v) {
            setStartZonedDateTime(ZonedDateTime.of(v, ZoneId.systemDefault()));
        }
        
        /** This is what Agenda uses to render the appointments */
        default LocalDateTime getEndLocalDateTime() {
            return getEndZonedDateTime() == null ? null : getEndZonedDateTime().toLocalDateTime();
        }
        /** End is exclusive */
        default void setEndLocalDateTime(LocalDateTime v) {
            setEndZonedDateTime(v == null ? null : ZonedDateTime.of(v, ZoneId.systemDefault()));
        }

    }
    
    
    /** Contains all the appointment data - no repeatable information
    *   Like Appointment, but contains extra fields - no repeat object */
    static public interface Appointment2 extends Agenda.Appointment
    {
        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Unique identifier as defined by iCalendar RFC 5545, 3.8.4.7 */
        String getUID();
        void setUID(String uid);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Number of times appointment was edited.  Sequence as defined by iCalendar RFC 5545, 3.8.7.4 */
        int getSequence();
        void setSequence(int sequence);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Last date/time object was revised.  Date-Time Stamp as defined by iCalendar RFC 5545, 3.8.7.2 */
        LocalDateTime getDTStamp();
        void setDTStamp(LocalDateTime dtStamp);

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Created date/time as defined by iCalendar RFC 5545, 3.8.7.1 */
        LocalDateTime getCreated();
        void setCreated(LocalDateTime created);

        
        // DEFAULTS - SHOULD BE INHERETED, BUT AREN'T - PROBLEM MAY BE RETROLAMBDA
        // ----
        // Calendar
        
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default Calendar getStartTime() {
            throw new RuntimeException("Not implemented");
        }
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default void setStartTime(Calendar c) {
            throw new RuntimeException("Not implemented");
        }
        
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default Calendar getEndTime() {
            throw new RuntimeException("Not implemented");
        }
        /** This method is not used by the control, it can only be called when implemented by the user through the default Datetime methods on this interface **/  
        default void setEndTime(Calendar c) {
            throw new RuntimeException("Not implemented");
        }
        
        // ----
        // ZonedDateTime
        
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default ZonedDateTime getStartZonedDateTime() {
            return DateTimeToCalendarHelper.createZonedDateTimeFromCalendar(getStartTime());
        }
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default void setStartZonedDateTime(ZonedDateTime v) {
            setStartTime(DateTimeToCalendarHelper.createCalendarFromZonedDateTime(v));
        }
        
        /** This is the replacement of Calendar, if you use ZonedDateTime be aware that the default implementations of the LocalDateTime methods in this interface convert LocalDateTime to ZonedDateTime using a rather crude approach */
        default ZonedDateTime getEndZonedDateTime() {
            return DateTimeToCalendarHelper.createZonedDateTimeFromCalendar(getEndTime());
        }
        /** End is exclusive */
        default void setEndZonedDateTime(ZonedDateTime v) {
            setEndTime(DateTimeToCalendarHelper.createCalendarFromZonedDateTime(v));
        }
        
        // ----
        // LocalDateTime 
        
        /** This is what Agenda uses to render the appointments */
        default LocalDateTime getStartLocalDateTime() {
            return getStartZonedDateTime().toLocalDateTime();
        }
        /** This is what Agenda uses to render the appointments */
        default void setStartLocalDateTime(LocalDateTime v) {
            setStartZonedDateTime(ZonedDateTime.of(v, ZoneId.systemDefault()));
        }
        
        /** This is what Agenda uses to render the appointments */
        default LocalDateTime getEndLocalDateTime() {
            return getEndZonedDateTime() == null ? null : getEndZonedDateTime().toLocalDateTime();
        }
        /** End is exclusive */
        default void setEndLocalDateTime(LocalDateTime v) {
            setEndZonedDateTime(v == null ? null : ZonedDateTime.of(v, ZoneId.systemDefault()));
        }

      Appointment copyNonUniqueFieldsTo(Appointment appointment, Appointment appointmentFromRepeatRule);
        
      Appointment copyFieldsTo(Appointment destination);

    }

    
    /** Contains all the appointment data - no repeatable information */
    static public abstract class AppointmentImplBase2<T> extends Agenda.AppointmentImplBase<T> implements Appointment2
    {
        // TODO - ADD PROPERTIES FOR NEW FIELDS AND WITH METHODS TOO
        @Override
        public String getUID() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setUID(String uid) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public int getSequence() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void setSequence(int sequence) {
            // TODO Auto-generated method stub
            
        }
        
        AppointmentImplBase2() { }
        
        /** Copy constructor 
         * @param <U>*/
        <T extends Appointment> AppointmentImplBase2(T appointment)
        {
            System.out.println("AppointmentImplBase2 constructor");
            if (appointment instanceof AppointmentImplBase2) copy((AppointmentImplBase2<T>) appointment, this);
//            setWholeDay(appointment.isWholeDay());
//            setLocation(appointment.getLocation());
//            setAppointmentGroup(appointment.getAppointmentGroup());
//            setDescription(appointment.getDescription());
//            setSummary(appointment.getSummary());
        }
        
        /** Copy fields from this to input parameter appointment */
        public Appointment copyFieldsTo(Appointment appointment) {
            System.out.println("AppointmentImplBase2 copyFieldsTo ");
            if (appointment instanceof AppointmentImplBase2) copy(this, (AppointmentImplBase2<T>) appointment);
            return appointment;
        }
        
        /** Copy fields from source to destination */
        private static void copy(AppointmentImplBase2<?> source, AppointmentImplBase2<?> destination)
        {
            destination.setWholeDay(source.isWholeDay());
            destination.setLocation(source.getLocation());
            destination.setAppointmentGroup(source.getAppointmentGroup());
            destination.setDescription(source.getDescription());
            destination.setSummary(source.getSummary());
            destination.setUID(source.getUID());
            destination.setSequence(source.getSequence());
            destination.setDTStamp(source.getDTStamp());
            destination.setCreated(source.getCreated());
        }
        
        // TODO - Add missing fields, make similar methods in inhereted classes
        /**
         * Copies this Appointment non-time fields into passed appointment
         * Used on recurrences when some of fields are unique and should not be copied.
         * This method must be overridden by an implementing class
         * 
         * @param appointment
         * @param appointmentFromRepeatRule
         * @param repeatMap
         * @return
         */
        public Appointment copyNonUniqueFieldsTo(Appointment appointment, Appointment appointmentFromRepeatRule) {
            if (appointment.getAppointmentGroup().equals(appointmentFromRepeatRule.getAppointmentGroup())) {
                appointment.setAppointmentGroup(getAppointmentGroup());
            }
            if (appointment.getDescription().equals(appointmentFromRepeatRule.getDescription())) {
                appointment.setDescription(getDescription());
            }
            if (appointment.getSummary().equals(appointmentFromRepeatRule.getSummary())) {
                appointment.setSummary(getSummary());
            }
//            Repeat repeat = repeatMap.get(appointmentFrom);
//            repeatMap.put(appointmentTo, repeat);
//            getRepeat().copyInto(appointment.getRepeat());
            return appointment;
        }
        
    }
    
//    /**
//     * TODO - This class may be obsolete and should be deleted
//     * Appointments only - no repeat rules
//     * Used as appointment data in the Repeat class.
//     * A class to help you get going using LocalDateTime; all the required methods of the interface are implemented as JavaFX properties 
//     */
//    @Deprecated
//    static public class AppointmentImplLocal2 extends AppointmentImplBase2<AppointmentImplLocal2> 
//    implements Appointment2
//    {
//       
//        /** StartDateTime: */
//        public ObjectProperty<LocalDateTime> startLocalDateTime() { return startLocalDateTime; }
//        final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startLocalDateTime");
//        public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
//        public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
//        public AppointmentImplLocal2 withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
//        
//        /** EndDateTime: */
//        public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTimeProperty; }
//        final private ObjectProperty<LocalDateTime> endLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(this, "endLocalDateTimeProperty");
//        public LocalDateTime getEndLocalDateTime() { return endLocalDateTimeProperty.getValue(); }
//        public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTimeProperty.setValue(value); }
//        public AppointmentImplLocal2 withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
//        
//        public String toString()
//        {
//            return super.toString()
//                 + ", "
//                 + this.getStartLocalDateTime()
//                 + " - "
//                 + this.getEndLocalDateTime()
//                 ;
//        }
//        
//        public AppointmentImplLocal2() {}
//        
//        public AppointmentImplLocal2(Appointment source) {
//            super(source);
//            if (source instanceof AppointmentImplLocal2) copy((AppointmentImplLocal2) source, this);
//            System.out.println("AppointmentImplLocal2 constructor");
////            if (source.getStartLocalDateTime() != null) setEndLocalDateTime(source.getEndLocalDateTime());
////            if (source.getEndLocalDateTime() != null) setStartLocalDateTime(source.getStartLocalDateTime());
//        }
//
//        /**
//         * Copies this Appointment fields into destination parameter
//         * This method must be overridden by an implementing class
//         * 
//         * @param destination
//         * @return
//         */
//        @Override
//        public Appointment copyFieldsTo(Appointment destination) {
//            System.out.println("AppointmentImplLocal2 copyFields");
//            if (destination instanceof AppointmentImplLocal2) copy(this, (AppointmentImplLocal2) destination);
////            destination.setStartLocalDateTime(getStartLocalDateTime());
////            destination.setEndLocalDateTime(getEndLocalDateTime());
//            return destination;
//        }
//        
//        /** Copy fields from source to destination */
//        private static void copy(AppointmentImplLocal2 source, AppointmentImplLocal2 destination)
//        {
//            destination.setStartLocalDateTime(source.getStartLocalDateTime());
//            destination.setEndLocalDateTime(source.getEndLocalDateTime());
//        }  
//
//        // used for unit testing, not needed by implementation
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == this) return true;
//            if((obj == null) || (obj.getClass() != getClass())) {
//                return false;
//            }
//            AppointmentImplLocal2 testObj = (AppointmentImplLocal2) obj;
//
//            boolean descriptionEquals = (getDescription() == null) ?
//                    (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
//            boolean locationEquals = (getLocation() == null) ?
//                    (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
//            boolean summaryEquals = (getSummary() == null) ?
//                    (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
//            boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
//                    (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
////            boolean repeatEquals = (getRepeat() == null) ?
////                    (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
//            System.out.println("Appointment2 equal " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals + " ");
//            return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
//        }
//        
//        @Override
//        public LocalDateTime getDTStamp() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public void setDTStamp(LocalDateTime dtStamp) {
//            // TODO Auto-generated method stub
//            
//        }
//
//        @Override
//        public LocalDateTime getCreated() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//        @Override
//        public void setCreated(LocalDateTime created) {
//            // TODO Auto-generated method stub
//            
//        }
//    }

    /** Contains repeatable information */
    static public interface RepeatableAppointment extends Appointment2
    {
//        void setCreated(LocalDateTime created);
        
        /** Date/time of repeatable appointment this appointment is replacing, if any (as defined by iCalendar RFC 5545, 3.8.4.4) */
        LocalDateTime getRecurrance();
        void setRecurrance(LocalDateTime t); // If not null, contains the start date and time of recurring appointment this appointment takes the place of

        /** True if appointment is part of a repeat rule AND has no unique data fields */
        boolean isRepeatMade();
        void setRepeatMade(boolean repeatMade);

        /** Repeat rule attached to appointment, if null appointment is individual */
        Repeat getRepeat();
        void setRepeat(Repeat repeat);

        /** Repeat rule attached to appointment, if null appointment is individual */
        VEvent getVEvent();
        void setVEvent(VEvent vevent);
    }
    
    /** Contains appointment data and repeatable information */
    static public abstract class RepeatableAppointmentImplBase<T> extends AppointmentImplBase2<T> implements RepeatableAppointment {

        protected RepeatableAppointmentImplBase() { }
               
        /** Repeat rules, null if an individual appointment */
        @Deprecated
        private Repeat repeat;
        @Deprecated
        public void setRepeat(Repeat repeat) { this.repeat = repeat; }
        @Deprecated
        public Repeat getRepeat() { return repeat; }
        @Deprecated
        public T withRepeat(Repeat repeat) { setRepeat(repeat); return (T)this; }

        /** VEvent - defined in RFC 5545 iCalendar 3.6.1, page 52. */
        // TODO - SOLVE PROBLEM WITH VEVENT GENERIC
        private VEvent vevent;
        public void setVEvent(VEvent vevent) { this.vevent = vevent; }
        public VEvent getVEvent() { return vevent; }
        public T withVEvent(VEvent vevent) { setVEvent(vevent); return (T)this; }
        
        /**
         * true = a temporary appointment created by a repeat rule
         * false = a individual permanent appointment
         */
        final private BooleanProperty repeatMade = new SimpleBooleanProperty(this, "repeatMade", false); // defaults to a individual permanent appointment
        public BooleanProperty repeatMadeProperty() { return repeatMade; }
        public boolean isRepeatMade() { return repeatMade.getValue(); }
        public void setRepeatMade(boolean b) {repeatMade.set(b); }
        public T withRepeatMade(boolean b) {repeatMade.set(b); return (T)this; }

        // TODO - FOR EDITED REPEATABLE APPOINTMENTS, THIS MARKS WHICH RECURRANCE THIS APPOINTMENT TAKES THE PLACE OF
        private LocalDateTime inPlaceOfRecurrance; // If not null, contains the start date and time of recurring appointment this appointment takes the place of
        public void setRecurranceLocalDateTime(LocalDateTime t) { inPlaceOfRecurrance = t; } // If not null, contains the start date and time of recurring appointment this appointment takes the place of

        /** Copy constructor 
         * @param <U>*/
        protected <U extends Appointment> RepeatableAppointmentImplBase(U source)
        {
            super(source);
            System.out.println("RepeatableAppointmentImplBase constructor ");
            if (source instanceof RepeatableAppointmentImplBase) copy((RepeatableAppointmentImplBase<T>) source, this);
        }
        
        /** Copy fields from this to input parameter appointment */
        @Override
        public Appointment copyFieldsTo(Appointment destination) {
            boolean copyRepeat = ! ((((RepeatableAppointment) destination).getRepeat() == null) ||  (getRepeat() == null));
            System.out.println("RepeatableAppointmentImplBase copyFieldsTo " + copyRepeat + " " + (((RepeatableAppointment) destination).getRepeat() == null) + " " + (getRepeat() == null) + "");
            if ((destination instanceof RepeatableAppointmentImplBase) && copyRepeat)
            {
                copy(this, (RepeatableAppointmentImplBase<T>) destination);
            }
            return super.copyFieldsTo(destination);
        }
        
        /** Copy fields from source to destination */
        private static void copy(RepeatableAppointmentImplBase<?> source, RepeatableAppointmentImplBase<?> destination)
        {
            if ((source.getRepeat() != null))
            { // only copy Repeat if BOTH source and destination have something in repeat
                Repeat r = RepeatFactory.newRepeat(source.getRepeat());
                destination.setRepeat(r);
            }
            destination.setRepeatMade(source.isRepeatMade());
        }       
        
      // used for unit testing, not needed by implementation
        // Is this in the right place?
      @Override
      public boolean equals(Object obj) {
          if (obj == this) return true;
          if((obj == null) || (obj.getClass() != getClass())) {
              return false;
          }
          RepeatableAppointment testObj = (RepeatableAppointment) obj;

//          System.out.println("getAppointmentGroup()2 " + getAppointmentGroup().getDescription() + " " + testObj.getAppointmentGroup().getDescription());
          
          boolean descriptionEquals = (getDescription() == null) ?
                  (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
          boolean locationEquals = (getLocation() == null) ?
                  (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
          boolean summaryEquals = (getSummary() == null) ?
                  (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
          boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                  (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
          System.out.println("repeats " + getRepeat() + " " + testObj.getRepeat());
          boolean repeatEquals = (getRepeat() == null) ?
                  (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
          System.out.println("RepeatableAppointmentImplBase equals1 " + descriptionEquals + " " + locationEquals 
                  + " " + summaryEquals + " " +  " " + appointmentGroupEquals + " " + repeatEquals + " " + getRepeat() + " " + testObj.getRepeat());
          return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals && repeatEquals;
      }

      @Override
      public LocalDateTime getRecurrance() {
          // TODO Auto-generated method stub
          return null;
      }
      @Override
      public void setRecurrance(LocalDateTime t) {
          // TODO Auto-generated method stub
          
      }
//      /** Checks if fields relevant for the repeat rule (non-time fields) are equal. */
//      // needs to be overridden by any class implementing Appointment or extending AppointmentImplBase
//      // Note: Location field is a problem - I think it should be removed.
//      public boolean repeatFieldsEquals(Object obj) {
//          return equals(obj);
//      }
    }
    
    /**
     * A class to help you get going; all the required methods of the interface are implemented as JavaFX properties 
     */
    static public class AppointmentGroupImpl implements AppointmentGroup
    {
        /** Description: */
        public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
        final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
        public String getDescription() { return descriptionObjectProperty.getValue(); }
        public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
        public AppointmentGroupImpl withDescription(String value) { setDescription(value); return this; } 
                
        /** StyleClass: */
        public ObjectProperty<String> styleClassProperty() { return styleClassObjectProperty; }
        final private ObjectProperty<String> styleClassObjectProperty = new SimpleObjectProperty<String>(this, "styleClass");
        public String getStyleClass() { return styleClassObjectProperty.getValue(); }
        public void setStyleClass(String value) { styleClassObjectProperty.setValue(value); }
        public AppointmentGroupImpl withStyleClass(String value) {
            setStyleClass(value);
            icon = new Pane();
            icon.setPrefSize(20, 20);
//            icon.getStyleClass().add(Agenda.class.getSimpleName());
            icon.getStylesheets().add(AGENDA_STYLE_CLASS);
            icon.getStyleClass().addAll("AppointmentGroup", getStyleClass());
            return this; 
        }
        
        private Pane icon;
        public Pane getIcon() { return icon; }

        private int key = 0;
        public int getKey() { return key; }
        public void setKey(int key) { this.key = key; }
        public AppointmentGroupImpl withKey(int key) {setKey(key); return this; }
        
        }
    
    static public class RepeatFactory {
                
//        public static Repeat newRepeat(
//                Class<? extends Repeat> repeatClass
//              , LocalDateTimeRange dateTimeRange
//              , Class<? extends RepeatableAppointment> appointmentClass)
//        {
//                try {
//                    return repeatClass
//                            .getConstructor(Class.class)
//                            .newInstance(appointmentClass)
//                            .withAppointmentClass(appointmentClass)
//                            .withLocalDateTimeDisplayRange(dateTimeRange);
//                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
//                    e.printStackTrace();
//                }
//            return null;
//        }
        @Deprecated
        public static Repeat newRepeat(Class<? extends Repeat> repeatClass)
        {
            try {
                return repeatClass.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static <T extends Repeat> Repeat newRepeat(T source)
        {
            try {
                return (T) source.getClass()
                        .getConstructor(Repeat.class)
                        .newInstance(source);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
          return null;
        }

        public static Repeat newRepeat(
                Class<? extends Repeat> repeatClass
              , Class<? extends RepeatableAppointment> appointmentClass)
        {
                try {
                    return repeatClass
                            .getConstructor(Class.class)
                            .newInstance(appointmentClass)
                            .withAppointmentClass(appointmentClass);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
            return null;
        }
    }
    
    static public class AppointmentFactory {
        
//        @Deprecated
//        public static RepeatableAppointment newRepeatableAppointment(
//                Class<? extends RepeatableAppointment> appointmentClass
//              , Class<? extends Repeat> repeatClass)
//        {
//            try {
//                RepeatableAppointment a = appointmentClass.newInstance();
//                Repeat r = RepeatFactory.newRepeat(repeatClass);
//                a.setRepeat(r);
//                return a;
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        /** Builds an incomplete Appointment object with the Repeat field null - used as data for a repeat object */
//        // TODO - SHOULD I MAKE A NEW CLASS WITHOUT REPEATS IN IT (APPT DATA)?
//        @Deprecated
//        public static RepeatableAppointment newRepeatableAppointment(Class<? extends RepeatableAppointment> appointmentClass)
//        {
//            try {
//                return appointmentClass.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }

//        // Returns deep copy of RepeatableAppointment
//        public static RepeatableAppointment newRepeatableAppointment(RepeatableAppointment appointment)
//        {
//            Class<? extends RepeatableAppointment> appointmentClass = appointment.getClass();
//            RepeatableAppointment a = null;
//            try {
//                a = appointmentClass.getConstructor(RepeatableAppointment.class).newInstance(appointment);
//            } catch (InstantiationException | IllegalAccessException
//                    | IllegalArgumentException | InvocationTargetException
//                    | NoSuchMethodException | SecurityException e) {
//                e.printStackTrace();
//            }
//
////          RepeatableAppointment a = newRepeatableAppointment(appointment.getClass());            
////            appointment.copyInto(a);
//            if (appointment.getRepeat() != null)
//            {
//                Repeat r = RepeatFactory.newRepeat(appointment.getRepeat().getClass());
//                appointment.getRepeat().copyInto(r);
//                Appointment2 a2 = newAppointment(appointment.getRepeat().getAppointmentData());
//                r.setAppointmentData(a2);
//                a.setRepeat(r);
//            }
//            return a;
//        }

        public static <T extends Appointment> T newAppointment(Class<T> appointmentClass)
        {
            try {
                return appointmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        // Returns deep copy of source
        public static <T extends Appointment> T newAppointment(T source)
        {
            try {
                return (T) source.getClass()
                        .getConstructor(Appointment.class) // gets copy constructor
                        .newInstance(source);               // calls copy constructor
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
          return null;

        }
    }

    /** Add ResourceBundle for FXML controllers that contains strings for the appointment popups */
    public void setResourceBundle(ResourceBundle resources) {
        Settings.setup(resources);
    }

//    public static RepeatableAppointment AppointmentFactory(RepeatableAppointment source) {
//        
//        return null;
//    }
        
}
