package jfxtras.labs.icalendaragenda.scene.control.agenda;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javafx.beans.InvalidationListener;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendar.DateTimeUtilities;
import jfxtras.labs.icalendar.components.VComponentDisplayableOld;
import jfxtras.labs.icalendar.components.VComponentUtilities;
import jfxtras.labs.icalendar.components.VComponentUtilities.VComponentPropertyOld;
import jfxtras.labs.icalendar.components.VEventOld;
import jfxtras.labs.icalendar.components.VEventUtilities;
import jfxtras.labs.icalendar.properties.descriptive.Description;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.agenda.Agenda.AppointmentGroup;

/**
 * Example of a VEvent for Agenda.
 * This class creates and edits appointments for rendering in Agenda.
 * 
 * @author David Bal
 *
 */
public class VEventImpl extends VEventOld<Appointment, VEventImpl>
{
    public ObjectProperty<AppointmentGroup> appointmentGroupProperty() { return appointmentGroup; }
    private ObjectProperty<AppointmentGroup> appointmentGroup = new SimpleObjectProperty<AppointmentGroup>(this, "CATEGORIES");
    public void setAppointmentGroup(AppointmentGroup appointmentGroup) { this.appointmentGroup.set(appointmentGroup); }
    public AppointmentGroup getAppointmentGroup() { return appointmentGroup.get(); }
    public VEventImpl withAppointmentGroup(AppointmentGroup appointmentGroup) { setAppointmentGroup(appointmentGroup); return this; }

    /** appointmentGroups from Agenda.  It is used to synch categories to appointmentGroup, 
     * which is needed by the makeAppointment method 
     * @see #makeInstances() */
    public List<AppointmentGroup> getAppointmentGroups() { return appointmentGroups; }
    final private List<AppointmentGroup> appointmentGroups;

    /* below listeners ensures appointmentGroup description and categories match.  
     * added to categoriesProperty and appointmentGroups by the constructor.
     * appointmentGroups must be set
     */
    private final InvalidationListener categoriesListener = obs ->
    {
        if (! getAppointmentGroups().isEmpty() && getCategories() != null)
        {
            Optional<AppointmentGroup> myGroup = getAppointmentGroups()
                    .stream()
//                    .peek(a -> System.out.println(a.getDescription()))
                    .filter(g -> g.getDescription().equals(getCategories()))
                    .findFirst();
            if (myGroup.isPresent()) setAppointmentGroup(myGroup.get());                
        }
    };
    private final ChangeListener<? super AppointmentGroup> appointmentGroupListener = 
            (obs, oldValue, newValue) -> getCategories().setText(newValue.getDescription());
        
    /*
     * CONSTRUCTORS
     */
    /** Copy constructor */
    public VEventImpl(VEventImpl vevent)
    {
        super(vevent);
        this.appointmentGroups = vevent.getAppointmentGroups();
        categoriesProperty().addListener(categoriesListener);
        appointmentGroup.addListener(appointmentGroupListener);
        copy(vevent, this);
    }
    
    public VEventImpl(List<AppointmentGroup> appointmentGroups)
    {
        super();
        this.appointmentGroups = appointmentGroups;
        categoriesProperty().addListener(categoriesListener);
        appointmentGroup.addListener(appointmentGroupListener);
        if (getAppointmentGroup() == null)
        {
            setAppointmentGroup(getAppointmentGroups().get(0)); // set default appointment group if none set
        }
    }
    
