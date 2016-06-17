package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.NewAppointmentDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.OneSelectedAppointmentPopup;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.components.EditComponentPopupScene;
import jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors.Behavior;
import jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors.VEventBehavior;
import jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors.VJournalBehavior;
import jfxtras.labs.icalendaragenda.scene.control.agenda.behaviors.VTodoBehavior;
import jfxtras.labs.icalendaragenda.scene.control.agenda.stores.DefaultVComponentAppointmentStore;
import jfxtras.labs.icalendaragenda.scene.control.agenda.stores.VComponentStore;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
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
    
//    private ObjectProperty<LocalDateTime> startRange = new SimpleObjectProperty<>(); // must be updated when range changes
//    private ObjectProperty<LocalDateTime> endRange = new SimpleObjectProperty<>();
    private LocalDateTimeRange dateTimeRange; // date range of current skin, set when localDateTimeRangeCallback fires

    public void setDateTimeRange(LocalDateTimeRange dateTimeRange)
    {
        this.dateTimeRange = dateTimeRange;
        getVComponentStore().setStartRange(dateTimeRange.getStartLocalDateTime());
        getVComponentStore().setEndRange(dateTimeRange.getEndLocalDateTime());
//        getRecurrenceHelper().setStartRange(dateTimeRange.getStartLocalDateTime());
//        getRecurrenceHelper().setEndRange(dateTimeRange.getEndLocalDateTime());
    }
    public LocalDateTimeRange getDateTimeRange() { return dateTimeRange; }
    
//    // Recurrence helper - handles making appointments, edit and delete components
//    @Deprecated
//    final private RecurrenceHelper<Appointment> recurrenceHelper;
//    @Deprecated
//    public RecurrenceHelper<Appointment> getRecurrenceHelper() { return recurrenceHelper; }
    
    public VComponentStore<Appointment> getVComponentStore() { return vComponentStore; }
    private VComponentStore<Appointment> vComponentStore; // default VComponent store - for Appointments, if other implementation used make new store
    public void setVComponentStore(VComponentStore<Appointment> vComponentStore) { this.vComponentStore = vComponentStore; }

    /** The VCalendar object that contains all scheduling information */
    public VCalendar getVCalendar() { return vCalendar; }
    final private VCalendar vCalendar;
    
    private ObservableList<String> categories; // initialized in constructor
    public ObservableList<String> getCategories() { return categories; }
    public void setCategories(ObservableList<String> categories)
    {
        this.categories = categories;
    }

//    // Component Behaviors
//    VEventBehavior vEventBehavior;
//    VTodoBehavior vTodoBehavior;
//    VJournalBehavior vJournalBehavior;
    
//    /** Callback to make appointment from VComponent and Temporal */
//    private final CallbackTwoParameters<VComponentRepeatable<?>, Temporal, Appointment> makeAppointmentCallback = (vComponentEdited, startTemporal) ->
//    {
//        Boolean isWholeDay = vComponentEdited.getDateTimeStart().getValue() instanceof LocalDate;
//        VComponentLocatable<?> vComponentLocatable = (VComponentLocatable<?>) vComponentEdited;
//        final TemporalAmount adjustment = vComponentLocatable.getActualDuration();
//        Temporal endTemporal = startTemporal.plus(adjustment);
//
//        /* Find AppointmentGroup
//         * control can only handle one category.  Checks only first category
//         */
//        final AppointmentGroup appointmentGroup;
//        if (vComponentLocatable.getCategories() != null)
//        {
//            String firstCategory = vComponentLocatable.getCategories().get(0).getValue().get(0);
//            Optional<AppointmentGroup> myGroup = appointmentGroups()
//                    .stream()
//                    .filter(g -> g.getDescription().equals(firstCategory))
//                    .findAny();
//            appointmentGroup = (myGroup.isPresent()) ? myGroup.get() : null;
//        } else
//        {
//            appointmentGroup = null;
//        }
//        // Make appointment
//        Appointment appt = new Agenda.AppointmentImplTemporal()
//                .withStartTemporal(startTemporal)
//                .withEndTemporal(endTemporal)
//                .withDescription( (vComponentLocatable.getDescription() != null) ? vComponentLocatable.getDescription().getValue() : null )
//                .withSummary( (vComponentLocatable.getSummary() != null) ? vComponentLocatable.getSummary().getValue() : null)
//                .withLocation( (vComponentLocatable.getLocation() != null) ? vComponentLocatable.getLocation().getValue() : null)
//                .withWholeDay(isWholeDay)
//                .withAppointmentGroup(appointmentGroup);
//        return appt;
//    };
    
