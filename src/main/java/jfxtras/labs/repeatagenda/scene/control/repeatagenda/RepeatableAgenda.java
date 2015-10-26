package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
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
//    public static ObservableList<AppointmentGroup> defaultAppointmentGroups() {
//        return javafx.collections.FXCollections.observableArrayList(
//                IntStream
//                .range(0, 24)
//                .mapToObj(i -> new RepeatableAgenda.AppointmentGroupImpl()
//                       .withStyleClass("group" + i)
//                       .withKey(i)
//                       .withDescription("group" + (i < 10 ? "0" : "") + i))
//                .collect(Collectors.toList()));
//    }
//    


    /**
     * Constructor with individualAppointments collection and repeats collection provided.
     * These objects will be automatically be kept current with Agenda's data.
     * 
     * @param individualAppointments
     * @param repeats
     */
    public RepeatableAgenda(Collection<T> individualAppointments, Collection<Repeat> repeats)
    {
        setIndividualAppointments(individualAppointments);
        setRepeats(repeats);
    }
    
    public RepeatableAgenda()
    {
        // Listen for changes to appointments (additions and deletions)
        appointments().addListener((ListChangeListener.Change<? extends Appointment> change)
            -> {
                // TODO - UPDATE individualAppointments WHEN NECESSARY - may not be needed
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
                            getIndividualAppointments().removeAll(removedIndividualAppointments);
                    }
                    if (change.wasAdded())
                    {
                        List<? extends Appointment> addedAppointments = change.getAddedSubList();
                        Set<T> newIndividualAppointments = addedAppointments.stream()
                            .map(a -> ((T) a))
                            .filter(a -> ! a.isRepeatMade())
                            .peek(a -> System.out.println("added individual " + a.getStartLocalDateTime()))
                            .collect(Collectors.toSet());
                        getIndividualAppointments().addAll(newIndividualAppointments);
                    }
                    System.out.println("appointment list changed");
                }
            });
        
        // Change edit popup to provide one with repeat options
        setEditAppointmentCallback((Appointment appointment) -> {
            RepeatMenu repeatMenu = new RepeatMenu(
                    (RepeatableAppointment) appointment
                    , dateTimeRange()
                    , appointments()
                    , getRepeats()
                    , appointmentGroups()
                    , getNewAppointmentCallback()
                    , appointmentWriteCallback   // write appointment callback initialized to null
                    , repeatWriteCallback);      // write repeat callback initialized to null
            repeatMenu.show();
            return null;
        });
    }

    private LocalDateTimeRange dateTimeRange; // range of current skin
    public LocalDateTimeRange dateTimeRange() { return dateTimeRange; }
    
    // I/O callbacks, must be set to provide functionality, null by default
    private Callback<Collection<Appointment>, Void> appointmentWriteCallback = null;
    public void setAppointmentWriteCallback(Callback<Collection<Appointment>, Void> appointmentWriteCallback) { this.appointmentWriteCallback = appointmentWriteCallback; }
    private Callback<Collection<Repeat>, Void> repeatWriteCallback = null;
    public void setRepeatWriteCallback(Callback<Collection<Repeat>, Void> repeatWriteCallback) { this.repeatWriteCallback = repeatWriteCallback; }
    
    // TODO - UPDATE WITH REPEATS
    /** Repeat rules */
    private Collection<Repeat> repeats;
    public Collection<Repeat> getRepeats() { return repeats; }
    public void setRepeats(Collection<Repeat> repeatRules)
    {
        this.repeats = repeatRules;
        if (getIndividualAppointments() != null)
        {
            getRepeats().stream().forEach(a -> a.collectAppointments(getIndividualAppointments())); // add individual appointments that have repeat rules to their Repeat objects
        }
        
        // manage repeat-made appointments when the range changes
        setLocalDateTimeRangeCallback(dateTimeRange -> {
            this.dateTimeRange = dateTimeRange;
            LocalDateTime startDate = dateTimeRange.getStartLocalDateTime();
            LocalDateTime endDate = dateTimeRange.getEndLocalDateTime();
            System.out.println("dates changed " + startDate + " " + endDate);
            System.out.println("2agenda.appointments().size() " + appointments().size());
            appointments().removeIf(a -> ((RepeatableAppointment) a).isRepeatMade());
            getRepeats().stream().forEach(r -> r.getAppointments().clear());
            getRepeats().stream().forEach(r ->
            { // Make new repeat-made appointments inside range
                Collection<RepeatableAppointment> newAppointments = r.makeAppointments(startDate, endDate);
                appointments().addAll(newAppointments);
//                agenda.appointments().addAll(newAppointments);
                System.out.println("newAppointments " + newAppointments.size());
//                r.removeOutsideRangeAppointments(data.getAppointments());                 // remove outside range appointments
            });
            System.out.println("3agenda.appointments().size() " + appointments().size());
//            System.exit(0);
            return null; // return argument for the Callback
        });

    }

    // TODO - UPDATE WITH APPOINTMENTS
    /** Individual appointments - kept updated with appointments*/
    private Collection<T> individualAppointments = FXCollections.observableArrayList();
    public Collection<T> getIndividualAppointments() { return individualAppointments; }
    public void setIndividualAppointments(Collection<T> list)
    {
        individualAppointments = list;
        if (getRepeats() != null)
        {
            getRepeats().stream().forEach(a -> a.collectAppointments(getIndividualAppointments())); // add individual appointments that have repeat rules to their Repeat objects
        }
    }
    
