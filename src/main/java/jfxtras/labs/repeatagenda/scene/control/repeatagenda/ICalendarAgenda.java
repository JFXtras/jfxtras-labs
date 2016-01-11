package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.DateTimeToCalendarHelper;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.EditPopupLoader;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda;
/**
 * Extension of JFXtras Agenda that uses iCalendar components to make appointments for
 * Agenda to render.
 * 
 * VComponents contains the iCalendar objects.
 * 
 * Appointment rendering:
 * Appointment rendering is handled by Agenda.  Agenda refreshes its rendering of appointments when changes to the
 * appointments ObservableList occur.
 * ICalendarAgenda handles changes to the vComponents list and refreshes refreshed when Agenda's localDateTimeRangeCallback fires.
 * 
 * @author David Bal
 *
 */
public class ICalendarAgenda extends Agenda {
    
    private static String AGENDA_STYLE_CLASS = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();

    
    // default appointment group list
    // if any element has been edited the edit list must be added to appointmentGroups
    final public static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
        = javafx.collections.FXCollections.observableArrayList(
                IntStream
                .range(0, 24)
                .mapToObj(i -> new ICalendarAgenda.AppointmentGroupImpl()
                       .withStyleClass("group" + i)
//                       .withKey(i)
                       .withDescription("group" + (i < 10 ? "0" : "") + i))
                .collect(Collectors.toList()));

    private LocalDateTimeRange dateTimeRange; // date range of current skin, set when localDateTimeRangeCallback fires
    public LocalDateTimeRange getDateTimeRange() { return dateTimeRange; }
    
    /** VComponents are iCalendar compliant calendar components.
     * They make appointments for Agenda to render. */
    public ObservableList<VComponent<Appointment>> vComponents() { return vComponents; }
    private ObservableList<VComponent<Appointment>> vComponents = FXCollections.observableArrayList();
    
    /** VEvent class - used in factory to instantiate new VEvent objects */
    Class<? extends VComponent<Appointment>> getVEventClass() { return vEventClass; }
    private Class<? extends VComponent<Appointment>> vEventClass = VEventImpl.class; // default class, change if other implementation is used
    public void setVEventClass(Class<? extends VComponent<Appointment>> clazz) { vEventClass = clazz; }

    // Extended appointment class used by the implementor - used to instantiate new appointment objects
    private Class<? extends Appointment> appointmentClass = Agenda.AppointmentImpl.class; // set to default class, change if using own implementation
    Class<? extends Appointment> getAppointmentClass() { return appointmentClass; }
    public void setAppointmentClass(Class<? extends Appointment> clazz) { appointmentClass = clazz; }

    /** Callback for creating unique identifier values 
     * @see VComponent#getUidGeneratorCallback() */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = VComponent.DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
    // I/O callbacks, must be set to provide I/O functionality, null by default - TODO - NOT IMPLEMENTED YET
    private Callback<Collection<VComponent< Appointment>>, Void> repeatWriteCallback = null;
    public void setRepeatWriteCallback(Callback<Collection<VComponent<Appointment>>, Void> repeatWriteCallback) { this.repeatWriteCallback = repeatWriteCallback; }

    private Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback = null; // TODO - NOT IMPLEMENTED YET
    public void setAppointmentGroupWriteCallback(Callback<Collection<AppointmentGroup>, Void> appointmentWriteCallback) { this.appointmentGroupWriteCallback = appointmentGroupWriteCallback; }

    // listen for additions to appointments from agenda. This listener must be removed and added back when a change
    // in the time range  occurs.
    private ListChangeListener<Appointment> appointmentsListener;

    // listen for changes to vComponents.
    private ListChangeListener<VComponent<Appointment>> vComponentsListener;
        
