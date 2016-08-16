package jfxtras.labs.icalendarfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.components.SimpleVComponentFactory;
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
    
    public static final Logger LOGGER = setupLogger(VCalendar.class.getName());
    private static final String LOG_FILE = "log_DATE.txt";
//    private static final ByteArrayOutputStream OUT = new ByteArrayOutputStream();
//    private static final SimpleFormatter FMT = new SimpleFormatter();
//    private static final StreamHandler HANDLER = new StreamHandler(OUT, FMT);
    private static Logger setupLogger(String name)
    {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(name);

        // suppress the logging output to the console
        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();

        if (handlers[0] instanceof ConsoleHandler) {
            handlers[0].setLevel(Level.SEVERE);
        }
        
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd.HH:mm");
        String now = dateFormat.format(LocalDateTime.now());
        String f = LOG_FILE.replace("DATE", now);
//        Settings.LOG_FILE.toFile().mkdirs(); // make directory if does not exist
        FileHandler fileTxt;
        try
        {
            fileTxt = new FileHandler(f);
            fileTxt.setFormatter(new LogFormatter());
            logger.addHandler(fileTxt);
        } catch (SecurityException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        logger.setLevel(Level.ALL);
        return logger;
    }
    
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
            calendarScale = new SimpleObjectProperty<CalendarScale>(this, CalendarProperty.CALENDAR_SCALE.toString());
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
            throw new IllegalArgumentException(CalendarProperty.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
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
            throw new IllegalArgumentException(CalendarProperty.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
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
            method = new SimpleObjectProperty<Method>(this, CalendarProperty.METHOD.toString());
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
            throw new IllegalArgumentException(CalendarProperty.METHOD.toString() + " can only occur once in a calendar component");
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
            throw new IllegalArgumentException(CalendarProperty.METHOD.toString() + " can only occur once in a calendar component");
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
            productIdentifier = new SimpleObjectProperty<ProductIdentifier>(this, CalendarProperty.PRODUCT_IDENTIFIER.toString());
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
            throw new IllegalArgumentException(CalendarProperty.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
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
            throw new IllegalArgumentException(CalendarProperty.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
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
            version = new SimpleObjectProperty<Version>(this, CalendarProperty.VERSION.toString());
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
            throw new IllegalArgumentException(CalendarProperty.VERSION.toString() + " can only occur once in a calendar component");
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
            throw new IllegalArgumentException(CalendarProperty.VERSION.toString() + " can only occur once in a calendar component");
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
     * @see VTimeZone
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
            CalendarComponent type = CalendarComponent.enumFromClass(child.getClass());
            if (type != null)
            {
                type.copyChild(child, this);
            } else
            {
                CalendarProperty property = CalendarProperty.enumFromClass(child.getClass());
                if (property != null)
                {
                    property.copyChild(child, this);
                }
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
//        throw new RuntimeException("not implemented");
        copyChildrenFrom(source);    
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
    
    @Override
    public void parseContent(String content)
    {
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        parseContent(unfoldedLines);
    }
    
    /** Parse unfolded content lines into calendar object */
    public void parseContent(Iterator<String> unfoldedLineIterator)
    {
//        Logger parseLogger = setupLogger(getClass().getName());
        
//        SimpleFormatter fmt = new SimpleFormatter();
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        StreamHandler handler = new StreamHandler(out, fmt);
//        parseLogger.addHandler(handler);
//        
//        parseLogger.log(Level.INFO, "allPanes data setup complete");
////        parseLogger.
//
//        handler.flush();
//        String errors = new String(out.toByteArray(), StandardCharsets.UTF_8);
//        System.out.println("errors:" + errors);
//
//        parseLogger.log(Level.INFO, "a2llPanes data setup complete");
//
//        handler.flush();
//         errors = new String(out.toByteArray(), StandardCharsets.UTF_8);
//        System.out.println("errors:" + errors);
        List<String> errors = new ArrayList<>();
        String firstLine = unfoldedLineIterator.next();
        if (! firstLine.equals("BEGIN:VCALENDAR"))
        {
            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
        }
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = unfoldedLine.substring(0, nameEndIndex);
            
            // Parse component
            if (propertyName.equals("BEGIN"))
            {
                String componentName = unfoldedLine.substring(nameEndIndex+1);
                VComponent newComponent = SimpleVComponentFactory.newVComponent(componentName, unfoldedLineIterator, errors);
                addVComponent(newComponent);
            } else
            {
                CalendarProperty elementType = CalendarProperty.enumFromName(propertyName);
                if (elementType != null)
                {
                    elementType.parse(this, unfoldedLine);
                }
            }
        }
        
//        StreamHandler handler = LOGGER.getHandlers()[1];
//        HANDLER.flush();
//        String errors = new String(OUT.toByteArray(), StandardCharsets.UTF_8);
//     System.out.println("errors:" + errors);
//        String errors = new String(out.toByteArray(), StandardCharsets.UTF_8);
        System.out.println("errors:" + errors);
    }

//    // multi threaded
//    /** Parse content lines into calendar object */
//    // TODO - TEST THIS - MAY NOT MAINTAIN ORDER
//    // TODO - FIX THIS - DOESN'T WORK, DOESN'T GET CALENDARY PROPERTIES
//    public void parseContentMulti(Iterator<String> lineIterator)
//    {
//        // Callables to generate components
//        ExecutorService service = Executors.newWorkStealingPool();
////        Map<Integer, Callable<Object>> taskMap = new LinkedHashMap<>();
//        Integer order = 0;
//        List<Callable<Object>> tasks = new ArrayList<>();
//        
//        String firstLine = lineIterator.next();
//        if (! firstLine.equals("BEGIN:VCALENDAR"))
//        {
//            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
//        }
//        while (lineIterator.hasNext())
//        {
//            String line = lineIterator.next();
//            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
//            String propertyName = line.substring(0, nameEndIndex);
//            
//            // Parse component
//            if (propertyName.equals("BEGIN"))
//            {
//                String componentName = line.substring(nameEndIndex+1);
//                List<String> myLines = new ArrayList<>(20);
//                myLines.add(line);
//                final String endLine = "END:" + componentName;
//                while (lineIterator.hasNext())
//                {
//                    String myLine = lineIterator.next();
//                    myLines.add(myLine);
//                    if (myLine.equals(endLine))
//                    {
//                        Integer myOrder = order;
//                        order += 100;
//                        Runnable vComponentRunnable = () -> 
//                        {
//                            CalendarComponent elementType = CalendarComponent.valueOf(componentName);
//                            VElement component = elementType.parse(this, myLines);
////                            orderer().elementSortOrderMap().put((VChild) component, myOrder);
//                        };
////                        taskMap.put(order, Executors.callable(vComponentRunnable));
//                        tasks.add(Executors.callable(vComponentRunnable));
//                        break;
//                    }
//                }
//                
//            // parse calendar properties (ignores unknown properties)
//            } else
//            {
//                CalendarComponent elementType = CalendarComponent.enumFromName(propertyName);
//                if (elementType != null)
//                {
//                    VElement property = elementType.parse(this, Arrays.asList(line));
////                    orderer().elementSortOrderMap().put((VChild) property, order);
////                    order += 100;
//                }
//            }
//        }
//        
//        try
//        {
////            List<Callable<Object>> tasks = taskMap.entrySet().stream().map(e -> e.getValue()).collect(Collectors.toList());
//            service.invokeAll(tasks);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//    }
    
    @Override
    public String toString()
    {
        return super.toString() + " " + toContent();
    }
    
    public static VCalendar parseICalendarFile(Path icsFilePath) throws IOException
    {
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> lines = br.lines().collect(Collectors.toList());
//        System.out.println("original lines:" + lines.size());
//        lines.stream().forEach(System.out::println);
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(lines).iterator();
//        System.out.println("unfolded lines:" + ICalendarUtilities.unfoldLines(lines).size());

//        ICalendarUtilities.unfoldLines(lines).stream().forEach(System.out::println);
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(unfoldedLines);
        System.out.println("length:" + vCalendar.toContent().length());
        return vCalendar;
    }

//    public static VCalendar parseICalendarFile(Iterator<String> lineIterator)
//    {
//        VCalendar vCalendar = new VCalendar();
//        ExecutorService service = Executors.newSingleThreadExecutor();
//        List<Callable<Object>> tasks = new ArrayList<>();
//        try
//        {
////            BufferedReader br = Files.newBufferedReader(icsFilePath);
////            Iterator<String> lineIterator = br.lines().iterator();
//            while (lineIterator.hasNext())
//            {
//                String line = ICalendarUtilities.unfoldLines(lineIterator);
////                String line = lineIterator.next();
//                Pair<String, String> p = ICalendarUtilities.parsePropertyLine(line); // TODO - REPLACE WITH PROPERTY NAME GET
//                String propertyName = p.getKey();
//                Arrays.stream(VCalendarComponent.values())
//                        .forEach(property -> 
//                        {
//                            boolean matchOneLineProperty = propertyName.equals(property.toString());
//                            if (matchOneLineProperty)
//                            {
//                                property.parseAndSetProperty(vCalendar, p.getValue());
//                            } else if (line.equals(property.startDelimiter()))
//                            {// multi-line property
//                                StringBuilder propertyValue = new StringBuilder(line + System.lineSeparator());
//                                boolean matchEnd = false;
//                                do
//                                {
//                                    String propertyLine = lineIterator.next();
//                                    matchEnd = propertyLine.equals(property.endDelimiter());
//                                    propertyValue.append(propertyLine + System.lineSeparator());
//                                } while (! matchEnd);
//                                Runnable multiLinePropertyRunnable = () -> property.parseAndSetProperty(vCalendar, propertyValue.toString());
//                                tasks.add(Executors.callable(multiLinePropertyRunnable));
//                            } // otherwise, unknown property should be ignored
//                        });
//            }
//                service.invokeAll(tasks);
//        } catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }
//        return vCalendar;
//    }

    public static VCalendar parse(String contentLines)
    {
        VCalendar c = new VCalendar();
        c.parseContent(contentLines);
        return c;
    }
    
    /**
     * LogFormatter to format log entries
     * From traceback only logs entries that contain ninjawise class.  Other lines are omitted.
     * If a null exception was passed then only the message is logged (no traceback exists).
     * 
     * @author David Bal
     *
     */
    final static class LogFormatter extends Formatter
    {
        private static final String LINE_SEPARATOR = System.getProperty("line.separator");

        public LogFormatter() {}
        
        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();

//            sb.append(new Date(record.getMillis()))
//                .append(" ")
//                .append(record.getLevel().getLocalizedName())
//                .append(": ")
//                .append(formatMessage(record))
//                .append(LINE_SEPARATOR);

            DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            sb.append(record.getThreadID())
                .append("::")
                .append(record.getLevel().getLocalizedName())
                .append("::")
                .append(record.getSourceClassName())
                .append("::")
                .append(record.getSourceMethodName())
                .append("::")
                .append(f.format(LocalDateTime.now()))
                .append("::")
                .append(record.getMessage());

            if (record.getThrown() != null) {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    record.getThrown().printStackTrace(pw);
                    pw.close();
                    if (record.getLevel() == Level.SEVERE) {
                        sb.append(LINE_SEPARATOR)
                            .append("\t")
                            .append(sw.toString());
                    } else {
                        String[] lines = sw.toString().split(LINE_SEPARATOR);
                        for (int i=0; i<lines.length; i++) {    // output all lines with class name
                            String classString = VCalendar.class.toString();
                            int beginIndex = classString.indexOf("com");
                            int endIndex = classString.indexOf(".Main");
                            String projectString = classString.substring(beginIndex, endIndex);
                            if (lines[i].contains(projectString)) {
                                sb.append(LINE_SEPARATOR)
                                  .append(lines[i]);
                            } else if (i==0) {
                                sb.append(LINE_SEPARATOR)
                                  .append("\t")
                                  .append(lines[i]);
                            }
//                            else
//                                break;
                        }
                    }
                } catch (Exception ex) {
                    // ignore
                }
            }
            sb.append(LINE_SEPARATOR);
            return sb.toString();
        }
        
    }
}
