package jfxtras.labs.icalendarfx;

import java.util.Arrays;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VEventNew;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;

/**
 * iCalendar Object
 * RFC 5545, 3.4, page 50
 * 
 * Parent calendar object represents a collection of calendaring and scheduling information 
 * 
 * @author David Bal
 *
 * @see VEventNew
 * @see VTodo
 * @see VJournal
 * @see VFreeBusy
 * @see VTimeZone
 */
public class VCalendar
{
    // version of this project, not associated with the iCalendar specification version
    private static String myVersion = "1.0";
    public static final ProductIdentifier DEFAULT_PRODUCT_IDENTIFIER = ProductIdentifier.parse("-////JFxtras////iCalendarFx " + myVersion + "////EN");
    public static final CalendarScale DEFAULT_CALENDAR_SCALE = CalendarScale.parse("GREGORIAN");
    public static final Version DEFAULT_ICALENDAR_SPECIFICATION_VERSION = Version.parse("2.0");
    
    /*
     * Calendar properties
     */
    
    /**
     *  CALSCALE: RFC 5545 iCalendar 3.7.1. page 76
     * This property defines the calendar scale used for the
     * calendar information specified in the iCalendar object.
     * 
     * This project is based on the Gregorian calendar scale.
     * The Gregorian calendar scale is assumed if this property is not
     * specified in the iCalendar object.  It is expected that other
     * calendar scales will be defined in other specifications or by
     * future versions of iCalendar.
     * 
     * The default value is "GREGORIAN"
     * 
     * Example:
     * CALSCALE:GREGORIAN
     * */
    ObjectProperty<CalendarScale> calendarScaleProperty()
    {
        if (calendarScale == null)
        {
            calendarScale = new SimpleObjectProperty<CalendarScale>(this, PropertyEnum.CALENDAR_SCALE.toString(), DEFAULT_CALENDAR_SCALE);
        }
        return calendarScale;
    }
    public CalendarScale getCalendarScale()
    {
        return (calendarScale == null) ? DEFAULT_CALENDAR_SCALE : calendarScaleProperty().get();
    }
    private ObjectProperty<CalendarScale> calendarScale;
    public void setCalendarScale(String calendarScale) { setCalendarScale(CalendarScale.parse(calendarScale)); }
    public void setCalendarScale(CalendarScale calendarScale) { calendarScaleProperty().set(calendarScale); }
    public VCalendar withCalendarScale(CalendarScale calendarScale) { setCalendarScale(calendarScale); return this; }
    public VCalendar withCalendarScale(String calendarScale)
    {
        if (getCalendarScale() == null)
        {
            setCalendarScale(calendarScale);
        } else
        {
            throw new IllegalArgumentException(PropertyEnum.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
        }
        return this;
    }

    /**
     * METHOD: RFC 5545 iCalendar 3.7.2. page 77
     * This property defines the iCalendar object method
     * associated with the calendar object
     * 
     * Example:
     * METHOD:REQUEST
     * */
    ObjectProperty<Method> methodProperty()
    {
        if (method == null)
        {
            method = new SimpleObjectProperty<Method>(this, PropertyEnum.METHOD.toString());
        }
        return method;
    }
    public Method getMethod() { return methodProperty().get(); }
    private ObjectProperty<Method> method;
    public void setMethod(String method) { setMethod(Method.parse(method)); }
    public void setMethod(Method method) { methodProperty().set(method); }
    public VCalendar withMethod(Method method) { setMethod(method); return this; }
    public VCalendar withMethod(String method)
    {
        if (getMethod() == null)
        {
            setMethod(method);
        } else
        {
            throw new IllegalArgumentException(PropertyEnum.METHOD.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    
    /**
     * PRODID: RFC 5545 iCalendar 3.7.3. page 78
     * This property specifies the identifier for the product that
     * created the iCalendar object
     * 
     * This project is named JFxtras iCalendar
     * 
     * Example:
     * PRODID:-//JFxtras//JFXtras iCalendar 1.0//EN
     * */
    ObjectProperty<ProductIdentifier> productIdentifierProperty()
    {
        if (productIdentifier == null)
        {
            productIdentifier = new SimpleObjectProperty<ProductIdentifier>(this, PropertyEnum.PRODUCT_IDENTIFIER.toString(), DEFAULT_PRODUCT_IDENTIFIER);
        }
        return productIdentifier;
    }
    public ProductIdentifier getProductIdentifier() { return productIdentifierProperty().get(); }
    private ObjectProperty<ProductIdentifier> productIdentifier;
    public void setProductIdentifier(String productIdentifier) { setProductIdentifier(ProductIdentifier.parse(productIdentifier)); }
    public void setProductIdentifier(ProductIdentifier productIdentifier) { productIdentifierProperty().set(productIdentifier); }
    public VCalendar withProductIdentifier(ProductIdentifier productIdentifier) { setProductIdentifier(productIdentifier); return this; }
    public VCalendar withProductIdentifier(String productIdentifier)
    {
        if (getProductIdentifier() == null)
        {
            setProductIdentifier(productIdentifier);
        } else
        {
            throw new IllegalArgumentException(PropertyEnum.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    
    /**
     *  VERSION: RFC 5545 iCalendar 3.7.4. page 79
     * This property specifies the identifier corresponding to the
     * highest version number or the minimum and maximum range of the
     * iCalendar specification that is required in order to interpret the
     * iCalendar object.
     * 
     * This project complies with version 2.0
     * 
     * Example:
     * VERSION:2.0
     * */
    ObjectProperty<Version> versionProperty()
    {
        if (version == null)
        {
            version = new SimpleObjectProperty<Version>(this, PropertyEnum.VERSION.toString(), DEFAULT_ICALENDAR_SPECIFICATION_VERSION);
        }
        return version;
    }
    public Version getVersion() { return versionProperty().get(); }
    private ObjectProperty<Version> version;
    public void setVersion(String version) { setVersion(Version.parse(version)); }
    public void setVersion(Version version) { versionProperty().set(version); }
    public VCalendar withVersion(Version version) { setVersion(version); return this; }
    public VCalendar withVersion(String version)
    {
        if (getVersion() == null)
        {
            setVersion(version);
        } else
        {
            throw new IllegalArgumentException(PropertyEnum.VERSION.toString() + " can only occur once in a calendar component");
        }
        return this;
    }

    /*
     * Calendar Components
     */

    /** 
     * VEVENT: RFC 5545 iCalendar 3.6.1. page 52
     * 
     * A grouping of component properties that describe an event.
     * 
     */
    public ObservableList<VEventNew> getVEvents() { return vEvents; }
    private ObservableList<VEventNew> vEvents = FXCollections.observableArrayList();
    public void setVEvents(ObservableList<VEventNew> vEvents) { this.vEvents = vEvents; }
    public VCalendar withVEventNew(ObservableList<VEventNew> vEvents) { setVEvents(vEvents); return this; }
    public VCalendar withVEventNew(String...vEvents)
    {
        Arrays.stream(vEvents).forEach(c -> getVEvents().add(VEventNew.parse(c)));
        return this;
    }
    public VCalendar withVEvents(VEventNew...vEvents)
    {
        getVEvents().addAll(vEvents);
        return this;
    }


//    /** 
//     * VEVENT Callback
//     * Callback to make a VEvent from a string.  This defines how the specific VEvent implementation
//     * creates VEvent objects
//     * 
//     * For example, the following callback is used in the test cases with the VEvent implementation VEventMock:
//     * (s) -> VEventMock.parse(s)
//     *  */
//    public  Callback<String, VEventNew> getMakeVEventCallback() { return makeVEventCallback; }
//    private Callback<String, VEventNew> makeVEventCallback;
//    public void setMakeVEventCallback(Callback<String, VEventNew> callback) { makeVEventCallback = callback; }
//    public VCalendar withVEventCallback(Callback<String, VEventNew> callback) { setMakeVEventCallback(callback); return this; }
    
    /** 
     * VTODO: RFC 5545 iCalendar 3.6.2. page 55
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     */
    public ObservableList<VTodo> vTodos() { return vTodos; }
    private ObservableList<VTodo> vTodos = FXCollections.observableArrayList();

    /**
     * VTODO Callback
     * Callback to make a VTodo from a string.  This defines how the specific VTodo implementation
     * creates VTodo objects
     * 
     *  */
//    public  Callback<String, VTodo> getMakeVTodoCallback() { return makeVTodoCallback; }
//    private Callback<String, VTodo> makeVTodoCallback;
//    public void setMakeVTodoCallback(Callback<String, VTodo> callback) { makeVTodoCallback = callback; }
//    public VCalendar withVTodoCallback(Callback<String, VTodo> callback) { setMakeVTodoCallback(callback); return this; }
    
    /** 
     * VJOURNAL: RFC 5545 iCalendar 3.6.3. page 57
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VJournal
     */
    public ObservableList<VJournal> vJournals() { return vJournals; }
    private ObservableList<VJournal> vJournals = FXCollections.observableArrayList();

//    /** 
//     * VJOURNAL callback
//     * Callback to make a VJournal from a string.  This defines how the specific VJournal implementation
//     * creates VJournal objects
//     * 
//     *  */
//    public  Callback<String, VJournal> getMakeVJournalCallback() { return makeVJournalCallback; }
//    private Callback<String, VJournal> makeVJournalCallback;
//    public void setMakeVJournalCallback(Callback<String, VJournal> callback) { makeVJournalCallback = callback; }
//    public VCalendar withVJournalCallback(Callback<String, VJournal> callback) { setMakeVJournalCallback(callback); return this; }
    
    /** 
     * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
     * 
     * @see VFreeBusy
     */
    public ObservableList<VFreeBusy> vFreeBusyList() { return vFreeBusyList; }
    private ObservableList<VFreeBusy> vFreeBusyList = FXCollections.observableArrayList();

    /** 
     * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
     * 
     * @see VTimeZoneOld
     */
    public ObservableList<VTimeZone> vTimeZones() { return vTimeZones; }
    private ObservableList<VTimeZone> vTimeZones = FXCollections.observableArrayList();

}
