package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.LocalDate;
import java.time.ZoneId;
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
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import jfxtras.internal.scene.control.skin.agenda.AgendaSkin;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.AgendaDateTimeUtilities;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.DeleteChoiceDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.EditChoiceDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.NewAppointmentDialog;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.OneAppointmentSelectedAlert;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.Settings;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors.EditDisplayableScene;
import jfxtras.labs.icalendaragenda.internal.scene.control.skin.agenda.base24hour.editors.SimpleEditSceneFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultRecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.DefaultVComponentFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.RecurrenceFactory;
import jfxtras.labs.icalendaragenda.scene.control.agenda.factories.VComponentFactory;
import jfxtras.labs.icalendarfx.VCalendar;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.components.deleters.SimpleDeleterFactory;
import jfxtras.labs.icalendarfx.components.revisors.ChangeDialogOption;
import jfxtras.labs.icalendarfx.components.revisors.SimpleRevisorFactory;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Categories;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Description;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Location;
import jfxtras.labs.icalendarfx.properties.component.descriptive.Summary;
import jfxtras.labs.icalendarfx.properties.component.recurrence.ExceptionDates;
import jfxtras.labs.icalendarfx.properties.component.recurrence.RecurrenceRule;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Count;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Frequency;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Interval;
import jfxtras.labs.icalendarfx.properties.component.recurrence.rrule.Until;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeEnd;
import jfxtras.labs.icalendarfx.properties.component.time.DateTimeStart;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities.DateTimeType;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.util.NodeUtil;