    // CONSTRUCTOR
    public ICalendarAgenda()
    {
        super();
        
        // setup i18n resource bundle
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.repeatagenda.i18n.ICalendarAgenda", myLocale);
        Settings.setup(resources);
        
        appointmentsListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            System.out.println("appointments changed2:");
            while (change.next())
            {
                // Deletions handled by vComponents listener
                if (change.wasAdded())
                {
                    change.getAddedSubList()
                            .stream()
                            .forEach(a -> 
                            { // make new VComponent
                                VComponent<Appointment> newVComponent = VComponentFactory
                                        .newVComponent(getVEventClass(), a, appointmentGroups());
                                LocalDateTime startRange = getDateTimeRange().getStartLocalDateTime();
                                LocalDateTime endRange = getDateTimeRange().getEndLocalDateTime();
                                newVComponent.setStartRange(startRange);
                                newVComponent.setEndRange(endRange);
                                newVComponent.setUidGeneratorCallback(getUidGeneratorCallback());
                                newVComponent.setUniqueIdentifier(getUidGeneratorCallback().call(null));
                                vComponents().removeListener(vComponentsListener);
                                vComponents.add(newVComponent);
                                vComponents().addListener(vComponentsListener);
                            });
                }
            }
        };
        
        vComponentsListener = (ListChangeListener.Change<? extends VComponent<Appointment>> change) ->
        {
            System.out.println("vcomponents changed:");
            while (change.next())
            {
                if (change.wasAdded() && (dateTimeRange != null)) // don't make appointment if range is not set
                {
                    LocalDateTime start = getDateTimeRange().getStartLocalDateTime();
                    LocalDateTime end = getDateTimeRange().getEndLocalDateTime();
                    List<Appointment> newAppointments = new ArrayList<>();
                    // add new appointments
                    change.getAddedSubList()
                            .stream()
                            .forEach(v -> 
                            {
                                if (v.instances().isEmpty()) newAppointments.addAll(v.makeInstances(start, end));
                            });
                    appointments().removeListener(appointmentsListener);
                    appointments().addAll(newAppointments);
                    appointments().addListener(appointmentsListener);
                } else if (change.wasRemoved())
                {
                    // remove associated appointments
                    change.getRemoved()
                        .stream()
                        .forEach(v -> appointments().removeIf(a -> v.instances().stream().anyMatch(a2 -> a2 == a)));
                }
            }
        };
        
//        Locale myLocale = Locale.getDefault();
//        appointments().addListener((InvalidationListener) obs -> System.out.println("changed appointments1:"));

        // Listen for changes to appointments (additions and deletions)
        appointments().addListener(appointmentsListener);

        // Listen for changes to vComponents (additions and deletions)
        vComponents().addListener(vComponentsListener);
        
        // CHANGE DEFAULT EDIT POPUP - replace default popup with one with repeat options
        setEditAppointmentCallback((Appointment appointment) ->
        {
            // Match appointment to VComponent
            VComponent<Appointment> vevent = vComponents()
                    .stream()
                    .filter(v -> v.instances().contains(appointment))
                    .findFirst()
                    .get();

            appointments().removeListener(appointmentsListener); // remove listener to prevent making extra vEvents during edit
            Stage editPopup = new EditPopupLoader(
                      appointment
                    , vevent
                    , this
                    , appointmentGroupWriteCallback
                    , repeatWriteCallback // write repeat callback initialized to null
                    , a -> { this.refresh(); return null; }); // refresh agenda
            
            // remove listener to prevent making extra vEvents during edit
            editPopup.setOnShowing((windowEvent) -> appointments().removeListener(appointmentsListener));
            editPopup.show();
            editPopup.setOnHiding((windowEvent) -> appointments().addListener(appointmentsListener)); // return listener
            return null;
        });
        
