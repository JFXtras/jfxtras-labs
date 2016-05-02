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

import javafx.collections.ObservableList;
import jfxtras.labs.icalendarfx.properties.PropertyEnum;
import jfxtras.labs.icalendarfx.properties.component.misc.IANAProperty;
import jfxtras.labs.icalendarfx.properties.component.misc.NonStandardProperty;
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
public abstract class VComponentBase<T> implements VComponentNew<T>
{
    /**
     * 3.8.8.2.  Non-Standard Properties
     * Any property name with a "X-" prefix
     * 
     * Example:
     * X-ABC-MMSUBJ;VALUE=URI;FMTTYPE=audio/basic:http://www.example.
     *  org/mysubj.au
     */
    @Override
    public ObservableList<NonStandardProperty> getNonStandardProperties() { return nonStandardProps; }
    private ObservableList<NonStandardProperty> nonStandardProps;
    @Override
    public void setNonStandardProperties(ObservableList<NonStandardProperty> nonStandardProps) { this.nonStandardProps = nonStandardProps; }
    /** add comma separated nonStandardProps into separate nonStandardProps objects */
    
    /**
     * 3.8.8.1.  IANA Properties
     * An IANA-registered property name
     * 
     * Examples:
     * NON-SMOKING;VALUE=BOOLEAN:TRUE
     * DRESSCODE:CASUAL
     */
    @Override
    public ObservableList<IANAProperty> getIANAProperties() { return ianaProps; }
    private ObservableList<IANAProperty> ianaProps;
    @Override
    public void setIANAProperties(ObservableList<IANAProperty> ianaProps) { this.ianaProps = ianaProps; }
    
    /**
     * List of all properties found in component.
     * The list is unmodifiable.
     * 
     * @return - the list of properties
     */
    @Override
    public List<PropertyEnum> propertyEnums()
    {
        List<PropertyEnum> populatedProperties = new ArrayList<>();
        System.out.println("componentType():" + componentType().allowedProperties());
        Iterator<PropertyEnum> i = componentType().allowedProperties().stream().iterator();
        while (i.hasNext())
        {
            PropertyEnum propertyType = i.next();
            Object property = propertyType.getProperty(this);
//            System.out.println("props:" + propertyType + " " + property );
            if (property != null)
            {
                populatedProperties.add(propertyType);
            }
        }
      return Collections.unmodifiableList(populatedProperties);
    }

    /** 
     * Property sort order map.  Key is property, value is the order.  The map is automatically
     * populated when parsing the content lines to preserve the existing property order.
     * 
     * When producing the content lines, if a property is not present in the map, it is put at
     * the end of the sorted ones in the order appearing in {@link #PropertyEnum} (should be
     * alphabetical) Generally, this map shouldn't be modified.  Only modify it when you want
     * to force a specific property order (e.g. unit testing).
     */
    public Map<String, Integer> propertySortOrder() { return propertySortOrder; }
    final private Map<String, Integer> propertySortOrder = new HashMap<>();
//    public Map<Property<?>, Integer> propertySortOrder() { return propertySortOrder; }
//    final private Map<Property<?>, Integer> propertySortOrder = new WeakHashMap<>();
    
    /*
     * CONSTRUCTORS
     */
    VComponentBase() { }
    
    /** Parse content lines into calendar component */
    @Deprecated // use static parse
    VComponentBase(String contentLines)
    {
        parseContent(contentLines);
    }
    
    /** Copy constructor */
    public VComponentBase(VComponentBase<T> source)
    {
        this();
        copyComponentFrom(source);
    }

    /** Parse content lines into calendar component */
    public void parseContent(String contentLines)
    {
        parseContent(Arrays.asList(contentLines.split(System.lineSeparator())));
    }
    
