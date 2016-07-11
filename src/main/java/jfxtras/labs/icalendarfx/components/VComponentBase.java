package jfxtras.labs.icalendarfx.components;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javafx.util.Callback;
import jfxtras.labs.icalendarfx.CalendarElementType;
import jfxtras.labs.icalendarfx.VChild;
import jfxtras.labs.icalendarfx.VParent;
import jfxtras.labs.icalendarfx.VParentBase;
import jfxtras.labs.icalendarfx.properties.Property;
import jfxtras.labs.icalendarfx.properties.PropertyType;
import jfxtras.labs.icalendarfx.utilities.ICalendarUtilities;

/**
 * iCalendar component
 * Contains the following properties:
 * Non-Standard Properties, IANA Properties
 * 
 * Each property contains the following methods:
 * Getter for iCalendar property
 * Getter for JavaFX property
 * Setter for iCalendar property
 * Setter for iCalendar's properties' encapsulated object (omitted if object is a String - Strings are parsed)
 * Setter for iCalendar String content (parsed into new iCalendar object)
 * Also contains chaining "with" methods matching the two or three setters.
 * 
 * @author David Bal
 *
 * @param <T> - implementation class
 * @see VEventNewInt
 * @see VTodoInt
 * @see VJournalInt
 * @see VFreeBusy
 * @see VTimeZone
 * @see VAlarmInt
 */
public abstract class VComponentBase extends VParentBase implements VComponent
{
    VParent myParent;
    @Override public void setParent(VParent parent) { myParent = parent; }
    @Override public VParent getParent() { return myParent; }
    
    @Override
    protected Callback<VChild, Void> copyChildCallback()
    {        
        return (child) ->
        {
            PropertyType type = PropertyType.enumFromClass(child.getClass());
            if (type != null)
            { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
                type.copyProperty((Property<?>) child, this);
            }
            return null;
        };
    }
    
//    @Override
//    public String componentName()
//    {
//        return CalendarElementType.enumFromClass(this.getClass()).toString();
//    }
    
    final private String componentName;
    @Override
    public String componentName() { return componentName; }

//    @Override
//    public CalendarElementType componentType() { return componentType; }
    
//    private void checkContentList()
//    {
//        List<String> elementNames = propertyEnums().stream().map(e -> e.toString()).collect(Collectors.toList());
//        Optional<VCalendarElement> propertyNotFound = orderer().elementSortOrderMap().entrySet()
//            .stream()
//            .map(e -> e.getKey())
//            .filter(v ->
//            {
//    //            PropertyType myType = PropertyType.enumFromClass(v.getClass());
//                String myElementName = PropertyType.enumFromClass(v.getClass()).toString();
//                return (myElementName == null) ? false : ! elementNames.contains(myElementName);
//            })
//            .findAny();
//        if (propertyNotFound.isPresent())
//        {
//            throw new RuntimeException("element not found:" + propertyNotFound.get());
//        }
//    }
    
