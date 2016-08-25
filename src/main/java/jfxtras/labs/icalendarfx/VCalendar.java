package jfxtras.labs.icalendarfx;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import jfxtras.labs.icalendarfx.components.SimpleVComponentFactory;
import jfxtras.labs.icalendarfx.components.VComponent;
import jfxtras.labs.icalendarfx.components.VComponentDisplayableBase;
import jfxtras.labs.icalendarfx.components.VComponentPersonalBase;
import jfxtras.labs.icalendarfx.components.VEvent;
import jfxtras.labs.icalendarfx.components.VFreeBusy;
import jfxtras.labs.icalendarfx.components.VJournal;
import jfxtras.labs.icalendarfx.components.VTimeZone;
import jfxtras.labs.icalendarfx.components.VTodo;
import jfxtras.labs.icalendarfx.content.MultiLineContent;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.properties.calendar.CalendarScale;
import jfxtras.labs.icalendarfx.properties.calendar.Method;
import jfxtras.labs.icalendarfx.properties.calendar.ProductIdentifier;
import jfxtras.labs.icalendarfx.properties.calendar.Version;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.RequestStatus;
import jfxtras.labs.icalendarfx.utilities.DateTimeUtilities;
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

    /**
     * <p>Defines a non-standard property that begins with a "X-" prefix<br>
     * 3.8.8.2.  Non-Standard Properties</p>
     * 
     * <p>Example:<br>
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.<br>
     *  org/mysubj.au
     * </p>
     */
    public ObservableList<NonStandardProperty> getNonStandardProperties() { return nonStandardProps; }
    private ObservableList<NonStandardProperty> nonStandardProps;
    public void setNonStandardProperties(ObservableList<NonStandardProperty> nonStandardProps)
    {
        if (nonStandardProps != null)
        {
            orderer().registerSortOrderProperty(nonStandardProps);
        } else
        {
            orderer().unregisterSortOrderProperty(this.nonStandardProps);
        }
        this.nonStandardProps = nonStandardProps;
    }
    public VCalendar withNonStandardProperty(String...nonStandardProps)
    {
        Arrays.stream(nonStandardProps).forEach(c -> PropertyType.NON_STANDARD.parse(this, c));
        return this;
    }
    public VCalendar withNonStandardProperty(ObservableList<NonStandardProperty> nonStandardProps) { setNonStandardProperties(nonStandardProps); return this; }
    public VCalendar withNonStandardProperty(NonStandardProperty...nonStandardProps)
    {
        if (getNonStandardProperties() == null)
        {
            setNonStandardProperties(FXCollections.observableArrayList(nonStandardProps));
        } else
        {
            getNonStandardProperties().addAll(nonStandardProps);
        }
        return this;
    }

    /**
     * <p>Defines an IANA-registered property<br>
     * 3.8.8.1.  IANA Properties</p>
     * 
     * <p>Examples:<br>
     * <ul>
     * <li>NON-SMOKING;VALUE=BOOLEAN:TRUE
     * <li>DRESSCODE:CASUAL
     * </ul>
     */
    public ObservableList<IANAProperty> getIANAProperties() { return ianaProps; }
    private ObservableList<IANAProperty> ianaProps;
    public void setIANAProperties(ObservableList<IANAProperty> ianaProps)
    {
        if (ianaProps != null)
        {
            orderer().registerSortOrderProperty(ianaProps);
        } else
        {
            orderer().unregisterSortOrderProperty(this.ianaProps);
        }
        this.ianaProps = ianaProps;
    }
    public VCalendar withIANAProperty(String...ianaProps)
    {
        Arrays.stream(ianaProps).forEach(c -> PropertyType.IANA_PROPERTY.parse(this, c));
        return this;
    }
    public VCalendar withIANAProperty(ObservableList<IANAProperty> ianaProps) { setIANAProperties(ianaProps); return this; }
    public VCalendar withIANAProperty(IANAProperty...ianaProps)
    {
        if (getIANAProperties() == null)
        {
            setIANAProperties(FXCollections.observableArrayList(ianaProps));
        } else
        {
            getIANAProperties().addAll(ianaProps);
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
     * @return  true if add was successful, false otherwise
     */
    public boolean addVComponent(VComponent newVComponent)
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
        return true;
    }
    
    /** Add a collection of {@link VComponent} to the correct ObservableList based on
    * its type, such as VEVENT, VTODO, etc.
     * 
     * @param newVComponents  collection of {@link VComponent} to add
     * @return  true if add was successful, false otherwise
     */
    public boolean addAllVComponents(Collection<VComponent> newVComponents)
    {
        return newVComponents.stream().map(v -> addVComponent(v)).allMatch(b -> true);
    }
    
    /** Add a varargs of {@link VComponent} to the correct ObservableList based on
    * its type, such as VEVENT, VTODO, etc.
     * 
     * @param newVComponents  collection of {@link VComponent} to add
     * @return  true if add was successful, false otherwise
     */
    public boolean addAllVComponents(VComponent... newVComponents)
    {
        return addAllVComponents(Arrays.asList(newVComponents));
    }

    
    /** Create a VComponent by parsing component text and add it to the appropriate list 
     * @see #addVComponent(VComponent)*/
    public void addVComponent(String contentText)
    {
        VComponent vComponent = SimpleVComponentFactory.emptyVComponent(contentText);
        vComponent.parseContent(contentText);
        addVComponent(vComponent);
    }
    
    /**
     * Parse component text to new VComponent with {@link RequestStatus REQUEST-STATUS} properties containing 
     * the result of the process, such as success message or error report.
     * 
     * @param contentText  iCalendar content lines
     * @return  the created VComponent with {@link RequestStatus REQUEST-STATUS} populated to indicate success or failuer.
     */
    public VComponent importVComponent(String contentText)
    {
        VComponentPersonalBase<?> vComponent = (VComponentPersonalBase<?>) SimpleVComponentFactory.emptyVComponent(contentText);
        List<String> contentLines = Arrays.asList(contentText.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        boolean useRequestStatus = true;
        vComponent.parseContent(unfoldedLines, useRequestStatus);
//        requestStatusErrors.stream().forEach(System.out::println);
        // TODO - only check conflict if opaque
        String conflict = (vComponent instanceof VEvent) ? DateTimeUtilities.checkScheduleConflict((VEvent) vComponent, getVEvents()) : null;
        if (conflict != null)
        {
            final ObservableList<RequestStatus> requestStatus;
            if (vComponent.getRequestStatus() == null)
            {
                requestStatus = FXCollections.observableArrayList();
                vComponent.setRequestStatus(requestStatus);
            } else
            {
                requestStatus = vComponent.getRequestStatus();
            }
            // remove success REQUEST-STATUS message, if present
            Iterator<RequestStatus> rsIterator = requestStatus.iterator();
            while (rsIterator.hasNext())
            {
                RequestStatus rs = rsIterator.next();
                if (rs.getValue().substring(0, 3).equals("2.0"))
                {
                    rsIterator.remove();
                }
            }
            requestStatus.add(RequestStatus.parse("4.1;Event conflict with " + conflict));
        }
        
        final boolean isVComponentValidToAdd;
        if (vComponent.getRequestStatus() == null)
        {
            isVComponentValidToAdd = true;
        } else
        {
            isVComponentValidToAdd = ! vComponent.getRequestStatus().stream()
                    .map(s -> s.getValue())
                    .anyMatch(s -> (s.charAt(0) == '3') || (s.charAt(0) == '4')); // error codes start with 3 or 4
        }
        
        if (isVComponentValidToAdd)
        {
            addVComponent(vComponent);
        }
        return vComponent;
    }
    
    // TODO - NEED TO GO IN DATE TIME UTILITIES
    /*
     * need list of recurrences and duration from vComponent
     * make list of recurrences and duration for all other opaque VEvents
     * check if ANY recurrences are in between any others
     */
//    private final static int CONFLICT_CHECK_QUANTITY = 100;
//    private boolean checkScheduleConflict(VEvent vComponent)
//    {
//        // must be opaque to cause conflict, opaque is default
//        TimeTransparencyType newTransparency = (vComponent.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : vComponent.getTimeTransparency().getValue();
//        if (newTransparency == TimeTransparencyType.TRANSPARENT)
//        {
//            return false;
//        }
//        
//        LocalDate dtstart = LocalDate.from(vComponent.getDateTimeStart().getValue());
//        TemporalAmount duration = vComponent.getActualDuration();
//        
//        // Make list of Pairs containing start and end temporals
//        List<Pair<Temporal,Temporal>> eventTimes = new ArrayList<>();
//        for (VEvent v : getVEvents())
//        {
//            // can only conflict with opaque events, opaque is default
//            TimeTransparencyType myTransparency = (v.getTimeTransparency() == null) ? TimeTransparencyType.OPAQUE : v.getTimeTransparency().getValue();
//            if (myTransparency == TimeTransparencyType.OPAQUE)
//            {
//                Temporal myDTStart = v.getDateTimeStart().getValue().with(dtstart);
//                TemporalAmount actualDuration = v.getActualDuration();
//                v.streamRecurrences(myDTStart)
//                        .limit(CONFLICT_CHECK_QUANTITY)
//                        .forEach(t -> eventTimes.add(new Pair<>(t, t.plus(actualDuration))));                
//            }
//        }
//        
//        /* Check for conflicts:
//         * Start and End must NOT:
//         *  Be after an event start
//         *  Be before same event end
//         *  Is new start before the existing end
//         *  Is new start after the existing start
//         */
//        return vComponent.streamRecurrences()
//                .limit(CONFLICT_CHECK_QUANTITY)
//                .anyMatch(newStart ->
//                {
//                    Temporal newEnd = newStart.plus(duration);
//                    return eventTimes.stream().anyMatch(p ->
//                    {
//                        Temporal existingStart = p.getKey();
//                        Temporal existingEnd = p.getValue();
//                        // test start
//                        boolean isAfter = DateTimeUtilities.isAfter(newStart, existingStart);
//                        if (isAfter)
//                        {
//                            return DateTimeUtilities.isBefore(newStart, existingEnd);
//                        }
//                        // test end
//                        boolean isAfter2 = DateTimeUtilities.isAfter(newEnd, existingStart);
//                        if (isAfter2)
//                        {
//                            return DateTimeUtilities.isBefore(newEnd, existingEnd);
//                        }
//                        return false;
//                    });
//                });
//    }
    
    /**
     * Import new VComponent with {@link RequestStatus REQUEST-STATUS} properties containing 
     * the result of the process, such as success message or error report.
     * 
     * @param contentText  iCalendar content lines
     * @return  list of error messages if import failed, null if successful
     */
    public List<String> importVComponent(VComponent newVComponent)
    {
        throw new RuntimeException("not implemented");
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
    private Map<String, List<VComponentDisplayableBase<?>>> uidComponentsMap = new HashMap<>(); // public for testing
    public Map<String, List<VComponentDisplayableBase<?>>> uidComponentsMap() { return Collections.unmodifiableMap(uidComponentsMap); }
    
    /**
     * RecurrenceID listener
     * notifies parents when a child component with recurrenceID is created or removed
     * also maintains {@link #uidToComponentMap}
     */
    private ListChangeListener<VComponentDisplayableBase<?>> displayableListChangeListener = (ListChangeListener.Change<? extends VComponentDisplayableBase<?>> change) ->
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
                        final List<VComponentDisplayableBase<?>> relatedComponents;
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
                        List<VComponentDisplayableBase<?>> relatedComponents = uidComponentsMap.get(uid);
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
        copyChildrenFrom(source);    
    }

    /*
     * OTHER METHODS
     */
    
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
    public String toString()
    {
        return super.toString() + " " + toContent();
    }
    
    @Override
    public void parseContent(String content)
    {
        List<String> contentLines = Arrays.asList(content.split(System.lineSeparator()));
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(contentLines).iterator();
        parseContent(unfoldedLines);
    }
    
//    /** Parse folded content lines into calendar object */
//    public void parseContent(List<String> unfoldedLineIterator)
//    {
////        List<String> errors = new ArrayList<>();
//        String firstLine = unfoldedLineIterator.next();
//        if (! firstLine.equals("BEGIN:VCALENDAR"))
//        {
//            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
//        }
//        while (unfoldedLineIterator.hasNext())
//        {
//            String unfoldedLine = unfoldedLineIterator.next();
//            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
//            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
//            
//            // Parse component
//            if (propertyName.equals("BEGIN"))
//            {
//                String componentName = unfoldedLine.substring(nameEndIndex+1);
//                VComponent newComponent = SimpleVComponentFactory.newVComponent(componentName, unfoldedLineIterator);
//                addVComponent(newComponent);
//            } else
//            {
//                CalendarProperty elementType = CalendarProperty.enumFromName(propertyName);
//                if (elementType != null)
//                {
//                    elementType.parse(this, unfoldedLine);
//                }
//            }
//        }
//    }

    /** Parse unfolded content lines into calendar object */
    public void parseContent(Iterator<String> unfoldedLineIterator)
    {
        boolean useResourceStatus = false;
        parseContent(unfoldedLineIterator, useResourceStatus);
    }

    
    /** Parse unfolded content lines into calendar object */
    public void parseContent(Iterator<String> unfoldedLineIterator, boolean useResourceStatus)
    {
//        List<String> errors = new ArrayList<>();
        String firstLine = unfoldedLineIterator.next();
        if (! firstLine.equals("BEGIN:VCALENDAR"))
        {
            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
        }
        while (unfoldedLineIterator.hasNext())
        {
            String unfoldedLine = unfoldedLineIterator.next();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(unfoldedLine);
            String propertyName = (nameEndIndex > 0) ? unfoldedLine.substring(0, nameEndIndex) : "";
            
            // Parse component
            if (propertyName.equals("BEGIN"))
            {
                String componentName = unfoldedLine.substring(nameEndIndex+1);
//                VComponent newComponent = SimpleVComponentFactory.newVComponent(componentName, unfoldedLineIterator);
                VComponent newComponent = SimpleVComponentFactory.emptyVComponent(componentName);
                newComponent.parseContent(unfoldedLineIterator, useResourceStatus);
                addVComponent(newComponent);
            } else
            { // parse calendar property
                CalendarProperty elementType = CalendarProperty.enumFromName(propertyName);
                if (elementType != null)
                {
                    elementType.parse(this, unfoldedLine);
                } else if (unfoldedLine.contains(":"))
                {
                    //non-standard
                    boolean isNonStandard = propertyName.substring(0, PropertyType.NON_STANDARD.toString().length()).equals(PropertyType.NON_STANDARD.toString());
                    boolean isIANA = IANAProperty.isIANAProperty(propertyName);
                    if (isNonStandard)
                    {
                        CalendarProperty.NON_STANDARD.parse(this, unfoldedLine);
                    } else if (isIANA)
                    {
                        CalendarProperty.IANA_PROPERTY.parse(this, unfoldedLine);
                    }
                } // else ignore unknown line
            }
        }
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
    
    /**
     * Creates a new VCalendar from an ics file
     * 
     * @param icsFilePath  path of ics file to parse
     * @return  Created VCalendar
     * @throws IOException
     */
    public static VCalendar parseICalendarFile(Path icsFilePath) throws IOException
    {
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> lines = br.lines().collect(Collectors.toList());
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(lines).iterator();
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(unfoldedLines);
        return vCalendar;
    }
    
    /**
     * Creates a new VCalendar from an ics file
     * 
     * @param icsFilePath  path of ics file to parse
     * @return  Created VCalendar
     * @throws IOException
     */
    public static VCalendar parseICalendarFile(Path icsFilePath, boolean useResourceStatus) throws IOException
    {
        BufferedReader br = Files.newBufferedReader(icsFilePath);
        List<String> lines = br.lines().collect(Collectors.toList());
        Iterator<String> unfoldedLines = ICalendarUtilities.unfoldLines(lines).iterator();
        VCalendar vCalendar = new VCalendar();
        vCalendar.parseContent(unfoldedLines, useResourceStatus);
        return vCalendar;
    }

    public static VCalendar parse(String contentLines)
    {
        VCalendar c = new VCalendar();
        c.parseContent(contentLines);
        return c;
    }
}
