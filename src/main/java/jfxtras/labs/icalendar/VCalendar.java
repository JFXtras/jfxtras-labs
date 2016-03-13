package jfxtras.labs.icalendar;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendar.VCalendarUtilities.VCalendarProperty;
import jfxtras.labs.icalendar.components.VAlarm;
import jfxtras.labs.icalendar.components.VComponent;
import jfxtras.labs.icalendar.components.VEvent;
import jfxtras.labs.icalendar.components.VFreeBusy;
import jfxtras.labs.icalendar.components.VJournal;
import jfxtras.labs.icalendar.components.VTimeZone;
import jfxtras.labs.icalendar.components.VTodo;

/**
 * API Based on
 * Internet Calendaring and Scheduling Core Object Specification
 *                            (iCalendar) RFC5545
 *
 * Parent calendaring and scheduling core object.
 * It contains two required properties, one optional property,
 * and collections of calendar components.
 * 
 *        calprops   = *(
                  ;
                  ; The following are REQUIRED,
                  ; but MUST NOT occur more than once.
                  ;
                  prodid / version /
                  ;
                  ; The following are OPTIONAL,
                  ; but MUST NOT occur more than once.
                  ;
                  calscale / method /
                  ;
                  ; The following are OPTIONAL,
                  ; and MAY occur more than once.
                  ;
                  x-prop / iana-prop
                  ;
                  )

       component  = 1*(eventc / todoc / journalc / freebusyc /
                    timezonec / iana-comp / x-comp)

       iana-comp  = "BEGIN" ":" iana-token CRLF
                    1*contentline
                    "END" ":" iana-token CRLF

       x-comp     = "BEGIN" ":" x-name CRLF
                    1*contentline
                    "END" ":" x-name CRLF
 * 
 * @author David Bal
 *
 * @see VCalendarUtilities
 */
public class VCalendar
{
    // version of this project, not associated with the iCalendar specification version
    private static String version = "1.0";
    public static final String DEFAULT_PRODUCT_IDENTIFIER = "JFxtras iCalendar " + version;
    public static final String DEFAULT_CALENDAR_SCALE = "GREGORIAN";
    public static final String DEFAULT_ICALENDAR_SPECIFICATION_VERSION = "2.0";
    
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
    public StringProperty calendarScaleProperty() { return calendarScale; }
    final private StringProperty calendarScale = new SimpleStringProperty(this, VCalendarProperty.CALENDAR_SCALE.toString(), DEFAULT_CALENDAR_SCALE);
    public String getCalendarScale() { return calendarScale.get(); }
    public void setCalendarScale(String value) { calendarScale.set(value); }
    public VCalendar withCalendarScale(String s) { setCalendarScale(s); return this; }

    /**
     * METHOD: RFC 5545 iCalendar 3.7.2. page 77
     * This property defines the iCalendar object method
     * associated with the calendar object
     * 
     * Example:
     * METHOD:REQUEST
     * */
    public StringProperty objectMethodProperty() { return objectMethod; }
    final private StringProperty objectMethod = new SimpleStringProperty(this, VCalendarProperty.OBJECT_METHOD.toString());
    public String getObjectMethod() { return objectMethod.get(); }
    public void setObjectMethod(String value) { objectMethod.set(value); }
    public VCalendar withObjectMethod(String s) { setObjectMethod(s); return this; }

    
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
    public StringProperty productIdentifierProperty() { return productIdentifier; }
    final private StringProperty productIdentifier = new SimpleStringProperty(this, VCalendarProperty.PRODUCT_IDENTIFIER.toString(), DEFAULT_PRODUCT_IDENTIFIER);
    public String getProductIdentifier() { return productIdentifier.get(); }
    public void setProductIdentifier(String value) { productIdentifier.set(value); }
    public VCalendar withProductIdentifier(String s) { setProductIdentifier(s); return this; }
    
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
    public StringProperty iCalendarSpecificationVersionProperty() { return iCalendarSpecificationVersion; }
    final private StringProperty iCalendarSpecificationVersion = new SimpleStringProperty(this, VCalendarProperty.ICALENDAR_SPECIFICATION_VERSION.toString(), DEFAULT_ICALENDAR_SPECIFICATION_VERSION);
    public String getICalendarSpecificationVersion() { return iCalendarSpecificationVersion.get(); }
    public void setICalendarSpecificationVersion(String value) { iCalendarSpecificationVersion.set(value); }
    public VCalendar withICalendarSpecificationVersion(String s) { setICalendarSpecificationVersion(s); return this; }

