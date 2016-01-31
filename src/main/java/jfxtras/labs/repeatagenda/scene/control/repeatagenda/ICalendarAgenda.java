package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.WeakHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.EditPopupLoader;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.LittlePopupLoader;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VComponent;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.VEvent;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.util.NodeUtil;
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
public class ICalendarAgenda extends Agenda
{
    private static String AGENDA_STYLE_CLASS = Agenda.class.getResource("/jfxtras/internal/scene/control/skin/agenda/" + Agenda.class.getSimpleName() + ".css").toExternalForm();
    
    // default appointment group list
    // if any element has been edited the edit list must be added to appointmentGroups
    final public static ObservableList<AppointmentGroup> DEFAULT_APPOINTMENT_GROUPS
        = javafx.collections.FXCollections.observableArrayList(
                IntStream
                .range(0, 24)
                .mapToObj(i -> new ICalendarAgenda.AppointmentGroupImpl()
                       .withStyleClass("group" + i)
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
    private ListChangeListener<Appointment> appointmentsListener2;
    private ListChangeListener<VComponent<Appointment>> vComponentsListener; // listen for changes to vComponents.
    
    // Default edit popup callback - this callback replaces Agenda's default edit popup
    private Callback<Appointment, Void> iCalendarEditPopupCallback = (Appointment appointment) ->
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
        
        // remove listeners during edit (to prevent making extra vEvents and appointments)
        editPopup.setOnShowing((windowEvent) -> 
        {
            appointments().removeListener(appointmentsListener);
            vComponents().removeListener(vComponentsListener);
        });
        editPopup.show();
        editPopup.setOnHiding((windowEvent) -> 
        {
            appointments().addListener(appointmentsListener);
            vComponents().addListener(vComponentsListener);
        });
        return null;
    };

    /*
     * Default little popup that opens when clicking on one appointment.
     * allows editing summary and buttons to open edit popup and delete
     */
    private Callback<Appointment, Void> littlePopupCallback = (Appointment appointment) ->
    {
        LittlePopupLoader popup = new LittlePopupLoader(appointment, appointments(), this);
        System.out.println("sizes " + NodeUtil.screenY(this) + " " + this.getHeight());
//      this.setX(NodeUtil.screenX(pane));
//      this.setY(NodeUtil.screenY(pane) - pane.getHeight());
        popup.show(this, NodeUtil.screenX(this) + this.getWidth()/2, NodeUtil.screenY(this) + this.getHeight()/2);
        System.out.println("add new appointment drawn popup");
        return null;
    };

    /*
     * Default simple edit popup that opens after new appointment is created.
     * allows editing summary and buttons to save and open regular edit popup
     */
    private Callback<Appointment, Void> simpleEditCallback = (Appointment appointment) ->
    {
        return null;
    };
    
    /* map stores start date/time of Appointments as they are made so I can get the original date/time
     * if Agenda changes one (e.g. drag-n-drop).  The original is needed for RECURRENCE-ID.  */
    private final Map<Appointment, Temporal> appointmentRecurrenceIDMap = new WeakHashMap<>();
    /* map matches appointment to VComponent that made it */
    private final Map<Appointment, VComponent<Appointment>> appointmentVComponentMap = new WeakHashMap<>();
    
    /*
     * Default appointment changed callback (actions like drag-n-drop)
     * allows dialog to prompt user for change to all, this-and-future or all for repeating VComponents
     */
    private Callback<Appointment, Void> appointmentChangedCallback = (Appointment appointment) ->
    {
        if (appointmentVComponentMap.get(appointment) == null)
        {
            Optional<VComponent<Appointment>> vo = vComponents.stream().filter(v -> v.instances().contains(appointment)).findAny();
            if (vo.isPresent()) appointmentVComponentMap.put(appointment, vo.get());
            else throw new RuntimeException("Can't edit appointment.  VComponent not found in map");
        }
        final VEvent<Appointment> vEvent = (VEvent<Appointment>) appointmentVComponentMap.get(appointment);
        System.out.println("Vevent changes:" + vEvent + " " + vComponents.size() + " " + appointmentVComponentMap.size());
        VEvent<Appointment> vEventOriginal = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent); // copy original vEvent.  If change is canceled its copied back.

        final Temporal startInstance;
        final Temporal endInstance;
        if (appointment.isWholeDay())
        {
            startInstance = LocalDate.from(appointment.getStartLocalDateTime()).atStartOfDay();
            endInstance = LocalDate.from(appointment.getEndLocalDateTime()).plus(1, ChronoUnit.DAYS).atStartOfDay();
        } else
        {
            startInstance = appointment.getStartLocalDateTime();
            endInstance = appointment.getEndLocalDateTime();
        }
        Temporal startOriginalInstance = appointmentRecurrenceIDMap.get(appointment);
  
        // apply changes to vEvent Note: only changes date and time.  If other types of changes become possible then add to the below list.
        // change start and end date/time
        final Temporal startNew;
        final long duration;
        if (appointment.isWholeDay())
        {
            long daysShift = ChronoUnit.DAYS.between(LocalDate.from(startOriginalInstance), startInstance);
            startNew = LocalDate.from(vEvent.getDateTimeStart()).plus(daysShift, ChronoUnit.DAYS);
            vEvent.setDateTimeStart(startNew);
            duration = ChronoUnit.DAYS.between(startInstance, endInstance) * VComponent.NANOS_IN_DAY;
            vEvent.setStartRange(LocalDate.from(vEvent.getStartRange()));
            vEvent.setEndRange(LocalDate.from(vEvent.getEndRange()));
        } else
        {
            long nanosShift = ChronoUnit.NANOS.between(startOriginalInstance, startInstance);
            startNew = VComponent.addNanos(vEvent.getDateTimeStart(), nanosShift);
            vEvent.setDateTimeStart(startNew);        
            duration = ChronoUnit.NANOS.between(startInstance, endInstance);
        }
        switch (vEvent.getEndPriority())
        {
        case DTEND:
            Temporal endNew = VComponent.addNanos(startNew, duration);
            vEvent.setDateTimeEnd(endNew);
            break;
        case DURATION:
            vEvent.setDurationInNanos(duration);
            break;
        }
        
        appointments().removeListener(appointmentsListener);
        vComponents().removeListener(vComponentsListener);
        ICalendarAgendaEditUtilities.saveChange(
                  vEvent
                , vEventOriginal
                , vComponents
                , startOriginalInstance
                , startInstance
                , endInstance
                , appointments());
        appointments().addListener(appointmentsListener);
        vComponents().addListener(vComponentsListener);
        
        if (vEvent.equals(vEventOriginal)) refresh(); // refresh if canceled
        
        return null;
    };
    