    // Ensures all elements in elementSortOrderMap are found in propertyEnums list
//    @Deprecated // doesn't work - some propertyEnum produces lists sometimes. elementSortOrderMap only produces individual properties
//    private void checkContentList()
//    {
////        List<String> elementNames1 = propertyEnums().stream().map(e -> e.toString()).collect(Collectors.toList());
//        List<Object> elementNames1 = propertyEnums().stream()
//                .map(e -> e.getProperty(this))
////                .peek(a -> System.out.println("ee1:" + a + " " ))
//                .collect(Collectors.toList());
////        System.out.println("elements2:" + orderer().elementSortOrderMap().size());
////        if (orderer().elementSortOrderMap().size() == 9) System.out.println("is 9");
////        orderer().elementSortOrderMap().entrySet().forEach(System.out::println);
////        System.exit(0);
//
//        List<Object> elementNames2 = orderer().elementSortOrderMap().entrySet()
//                .stream()
//                .filter(a -> ! (a.getKey() instanceof VComponent))
////                .peek(a -> System.out.println("ee2:" + a + " " + (a.getKey() instanceof VComponent)))
//                .map(e -> e.getKey())
//                .collect(Collectors.toList());
////        System.out.println("list done:");
////        elementNames1.stream().forEach(System.out::println);
////        elementNames2.stream().forEach(System.out::println);
//        Optional<Object> propertyNotFound1 = elementNames1.stream().filter(s -> ! elementNames2.contains(s)).findAny();
//        if (propertyNotFound1.isPresent())
//        {
//            throw new RuntimeException("element not found:" + propertyNotFound1.get());
//        }
//        Optional<Object> propertyNotFound2 = elementNames2.stream().filter(s -> ! elementNames1.contains(s)).findAny();
//        if (propertyNotFound2.isPresent())
//        {
//            throw new RuntimeException("element not found:" + propertyNotFound2.get());
//        }
//    }
//    private Callback<Void, List<String>> elementNameListCallback;
//    private Callback<VCalendarElement, String> elementNameCallback;
    
//    /**
//     * List of all {@link PropertyType} found in component.
//     * The list is unmodifiable.
//     * 
//     * @return - the list of properties
//     * @deprecated  not needed due to addition of Orderer, may be deleted
//     */
//    @Deprecated
//    public List<PropertyType> propertyEnums()
//    {
//        List<PropertyType> populatedProperties = componentType().allowedProperties().stream()
//                .filter(p -> p.getProperty(this) != null)
//                .collect(Collectors.toList());
//      return Collections.unmodifiableList(populatedProperties);
//    }

//    /** 
//     * SORT ORDER
//     * 
//     * Property sort order map.  Key is property name, value is the sort order.  The map is automatically
//     * populated when parsing the content lines to preserve the existing property order.
//     * 
//     * When producing the content lines, if a property is not present in the map, it is put at
//     * the end of the sorted ones in the order appearing in {@link #PropertyEnum} (should be
//     * alphabetical) Generally, this map shouldn't be modified.  Only modify it when you want
//     * to force a specific property order (e.g. unit testing).
//     */
//    @Override
//    @Deprecated
//    public Map<String, Integer> propertySortOrder() { return propertySortOrder; }
//    final private Map<String, Integer> propertySortOrder = new HashMap<>();
//    private volatile Integer sortOrderCounter = 0;
    
    /*
     * CONSTRUCTORS
     */
    VComponentBase()
    {
        addListeners();
        
//        orderer = new OrdererBase(copyPropertyChildCallback);
        componentName = CalendarElementType.enumFromClass(this.getClass()).toString();
//        System.out.println("componentType:" + componentType);
    }
    
    /** Parse content lines into calendar component */
    VComponentBase(String contentLines)
    {
        this();
        parseContent(contentLines);
    }
    
    /** Copy constructor */
    public VComponentBase(VComponentBase source)
    {
        this();
        copyChildrenFrom(source);
    }

    void addListeners()
    {
        // functionality added in subclasses
    }
    
    /** Parse content lines into calendar component */
    @Override
    public void parseContent(String contentLines)
    {
        parseContent(ICalendarUtilities.unfoldLines(contentLines));
    }
    
    /** Parse component from list of unfolded lines */
    public void parseContent(List<String> contentLines)
    {
        for (int index=0; index<contentLines.size(); index++)
        {
            String line = contentLines.get(index);
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
            String propertyName = line.substring(0, nameEndIndex);
            
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = line.substring(nameEndIndex+1).equals(componentName());
                if  (! isMainComponent)
                {
                    CalendarElementType subcomponentType = CalendarElementType.enumFromName(line.substring(nameEndIndex+1));
                    StringBuilder subcomponentContentBuilder = new StringBuilder(200);
                    subcomponentContentBuilder.append(line + System.lineSeparator());
                    boolean isEndFound = false;
                    do
                    {
                        String subLine = contentLines.get(++index);
                        subcomponentContentBuilder.append(subLine + System.lineSeparator());
                        isEndFound = subLine.subSequence(0, 3).equals("END");
                    } while (! isEndFound);
                    parseSubComponents(subcomponentType, subcomponentContentBuilder.toString());
                }
                
            // parse properties - ignore unknown properties
            } else if (! propertyName.equals("END"))
            {
                PropertyType propertyType = PropertyType.enumFromName(propertyName);
                if (propertyType != null)
                {
//                    propertySortOrder.put(propertyName, sortOrderCounter);
//                    sortOrderCounter += 100; // add 100 to allow insertions in between
                    propertyType.parse(this, line);
                } else
                {
                    // TODO - check IANA properties and X- properties
                    System.out.println("unknown prop:" );
                    throw new RuntimeException("not implemented");
                }
            }
        }
    }
    
    /** Copy properties and subcomponents from source into this component,
     * essentially making a copy of source 
     * 
     * Note: this method only overwrites properties found in source.  If there are properties in
     * this component that are not present in source then those will remain unchanged.
     * */
 //    @Override
