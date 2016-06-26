package jfxtras.labs.icalendarfx.components;

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
import java.util.stream.Collectors;

import jfxtras.labs.icalendarfx.OrderedElement;
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
public abstract class VComponentBase extends OrderedElement implements VComponent
{   
    /**
     * List of all {@link PropertyType} found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */
    @Override
    public List<PropertyType> propertyEnums()
    {
        List<PropertyType> populatedProperties = componentType().allowedProperties().stream()
                .filter(p -> p.getProperty(this) != null)
                .collect(Collectors.toList());
      return Collections.unmodifiableList(populatedProperties);
    }

    /** 
     * SORT ORDER
     * 
     * Property sort order map.  Key is property name, value is the sort order.  The map is automatically
     * populated when parsing the content lines to preserve the existing property order.
     * 
     * When producing the content lines, if a property is not present in the map, it is put at
     * the end of the sorted ones in the order appearing in {@link #PropertyEnum} (should be
     * alphabetical) Generally, this map shouldn't be modified.  Only modify it when you want
     * to force a specific property order (e.g. unit testing).
     */
    @Override
    @Deprecated
    public Map<String, Integer> propertySortOrder() { return propertySortOrder; }
    final private Map<String, Integer> propertySortOrder = new HashMap<>();
    private volatile Integer sortOrderCounter = 0;
    
    /*
     * CONSTRUCTORS
     */
    VComponentBase()
    {
        addListeners();
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
        copyComponentFrom(source);
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
                boolean isMainComponent = line.substring(nameEndIndex+1).equals(componentType().toString());
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
        builder.append(firstContentLine + System.lineSeparator());
//        sortedContent().stream().forEach(System.out::println);
        String content = sortedContent().stream()
//                .map(s -> ICalendarUtilities.foldLine(s))
                .collect(Collectors.joining(System.lineSeparator()));
        if (content != null)
        {
            builder.append(content + System.lineSeparator());
        }
        builder.append(lastContentLine);
        return builder.toString();
    }
    
    @Override
    public String toString()
    {
        return super.toString() + System.lineSeparator() + toContent();
    }
    
    @Override
    public int hashCode()
    {
        int hash = 7;
        Iterator<PropertyType> i = propertyEnums().iterator();
        while (i.hasNext())
        {
            Object property = i.next().getProperty(this);
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
        List<PropertyType> properties = propertyEnums(); // make properties local to avoid creating list multiple times
        List<PropertyType> testProperties = testObj.propertyEnums(); // make properties local to avoid creating list multiple times
//        System.out.println("equals:" + this + " " + testObj);
        if (properties.size() == testProperties.size())
        {
            Iterator<PropertyType> i1 = properties.iterator();
            Iterator<PropertyType> i2 = testProperties.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next().getProperty(this);
                Object p2 = i2.next().getProperty(testObj);
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
    
    @Deprecated
    void appendMiddleContentLines2(StringBuilder builder)
    {
        /* map of property name/content
         * 
         * Note: key is property name and not Property reference because I want all properties
         * with same name, even non-standard and IANA properties, to have same sort order.  The non-
         * standard properties with different names should be sorted together, not lumped with all
         * non-standard properties.
         */
        
        Map<String, List<CharSequence>> propertyNameContentMap = new LinkedHashMap<>();
        properties().forEach(property -> 
                {
                    if (propertyNameContentMap.get(property.getPropertyName()) == null)
                    { // make new list for new entry
                        List<CharSequence> list = new ArrayList<>(Arrays.asList(property.toContent()));
                        propertyNameContentMap.put(property.getPropertyName(), list);
                    } else
                    { // add properties to existing list for existing entry
                        List<CharSequence> list = propertyNameContentMap.get(property.getPropertyName());
                        list.add(property.toContent());
                    }
                });
        
        // restore property sort order if properties were parsed from content
        propertyNameContentMap.entrySet().stream()
                .sorted((Comparator<? super Entry<String, List<CharSequence>>>) (e1, e2) -> 
                {
                    Integer s1 = propertySortOrder().get(e1.getKey());
                    Integer sort1 = (s1 == null) ? Integer.MAX_VALUE : s1;
                    Integer s2 = propertySortOrder().get(e2.getKey());
                    Integer sort2 = (s2 == null) ? Integer.MAX_VALUE : s2;
                    return sort1.compareTo(sort2);
                })
                .forEach(p -> 
                {
                    p.getValue().stream()
                            .forEach(s -> builder.append(ICalendarUtilities.foldLine(s) + System.lineSeparator()));
                });
    }
    private final String firstContentLine = "BEGIN:" + componentType().toString();
    private final String lastContentLine = "END:" + componentType().toString();


    @Deprecated // may not use - delete if that is the case
    final private static Comparator<? super String> DTSTART_FIRST_COMPARATOR = (p1, p2) ->
    {
        int endIndex = PropertyType.DATE_TIME_START.toString().length();
        String myString = p1.substring(0, endIndex);
        return (myString.equals(PropertyType.DATE_TIME_START.toString())) ? -1 : 1;
    };
}
