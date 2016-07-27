package jfxtras.labs.icalendarfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayable;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.content.MultiLineContent;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

/**
 * iCalendar Object
 * RFC 5545, 3.4, page 50
 * 
 * Parent calendar object represents a collection of calendaring and scheduling information 
 * 
 * @author David Bal
 *
 */
public class VCalendar extends VParentBase
{
    // version of this project, not associated with the iCalendar specification version
    public static String myVersion = "1.0";
    private static final String FIRST_CONTENT_LINE = "BEGIN:VCALENDAR";
    private static final String LAST_CONTENT_LINE = "END:VCALENDAR";
    
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
            calendarScale = new SimpleObjectProperty<CalendarScale>(this, CalendarElementType.CALENDAR_SCALE.toString());
            orderer().registerSortOrderProperty(calendarScale);
        }
        return calendarScale;
    }
    public CalendarScale getCalendarScale() { return (calendarScale == null) ? null : calendarScaleProperty().get(); }
    private ObjectProperty<CalendarScale> calendarScale;
    public void setCalendarScale(String calendarScale) { setCalendarScale(CalendarScale.parse(calendarScale)); }
    public void setCalendarScale(CalendarScale calendarScale) { calendarScaleProperty().set(calendarScale); }
    public VCalendar withCalendarScale(CalendarScale calendarScale)
    {
        if (getCalendarScale() == null)
        {
            setCalendarScale(calendarScale);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    public VCalendar withCalendarScale(String calendarScale)
    {
        if (getCalendarScale() == null)
        {
            setCalendarScale(calendarScale);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
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
            method = new SimpleObjectProperty<Method>(this, CalendarElementType.METHOD.toString());
            orderer().registerSortOrderProperty(method);
        }
        return method;
    }
    public Method getMethod() { return (method == null) ? null : methodProperty().get(); }
    private ObjectProperty<Method> method;
    public void setMethod(String method) { setMethod(Method.parse(method)); }
    public void setMethod(Method method) { methodProperty().set(method); }
    public VCalendar withMethod(Method method)
    {
        if (getMethod() == null)
        {
            setMethod(method);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.METHOD.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    public VCalendar withMethod(String method)
    {
        if (getMethod() == null)
        {
            setMethod(method);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.METHOD.toString() + " can only occur once in a calendar component");
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
            productIdentifier = new SimpleObjectProperty<ProductIdentifier>(this, CalendarElementType.PRODUCT_IDENTIFIER.toString());
            orderer().registerSortOrderProperty(productIdentifier);
        }
        return productIdentifier;
    }
    public ProductIdentifier getProductIdentifier() { return (productIdentifier == null) ? null : productIdentifierProperty().get(); }
    private ObjectProperty<ProductIdentifier> productIdentifier;
    public void setProductIdentifier(String productIdentifier) { setProductIdentifier(ProductIdentifier.parse(productIdentifier)); }
    public void setProductIdentifier(ProductIdentifier productIdentifier) { productIdentifierProperty().set(productIdentifier); }
    public VCalendar withProductIdentifier(ProductIdentifier productIdentifier)
    {
        if (getProductIdentifier() == null)
        {
            setProductIdentifier(productIdentifier);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    public VCalendar withProductIdentifier(String productIdentifier)
    {
        if (getProductIdentifier() == null)
        {
            setProductIdentifier(productIdentifier);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
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
            version = new SimpleObjectProperty<Version>(this, CalendarElementType.VERSION.toString());
            orderer().registerSortOrderProperty(version);
        }
        return version;
    }
    public Version getVersion() { return (version == null) ? null : versionProperty().get(); }
    private ObjectProperty<Version> version;
    public void setVersion(String version) { setVersion(Version.parse(version)); }
    public void setVersion(Version version) { versionProperty().set(version); }
    public VCalendar withVersion(Version version)
    {
        if (getVersion() == null)
        {
            setVersion(version);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.VERSION.toString() + " can only occur once in a calendar component");
        }
        return this;
    }
    public VCalendar withVersion(String version)
    {
        if (getVersion() == null)
        {
            setVersion(version);
        } else
        {
            throw new IllegalArgumentException(CalendarElementType.VERSION.toString() + " can only occur once in a calendar component");
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
    public ObservableList<VEvent> getVEvents() { return vEvents; }
    private ObservableList<VEvent> vEvents = FXCollections.observableArrayList();
    public void setVEvents(ObservableList<VEvent> vEvents) { this.vEvents = vEvents; }
    public VCalendar withVEventNew(ObservableList<VEvent> vEvents)
    {
        setVEvents(vEvents);
        return this;
    }
    public VCalendar withVEventNew(String...vEvents)
    {
        Arrays.stream(vEvents).forEach(c -> getVEvents().add(VEvent.parse(c)));
        return this;
    }
    public VCalendar withVEvents(VEvent...vEvents)
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
    public VCalendar withVTodo(ObservableList<VTodo> vTodos)
    {
        setVTodos(vTodos);
        return this;
    }
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
    
    /**
     * A convenience method that adds a VComponent to one of the ObservableLists based on
     * its type such as VEVENT, VTODO, etc.
     * 
     * @param newVComponent - VComponent to add
     */
    public void addVComponent(VComponent newVComponent)
    {
        if (newVComponent instanceof VEvent)
        {
            getVEvents().add((VEvent) newVComponent);
        } else if (newVComponent instanceof VTodo)
        {
            getVTodos().add((VTodo) newVComponent);            
        } else if (newVComponent instanceof VJournal)
        {
            getVJournals().add((VJournal) newVComponent);
        } else if (newVComponent instanceof VFreeBusy)
        {
            getVFreeBusies().add((VFreeBusy) newVComponent);            
        } else if (newVComponent instanceof VTimeZone)
        {
            getVTimeZones().add((VTimeZone) newVComponent);            
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + newVComponent.getClass());
        }
    }
    
    /**
     * A convenience method that returns parent list of the {@link VComponent} parameter.
     * Returns null if component is not in any {@link VComponent} list not found.
     * 
     * @param vComponent - VComponent to look up
     */
    public List<? extends VComponent> getParentComponentList(VComponent vComponent)
    {
        if (vComponent instanceof VEvent)
        {
            return (getVEvents().contains(vComponent)) ? getVEvents() : null;
        } else if (vComponent instanceof VTodo)
        {
            return (getVTodos().contains(vComponent)) ? getVTodos() : null;
        } else if (vComponent instanceof VJournal)
        {
            return (getVJournals().contains(vComponent)) ? getVJournals() : null;
        } else if (vComponent instanceof VFreeBusy)
        {
            return (getVFreeBusies().contains(vComponent)) ? getVFreeBusies() : null;
        } else if (vComponent instanceof VTimeZone)
        {
            return (getVTimeZones().contains(vComponent)) ? getVTimeZones() : null;
        } else
        {
            throw new RuntimeException("Unsuppored VComponent type:" + vComponent.getClass());
        }
    }
    
    /*
     * Map of Related Components - UID is key and List of all related VComponents is value.
     * Note: if you only want child components you need to filter the list to only include components
     * that have a RECURRENCE-ID
     */
    private Map<String, List<VComponentDisplayable<?>>> uidComponentsMap = new HashMap<>(); // public for testing
    public Map<String, List<VComponentDisplayable<?>>> uidComponentsMap() { return Collections.unmodifiableMap(uidComponentsMap); }
    
    /**
     * RecurrenceID listener
     * notifies parents when a child component with recurrenceID is created or removed
     * also maintains {@link #uidToComponentMap}
     */
    private ListChangeListener<VComponentDisplayable<?>> displayableListChangeListener = (ListChangeListener.Change<? extends VComponentDisplayable<?>> change) ->
    {
        while (change.next())
        {
            if (change.wasAdded())
            {
                change.getAddedSubList().forEach(vComponent -> 
                {
                    // set recurrence children callback (VComponents having RecurrenceIDs and matching UID to a recurrence parent)
                    vComponent.setRecurrenceChildrenListCallBack( (c) ->
                    {
                        if (c.getUniqueIdentifier() == null) return null;
                        return uidComponentsMap.get(c.getUniqueIdentifier().getValue())
                                .stream()
                                .filter(v -> v.getRecurrenceId() != null) // keep only children objects
                                .collect(Collectors.toList());
                    });
                    // set recurrence parent callback (the VComponent with matching UID and no RECURRENCEID)
                    vComponent.setRecurrenceParentListCallBack( (c) ->
                    {
                        if (c.getUniqueIdentifier() == null) return null;
                        return uidComponentsMap.get(c.getUniqueIdentifier().getValue())
                                .stream()
                                .filter(v -> v.getRecurrenceId() == null) // parents don't have RECURRENCEID
                                .findAny()
                                .orElse(null);
                    });
                    // add VComponent to map
                    if (vComponent.getUniqueIdentifier() != null)
                    {
                        String uid = vComponent.getUniqueIdentifier().getValue();
                        final List<VComponentDisplayable<?>> relatedComponents;
                        if (uidComponentsMap.get(uid) == null)
                        {
                            relatedComponents = new ArrayList<>();
                            uidComponentsMap.put(uid, relatedComponents);
                        } else
                        {
                            relatedComponents = uidComponentsMap.get(uid);
                        }
                        relatedComponents.add(vComponent);
                    }
                });
            } else
            {
                if (change.wasRemoved())
                {
                    change.getRemoved().forEach(vComponent -> 
                    {
                        String uid = vComponent.getUniqueIdentifier().getValue();
                        List<VComponentDisplayable<?>> relatedComponents = uidComponentsMap.get(uid);
                        if (relatedComponents != null)
                        {
                            relatedComponents.remove(vComponent);
                            if (relatedComponents.isEmpty())
                            {
                                uidComponentsMap.remove(uid);
                            }
                        }
                    });
                }                
            }
        }
    };
    
//    /*
//     * SORT ORDER FOR CHILD ELEMENTS
//     */
//    final private Orderer orderer;
//    @Override
//    public Orderer orderer() { return orderer; }
    
//    private Callback<VElement, Void> copyChildElementCallback = (child) ->
//    {
//        CalendarElementType type = CalendarElementType.enumFromClass(child.getClass());
//        if (type != null)
//        { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
//            type.copyChild(child, this);
//        }
//        return null;
//    };
    
    @Override
    protected Callback<VChild, Void> copyChildCallback()
    {        
        return (child) ->
        {
            CalendarElementType type = CalendarElementType.enumFromClass(child.getClass());
            if (type != null)
            {
                type.copyChild(child, this);
            }
            return null;
        };
    }
    
//    /** 
//     * SORT ORDER
//     * Component sort order map.  Key is component, value is order.  Follows sort order of parsed content or
//     * order of added components.
//     * 
//     * If a parameter is not present in the map, it is put at the end of the sorted by
//     * DTSTAMP.  If DTSTAMP is not present, the component is put on top.
//     * Generally, this map shouldn't be modified.  Only modify it when you want to force
//     * a specific parameter order (e.g. unit testing).
//     */
//    public Map<VCalendarElement, Integer> elementSortOrder() { return elementSortOrder; }
//    final private Map<VCalendarElement, Integer> elementSortOrder = new HashMap<>();
//    private volatile Integer sortOrderCounter = 0;
//    
//    /**
//     * Maintains {@link #elementSortOrder} map
//     */
//    private ListChangeListener<VCalendarElement> sortOrderListChangeListener = (ListChangeListener.Change<? extends VCalendarElement> change) ->
//    {
//        while (change.next())
//        {
//            if (change.wasAdded())
//            {
//                change.getAddedSubList().forEach(vComponent ->  elementSortOrder().put(vComponent, sortOrderCounter));
//                sortOrderCounter += 100;
//            } else
//            {
//                if (change.wasRemoved())
//                {
//                    change.getRemoved().forEach(vComponent -> 
//                    {
//                        elementSortOrder().remove(vComponent);
//                    });
//                }                
//            }
//        }
//    };
//    
//    private ChangeListener<? super VCalendarElement> sortOrderChangeListener = (obs, oldValue, newValue) ->
//    {
//        if (oldValue != null)
//        {
//            elementSortOrder().remove(oldValue);
//        }
//        elementSortOrder().put(newValue, sortOrderCounter);
//        sortOrderCounter += 100;
//    };
    
    /*
     * CONSTRUCTORS
     */
    
    public VCalendar()
    {
        addListeners();
        setContentLineGenerator(new MultiLineContent(
                orderer(),
                FIRST_CONTENT_LINE,
                LAST_CONTENT_LINE,
                1000));
    }
  
    /** Copy constructor */
    public VCalendar(VCalendar source)
    {
        this();
        throw new RuntimeException("not implemented");
//        orderer().copyChildrenFrom(source);    
    }

    /*
     * OTHER METHODS
     */
    
//    /** Copy property into this component */
//    @Override protected void copyChild(VCalendarElement child)
//    {
//        CalendarElementType type = CalendarElementType.enumFromClass(child.getClass());
//        if (type != null)
//        { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
//            type.copyChild(child, this);
//        }        
//    }
    
    private void addListeners()
    {
        // listeners to keep map to related components from UID string
        getVEvents().addListener(displayableListChangeListener);
        getVTodos().addListener(displayableListChangeListener);
        getVJournals().addListener(displayableListChangeListener);

        // Sort order listeners
        orderer().registerSortOrderProperty(getVEvents());
        orderer().registerSortOrderProperty(getVTodos());
        orderer().registerSortOrderProperty(getVJournals());
        orderer().registerSortOrderProperty(getVTimeZones());
        orderer().registerSortOrderProperty(getVFreeBusies());
    }

    /** Parse content lines into calendar object */
    @Override
    public void parseContent(String content)
    {
        List<String> contentLines = ICalendarUtilities.unfoldLines(content);
        if (! contentLines.get(0).equals("BEGIN:VCALENDAR"))
        {
            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
        }
        for (int index=1; index<contentLines.size(); index++)
        {
            String line = contentLines.get(index);
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
            String propertyName = line.substring(0, nameEndIndex);
            
            // Parse component
            if (propertyName.equals("BEGIN"))
            {
                String componentName = line.substring(nameEndIndex+1);
                List<String> myLines = new ArrayList<>(20);
                myLines.add(line);
                final String endLine = "END:" + componentName;
                do
                {
                    index++;
                    line = contentLines.get(index);
                    myLines.add(line);
                } while (! line.equals(endLine));

                CalendarElementType elementType = CalendarElementType.valueOf(componentName);
                elementType.parse(this, myLines);
                
            // parse calendar properties (ignores unknown properties)
            } else
            {
                CalendarElementType elementType = CalendarElementType.enumFromName(propertyName);
                if (elementType != null)
                {
                    elementType.parse(this, Arrays.asList(line));
                }
            }
        }
    }

    public static VCalendar parse(String contentLines)
    {
        VCalendar c = new VCalendar();
        c.parseContent(contentLines);
        return c;
    }
}
