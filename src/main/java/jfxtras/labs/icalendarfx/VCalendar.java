package jfxtras.labs.icalendarfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentEnum;
import jfxtras.labs.icalendarfx.components.VComponentNew;
import jfxtras.labs.icalendarfx.components.VComponentPersonal;
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
import jfxtras.labs.icalendarfx.properties.component.change.DateTimeStamp;

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
            calendarScale = new SimpleObjectProperty<CalendarScale>(this, PropertyEnum.CALENDAR_SCALE.toString());
        }
        return calendarScale;
    }
    public CalendarScale getCalendarScale()
    {
        return (calendarScale == null) ? null : calendarScaleProperty().get();
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
    public Method getMethod()
    {
        return (method == null) ? null : methodProperty().get();
    }
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
  
    /** 
     * VTODO: RFC 5545 iCalendar 3.6.2. page 55
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     */
    public ObservableList<VTodo> getVTodos() { return vTodos; }
    private ObservableList<VTodo> vTodos = FXCollections.observableArrayList();
    public void setVTodos(ObservableList<VTodo> vTodos) { this.vTodos = vTodos; }
    public VCalendar withVTodo(ObservableList<VTodo> vTodos) { setVTodos(vTodos); return this; }
    public VCalendar withVTodo(String...vTodos)
    {
        Arrays.stream(vTodos).forEach(c -> getVTodos().add(VTodo.parse(c)));
        return this;
    }
    public VCalendar withVTodos(VTodo...vTodos)
    {
        getVTodos().addAll(vTodos);
        return this;
    }
 
    /** 
     * VJOURNAL: RFC 5545 iCalendar 3.6.3. page 57
     * 
     * A grouping of component properties that describe a task that needs to be completed.
     * 
     * @see VComponent
     * @see VJournal
     */
    public ObservableList<VJournal> getVJournals() { return vJournals; }
    private ObservableList<VJournal> vJournals = FXCollections.observableArrayList();
    public void setVJournals(ObservableList<VJournal> vJournals) { this.vJournals = vJournals; }
    public VCalendar withVJournal(ObservableList<VJournal> vJournals) { setVJournals(vJournals); return this; }
    public VCalendar withVJournal(String...vJournals)
    {
        Arrays.stream(vJournals).forEach(c -> getVJournals().add(VJournal.parse(c)));
        return this;
    }
    public VCalendar withVJournals(VJournal...vJournals)
    {
        getVJournals().addAll(vJournals);
        return this;
    }

    /** 
     * VFREEBUSY: RFC 5545 iCalendar 3.6.4. page 59
     * 
     * @see VFreeBusy
     */
    public ObservableList<VFreeBusy> getVFreeBusies() { return vFreeBusys; }
    private ObservableList<VFreeBusy> vFreeBusys = FXCollections.observableArrayList();
    public void setVFreeBusys(ObservableList<VFreeBusy> vFreeBusys) { this.vFreeBusys = vFreeBusys; }
    public VCalendar withVFreeBusy(ObservableList<VFreeBusy> vFreeBusys) { setVFreeBusys(vFreeBusys); return this; }
    public VCalendar withVFreeBusy(String...vFreeBusys)
    {
        Arrays.stream(vFreeBusys).forEach(c -> getVFreeBusies().add(VFreeBusy.parse(c)));
        return this;
    }
    public VCalendar withVFreeBusys(VFreeBusy...vFreeBusys)
    {
        getVFreeBusies().addAll(vFreeBusys);
        return this;
    }

    /** 
     * VTIMEZONE: RFC 5545 iCalendar 3.6.5. page 62
     * 
     * @see VTimeZoneOld
     */
    public ObservableList<VTimeZone> getVTimeZones() { return vTimeZones; }
    private ObservableList<VTimeZone> vTimeZones = FXCollections.observableArrayList();
    public void setVTimeZones(ObservableList<VTimeZone> vTimeZones) { this.vTimeZones = vTimeZones; }
    public VCalendar withVTimeZones(ObservableList<VTimeZone> vTimeZones) { setVTimeZones(vTimeZones); return this; }
    public VCalendar withVTimeZones(String...vTimeZones)
    {
        Arrays.stream(vTimeZones).forEach(c -> getVTimeZones().add(VTimeZone.parse(c)));
        return this;
    }
    public VCalendar withVTimeZones(VTimeZone...vTimeZones)
    {
        getVTimeZones().addAll(vTimeZones);
        return this;
    }
    
    /*
     * CONSTRUCTORS
     */
    
    public VCalendar() {  }
  
    /** Copy constructor */
    public VCalendar(VCalendar source)
    {
        // TODO Auto-generated method stub        
    }

    /*
     * OTHER METHODS
     */
    
    /**
     * List of all components found in calendar object.
     * The list is unmodifiable.
     * 
     * @return - the list of components
     */
    public List<VComponentNew<?>> components()
    {
        List<VComponentNew<?>> allComponents = new ArrayList<>();
        Iterator<VComponentEnum> i = Arrays.stream(VComponentEnum.values()).iterator();
        while (i.hasNext())
        {
            VComponentEnum componentType = i.next();
            List<? extends VComponentNew<?>> myComponents = componentType.getComponents(this);
            if (myComponents != null)
            {
                allComponents.addAll(myComponents);
            }
        }
        return Collections.unmodifiableList(allComponents);
    }
    
    /** 
     * Component sort order map.  Key is component, value is order.  Follows sort order of parsed content.
     * If a parameter is not present in the map, it is put at the end of the sorted by
     * DTSTAMP.  If DTSTAMP is not present, the component is put on top.
     * Generally, this map shouldn't be modified.  Only modify it when you want to force
     * a specific parameter order (e.g. unit testing).
     */
    public Map<VComponentNew<?>, Long> componentSortOrder() { return componentSortOrder; }
    final private Map<VComponentNew<?>, Long> componentSortOrder = new HashMap<>();
    
    
    /** Parse content lines into calendar object */
    public String toContentLines()
    {
        List<VComponentNew<?>> components = components(); // make local to avoid multiple list creation events
        StringBuilder builder = new StringBuilder(components.size()*300);
        builder.append(firstContentLine + System.lineSeparator());

        Map<VComponentNew<?>, CharSequence> componentContentMap = new LinkedHashMap<>();
        components.forEach(component -> componentContentMap.put(component, component.toContentLines()));
        
        // restore component sort order if components were parsed from content
        componentContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<VComponentNew<?>, CharSequence>>) (e1, e2) -> 
                {
                    final Long s1Initial = componentSortOrder().get(e1.getKey());
                    final Long s2Initial = componentSortOrder().get(e2.getKey());
                    final Long s1Final = finalSortOrder(s1Initial, e1.getKey());
                    final Long s2Final = finalSortOrder(s2Initial, e2.getKey());
                    return s1Final.compareTo(s2Final);
                })
                .forEach(p -> 
                {
                    builder.append(p.getValue() + System.lineSeparator());
                });
        
        builder.append(lastContentLine);
        return builder.toString();

    }
    private Long finalSortOrder(Long initialSort, VComponentNew<?> c)
    {
        final Long s1Final;
        if (initialSort == null)
        {
            if (c instanceof VComponentPersonal)
            { // sort by DTSTAMP
                DateTimeStamp dateTimeStamp = ((VComponentPersonal<?>) c).getDateTimeStamp();
                s1Final = (dateTimeStamp != null) ? dateTimeStamp.getValue().toInstant().toEpochMilli() : 0L; // shouldn't even be 0, DTSTAMP is REQUIRED
            } else
            {
                s1Final = 0L; // no DTSTAMP sort order value is zero
            }
        } else
        {
            s1Final = initialSort - Long.MIN_VALUE; // make sorted values use negative values
        }
        return s1Final;
    }
    private final String firstContentLine = "BEGIN:VCALENDAR";
    private final String lastContentLine = "END:VCALENDAR";


    /** Parse content lines into calendar object */
    public static VCalendar parse(String contentLines)
    {
        return null;
        // TODO Auto-generated method stub
    }
}