//    public void setVCalendar(VCalendar vCalendar) { this.vCalendar = vCalendar; }
    
//    /** VComponents are iCalendar compliant calendar components.
//     * They make appointments for Agenda to render. */
//    @Deprecated
//    public ObservableList<VComponentNew<?>> vComponents() { return vComponents; }
//    @Deprecated
//    private ObservableList<VComponentNew<?>> vComponents = FXCollections.observableArrayList();
//    
//    /** VEvent class - used in factory to instantiate new VEvent objects */
//    Class<? extends VComponentNew<?>> getVEventClass() { return vEventClass; }
//    private Class<? extends VComponentNew<?>> vEventClass = VEventImpl.class; // default class, change if other implementation is used
//    public void setVEventClass(Class<? extends VComponentNew<?>> clazz) { vEventClass = clazz; }

    /* 
     * Match up maps
     * 
     * map stores start date/time of Appointments as they are made so I can get the original date/time
     * if Agenda changes one (e.g. drag-n-drop).  The original is needed for RECURRENCE-ID.  */
    private final Map<Integer, Temporal> appointmentStartOriginalMap = new HashMap<>();
    public Map<Integer, Temporal> appointmentStartOriginalMap() { return appointmentStartOriginalMap; } // TODO - POPULATE

    private final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VEvent that made it */
    public Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap() { return appointmentVComponentMap; }
//    private final Map<Integer, VEvent> appointmentVEventMap = new HashMap<>(); /* map matches appointment to VEvent that made it */
//    private final Map<Integer, VTodo> appointmentVTodoMap = new HashMap<>(); /* map matches appointment to VTodo that made it */
//    private final Map<Integer, VJournal> appointmentVJournalMap = new HashMap<>(); /* map matches appointment to VJournal that made it */

    private final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>(); /* map matches VComponent to their appointments */