//    // IO Callbacks
//    private Callback<Collection<RepeatableAppointment>, Void> writeAppointmentsCallback;
//    /** Write individual appointment collection callback */
//    public void setWriteAppointmentsCallback(Callback<Collection<RepeatableAppointment>, Void> callBack) { writeAppointmentsCallback = callBack; }
//    private Callback<Collection<Repeat>, Void> writeRepeatsCallback;
//    /** Write repeat collection callback */
//    public void setWriteRepeatsCallback(Callback<Collection<Repeat>, Void> callBack) { writeRepeatsCallback = callBack; }

//    private ObservableList<RepeatableAppointment> repeatMadeAppointments = FXCollections.observableArrayList();
//    /** Appointment list - is kept updated with Agenda.appointments */
//    private ObservableList<Appointment> appointmentItems = FXCollections.observableArrayList();
//    public void setAppointmentItems(ObservableList<Appointment> items)
//    { 
//        this.appointmentItems = items;
//        items.addListener(listener);
//    }
//    public ObservableList<T> getAppointmentItems() { return appointmentItems; }
//    private final ListChangeListener<RepeatableAppointment> listener = (ListChangeListener<RepeatableAppointment>) change -> {
//        System.out.println("item change");
//        while (change.next())
//        {
//            for (Appointment myAppointment : change.getAddedSubList())
//            {
//                appointments().add(myAppointment);
//            }
//            for (Appointment myAppointment : change.getRemoved())
//            {
//                appointments().remove(myAppointment);
//            }
//        }
//    };
//    
//    public RepeatableAgenda(ObservableList<T> items) {
//        this.appointmentItems = items;
//        items.addListener(listener);
//    }
//
//    public RepeatableAgenda() {
//        appointmentItems.addListener(listener);
//    }
    
    static public interface RepeatableAppointment extends Agenda.Appointment
    {

        boolean isRepeatMade();
        void setRepeatMade(boolean b);

        void setRepeat(Repeat repeat);
        Repeat getRepeat();
////        boolean hasRepeat();
        boolean repeatFieldsEquals(Object obj); // TODO - CAN THIS BE MADE DEFAULT OR REMOVED?
        
        // TODO - FOR EDITED REPEATABLE APPOINTMENTS, THIS MARKS WHICH RECURRANCE THIS APPOINTMENT TAKES THE PLACE OF
        public void setInPlaceOfRecurrance(LocalDateTime t); // If not null, contains the start date and time of recurring appointment this appointment takes the place of

        /**
         * Copies all fields into parameter appointment
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyInto(RepeatableAppointment appointment) {
            appointment.setEndLocalDateTime(getEndLocalDateTime());
            appointment.setStartLocalDateTime(getStartLocalDateTime());
            copyNonDateFieldsInto(appointment);
//            Iterator<DayOfWeek> dayOfWeekIterator = Arrays 
//                    .stream(DayOfWeek.values())
//                    .limit(7)
//                    .iterator();
//                while (dayOfWeekIterator.hasNext())
//                {
//                    DayOfWeek key = dayOfWeekIterator.next();
//                    boolean b1 = this.getRepeat().getDayOfWeekMap().get(key).get();
//                    boolean b2 = appointment.getRepeat().getDayOfWeekMap().get(key).get();
//                    System.out.println("copied day of week2 " + key + " " + b1 + " " + b2);
//                }
            return appointment;
        }
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment) {
            appointment.setAppointmentGroup(getAppointmentGroup());
            appointment.setDescription(getDescription());
            appointment.setSummary(getSummary());
//            boolean b1 = getRepeat() == null;
//            boolean b2 = appointment.getRepeat() == null;
//            System.out.println("repeats " + b1 + " " + b2);
//            if (getRepeat() == null) return appointment;
//            if (appointment.getRepeat() == null)
//            {
//                appointment.setRepeat(RepeatFactory.newRepeat(getRepeat()));
//            } else
//            {
//                getRepeat().copyInto(appointment.getRepeat());
//            }
            return appointment;
        }
        
        /**
         * Copies this Appointment non-time fields into parameter appointment
         * Used when some of fields are unique and should not be copied.
         * 
         * @param appointment
         * @return
         */
        default RepeatableAppointment copyNonDateFieldsInto(RepeatableAppointment appointment, RepeatableAppointment appointmentOld) {
            if (appointment.getAppointmentGroup().equals(appointmentOld.getAppointmentGroup())) {
                appointment.setAppointmentGroup(getAppointmentGroup());
            }
            if (appointment.getDescription().equals(appointmentOld.getDescription())) {
                appointment.setDescription(getDescription());
            }
            if (appointment.getSummary().equals(appointmentOld.getSummary())) {
                appointment.setSummary(getSummary());
            }
            getRepeat().copyInto(appointment.getRepeat());
//            repeatMap.get(this).copyInto(repeatMap.get(appointment));
            return appointment;
        }
    }

    static public abstract class RepeatableAppointmentImplBase<T> {

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
        public void setInPlaceOfRecurrance(LocalDateTime t) { inPlaceOfRecurrance = t; } // If not null, contains the start date and time of recurring appointment this appointment takes the place of

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

      // used for Assert methods in testing
        // TODO - THINK ABOUT ALTERNATIVE TESTING THAT WON'T REQUIRE THIS METHOD
      @Override
      public boolean equals(Object obj) {
          if (obj == this) return true;
          if((obj == null) || (obj.getClass() != getClass())) {
              return false;
          }
          Appointment testObj = (Appointment) obj;

          boolean descriptionEquals = (getDescription() == null)
                  ? (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
          boolean locationEquals = (getLocation() == null)
                  ? (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
          boolean summaryEquals = (getSummary() == null)
                  ? (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
//          boolean repeatEquals = (getRepeat() == null)
//                  ? (testObj.getRepeat() == null) : getRepeat().equals(testObj.getRepeat());
          boolean appointmentGroupEquals = (getAppointmentGroup() == null)
                  ? (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
           System.out.println("repeat appointment " + descriptionEquals + " " + locationEquals + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
          return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals;
      }
      
      /** Checks if fields relevant for the repeat rule (non-time fields) are equal. */
      // needs to be overridden by any class implementing Appointment or extending AppointmentImplBase
      // Note: Location field is a problem - I think it should be removed.
      public boolean repeatFieldsEquals(Object obj) {
          return equals(obj);
      }
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

}