    /** Parse content lines into calendar component */
    // TODO - MAYBE I WANT TO COMBINE THIS WITH ABOVE METHOD? - THIS MAY NOT BE USED
    public void parseContent(List<String> contentLines)
    {
        Integer propertyCounter = 0;
//        Iterator<String> i = unfoldLines(contentLines).iterator();
        // TODO - WHERE SHOULD UNFOLDING OCCUR? - HERE?
//        for (String line : contentLines)
        for (int index=0; index<contentLines.size(); index++)
//        while (i.hasNext())
        {
            String line = contentLines.get(index);
//            List<Integer> indices = new ArrayList<>();
//            indices.add(line.indexOf(':'));
//            indices.add(line.indexOf(';'));
//            int nameEndIndex = indices
//                  .stream()
//                  .filter(v -> v > 0)
//                  .min(Comparator.naturalOrder())
//                  .get();
            int nameEndIndex = ICalendarUtilities.getPropertyNameIndex(line);
            String propertyName = line.substring(0, nameEndIndex);
            
            // Parse subcomponent
            if (propertyName.equals("BEGIN"))
            {
                boolean isMainComponent = line.substring(nameEndIndex+1).equals(componentType().toString());
                if  (! isMainComponent)
                {
                    VComponentEnum subcomponentType = VComponentEnum.valueOf(line.substring(nameEndIndex+1));
                    StringBuilder subcomponentContentBuilder = new StringBuilder(200);
                    subcomponentContentBuilder.append(line + System.lineSeparator());
                    boolean isEndFound = false;
                    do
                    {
                        String subLine = contentLines.get(++index);
//                        String subLine = i.next();
                        subcomponentContentBuilder.append(subLine + System.lineSeparator());
                        isEndFound = subLine.subSequence(0, 3).equals("END");
                    } while (! isEndFound);
                    parseSubComponents(subcomponentType, subcomponentContentBuilder.toString());
                }
                
            // parse properties
            } else if (! propertyName.equals("END"))
            {
                // parse property
                // ignores unknown properties
                PropertyEnum propertyType = PropertyEnum.enumFromName(propertyName);
                if (propertyType != null)
                {
                    propertySortOrder.put(propertyName, propertyCounter);
                    propertyCounter += 100; // add 100 to allow insertions in between
                    propertyType.parse(this, line);
                }
            }
        }
    }
    
    /** Copy properties and subcomponents from source into this component,
     * essentially making a copy of source */
    public void copyComponentFrom(VComponentBase<?> source)
    {
        source.propertyEnums().forEach(p -> p.copyProperty(source, this));
        propertySortOrder().putAll(source.propertySortOrder());
    }
    
    /**
     * Parse any subcomponents such as {@link #VAlarm}, {@link #StandardTime} and {@link #DaylightSavingTime}
     * @param subcomponentType 
     * @param string 
     */
    void parseSubComponents(VComponentEnum subcomponentType, String subcomponentcontentLines) { }

    @Override
    public int hashCode()
    {
        int hash = 7;
        Iterator<PropertyEnum> i = propertyEnums().iterator();
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
        VComponentBase<?> testObj = (VComponentBase<?>) obj;
        
        final boolean propertiesEquals;
        List<PropertyEnum> properties = propertyEnums(); // make properties local to avoid creating list multiple times
        List<PropertyEnum> testProperties = testObj.propertyEnums(); // make properties local to avoid creating list multiple times
//        System.out.println("equals:" + this + " " + testObj);
        if (properties.size() == testProperties.size())
        {
            Iterator<PropertyEnum> i1 = properties.iterator();
            Iterator<PropertyEnum> i2 = testProperties.iterator();
            boolean isFailure = false;
            while (i1.hasNext())
            {
                Object p1 = i1.next().getProperty(this);
                Object p2 = i2.next().getProperty(testObj);
//                System.out.println("p1,p2:" + p1 + " " + p2 + " " + p1.equals(p2));
                if (! p1.equals(p2))
                {
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
    
    @Override
    public String toContentLines()
    {
        StringBuilder builder = new StringBuilder(400);
        builder.append(firstContentLine + System.lineSeparator());
        appendMiddleContentLines(builder);
        builder.append(lastContentLine);
        return builder.toString();
    }

    void appendMiddleContentLines(StringBuilder builder)
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
                        List<CharSequence> list = new ArrayList<>(Arrays.asList(property.toContentLines()));
                        propertyNameContentMap.put(property.getPropertyName(), list);
                    } else
                    { // add properties to existing list for existing entry
                        List<CharSequence> list = propertyNameContentMap.get(property.getPropertyName());
                        list.add(property.toContentLines());
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
                            .forEach(s -> builder.append(foldLine(s) + System.lineSeparator()));
                });
    }
    private final String firstContentLine = "BEGIN:" + componentType().toString();
    private final String lastContentLine = "END:" + componentType().toString();


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
    @Deprecated
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
    @Deprecated
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
    
    final private static Comparator<? super String> DTSTART_FIRST_COMPARATOR = (p1, p2) ->
    {
        int endIndex = PropertyEnum.DATE_TIME_START.toString().length();
        String myString = p1.substring(0, endIndex);
        return (myString.equals(PropertyEnum.DATE_TIME_START.toString())) ? -1 : 1;
    };
}