//    private final Map<VComponentNew<?>, List<Appointment>> vComponentAppointmentMap = new WeakHashMap<>(); /* map matches VComponent to their appointments */

    private final Map<Class<? extends VComponent>, Behavior<?>> vComponentClassBehaviorMap = new HashMap<>();
    
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
    private Callback<Collection<VComponentDisplayable<?>>, Void> repeatWriteCallback = null;
    public void setRepeatWriteCallback(Callback<Collection<VComponentDisplayable<?>>, Void> repeatWriteCallback) { this.repeatWriteCallback = repeatWriteCallback; }

    private Callback<Collection<AppointmentGroup>, Void> appointmentGroupWriteCallback = null; // TODO - NOT IMPLEMENTED YET
    public void setAppointmentGroupWriteCallback(Callback<Collection<AppointmentGroup>, Void> appointmentWriteCallback) { this.appointmentGroupWriteCallback = appointmentGroupWriteCallback; }

    /*
     * APPOINTMENT AND VCOMPONENT LISTENERS 
     * Keeps appointments and vComponents synchronized.
     * listen for additions to appointments from agenda. This listener must be removed and added back when a change
     * in the time range  occurs.
     */
    public ListChangeListener<Appointment> appointmentsListChangeListener;
    public void setAppointmentsListChangeListener(ListChangeListener<Appointment> listener) { appointmentsListChangeListener = listener; }
    public ListChangeListener<Appointment> getAppointmentsListChangeListener() { return appointmentsListChangeListener; }
    
    private ListChangeListener<VComponentDisplayable<?>> vComponentsChangeListener; // listen for changes to vComponents.
    public void setVComponentsChangeListener(ListChangeListener<VComponentDisplayable<?>> listener) { vComponentsChangeListener = listener; }
    public ListChangeListener<VComponentDisplayable<?>> getVComponentsChangeListener() { return vComponentsChangeListener; }

    /*
     * Callback for determing scope of edit change - defaults to always answering ALL
     * Add For choice dialog, change to different callback
     */
    private Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> oneAllThisAndFutureDialogCallback = (m) -> ChangeDialogOption.ALL;
    public void setOneAllThisAndFutureDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> callback) { oneAllThisAndFutureDialogCallback = callback; }
    
    // Default edit popup callback - this callback replaces Agenda's default edit popup
    // It has controls for repeatable events
    private Callback<Appointment, Void> iCalendarEditPopupCallback = (Appointment appointment) ->
    {
        VComponentDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
        } else
        {
            // make popup
            Stage popupStage = new Stage();
            EditComponentPopupScene popupScene = vComponentClassBehaviorMap.get(vComponent.getClass()).getEditScene(appointment);
            popupStage.setScene(popupScene);
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setResizable(false);
            
//            EditComponentPopupStage<?> editPopup = editStage(appointment);

            popupStage.getScene().getStylesheets().addAll(getUserAgentStylesheet(), ICalendarAgenda.ICALENDAR_STYLE_SHEET);

            // remove listeners during edit (to prevent creating extra vEvents when making appointments)
//            popupStage.setOnShowing((windowEvent) -> appointments().removeListener(appointmentsListChangeListener));
            
            /* POSITION POPUP
             * Position popup to left or right of bodyPane, where there is room.
             * Note: assumes the control is displayed at its preferred height and width */
            Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
            double prefHeightControl = ((Control) popupStage.getScene().getRoot()).getPrefHeight();
            double prefWidthControl = ((Control) popupStage.getScene().getRoot()).getPrefWidth();
            double xLeft = NodeUtil.screenX(bodyPane) - prefWidthControl - 5;
            double xRight = NodeUtil.screenX(bodyPane) + bodyPane.getWidth() + 5;
            double x = (xLeft > 0) ? xLeft : xRight;
            double y = NodeUtil.screenY(bodyPane) - prefHeightControl/2;
            popupStage.setX(x);
            popupStage.setY(y);
            popupStage.show();
            
            popupScene.getEditDisplayableTabPane().isFinished().addListener((obs) -> popupStage.hide());
            // return listener after edit
//            popupStage.setOnHiding((windowEvent) ->  appointments().addListener(appointmentsListChangeListener));
//            vComponentClassBehaviorMap.get(vComponent.getClass()).iCalendarEditBehavior(appointment);
        }
        return null;
    };
    public Callback<Appointment, Void> getICalendarEditPopupCallback() { return iCalendarEditPopupCallback; }

    /** selectOneAppointmentCallback:
     * When one appointment is selected this callback is run.  It can be used to open a popup to provide edit,
     * delete or other edit options.
     */
    public Callback<Appointment, Void> getSelectedOneAppointmentCallback() { return selectedOneAppointmentCallback; }
    private Callback<Appointment, Void> selectedOneAppointmentCallback = (Appointment appointment) ->
    {
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
//        SelectedOneAppointmentLoader oneSelectedPopup = new SelectedOneAppointmentLoader(this, appointment);
        OneSelectedAppointmentPopup oneSelectedPopup = new OneSelectedAppointmentPopup();
        oneSelectedPopup.setupData(this, appointment);
        oneSelectedPopup.isFinished().addListener((obs) -> oneSelectedPopup.hide());
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
        Dialog<ButtonData> newAppointmentDialog = new NewAppointmentDialog(appointment, appointmentGroups(), Settings.resources);
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
//        appointments().removeListener(appointmentsListChangeListener);
//        getVCalendar().getVEvents().removeListener(vComponentsChangeListener);
        
        VComponentDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
        
        System.out.println("about to revise:" + vComponent + " " + appointmentVComponentMap.size());
        vComponentClassBehaviorMap.get(vComponent.getClass()).callRevisor(appointment);

        //  TODO - handle VTODO, VJOURNAL
//        System.out.println("about to add back listeners:");
//        appointments().addListener(appointmentsListChangeListener);
//            getVCalendar().getVEvents().addListener(vComponentsChangeListener);
            
//            System.out.println("vComponents changed - added:******************************" + vComponents.size());       
            
//            if (! changed) refresh(); // refresh if canceled (undo drag effect, if edited a refresh occurred when updating Appointments)
//            System.out.println("map4:" + System.identityHashCode(appointment)+ " " +  appointment.getStartTemporal());
        appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal()); // update start map
            
