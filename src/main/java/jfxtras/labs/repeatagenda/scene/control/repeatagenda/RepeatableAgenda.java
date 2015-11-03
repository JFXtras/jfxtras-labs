package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
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
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.RepeatMenu;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.RepeatableAgenda.RepeatableAppointment;
import jfxtras.scene.control.agenda.Agenda;

public class RepeatableAgenda<T extends RepeatableAppointment> extends Agenda {
    
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
                    r.getAppointments().addAll(s);
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
    private Collection<T> appointmentsIndividual = new HashSet<T>(); //FXCollections.observableArrayList();
    public Collection<T> getAppointmentsIndividual() { return appointmentsIndividual; }
    public void setIndividualAppointments(Collection<T> list)
    {
        appointmentsIndividual = list;
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
                r.getAppointments().addAll(s);
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
            Collection<T> individualAppointments
          , Collection<Repeat> repeats
          , Class<? extends Repeat> repeatClass)
    {
        getAppointmentsIndividual().addAll(individualAppointments);
        setRepeats(repeats);
        this.repeatClass = repeatClass;
    }
    
    public RepeatableAgenda()
    {
        // Listen for changes to appointments (additions and deletions)
        appointments().addListener((ListChangeListener.Change<? extends Appointment> change)
            -> {
                while (change.next())
                {
                    if (change.wasReplaced())
                    {
                        List<? extends Appointment> removedAppointments = change.getRemoved();
                        Set<T> removedIndividualAppointments = removedAppointments.stream()
                                .map(a -> ((T) a))
                                .filter(a -> ! a.isRepeatMade())
                                .peek(a -> System.out.println("removed individual " + a.getStartLocalDateTime()))
                                .collect(Collectors.toSet());
                            getAppointmentsIndividual().removeAll(removedIndividualAppointments);
                    }
                    if (change.wasAdded())
                    {
                        List<? extends Appointment> addedAppointments = change.getAddedSubList();
                        Set<T> newIndividualAppointments = addedAppointments.stream()
                            .map(a -> ((T) a))
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
                Set<RepeatableAppointment> s = r.getAppointments()
                        .stream()
                        .filter(a -> a.getRepeat() == r)
//                        .filter(a -> ! repeatMap.containsKey(a))
                        .collect(Collectors.toSet());
                r.getAppointments().removeAll(s);
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
    
//    /** map that matches repeatable appointments to the Repeat that made them */
//    // When repeat-made appointments are made they need to be added to this map
//    private Map<Appointment, Repeat> repeatMap = new HashMap<Appointment, Repeat>();
//    Map<Appointment, Repeat> getRepeatMap() { return repeatMap; }
//    public void setRepeat(Appointment a, Repeat r)
//    {
//        repeatMap.put(a, r);
//    }
//    public Repeat getRepeat(Appointment a)
//    {
//        return repeatMap.get(a);
//    }

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
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * This method must be overridden by an implementing class
         * 
         * @param appointment
         * @return
         */
        @Deprecated
        default Appointment2 copyInto(Appointment2 appointment) {
            appointment.setAppointmentGroup(getAppointmentGroup());
            appointment.setDescription(getDescription());
            appointment.setSummary(getSummary());
            return appointment;
        }
        
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
        @Deprecated
        default Appointment2 copyInto(Appointment2 appointment, Appointment2 appointmentFromRepeatRule) {
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

    
    /** Contains all the appointment data - no repeatable information */
    static public abstract class AppointmentImplBase2<T> extends Agenda.AppointmentImplBase<T> implements Appointment2
    {
        AppointmentImplBase2() { }
        
        /** Copy constructor */
        AppointmentImplBase2(Appointment2 a)
        {
            setWholeDay(a.isWholeDay());
            setLocation(a.getLocation());
            setAppointmentGroup(a.getAppointmentGroup());
            setDescription(a.getDescription());
            setSummary(a.getSummary());
        }
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

    }
    
    /**
     * Appointments only - no repeat rules
     * Used as appointment data in the Repeat class.
     * A class to help you get going using LocalDateTime; all the required methods of the interface are implemented as JavaFX properties 
     */
    static public class AppointmentImplLocal2 extends AppointmentImplBase2<AppointmentImplLocal2> 
    implements Appointment2
    {
        public AppointmentImplLocal2() {}
        
        public AppointmentImplLocal2(Appointment2 a) {
            super(a);
            setEndLocalDateTime(a.getEndLocalDateTime());
            setStartLocalDateTime(a.getStartLocalDateTime());
        }
        /** StartDateTime: */
        public ObjectProperty<LocalDateTime> startLocalDateTime() { return startLocalDateTime; }
        final private ObjectProperty<LocalDateTime> startLocalDateTime = new SimpleObjectProperty<LocalDateTime>(this, "startLocalDateTime");
        public LocalDateTime getStartLocalDateTime() { return startLocalDateTime.getValue(); }
        public void setStartLocalDateTime(LocalDateTime value) { startLocalDateTime.setValue(value); }
        public AppointmentImplLocal2 withStartLocalDateTime(LocalDateTime value) { setStartLocalDateTime(value); return this; }
        
        /** EndDateTime: */
        public ObjectProperty<LocalDateTime> endLocalDateTimeProperty() { return endLocalDateTimeProperty; }
        final private ObjectProperty<LocalDateTime> endLocalDateTimeProperty = new SimpleObjectProperty<LocalDateTime>(this, "endLocalDateTimeProperty");
        public LocalDateTime getEndLocalDateTime() { return endLocalDateTimeProperty.getValue(); }
        public void setEndLocalDateTime(LocalDateTime value) { endLocalDateTimeProperty.setValue(value); }
        public AppointmentImplLocal2 withEndLocalDateTime(LocalDateTime value) { setEndLocalDateTime(value); return this; } 
        
        public String toString()
        {
            return super.toString()
                 + ", "
                 + this.getStartLocalDateTime()
                 + " - "
                 + this.getEndLocalDateTime()
                 ;
        }

        // used for unit testing, not needed by implementation
        @Override
        public boolean equals(Object obj) {
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
//            boolean repeatEquals = (getRepeat() == null) ?
//                    (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
            System.out.println("Appointment2 equal " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals + " ");
            return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
        }
        
        @Override
        public LocalDateTime getDTStamp() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setDTStamp(LocalDateTime dtStamp) {
            // TODO Auto-generated method stub
            
        }

        @Override
        public LocalDateTime getCreated() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void setCreated(LocalDateTime created) {
            // TODO Auto-generated method stub
            
        }
        
        // These are defaults and shouldn't be here. Some kind of an error.
        @Override
        public Calendar getStartTime() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void setStartTime(Calendar c) {
            // TODO Auto-generated method stub
            
        }
        @Override
        public Calendar getEndTime() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void setEndTime(Calendar c) {
            // TODO Auto-generated method stub
            
        }
        @Override
        public ZonedDateTime getStartZonedDateTime() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void setStartZonedDateTime(ZonedDateTime v) {
            // TODO Auto-generated method stub
            
        }
        @Override
        public ZonedDateTime getEndZonedDateTime() {
            // TODO Auto-generated method stub
            return null;
        }
        @Override
        public void setEndZonedDateTime(ZonedDateTime v) {
            // TODO Auto-generated method stub
            
        }
    }

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

//        // Copy methods - required to reverse edit changes when cancel is selected and for editing "this and future" repeatable appointments.
//        /**
//         * Copies all fields into parameter appointment.
//         * This method must be overridden by an implementing class
//         * 
//         * @param appointment
//         * @return
//         */
//        default RepeatableAppointment copyInto(RepeatableAppointment appointment)
//        {
//            Appointment2.super.copyInto(appointment);
//            appointment.setEndLocalDateTime(getEndLocalDateTime());
//            appointment.setStartLocalDateTime(getStartLocalDateTime());
////            System.out.println("copy repeat " + appointment + " " + appointment.getRepeat());
////            if (getRepeat() != null )
////            {
////                getRepeat().copyInto(appointment.getRepeat());
////            }
////            super.copyInto(appointment);
//            return appointment;
//        }
    }
    
    /** Contains appointment data and repeatable information */
    static public abstract class RepeatableAppointmentImplBase<T> extends AppointmentImplBase2<T> implements RepeatableAppointment {

        protected RepeatableAppointmentImplBase() { }
        
        /** Copy constructor */
        protected RepeatableAppointmentImplBase(RepeatableAppointment a)
        {
            super(a);
        }
        
        /** Repeat rules, null if an individual appointment */
        private Repeat repeat;
        public void setRepeat(Repeat repeat) { this.repeat = repeat; }
        public Repeat getRepeat() { return repeat; }
        public T withRepeat(Repeat value) { setRepeat(value); return (T)this; }
        
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

      // used for unit testing, not needed by implementation
      @Override
      public boolean equals(Object obj) {
          if (obj == this) return true;
          if((obj == null) || (obj.getClass() != getClass())) {
              return false;
          }
          RepeatableAppointment testObj = (RepeatableAppointment) obj;

          boolean descriptionEquals = (getDescription() == null) ?
                  (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
          boolean locationEquals = (getLocation() == null) ?
                  (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
          boolean summaryEquals = (getSummary() == null) ?
                  (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
          boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                  (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
          boolean repeatEquals = (getRepeat() == null) ?
                  (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
          System.out.println("repeatable appointment " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals + " " + repeatEquals);
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
      
    public Calendar getStartTime() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setStartTime(Calendar c) {
        // TODO Auto-generated method stub
        
    }
    public Calendar getEndTime() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setEndTime(Calendar c) {
        // TODO Auto-generated method stub
        
    }
    public ZonedDateTime getStartZonedDateTime() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setStartZonedDateTime(ZonedDateTime v) {
        // TODO Auto-generated method stub
        
    }
    public ZonedDateTime getEndZonedDateTime() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setEndZonedDateTime(ZonedDateTime v) {
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
        
        public static Repeat newRepeat(Class<? extends Repeat> repeatClass)
        {
            try {
                return repeatClass.newInstance();
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | SecurityException e) {
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
        
        public static RepeatableAppointment newRepeatableAppointment(
                Class<? extends RepeatableAppointment> appointmentClass
              , Class<? extends Repeat> repeatClass)
        {
            try {
                RepeatableAppointment a = appointmentClass.newInstance();
                Repeat r = RepeatFactory.newRepeat(repeatClass);
                a.setRepeat(r);
                return a;
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        /** Builds an incomplete Appointment object with the Repeat field null - used as data for a repeat object */
        // TODO - SHOULD I MAKE A NEW CLASS WITHOUT REPEATS IN IT (APPT DATA)?
        public static RepeatableAppointment newRepeatableAppointment(Class<? extends RepeatableAppointment> appointmentClass)
        {
            try {
                return appointmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }

        // Returns deep copy of RepeatableAppointment
        public static RepeatableAppointment newRepeatableAppointment(RepeatableAppointment appointment)
        {
            Class<? extends RepeatableAppointment> appointmentClass = appointment.getClass();
            RepeatableAppointment a = null;
            try {
                a = appointmentClass.getConstructor(RepeatableAppointment.class).newInstance(appointment);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }

//          RepeatableAppointment a = newRepeatableAppointment(appointment.getClass());            
//            appointment.copyInto(a);
            if (appointment.getRepeat() != null)
            {
                Repeat r = RepeatFactory.newRepeat(appointment.getRepeat().getClass());
                appointment.getRepeat().copyInto(r);
                Appointment2 a2 = newAppointment(appointment.getRepeat().getAppointmentData());
                r.setAppointmentData(a2);
                a.setRepeat(r);
            }
            return a;
        }

        public static Appointment2 newAppointment(Class<? extends Appointment2> appointmentClass)
        {
            try {
                return appointmentClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
            return null;
        }
    
        // Returns deep copy of Appointment2
        public static Appointment2 newAppointment(Appointment2 appointment)
        {
//            Constructor<?>[] c = appointment.getClass().getConstructors();
//            System.out.println(c.length);
//            for (int i=0; i<c.length;i++)
//            {
//                System.out.println(c[i]);
//            }
//            System.exit(0);
            try {
                return appointment.getClass()
                        .getConstructor(Appointment2.class)
                        .newInstance(appointment);
            } catch (InstantiationException | IllegalAccessException
                    | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
          return appointment;

            
//            Appointment2 a = newAppointment(appointment.getClass());
//            appointment.copyInto(a);
//            return a;
        }
    }
        
}