    /**
     * makes new VEventImpl by copying properties from appointment.
     * stores start and end date/times as ZonedDateTime in default time zone
     * 
     * @param appointment - from Agenda
     */
    public VEventImpl(Appointment appointment, ObservableList<AppointmentGroup> appointmentGroups)
    {
        this(appointmentGroups);
        VComponentPropertyOld.CATEGORIES.parseAndSetProperty(this, appointment.getAppointmentGroup().getDescription());
//        setCategories(appointment.getAppointmentGroup().getDescription());
        setDateTimeStart(appointment.getStartTemporal());
        setDateTimeEnd(appointment.getEndTemporal());
        setDateTimeStamp(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        setDateTimeCreated(ZonedDateTime.now().withZoneSameInstant(ZoneId.of("Z")));
        setDescription(new Description(appointment.getDescription()));
        setLocation(appointment.getLocation());
        VComponentPropertyOld.SUMMARY.parseAndSetProperty(this, appointment.getSummary());
        setUniqueIdentifier(getUidGeneratorCallback().call(null));
        instances().add(appointment);
        if (! errorString().equals("")) throw new IllegalArgumentException(errorString());
    }

    /** Deep copy all fields from source to destination 
     * */
    private static void copy(VEventImpl source, VEventImpl destination)
    {
        destination.setAppointmentGroup(source.getAppointmentGroup());
    }
    
    /** Deep copy all fields from source to destination */
    @Override
    public void copyTo(VComponentDisplayableOld<Appointment> destination)
    {
        super.copyTo(destination);
        copy(this, (VEventImpl) destination);
    }
        
    /** Make new VEventImpl and populate properties by parsing a string of line-separated
     * content lines
     *  */
    public static VEventImpl parse(String vEventString, List<AppointmentGroup> appointmentGroups)
    {
        VEventImpl vEvent = new VEventImpl(appointmentGroups);
        Iterator<String> lineIterator = Arrays.stream( vEventString.split(System.lineSeparator()) ).iterator();
        while (lineIterator.hasNext())
        {
            String line = lineIterator.next();
            // parse each property name by its associated property enums
            boolean success = VEventUtilities.parse(vEvent, line);
            if (! success)
            {
                VComponentUtilities.parse(vEvent, line);
            }
        }
        return vEvent;
    }

    /**
     * Tests equality between two VEventImpl objects.  Treats v1 as expected.  Produces a JUnit-like
     * output if objects are not equal.
     * 
     * @param v1 - expected VEventImpl
     * @param v2 - actual VEventImpl
     * @param verbose - true = display list of unequal properties, false no display output
     * @return - equality result
     */
    public static boolean isEqualTo(VEventImpl v1, VEventImpl v2, boolean verbose)
    {
        // VEventImpl properties
        boolean appointmentGroupEquals = (v1.getAppointmentGroup() == null) ? (v2.getAppointmentGroup() == null) : v1.getAppointmentGroup().equals(v2.getAppointmentGroup());
//        if (! appointmentGroupEquals && verbose) { System.out.println("Appointment Group:" + " not equal:" + v1.getAppointmentGroup() + " " + v2.getAppointmentGroup()); }
        boolean vEventResult = VEventUtilities.isEqualTo(v1, v2, verbose);
        return vEventResult && appointmentGroupEquals;
    }
    
    /**
     * Tests equality between two VEventImpl objects.  Treats v1 as expected.  Produces a JUnit-like
     * output if objects are not equal.
     * 
     * @param v1 - expected VEventImpl
     * @param v2 - actual VEventImpl
     * @param verbose - true = display list of unequal properties, false no display output
     * @return - equality result
     */
    public static boolean isEqualTo(VEventImpl v1, VEventImpl v2)
    {
        return isEqualTo(v1, v2, true);
    }
            
    /**
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @return created appointments
     */
    @Override
    public List<Appointment> makeInstances()
    {
        List<Appointment> madeAppointments = new ArrayList<>();
        streamLimitedByRange().forEach(temporalStart ->
        {
            TemporalAmount duration = endType().getDuration(this);
            Temporal temporalEnd = temporalStart.plus(duration);
            Appointment appt = new Agenda.AppointmentImplTemporal()
                    .withStartTemporal(temporalStart)
                    .withEndTemporal(temporalEnd)
                    .withDescription(getDescription().getText())
                    .withSummary(VComponentPropertyOld.SUMMARY.toPropertyString(this))
                    .withLocation(getLocation())
                    .withWholeDay(isWholeDay())
                    .withAppointmentGroup(getAppointmentGroup());
            madeAppointments.add(appt);   // add appointments to return argument
            instances().add(appt); // add appointments to this object's collection
        });
        return madeAppointments;
    }
    
    /**
     * Returns appointments for Agenda that should exist between dateTimeRangeStart and dateTimeRangeEnd
     * based on VEvent properties.  Uses dateTimeRange previously set in VEvent.
     * @param <Appointment>
     * 
     * @param startRange
     * @param endRange
     * @return created appointments
     */
    @Override
    public List<Appointment> makeInstances(Temporal startRange, Temporal endRange)
    {
        if (DateTimeUtilities.isAfter(startRange, endRange)) throw new DateTimeException("endRange must be after startRange");
        setEndRange(endRange);
        setStartRange(startRange);
        return makeInstances();
    }
    
}
