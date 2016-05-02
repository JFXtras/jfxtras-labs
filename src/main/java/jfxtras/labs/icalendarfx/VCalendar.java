package jfxtras.labs.icalendarfx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import jfxtras.labs.icalendarfx.components.VCalendarElement;
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
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

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
    public static final String DEFAULT_PRODUCT_IDENTIFIER = ("-//JFxtras//iCalendarFx " + myVersion + "//EN");
//    public static final ProductIdentifier DEFAULT_PRODUCT_IDENTIFIER = ProductIdentifier.parse("-////JFxtras////iCalendarFx " + myVersion + "////EN");
//    public static final Version DEFAULT_ICALENDAR_SPECIFICATION_VERSION = Version.parse("2.0");
    public static final String DEFAULT_ICALENDAR_SPECIFICATION_VERSION = ("2.0");
    
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
            throw new IllegalArgumentException(PropertyEnum.CALENDAR_SCALE.toString() + " can only occur once in a calendar component");
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
            throw new IllegalArgumentException(PropertyEnum.METHOD.toString() + " can only occur once in a calendar component");
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
            productIdentifier = new SimpleObjectProperty<ProductIdentifier>(this, PropertyEnum.PRODUCT_IDENTIFIER.toString());
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
            throw new IllegalArgumentException(PropertyEnum.PRODUCT_IDENTIFIER.toString() + " can only occur once in a calendar component");
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
            version = new SimpleObjectProperty<Version>(this, PropertyEnum.VERSION.toString());
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
            throw new IllegalArgumentException(PropertyEnum.VERSION.toString() + " can only occur once in a calendar component");
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
    public Map<VCalendarElement, Long> componentSortOrder() { return componentSortOrder; }
    final private Map<VCalendarElement, Long> componentSortOrder = new HashMap<>();
    
    
    /** Parse content lines into calendar object */
    public String toContentLines()
    {
        List<VCalendarElement> elements = new ArrayList<VCalendarElement>();
        // Add calendar properties
        // Order is PRODID, VERSION, CALSCALE and METHOD unless componentSortOrder specifies other values
        if (getProductIdentifier() != null)
        {
            elements.add(getProductIdentifier());
        }
        if (getVersion() != null)
        {
            elements.add(getVersion());
        }
        if (getCalendarScale() != null)
        {
            elements.add(getCalendarScale());
        }
        if (getMethod() != null)
        {
            elements.add(getMethod());
        }
        elements.addAll((Collection<? extends VCalendarElement>) components());

        StringBuilder builder = new StringBuilder(elements.size()*300);
        builder.append(firstContentLine + System.lineSeparator());

        Map<VCalendarElement, CharSequence> elementContentMap = new LinkedHashMap<>();
        elements.forEach(element -> elementContentMap.put(element, element.toContentLines()));
        
        // restore component sort order if components were parsed from content
        elementContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<VCalendarElement, CharSequence>>) (e1, e2) -> 
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
    private Long finalSortOrder(Long initialSort, VCalendarElement element)
    {
        final Long s1Final;
        if (initialSort == null)
        {
            if (element instanceof VComponentPersonal)
            { // sort by DTSTAMP
                DateTimeStamp dateTimeStamp = ((VComponentPersonal<?>) element).getDateTimeStamp();
                s1Final = (dateTimeStamp != null) ? dateTimeStamp.getValue().toInstant().toEpochMilli() : 0L; // shouldn't even be 0, DTSTAMP is REQUIRED
            } else
            {
                s1Final = 0L; // no DTSTAMP sort order value is zero, keep natural order of elements
            }
        } else
        {
            /* make sorted values use negative values to ensure following order
             * 1 - Elements with sort order
             * 2 - Elements with no DTSTAMP (i.e. VTimeZone and calendar properties including PRODID, VERSION, CALSCALE and PUBLISH)
             * 3 - Elements with DTSTAMP, in chronological order
             */
            s1Final = initialSort - Long.MIN_VALUE;
        }
        return s1Final;
    }
    private final String firstContentLine = "BEGIN:VCALENDAR";
    private final String lastContentLine = "END:VCALENDAR";

    /** Parse content lines into calendar object */
    public VCalendar parseContent(String content)
    {
        Long componentCounter = 0L;
        List<String> contentLines = unfoldLines(content);
//        Iterator<String> i = unfoldLines(contentLines).iterator();
//        while (i.hasNext())
        if (! contentLines.get(0).equals("BEGIN:VCALENDAR"))
        {
            throw new IllegalArgumentException("Content lines must begin with BEGIN:VCALENDAR");
        }
        for (int index=1; index<contentLines.size(); index++)
        {
//            String line = i.next();
            String line = contentLines.get(index);
//            List<Integer> indices = new ArrayList<>();
//            indices.add(line.indexOf(':'));
//            indices.add(line.indexOf(';'));
//            int nameEndIndex = indices
//                  .stream()
//                  .filter(v -> v > 0)
//                  .min(Comparator.naturalOrder())
//                  .get();
//            String propertyName = line.substring(0, nameEndIndex);
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
                    System.out.println("line:" + line + " " + endLine + " " + index);
                    myLines.add(line);
                } while (! line.equals(endLine));
                
                VComponentEnum vComponentType = VComponentEnum.valueOf(componentName);
                vComponentType.parse(this, myLines);
                componentSortOrder().put(vComponentType.getElement(), componentCounter);
                componentCounter += 100;
                
            // parse calendar properties
            } //else if (! propertyName.equals("END"))
            {
                // parse property
                // ignores unknown properties
                System.out.println("property:" + propertyName);
                PropertyEnum propertyType = PropertyEnum.enumFromName(propertyName);
//                if (propertyType != null)
//                {
//                    propertySortOrder.put(propertyName, componentCounter);
//                    componentCounter =+ 100; // add 100 to allow insertions in between
//                    propertyType.parse(this, line);
//                }
            }
        }
        
        return null;
        // TODO Auto-generated method stub
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calendarScale == null) ? 0 : calendarScale.hashCode());
        result = prime * result + ((method == null) ? 0 : method.hashCode());
        result = prime * result + ((productIdentifier == null) ? 0 : productIdentifier.hashCode());
        result = prime * result + ((vEvents == null) ? 0 : vEvents.hashCode());
        result = prime * result + ((vFreeBusys == null) ? 0 : vFreeBusys.hashCode());
        result = prime * result + ((vJournals == null) ? 0 : vJournals.hashCode());
        result = prime * result + ((vTimeZones == null) ? 0 : vTimeZones.hashCode());
        result = prime * result + ((vTodos == null) ? 0 : vTodos.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VCalendar other = (VCalendar) obj;
        if (calendarScale == null)
        {
            if (other.calendarScale != null)
                return false;
        } else if (!calendarScale.equals(other.calendarScale))
            return false;
        if (method == null)
        {
            if (other.method != null)
                return false;
        } else if (!method.equals(other.method))
            return false;
        if (productIdentifier == null)
        {
            if (other.productIdentifier != null)
                return false;
        } else if (!productIdentifier.equals(other.productIdentifier))
            return false;
        if (vEvents == null)
        {
            if (other.vEvents != null)
                return false;
        } else if (!vEvents.equals(other.vEvents))
            return false;
        if (vFreeBusys == null)
        {
            if (other.vFreeBusys != null)
                return false;
        } else if (!vFreeBusys.equals(other.vFreeBusys))
            return false;
        if (vJournals == null)
        {
            if (other.vJournals != null)
                return false;
        } else if (!vJournals.equals(other.vJournals))
            return false;
        if (vTimeZones == null)
        {
            if (other.vTimeZones != null)
                return false;
        } else if (!vTimeZones.equals(other.vTimeZones))
            return false;
        if (vTodos == null)
        {
            if (other.vTodos != null)
                return false;
        } else if (!vTodos.equals(other.vTodos))
            return false;
        return true;
    }
    
    /**
     * Starting with lines-separated list of content lines, the lines are unwrapped and 
     * converted into a list of property name and property value list wrapped in a Pair.
     * DTSTART property is put on top, if present.
     * 
     * This method unwraps multi-line content lines as defined in iCalendar RFC5545 3.1, page 9
     * 
     * For example:
     * string is
     * BEGIN:VEVENT
     * SUMMARY:test1
     * DTSTART;TZID=Etc/GMT:20160306T080000Z
     * DTEND;TZID=Etc/GMT:20160306T093000Z
     * RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     * ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yaho
     *  o.com
     * UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     * DTSTAMP:20160313T053147Z
     * END:VEVENT
     *  
     *  results in list
     *  Element 1, BEGIN:VEVENT
     *  Element 2, SUMMARY:test1
     *  Element 3, DTSTART;TZID=Etc/GMT:20160306T080000Z
     *  Element 4, DTEND;TZID=Etc/GMT:20160306T093000Z
     *  Element 5, RRULE:FREQ=DAILY;UNTIL=20160417T235959Z;INTERVAL=1
     *  Element 5, ORGANIZER;CN=David Bal;SENT-BY="mailto:ddbal1@yahoo.com":mailto:ddbal1@yahoo.com
     *  Element 6, UID:fc3577e0-8155-4fa2-a085-a15bdc50a5b4
     *  Element 7, DTSTAMP:20160313T053147Z
     *  Element 8, END:VEVENT
     *  
     *  Note: the list of Pair<String,String> is used instead of a Map<String,String> because some properties
     *  can appear more than once resulting in duplicate key values.
     *  
     * @param componentString
     * @return
     */
    // TODO - MOVE THIS TO A UTILITY CLASS
