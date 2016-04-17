package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
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
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AppointmentEditLoader;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.NewAppointmentDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.SelectedOneAppointmentLoader;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VComponent.StartEndRange;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExDate;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities.ChangeDialogOption;
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
    public final static String ICALENDAR_STYLE_SHEET = ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
    
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
        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
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
     * APPOINTMENT AND VCOMPONENT LISTENERS 
     * Keeps appointments and vComponents synchronized.
     * listen for additions to appointments from agenda. This listener must be removed and added back when a change
     * in the time range  occurs.
     */
    private ListChangeListener<Appointment> appointmentsListChangeListener;
    public void setAppointmentsListChangeListener(ListChangeListener<Appointment> listener) { appointmentsListChangeListener = listener; }
    public ListChangeListener<Appointment> getAppointmentsListChangeListener() { return appointmentsListChangeListener; }
    
    private ListChangeListener<VComponent<Appointment>> vComponentsChangeListener; // listen for changes to vComponents.
    public void setVComponentsChangeListener(ListChangeListener<VComponent<Appointment>> listener) { vComponentsChangeListener = listener; }
    public ListChangeListener<VComponent<Appointment>> getVComponentsChangeListener() { return vComponentsChangeListener; }

    /*
     * Callback for determing scope of edit change - defaults to always answering ALL
     * Add For choice dialog, change to different callback
     */
    private Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> oneAllThisAndFutureDialogCallback = (m) -> ChangeDialogOption.ALL;
    public void setOneAllThisAndFutureDialogCallback(Callback<Map<ChangeDialogOption, StartEndRange>, ChangeDialogOption> callback) { oneAllThisAndFutureDialogCallback = callback; }
    
    // Default edit popup callback - this callback replaces Agenda's default edit popup
    // It has controls for repeatable events
    private Callback<Appointment, Void> iCalendarEditPopupCallback = (Appointment appointment) ->
    {
        VComponent<Appointment> vComponent = findVComponent(appointment); // Match appointment to VComponent
        appointments().removeListener(appointmentsListChangeListener); // remove listener to prevent making extra vEvents during edit
        Stage editPopup = new AppointmentEditLoader(
                  appointment
                , vComponent
                , this
                , appointmentGroupWriteCallback
                , repeatWriteCallback);

        editPopup.getScene().getStylesheets().addAll(getUserAgentStylesheet(), ICALENDAR_STYLE_SHEET);

        // remove listeners during edit (to prevent creating extra vEvents when making appointments)
        editPopup.setOnShowing((windowEvent) -> 
        {
            appointments().removeListener(appointmentsListChangeListener);
            vComponents().removeListener(vComponentsChangeListener);
        });
        
        /*
         * POSITION POPUP
         * Position popup to left or right of bodyPane, where there is room.
         * Note: assumes the control is displayed at its preferred height and width
         */
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        double prefHeightControl = ((Control) editPopup.getScene().getRoot()).getPrefHeight();
        double prefWidthControl = ((Control) editPopup.getScene().getRoot()).getPrefWidth();
        double xLeft = NodeUtil.screenX(bodyPane) - prefWidthControl - 5;
        double xRight = NodeUtil.screenX(bodyPane) + bodyPane.getWidth() + 5;
        double x = (xLeft > 0) ? xLeft : xRight;
        double y = NodeUtil.screenY(bodyPane) - prefHeightControl/2;
        editPopup.setX(x);
        editPopup.setY(y);
        editPopup.show();
        
        editPopup.setOnHiding((windowEvent) -> 
        {
            appointments().addListener(appointmentsListChangeListener);
            vComponents().addListener(vComponentsChangeListener);
        });
        return null;
    };
    public Callback<Appointment, Void> getICalendarEditPopupCallback() { return iCalendarEditPopupCallback; }

    // TODO - ORGANIZE ALL CALLBACKS IN ONE PLACE
    /** selectOneAppointmentCallback:
     * When one appointment is selected this callback is run.  It can be used to open a popup to provide edit,
     * delete or other edit options.
     */
    public Callback<Appointment, Void> getSelectedOneAppointmentCallback() { return selectedOneAppointmentCallback; }
    private Callback<Appointment, Void> selectedOneAppointmentCallback = (Appointment appointment) ->
    {
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        SelectedOneAppointmentLoader oneSelectedPopup = new SelectedOneAppointmentLoader(this, appointment);
        oneSelectedPopup.show(bodyPane, NodeUtil.screenX(bodyPane) + bodyPane.getWidth()/2, NodeUtil.screenY(bodyPane) + bodyPane.getHeight()/2);
        oneSelectedPopup.focusedProperty().addListener((obs) -> oneSelectedPopup.hide());
        return null;
    };
    public void setSelectedOneAppointmentCallback(Callback<Appointment, Void> c) { selectedOneAppointmentCallback = c; }

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
        VEvent<Appointment,?> vEvent = (VEvent<Appointment,?>) findVComponent(appointment);
        VEvent<Appointment,?> vEventOriginal = (VEvent<Appointment,?>) VComponentFactory.newVComponent(vEvent); // copy original vEvent.  If change is canceled its copied back.
        Temporal startOriginalInstance = appointmentStartOriginalMap.get(System.identityHashCode(appointment));
        final Temporal startInstance;
        final Temporal endInstance;
        boolean wasDateType = DateTimeType.of(startOriginalInstance).equals(DateTimeType.DATE);
        boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
        boolean isChangedToTimeBased = wasDateType && isNotDateType;
        boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
        if (isChangedToTimeBased)
        {
            startInstance = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
            endInstance = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
        } else if (isChangedToWholeDay)
        {
            startInstance = LocalDate.from(appointment.getStartTemporal());
            Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
            endInstance = (endInstanceTemp.equals(startInstance)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
        } else
        {
            startInstance = appointment.getStartTemporal();
            endInstance = appointment.getEndTemporal();            
        }

//        System.out.println("change instances:" + startOriginalInstance + " " + startInstance + " " + endInstance);
//        System.out.println("change localdatetime:" + appointment.getStartLocalDateTime() + " " + appointment.getEndLocalDateTime() + " " + appointment.isWholeDay());
        appointments().removeListener(appointmentsListChangeListener);
        vComponents().removeListener(vComponentsChangeListener);
        boolean changed = vEvent.handleEdit(
                vEventOriginal
              , vComponents
              , startOriginalInstance
              , startInstance
              , endInstance
              , appointments()
              , oneAllThisAndFutureDialogCallback);
        appointments().addListener(appointmentsListChangeListener);
        vComponents().addListener(vComponentsChangeListener);
        
//        System.out.println("vComponents changed - added:******************************" + vComponents.size());       
        
        if (! changed) refresh(); // refresh if canceled (undo drag effect, if edited a refresh occurred when updating Appointments)
//        System.out.println("map4:" + System.identityHashCode(appointment)+ " " +  appointment.getStartTemporal());
        appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal()); // update start map
        return null;
    };
    
    // CONSTRUCTOR
    public ICalendarAgenda()
    {
        super();
        vComponents().addListener((InvalidationListener) (obs) -> 
        {
//            System.out.println("vComponents chagned:******************************" + vComponents.size());
//            vComponents.stream().forEach(System.out::println);
        });

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
        ResourceBundle resources = ResourceBundle.getBundle("jfxtras.labs.icalendaragenda.ICalendarAgenda", myLocale);
        Settings.setup(resources);
        
        setAppointmentChangedCallback(appointmentChangedCallback);

        // Ensures VComponent are synched with appointments.
        // Are assigned here instead of when defined because it removes the vComponentsListener
        // which can't be done before its defined.
        appointmentsListChangeListener = (ListChangeListener.Change<? extends Appointment> change) ->
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
//                            System.out.println("OK:");
                            if ((a.getSummary() != null) && ! (a.getSummary().equals(originalSummary)) || ! (a.getAppointmentGroup().equals(originalAppointmentGroup)))
                            {
                                Platform.runLater(() -> refresh());
                            }
                            // fall through
                        case OTHER: // ADVANCED EDIT
//                            System.out.println("Advanced edit:");
                            VComponent<Appointment> newVComponent = VComponentFactory
                                    .newVComponent(getVEventClass(), a, appointmentGroups());
                            Temporal startRange = getDateTimeRange().getStartLocalDateTime();
                            Temporal endRange = getDateTimeRange().getEndLocalDateTime();
                            newVComponent.setStartRange(startRange);
                            newVComponent.setEndRange(endRange);
                            newVComponent.setUniqueIdentifier(getUidGeneratorCallback().call(null));
                            newVComponent.setDateTimeCreated(created);
                            vComponents().removeListener(vComponentsChangeListener);
                            vComponents().add(newVComponent);
                            System.out.println("new:" + newVComponent);
                            vComponents().addListener(vComponentsChangeListener);
                            // put data in maps
//                            System.out.println("dtstart:" + newVComponent.getDateTimeStart());
//                            System.out.println("map3:" + System.identityHashCode(a) + " " + a.getStartTemporal());
                            appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
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
                    change.getRemoved().stream().forEach(a -> 
                    { // add appointments to EXDATE
                        VComponent<Appointment> v = findVComponent(a);
                        if (v.getExDate() == null) v.setExDate(new ExDate());
//                        Temporal t = (a.isWholeDay()) ? LocalDate.from(a.getStartLocalDateTime()) : a.getStartLocalDateTime();
                        v.getExDate().getTemporals().add(a.getStartTemporal());
                        if (v.isRecurrenceSetEmpty()) vComponents().remove(v);
                    });
                }
            }
        };
        
        // fires when VComponents are added outside the edit popup, such as initialization
        vComponentsChangeListener = (ListChangeListener.Change<? extends VComponent<Appointment>> change) ->
        {
            System.out.println("vcomponents changed:" + vComponents.size());
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
//                    System.out.println("was added:" + dateTimeRange + " " +  getDateTimeRange());
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
//                                    System.out.println("add instances:");
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
                        appointments().removeListener(appointmentsListChangeListener);
                        appointments().addAll(newAppointments);
                        appointments().addListener(appointmentsListChangeListener);
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
        appointments().addListener(appointmentsListChangeListener);
        
        // Keeps appointmentRecurrenceIDMap and appointmentVComponentMap synched with appointments
        ListChangeListener<Appointment> appointmentsListener2 = (ListChangeListener.Change<? extends Appointment> change) ->
        {
//            System.out.println("appointmentsListener2:");
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList()
                            .stream()
                            .forEach(a -> 
                            {
                                appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
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
        vComponents().addListener(vComponentsChangeListener);

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
                    getSelectedOneAppointmentCallback().call(appointment);
                    VEvent<Appointment,?> vEvent = (VEvent<Appointment,?>) findVComponent(appointment);
                    System.out.println("selected vEvent:" + vEvent);
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
                appointments().removeListener(appointmentsListChangeListener); // remove appointmentListener to prevent making extra vEvents during refresh
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
//                        System.out.println("map5:" + System.identityHashCode(a) + " " + a.getStartTemporal());
                        appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal()); // populate recurrence-id map
                        appointmentVComponentMap.put(System.identityHashCode(a), v); // populate appointment-vComponent map
                    });
                });
                appointments().addListener(appointmentsListChangeListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
    } // end of constructor
    
    
    // TODO - SHOULD THESE LISTENERS AND BACKING MAPS GO TO NEW CLASS?
    public VComponent<Appointment> findVComponent(Appointment appointment)
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
}