        // LISTEN FOR AGENDA RANGE CHANGES
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
//            System.out.println("range callback:");
            this.dateTimeRange = dateTimeRange;
            if (dateTimeRange != null)
            {
                // Remove instances and appointments
                vComponents().stream().forEach(v -> v.instances().clear());
        
                appointments().removeListener(appointmentsListener); // remove appointmentListener to prevent making extra vEvents during refresh
                appointments().clear();
                System.out.println("vs:" + vComponents().size());
                vComponents().stream().forEach(r ->
                {
//                    r.setDateTimeRangeStart(dateTimeRange.getStartLocalDateTime());
//                    r.setDateTimeRangeEnd(dateTimeRange.getEndLocalDateTime());
                    LocalDateTime start = getDateTimeRange().getStartLocalDateTime();
                    LocalDateTime end = getDateTimeRange().getEndLocalDateTime();
                    System.out.println(r.getSummary() + " " + start + " " + end);
                    Collection<Appointment> newAppointments = r.makeInstances(start, end);
                    appointments().addAll(newAppointments);
                });
                appointments().addListener(appointmentsListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
        
    }

//    /**
//     * Clear appointments and rebuild them from vComponents.  This is run when
//     * either the dateTimeRange changes, or changes are made to the vComponents list.
//     */
//    @Override
//    public void refresh()
//    {
//        if (dateTimeRange != null)
//        {
//            // Remove instances and appointments
//            vComponents().stream().forEach(v -> v.instances().clear());
//    
//            appointments().removeListener(appointmentsListener); // remove appointmentListener to prevent making extra vEvents during refresh
//            appointments().clear();
//            vComponents().stream().forEach(r ->
//            {
////                r.setDateTimeRangeStart(dateTimeRange.getStartLocalDateTime());
////                r.setDateTimeRangeEnd(dateTimeRange.getEndLocalDateTime());
//                LocalDateTime start = getDateTimeRange().getStartLocalDateTime();
//                LocalDateTime end = getDateTimeRange().getEndLocalDateTime();
//                Collection<Appointment> newAppointments = r.makeInstances(start, end);
//                appointments().addAll(newAppointments);
//            });
//            appointments().addListener(appointmentsListener); // add back appointmentListener
//        }
//    }
    
    /** Example Appointment class with overridden equals method used by unit testing */
    static public class AppointmentImplLocal2 extends AppointmentImplLocal
    {
        @Override
        public boolean equals(Object obj)
        {
            if (obj == this) return true;
            if((obj == null) || (obj.getClass() != getClass())) {
                return false;
            }
            AppointmentImplLocal2 testObj = (AppointmentImplLocal2) obj;

            boolean descriptionEquals = (getDescription() == null) ?
                    (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
            boolean locationEquals = (getLocation() == null) ?
                    (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
            boolean summaryEquals = (getSummary() == null) ?
                    (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
            boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                    (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
            System.out.println("AppointmentImplLocal2 equals: " + descriptionEquals + " " + locationEquals 
                    + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
            return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals ;
        }
    }
    
    /**
     * VComponent factory methods
     * 
     * @author David Bal
     *
     */
    static public class VComponentFactory
    {
                
        /** Return new VComponent made from an Appointment
         * Used when graphically adding an appointment to Agenda.
         * @param <U>
         * 
         * @param vComponentClass - class of new VEvent
         * @param appointment - an Agenda Appointment
         * @param appointmentGroups - list of AppointmentGroups
         * @return
         */
        public static <U extends Appointment> VComponent<U> newVComponent(
                Class<? extends VComponent<U>> vComponentClass
              , U appointment
              , ObservableList<AppointmentGroup> appointmentGroups)
        {
            try {
                return vComponentClass
                        .getConstructor(Appointment.class, ObservableList.class)
                        .newInstance(appointment, appointmentGroups);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }
        
        @SuppressWarnings("unchecked")
        public static <U extends Appointment> VComponent<U> newVComponent(VComponent<U> vComponent)
        {
            try {
                return vComponent.getClass()
                        .getConstructor(vComponent.getClass())
                        .newInstance(vComponent);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    /**
     * Appointment factory methods
     * 
     * @author David Bal
     *
     */
    static public class AppointmentFactory {

        /** returns new Appointment */
        public static <T extends Appointment> T newAppointment(Class<T> appointmentClass)
        {
            try {
                return appointmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        /** Returns deep copy of source appointment */
        @SuppressWarnings("unchecked")
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
    
    // TODO - revise Appointment interface changes
    // I MAY JUST REMOVE ALL CODE BELOW HERE
    /** Contains all the appointment data - no repeatable information
    *   Like Appointment, but contains extra fields - no repeat object */
    static public interface Appointment2 extends Agenda.Appointment
    {
        // TODO - add all iCalendar properties for VEvent
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
            return appointment;
        }
        
    }
    

    /** Contains repeatable information */
    static public interface RepeatableAppointment extends Appointment2
    {
//        void setCreated(LocalDateTime created);
        
//        /** Date/time of repeatable appointment this appointment is replacing, if any (as defined by iCalendar RFC 5545, 3.8.4.4) */
//        LocalDateTime getRecurrance();
//        void setRecurrance(LocalDateTime t); // If not null, contains the start date and time of recurring appointment this appointment takes the place of
//
//        /** True if appointment is part of a repeat rule AND has no unique data fields */
//        boolean isRepeatMade();
//        void setRepeatMade(boolean repeatMade);
//
//        /** Repeat rule attached to appointment, if null appointment is individual */
//        Repeat getRepeat();
//        void setRepeat(Repeat repeat);
//
//        /** Repeat rule attached to appointment, if null appointment is individual */
//        VEvent getVEvent();
//        void setVEvent(VEvent vevent);
    }
    
    /** Contains appointment data and repeatable information */
    static public abstract class RepeatableAppointmentImplBase<T> extends AppointmentImplBase2<T> implements RepeatableAppointment
    {

        protected RepeatableAppointmentImplBase() { }
               
        /** VEvent - defined in RFC 5545 iCalendar 3.6.1, page 52. */
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
        
//        /** Copy fields from this to input parameter appointment */
//        @Override
//        public Appointment copyFieldsTo(Appointment destination) {
//            boolean copyRepeat = ! ((((RepeatableAppointment) destination).getRepeat() == null) ||  (getRepeat() == null));
//            System.out.println("RepeatableAppointmentImplBase copyFieldsTo " + copyRepeat + " " + (((RepeatableAppointment) destination).getRepeat() == null) + " " + (getRepeat() == null) + "");
//            if ((destination instanceof RepeatableAppointmentImplBase) && copyRepeat)
//            {
//                copy(this, (RepeatableAppointmentImplBase<T>) destination);
//            }
//            return super.copyFieldsTo(destination);
//        }
        
        /** Copy fields from source to destination */
        private static void copy(RepeatableAppointmentImplBase<?> source, RepeatableAppointmentImplBase<?> destination)
        {
//            if ((source.getRepeat() != null))
//            { // only copy Repeat if BOTH source and destination have something in repeat
//                Repeat r = RepeatFactory.newRepeat(source.getRepeat());
//                destination.setRepeat(r);
//            }
//            destination.setRepeatMade(source.isRepeatMade());
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
//          System.out.println("repeats " + getRepeat() + " " + testObj.getRepeat());
//          boolean repeatEquals = (getRepeat() == null) ?
//                  (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
//          System.out.println("RepeatableAppointmentImplBase equals1 " + descriptionEquals + " " + locationEquals 
//                  + " " + summaryEquals + " " +  " " + appointmentGroupEquals + " " + repeatEquals + " " + getRepeat() + " " + testObj.getRepeat());
          return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals ;
      }

    }
    
    /**
     * Class implementing Appointment that includes an icon Pane field for easy rendering of
     * color boxes representing each AppointmentGroup
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
        public AppointmentGroupImpl withStyleClass(String value)
        {
            setStyleClass(value);
            makeIcon();
            return this; 
        }
        
        private Pane icon;
        public Pane getIcon() { return icon; }
        void makeIcon()
        {
            icon = new Pane();
            icon.setPrefSize(20, 20);
//            icon.getStyleClass().add(Agenda.class.getSimpleName());
            icon.getStylesheets().add(AGENDA_STYLE_CLASS);
            icon.getStyleClass().addAll("AppointmentGroup", getStyleClass());
        }

//        private int key = 0;
//        public int getKey() { return key; }
//        public void setKey(int key) { this.key = key; }
//        public AppointmentGroupImpl withKey(int key) {setKey(key); return this; }
        
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