    // CONSTRUCTOR
    public ICalendarAgenda()
    {
        super();
        
        appointments().addListener((InvalidationListener) (obs) -> System.out.println("appointments chagned:"));
        
        // override Agenda appointmentGroups
        appointmentGroups().clear();
        appointmentGroups().addAll(DEFAULT_APPOINTMENT_GROUPS);

        // setup i18n resource bundle
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.repeatagenda.ICalendarAgenda", myLocale);
        Settings.setup(resources);
        
        setAppointmentChangedCallback(appointmentChangedCallback);

        // Ensures VComponent are synched with appointments.
        appointmentsListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList()
                            .stream()
                            .forEach(a -> 
                            { // make new VComponent(s)
                                VComponent<Appointment> newVComponent = VComponentFactory
                                        .newVComponent(getVEventClass(), a, appointmentGroups());
                                LocalDateTime startRange = getDateTimeRange().getStartLocalDateTime();
                                LocalDateTime endRange = getDateTimeRange().getEndLocalDateTime();
                                newVComponent.setStartRange(startRange);
                                newVComponent.setEndRange(endRange);
                                newVComponent.setUniqueIdentifier(getUidGeneratorCallback().call(null));
                                appointmentRecurrenceIDMap.put(a, a.getStartLocalDateTime()); // populate recurrence-id map
                                appointmentVComponentMap.put(a, newVComponent); // populate appointment-vComponent map
                                vComponents().removeListener(vComponentsListener);
                                vComponents().add(newVComponent);
                                vComponents().addListener(vComponentsListener);
                                System.out.println("newVComponent:" + newVComponent);
                            });
                    if (change.getAddedSubList().size() == 1)
                    { // Open little popup - edit, delete

                    }
                }
                if (change.wasRemoved())
                {
                    // TODO - add if deletions are implemented in Agenda
                }
            }
        };
        
        // fires when VComponents are added outside the edit popup, such as initialization
        vComponentsListener = (ListChangeListener.Change<? extends VComponent<Appointment>> change) ->
        {
            System.out.println("vcomponents changed:");
            while (change.next())
            {
                if (change.wasAdded() && (dateTimeRange != null)) // can't make appointment if range is not set
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

                                // add recurrence-id Temporal to parents (required to skip recurrences when making appointments)
                                if (v.getDateTimeRecurrence() != null)
                                {
                                    VComponent<Appointment> parent = vComponents().stream()
                                            .filter(v2 -> 
                                            {
                                                boolean isParent1 = v2.getUniqueIdentifier().equals(v.getUniqueIdentifier());
                                                boolean isParent2 = v2.getDateTimeRecurrence() == null;
                                                return isParent1 && isParent2;
                                            })
                                            .findFirst()
                                            .get();
                                    parent.getRRule().recurrences().add(v);
                                }
                            });
                    appointments().removeListener(appointmentsListener);
                    appointments().addAll(newAppointments);
                    appointments().addListener(appointmentsListener);
                } else if (change.wasRemoved())
                {
                    // remove associated appointments
                    change.getRemoved()
                        .stream()
                        .forEach(v -> 
                        {
                            List<Appointment> remove = appointments()
                                    .stream()
                                    .filter(a -> v.instances().stream().anyMatch(a2 -> a2 == a))
                                    .collect(Collectors.toList());
                            appointments().removeAll(remove);
                            
                            // move deleted recurrence-id into ExDates (ensure deleted instance stays deleted)
                            if (v.getDateTimeRecurrence() != null)
                            {
                                VComponent<Appointment> parent = vComponents().stream()
                                        .filter(v2 -> 
                                        {
                                            boolean isParent1 = v2.getUniqueIdentifier().equals(v.getUniqueIdentifier());
                                            boolean isParent2 = v2.getDateTimeRecurrence() == null;
                                            return isParent1 && isParent2;
                                        })
                                        .findFirst()
                                        .get();
                                parent.getExDate().getTemporals().add(v.getDateTimeRecurrence());
                            }
                        });
                }
            }
        };

        // Listen for changes to appointments (additions and deletions)
        appointments().addListener(appointmentsListener);
        
        // Keeps appointmentRecurrenceIDMap and appointmentVComponentMap synched with appointments
        ListChangeListener<Appointment> appointmentsListener2 = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList()
                            .stream()
                            .forEach(a -> 
                            { // make new VComponent(s)
                                appointmentRecurrenceIDMap.put(a, a.getStartLocalDateTime()); // populate recurrence-id map
//                                appointmentVComponentMap.put(a, newVComponent); // populate appointment-vComponent map
                                // TODO - IF I MOVE INSTANCE MAKING TO HERE - EITHER CALLBACK OR LISTENER THEN I CAN UPDATE
                                // BOTH MAPS HERE
                            });
                }
            }
        };
        appointments().addListener(appointmentsListener2);

        // Listen for changes to vComponents (additions and deletions)
        vComponents().addListener(vComponentsListener);
        
        // CHANGE DEFAULT EDIT POPUP - replace default popup with one with repeat options
        setEditAppointmentCallback(iCalendarEditPopupCallback);
        
        // LISTEN FOR AGENDA RANGE CHANGES
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
            System.out.println("Vcomponents:" + vComponents().size());
            vComponents().stream().forEach(System.out::println);
            this.dateTimeRange = dateTimeRange;
            if (dateTimeRange != null)
            {        
                appointments().removeListener(appointmentsListener); // remove appointmentListener to prevent making extra vEvents during refresh
                appointments().clear();
                vComponents().stream().forEach(v ->
                {
                    v.instances().clear(); // Remove instances and appointments
                    LocalDateTime start = getDateTimeRange().getStartLocalDateTime();
                    LocalDateTime end = getDateTimeRange().getEndLocalDateTime();
                    Collection<Appointment> newAppointments = v.makeInstances(start, end);
                    appointments().addAll(newAppointments);
                    newAppointments.stream().forEach(a -> appointmentRecurrenceIDMap.put(a, a.getStartLocalDateTime())); // populate recurrence-id map
                    newAppointments.stream().forEach(a -> appointmentVComponentMap.put(a, v)); // populate appointment-vComponent map
                });
                appointments().addListener(appointmentsListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
        
    }
    
//    /** Example Appointment class with overridden equals method used by unit testing */
//    static public class AppointmentImplLocal2 extends AppointmentImplLocal
//    {
//        @Override
//        public boolean equals(Object obj)
//        {
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
////            System.out.println("AppointmentImplLocal2 equals: " + descriptionEquals + " " + locationEquals 
////                    + " " + summaryEquals + " " +  " " + appointmentGroupEquals);
//            return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals ;
//        }
//    }
    
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
    
//    /** used for unit testing - equals method required */
//    static public class AppointmentImplLocal2 extends Agenda.AppointmentImplLocal
//    {
//        @Override
//        public boolean equals(Object obj) {
//            if (obj == this) return true;
//            if((obj == null) || (obj.getClass() != getClass())) {
//                return false;
//            }
//            AppointmentImplLocal2 testObj = (AppointmentImplLocal2) obj;
//
//            boolean startEquals = getStartLocalDateTime().equals(testObj.getStartLocalDateTime());
//            boolean endEquals = getEndLocalDateTime().equals(testObj.getEndLocalDateTime());
//            boolean descriptionEquals = (getDescription() == null) ?
//                    (testObj.getDescription() == null) : getDescription().equals(testObj.getDescription());
//            boolean locationEquals = (getLocation() == null) ?
//                    (testObj.getLocation() == null) : getLocation().equals(testObj.getLocation());
//            boolean summaryEquals = (getSummary() == null) ?
//                    (testObj.getSummary() == null) : getSummary().equals(testObj.getSummary());
//            boolean appointmentGroupEquals = (getAppointmentGroup() == null) ?
//                    (testObj.getAppointmentGroup() == null) : getAppointmentGroup().equals(testObj.getAppointmentGroup());
//            return descriptionEquals && locationEquals && summaryEquals && appointmentGroupEquals 
//                    && startEquals && endEquals;
//        }
//    }
    
    /**
     * Class implementing Appointment that includes an icon Pane field for easy rendering of
     * color boxes representing each AppointmentGroup
     */
    static public class AppointmentGroupImpl implements AppointmentGroup
    {
        /** Description: */
        public ObjectProperty<String> descriptionProperty() { return descriptionObjectProperty; }
        final private ObjectProperty<String> descriptionObjectProperty = new SimpleObjectProperty<String>(this, "description");
        @Override
        public String getDescription() { return descriptionObjectProperty.getValue(); }
        @Override
        public void setDescription(String value) { descriptionObjectProperty.setValue(value); }
        public AppointmentGroupImpl withDescription(String value) { setDescription(value); return this; } 
                
        /** StyleClass: */
        public ObjectProperty<String> styleClassProperty() { return styleClassObjectProperty; }
        final private ObjectProperty<String> styleClassObjectProperty = new SimpleObjectProperty<String>(this, "styleClass");
        @Override
        public String getStyleClass() { return styleClassObjectProperty.getValue(); }
        @Override
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