/**
 * <p>The {@link ICalendarAgenda} control is designed to take a {@link VCalendar VCALENDAR} object,
 * which is based on the iCalendar RFC 5545 standard, and renders it in {@link Agenda}, which is a calendar
 * display control. {@link ICalendarAgenda} renders only the {@link VComponentDisplayable displayable}
 * iCalendar components which are {@link VEvent VEVENT}, {@link VTodo VTODO}, and {@link VJournal VJOURNAL}.
 * Other calendar components are ignored.</p>
 * 
 * <p>The {@link ICalendarAgenda} control has a number of features, including:
 * <ul>
 * <li>Powerful {@link EditDisplayableScene edit control} to modify calendar components:
 *   <ul>
 *   <li>Edits DATE or DATE-TIME properties including:
 *     <ul>
 *     <li>{@link DateTimeStart DTSTART} - when the calendar component begins.
 *     <li>{@link DateTimeEnd DTEND} - when the calendar component ends.
 *     </ul>
 *   <li>Can toggle between DATE or DATE-TIME values
 *   <li>Edits descriptive properties including:
 *     <ul>
 *     <li>{@link Summary SUMMARY}
 *     <li>{@link Description DESCRIPTION}
 *     <li>{@link Location LOCATION}
 *     <li>{@link Categories CATEGORIES} - from a color-coded selectable grid (only one category supported)
 *     </ul>
 *   <li>Edits {@link RecurrenceRule RRULE}, recurrence rule, elements including:
 *     <ul>
 *     <li>{@link Frequency FREQUENCY} - type of recurrence, including Daily, Weekly, Monthly and Yearly 
 *     <li>{@link Interval INTERVAL} - represents the intervals the recurrence rule repeats
 *     <li>{@link Count COUNT} - the number of occurrences.
 *     <li>{@link Until UNTIL} - the DATE or DATE-TIME value that bounds the recurrence rule in an inclusive manner
 *     <li>{@link ExceptionDates EXDATE} - list of DATE-TIME values that are skipped
 *     </ul>
 *   <li>Displays a easy-to-read description of the {@link RecurrenceRule RRULE}, recurrence rule
 *   </ul>
 * <li>Automatically synchronizes graphical changes with the {@link VCalendar VCALENDAR} object.
 * <li>Uses an abstract {@link RecurrenceFactory} to create {@link Appointment} objects that are rendered
 *  by {@link Agenda}
 *   <ul>
 *   <li>A default factory is included that creates the default {@link AppointmentImplTemporal} objects
 *   <li>A custom factory can be added to create custom {@link Appointment} objects.
 *   </ul>
 * <li>Uses an abstract {@link VComponentFactory} to create {@link VComponentDisplayable} objects when new events
 *  are drawn by clicking and drag-and-drop actions.
 *   <ul>
 *   <li>A default factory is included that creates {@link VEvent VEVENT} and {@link VTodo VTODO} components
 *    from the default {@link AppointmentImplTemporal} object.
 *   <li>A custom factory can be added to create iCalendar components from custom {@link Appointment} objects.
 *   </ul>
 * </ul>
 * </p>
 * 
 * <p>If not using the default {@link AppointmentImplTemporal} implementation, but a different {@link Appointment}
 * implementation, then use the following setter methods to configure the required factories and callback:
 * <ul>
 * <li>{@link #setRecurrenceFactory(RecurrenceFactory)}
 * <li>{@link #setVComponentFactory(VComponentFactory)} 
 * <li>{@link #setNewAppointmentCallback(Callback)} 
 * </ul>
 * </p>
 * 
 * <h2>Creating a ICalendarAgenda</h2>
 * 
 * <p>Firstly, a {@link VCalendar VCALENDAR} instance needs to be defined.  For example:
 * <pre> {@code VCalendar vCalendar = new VCalendar();}</pre>
 * Optionally, the {@link VCalendar VCALENDAR} instance can be set with calendar components.  This can be done
 * by reading a .ics file or building the calendar components programmatically through the API.  Please see the
 * iCalendarFX documentation for more details.  An empty {@link VCalendar VCALENDAR} is also acceptable.</p>
 * 
 * <p>Next, the {@link VCalendar VCALENDAR} instance must be provided in the {@link ICalendarAgenda} constructor
 * as shown below:.
 * <pre> {@code ICalendarAgenda iCalendarAgenda = new ICalendarAgenda(vCalendar);}</pre>
 * Nothing else special is required to instantiate {@link ICalendarAgenda} if you use the default factories.</p>
 * 
 * <p> A simple example to display a {@link ICalendarAgenda} with an example {@link VEvent VEVENT} is below:
 * 
 * <pre>
 * {@code
 * public class ICalendarAgendaSimpleTrial extends Application
 * {        
 *    public static void main(String[] args) {
 *       launch(args);       
 * }
 *
 *   public void start(Stage primaryStage) {
 *       VCalendar vCalendar = new VCalendar();
 *       VEvent vEvent = new VEvent()
 *               .withDateTimeStart(LocalDateTime.now().minusMonths(1))
 *               .withDateTimeEnd(LocalDateTime.now().minusMonths(1).plusHours(1))
 *               .withSummary("Example Daily Event")
 *               .withRecurrenceRule("RRULE:FREQ=DAILY")
 *               .withUniqueIdentifier("exampleuid000jfxtras.org");
 *       vCalendar.addVComponent(vEvent);
 *       ICalendarAgenda agenda = new ICalendarAgenda(vCalendar);
 *       
 *       BorderPane root = new BorderPane();
 *       root.setCenter(agenda);
 *       Scene scene = new Scene(root, 1366, 768);
 *       primaryStage.setScene(scene);
 *       primaryStage.setTitle("ICalendar Agenda Simple Demo");
 *       primaryStage.show();
 *   }
 * }}</pre>
 * </p>
 * 
 * @see <a target="_blank" href="https://tools.ietf.org/html/rfc5545#section-3.8.2.2">iCalendar 5545 Specification</a>
 * @author David Bal
 *
 */
public class ICalendarAgenda extends Agenda
{   
    public final static String ICALENDAR_STYLE_SHEET = ICalendarAgenda.class.getResource(ICalendarAgenda.class.getSimpleName() + ".css").toExternalForm();
    
    /*
     * Factory to make VComponents from Appointment
     */
    /** 
     * 
     * @return - 
     */
    public VComponentFactory<Appointment> getVComponentFactory() { return vComponentFactory; }
    private VComponentFactory<Appointment> vComponentFactory;
    public void setVComponentFactory(VComponentFactory<Appointment> vComponentFactory) { this.vComponentFactory = vComponentFactory; }
TableView t;
    /*
     * Factory to make Appointments from VComponents
     */
    public RecurrenceFactory<Appointment> getRecurrenceFactory() { return recurrenceFactory; }
    private RecurrenceFactory<Appointment> recurrenceFactory;
    public void setRecurrenceFactory(RecurrenceFactory<Appointment> recurrenceFactory) { this.recurrenceFactory = recurrenceFactory; }
    