    /*
     * VCOMPONENTS
     */

    /** 
     * VEVENT: RFC 5545 iCalendar 3.6.1. page 52
     * 
     * A grouping of component properties that describe an event.
     * 
     * @see VComponent
     * @see VEvent
     */
    public ObservableList<VEvent<?, ?>> vEvents() { return vEvents; }
    private ObservableList<VEvent<?,?>> vEvents = FXCollections.observableArrayList();

    /** 
     * VEVENT Callback
     * Callback to make a VEvent from a string.  This defines how the specific VEvent implementation
     * creates VEvent objects
     * 
     * For example, the following callback is used in the test cases with the VEvent implementation VEventMock:
     * (s) -> VEventMock.parse(s)
     *  */
    public  Callback<String, VEvent<?,?>> getMakeVEventCallback() { return makeVEventCallback; }
    private Callback<String, VEvent<?,?>> makeVEventCallback;
    public void setMakeVEventCallback(Callback<String, VEvent<?,?>> callback) { makeVEventCallback = callback; }
    public VCalendar withVEventCallback(Callback<String, VEvent<?,?>> callback) { setMakeVEventCallback(callback); return this; }
    
    /** 
     * VTODO: RFC 5545 iCalendar 3.6.2. page 55
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VTodo
     */
    public ObservableList<VTodo<?,?>> vTodos() { return vTodos; }
    private ObservableList<VTodo<?,?>> vTodos = FXCollections.observableArrayList();

    /**
     * VTODO Callback
     * Callback to make a VTodo from a string.  This defines how the specific VTodo implementation
     * creates VTodo objects
     * 
     *  */
    public  Callback<String, VTodo<?,?>> getMakeVTodoCallback() { return makeVTodoCallback; }
    private Callback<String, VTodo<?,?>> makeVTodoCallback;
    public void setMakeVTodoCallback(Callback<String, VTodo<?,?>> callback) { makeVTodoCallback = callback; }
    public VCalendar withVTodoCallback(Callback<String, VTodo<?,?>> callback) { setMakeVTodoCallback(callback); return this; }
    
    /** 
     * VJOURNAL: RFC 5545 iCalendar 3.6.3. page 57
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VJournal
     */
    public ObservableList<VJournal<?,?>> vJournals() { return vJournals; }
    private ObservableList<VJournal<?,?>> vJournals = FXCollections.observableArrayList();

    /** 
     * VJOURNAL callback
     * Callback to make a VJournal from a string.  This defines how the specific VJournal implementation
     * creates VJournal objects
     * 
     *  */
    public  Callback<String, VJournal<?,?>> getMakeVJournalCallback() { return makeVJournalCallback; }
    private Callback<String, VJournal<?,?>> makeVJournalCallback;
    public void setMakeVJournalCallback(Callback<String, VJournal<?,?>> callback) { makeVJournalCallback = callback; }
    public VCalendar withVJournalCallback(Callback<String, VJournal<?,?>> callback) { setMakeVJournalCallback(callback); return this; }
    
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
     * @see VTimeZone
     */
    public ObservableList<VTimeZone> vTimeZones() { return vTimeZones; }
    private ObservableList<VTimeZone> vTimeZones = FXCollections.observableArrayList();
    
    /** 
     * VALARM: RFC 5545 iCalendar 3.6.6. page 71
     * 
     * @see VAlarm
     */
    public ObservableList<VAlarm> vAlarms() { return vAlarms; }
    private ObservableList<VAlarm> vAlarms = FXCollections.observableArrayList();
}