//        } catch (InstantiationException | IllegalAccessException e)
//        {
//            e.printStackTrace();
//        }
        return null;
    };
    
    // CONSTRUCTOR
    public ICalendarAgenda(VCalendar vCalendar)
    {
        super();
        this.vCalendar = vCalendar;
        vComponentStore = new DefaultVComponentAppointmentStore(appointmentGroups()); // default VComponent store - for Appointments, if other implementation used make new store

//        recurrenceHelper = new RecurrenceHelper<Appointment>(makeAppointmentCallback);
//        vEventBehavior = new VEventBehavior(this);
        
        // Populate component class to behavior map with required behaviors
        vComponentClassBehaviorMap.put(VEvent.class, new VEventBehavior(this));
        vComponentClassBehaviorMap.put(VJournal.class, new VJournalBehavior(this));
        vComponentClassBehaviorMap.put(VTodo.class, new VTodoBehavior(this));

//        getVCalendar().getVEvents().addListener((InvalidationListener) (obs) -> 
//        {
////            System.out.println("vComponents chagned:******************************" + vComponents.size());
////            vComponents.stream().forEach(System.out::println);
//        });
        
        // setup default categories from appointment groups
        categories = FXCollections.observableArrayList(
                appointmentGroups().stream()
                    .map(a -> a.getDescription())
                    .collect(Collectors.toList())
                    );
        
        // update appointmentGroup descriptions with changed categories
        getCategories().addListener((ListChangeListener.Change<? extends String> change) ->
        {
            while (change.next())
            {
                if (change.wasAdded())
                {
                    change.getAddedSubList().forEach(c ->
                    {
                        int index = change.getList().indexOf(c);
                        appointmentGroups().get(index).setDescription(c);
                        System.out.println("updated apointmentgroup: " + index + " " + c);
                    });
                }
            }
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
//                        VComponent<?> v = appointmentVComponentMap.get(System.identityHashCode(selectedAppointments().get(0)));
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
                        Appointment appointment = change.getAddedSubList().get(0);
                        String originalSummary = appointment.getSummary();
                        AppointmentGroup originalAppointmentGroup = appointment.getAppointmentGroup();
                        ButtonData button = newAppointmentDrawnCallback.call(change.getAddedSubList().get(0)); // runs NewAppointmentDialog by default
                        switch (button)
                        {
                        case CANCEL_CLOSE:
//                            appointments().remove(appointment);
                            refresh();
                            break;
                        case OK_DONE:
                        {
                            VComponent newVComponent = getVComponentStore().createVComponent(appointment);
                            getVCalendar().addVComponent(newVComponent);
//                            System.out.println("vevents2:"+getVCalendar().getVEvents().size());
//                            System.out.println("vevent:"+newVComponent.toContent());
                            if ((appointment.getSummary() != null) && ! (appointment.getSummary().equals(originalSummary)) || ! (appointment.getAppointmentGroup().equals(originalAppointmentGroup)))
                            {
                                Platform.runLater(() -> refresh());
                            }
                            break;
                        }
                        case OTHER: // ADVANCED EDIT
                        {
//                            System.out.println("vevents1:"+getVCalendar().getVEvents().size());
                            VComponent newVComponent = getVComponentStore().createVComponent(appointment);
                            getVCalendar().addVComponent(newVComponent);
//                            System.out.println("vevents2:"+getVCalendar().getVEvents().size());
//                            System.out.println("vevent:"+newVComponent.toContent());
                            iCalendarEditPopupCallback.call(vComponentAppointmentMap.get(System.identityHashCode(newVComponent)).get(0));
//                            System.out.println("vevents3:"+getVCalendar().getVEvents().size());
                            break;
                        }
                        default:
                            throw new RuntimeException("unknown button type:" + button);
                        }
                        // remove drawn appointment - if not canceled, it was replaced with one made by the new VComponent
                        appointments().remove(appointment);

//                        if (button == ButtonData.OTHER) // edit appointment
//                        {
//                            iCalendarEditPopupCallback.call(appointment);
//                        }
                    } else throw new RuntimeException("Adding multiple appointments at once is not supported (" + change.getAddedSubList().size() + ")");
                }
                if (change.wasRemoved())
                {
                    change.getRemoved().stream().forEach(a -> 
                    { // add appointments to EXDATE
                        VComponentDisplayable<?> v = appointmentVComponentMap().get(a);
//                        VComponentDisplayable<?> v = findVComponent(a);
//                        if (v.getExceptionDates() == null)
//                        {
//                            v.withExceptionDates(a.getStartTemporal());
//                        }
////                        Temporal t = (a.isWholeDay()) ? LocalDate.from(a.getStartLocalDateTime()) : a.getStartLocalDateTime();
////                        v.getExDate().getTemporals().add(a.getStartTemporal());
//                        if (v.isRecurrenceSetEmpty())
//                        {
//                            getVCalendar().getVEvents().remove(v);
//                        }
                    });
                }
            }
        };

        // fires when VComponents are added or removed
        // TODO - make generic for VEVENT, VTODO, VJOURNAL
        vComponentsChangeListener = (ListChangeListener.Change<? extends VComponentDisplayable<?>> change) ->
        {
//            System.out.println("vcomponents changed:" + getVCalendar().getVEvents().size());
            while (change.next())
            {
                if (change.wasAdded()) // can't make appointment if range is not set
                {
                    // Check if all VComponets are valid, throw exception otherwise
                    change.getAddedSubList()
                            .stream()
                            .forEach(v -> 
                            {
                                if (! v.isValid())
                                {
                                    throw new RuntimeException("Invalid VComponent:" + System.lineSeparator() + 
                                            v.errors().stream().collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator() +
                                            v.toContent());
                                }
                            });
//                    System.out.println("was added:" + dateTimeRange + " " +  getDateTimeRange());
                    if (dateTimeRange != null)
                    {
//                        Temporal start = getDateTimeRange().getStartLocalDateTime();
//                        Temporal end = getDateTimeRange().getEndLocalDateTime();
                        List<Appointment> newAppointments = new ArrayList<>();
                        // add new appointments
                        change.getAddedSubList()
                                .stream()
                                .forEach(v -> newAppointments.addAll(makeAppointments(v)));
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
                            List<Appointment> remove = vComponentAppointmentMap.get(System.identityHashCode(v));
                            appointments().removeAll(remove);
//                            throw new RuntimeException("here4:");
//                            List<Appointment> remove = appointments()
//                                    .stream()
//                                    .filter(a -> v.instances().stream().anyMatch(a2 -> a2 == a))
//                                    .collect(Collectors.toList());
                            
//                            // move deleted recurrence-id into ExDates (ensure deleted instance stays deleted)
//                            if (v.getDateTimeRecurrence() != null)
//                            {
//                                VComponentNew<?> parent = vComponents().stream()
//                                        .filter(v2 -> 
//                                        {
//                                            boolean isParent1 = v2.getUniqueIdentifier().equals(v.getUniqueIdentifier());
//                                            boolean isParent2 = v2.getDateTimeRecurrence() == null;
//                                            return isParent1 && isParent2;
//                                        })
//                                        .findFirst()
//                                        .get();
//                                parent.getExDate().getTemporals().add(v.getDateTimeRecurrence());
//                            }
                        });
                }
            }
        };

        // Listen for changes to appointments (additions and deletions)
        appointments().addListener(appointmentsListChangeListener);
        