    /** The VCalendar object that contains all scheduling information */
    public VCalendar getVCalendar() { return vCalendar; }
    final private VCalendar vCalendar;
    
    private ObservableList<String> categories; // initialized in constructor
    public ObservableList<String> getCategories() { return categories; }
    public void setCategories(ObservableList<String> categories)
    {
        this.categories = categories;
    }

    /* 
     * Match up maps
     * 
     * map stores start date/time of Appointments as they are made so I can get the original date/time
     * if Agenda changes one (e.g. drag-n-drop).  The original is needed for RECURRENCE-ID.  */
    private final Map<Integer, Temporal> appointmentStartOriginalMap = new HashMap<>();
    public Map<Integer, Temporal> appointmentStartOriginalMap() { return appointmentStartOriginalMap; } // TODO - POPULATE

    // TODO - FIX MEMORY LEAK WITH appointmentVComponentMap
    private final Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap = new HashMap<>(); /* map matches appointment to VEvent that made it */
    public Map<Integer, VComponentDisplayable<?>> appointmentVComponentMap() { return appointmentVComponentMap; }

    private final Map<Integer, List<Appointment>> vComponentAppointmentMap = new HashMap<>(); /* map matches VComponent to their appointments */

//    @Deprecated private final Map<Class<? extends VComponent>, AppointmentChangeBehavior> vComponentClassBehaviorMap = new HashMap<>();
//    @Deprecated public Map<Class<? extends VComponent>, AppointmentChangeBehavior> vComponentClassBehaviorMap() { return vComponentClassBehaviorMap; }

//    /** Callback for creating unique identifier values
//     * @see VComponent#getUidGeneratorCallback() */
//    public Callback<Void, String> getUidGeneratorCallback() { return uidGeneratorCallback; }
//    private static Integer nextKey = 0;
//    private Callback<Void, String> uidGeneratorCallback = (Void) ->
//    { // default UID generator callback
//        String dateTime = DateTimeUtilities.LOCAL_DATE_TIME_FORMATTER.format(LocalDateTime.now());
//        String domain = "jfxtras.org";
//        return dateTime + "-" + nextKey++ + domain;
//    };
//    public void setUidGeneratorCallback(Callback<Void, String> uidCallback) { this.uidGeneratorCallback = uidCallback; }
    
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
     * Callback for determining scope of edit change - defaults to always answering ALL
     * Add For choice dialog, change to different callback
     */
    private Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> oneAllThisAndFutureDialogCallback = (m) -> ChangeDialogOption.ALL;
    public void setOneAllThisAndFutureDialogCallback(Callback<Map<ChangeDialogOption, Pair<Temporal,Temporal>>, ChangeDialogOption> callback) { oneAllThisAndFutureDialogCallback = callback; }
    
    // Default edit popup callback - this callback replaces Agenda's default edit popup
    private Callback<Appointment, Void> editAppointmentCallback = (Appointment appointment) ->
    {
        VComponentDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
        } else
        {
            // make popup stage
            Stage popupStage = new Stage();
            String appointmentTime = AgendaDateTimeUtilities.formatRange(appointment.getStartTemporal(), appointment.getEndTemporal());
            popupStage.setTitle(vComponent.getSummary().getValue() + ":" + appointmentTime);

            Object[] params = new Object[] {
                    getVCalendar(),
                    appointment.getStartTemporal(),
                    appointment.getEndTemporal(),
                    getCategories()
                    };
            EditDisplayableScene popupScene = SimpleEditSceneFactory.newScene(vComponent, params);
            popupScene.getStylesheets().addAll(getUserAgentStylesheet(), ICalendarAgenda.ICALENDAR_STYLE_SHEET);
            popupStage.setScene(popupScene);
            
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
            
            // hide when finished
            popupScene.getEditDisplayableTabPane().isFinished().addListener((obs) -> popupStage.hide());
        }
        return null;
    };
    public Callback<Appointment, Void> getICalendarEditPopupCallback() { return editAppointmentCallback; }

    /** selectOneAppointmentCallback:
     * When one appointment is selected this callback is run.  It can be used to open a popup to provide edit,
     * delete or other edit options.
     */
    public Callback<Appointment, Void> getSelectedOneAppointmentCallback() { return selectedOneAppointmentCallback; }
