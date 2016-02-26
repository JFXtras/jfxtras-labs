package jfxtras.labs.repeatagenda.scene.control.repeatagenda;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.AppointmentEditLoader;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.ICalendarUtilities;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.NewAppointmentDialog;
import jfxtras.labs.repeatagenda.internal.scene.control.skin.repeatagenda.base24hour.SelectOneLoader;
import jfxtras.labs.repeatagenda.scene.control.repeatagenda.icalendar.ExDate;
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
    public final static String iCalendarStyleSheet = ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
    
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

    /* 
     * Match up maps
     * 
     * map stores start date/time of Appointments as they are made so I can get the original date/time
     * if Agenda changes one (e.g. drag-n-drop).  The original is needed for RECURRENCE-ID.  */
    private final Map<Integer, Temporal> appointmentStartOriginalMap = new HashMap<>();
    private final Map<Integer, VComponent<Appointment>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VComponent that made it */
    
    // not here - in VEventImpl
//    // Extended appointment class used by the implementor - used to instantiate new appointment objects
//    private Class<? extends Appointment> appointmentClass = Agenda.AppointmentImpl.class; // set to default class, change if using own implementation
//    Class<? extends Appointment> getAppointmentClass() { return appointmentClass; }
//    public void setAppointmentClass(Class<? extends Appointment> clazz) { appointmentClass = clazz; }

    /** Callback for creating unique identifier values
     * @see VComponent#getUidGeneratorCallback() */
    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
    private static Integer nextKey = 0;
    private Callback<Void, String> uidGeneratorCallback = (Void) ->
    { // default UID generator callback
        String dateTime = VComponent.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String domain = "jfxtras.org";
        return dateTime + "-" + nextKey++ + domain;
    };
    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
    // I/O callbacks, must be set to provide I/O functionality, null by default - TODO - NOT IMPLEMENTED YET
    private Callback<Collection<VComponent< Appointment>>, Void> repeatWriteCallback = null;
    public void setRepeatWriteCallback(Callback<Collection<VComponent<Appointment>>, Void> repeatWriteCallback) { this.repeatWriteCallback = repeatWriteCallback; }

    private Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback = null; // TODO - NOT IMPLEMENTED YET
    public void setAppointmentGroupWriteCallback(Callback<Collection<AppointmentGroup>, Void> appointmentWriteCallback) { this.appointmentGroupWriteCallback = appointmentGroupWriteCallback; }

    /*
     * PRIVATE APPOINTMENT AND VCOMPONENT LISTENERS 
     * Keeps appointments and vComponents synchronized.
     * listen for additions to appointments from agenda. This listener must be removed and added back when a change
     * in the time range  occurs.
     */
    private ListChangeListener<Appointment> appointmentsListener;
    private ListChangeListener<VComponent<Appointment>> vComponentsListener; // listen for changes to vComponents.
    
    // Default edit popup callback - this callback replaces Agenda's default edit popup
    // It has controls for repeatable events
    private Callback<Appointment, Void> iCalendarEditPopupCallback = (Appointment appointment) ->
    {
        VComponent<Appointment> vComponent = findVComponent(appointment); // Match appointment to VComponent
        appointments().removeListener(appointmentsListener); // remove listener to prevent making extra vEvents during edit
        Stage editPopup = new AppointmentEditLoader(
                  appointment
                , vComponent
                , this
                , appointmentGroupWriteCallback
                , repeatWriteCallback // write repeat callback initialized to null
                , a -> { this.refresh(); return null; }); // refresh agenda

        editPopup.getScene().getStylesheets().addAll(getUserAgentStylesheet(), iCalendarStyleSheet);
        // remove listeners during edit (to prevent creating extra vEvents when making appointments)
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
    public Callback<Appointment, Void> getICalendarEditPopupCallback() { return iCalendarEditPopupCallback; }

    // TODO - ORGANIZE ALL CALLBACKS IN ONE PLACE
    /** selectOneAppointmentCallback:
     * When one appointment is selected this callback is run.  It can be used to open a popup to provide edit,
     * delete or other edit options.
     */
    public Callback<Appointment, Void> getOneAppointmentSelectedCallback() { return oneAppointmentSelectedCallback; }
    private SelectOneLoader oneSelectedPopup; // TODO - is this necessary?
    private Callback<Appointment, Void> oneAppointmentSelectedCallback = (Appointment appointment) ->
    {
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        oneSelectedPopup = new SelectOneLoader(this, appointment, appointments());
        oneSelectedPopup.show(bodyPane, NodeUtil.screenX(bodyPane) + bodyPane.getWidth()/2, NodeUtil.screenY(bodyPane) + bodyPane.getHeight()/2);
        return null;
    };
    public void setOneAppointmentSelectedCallback(Callback<Appointment, Void> c) { oneAppointmentSelectedCallback = c; }

    /*
     * Default simple edit popup that opens after new appointment is created.
     * For example, this is done by drawing an appointment in Agenda.
     * allows editing summary and buttons to save and open regular edit popup
     * 
     * to skip the callback, replace with a stub that always returns ButtonData.OK_DONE
     */
    private Callback<Appointment, ButtonData> newAppointmentDrawnCallback = (Appointment appointment) ->
    {
            // TODO - CAN I REMOVE RETURN ARGUMENT FROM CALLBACK - IT WOULD BE CONSISTENT WITH OTHERS
        Dialog<ButtonData> newAppointmentDialog = new NewAppointmentDialog(appointment, appointmentGroups(), iCalendarEditPopupCallback, Settings.resources);
        newAppointmentDialog.getDialogPane().getStylesheets().add(getUserAgentStylesheet());
        Optional<ButtonData> result = newAppointmentDialog.showAndWait();
        ButtonData button = result.isPresent() ? result.get() : ButtonData.CANCEL_CLOSE;
        return button;
    };
    public Callback<Appointment, ButtonData> getNewAppointmentDrawnCallback() { return newAppointmentDrawnCallback; }
    public void setNewAppointmentDrawnCallback(Callback<Appointment, ButtonData> c) { newAppointmentDrawnCallback = c; }
        
    /*
     * Default changed appointment callback (handles drag-n-drop and expend end time)
     * allows dialog to prompt user for change to all, this-and-future or all for repeating VComponents
     */
    private Callback<Appointment, Void> appointmentChangedCallback = (Appointment appointment) ->
    {
        // TODO - NEED ANOTHER VERSION OF THIS CODE FOR VTODO
        VEvent<Appointment> vEvent = (VEvent<Appointment>) findVComponent(appointment);
        VEvent<Appointment> vEventOriginal = (VEvent<Appointment>) VComponentFactory.newVComponent(vEvent); // copy original vEvent.  If change is canceled its copied back.
        final Temporal startInstance;
        final Temporal endInstance;
        Temporal startOriginalInstance = appointmentStartOriginalMap.get(System.identityHashCode(appointment));
        if (appointment.isWholeDay())
        {
            startInstance = LocalDate.from(appointment.getStartLocalDateTime()).atStartOfDay();
            endInstance = LocalDate.from(appointment.getEndLocalDateTime()).plus(1, ChronoUnit.DAYS).atStartOfDay();
        } else
        {
            switch (vEvent.getDateTimeType())
            {
            case DATE:
                throw new RuntimeException("VEvent should be wholeday, but isn't");
            case DATE_WITH_LOCAL_TIME:
                startInstance = appointment.getStartLocalDateTime();
                endInstance = appointment.getEndLocalDateTime();
                break;
            case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
            case DATE_WITH_UTC_TIME:
                System.out.println("appointment class:" + appointment.getStartTemporal().getClass());
                startInstance = appointment.getStartTemporal();
                endInstance = appointment.getEndTemporal();
                ZoneId zone = ((ZonedDateTime) startInstance).getZone();
//                startInstance = appointment.getStartZonedDateTime();
//                endInstance = appointment.getEndZonedDateTime();
//                ZoneId zone = appointment.getStartZonedDateTime().getZone();
                startOriginalInstance = LocalDateTime.from(startOriginalInstance).atZone(zone); // TODO I don't like this line.  I want the Temporals in appointmentStartOriginalMap to be the correct type.
                break;
            default:
                throw new RuntimeException("Unsupported Temporal type:" + vEvent.getDateTimeType());
            }
        }
  
        // apply changes to vEvent Note: only changes date and time.  If other types of changes become possible then add to the below list.
        // change start and end date/time
        
        // set start date-time
        final Temporal startNew;
        
        if (appointment.isWholeDay())
        {
            Period dayShift = Period.between(LocalDate.from(startOriginalInstance), LocalDate.from(startInstance));
            startNew = vEvent.getDateTimeStart().plus(dayShift);
            vEvent.setDateTimeStart(startNew);

            // Convert range to LocalDate if necessary
            if (! (vEvent.getStartRange() instanceof LocalDate))
            {
                vEvent.setStartRange(LocalDate.from(vEvent.getStartRange()));
                vEvent.setEndRange(LocalDate.from(vEvent.getEndRange()));
            }
        } else
        {
            Duration changeShift = Duration.between(startOriginalInstance, startInstance);
            startNew = vEvent.getDateTimeStart().plus(changeShift);
            vEvent.setDateTimeStart(startNew);
        }
        
        // set end date-time or duration
        final TemporalAmount duration;
        switch (vEvent.endPriority())
        {
        case DTEND:
            Temporal endNew;
            if (appointment.isWholeDay())
            {
                duration = Period.between(LocalDate.from(startInstance), LocalDate.from(endInstance));
            } else
            {
                duration = Duration.between(startInstance, endInstance);
            }
            endNew = startNew.plus(duration);                
            vEvent.setDateTimeEnd(endNew);
            break;
        case DURATION:
                duration = Duration.between(startInstance, endInstance);
                vEvent.setDuration(duration);
            break;
        }
        appointments().removeListener(appointmentsListener);
        vComponents().removeListener(vComponentsListener);
        vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , appointments()
              , ICalendarUtilities.EDIT_DIALOG_CALLBACK);
        appointments().addListener(appointmentsListener);
        vComponents().addListener(vComponentsListener);
        
        if (vEvent.equals(vEventOriginal)) refresh(); // refresh if canceled
        appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartLocalDateTime()); // update start map
        return null;
    };
    
    // CONSTRUCTOR
    public ICalendarAgenda()
    {
        super();
//        appointments().addListener((InvalidationListener) (obs) -> System.out.println("appointments chagned:"));

        // setup event listener to delete selected appointments when Agenda is added to a scene
        sceneProperty().addListener((obs, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                getScene().setOnKeyPressed((event) ->
                {
                    if (event.getCode().equals(KeyCode.DELETE) && (! selectedAppointments().isEmpty()))
                    {
                        VComponent<Appointment> v = appointmentVComponentMap.get(System.identityHashCode(selectedAppointments().get(0)));
                        appointments().removeAll(selectedAppointments());
                    }
                });
            }
        });
        
        // setup i18n resource bundle
        Locale myLocale = Locale.getDefault();
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.repeatagenda.ICalendarAgenda", myLocale);
        Settings.setup(resources);
        
        setAppointmentChangedCallback(appointmentChangedCallback);

        // Ensures VComponent are synched with appointments.
        // Are assigned here instead of when defined because it removes the vComponentsListener
        // which can't be done before its defined.
        appointmentsListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    if (change.getAddedSubList().size() == 1)
                    { // Open little popup - edit, delete
                        ZonedDateTime created = ZonedDateTime.now(ZoneId.of("Z"));
                        Appointment a = change.getAddedSubList().get(0);
                        String originalSummary = a.getSummary();
                        AppointmentGroup originalAppointmentGroup = a.getAppointmentGroup();
                        ButtonData button = newAppointmentDrawnCallback.call(change.getAddedSubList().get(0)); // runs NewAppointmentDialog by default
                        switch (button)
                        {
                        case CANCEL_CLOSE:
                            appointments().remove(a);
                            refresh();
                            break;
                        case OK_DONE: // CREATE EVENT assumes newAppointmentCallback can only edit summary and appointmentGroup
                            System.out.println("OK:");
                            if (! (a.getSummary().equals(originalSummary)) || ! (a.getAppointmentGroup().equals(originalAppointmentGroup)))
                            {
                                Platform.runLater(() -> refresh());
                            }
                            // fall through
                        case OTHER: // ADVANCED EDIT
                            System.out.println("Advanced edit:");
                            VComponent<Appointment> newVComponent = VComponentFactory
                                    .newVComponent(getVEventClass(), a, appointmentGroups());
                            Temporal startRange = getDateTimeRange().getStartLocalDateTime();
                            Temporal endRange = getDateTimeRange().getEndLocalDateTime();
                            newVComponent.setStartRange(startRange);
                            newVComponent.setEndRange(endRange);
                            newVComponent.setUniqueIdentifier(getUidGeneratorCallback().call(null));
                            newVComponent.setDateTimeCreated(created);
                            vComponents().removeListener(vComponentsListener);
                            vComponents().add(newVComponent);
                            System.out.println("new:" + newVComponent);
                            vComponents().addListener(vComponentsListener);
                            // put data in maps
                            appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartLocalDateTime());
                            appointmentVComponentMap.put(System.identityHashCode(a), newVComponent); // populate appointment-vComponent map
                            break;
                        default:
                            throw new RuntimeException("unknown button type:" + button);
                        }
                        if (button == ButtonData.OTHER) // edit appointment
                        {
                            iCalendarEditPopupCallback.call(a);
                        }
                    } else throw new RuntimeException("Adding multiple appointments at once not supported");
                }
                if (change.wasRemoved())
                {
                    if ((oneSelectedPopup != null) && (oneSelectedPopup.isShowing())) oneSelectedPopup.hide();
                    change.getRemoved().stream().forEach(a -> 
                    { // add appointments to EXDATE
                        VComponent<Appointment> v = findVComponent(a);
                        if (v.getExDate() == null) v.setExDate(new ExDate());
                        Temporal t = (a.isWholeDay()) ? LocalDate.from(a.getStartLocalDateTime()) : a.getStartLocalDateTime();
                        v.getExDate().getTemporals().add(t);
                        if (v.isRecurrenceSetEmpty()) vComponents().remove(v);
                    });
                }
            }
        };
        
        // fires when VComponents are added outside the edit popup, such as initialization
        vComponentsListener = (ListChangeListener.Change<? extends VComponent<Appointment>> change) ->
        {
            System.out.println("vcomponents changed:");
            while (change.next())
            {
                if (change.wasAdded()) // can't make appointment if range is not set
                {
                    // Check if all VComponets are valid, throw exception otherwise
                    change.getAddedSubList()
                            .stream()
                            .forEach(v -> 
                            {
                                if (! v.isValid()) { throw new IllegalArgumentException("Invalid VComponent:" + v.errorString()); }                        
                            });
                    System.out.println("was added:" + dateTimeRange + " " +  getDateTimeRange());
                    if (dateTimeRange != null)
                    {
                        Temporal start = getDateTimeRange().getStartLocalDateTime();
                        Temporal end = getDateTimeRange().getEndLocalDateTime();
                        List<Appointment> newAppointments = new ArrayList<>();
                        // add new appointments
                        change.getAddedSubList()
                                .stream()
                                .forEach(v -> 
                                {
                                    System.out.println("add instances:");
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
                    }
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
                            {
//                                VComponent<Appointment> v = findVComponent(a); // FIX THIS - CAN'T FIND WHEN DRAG-N-DROP
//                                switch (v.getTemporalType())
//                                {
//                                case DATE:
//                                case DATE_WITH_LOCAL_TIME:
//                                    appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartLocalDateTime());
//                                    break;
//                                case DATE_WITH_LOCAL_TIME_AND_TIME_ZONE:
//                                case DATE_WITH_UTC_TIME:
//                                    appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartZonedDateTime());
//                                    break;
//                                default:
//                                    throw new RuntimeException("Unknown TemporalType:" + v.getTemporalType());
//                                }
                                
                                appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartLocalDateTime());
//                                appointmentVComponentMap.put(a, newVComponent); // populate appointment-vComponent map
                                // TODO - IF I MOVE INSTANCE MAKING TO HERE - EITHER CALLBACK OR LISTENER THEN I CAN UPDATE
                                // BOTH MAPS HERE
                            });
                } else if (change.wasRemoved())
                {
                    change.getRemoved()
                            .stream()
                            .forEach(a -> 
                            { // remove map entries
                                appointmentStartOriginalMap.remove(System.identityHashCode(a));
                                appointmentVComponentMap.remove(System.identityHashCode(a));
                            });
                }
            }
        };
        appointments().addListener(appointmentsListener2);

        // Listen for changes to vComponents (additions and deletions)
        vComponents().addListener(vComponentsListener);

        /*
         * Open select-one appointment popup
         * listen for changes to selectedAppointments, if only one run callback.
         */
        ListChangeListener<Appointment> selectedAppointmentListener = (ListChangeListener.Change<? extends Appointment> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded() && (selectedAppointments().size() == 1))
                {
                    Appointment appointment = selectedAppointments().get(0);
                    getOneAppointmentSelectedCallback().call(appointment);
                }
            }
        };
        selectedAppointments().addListener(selectedAppointmentListener);
        
        // CHANGE DEFAULT EDIT POPUP - replace default popup with one with repeat options
        setEditAppointmentCallback(iCalendarEditPopupCallback);
        
        // LISTEN FOR AGENDA RANGE CHANGES
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
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
                    newAppointments.stream().forEach(a ->
                    {
                        appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartLocalDateTime()); // populate recurrence-id map
                        appointmentVComponentMap.put(System.identityHashCode(a), v); // populate appointment-vComponent map
                    });
                });
                appointments().addListener(appointmentsListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
    } // end of constructor
    
    
    // TODO - SHOULD THESE LISTENERS AND BACKING MAPS GO TO NEW CLASS?
    private VComponent<Appointment> findVComponent(Appointment appointment)
    {
        if (appointmentVComponentMap.get(System.identityHashCode(appointment)) == null)
        { // find appointment by searching all VComponents.  Then add it to map if not present.  This can happen if multiple edits occur between refreshes.
            Optional<VComponent<Appointment>> v2 = vComponents.stream()
                    .filter(v -> v.instances().stream()
                            .map(a -> System.identityHashCode(a))
                            .filter(h -> h.equals(System.identityHashCode(appointment)))
                            .findAny()
                            .isPresent())
                    .findAny();
            if (v2.isPresent()) appointmentVComponentMap.put(System.identityHashCode(appointment), v2.get());
            else throw new RuntimeException("Can't find matching VComponent for appointment");
        }
        return appointmentVComponentMap.get(System.identityHashCode(appointment));
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
    
//    /**
//     * Appointment factory methods
//     * 
//     * @author David Bal
//     *
//     */
//    static public class AppointmentFactory {
//
//        /** returns new Appointment */
//        public static <T extends Appointment> T newAppointment(Class<T> appointmentClass)
//        {
//            try {
//                return appointmentClass.newInstance();
//            } catch (InstantiationException | IllegalAccessException e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    
//        /** Returns deep copy of source appointment */
//        @SuppressWarnings("unchecked")
//        public static <T extends Appointment> T newAppointment(T source)
//        {
//            try {
//                return (T) source.getClass()
//                        .getConstructor(Appointment.class) // gets copy constructor
//                        .newInstance(source);               // calls copy constructor
//            } catch (InstantiationException | IllegalAccessException
//                    | IllegalArgumentException | InvocationTargetException
//                    | NoSuchMethodException | SecurityException e) {
//                e.printStackTrace();
//            }
//          return null;
//
//        }
//    }

    /** Add ResourceBundle for FXML controllers that contains strings for the appointment popups */
    public void setResourceBundle(ResourceBundle resources) {
        Settings.setup(resources);
    }
        
}