//    final private static char[] SPECIAL_CHARACTERS = new char[] {',' , ';' , '\\' , 'n', 'N' };
//    final private static char[] REPLACEMENT_CHARACTERS = new char[] {',' , ';' , '\\' , '\n', 'n'};
    private static List<String> unfoldLines(String componentString)
    {
        List<String> propertyLines = new ArrayList<>();
        String storedLine = "";
        Iterator<String> lineIterator = Arrays.stream( componentString.split(System.lineSeparator()) ).iterator();
        while (lineIterator.hasNext())
        {
            // unwrap lines by storing previous line, adding to it if next is a continuation
            // when no more continuation lines are found loop back and start with last storedLine
            String startLine;
            if (storedLine.isEmpty())
            {
                startLine = lineIterator.next();
            } else
            {
                startLine = storedLine;
                storedLine = "";
            }
            StringBuilder builder = new StringBuilder(startLine);
            while (lineIterator.hasNext())
            {
                String anotherLine = lineIterator.next();
                if ((anotherLine.charAt(0) == ' ') || (anotherLine.charAt(0) == '\t'))
                { // unwrap anotherLine into line
                    builder.append(anotherLine.substring(1, anotherLine.length()));
                } else
                {
                    storedLine = anotherLine; // save for next iteration
                    break;  // no continuation line, exit while loop
                }
            }
            String line = builder.toString();
            propertyLines.add(line);
        }
//        Collections.sort(propertyLines, DTSTART_FIRST_COMPARATOR); // put DTSTART property on top of list (so I can get its Temporal type)
        return propertyLines;
    }
    
    /**
     * Folds lines at character 75 into multiple lines.  Follows rules in
     * RFC 5545, 3.1 Content Lines, page 9.
     * A space is added to the first character of the subsequent lines.
     * doesn't break lines at escape characters
     * 
     * @param line - content line
     * @return - folded content line
     */
    private static CharSequence foldLine(CharSequence line)
    {
        // first position is 0
        final int maxLineLength = 75;
        if (line.length() <= maxLineLength)
        {
            return line;
        } else
        {
            StringBuilder builder = new StringBuilder(line.length()+20);
            int leadingSpaceAdjustment = 0;
            String leadingSpace = "";
            int startIndex = 0;
            while (startIndex < line.length())
            {
                int endIndex = Math.min(startIndex+maxLineLength-leadingSpaceAdjustment, line.length());
                if (endIndex < line.length())
                {
                    // ensure escaped characters are not broken up
                    if (line.charAt(endIndex-1) == '\\')
                    {
                        endIndex = endIndex-1; 
                    }
                    builder.append(leadingSpace + line.subSequence(startIndex, endIndex) + System.lineSeparator());
                } else
                {                    
                    builder.append(leadingSpace + line.subSequence(startIndex, endIndex));
                }
                startIndex = endIndex;
                leadingSpaceAdjustment = 1;
                leadingSpace = " ";
            }
            return builder;
        }
    }
    public static VCalendar parse(String contentLines)
    {
        VCalendar c = new VCalendar();
        c.parseContent(contentLines);
        return c;
    }
}