//    private Callback<Appointment, Void> selectedOneAppointmentCallbackOld = (Appointment appointment) ->
//    {
//        System.out.println("selected one");
//        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
////        SelectedOneAppointmentLoader oneSelectedPopup = new SelectedOneAppointmentLoader(this, appointment);
//        OneSelectedAppointmentPopup oneSelectedPopup = new OneSelectedAppointmentPopup();
//        oneSelectedPopup.setupData(this, appointment);
////        oneSelectedPopup.isFinished().addListener((obs) -> oneSelectedPopup.hide());
//        oneSelectedPopup.show(bodyPane, NodeUtil.screenX(bodyPane) + bodyPane.getWidth()/2, NodeUtil.screenY(bodyPane) + bodyPane.getHeight()/2);
//        oneSelectedPopup.focusedProperty().addListener((obs) -> oneSelectedPopup.hide());
//        return null;
//    };

    Alert lastOneAppointmentSelectedAlert;
    private Callback<Appointment, Void> selectedOneAppointmentCallback = (Appointment appointment) ->
    {
        OneAppointmentSelectedAlert alert = new OneAppointmentSelectedAlert(appointment, Settings.resources);

        alert.initOwner(this.getScene().getWindow());
        Pane bodyPane = (Pane) ((AgendaSkin) getSkin()).getNodeForPopup(appointment);
        alert.setX(NodeUtil.screenX(bodyPane) + bodyPane.getWidth()/2);
        alert.setY(NodeUtil.screenY(bodyPane) + bodyPane.getHeight()/2);
//        System.out.println(alert.getX() + " " + alert.getY());
        
        // Check if previous alert so it can be closed (like autoHide for popups)
        if (lastOneAppointmentSelectedAlert != null)
        {
            lastOneAppointmentSelectedAlert.close();
        }
        lastOneAppointmentSelectedAlert = alert; // save for next time

        alert.resultProperty().addListener((obs, oldValue, newValue) -> 
        {
            if (newValue != null)
            {
                lastOneAppointmentSelectedAlert = null;
                String buttonText = newValue.getText();
                if (buttonText.equals(Settings.resources.getString("edit")))
                {
                    getEditAppointmentCallback().call(appointment);
                } else if (buttonText.equals(Settings.resources.getString("delete")))
                {
                    VComponentDisplayable<?> vComponent = appointmentVComponentMap().get(System.identityHashCode(appointment));
                    Object[] params = new Object[] {
                            DeleteChoiceDialog.DELETE_DIALOG_CALLBACK,
                            appointment.getStartTemporal(),
                            getVCalendar().getParentComponentList(vComponent)
                    };
                    SimpleDeleterFactory.newDeleter(vComponent, params).delete();
                }
            }
        });
        
        alert.show(); // NOTE: alert.showAndWait() doesn't work - results in a blank dialog panel for 2nd Alert and beyond
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
     * Default changed appointment callback (handles drag-n-drop and expand end time)
     * allows dialog to prompt user for change to all, this-and-future or all for repeating VComponents
     */
    private Callback<Appointment, Void> appointmentChangedCallback = (Appointment appointment) ->
    {
        VComponentDisplayable<?> vComponent = appointmentVComponentMap.get(System.identityHashCode(appointment));
        Object[] params = reviseParamGenerator(vComponent, appointment);
        SimpleRevisorFactory.newReviser(vComponent, params).revise();
        appointmentStartOriginalMap.put(System.identityHashCode(appointment), appointment.getStartTemporal()); // update start map
        Platform.runLater(() -> refresh());
        return null;
    };
    
    /** Generate the parameters required for {@link SimpleRevisorFactory} */
    private Object[] reviseParamGenerator(VComponent vComponent, Appointment appointment)
    {
//        VEvent vComponent = (VEvent) appointmentVComponentMap().get(System.identityHashCode(appointment));

        if (vComponent == null)
        {
            // NOTE: Can't throw exception here because in Agenda there is a mouse event that isn't consumed.
            // Throwing an exception will leave the mouse unresponsive.
            System.out.println("ERROR: no component found - popup can'b be displayed");
            return null;
        } else
        {
            VComponent vComponentOriginalCopy = null;
            try
            {
                vComponentOriginalCopy = vComponent.getClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e)
            {
                e.printStackTrace();
            }
            vComponentOriginalCopy.copyChildrenFrom(vComponent);
            Temporal startOriginalRecurrence = appointmentStartOriginalMap().get(System.identityHashCode(appointment));
            final Temporal startRecurrence;
            final Temporal endRecurrence;

            boolean wasDateType = DateTimeType.of(startOriginalRecurrence).equals(DateTimeType.DATE);
            boolean isNotDateType = ! DateTimeType.of(appointment.getStartTemporal()).equals(DateTimeType.DATE);
            boolean isChangedToTimeBased = wasDateType && isNotDateType;
            boolean isChangedToWholeDay = appointment.isWholeDay() && isNotDateType;
            if (isChangedToTimeBased)
            {
                startRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getStartTemporal(), ZoneId.systemDefault());
                endRecurrence = DateTimeType.DATE_WITH_LOCAL_TIME_AND_TIME_ZONE.from(appointment.getEndTemporal(), ZoneId.systemDefault());
            } else if (isChangedToWholeDay)
            {
                startRecurrence = LocalDate.from(appointment.getStartTemporal());
                Temporal endInstanceTemp = LocalDate.from(appointment.getEndTemporal());
                endRecurrence = (endInstanceTemp.equals(startRecurrence)) ? endInstanceTemp.plus(1, ChronoUnit.DAYS) : endInstanceTemp; // make period between start and end at least one day
            } else
            {
                startRecurrence = appointment.getStartTemporal();
                endRecurrence = appointment.getEndTemporal();            
            }
            return new Object[] {
                    EditChoiceDialog.EDIT_DIALOG_CALLBACK,
                    endRecurrence,
                    startOriginalRecurrence,
                    startRecurrence,
                    getVCalendar().getParentComponentList(vComponent),
                    vComponent,
                    vComponentOriginalCopy
                    };
        }
    }
    
    // CONSTRUCTOR
    public ICalendarAgenda(VCalendar vCalendar)
    {
        super();
        this.vCalendar = vCalendar;
//        vComponentStore = new DefaultVComponentFromAppointment(appointmentGroups()); // default VComponent store - for Appointments, if other implementation used make new store
        recurrenceFactory = new DefaultRecurrenceFactory(appointmentGroups()); // default recurrence factory - for Appointments, if other implementation is used assign different factory
        vComponentFactory = new DefaultVComponentFactory(); // default VComponent factory - for ppointments
        
        /*
         *  Default callback to accept new drawn appointments.
         *  Note: If a different Appointment implementation is used then custom recurrenceFactory, 
         *  vComponentFactory and a custom newAppointmentCallback is required
         */
        setNewAppointmentCallback((LocalDateTimeRange dateTimeRange) -> 
        {
            Temporal s = dateTimeRange.getStartLocalDateTime().atZone(ZoneId.systemDefault());
            Temporal e = dateTimeRange.getEndLocalDateTime().atZone(ZoneId.systemDefault());
            return new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(s)
                    .withEndTemporal(e)
                    .withSummary("New")
                    .withDescription("")
                    .withAppointmentGroup(appointmentGroups().get(0));
        });
        
        // Populate component class to behavior map with required behaviors
//        vComponentClassBehaviorMap.put(VEvent.class, new VEventBehavior(this));
//        vComponentClassBehaviorMap.put(VJournal.class, new VJournalBehavior(this));
//        vComponentClassBehaviorMap.put(VTodo.class, new VTodoBehavior(this));
        
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
//                        ZonedDateTime created = ZonedDateTime.now(ZoneId.of("Z"));
                        Appointment appointment = change.getAddedSubList().get(0);
                        String originalSummary = appointment.getSummary();
                        AppointmentGroup originalAppointmentGroup = appointment.getAppointmentGroup();
                        ButtonData button = newAppointmentDrawnCallback.call(change.getAddedSubList().get(0));
                        System.out.println("buttonDate:" + button);
                        switch (button)
                        {
                        case CANCEL_CLOSE:
//                            appointments().remove(appointment);
//                            Platform.runLater(() -> refresh());
                            break;
                        case OK_DONE:
                        {
                            VComponent newVComponent = getVComponentFactory().createVComponent(appointment);
                            getVCalendar().addVComponent(newVComponent);
                            getVCalendar().getVEvents().forEach(System.out::println);
//                            boolean hasSummary = appointment.getSummary() != null;
//                            boolean hasSummaryChanged = appointment.getSummary().equals(originalSummary);
//                            boolean hasAppointmentGroupChanged = appointment.getAppointmentGroup().equals(originalAppointmentGroup);
//                            if ((hasSummary && ! hasSummaryChanged) || ! hasAppointmentGroupChanged)
//                            {
//                                Platform.runLater(() -> refresh());
//                            }
                            break;
                        }
                        case OTHER: // ADVANCED EDIT
                        {
                            VComponent newVComponent = getVComponentFactory().createVComponent(appointment);
                            getVCalendar().addVComponent(newVComponent);
                            editAppointmentCallback.call(vComponentAppointmentMap.get(System.identityHashCode(newVComponent)).get(0));
//                            Platform.runLater(() -> refresh());
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
                } else if (change.wasRemoved())
                {
                    change.getRemoved().stream().forEach(a -> 
                    { // add appointments to EXDATE
//                        VComponentDisplayable<?> v = appointmentVComponentMap().get(a);
//                        System.out.println("remove:" + a.hashCode());
//                        Platform.runLater(() -> refresh());
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
                    
//                    if (dateTimeRange != null)
//                    {
                        List<Appointment> newAppointments = new ArrayList<>();
                        // add new appointments
                        change.getAddedSubList()
                                .stream()
                                .forEach(v -> newAppointments.addAll(makeAppointments(v)));
                        appointments().removeListener(appointmentsListChangeListener);
                        appointments().addAll(newAppointments);
                        appointments().addListener(appointmentsListChangeListener);
//                    }
                } else if (change.wasRemoved())
                {
                    // remove associated appointments
                    change.getRemoved()
                        .stream()
                        .forEach(v -> 
                        {
                            List<Appointment> remove = vComponentAppointmentMap.get(System.identityHashCode(v));
                            appointments().removeAll(remove);
                        });
                }
            }
        };

        // Listen for changes to appointments (additions and deletions)
        appointments().addListener(appointmentsListChangeListener);

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
                }
            }
        };
        selectedAppointments().addListener(selectedAppointmentListener);
        
        // CHANGE DEFAULT EDIT POPUP - replace default popup with one with repeat options
        setEditAppointmentCallback(editAppointmentCallback);

        // LISTEN FOR AGENDA RANGE CHANGES
        setLocalDateTimeRangeCallback(dateTimeRange ->
        {
            List<Appointment> newAppointments = new ArrayList<>();
            getRecurrenceFactory().setStartRange(dateTimeRange.getStartLocalDateTime());
            getRecurrenceFactory().setEndRange(dateTimeRange.getEndLocalDateTime());
            if (dateTimeRange != null)
            {
                appointments().removeListener(appointmentsListChangeListener); // remove appointmentListener to prevent making extra vEvents during refresh
//                System.out.println("appointments:" + appointments());
                appointments().clear();
                vComponentAppointmentMap.clear();
                appointmentStartOriginalMap.clear();
                appointmentVComponentMap.clear();
                getVCalendar().getVEvents().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
                getVCalendar().getVTodos().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
                getVCalendar().getVJournals().stream().forEach(v -> newAppointments.addAll(makeAppointments(v)));
                appointments().addAll(newAppointments);
                appointments().addListener(appointmentsListChangeListener); // add back appointmentListener
            }
            return null; // return argument for the Callback
        });
    } // end of constructor
    
    private Collection<Appointment> makeAppointments(VComponentDisplayable<?> v)
    {
        List<Appointment> myAppointments = getRecurrenceFactory().makeRecurrences(v);
        myAppointments.forEach(a -> 
        {
            appointmentVComponentMap.put(System.identityHashCode(a), v);
            appointmentStartOriginalMap.put(System.identityHashCode(a), a.getStartTemporal());
        });
        vComponentAppointmentMap.put(System.identityHashCode(v), myAppointments);
        return myAppointments;
    }  
}