//    public void copyComponentFrom(VComponent source)
//    {
////        System.out.println("copyComponentFrom1");
////        source.propertyEnums().forEach(p -> p.copyProperty(source, this));
////        propertySortOrder().putAll(source.propertySortOrder());
//        source.elementSortOrderMap().forEach((key, value) ->
//        {
////            value.copyToParent(this);
//            
//            PropertyType type = PropertyType.enumFromClass(key.getClass());
////            System.out.println("type:" + type + " " + elementSortOrderMap().size());
//            if (type != null)
//            { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
////                // TODO - FIX problem with LISTS - when property is a list copyProperty copies whole list - not just one element.
//////                type.copyProperty(source, this);
//                type.copyProperty((Property<?>) key, this);
//////                ((Property<?>) value).copyToParent(this);
//////                copyToParent(this);
//            }
////            value.copyToNewParent(this);
//        });
//    }
    
//    @Override
//    protected List<String> sortedContent()
//    {
//        // check properties to make sure all are accounted for in map
//        List<PropertyType> types = propertyEnums();
//        Optional<VCalendarElement> propertyNotFound = orderer().elementSortOrderMap().entrySet()
//            .stream()
//            .map(e -> e.getKey())
//            .filter(v ->
//            {
//                PropertyType myType = PropertyType.enumFromClass(v.getClass());
//                return (myType == null) ? false : types.contains(myType);
//            })
//            .findAny();
//        if (propertyNotFound.isPresent())
//        {
//            throw new RuntimeException("property not found:" + propertyNotFound.get());
//        }
//        return super.sortedContent();
//    }
//    
//    /** Copy property into this component */
//    @Override protected void copyChild(VCalendarElement child)
//    {
//        PropertyType type = PropertyType.enumFromClass(child.getClass());
//        if (type != null)
//        { // Note: if type is null then element is a subcomponent such as a VALARM, STANDARD or DAYLIGHT and copying happens in subclasses
//            type.copyProperty((Property<?>) child, this);
//        }        
//    }

    /**
     * Parse any subcomponents such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * @param subcomponentType 
     * @param string 
     */
    void parseSubComponents(CalendarElementType subcomponentType, String subcomponentcontentLines) { }
    
    @Override
    public String toContent()
    {
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine() + System.lineSeparator());
//        sortedContent().stream().forEach(System.out::println);
//        System.out.println("elements1:" + orderer().elementSortOrderMap().size() + " " + this.hashCode());
//        checkContentList(); // test elements for completeness (can be removed for improved speed)
//        System.exit(0);
        String content = childrenUnmodifiable().stream()
                .map(c -> c.toContent())
                .collect(Collectors.joining(System.lineSeparator()));
        if (content != null)
        {
            builder.append(content + System.lineSeparator());
        }
        builder.append(lastContentLine());
        return builder.toString();
    }
    private String firstContentLine() { return "BEGIN:" + componentName(); }
    private String lastContentLine() { return "END:" + componentName(); }
    
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        Iterator<?> i = childrenUnmodifiable().iterator();
        while (i.hasNext())
        {
            Object property = i.next();
            hash = (31 * hash) + property.hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if((obj == null) || (obj.getClass() != getClass())) {
            return false;
        }
        VComponentBase testObj = (VComponentBase) obj;
        
        final boolean propertiesEquals;
     // TODO - FIX EQUALS TO USE MAP
        Collection<?> properties = childrenUnmodifiable(); // make properties local to avoid creating list multiple times
        Collection<?> testProperties = testObj.childrenUnmodifiable(); // make properties local to avoid creating list multiple times
//        System.out.println("equals:" + this + " " + testObj);
        if (properties.size() == testProperties.size())
        {
            Iterator<?> i1 = properties.iterator();
            Iterator<?> i2 = testProperties.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next();
                Object p2 = i2.next();
                if (! p1.equals(p2))
                {
//                    System.out.println("p1,p2:" + p1 + " " + p2 + " " + p1.equals(p2));
                    isFailure = true;
                    break;
                }
            }
            propertiesEquals = ! isFailure;
        } else
        {
            propertiesEquals = false;
        }
        return propertiesEquals;
    }