//        // Keeps appointmentRecurrenceIDMap and appointmentVComponentMap synched with appointments
//        ListChangeListener<Appointment> appointmentsListener2 = (ListChangeListener.Change<? extends Appointment> change) ->
//        {
////            System.out.println("appointmentsListener2:");
//            while (change.next())
//            {
//                if (change.wasAdded())
//                {
//                    change.getAddedSubList()
//                            .stream()
//                            .forEach(a -> 
//                            {
//                                appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
////                                appointmentVComponentMap.put(a, newVComponent); // populate appointment-vComponent map
//                                // TODO - IF I MOVE INSTANCE MAKING TO HERE - EITHER CALLBACK OR LISTENER THEN I CAN UPDATE
//                                // BOTH MAPS HERE
//                            });
//                } else if (change.wasRemoved())
//                {
//                    change.getRemoved()
//                            .stream()
//                            .forEach(a -> 
//                            { // remove map entries
//                                appointmentStartOriginalMap.remove(System.identityHashCode(a));
//                                appointmentVComponentMap.remove(System.identityHashCode(a));
//                            });
//                }
//            }
//        };
//        appointments().addListener(appointmentsListener2);

        // Listen for changes to vComponents (additions and deletions)
        getVCalendar().getVEvents().addListener(vComponentsChangeListener);

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
//                    VEventOld<Appointment,?> vEvent = (VEventOld<Appointment,?>) findVComponent(appointment);
//                    System.out.println("selected vEvent:" + vEvent);
                }
            }
        };
        selectedAppointments().addListener(selectedAppointmentListener);
        
        // CHANGE DEFAULT EDIT POPUP - replace default popup with one with repeat options
        setEditAppointmentCallback(iCalendarEditPopupCallback);

        // LISTEN FOR AGENDA RANGE CHANGES
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
            List<Appointment> newAppointments = new ArrayList<>();
            setDateTimeRange(dateTimeRange);
            getVComponentStore().setStartRange(dateTimeRange.getStartLocalDateTime());
            getVComponentStore().setEndRange(dateTimeRange.getEndLocalDateTime());
