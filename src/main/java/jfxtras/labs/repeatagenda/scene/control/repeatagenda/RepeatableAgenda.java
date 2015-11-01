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

    // I/O callbacks, must be set to provide functionality, null by default
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
    
    static public interface RepeatableAppointment extends Agenda.Appointment
    {
        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Unique identifier as defined by iCalendar RFC 5545, 3.8.4.7 */
        void setUID(String uid);
        /** Unique identifier as defined by iCalendar RFC 5545, 3.8.4.7 */
        String getUID();

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Number of times appointment was edited.  Sequence as defined by iCalendar RFC 5545, 3.8.7.4 */
        void setSequence(int sequence);
        /** Number of times appointment was edited.  Sequence as defined by iCalendar RFC 5545, 3.8.7.4 */
        int getSequence();

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Last date/time object was revised.  Date-Time Stamp as defined by iCalendar RFC 5545, 3.8.7.2 */
        void setDTStamp(LocalDateTime dtStamp);
        /** Last date/time object was revised.  Date-Time Stamp as defined by iCalendar RFC 5545, 3.8.7.2 */
        LocalDateTime getDTStamp();

        // TODO - SHOULD GO TO REGULAR AGENDA
        /** Created date/time as defined by iCalendar RFC 5545, 3.8.7.1 */
        void setCreated(LocalDateTime created);
        /** Created date/time as defined by iCalendar RFC 5545, 3.8.7.1 */
        LocalDateTime getCreated();
        
        /** Date/time of repeatable appointment this appointment is replacing, if any (as defined by iCalendar RFC 5545, 3.8.4.4) */
        void setRecurrance(LocalDateTime t); // If not null, contains the start date and time of recurring appointment this appointment takes the place of
        /** Date/time of repeatable appointment this appointment is replacing, if any (as defined by iCalendar RFC 5545, 3.8.4.4) */
        LocalDateTime getRecurrance();

        /** True if appointment is part of a repeat rule AND has no unique data fields */
        boolean isRepeatMade();
        void setRepeatMade(boolean repeatMade);

        /** Repeat rule attached to appointment, if null appointment is individual */
        void setRepeat(Repeat repeat);
        Repeat getRepeat();

        
//        public default Calendar getStartTime() {
//            throw new RuntimeException("Not implemented");
//        }
        
//        default boolean repeatFieldsEquals(Object obj)
//        {
//            return equals(obj);
//        };
        

        // Copy methods - required to reverse edit changes when cancel is selected and for editing "this and future" repeatable appointments.
        /**
         * Copies all fields into parameter appointment.
         * This method must be overridden by an implementing class
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyInto(RepeatableAppointment appointment) {
            appointment.setEndLocalDateTime(getEndLocalDateTime());
            appointment.setStartLocalDateTime(getStartLocalDateTime());
            copyNonDateFieldsInto(appointment);
            return appointment;
        }
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * This method must be overridden by an implementing class
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
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
        default RepeatableAppointment copyNonDateFieldsInto(
                RepeatableAppointment appointment
              , RepeatableAppointment appointmentFromRepeatRule) {
            if (((RepeatableAppointmentImplBase<RepeatableAppointmentImpl>) appointment).getAppointmentGroup().equals(appointmentFromRepeatRule.getAppointmentGroup())) {
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
            getRepeat().copyInto(appointment.getRepeat());
            return appointment;
        }
    }

    static public class RepeatableAppointmentImplBase<T> {

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

        /** WholeDay: */
        public ObjectProperty<Boolean> wholeDayProperty() { return wholeDayObjectProperty; }
        final private ObjectProperty<Boolean> wholeDayObjectProperty = new SimpleObjectProperty<Boolean>(this, "wholeDay", false);
        public Boolean isWholeDay() { return wholeDayObjectProperty.getValue(); }
        public void setWholeDay(Boolean value) { wholeDayObjectProperty.setValue(value); }
        public T withWholeDay(Boolean value) { setWholeDay(value); return (T)this; } 
        
        /** Summary: */
        public ObjectProperty<String> summaryProperty() { return summaryObjectProperty; }
        final private ObjectProperty<String> summaryObjectProperty = new SimpleObjectProperty<String>(this, "summary");
        public String getSummary() { return summaryObjectProperty.getValue(); }
        public void setSummary(String value) { summaryObjectProperty.setValue(value); }
        public T withSummary(String value) { setSummary(value); return (T)this; } 
        
        /** Description: */
        public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
        final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
        public String getDescription() { return descriptionObjectProperty.getValue(); }
        public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
        public T withDescription(String value) { setDescription(value); return (T)this; } 
        
        /** Location: */
        public ObjectProperty<String> locationProperty() { return locationObjectProperty; }
        final private ObjectProperty<String> locationObjectProperty = new SimpleObjectProperty<String>(this, "location");
        public String getLocation() { return locationObjectProperty.getValue(); }
        public void setLocation(String value) { locationObjectProperty.setValue(value); }
        public T withLocation(String value) { setLocation(value); return (T)this; } 
        
        /** AppointmentGroup: */
        public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroupObjectProperty; }
        final private ObjectProperty<AppointmentGroup> appointmentGroupObjectProperty = new SimpleObjectProperty<AppointmentGroup>(this, "appointmentGroup");
        public AppointmentGroup getAppointmentGroup() { return appointmentGroupObjectProperty.getValue(); }
        public void setAppointmentGroup(AppointmentGroup value) { appointmentGroupObjectProperty.setValue(value); }
        public T withAppointmentGroup(AppointmentGroup value) { setAppointmentGroup(value); return (T)this; }

      // used for unit testing, not needed by implementation
      @Override
      public boolean equals(Object obj) {
          if (obj == this) return true;
          if((obj == null) || (obj.getClass() != getClass())) {
              return false;
          }
          Appointment testObj = (Appointment) obj;

          boolean descriptionEquals = (getDescription() == null) ?
                  (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
          boolean locationEquals = (getLocation() == null) ?
                  (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
          boolean summaryEquals = (getSummary() == null) ?
                  (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
          boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
                  (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
          System.out.println("repeat appointment " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
          return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
      }
 
    public void setUID(String uid) {
        // TODO Auto-generated method stub
        
    }
    public String getUID() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setSequence(int sequence) {
        // TODO Auto-generated method stub
        
    }
    public int getSequence() {
        // TODO Auto-generated method stub
        return 0;
    }
    public void setDTStamp(LocalDateTime dtStamp) {
        // TODO Auto-generated method stub
        
    }
    public LocalDateTime getDTStamp() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setCreated(LocalDateTime created) {
        // TODO Auto-generated method stub
        
    }
    public LocalDateTime getCreated() {
        // TODO Auto-generated method stub
        return null;
    }
    public void setRecurrance(LocalDateTime t) {
        // TODO Auto-generated method stub
        
    }
    public LocalDateTime getRecurrance() {
        // TODO Auto-generated method stub
        return null;
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
    static public class AppointmentGroupImpl 
    implements AppointmentGroup
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
                
        public static Repeat newRepeat(
                Class<? extends Repeat> repeatClass
              , LocalDateTimeRange dateTimeRange
              , Class<? extends RepeatableAppointment> appointmentClass)
        {
                try {
                    return repeatClass
                            .getConstructor(Class.class)
                            .newInstance(appointmentClass)
                            .withAppointmentClass(appointmentClass)
                            .withLocalDateTimeDisplayRange(dateTimeRange);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    e.printStackTrace();
                }
//            }
            return null;
        }
    }

    static public class AppointmentFactory {
        
        public static RepeatableAppointment newAppointment(Class<? extends RepeatableAppointment> appointmentClass)
        {
//            System.out.println("repeatClass " + appointmentClass);

//            if (appointmentClass != null)
//            {
                try {
                    return appointmentClass.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
//            }
            return null;
        }
    }
    
    
}