//    @Override
//    public List<String> errors()
//    {
//        List<String> errors = new ArrayList<>();
//        
//        return errors;
//    }
//    public void appendMiddleContentLines(StringBuilder builder)
//    {
//        /* map of property name/content
//         * 
//         * Note: key is property name and not Property reference because I want all properties
//         * with same name, even non-standard and IANA properties, to have same sort order.  The non-
//         * standard properties with different names should be sorted together, not lumped with all
//         * non-standard properties.
//         */
//        
//        Map<VCalendarElement, CharSequence> elementContentMap = new LinkedHashMap<>();
//        List<VCalendarElement> elements = new ArrayList<VCalendarElement>();
//        elements.forEach(element -> elementContentMap.put(element, element.toContent()));
//        
//        // restore component sort order if components were parsed from content
//        elementContentMap.entrySet().stream()
//                .sorted((Comparator<? super Entry<VCalendarElement, CharSequence>>) (e1, e2) -> 
//                {
//                    Integer s1 = elementSortOrder().get(e1.getKey());
//                    Integer s2 = elementSortOrder().get(e2.getKey());
//                    s1 = (s1 == null) ? 0 : s1;
//                    s2 = (s2 == null) ? 0 : s2;
//                    return s1.compareTo(s2);
//                })
//                .forEach(p -> 
//                {
//                    builder.append(p.getValue() + System.lineSeparator());
//                });
//        
//        builder.append(lastContentLine);
//    }
    
//    @Deprecated
//    void appendMiddleContentLines2(StringBuilder builder)
//    {
//        /* map of property name/content
//         * 
//         * Note: key is property name and not Property reference because I want all properties
//         * with same name, even non-standard and IANA properties, to have same sort order.  The non-
//         * standard properties with different names should be sorted together, not lumped with all
//         * non-standard properties.
//         */
//        
//        Map<String, List<CharSequence>> propertyNameContentMap = new LinkedHashMap<>();
//        properties().forEach(property -> 
//                {
//                    if (propertyNameContentMap.get(property.getPropertyName()) == null)
//                    { // make new list for new entry
//                        List<CharSequence> list = new ArrayList<>(Arrays.asList(property.toContent()));
//                        propertyNameContentMap.put(property.getPropertyName(), list);
//                    } else
//                    { // add properties to existing list for existing entry
//                        List<CharSequence> list = propertyNameContentMap.get(property.getPropertyName());
//                        list.add(property.toContent());
//                    }
//                });
//        
//        // restore property sort order if properties were parsed from content
//        propertyNameContentMap.entrySet().stream()
//                .sorted((Comparator<? super Entry<String, List<CharSequence>>>) (e1, e2) -> 
//                {
//                    Integer s1 = propertySortOrder().get(e1.getKey());
//                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
//                    Integer s2 = propertySortOrder().get(e2.getKey());
//                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
//                    return sort1.compareTo(sort2);
//                })
//                .forEach(p -> 
//                {
//                    p.getValue().stream()
//                            .forEach(s -> builder.append(ICalendarUtilities.foldLine(s) + System.lineSeparator()));
//                });
//    }



    @Deprecated // may not use - delete if that is the case
    final private static Comparator<? super String> DTSTART_FIRST_COMPARATOR = (p1, p2) ->
    {
        int endIndex = PropertyType.DATE_TIME_START.toString().length();
        String myString = p1.substring(0, endIndex);
        return (myString.equals(PropertyType.DATE_TIME_START.toString())) ? -1 : 1;
    };
}