//            System.out.println("range0:" + dateTimeRange);
            if (dateTimeRange != null)
            {
                appointments().removeListener(appointmentsListChangeListener); // remove appointmentListener to prevent making extra vEvents during refresh
                appointments().clear();
                vComponentAppointmentMap.clear();
                appointmentStartOriginalMap.clear();
                getVCalendar().getVEvents().stream().forEach(v ->
                {
//                    v.instances().clear(); // Remove instances and appointments
//                    LocalDateTime start = getDateTimeRange().getStartLocalDateTime();
//                    LocalDateTime end = getDateTimeRange().getEndLocalDateTime();
//                    List<Appointment> newAppointments = ICalendarAgendaUtilities.makeAppointments(v, start, end, appointmentGroups());
//                    List<Appointment> newAppointments = makeRecurrences(v);
//                    vComponentAppointmentMap.put(v, newAppointments);

//                    newAppointments.addAll(getVComponentStore().makeRecurrences(v));

                    newAppointments.addAll(makeAppointments(v));
                    
//                    vComponentAppointmentMap.entrySet().stream().forEach(System.out::println);
                    
//                    newAppointments.stream().forEach(a ->
//                    {
////                        System.out.println("map5:" + System.identityHashCode(a) + " " + a.getStartTemporal());
////                        appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal()); // populate recurrence-id map
//                        appointmentVComponentMap.put(System.identityHashCode(a), v); // populate appointment-vComponent map
//                    });
                });
                appointments().addAll(newAppointments);
                appointments().addListener(appointmentsListChangeListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
    } // end of constructor
    
    private Collection<Appointment> makeAppointments(VComponentDisplayable<?> v)
    {
        List<Appointment> myAppointments = getVComponentStore().makeRecurrences(v);
        myAppointments.forEach(a -> 
        {
            appointmentVComponentMap.put(System.identityHashCode(a), v);
            appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
        });
        vComponentAppointmentMap.put(System.identityHashCode(v), myAppointments);
        return myAppointments;
    }
    
    
    // TODO - SHOULD THESE LISTENERS AND BACKING MAPS GO TO NEW CLASS?
    @Deprecated
    public VComponentDisplayable<?> findVComponent(Appointment appointment)
    {
//        System.out.println("map:" + appointmentVComponentMap.get(System.identityHashCode(appointment)));
//        System.exit(0);
        VComponentDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
        if (vComponent == null)
        { // find appointment by searching all VComponents.  Then add it to map if not present.  This can happen if multiple edits occur between refreshes.
//            Stream<VComponentDisplayable<?>> displayableComponents = Stream.concat(Stream.concat(vCalendar.getVEvents().stream(), vCalendar.getVTodos().stream()), vCalendar.getVJournals().stream());
//            Optional<VComponentDisplayable<?>> v2 = displayableComponents
//                    .filter(v -> v.instances().stream()
//                            .map(a -> System.identityHashCode(a))
//                            .filter(h -> h.equals(System.identityHashCode(appointment)))
//                            .findAny()
//                            .isPresent())
//                    .findAny();
//            if (v2.isPresent()) appointmentVComponentMap.put(System.identityHashCode(appointment), v2.get());
            System.out.println("map:");
            throw new RuntimeException("Can't find matching VComponent for appointment");
        }
        return vComponent;
    }
    
//    /**
//     * A convenience class to represent start and end date-time pairs
//     * 
//     */
//    // TODO - FIND A BETTER PLACE FOR THIS CLASS
//   static public class StartEndRange
//   {
//       public StartEndRange(Temporal start, Temporal end)
//       {
//           if ((start != null) && (end != null) && (start.getClass() != end.getClass())) { throw new RuntimeException("Temporal classes of start and end must be the same."); }
//           this.start = start;
//           this.end = end;
//       }
//       
//       public Temporal getDateTimeStart() { return start; }
//       private final Temporal start;
//       
//       public Temporal getDateTimeEnd() { return end; }
//       private final Temporal end; 
//       
//       @Override
//       public String toString() { return super.toString() + " " + start + " to " + end; }
//   }
    
     
}
